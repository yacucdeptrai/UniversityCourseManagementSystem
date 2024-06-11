package main.java.com.university.ui;

import main.java.com.university.dao.LecturerDAO;
import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EditSubjectDialog extends JDialog {
    private JTextField subjectNameField;
    private JComboBox<Lecturer> lecturerComboBox;
    private JTextField creditsField;
    private JButton btnEditSubject;

    private Subject subject;

    public EditSubjectDialog(Frame parent, Subject subject) {
        super(parent, "Edit Subject", true);
        this.subject = subject;
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JLabel lblSubjectName = new JLabel("Subject Name:");
        subjectNameField = new JTextField(subject.getSubjectName());
        add(lblSubjectName);
        add(subjectNameField);

        JLabel lblLecturer = new JLabel("Select Lecturer:");
        lecturerComboBox = new JComboBox<>();
        loadLecturers();
        lecturerComboBox.setSelectedItem(subject.getLecturer());
        add(lblLecturer);
        add(lecturerComboBox);

        JLabel lblCredits = new JLabel("Credits:");
        creditsField = new JTextField(String.valueOf(subject.getCredits()));
        add(lblCredits);
        add(creditsField);

        btnEditSubject = new JButton("Edit Subject");
        btnEditSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSubject();
            }
        });
        add(new JLabel());
        add(btnEditSubject);
    }

    private void loadLecturers() {
        List<Lecturer> lecturers = new LecturerDAO().getAllLecturers();
        for (Lecturer lecturer : lecturers) {
            lecturerComboBox.addItem(lecturer);
        }
    }

    private void editSubject() {
        String subjectName = subjectNameField.getText();
        Lecturer lecturer = (Lecturer) lecturerComboBox.getSelectedItem();
        int credits = Integer.parseInt(creditsField.getText());

        subject.setSubjectName(subjectName);
        subject.setLecturer(lecturer);
        subject.setCredits(credits);
        new SubjectDAO().updateSubject(subject);

        JOptionPane.showMessageDialog(this, "Subject updated successfully!");
        dispose();
    }
}
