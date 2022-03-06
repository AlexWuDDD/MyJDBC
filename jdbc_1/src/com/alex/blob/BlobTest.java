package com.alex.blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.alex.bean.Customer;
import com.alex.util.JDBCUtils;

import org.junit.Test;

/**
 * @Description:测试使用PreparedStatement操作Blob类型的数据
 * 
 */


public class BlobTest {
    
    //向数据表customer中插入Blob数据
    @Test
    public void testInsert() throws Exception{
        Connection conn = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1, "张三");
        ps.setObject(2, "张三@qq.com");
        ps.setObject(3, "1992-09-08");

        FileInputStream is = new FileInputStream(new File("/Users/alexwu/Documents/JDBC/jdbc_1/src/img1.jpeg"));

        ps.setBlob(4, is);

        ps.execute();

        is.close();
        JDBCUtils.closeResource(conn, ps);
    }

    //查询customer表中的Blob数据
    @Test
    public void testQuery(){

        Connection conn = null;
        PreparedStatement ps = null;
        InputStream is = null;
        FileOutputStream fos = null;
        ResultSet rs = null;
        try{
            conn = JDBCUtils.getConnection();
            String sql = "select id, name, email, birth, photo from customers where id = ?";

            ps = conn.prepareStatement(sql);
            ps.setObject(1, 21);
            rs = ps.executeQuery();
            if(rs.next()){
            //方式一 
            // int id = rs.getInt(1);
            // String name = rs.getString(2);
            // String email = rs.getString(3);
            // Date birth = rs.getDate(4);
            //方式二
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            Date birth = rs.getDate("birth");
            
            Customer customer = new Customer(id, name, email, birth);
            System.out.println(customer);

            //将Blob的类型的字段下载下来，以文件的形式保存在本地
            Blob photo = rs.getBlob("photo");
            is = photo.getBinaryStream();
            fos = new FileOutputStream("/Users/alexwu/Documents/JDBC/jdbc_1/src/img2.jpeg");
            byte[] buffer = new byte[1024];
            int len; 
            while((len = is.read(buffer)) != -1){
                fos.write(buffer, 0, len);
            }
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(is != null){
                    is.close();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

            try{
                if(fos != null){
                    fos.close();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }
}
