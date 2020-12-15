package ru.zolax.callapp.database.managers;

import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.UserCallEntity;
import ru.zolax.callapp.util.DialogUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserCallEntityManager {

    private MySqlDatabase db;

    public UserCallEntityManager(MySqlDatabase db) {
        this.db = db;
    }

    public void add(UserCallEntity entity) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "INSERT INTO user_call (user_id, call_id, user_call_pay_date, user_call_is_paid) VALUES (?,?,?,?)";
            PreparedStatement statement = c.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getUserIdFK());
            statement.setInt(2, entity.getCallIdFK());
            statement.setTimestamp(3, entity.getPayDate());
            statement.setBoolean(4, entity.isPaid());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("usercall not added");
        }
    }

    public int update(UserCallEntity entity) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "UPDATE user_call SET user_id=?, call_id=?, user_call_pay_date=?, user_call_is_paid=? WHERE user_call_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, entity.getUserIdFK());
            statement.setInt(2, entity.getCallIdFK());
            statement.setTimestamp(3, entity.getPayDate());
            statement.setBoolean(4, entity.isPaid());
            statement.setInt(5, entity.getId());
            return statement.executeUpdate();
        }
    }


    public List<UserCallEntity> getByUserId(int id) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM user_call natural join user_information where user_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1,id);
            ResultSet result = statement.executeQuery();
            List<UserCallEntity> list = new ArrayList<>();
            while (result.next()){
                list.add(new UserCallEntity(
                        result.getInt("user_call_id"),
                        result.getInt("user_id"),
                        result.getInt("call_id"),
                        result.getTimestamp("user_call_pay_date"),
                        result.getBoolean("user_call_is_paid")
                ));
            }
            return list;


        }

    }


    public List<UserCallEntity> getAll() throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM user_call";
            Statement statement = c.createStatement();
            ResultSet result = statement.executeQuery(query);
            List<UserCallEntity> list = new ArrayList<>();
            while (result.next()) {
                list.add(new UserCallEntity(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getTimestamp(4),
                        result.getBoolean(5)
                ));
            }
            return list;
        }

    }

    public UserCallEntity getByCallID(int id) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM user_call WHERE call_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1,id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return (new UserCallEntity(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getTimestamp(4),
                        result.getBoolean(5)
                ));
            }
            return null;
        }

    }

    public boolean payCall(int id, boolean isEnough) throws SQLException {
        if (isEnough){
            UserCallEntity entity = this.getByCallID(id);
            entity.setPaid(true);
            entity.setPayDateInMillis(new Date().getTime());
            this.update(entity);
            return true;
        }
        else {
            return false;
        }
    }

    public int deleteAllByUserId(int id) throws SQLException{
        try(Connection c = db.getConnection()) {
            String query = "DELETE FROM user_call WHERE user_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1,id);
            return statement.executeUpdate();
        }

    }
}
