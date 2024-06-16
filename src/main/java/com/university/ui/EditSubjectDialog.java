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
        setSize(350, 200);
        setLocationRelativeTo(parent);

        add(new JLabel("Subject Name:"));
        subjectNameField = new JTextField(subject.getSubjectName());
        add(subjectNameField);

        add(new JLabel("Lecturer:"));
        lecturerComboBox = new JComboBox<>();
        loadLecturers(subject.getLecturer());  // Nạp giảng viên vào combo box
        add(lecturerComboBox);

        add(new JLabel("Credits:"));
        creditsField = new JTextField(String.valueOf(subject.getCredits()));
        add(creditsField);

        btnEditSubject = new JButton("Edit Subject");
        btnEditSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSubject();
            }
        });
        add(new JLabel());  // Chừa khoảng trống
        add(btnEditSubject);
    }

    private void loadLecturers(Lecturer selectedLecturer) {
        LecturerDAO lecturerDAO = new LecturerDAO();
        List<Lecturer> lecturers = lecturerDAO.getAllLecturers();
        for (Lecturer lecturer : lecturers) {
            lecturerComboBox.addItem(lecturer);  // Sử dụng toString để hiển thị
        }
        lecturerComboBox.setSelectedItem(selectedLecturer);  // Đặt giảng viên được chọn
    }

    private void editSubject() {
        String subjectName = subjectNameField.getText();
        Lecturer selectedLecturer = (Lecturer) lecturerComboBox.getSelectedItem();
        int credits = Integer.parseInt(creditsField.getText());

        if (selectedLecturer != null) {
            subject.setSubjectName(subjectName);
            subject.setLecturer(selectedLecturer);
            subject.setCredits(credits);
            new SubjectDAO().updateSubject(subject);
            JOptionPane.showMessageDialog(this, "Subject updated successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a lecturer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
