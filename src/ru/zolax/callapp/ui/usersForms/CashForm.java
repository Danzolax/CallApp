package ru.zolax.callapp.ui.usersForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.database.entities.CashInformationEntity;
import ru.zolax.callapp.database.entities.UserInformationEntity;
import ru.zolax.callapp.database.managers.CashInformationEntityManager;
import ru.zolax.callapp.database.managers.UserInformationEntityManager;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

public class CashForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(db);
    private final CashInformationEntityManager cashManager = new CashInformationEntityManager(db);
    private JPanel mainPanel;
    private JButton backButton;
    private JButton enterCashButton;
    private JLabel balance;
    private JLabel dateAddition;

    public CashForm() {
        setContentPane(mainPanel);
        setVisible(true);
        initButtons();
        try {
            initUI();
        } catch (SQLException sqlException) {
            DialogUtil.showError("Ошибка взятия данных из БД");
            sqlException.printStackTrace();
        }
    }

    private void initUI() throws SQLException {
        AccountEntity currAccount = Application.getCurrentAccount();
        UserInformationEntity user = userManager.getById(currAccount.getUserFK());
        CashInformationEntity cash = cashManager.getById(user.getCashInformationFK());
        balance.setText(String.valueOf(cash.getBalance()));
        dateAddition.setText(cash.getAdditionDate().toString());
    }

    private void initButtons() {
        backButton.addActionListener(e->{
            dispose();
            new MainForm();
        });
        enterCashButton.addActionListener(e->{
            dispose();
            new enterCashForm();
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
