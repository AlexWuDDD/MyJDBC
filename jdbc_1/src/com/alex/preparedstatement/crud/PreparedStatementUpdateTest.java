package com.alex.preparedstatement.crud;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;

import com.alex.util.JDBCUtils;

import org.junit.Test;

/*
 * 使用PreparedStatement来替换Statement,实现对数据的增删改查操作
 * 
 * 增删改，查
 */



public class PreparedStatementUpdateTest {

    @Test
    public void testCommonUpdate2(){
        String sql = "update `order` set order_name = ? where order_id = ?";
        updtae(sql, "DD", 2);
    }

    @Test
    public void testCommonUpdate1(){
        String sql = "delete from customers where id = ?";
        updtae(sql, 3);
    }

    //通用的增删改操作
    //sql中的占位符与可变形参的长度一致
    public void updtae(String sql, Object ...args){
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
            ps.execute();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            JDBCUtils.closeResource(conn, ps);
        }
    }

    //修改customers表的一条记录
    @Test
    public void testUpdate(){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            //1. 获取数据库的连接
            conn = JDBCUtils.getConnection();
            //2. 预编译sql语句,返回PreparedStatement的实例
            ps = conn.prepareStatement("update customers set name = ? where id = ?");
            //3. 填充占位符
            ps.setObject(1, "莫扎特");
            ps.setObject(2, 18);
            //4. 执行sql语句
            ps.execute();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            //5. 关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
    }

  
    //向customersb表中添加一条数据
    @Test
    public void testInsert(){

        Connection conn = null;
        PreparedStatement ps = null;
        try{
            InputStream is = ClassLoader.getSystemClassLoader().getResource("jdbc.properties").openStream();
            
            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            //2.加载驱动
            Class.forName(driverClass);

            //3. 获取连接
            conn = DriverManager.getConnection(url, user, password);
            // System.out.println(conn);

            //4. 预编译sql语句，返回PreparedStatement的实例
            String sql = "insert into customers(name, email, birth) values(?,?,?)"; //?占位符
            ps = conn.prepareStatement(sql);
            //5. 填充占位符, 数据库索引从1开始
            ps.setString(1, "哪吒");
            ps.setString(2, "nezha@gmail.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1000-01-01");
            ps.setDate(3, new Date(date.getTime()));

            //6. 执行sql语句
            ps.execute();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            //7. 释放的关闭
            try {
                if(ps != null){
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try{
                if(conn != null){
                    conn.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            
        }
    }
}
