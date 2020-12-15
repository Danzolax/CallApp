package ru.zolax.callapp.database.managers;

import java.sql.SQLException;
import java.util.List;

public interface ManagerInterface <T> {

    public void add(T entity) throws SQLException;
    public int update(T entity) throws SQLException;
    public int deleteById(int id) throws SQLException;
    public T getById(int id) throws SQLException;
    public List<T> getAll() throws SQLException;
}
