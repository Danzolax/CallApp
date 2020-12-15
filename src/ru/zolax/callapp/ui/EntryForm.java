package ru.zolax.callapp.ui;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.database.managers.AccountEntityManager;
import ru.zolax.callapp.ui.adminsForms.AdminMainForm;
import ru.zolax.callapp.ui.usersForms.MainForm;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;


import javax.swing.*;
import java.sql.SQLException;

public class EntryForm extends BaseForm {
    private final AccountEntityManager accountEntityManager = new AccountEntityManager(Application.getInstance().getDatabase());
    private JTextField login;
    private JPasswordField password;
    private JButton registrationButton;
    private JButton enterButton;
    private JPanel mainPanel;

    public EntryForm() {
        setContentPane(mainPanel);
        setVisible(true);
        initButtons();
    }

    private void initButtons() {
        enterButton.addActionListener(e -> {
            String loginText = login.getText();
            String passwordText = new String(password.getPassword());
            try {
                AccountEntity account = accountEntityManager.getByLogin(loginText);
                if (account == null) {
                    DialogUtil.showWarn("Такого аккаунта не существует");
                } else if (!account.getPassword().equals(passwordText)) {
                    DialogUtil.showWarn("Неправильный пароль");
                } else {
                    Application.setCurrentAccount(account);
                    dispose();
                    if (account.isAdmin()) {
                        new AdminMainForm();
                    } else {
                        new MainForm();
                    }
                }
            } catch (SQLException sqlException) {
                DialogUtil.showError("Ошибка бд");
                sqlException.printStackTrace();
            }
        });
        registrationButton.addActionListener(e -> {
            dispose();
            new RegistrationForm();
        });
    }

    @Override
    public int getFormWidth() {
        return 500;
    }

    @Override
    public int getFormHeight() {
        return 500;
    }
}
