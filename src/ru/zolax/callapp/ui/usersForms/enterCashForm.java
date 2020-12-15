package ru.zolax.callapp.ui.usersForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.database.entities.CashInformationEntity;
import ru.zolax.callapp.database.entities.UserInformationEntity;
import ru.zolax.callapp.database.managers.AccountEntityManager;
import ru.zolax.callapp.database.managers.CashInformationEntityManager;
import ru.zolax.callapp.database.managers.UserInformationEntityManager;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Date;

public class enterCashForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final AccountEntityManager accountManager = new AccountEntityManager(db);
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(db);
    private final CashInformationEntityManager cashManager = new CashInformationEntityManager(db);
    private JPanel mainPanel;
    private JTextField enterCash;
    private JButton enterButton;
    private JButton backButton;

    public enterCashForm() {
        setContentPane(mainPanel);
        initButtons();
        setVisible(true);
    }

    private void initButtons()  {
        backButton.addActionListener(e->{
            dispose();
            new CashForm();
        });
        enterButton.addActionListener(e->{
            if(DialogUtil.showConfirm("Подтвердите операцию")){
                AccountEntity currAccount = Application.getCurrentAccount();
                try {
                    UserInformationEntity user = userManager.getById(currAccount.getUserFK());
                    CashInformationEntity cash = cashManager.getById(user.getCashInformationFK());
                    cash.setBalance(cash.getBalance() + Double.parseDouble(enterCash.getText()));
                    cash.setAdditionDateInMillis(new Date().getTime());
                    cashManager.update(cash);
                    dispose();
                    new CashForm();
                } catch (SQLException sqlException) {
                    DialogUtil.showError("Ошибка взятия данных из БД");
                    sqlException.printStackTrace();
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
