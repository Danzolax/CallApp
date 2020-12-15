package ru.zolax.callapp.ui.adminsForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.RateEntity;
import ru.zolax.callapp.database.managers.RateEntityManager;
import ru.zolax.callapp.database.managers.ZoneCodeNameEntityManager;
import ru.zolax.callapp.ui.usersForms.MainForm;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;
import ru.zolax.callapp.util.ObjectTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class AdminRatesTableForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final RateEntityManager rates = new RateEntityManager(db);
    private final ZoneCodeNameEntityManager zoneCodes = new ZoneCodeNameEntityManager(db);

    private JPanel mainPanel;
    private JTable table;
    private JButton backButton;
    private JButton addRateButton;

    private ObjectTableModel<RateEntity> model;


    public AdminRatesTableForm() {
        setContentPane(mainPanel);
        setVisible(true);
        initTable();
        initButtons();
    }

    private void initButtons() {
        backButton.addActionListener(e->{
            dispose();
            new AdminMainForm();
        });
        addRateButton.addActionListener(e->{
            dispose();
            new AdminAddRateForm();
        });
    }

    private void initTable() {
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    if (DialogUtil.showConfirm("Вы действительно хотите удалить тариф?")){
                        int row = table.rowAtPoint(e.getPoint());
                        deleteRate(row);
                    }
                }
            }
        });

        model = new ObjectTableModel<RateEntity>() {

            @Override
            protected RateEntity getEntityFromRowData(Object[] rowData) {
                return new RateEntity(
                        (int) rowData[1],
                        (int) rowData[2],
                        (int) rowData[3],
                        (double) rowData[4]
                );
            }

            @Override
            protected Object[] getRowDataFromEntity(RateEntity entity) {
                int counter = this.getCounter();
                this.setCounter(++counter);
                return new Object[]{
                        counter,
                        entity.getId(),
                        entity.getUserZoneCodeFK(),
                        entity.getCallZoneCodeFK(),
                        entity.getPricePerMinute()
                };
            }
        };


        table.setModel(model);
        model.addColumn("Номер");
        model.addColumn("ID");
        model.addColumn("Код зоны звонящего");
        model.addColumn("Код зоны отвечающего");
        model.addColumn("Цена за минуту");
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
            model.addRowEntities(rates.getAll());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void deleteRate(int row) {
        try {
            RateEntity entity = rates.getAll().get(row);
            rates.deleteById(entity.getId());
            fillData();
        } catch (SQLIntegrityConstraintViolationException sqlException) {
            sqlException.printStackTrace();
            DialogUtil.showError("Невозможно удалить тариф так как он привязан к конкретному звонку/конкретным звонкам");
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
