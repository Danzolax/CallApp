package ru.zolax.callapp.database.managers;

import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.UserInformationEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserInformationEntityManager implements ManagerInterface<UserInformationEntity> {
    private MySqlDatabase db;

    public UserInformationEntityManager(MySqlDatabase db) {
        this.db = db;
    }

    @Override
    public void add(UserInformationEntity entity) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "INSERT INTO User_information (" +
                    "user_name," +
                    "user_telephone_number," +
                    "user_adress," +
                    "user_city," +
                    "cash_information) VALUES(?,?,?,?,?)";
            PreparedStatement statement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getTelephoneNumber());
            statement.setString(3, entity.getAddress());
            statement.setString(4, entity.getCity());
            statement.setInt(5, entity.getCashInformationFK());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("User not added");
        }
    }


    @Override
    public int update(UserInformationEntity entity) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "UPDATE User_information SET  " +
                    "user_name=?, " +
                    "user_telephone_number=?, " +
                    "user_adress=?, " +
                    "user_city=?, " +
                    "cash_information=? WHERE user_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getTelephoneNumber());
            statement.setString(3, entity.getAddress());
            statement.setString(4, entity.getCity());
            statement.setInt(5, entity.getCashInformationFK());
            statement.setInt(6, entity.getId());
            return statement.executeUpdate();
        }
    }

    @Override
    public int deleteById(int id) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "DELETE FROM User_information WHERE user_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }

    @Override
    public UserInformationEntity getById(int id) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM User_information WHERE user_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new UserInformationEntity(
                        result.getInt("user_id"),
                        result.getString("user_name"),
                        result.getInt("user_telephone_number"),
                        result.getString("user_adress"),
                        result.getString("user_city"),
                        result.getInt("cash_information")
                );
            }
        }
        return null;
    }

    @Override
    public List<UserInformationEntity> getAll() throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM User_information";
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            List<UserInformationEntity> usersList = new ArrayList<>();
            while (result.next()) {
                usersList.add(new UserInformationEntity(
                        result.getInt("user_id"),
                        result.getString("user_name"),
                        result.getInt("user_telephone_number"),
                        result.getString("user_adress"),
                        result.getString("user_city"),
                        result.getInt("cash_information")
                ));
            }
            return usersList;
        }
    }

    public UserInformationEntity getByTelephoneNumber(String telephoneText) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM User_information WHERE user_telephone_number=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(telephoneText));
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new UserInformationEntity(
                        result.getInt("user_id"),
                        result.getString("user_name"),
                        result.getInt("user_telephone_number"),
                        result.getString("user_adress"),
                        result.getString("user_city"),
                        result.getInt("cash_information")
                );
            }
        }
        return null;
    }

    public UserInformationEntity getByCashID(int cashId) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM User_information WHERE cash_information=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, cashId);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new UserInformationEntity(
                        result.getInt("user_id"),
                        result.getString("user_name"),
                        result.getInt("user_telephone_number"),
                        result.getString("user_adress"),
                        result.getString("user_city"),
                        result.getInt("cash_information")
                );
            }
        }
        return null;
    }
}
