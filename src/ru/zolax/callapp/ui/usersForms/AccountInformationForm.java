package ru.zolax.callapp.ui.usersForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.database.entities.UserInformationEntity;
import ru.zolax.callapp.database.managers.AccountEntityManager;
import ru.zolax.callapp.database.managers.UserInformationEntityManager;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

public class AccountInformationForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final AccountEntityManager accountManager = new AccountEntityManager(db);
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(db);


    private JPanel mainPanel;
    private JButton backButton;
    private JLabel loginText;
    private JLabel telephoneText;
    private JLabel cityText;
    private JLabel addressText;
    private JLabel userNameText;
    private JButton updateButton;

    public AccountInformationForm() {
        setContentPane(mainPanel);
        setVisible(true);
        initButtons();
        try {
            initUI();
        } catch (SQLException sqlException) {
            DialogUtil.showError("Ошибка взятия данных из БД");
        }
    }

    private void initUI() throws SQLException {
        AccountEntity currAccount = Application.getCurrentAccount();
        UserInformationEntity user = userManager.getById(currAccount.getUserFK());
        loginText.setText(currAccount.getLogin());
        userNameText.setText(user.getName());
        telephoneText.setText(String.valueOf(user.getTelephoneNumber()));
        cityText.setText(user.getCity());
        addressText.setText(user.getAddress());
    }

    private void initButtons() {
        backButton.addActionListener(e->{
            dispose();
            new MainForm();
        });
        updateButton.addActionListener(e->{
            dispose();
            new UpdateUserInfForm();
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
