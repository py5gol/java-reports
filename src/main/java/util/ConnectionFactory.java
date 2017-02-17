package util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionFactory {

    public static Connection getConnection() {

        String DATASOURCE_CONTEXT = "java:comp/env/jdbc/RelatoriosWEBDB";

        Connection conn = null;

        InitialContext ctx = null;
        try {
            ctx = new InitialContext();
        }
        catch (NamingException e) {
            e.printStackTrace();
        }

        if (ctx == null) {
            System.out.println("JNDI problem. Cannot get InitialContext.");
        }

        DataSource datasource = null;
        try {
            datasource = (DataSource) ctx.lookup(DATASOURCE_CONTEXT);
        }
        catch (NamingException e) {
            e.printStackTrace();
        }

        if (datasource == null) {
            System.out.println("Failed to lookup datasource");
        }

        try {
            conn = datasource.getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;

    }

}