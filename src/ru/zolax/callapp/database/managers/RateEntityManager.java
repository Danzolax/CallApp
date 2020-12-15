package ru.zolax.callapp.database.managers;

import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.RateEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RateEntityManager implements ManagerInterface<RateEntity> {
    private MySqlDatabase db;

    public RateEntityManager(MySqlDatabase db) {
        this.db = db;
    }

    @Override
    public void add(RateEntity entity) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "INSERT INTO rate (rate_user_zone_code, rate_call_zone_code, rate_call_price_per_minute) " +
                    "VALUES (?,?,?)";
            PreparedStatement statement = c.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getUserZoneCodeFK());
            statement.setInt(2, entity.getCallZoneCodeFK());
            statement.setDouble(3, entity.getPricePerMinute());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("rate not added");
        }
    }

    @Override
    public int update(RateEntity entity) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "UPDATE rate SET rate_user_zone_code=?, rate_call_zone_code=?, rate_call_price_per_minute=? WHERE rate_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, entity.getUserZoneCodeFK());
            statement.setInt(2, entity.getCallZoneCodeFK());
            statement.setDouble(3, entity.getPricePerMinute());
            statement.setInt(4, entity.getId());
            return statement.executeUpdate();
        }

    }

    @Override
    public int deleteById(int id) throws SQLException  {
        try (Connection c = db.getConnection()) {
            String query = "DELETE FROM rate WHERE rate_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }

    @Override
    public RateEntity getById(int id) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM rate WHERE rate_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new RateEntity(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getDouble(4)
                );
            }
        }
        return null;
    }

    @Override
    public List<RateEntity> getAll() throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM rate";
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            List<RateEntity> ratesList = new ArrayList<>();
            while (result.next()) {
                ratesList.add(new RateEntity(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getDouble(4)
                ));
            }
            return ratesList;
        }
    }
}
