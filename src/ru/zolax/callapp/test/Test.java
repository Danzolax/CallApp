package ru.zolax.callapp.test;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.database.managers.AccountEntityManager;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        AccountEntity entity = new AccountEntity(1, "fdsf", "asdsad", true);
        AccountEntityManager accountEntityManager = new AccountEntityManager(new MySqlDatabase("localhost", "callapp", "root", "161200"));
        System.out.println(accountEntityManager.getByLogin("fdsf"));
    }
}
