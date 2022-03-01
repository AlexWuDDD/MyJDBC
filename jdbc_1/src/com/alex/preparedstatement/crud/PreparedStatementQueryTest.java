package com.alex.preparedstatement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import com.alex.bean.Customer;
import com.alex.bean.Order;
import com.alex.util.JDBCUtils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Description: 使用PreparedStatement实现针对于不同表的通用的查询操作
 * 
 */

public class PreparedStatementQueryTest {

    @Test
    public void testGetForList(){
        String sql = "select id, name, email, birth from customers where id < ?";
        List<Customer> list = getForList(Customer.class, sql, 5);
        Assert.assertNotEquals(null, list);
        list.forEach(System.out::println);

        String sql1 = "select order_id orderId, order_name orderName, order_date orderDate from `order`";
        List<Order> list1 = getForList(Order.class, sql1);
        Assert.assertNotEquals(null, list1);
        list1.forEach(System.out::println);
    }



    public <T> List<T> getForList(Class<T> clazz, String sql, Object ...args){
        
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
            //创建集合对象
            ArrayList<T> list =  new ArrayList<T>();

            while(rs.next()){
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
                
                list.add(t);
            }
            return list;
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            JDBCUtils.closeResource(conn, ps, rs);
        }

        return null;
    }


    @Test 
    public void testGetInstance(){

        String sql = "select id, name, email, birth from customers where id = ?";

        Customer customer =  getInstance(Customer.class, sql, 1);
    
        Assert.assertNotEquals(null, customer);
        System.out.println(customer);

        String sql1 = "select order_id orderId, order_name orderName, order_date orderDate from `order` where order_id = ?";
        Order order = getInstance(Order.class, sql1, 1);
        Assert.assertNotEquals(null, order);
        System.out.println(order);
    }

    /**
     * @Description:  针对于不同的表的通用的查询操作，返回表中的一条数据
     * @param <T>
     * @param clazz
     * @param sql
     * @param args
     * @return
     */

    
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
