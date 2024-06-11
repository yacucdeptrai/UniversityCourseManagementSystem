package main.java.com.university.ui;

import main.java.com.university.dao.*;
import main.java.com.university.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddSubjectDialog extends JDialog {
    private JTextField subjectNameField;
    private JTextField subjectIdField;
    private JComboBox<Lecturer> lecturerComboBox;
    private JTextField creditsField;
    private JButton btnAddSubject;

    public AddSubjectDialog(Frame parent) {
        super(parent, "Add Subject", true);
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JLabel lblSubjectName = new JLabel("Subject Name:");
        subjectNameField = new JTextField();
        add(lblSubjectName);
        add(subjectNameField);

        JLabel lblSubjectId = new JLabel("Subject ID:");
        subjectIdField = new JTextField();
        add(lblSubjectId);
        add(subjectIdField);

        JLabel lblLecturer = new JLabel("Select Lecturer:");
        lecturerComboBox = new JComboBox<>();
        loadLecturers();
        add(lblLecturer);
        add(lecturerComboBox);

        JLabel lblCredits = new JLabel("Credits:");
        creditsField = new JTextField();
        add(lblCredits);
        add(creditsField);

        btnAddSubject = new JButton("Add Subject");
        btnAddSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSubject();
            }
        });
        add(new JLabel());
        add(btnAddSubject);
    }

    private void loadLecturers() {
        List<Lecturer> lecturers = new LecturerDAO().getAllLecturers();
        for (Lecturer lecturer : lecturers) {
            lecturerComboBox.addItem(lecturer);
        }
    }

    private void addSubject() {
        String subjectName = subjectNameField.getText();
        int subjectId = Integer.parseInt(subjectIdField.getText());
        Lecturer lecturer = (Lecturer) lecturerComboBox.getSelectedItem();
        int credits = Integer.parseInt(creditsField.getText());

        Subject subject = new Subject(subjectId, subjectName, lecturer, credits);
        new SubjectDAO().saveSubject(subject);

        JOptionPane.showMessageDialog(this, "Subject added successfully!");
        dispose();
    }
}
