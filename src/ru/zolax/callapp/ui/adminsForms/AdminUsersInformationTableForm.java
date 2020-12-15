package ru.zolax.callapp.ui.adminsForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.UserInformationEntity;
import ru.zolax.callapp.database.managers.UserInformationEntityManager;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;
import ru.zolax.callapp.util.ObjectTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class AdminUsersInformationTableForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(db);

    private JPanel mainPanel;
    private JTable table;
    private JButton backButton;

    private ObjectTableModel<UserInformationEntity> model;


    public AdminUsersInformationTableForm() {
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
    }

    private void initTable() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.rowAtPoint(e.getPoint());
                    dispose();
                    try {
                        new AdminUpdateUserInformationForm(userManager.getAll().get(row));
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }
            }
        });
        table.getTableHeader().setReorderingAllowed(false);
        model = new ObjectTableModel<>() {
            @Override
            protected UserInformationEntity getEntityFromRowData(Object[] rowData) {
                return new UserInformationEntity(
                        (int) rowData[1],
                        (String) rowData[2],
                        (int) rowData[3],
                        (String) rowData[4],
                        (String) rowData[5],
                        (int) rowData[6]
                );
            }

            @Override
            protected Object[] getRowDataFromEntity(UserInformationEntity entity) {
                int counter = this.getCounter();
                this.setCounter(++counter);
                return new Object[]{
                        counter,
                        entity.getId(),
                        entity.getName(),
                        entity.getTelephoneNumber(),
                        entity.getAddress(),
                        entity.getCity(),
                        entity.getCashInformationFK()
                };
            }
        };
        table.setModel(model);
        model.addColumn("Номер");
        model.addColumn("ID абонента");
        model.addColumn("ФИО владельца");
        model.addColumn("Номер телефона");
        model.addColumn("Адрес");
        model.addColumn("Город");
        fillData();
        table.removeColumn(table.getColumnModel().getColumn(1));

    }

    private void fillData() {
        model.setCounter(0);
        try {
            model.addRowEntities(userManager.getAll());
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
