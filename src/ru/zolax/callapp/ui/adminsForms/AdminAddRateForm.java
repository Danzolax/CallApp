package ru.zolax.callapp.ui.adminsForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.MySqlDatabase;
import ru.zolax.callapp.database.entities.RateEntity;
import ru.zolax.callapp.database.entities.ZoneCodeNameEntity;
import ru.zolax.callapp.database.managers.RateEntityManager;
import ru.zolax.callapp.database.managers.ZoneCodeNameEntityManager;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class AdminAddRateForm extends BaseForm {
    private final MySqlDatabase db = Application.getInstance().getDatabase();
    private final RateEntityManager rates = new RateEntityManager(db);
    private final ZoneCodeNameEntityManager zoneCodes = new ZoneCodeNameEntityManager(db);

    private JTextField pricePerMinuteTextField;
    private JButton backButton;
    private JButton addButton;
    private JComboBox<String> callCodeComboBox;
    private JComboBox<String> userCodecomboBox;
    private JPanel mainPanel;

    public AdminAddRateForm() {
        setContentPane(mainPanel);
        setVisible(true);
        initComboBoxAndButtons();
    }


    private void initComboBoxAndButtons() {
        backButton.addActionListener(e -> {
            dispose();
            new AdminRatesTableForm();
        });
        try {
            List<ZoneCodeNameEntity> zoneCodeNameEntityList = zoneCodes.getAll();
            zoneCodeNameEntityList.forEach(e -> {
                callCodeComboBox.addItem(e.getZoneCode() + " - " + e.getZoneName());
                userCodecomboBox.addItem(e.getZoneCode() + " - " + e.getZoneName());
            });
            addButton.addActionListener(e -> {
                boolean errorFlag = false;
                try {
                    ZoneCodeNameEntity userCodeFK = zoneCodeNameEntityList.get(callCodeComboBox.getSelectedIndex());
                    ZoneCodeNameEntity callCodeFK = zoneCodeNameEntityList.get(userCodecomboBox.getSelectedIndex());
                    double pricePerMinute = Double.parseDouble(pricePerMinuteTextField.getText());
                    if (pricePerMinute == 0){
                        throw new NumberFormatException("zero in price");
                    }
                    RateEntity newRate = new RateEntity(userCodeFK.getZoneCode(), callCodeFK.getZoneCode(), pricePerMinute);
                    rates.add(newRate);
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                } catch (NumberFormatException numberFormatException){
                    numberFormatException.printStackTrace();
                    DialogUtil.showError("Неправильный формат цены");
                    errorFlag = true;
                }
                if (!errorFlag){
                    dispose();
                    new AdminRatesTableForm();
                }

            });
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    private void initButtons() {

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
