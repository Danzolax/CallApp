package ru.zolax.callapp.ui.usersForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.database.managers.AccountEntityManager;
import ru.zolax.callapp.ui.EntryForm;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

public class UpdateUserInfForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final AccountEntityManager accountManager = new AccountEntityManager(db);
    private JPanel mainPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton backButton;
    private JButton updateButton;

    public UpdateUserInfForm() {
        setContentPane(mainPanel);
        setVisible(true);
        initButtons();
        initUI();
    }

    private void initUI() {
        textField1.setText(Application.getCurrentAccount().getLogin());
        passwordField1.setText(Application.getCurrentAccount().getPassword());
    }

    private void initButtons() {
        backButton.addActionListener(e -> {
            dispose();
            new AccountInformationForm();
        });
        updateButton.addActionListener(e -> {
            if (DialogUtil.showConfirm("Подтвердите действие")) {
                AccountEntity currAccount = Application.getCurrentAccount();
                if (!textField1.getText().equals("") && !new String(passwordField1.getPassword()).equals("")){
                    currAccount.setLogin(textField1.getText());
                    currAccount.setPassword(new String(passwordField1.getPassword()));
                    try {
                        accountManager.update(currAccount);
                        Application.setCurrentAccount(null);
                        dispose();
                        new EntryForm();
                    } catch (SQLException sqlException) {
                        DialogUtil.showError("Ошибка взятия данных из БД");
                        sqlException.printStackTrace();
                    }
                } else {
                    DialogUtil.showWarn("Вы оставили пустое поле");
                }
            }


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
