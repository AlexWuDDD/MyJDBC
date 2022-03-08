package com.alex.transaction;

import java.sql.Connection;
import com.alex.util.JDBCUtils;
import org.junit.Test;

public class ConnectionTest {

    @Test
    public void testGetConnection() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }
}
