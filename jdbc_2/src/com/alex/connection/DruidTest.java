package com.alex.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import org.junit.Test;

public class DruidTest {
  
  @Test
  public void test() throws Exception{
    Properties pros = new Properties();

    InputStream is = ClassLoader.getSystemClassLoader().getResource("druid.properties").openStream();

    pros.load(is);
    DataSource source =  DruidDataSourceFactory.createDataSource(pros);
    Connection conn = source.getConnection();
    System.out.println(conn);
  }
}
