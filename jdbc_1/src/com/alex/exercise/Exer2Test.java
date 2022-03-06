package com.alex.exercise;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.alex.util.JDBCUtils;

import org.junit.Assert;
import org.junit.Test;

public class Exer2Test {

    //问题3，删除指定的学生信息
    @Test
    public void testDeleteByExamCard(){
        String sql = "select FlowID flowID, Type type, IDCard IDCard, ExamCard examCard, StudentName name, Location location, Grade grade from examstudent where ExamCard = ?";
        String examCard = "200523164754004";   
        
        Student stu = getInstance(Student.class, sql, examCard);
        if(stu != null){
            System.out.println(stu);
            sql = "delete from examstudent where ExamCard = ?";
            int count = updtae(sql, examCard);
            if(count > 0){
                System.out.println("删除成功!");
            }
            else{
                System.out.println("删除失败!");
            }
        }
        else{
            System.out.println("查无此人");
        }
    }

    //优化以后的代码
    @Test
    public void testDeleteByExamCard1(){
        String examCard = "200523164754004";  
        String sql = "delete from examstudent where ExamCard = ?";
        int count = updtae(sql, examCard);
        if(count > 0){
            System.out.println("删除成功!");
        }
        else{
            System.out.println("查无此人!");
        }
    }

    //问题2，根据身份证号或者准考证号查询学生成绩

    @Test
    public void queryWithIDCardOrExamCard(){

        String sql = "select FlowID flowID, Type type, IDCard IDCard, ExamCard examCard, StudentName name, Location location, Grade grade from examstudent where IDCard = ?";
        Student stu = getInstance(Student.class, sql, "854524195263214584");
        Assert.assertNotEquals(null, stu);
        System.out.println(stu);
    }



    public <T> T getInstance(Class<T> clazz, String sql, Object ...args){
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try{
            conn = JDBCUtils.getConnection();

            ps =  conn.prepareStatement(sql);
            for(int i = 0; i < args.length; ++i){
                ps.setObject(i+1, args[i]);
            }

            rs = ps.executeQuery();

            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if(rs.next()){
                T t = clazz.getDeclaredConstructor().newInstance();
                //处理一行数据中的每一个列
                for(int i = 0;i < columnCount; ++i){
                    Object columnValue = rs.getObject(i+1);
                    //获取每个列的列名
                    // String columnName = rsmd.getColumnName(i+1);
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    //给customer对象指定的某个属性赋值，通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            JDBCUtils.closeResource(conn, ps, rs);
        }

        return null;
    }
    
    //问题1，向examsStudents表中插入数据
    @Test
    public void testInsert(){
        String sql = "insert into examsStudent(Typem, IDCard, ExamCard, StudentName, Location, Grade) values(?, ?, ?, ?, ?, ?)";
        int Type = 0;
        String IDCard = "";
        String ExamCard = "";
        String StudentName = "";
        String Location = "";
        int Grade = 0;
        
        int insertCount= updtae(sql, Type, IDCard, ExamCard, StudentName, Location, Grade);
        if(insertCount > 0){
            System.out.println("插入成功");
        }
        else{
            System.out.println("插入失败");
        }
        
    }



    //通用的增删改操作
    //sql中的占位符与可变形参的长度一致
    public int updtae(String sql, Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //1. 获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2. 预编译sql语句,返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //3. 填充占位符
            for(int i = 0; i < args.length; i++){
                ps.setObject(i+1, args[i]);
            }
            //4. 执行sql语句
            /*
             * ps.execute();
             * 如果执行的是查询操作， 有返回结果，则此方法返回true
             * 如果执行的是增删改操作，没有返回结果，则此方法返回false
             */
            //ps.execute();
            return ps.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }
}
