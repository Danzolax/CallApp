package ru.zolax.callapp.database.managers;

import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.database.entities.CallInformationEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CallInformationEntityManager{
    private MySqlDatabase db;

    public CallInformationEntityManager(MySqlDatabase db){
        this.db = db;
    }

    public void add(CallInformationEntity entity) throws SQLException {
        try(Connection c  = db.getConnection()){
            String query = "INSERT INTO call_information (rate_id, call_duration, call_datetime, call_price) VALUES (?,?,?,?)";
            PreparedStatement statement = c.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1,entity.getRateIdFK());
            statement.setInt(2,entity.getDuration());
            statement.setTimestamp(3,entity.getDate());
            statement.setDouble(4,entity.getPrice());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()){
                entity.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("callInf not added");

        }
    }


    public List<CallInformationEntity> getAll() throws SQLException {
        try(Connection c  = db.getConnection()){
            String query = "SELECT * FROM call_information";
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            List<CallInformationEntity> callInformationEntities = new ArrayList<>();
            while (result.next()){
                callInformationEntities.add(new CallInformationEntity(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getTimestamp(4),
                        result.getDouble(5)
                ));
            }
            return callInformationEntities;
        }
    }

    public List<CallInformationEntity> getAllByUserID(int id) throws SQLException{
        try(Connection c = db.getConnection()) {
            String query = "SELECT * FROM call_information " +
                    "INNER JOIN user_call uc on call_information.call_id = uc.call_id " +
                    "INNER JOIN user_information ui on uc.user_id = ui.user_id" +
                    " WHERE ui.user_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1,id);
            ResultSet result = statement.executeQuery();
            List<CallInformationEntity> callInformationEntities = new ArrayList<>();
            while (result.next()){
                callInformationEntities.add(new CallInformationEntity(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getTimestamp(4),
                        result.getDouble(5)
                ));
            }
            return callInformationEntities;
        }
    }

    public CallInformationEntity getById(int id) throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "SELECT  * FROM call_information WHERE call_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1,id);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                return new CallInformationEntity(
                        result.getInt(1),
                        result.getInt(2),
                        result.getInt(3),
                        result.getTimestamp(4),
                        result.getDouble(5)
                );
            }
        }
        return null;
    }
}
