package com.alex.utilWithPool;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

public class JDBCUtils {
  
  //使用CPDS
  //数据库连接池只需提供一个
  private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
  
  /**
   * @Description: 使用C3P0的数据库连接池技术
   * @return
   * @throws Exception
   */
  public static Connection getConnection() throws Exception{
    Connection conn = cpds.getConnection();
    return conn;
  }

  //使用DBCP
  private static DataSource source;
  static{
    try{
      InputStream is = ClassLoader.getSystemClassLoader().getResource("dbcp.properties").openStream();
      // is = new FileInputStream(new File("src/dbcp.properties"));
      Properties pros = new Properties();
      pros.load(is);
      //创建了DBCP的数据路连接池
      source = BasicDataSourceFactory.createDataSource(pros);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public static Connection getConnection2() throws Exception{
    Connection conn = source.getConnection();
    return conn;
  }

  //使用Druid
  private static DataSource source1;
  static {
    try{
      Properties pros = new Properties();

      InputStream is = ClassLoader.getSystemClassLoader().getResource("druid.properties").openStream();
  
      pros.load(is);
      source1 =  DruidDataSourceFactory.createDataSource(pros);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }
  public static Connection getConnection3() throws Exception{
    Connection conn = source1.getConnection();
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

  public static void closeResource(Connection conn, Statement ps, ResultSet rs){
      
      try{
          if(rs != null){
              rs.close();
          }
      }
      catch(Exception e){
          e.printStackTrace();
      }

      
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

  //使用DBUtils提供的工具，实现资源的关闭
  public static void closeResource1(Connection conn, Statement ps, ResultSet rs){
    DbUtils.closeQuietly(conn);
    DbUtils.closeQuietly(ps);
    DbUtils.closeQuietly(rs);
  }
}
