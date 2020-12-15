package ru.zolax.callapp.ui.usersForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.RateEntity;
import ru.zolax.callapp.database.managers.RateEntityManager;
import ru.zolax.callapp.database.managers.ZoneCodeNameEntityManager;
import ru.zolax.callapp.ui.usersForms.MainForm;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.ObjectTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class RatesTableForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final RateEntityManager rates = new RateEntityManager(db);
    private final ZoneCodeNameEntityManager zoneCodes = new ZoneCodeNameEntityManager(db);

    private ObjectTableModel<RateEntity> model;

    private JPanel mainPanel;
    private JTable ratesTable;
    private JButton backButton;

    public RatesTableForm() {
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
        ratesTable.getTableHeader().setReorderingAllowed(false);
        ratesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && ratesTable.getSelectedRow() != -1) {
                    int row = ratesTable.rowAtPoint(e.getPoint());
                    System.out.println(model.getRowEntity(row));
                }
            }
        });

        model = new ObjectTableModel<RateEntity>() {
            private int counter = 0;
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
                counter++;
                return new Object[]{
                        counter,
                        entity.getId(),
                        entity.getUserZoneCodeFK(),
                        entity.getCallZoneCodeFK(),
                        entity.getPricePerMinute()
                };
            }
        };
        ratesTable.setModel(model);
        model.addColumn("Номер");
        model.addColumn("ID тарифа");
        model.addColumn("Код зоны звонящего");
        model.addColumn("Код зоны отвечающего");
        model.addColumn("Цена за минуту(руб)");

        ratesTable.removeColumn(ratesTable.getColumnModel().getColumn(1));


        try {
            model.addRowEntities(rates.getAll());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


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
