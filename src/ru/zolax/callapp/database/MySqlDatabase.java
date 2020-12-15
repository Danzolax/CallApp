package ru.zolax.callapp.database;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlDatabase {
    private final String address;
    private final int port;
    private final String db;
    private final String user;
    private final String pass;

    private MysqlDataSource source;

    public MySqlDatabase(String address, int port, String db, String user, String pass) {
        this.address = address;
        this.port = port;
        this.db = db;
        this.user = user;
        this.pass = pass;
    }

    public MySqlDatabase(String address, String db, String user, String pass) {
        this(address, 3306, db, user, pass);
    }

    public Connection getConnection() throws SQLException {
        if(source == null)
            source = new MysqlDataSource();
        source.setServerName(address);
        source.setPort(port);
        source.setDatabaseName(db);
        source.setUser(user);
        source.setPassword(pass);
        source.setCharacterEncoding("UTF-8");
        source.setServerTimezone("Europe/Moscow");
        source.setUseSSL(false);
        return source.getConnection();
    }
}
