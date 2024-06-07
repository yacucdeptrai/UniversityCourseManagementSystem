package main.java.com.university.main;

import main.java.com.university.ui.AddStudentUI;
import main.java.com.university.ui.EditStudentUI;
import main.java.com.university.ui.AddLecturerUI;
import main.java.com.university.ui.AddSubjectUI;
import main.java.com.university.ui.ManageEnrollmentUI;
import main.java.com.university.dao.StudentDAO;
import main.java.com.university.model.Student;

import javax.swing.*;

public class UniversityManagementSystem {
    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("University Management System");
        mainFrame.setSize(600, 400);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);

        JButton btnAddStudent = new JButton("Add Student");
        btnAddStudent.setBounds(10, 10, 160, 30);
        mainFrame.add(btnAddStudent);

        JButton btnEditStudent = new JButton("Edit Student");
        btnEditStudent.setBounds(10, 50, 160, 30);
        mainFrame.add(btnEditStudent);

        JButton btnAddLecturer = new JButton("Add Lecturer");
        btnAddLecturer.setBounds(200, 10, 160, 30);
        mainFrame.add(btnAddLecturer);

        JButton btnAddSubject = new JButton("Add Subject");
        btnAddSubject.setBounds(200, 50, 160, 30);
        mainFrame.add(btnAddSubject);

        JButton btnManageEnrollment = new JButton("Manage Enrollment");
        btnManageEnrollment.setBounds(400, 10, 160, 30);
        mainFrame.add(btnManageEnrollment);

        btnAddStudent.addActionListener(e -> {
            AddStudentUI addStudentUI = new AddStudentUI();
            addStudentUI.setVisible(true);
        });

        btnEditStudent.addActionListener(e -> {
            StudentDAO studentDAO = new StudentDAO();
            int studentID = 999; // Lấy studentID từ đầu vào người dùng hoặc một cách khác
            Student student = studentDAO.getStudentById(studentID);
            if (student != null) {
                EditStudentUI editStudentUI = new EditStudentUI(student);
                editStudentUI.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Student not found with ID: " + studentID, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAddLecturer.addActionListener(e -> {
            AddLecturerUI addLecturerUI = new AddLecturerUI();
            addLecturerUI.setVisible(true);
        });

        btnAddSubject.addActionListener(e -> {
            AddSubjectUI addSubjectUI = new AddSubjectUI();
            addSubjectUI.setVisible(true);
        });

        btnManageEnrollment.addActionListener(e -> {
            ManageEnrollmentUI manageEnrollmentUI = new ManageEnrollmentUI();
            manageEnrollmentUI.setVisible(true);
        });

        mainFrame.setVisible(true);
    }
}
