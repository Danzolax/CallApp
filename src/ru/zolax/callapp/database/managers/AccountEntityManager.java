package ru.zolax.callapp.database.managers;

import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountEntityManager implements ManagerInterface<AccountEntity> {
    private final MySqlDatabase db;

    public AccountEntityManager(MySqlDatabase db){
        this.db = db;
    }


    @Override
    public void add(AccountEntity entity) throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "INSERT INTO Account (user_id,login,password,isAdmin) VALUES (?,?,?,?)";
            PreparedStatement  statement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,entity.getUserFK());
            statement.setString(2,entity.getLogin());
            statement.setString(3,entity.getPassword());
            statement.setBoolean(4,entity.isAdmin());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if(keys.next()){
                entity.setId(keys.getInt(1));
                return;
            }
            throw new SQLException("account not added");
        }
    }

    @Override
    public int update(AccountEntity entity) throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "UPDATE account SET user_id=?,login=?,password=?,isAdmin=? WHERE account_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1,entity.getUserFK());
            statement.setString(2,entity.getLogin());
            statement.setString(3,entity.getPassword());
            statement.setBoolean(4,entity.isAdmin());
            statement.setInt(5,entity.getId());

            return statement.executeUpdate();
        }
    }

    @Override
    public int deleteById(int id) throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "DELETE FROM account WHERE account_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1,id);
            return statement.executeUpdate();
        }
    }

    @Override
    public AccountEntity getById(int id) throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "SELECT  * FROM account WHERE account_id=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setInt(1,id);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                return new AccountEntity(
                  result.getInt(1),
                  result.getInt(2),
                  result.getString(3),
                  result.getString(4),
                  result.getBoolean(5)
                );
            }
        }
        return null;
    }

    @Override
    public List<AccountEntity> getAll() throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "SELECT  * FROM account";
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            List<AccountEntity> accountsList = new ArrayList<>();
            while (result.next()){
                accountsList.add(new AccountEntity(
                        result.getInt(1),
                        result.getInt(2),
                        result.getString(3),
                        result.getString(4),
                        result.getBoolean(5)
                ));
            }
            return accountsList;
        }
    }

    public AccountEntity getByLogin(String login) throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "SELECT * FROM account where login=?";
            PreparedStatement statement = c.prepareStatement(query);
            statement.setString(1,login);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return new AccountEntity(
                        result.getInt(1),
                        result.getInt(2),
                        result.getString(3),
                        result.getString(4),
                        result.getBoolean(5)
                );
            }
        }
        return null;
    }

    public List<AccountEntity> getAllNoAdmin() throws SQLException {
        try(Connection c = db.getConnection()){
            String query = "SELECT  * FROM account where isAdmin=false";
            PreparedStatement statement = c.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            List<AccountEntity> accountsList = new ArrayList<>();
            while (result.next()){
                accountsList.add(new AccountEntity(
                        result.getInt(1),
                        result.getInt(2),
                        result.getString(3),
                        result.getString(4),
                        result.getBoolean(5)
                ));
            }
            return accountsList;
        }
    }


}
