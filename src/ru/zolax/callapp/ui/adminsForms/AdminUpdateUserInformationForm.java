package ru.zolax.callapp.ui.adminsForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.database.entities.UserInformationEntity;
import ru.zolax.callapp.database.managers.UserInformationEntityManager;
import ru.zolax.callapp.util.BaseForm;
import ru.zolax.callapp.util.DialogUtil;

import javax.swing.*;
import java.sql.SQLException;

public class AdminUpdateUserInformationForm extends BaseForm {
    private UserInformationEntity user;
    private final UserInformationEntityManager userManager = new UserInformationEntityManager(Application.getInstance().getDatabase());

    private JPanel mainPanel;
    private JTextField nameField;
    private JTextField telephoneField;
    private JTextField cityField;
    private JTextField addressField;
    private JButton updateButton;
    private JButton backButton;

    public AdminUpdateUserInformationForm(UserInformationEntity user) {
        this.user = user;
        setContentPane(mainPanel);
        setVisible(true);
        initButtons();
        initUI();
    }

    private void initUI() {
        nameField.setText(user.getName());
        telephoneField.setText(Integer.toString(user.getTelephoneNumber()));
        cityField.setText(user.getCity());
        addressField.setText(user.getAddress());
    }

    private void initButtons() {
        backButton.addActionListener(e -> {
            dispose();
            new AdminUsersInformationTableForm();
        });

        updateButton.addActionListener(e -> {
            if (DialogUtil.showConfirm("Вы точно хотите изменить информацию о пользователе?")) {
                if (!nameField.getText().equals("") &&
                        !telephoneField.getText().equals("") &&
                        !cityField.getText().equals("") &&
                        !addressField.getText().equals("")) {
                    user.setName(nameField.getText());
                    try {
                        user.setTelephoneNumber(Integer.parseInt(telephoneField.getText()));
                    } catch (NumberFormatException exception){
                        exception.printStackTrace();
                        DialogUtil.showError("Неправильный формат телефона");
                    }
                    user.setCity(cityField.getText());
                    user.setAddress(addressField.getText());
                    try {
                        userManager.update(user);
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                } else {
                    DialogUtil.showError("Пустое поле!");
                }
                dispose();
                new AdminUsersInformationTableForm();
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
