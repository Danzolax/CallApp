package ru.zolax.callapp.ui.adminsForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.ui.EntryForm;
import ru.zolax.callapp.util.BaseForm;

import javax.swing.*;

public class AdminMainForm extends BaseForm {
    private JPanel mainPanel;
    private JButton accountsButton;
    private JButton ratesButton;
    private JButton callsButtonButton;
    private JButton usersButton;
    private JButton backButton;
    private JButton usersCashButton;

    public AdminMainForm() {
        setContentPane(mainPanel);
        setVisible(true);
        initButtons();
    }

    private void initButtons() {
        backButton.addActionListener(e->{
            Application.setCurrentAccount(null);
            dispose();
            new EntryForm();
        });
        ratesButton.addActionListener(e->{
            dispose();
            new AdminRatesTableForm();
        });
        callsButtonButton.addActionListener(e->{
            dispose();
            new AdminCallsInformationTableForm();
        });
        usersCashButton.addActionListener(e->{
            dispose();
            new AdminCashInformationTableForm();
        });
        usersButton.addActionListener(e->{
            dispose();
            new AdminUsersInformationTableForm();
        });
        accountsButton.addActionListener(e->{
            dispose();
            new AdminAccountsTableForm();
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
