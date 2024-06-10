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

public class AddSubjectUI extends JFrame {
    private JTextField subjectNameField;
    private JTextField subjectIDField;
    private JComboBox<Lecturer> lecturerComboBox;

    public AddSubjectUI() {
        setTitle("Add New Subject");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblSubjectName = new JLabel("Subject Name:");
        lblSubjectName.setBounds(10, 20, 100, 25);
        getContentPane().add(lblSubjectName);

        subjectNameField = new JTextField();
        subjectNameField.setBounds(120, 20, 250, 25);
        getContentPane().add(subjectNameField);

        JLabel lblSubjectID = new JLabel("Subject ID:");
        lblSubjectID.setBounds(10, 60, 100, 25);
        getContentPane().add(lblSubjectID);

        subjectIDField = new JTextField();
        subjectIDField.setBounds(120, 60, 250, 25);
        getContentPane().add(subjectIDField);

        JLabel lblSelectLecturer = new JLabel("Select Lecturer:");
        lblSelectLecturer.setBounds(10, 100, 100, 25);
        getContentPane().add(lblSelectLecturer);

        lecturerComboBox = new JComboBox<>();
        lecturerComboBox.setBounds(120, 100, 250, 25);
        getContentPane().add(lecturerComboBox);

        JButton btnAddSubject = new JButton("Add Subject");
        btnAddSubject.setBounds(10, 140, 140, 25);
        getContentPane().add(btnAddSubject);

        btnAddSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subjectName = subjectNameField.getText();
                int subjectID;
                try {
                    subjectID = Integer.parseInt(subjectIDField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid Subject ID", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Lecturer selectedLecturer = (Lecturer) lecturerComboBox.getSelectedItem();

                if (selectedLecturer != null) {
                    Subject subject = new Subject(subjectID, subjectName, selectedLecturer);
                    SubjectDAO subjectDAO = new SubjectDAO();

                    // Kiểm tra ID đã tồn tại
                    if (subjectDAO.getSubjectById(subjectID) != null) {
                        JOptionPane.showMessageDialog(null, "Subject ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    subjectDAO.saveSubject(subject);
                    JOptionPane.showMessageDialog(null, "Subject added successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a lecturer", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadLecturers();
    }

    private void loadLecturers() {
        LecturerDAO lecturerDAO = new LecturerDAO();
        List<Lecturer> lecturers = lecturerDAO.getAllLecturers();
        for (Lecturer lecturer : lecturers) {
            lecturerComboBox.addItem(lecturer);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AddSubjectUI frame = new AddSubjectUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
