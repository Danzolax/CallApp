package ru.zolax.callapp.database.managers;

import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.ZoneCodeNameEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ZoneCodeNameEntityManager implements ManagerInterface<ZoneCodeNameEntity> {
    private MySqlDatabase db;

    public ZoneCodeNameEntityManager(MySqlDatabase db) {
        this.db = db;
    }

    @Override
    public void add(ZoneCodeNameEntity entity) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "INSERT INTO zone_code_name (zoneCode, zoneName) VALUES (?,?)";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, entity.getZoneCode());
            statement.setString(2, entity.getZoneName());
            statement.executeUpdate();
        }
    }

    @Override
    public int update(ZoneCodeNameEntity entity) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "UPDATE zone_code_name SET zoneName=? WHERE zoneCode=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1, entity.getZoneName());
            statement.setInt(2, entity.getZoneCode());
            return statement.executeUpdate();
        }
    }

    @Override
    public int deleteById(int id) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "DELETE FROM zone_code_name WHERE zoneCode=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }

    @Override
    public ZoneCodeNameEntity getById(int id) throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM zone_code_name WHERE zoneCode=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return new ZoneCodeNameEntity(
                        result.getInt(1),
                        result.getString(2)
                );
            }
        }
        return null;
    }

    @Override
    public List<ZoneCodeNameEntity> getAll() throws SQLException {
        try (Connection c = db.getConnection()) {
            String query = "SELECT * FROM zone_code_name";
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            List<ZoneCodeNameEntity> zonesList = new ArrayList<>();
            while (result.next()){
                zonesList.add(new ZoneCodeNameEntity(
                        result.getInt(1),
                        result.getString(2)
                ));
            }
            return zonesList;
        }
    }
}
