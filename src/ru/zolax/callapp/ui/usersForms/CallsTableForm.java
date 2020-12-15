package ru.zolax.callapp.ui.usersForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.CallInformationEntity;
import ru.zolax.callapp.database.entities.CashInformationEntity;
import ru.zolax.callapp.database.managers.CallInformationEntityManager;
import ru.zolax.callapp.database.managers.CashInformationEntityManager;
import ru.zolax.callapp.database.managers.UserCallEntityManager;
import ru.zolax.callapp.database.managers.UserInformationEntityManager;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;
import ru.zolax.callapp.util.ObjectTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CallsTableForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final CallInformationEntityManager callsInformation = new CallInformationEntityManager(db);
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(db);
    private final UserCallEntityManager userCall = new UserCallEntityManager(db);
    private final CashInformationEntityManager cashManager = new CashInformationEntityManager(db);
    private JPanel mainPanel;
    private JTable table;
    private JButton backButton;
    private JButton payAllCalsButton;

    private ObjectTableModel<CallInformationEntity> model;

    public CallsTableForm() {
        setContentPane(mainPanel);
        initTable();
        initButtons();
        setVisible(true);
    }

    private void initButtons() {
        backButton.addActionListener(e -> {
            dispose();
            new MainForm();
        });
    }

    private void initTable() {

        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    if (DialogUtil.showConfirm("Вы хотите оплатить звонок?")) {
                        int row = table.rowAtPoint(e.getPoint());
                        payCall(row);
                    }
                }
            }
        });
        payAllCalsButton.addActionListener(e -> {
            if (DialogUtil.showConfirm("Вы хотите оплатить все звонки?")) {
                try {
                    CashInformationEntity cash = cashManager.getById(userManager.getById(Application.getCurrentAccount().getUserFK()).getCashInformationFK());
                    double money = 0;
                    for (int row = model.getRowCount() - 1; row > -1; row--) {
                        money += callsInformation.getById(model.getRowEntity(row).getId()).getPrice();
                    }
                    if ((cash.getBalance() - money) >= 0){
                        for (int row = model.getRowCount() - 1; row > -1; row--) {
                            if (!userCall.getByCallID(model.getRowEntity(row).getId()).isPaid()) {
                                payCall(row);
                            }
                        }
                    } else{
                        DialogUtil.showError("Недостаточно средств на балансе");
                    }


                } catch (SQLException error) {
                    error.printStackTrace();
                }

            }
        });

        model = new ObjectTableModel<>() {
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
        model.addColumn("Длительность звонка(мин)");
        model.addColumn("Дата звонка");
        model.addColumn("Цена за весь звонок(руб)");
        model.addColumn("Статус оплаты звонка");
        model.addColumn("Дата оплаты звонка");
        fillData();
        table.removeColumn(table.getColumnModel().getColumn(1));
        table.removeColumn(table.getColumnModel().getColumn(1));

    }

    private void payCall(int row) {
        try {
            CashInformationEntity cash = cashManager.getById(userManager.getById(Application.getCurrentAccount().getUserFK()).getCashInformationFK());
            if(!userCall.getByCallID(callsInformation.getById(model.getRowEntity(row).getId()).getId()).isPaid()){
                double money = cash.getBalance() - callsInformation.getById(model.getRowEntity(row).getId()).getPrice();
                boolean moneyFlag = false;
                if (money >= 0) {
                    cash.setBalance(money);
                    cashManager.update(cash);
                    moneyFlag = true;
                } else {
                    DialogUtil.showError("Недостаточно средств на балансе");
                }
                if (userCall.payCall(model.getRowEntity(row).getId(), moneyFlag)) {
                    fillData();
                }
            } else{
                DialogUtil.showError("Данный звонок уже оплачен!");
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            DialogUtil.showError("Ошибка взятия данных из БД");
        }
    }

    public void fillData() {
        model.setCounter(0);
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }
        try {
            List<CallInformationEntity> ciList = callsInformation.getAllByUserID(Application.getCurrentAccount().getUserFK());
            model.addRowEntities(ciList);
            for (int row = 0; row < ciList.size(); row++) {
                model.setValueAt(userCall.getByCallID(ciList.get(row).getId()).isPaid(), row, 6);
                if (userCall.getByCallID(ciList.get(row).getId()).getPayDate() == null) {
                    model.setValueAt(null, row, 7);
                } else {
                    model.setValueAt(userCall.getByCallID(ciList.get(row).getId()).getPayDate().toLocalDateTime().format(DateTimeFormatter.ISO_DATE), row, 7);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            DialogUtil.showError("Ошибка взятия данных из БД");
        }


    }


    @Override
    public int getFormWidth() {
        return 1000;
    }

    @Override
    public int getFormHeight() {
        return 500;
    }
}
