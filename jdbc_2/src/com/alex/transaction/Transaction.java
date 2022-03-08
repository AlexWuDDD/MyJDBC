package com.alex.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.alex.util.JDBCUtils;

import org.junit.Test;

public class Transaction {
    

    /**
     * 针对于数据表user_table来说
     * AA用户给BB用户转账100
     * 
     */
    @Test
    public void testUpdate(){
        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        update(sql1, "AA");
        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(sql2, "BB");

        System.out.println("转账成功");
    }

    //通用的增删改操作 --- version 1.0
    //sql中的占位符与可变形参的长度一致
    public int update(String sql, Object ...args){
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
