package com.alex.transaction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.alex.util.JDBCUtils;

import org.junit.Test;


/**
 * 1. 什么叫数据库事务
 * 事务：一组逻辑操作单元，使数据从一种状态变换到另一种状态
 *      >一组逻辑单元、一个或多个DML操作
 * 2. 事务处理的原则，保证所有事务都作为一个工作单元来执行
 * 即使出现了故障，都不能改变这种执行方式。当在一个事务中执行
 * 多个操作时，要么所有的事务都被提交（commit),那么这些修改就
 * 永久地保存下来，要么数据库管理系统将放弃所作的所有操作，
 * 整个事务回滚（rollback)到最初状态。
 * 
 * 3. 数据一旦提交，就不可回滚
 * 
 * 4. 哪些操作会导致数据的自动提交
 *      > DDL操作一旦执行，就会自动提交
 *          > set autocommit = false 对DDL操作无效
 *      > DML默认操作情况下，一旦执行，就会自动提交
 *          >我们可以通过set autocommit = false的方式取消DML操作的自动提交
 *      > 默认在关闭连接时，会走动提交数据
 */



public class Transaction {
    

    //未考虑数据库事务的转账操作
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

    //考虑数据库事务的转账操作

    @Test
    public void testUpdateWithTx(){
      Connection conn = null;
      try{
        conn = JDBCUtils.getConnection();

        //取消数据的自动提交功能
        conn.setAutoCommit(false);

        String sql1 = "update user_table set balance = balance - 100 where user = ?";
        update(conn, sql1, "AA");
        String sql2 = "update user_table set balance = balance + 100 where user = ?";
        update(conn, sql2, "BB");

        System.out.println("转账成功");

        //提交数据
        conn.commit();
      }
      catch(Exception e){
        e.printStackTrace();
        //回滚数据
        try{
          conn.rollback();
        }
        catch(Exception e1){
          e1.printStackTrace();
        }
      }
      finally{
        JDBCUtils.closeResource(conn, null);
      }
    }


    //通用的增删改操作 --- version 2.0
    public int update(Connection conn, String sql, Object ...args){
      PreparedStatement ps = null;
      try{
          //1. 预编译sql语句,返回PreparedStatement的实例
          ps = conn.prepareStatement(sql);
          //2. 填充占位符
          for(int i = 0; i < args.length; i++){
              ps.setObject(i+1, args[i]);
          }
          //3. 执行sql语句
          return ps.executeUpdate();
      }
      catch(Exception e){
          e.printStackTrace();
      }
      finally{
          //4.资源的关闭
          JDBCUtils.closeResource(null, ps);
      }

      return 0;
  }

  //********************************* /

  @Test
  public void testTransactionSelect(){
    Connection conn = null;
    try{
      conn = JDBCUtils.getConnection();
      //获取事务的隔离级别
      System.out.println(conn.getTransactionIsolation());
      //设置事务的隔离级别
      conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
      //取消自动提交数据
      conn.setAutoCommit(false);
      String sql = "select user, password, balance from user_table where user = ?";
      User user = getInstance(conn, User.class, sql, "CC");
      System.out.println(user);
    }
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      JDBCUtils.closeResource(conn, null);
    }
  }

  @Test
  public void testTransactionUpdate(){
    Connection conn = null;
    try{
      conn = JDBCUtils.getConnection();

      //取消自动提交数据
      conn.setAutoCommit(false);
      String sql = "update user_table set balance = ? where user = ?";
      update(conn, sql, 5000, "CC");
      Thread.sleep(15000);
      conn.commit();
      System.out.println("修改结束");

    }
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      JDBCUtils.closeResource(conn, null);
    }
    
  }



  //通用的查询操作，用于返回数据表中的一条记录（version2.0)
  public <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object ...args){

    ResultSet rs = null;
    PreparedStatement ps = null;
    try{

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
        JDBCUtils.closeResource(null, ps, rs);
    }

    return null;
}

}
