package ru.zolax.callapp.ui.adminsForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.AccountEntity;
import ru.zolax.callapp.database.managers.AccountEntityManager;
import ru.zolax.callapp.database.managers.UserInformationEntityManager;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.ObjectTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class AdminAccountsTableForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final AccountEntityManager accountManager = new AccountEntityManager(db);
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(db);

    private JPanel mainPanel;
    private JTable table;
    private JButton backButton;
    private ObjectTableModel<AccountEntity> model;

    public AdminAccountsTableForm() {
        setContentPane(mainPanel);
        setVisible(true);
        initTable();
        initButtons();
    }

    private void initButtons() {
        backButton.addActionListener(e -> {
            dispose();
            new AdminMainForm();
        });
    }

    private void initTable() {
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.rowAtPoint(e.getPoint());
                    dispose();
                    try {
                        new AdminAccountUpdateDeleteForm(accountManager.getAllNoAdmin().get(row));
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        });
        model = new ObjectTableModel<AccountEntity>() {

            @Override
            protected AccountEntity getEntityFromRowData(Object[] rowData) {
                return new AccountEntity((int) rowData[1], (int) rowData[2], (String) rowData[4], (String) rowData[5], (boolean) rowData[6]);
            }
            // TODO("asdsadasd")

            @Override
            protected Object[] getRowDataFromEntity(AccountEntity entity) {
                int counter = this.getCounter();
                this.setCounter(++counter);
                try {
                    return new Object[]{
                            counter,
                            entity.getId(),
                            entity.getUserFK(),
                            userManager.getById(entity.getUserFK()).getName(),
                            entity.getLogin(),
                            entity.getPassword(),
                            entity.isAdmin()
                    };
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                return new Object[]{
                        counter,
                        entity.getId(),
                        entity.getUserFK(),
                        null,
                        entity.getLogin(),
                        entity.getPassword(),
                        entity.isAdmin()
                };
            }

        };
        table.setModel(model);
        model.addColumn("Номер"); // 0
        model.addColumn("ID"); // 1
        model.addColumn("ID абонента"); // 2
        model.addColumn("ФИО абонента"); // 3
        model.addColumn("Логин"); //4
        model.addColumn("Пароль"); //5
        fillData();
        table.removeColumn(table.getColumnModel().getColumn(1));
        table.removeColumn(table.getColumnModel().getColumn(1));
    }

    private void fillData() {
        model.setCounter(0);
        try {
            model.addRowEntities(accountManager.getAllNoAdmin());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public int getFormWidth() {
        return 800;
    }

    @Override
    public int getFormHeight() {
        return 500;
    }
}
