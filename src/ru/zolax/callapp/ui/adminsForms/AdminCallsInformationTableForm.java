package ru.zolax.callapp.ui.adminsForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.CallInformationEntity;
import ru.zolax.callapp.database.entities.UserInformationEntity;
import ru.zolax.callapp.database.managers.CallInformationEntityManager;
import ru.zolax.callapp.database.managers.UserCallEntityManager;
import ru.zolax.callapp.database.managers.UserInformationEntityManager;
import ru.zolax.callapp.ui.usersForms.MainForm;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;
import ru.zolax.callapp.util.ObjectTableModel;

import javax.swing.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminCallsInformationTableForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final CallInformationEntityManager callsInformation = new CallInformationEntityManager(db);
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(db);
    private final UserCallEntityManager userCall = new UserCallEntityManager(db);

    private JPanel mainPanel;
    private JTable table;
    private JButton backButton;
    private JComboBox<String> changeUserComboBox;
    private JButton showAllButton;

    private ObjectTableModel<CallInformationEntity> model;

    public AdminCallsInformationTableForm() {
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
        showAllButton.addActionListener(e -> {
            fillData(-1);
        });

        try {
            List<UserInformationEntity> usersList = userManager.getAll();
            usersList.forEach(e->{
                changeUserComboBox.addItem(e.getName());
            });
            changeUserComboBox.addActionListener(e->{
                fillData(usersList.get(changeUserComboBox.getSelectedIndex()).getId());
            });
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }



    }

    private void initTable() {
        table.getTableHeader().setReorderingAllowed(false);
        model = new ObjectTableModel<CallInformationEntity>() {
            @Override
            protected CallInformationEntity getEntityFromRowData(Object[] rowData) {
                return new CallInformationEntity(
                        (int) rowData[1],
                        (int) rowData[2],
                        (int) rowData[3],
                        (Timestamp) rowData[4],
                        (double) rowData[5]
                );
            }

            @Override
            protected Object[] getRowDataFromEntity(CallInformationEntity entity) {
                int counter = this.getCounter();
                this.setCounter(++counter);
                return new Object[]{
                        counter,
                        entity.getId(),
                        entity.getRateIdFK(),
                        entity.getDuration(),
                        entity.getDate(),
                        entity.getPrice()
                };
            }
        };
        table.setModel(model);
        model.addColumn("Номер");
        model.addColumn("ID");
        model.addColumn("ID тарифа");
        model.addColumn("Длительность звонка");
        model.addColumn("Дата звонка");
        model.addColumn("Цена");
        model.addColumn("Статус оплаты звонка");
        model.addColumn("Дата оплаты звонка");
        fillData(-1);
        table.removeColumn(table.getColumnModel().getColumn(1));
        table.removeColumn(table.getColumnModel().getColumn(1));


    }

    public void fillData(int userId) {
        model.setCounter(0);
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }
        try {
            List<CallInformationEntity> ciList;
            if (userId == -1)
                ciList = callsInformation.getAll();
            else
                ciList = callsInformation.getAllByUserID(userId);
            model.addRowEntities(ciList);
            for (int row = 0; row < ciList.size(); row++) {
                if(userCall.getByCallID(ciList.get(row).getId()) == null){
                    model.setValueAt("no", row, 6);
                    model.setValueAt("no", row, 7);
                } else{
                    model.setValueAt(userCall.getByCallID(ciList.get(row).getId()).isPaid(), row, 6);
                    if (userCall.getByCallID(ciList.get(row).getId()).getPayDate() == null) {
                        model.setValueAt(null, row, 7);
                    } else {
                        model.setValueAt(userCall.getByCallID(ciList.get(row).getId()).getPayDate().toLocalDateTime().format(DateTimeFormatter.ISO_DATE), row, 7);
                    }
                }

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            DialogUtil.showError("Ошибка взятия данных из БД");
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
