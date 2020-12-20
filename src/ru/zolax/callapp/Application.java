package ru.zolax.callapp;

import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.ui.EntryForm;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;
import ru.zolax.callapp.util.SqlDump;


import javax.swing.*;
import java.sql.Connection;
import java.util.Timer;
public class Application
{
    private static Application instance;

    private final MySqlDatabase database = new MySqlDatabase("localhost","callapp","root","161200");
    private static AccountEntity currentAccount;

    private Application()
    {
        instance = this;

        initUi();
        initDatabase();
        initDumpService();
        new EntryForm();

    }

    private void initDumpService() {
        Timer timer = new Timer();
        timer.schedule(new SqlDump(),1,86_400_000);
    }

    public static AccountEntity getCurrentAccount() {
        return currentAccount;
    }

    public static void setCurrentAccount(AccountEntity currentAccount) {
        Application.currentAccount = currentAccount;
    }

    private void initDatabase()
    {
        try(Connection c = database.getConnection()) {
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.showError("Ошибка подключения к бд");
            System.exit(-1);
        }

    }

    private void initUi()
    {
        BaseForm.setBaseApplicationTitle("Call app");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MySqlDatabase getDatabase() {
        return database;
    }

    public static Application getInstance() {
        return instance;
    }

    public static void main(String[] args)
    {
        new Application();
    }
}
