package ingu.flux.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DBService {
    private static final Logger log = LoggerFactory.getLogger(DBService.class);
    private Connection connection = null;
    public DBService() {
        this.open();
    }
    private void open() {
        String connectionUrl =
            "jdbc:sqlserver://localhost:1433;"
                + "database=seodb;"
                + "user=ingu;"
                + "password=m;"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";
        try {
            if (this.connection == null) {
                this.connection = DriverManager.getConnection(connectionUrl);
            }
            if (this.connection == null) {
                log.error("failed to DB");
            } else {
                log.info("connected DB");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public ResultSet execQuery(String sql) {
        ResultSet rs = null;
        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            return rs;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean execCmd(String sql) {
        PreparedStatement stmt = null;
        try {
            stmt = this.connection.prepareStatement(sql);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public int execSQL(String sql) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
            stmt.execute();
            rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                log.debug(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
