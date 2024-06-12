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

public class AddSubjectDialog extends JDialog {
    private JTextField subjectNameField;
    private JComboBox<Lecturer> lecturerComboBox;
    private JTextField creditsField;
    private JButton btnAddSubject;

    public AddSubjectDialog(Frame parent) {
        super(parent, "Add Subject", true);
        setLayout(new GridLayout(5, 2, 5, 5));
        setSize(350, 180);
        setLocationRelativeTo(parent);

        add(new JLabel("Subject Name:"));
        subjectNameField = new JTextField();
        add(subjectNameField);

        add(new JLabel("Lecturer:"));
        lecturerComboBox = new JComboBox<>();
        loadLecturers();  // Nạp giảng viên vào combo box
        add(lecturerComboBox);

        add(new JLabel("Credits:"));
        creditsField = new JTextField();
        add(creditsField);

        btnAddSubject = new JButton("Add Subject");
        btnAddSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSubject();
            }
        });
        add(new JLabel());  // Chừa khoảng trống
        add(btnAddSubject);
    }

    private void loadLecturers() {
        LecturerDAO lecturerDAO = new LecturerDAO();
        List<Lecturer> lecturers = lecturerDAO.getAllLecturers();
        for (Lecturer lecturer : lecturers) {
            lecturerComboBox.addItem(lecturer);  // Sử dụng toString để hiển thị
        }
    }

    private void addSubject() {
        String subjectName = subjectNameField.getText();
        Lecturer selectedLecturer = (Lecturer) lecturerComboBox.getSelectedItem();
        int credits = Integer.parseInt(creditsField.getText());

        if (selectedLecturer != null) {
            Subject subject = new Subject(0, subjectName, selectedLecturer, credits);
            new SubjectDAO().saveSubject(subject);
            JOptionPane.showMessageDialog(this, "Subject added successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a lecturer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
