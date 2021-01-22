package com.java.gds.analytics.db;

import com.java.gds.analytics.constants.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;

@Service
public class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class);

    @Value("${" + Constants.JDBC_URL + "}")
    private String jdbcURL;

    @Value("${" + Constants.DB_USERNAME + "}")
    private String dbUserName;

    @Value("${" + Constants.DB_PASSWORD + "}")
    private String dbPassword;

    private Connection con = null;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            LOGGER.error("Error occurred while loading postgres drive ", e);
        }
    }

    public Connection getConnection() {
        try {
            con = DriverManager.getConnection(jdbcURL, dbUserName, dbPassword);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while opening a connection ", e);
        }
        return con;
    }

    public void close() {
        try {
            if (null != con && !con.isClosed()) {
                con.close();
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while closing connection ", e);
        }
    }

}