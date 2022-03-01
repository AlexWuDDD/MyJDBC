package com.alex.statement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.alex.util.JDBCUtils;

import org.junit.Test;

/**
 *  @Description 演示使用PreparedStatement解决SQL注入问题
 * 
 * 
 * 除了解决Statement的拼串， sql注入问题之外，PreparedStatement还有哪些好处
 * 1. PreparedStatement操作Blob的数据， 而Statement做不到
 * 2. PreparedStatement可以实现更高效的批量操作
 */

public class PreparedStatementTest {

    @Test
	public void testLogin() {

        int i = 2;

        String userData1 = "AA";
        String passwordData1 = "123456";

        String userData2 = "1' OR";
        String passwordData2 = "=1 OR '1' = '1";

        String user;
        String password;

        switch(i){
        case 1:
            user = userData1;
            password = passwordData1;
            break;
        case 2: //SQL注入
            user = userData2;
            password = passwordData2;
            break;
        /**
         * SELECT user, password 
             FROM user_table 
            WHERE user = '1' OR' AND password = '=1 OR '1' = '1'
        */
        default:
            user = "";
            password = "";
            break;
        }

        String sql = "SELECT user, password FROM user_table WHERE user = ? AND password = ?";
        User returnUser = getInstance(User.class, sql, user, password);
        if(returnUser != null){
            System.out.println("登录成功");
        }
        else{
            System.out.println("用户名不存在或密码错误");
        }
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
}
