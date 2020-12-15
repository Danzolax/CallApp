package ru.zolax.callapp.ui.adminsForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.database.entities.UserInformationEntity;
import ru.zolax.callapp.database.managers.*;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

public class AdminAccountUpdateDeleteForm extends BaseForm {
    private final AccountEntity accountEntity;

    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final AccountEntityManager accountManager = new AccountEntityManager(db);
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(db);
    private final UserCallEntityManager userCallManager = new UserCallEntityManager(db);
    private final CallInformationEntityManager callManager = new CallInformationEntityManager(db);
    private final CashInformationEntityManager cashManager = new CashInformationEntityManager(db);

    private JPanel mainPanel;
    private JTextField loginText;
    private JTextField passwordText;
    private JButton backButton;
    private JButton deleteButton;
    private JButton updateButton;

    public AdminAccountUpdateDeleteForm(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
        setContentPane(mainPanel);
        setVisible(true);
        initButtons();
        initUI();
    }

    private void initUI() {
        loginText.setText(accountEntity.getLogin());
        passwordText.setText(accountEntity.getPassword());
    }

    private void initButtons() {
        backButton.addActionListener(e -> {
            dispose();
            new AdminAccountsTableForm();
        });
        updateButton.addActionListener(e -> {
            if(DialogUtil.showConfirm("Вы точно хотите изменить информацию об аккаунте")){
                if (!loginText.equals("") && !passwordText.equals("")){
                    accountEntity.setLogin(loginText.getText());
                    accountEntity.setPassword(passwordText.getText());
                    try {
                        accountManager.update(accountEntity);
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                        DialogUtil.showError("Ошибка БД");
                    }
                } else {
                    DialogUtil.showError("Пустое поле!");
                }
                dispose();
                new AdminAccountsTableForm();
            }
        });
        deleteButton.addActionListener(e -> {
            if(DialogUtil.showConfirm("Вы точно хотите удалить аккаунт? Это повлечет за собой удаление все данных связанных с ним.")){
                try {
                    UserInformationEntity user = userManager.getById(accountEntity.getUserFK());
                    user.setName("(Архив) "+user.getName());
                    accountManager.deleteById(accountEntity.getId());
                    userManager.update(user);
                    dispose();
                    new AdminAccountsTableForm();
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                    DialogUtil.showError("Ошибка удаления из БД");
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
