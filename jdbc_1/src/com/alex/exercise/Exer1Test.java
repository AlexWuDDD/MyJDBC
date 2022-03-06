package com.alex.exercise;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.alex.util.JDBCUtils;

import org.junit.Test;

public class Exer1Test {

    @Test
    public void testInsert(){
        String sql = "insert into customers(name, email, birth) values(?, ?, ?)";
        String name = "alex";
        String email = "alex@126.com";
        String birth = "1991-05-13";
        int insertCount= updtae(sql, name, email, birth);
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
