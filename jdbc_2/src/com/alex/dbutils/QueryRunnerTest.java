package com.alex.dbutils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.alex.bean.Customer;
import com.alex.utilWithPool.JDBCUtils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

public class QueryRunnerTest {

  //测试插入
  @Test
  public void testInsert(){

    Connection conn = null;
    try{
      QueryRunner runner = new QueryRunner();
      conn = JDBCUtils.getConnection3();
      String sql = "insert into customers(name,email,birth) values(?,?,?)";
      int insertCount = runner.update(conn, sql, "蔡徐坤", "caixukun@126.com", "1990-01-01");
      System.out.println("添加了" + insertCount + "条记录");
    } 
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      JDBCUtils.closeResource(conn, null);
    }
  }

  //测试查询
  /**
   * BeanHandler: 是ResultSetHandler接口的实现类，用于封装表中的一条记录
   */
  @Test
  public void testQuery1(){
    Connection conn = null;
    try{
      QueryRunner runner = new QueryRunner();
      conn = JDBCUtils.getConnection3();
      String sql = "select id, name, email, birth from customers where id = ?";
      BeanHandler<Customer> handler = new BeanHandler<Customer>(Customer.class);
      Customer customer = runner.query(conn, sql, handler, 22);
      System.out.println(customer);
    } 
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      JDBCUtils.closeResource(conn, null);
    }
  }

  /**
   * BeanListHandler: 是ResultSetHandler接口的实现类，用于封装表中的多条记录
   */
  @Test
  public void testQuery2(){
    Connection conn = null;
    try{
      QueryRunner runner = new QueryRunner();
      conn = JDBCUtils.getConnection3();
      String sql = "select id, name, email, birth from customers where id < ?";
      BeanListHandler<Customer> handler = new BeanListHandler<Customer>(Customer.class);
      List<Customer> customers = runner.query(conn, sql, handler, 22);
      customers.forEach(customer->System.out.println(customer));
    } 
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      JDBCUtils.closeResource(conn, null);
    }
  }

  /**
   * MapHandler: 是ResultSetHandler接口的实现类，对应表中的一条记录
   * 将字段及相应字段的值作为map中的key和value
   */
  @Test
  public void testQuery3(){
    Connection conn = null;
    try{
      QueryRunner runner = new QueryRunner();
      conn = JDBCUtils.getConnection3();
      String sql = "select id, name, email, birth from customers where id = ?";
      MapHandler handler = new MapHandler();
      Map<String, Object> map = runner.query(conn, sql, handler, 22);
      System.out.println(map);
    } 
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      JDBCUtils.closeResource(conn, null);
    }
  }  

    /**
   * MapListHandler: 是ResultSetHandler接口的实现类，对应表中的多条记录
   * 将字段及相应字段的值作为map中的key和value, 将这些map添加到List中
   */
  @Test
  public void testQuery4(){
    Connection conn = null;
    try{
      QueryRunner runner = new QueryRunner();
      conn = JDBCUtils.getConnection3();
      String sql = "select id, name, email, birth from customers where id < ?";
      MapListHandler handler = new MapListHandler();
      List<Map<String, Object>> mapList = runner.query(conn, sql, handler, 22);
      mapList.forEach(map->System.out.println(map));
    } 
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      JDBCUtils.closeResource(conn, null);
    }
  }

  /**
   * 用于查询特殊信息
   */

  @Test
  public void testQuery5(){
    Connection conn = null;
    try{
      QueryRunner runner = new QueryRunner();
      conn = JDBCUtils.getConnection3();
      String sql = "select count(*) from customers";
      ScalarHandler<Long> handler = new ScalarHandler<>();
      Long count = runner.query(conn, sql, handler);
      System.out.println(count);
    } 
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      JDBCUtils.closeResource(conn, null);
    }
  }  

  @Test
  public void testQuery6(){
    Connection conn = null;
    try{
      QueryRunner runner = new QueryRunner();
      conn = JDBCUtils.getConnection3();
      String sql = "select max(birth) from customers";
      ScalarHandler<Date> handler = new ScalarHandler<>();
      Date birth = runner.query(conn, sql, handler);
      System.out.println(birth);
    } 
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      JDBCUtils.closeResource(conn, null);
    }
  }  


  /**
   * 自定义ResultSetHandler的实现类
   */
  @Test
  public void testQuery7(){
    Connection conn = null;
    try{
      QueryRunner runner = new QueryRunner();
      conn = JDBCUtils.getConnection3();
      String sql = "select id, name, email, birth from customers where id = ?";
      ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
        @Override
        public Customer handle(ResultSet rs) throws SQLException {
          if(rs.next()){
            Customer customer = new Customer();
            customer.setId(rs.getInt("id"));
            customer.setName(rs.getString("name"));
            customer.setEmail(rs.getString("email"));
            customer.setBirth(rs.getDate("birth"));
            return customer;
          }
          return null;
        }
      };
      Customer cust = runner.query(conn, sql, handler, 23);
      System.out.println(cust);
    } 
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      JDBCUtils.closeResource(conn, null);
    }
  }
}
