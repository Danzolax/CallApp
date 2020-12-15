package ru.zolax.callapp.ui.usersForms;

import ru.zolax.callapp.Application;
import ru.zolax.callapp.ui.EntryForm;
import ru.zolax.callapp.util.BaseForm;

import javax.swing.*;

public class MainForm extends BaseForm {
    private JButton accountInfoButton;
    private JButton cashButton;
    private JButton ratesButton;
    private JButton callsButton;
    private JPanel mainPanel;
    private JButton backButton;

    public MainForm() {
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
        accountInfoButton.addActionListener(e->{
            dispose();
            new AccountInformationForm();
        });
        cashButton.addActionListener(e->{
            dispose();
            new CashForm();
        });
        ratesButton.addActionListener(e->{
            dispose();
            new RatesTableForm();
        });
        callsButton.addActionListener(e->{
            dispose();
            new CallsTableForm();
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
