package main.java.com.university.ui;

import main.java.com.university.dao.LecturerDAO;
import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddSubjectDialog extends JDialog {
    private JTextField customSubjectIDField;
    private JTextField subjectNameField;
    private JTextField creditsField;
    private JComboBox<Lecturer> lecturerComboBox;
    private JButton btnAddSubject;

    public AddSubjectDialog(Frame parent) {
        super(parent, "Add Subject", true);
        setLayout(new GridBagLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Custom Subject ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        customSubjectIDField = new JTextField();
        add(customSubjectIDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Subject Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        subjectNameField = new JTextField();
        add(subjectNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Credits:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        creditsField = new JTextField();
        add(creditsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Lecturer:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        lecturerComboBox = new JComboBox<>();
        // Load lecturers into the combo box
        for (Lecturer lecturer : new LecturerDAO().getAllLecturers()) {
            lecturerComboBox.addItem(lecturer);
        }
        add(lecturerComboBox, gbc);

        btnAddSubject = new JButton("Add Subject");
        btnAddSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSubject();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnAddSubject, gbc);
    }

    private void addSubject() {
        String customSubjectIDStr = customSubjectIDField.getText();
        int customSubjectID;
        try {
            customSubjectID = Integer.parseInt(customSubjectIDStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Custom Subject ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String subjectName = subjectNameField.getText();
        String creditsStr = creditsField.getText();
        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Credits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Lecturer lecturer = (Lecturer) lecturerComboBox.getSelectedItem();

        // Auto-generated subjectID is handled inside DAO
        Subject subject = new Subject(0, customSubjectID, subjectName, credits, lecturer);
        new SubjectDAO().saveSubject(subject);

        JOptionPane.showMessageDialog(this, "Subject added successfully!");
        dispose();
    }
}
