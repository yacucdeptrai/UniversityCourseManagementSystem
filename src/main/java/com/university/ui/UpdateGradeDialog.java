package main.java.com.university.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateGradeDialog extends JDialog {
    private JTextField gradeField;
    private boolean confirmed = false;

    public UpdateGradeDialog(JDialog parent, String subjectName, String currentGrade) {
        super(parent, "Update Grade", true);
        setLayout(new BorderLayout());
        setSize(200, 130);
        setLocationRelativeTo(parent);

        JLabel lblMessage = new JLabel("Enter new grade for " + subjectName + ":");
        lblMessage.setHorizontalAlignment(JLabel.CENTER);
        add(lblMessage, BorderLayout.NORTH);

        gradeField = new JTextField(currentGrade, 10);
        JPanel inputPanel = new JPanel();
        inputPanel.add(gradeField);
        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Cancel");

        btnOk.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        btnCancel.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public double getGrade() throws NumberFormatException {
        return Double.parseDouble(gradeField.getText());
    }
}
