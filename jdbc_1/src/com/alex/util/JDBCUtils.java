package com.alex.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
  * 
  * @Description:操作数据的工具类
  * @author Alex
  * @date 2022-03-01 06:49:00
  */
public class JDBCUtils {

    /**
     * @Description:获取数据库的连接
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception{
        //1.读取配置文件中的4个基本信息
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
        Connection conn = DriverManager.getConnection(url, user, password);

        return conn;
    }

    /**
     * @Description:关闭数据库的连接
     * @param conn
     * @param ps
     */

    public static void closeResource(Connection conn, Statement ps){
        try {
            if(ps != null){
                ps.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            if(conn != null){
                conn.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
  
}
