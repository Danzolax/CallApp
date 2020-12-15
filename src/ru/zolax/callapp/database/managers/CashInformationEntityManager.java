package ru.zolax.callapp.database.managers;

import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.CashInformationEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CashInformationEntityManager implements ManagerInterface<CashInformationEntity> {
    private MySqlDatabase db;

    public CashInformationEntityManager(MySqlDatabase db){
        this.db = db;
    }

    @Override
    public void add(CashInformationEntity entity) throws SQLException {
        try(Connection c = this.db.getConnection()){
            String query = "INSERT INTO cash_information (cash_balance, cash_date_addition) VALUES (?, ?)";
            PreparedStatement statement = c.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setDouble(1,entity.getBalance());
            statement.setTimestamp(2,entity.getAdditionDate());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()){
                entity.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("CashInformation not added");
        }
    }

    @Override
    public int update(CashInformationEntity entity) throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "UPDATE cash_information SET cash_balance=?, cash_date_addition=? WHERE cash_information_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setDouble(1,entity.getBalance());
            statement.setTimestamp(2,entity.getAdditionDate());
            statement.setInt(3,entity.getId());
            return statement.executeUpdate();
        }
    }

    @Override
    public int deleteById(int id) throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "DELETE FROM cash_information WHERE cash_information_id =?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1,id);
            return statement.executeUpdate();
        }
    }

    @Override
    public CashInformationEntity getById(int id) throws SQLException {
        try(Connection c = db.getConnection()) {
            String query = "SELECT * FROM cash_information WHERE cash_information_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1,id);

            ResultSet result = statement.executeQuery();
            if(result.next()){
                return new CashInformationEntity(
                        result.getInt("cash_information_id"),
                        result.getDouble("cash_balance"),
                        result.getTimestamp("cash_date_addition")
                );
            }

        }
        return null;
    }

    @Override
    public List<CashInformationEntity> getAll() throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "SELECT * FROM cash_information";
            Statement statement = c.createStatement();
            ResultSet result = statement.executeQuery(query);
            List<CashInformationEntity> cashes = new ArrayList<>();
            while (result.next()){
                cashes.add(new CashInformationEntity(
                        result.getInt("cash_information_id"),
                        result.getDouble("cash_balance"),
                        result.getTimestamp("cash_date_addition")
                ));
            }
            return cashes;
        }
    }
}
