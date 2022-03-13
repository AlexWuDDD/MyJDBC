package com.alex.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

public class DBCPTest {
 
  //方式一
  @Test
  public void testGetConnection(){

    BasicDataSource source = null;
    Connection conn = null;
    try{
      //创建了DBCP的数据路连接池
      source = new BasicDataSource();
       //设置基本信息
      source.setDriverClassName("com.mysql.cj.jdbc.Driver");
      source.setUrl("jdbc:mysql://localhost:10086/test");
      source.setUsername("root");
      source.setPassword("123456");

      //设置其他涉及数据库管理的属性
      source.setInitialSize(10);
      source.setMaxTotal(10);
      //....

      conn = source.getConnection();
      System.out.println(conn);
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    finally{
      try {
        if(conn != null){
          conn.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

      try {
        if(source!=null){
          source.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }

  //方式二
  @Test
  public void testGetConnection1(){

    BasicDataSource source = null;
    Connection conn = null;
    InputStream is = null;

    try{

      is = ClassLoader.getSystemClassLoader().getResource("dbcp.properties").openStream();
      // is = new FileInputStream(new File("src/dbcp.properties"));
      Properties pros = new Properties();
      pros.load(is);
      //创建了DBCP的数据路连接池
      source = BasicDataSourceFactory.createDataSource(pros);

      conn = source.getConnection();
      System.out.println(conn);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      try {
        if(is != null){
          is.close();
        }
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

      try {
        if(conn != null){
          conn.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

      try {
        if(source!=null){
          source.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }
}
