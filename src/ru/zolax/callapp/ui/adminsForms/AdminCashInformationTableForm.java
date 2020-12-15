package ru.zolax.callapp.ui.adminsForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.CashInformationEntity;
import ru.zolax.callapp.database.entities.UserInformationEntity;
import ru.zolax.callapp.database.managers.CashInformationEntityManager;
import ru.zolax.callapp.database.managers.UserInformationEntityManager;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.ObjectTableModel;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class AdminCashInformationTableForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(db);
    private final CashInformationEntityManager cashManager = new CashInformationEntityManager(db);

    private JPanel mainPanel;
    private JTable table;
    private JButton backButton;


    private ObjectTableModel<CashInformationEntity> model;


    public AdminCashInformationTableForm() {
        setContentPane(mainPanel);
        setVisible(true);
        initTable();
        initCheckBoxAndButtons();
    }

    private void initCheckBoxAndButtons() {
        backButton.addActionListener(e -> {
            dispose();
            new AdminMainForm();
        });
    }

    private void initTable() {
        table.getTableHeader().setReorderingAllowed(false);
        model = new ObjectTableModel<>() {
            @Override
            protected CashInformationEntity getEntityFromRowData(Object[] rowData) {
                return new CashInformationEntity(
                        (int) rowData[1],
                        (double) rowData[2],
                        (Timestamp) rowData[3]
                );
            }

            @Override
            protected Object[] getRowDataFromEntity(CashInformationEntity entity) {
                int counter = this.getCounter();
                this.setCounter(++counter);
                return new Object[]{counter,entity.getId(), entity.getBalance(), entity.getAdditionDate()
                };
            }
        };

        table.setModel(model);
        model.addColumn("Номер");
        model.addColumn("ID");
        model.addColumn("Баланс");
        model.addColumn("Дата пополнения");
        model.addColumn("ФИО владельца");
        fillData();
        table.removeColumn(table.getColumnModel().getColumn(1));


    }


    private void fillData() {
        model.setCounter(0);
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }

        try {
            List<CashInformationEntity> cashList = cashManager.getAll();
            model.addRowEntities(cashList);
            for (int row = 0; row < model.getRowCount(); row++) {
                model.setValueAt(userManager.getByCashID(cashList.get(row).getId()).getName(),row,4);
            }
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
