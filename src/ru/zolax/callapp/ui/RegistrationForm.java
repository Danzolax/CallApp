package ru.zolax.callapp.ui;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.database.entities.CashInformationEntity;
import ru.zolax.callapp.database.entities.UserInformationEntity;
import ru.zolax.callapp.database.managers.AccountEntityManager;
import ru.zolax.callapp.database.managers.CashInformationEntityManager;
import ru.zolax.callapp.database.managers.UserInformationEntityManager;
import ru.zolax.callapp.ui.EntryForm;
import ru.zolax.callapp.ui.usersForms.MainForm;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

public class RegistrationForm  extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final AccountEntityManager accountManager = new AccountEntityManager(db);
    private final CashInformationEntityManager cashManager = new CashInformationEntityManager(db);
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(db);

    private JTextField login;
    private JTextField password;
    private JTextField city;
    private JTextField telephoneNumber;
    private JTextField userName;
    private JPanel mainPanel;
    private JTextField address;
    private JButton registrationButton;
    private JButton backButton;

    public RegistrationForm() {
        setContentPane(mainPanel);
        setVisible(true);
        initButtons();
    }

    private void initButtons() {
        backButton.addActionListener(e->{
            dispose();
            new EntryForm();
        });
        registrationButton.addActionListener(e->{
            String loginText = login.getText();
            String passwordText = password.getText();
            String cityText = city.getText();
            String addressText = address.getText();
            String telephoneText = telephoneNumber.getText();
            String userText = userName.getText();

            try {
                if (accountManager.getByLogin(loginText)!= null){
                    DialogUtil.showWarn("Такой логин уже занят!!!");
                } else if(userManager.getByTelephoneNumber(telephoneText) !=null){
                    DialogUtil.showWarn("Такой номер телефона уже занят!!!");
                }
                else{
                    CashInformationEntity cash = new CashInformationEntity(0);
                    cashManager.add(cash);
                    UserInformationEntity user = new UserInformationEntity(userText,Integer.parseInt(telephoneText),addressText,cityText,cash.getId());
                    userManager.add(user);
                    AccountEntity account = new AccountEntity(user.getId(),loginText,passwordText,false);
                    accountManager.add(account);
                    Application.setCurrentAccount(account);
                    dispose();
                    new MainForm();
                }
            } catch (SQLException sqlException) {
                DialogUtil.showError("Ошибка бд");
                sqlException.printStackTrace();
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
