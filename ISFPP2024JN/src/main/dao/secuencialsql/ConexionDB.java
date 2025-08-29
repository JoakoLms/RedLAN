package main.dao.secuencialsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;

public class ConexionDB {
    public static Connection getConexion() throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("db.properties"));
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }
}