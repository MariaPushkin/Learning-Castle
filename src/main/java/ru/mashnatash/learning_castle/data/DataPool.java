package ru.mashnatash.learning_castle.data;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DataPool {

    private static DataPool datapool;
    private ComboPooledDataSource cpds;

    private DataPool() throws IOException, SQLException, PropertyVetoException {
        /*cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
        cpds.setJdbcUrl("jdbc:mysql://localhost/test");
        cpds.setUser("root");
        cpds.setPassword("root");

        // the settings below are optional -- c3p0 can work with defaults
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);*/


        //final Settings settings = Settings.getInstance();
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("org.postgresql.Driver");
        cpds.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/Learning_Castle");
        cpds.setUser("postgres");
        cpds.setPassword("gtxfkm");
    }

    public static DataPool getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datapool == null) {
            datapool = new DataPool();
            return datapool;
        } else {
            return datapool;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }

}
