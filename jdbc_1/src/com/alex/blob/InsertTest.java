package com.alex.blob;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.alex.util.JDBCUtils;
import org.junit.Test;

/**
 * 使用PreparedStatements实现批量数据的操作
 * 
 * updtate、delete本身就具有批量操作的效果
 * 此时的批量操作，主要指的批量插入，
 * 使用PreparedStatement如何实现批量操作
 * 
 * 题目：向goods表中插入20000条数据
 * CREATE TABLE goods(
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(25)
    );
 * 方式一：使用Statement
 * Connection conn = JDBCUtil.getConnection();
 * Statement st = conn.createStatement();
 * for(int i = 1; i <= 20000; ++i){
 *  String sql = "insert into goods(name) value('name_" + i + + "')";
 *  st.execute(sql)
 * }
 */

public class InsertTest {
    
    //批量插入的方式2
    @Test
    public void testInsert1(){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps =  conn.prepareStatement(sql);
            for (int i = 0; i< 20000; ++i){
                ps.setObject(1, "name_" + i);
                ps.execute();
            }

            long end = System.currentTimeMillis();
            System.out.println("花费的时间为:" + (end - start));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            JDBCUtils.closeResource(conn, ps);
        }
    }

    //批量插入的方式3
    // 1. addBatch(), executeBatch(), clearBatch()
    // 2. mysql服务器默认是关闭批处理的，我们需要通过一个参数，
    // 让mysql开启批处理的支持
    // ?rewriteBatchedStatements=true，写在配置文件的url后面
    // 3. 
    @Test
    public void testInsert2(){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps =  conn.prepareStatement(sql);
            for (int i = 1; i<= 20000; ++i){
                ps.setObject(1, "name_" + i);

                //1. ’攒‘sql
                ps.addBatch();

                if(i % 500 == 0){
                    //2.执行batch
                    ps.executeBatch();

                    //3. 清空batch
                    ps.clearBatch();
                }
            }

            long end = System.currentTimeMillis();
            System.out.println("花费的时间为:" + (end - start));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            JDBCUtils.closeResource(conn, ps);
        }
    }

    //批量插入的方式4: 设置连接不允许自动提交
    @Test
    public void testInsert3(){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            //设置不允许自动提交数据
            conn.setAutoCommit(false);
            String sql = "insert into goods(name) values(?)";
            ps =  conn.prepareStatement(sql);
            for (int i = 1; i <= 20000; ++i){
                ps.setObject(1, "name_" + i);

                //1. ’攒‘sql
                ps.addBatch();

                if(i % 500 == 0){
                    //2.执行batch
                    ps.executeBatch();

                    //3. 清空batch
                    ps.clearBatch();
                }
            }
            //提交数据
            conn.commit();

            long end = System.currentTimeMillis();
            System.out.println("花费的时间为:" + (end - start));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            JDBCUtils.closeResource(conn, ps);
        }
    }
}
