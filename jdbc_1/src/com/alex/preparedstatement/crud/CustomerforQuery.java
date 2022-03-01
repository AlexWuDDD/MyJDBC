package com.alex.preparedstatement.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.alex.bean.Customer;
import com.alex.util.JDBCUtils;


import org.junit.Test;

/**
 * @Description:针对Customers表的查询操作
 * @author: Alex
 * @date: 2022-03-01
 */


public class CustomerforQuery {

    @Test
    public void testQueryForCustomers(){
        String sql = "select id,name,email, birth from customers where id = ?";

        Customer customer = queryForCustomer(sql, 1);
        if(customer != null){
            System.out.println(customer);
        }

        sql = "select name, email from customers where name = ?";
        customer = queryForCustomer(sql, "哪吒");
        if(customer != null){
            System.out.println(customer);
        }
    
    }

    /**
     * @Description， 针对于customers表的通用的查询操作
     * @author alex
     * @date: 2022-03-01
     */

    public Customer queryForCustomer(String sql, Object ...args){
        
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
                Customer customer = new Customer();
                //处理一行数据中的每一个列
                for(int i = 0;i < columnCount; ++i){
                    Object columnValue = rs.getObject(i+1);
                    //获取每个列的列名
                    // String columnName = rsmd.getColumnName(i+1);
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    //给customer对象指定的某个属性赋值，通过反射
                    Field field = customer.getClass().getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(customer, columnValue);
                }
                return customer;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            JDBCUtils.closeResource(conn, ps, rs);
        }

        return null;
    }


    
    @Test
    public void testQuery1(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = JDBCUtils.getConnection();   
            String sql = "select id, name, email, birth from customers where id = ?";
            ps = conn.prepareStatement(sql);

            ps.setObject(1, 1);

            //执行，并返回结果集
            rs = ps.executeQuery();
            //处理结果集
            if(rs.next()){ //判断结果集的下一条是否有数据，如果有数据返回true，并指针下移，如果返回false, 执政不下移，返回false
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                Date birth = rs.getDate(4);
                
                //方式一
                //System.out.println("id = " + id + ", name = " + name + ", email = " + email + ", birth = " + birth);
                
                //方式二
                //Object[] data = new Object[]{id, name, email, birth};

                //方式三, 将数据封装为一个对象（推荐）
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }
}
