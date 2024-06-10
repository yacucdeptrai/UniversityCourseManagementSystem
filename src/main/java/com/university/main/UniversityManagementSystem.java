package main.java.com.university.main;

import main.java.com.university.ui.AddStudentUI;
import main.java.com.university.ui.StudentManagementUI;
import main.java.com.university.ui.AddLecturerUI;
import main.java.com.university.ui.AddSubjectUI;

import javax.swing.*;

public class UniversityManagementSystem {
    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("University Management System");
        mainFrame.setSize(420, 200);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);

        JButton btnAddStudent = new JButton("Add Student");
        btnAddStudent.setBounds(10, 10, 160, 30);
        mainFrame.add(btnAddStudent);

        JButton btnEditStudent = new JButton("Manage Student");
        btnEditStudent.setBounds(10, 50, 160, 30);
        mainFrame.add(btnEditStudent);

        JButton btnAddLecturer = new JButton("Add Lecturer");
        btnAddLecturer.setBounds(200, 10, 160, 30);
        mainFrame.add(btnAddLecturer);

        JButton btnAddSubject = new JButton("Add Subject");
        btnAddSubject.setBounds(200, 50, 160, 30);
        mainFrame.add(btnAddSubject);

        btnAddStudent.addActionListener(e -> {
            AddStudentUI addStudentUI = new AddStudentUI();
            addStudentUI.setVisible(true);
        });

        btnEditStudent.addActionListener(e -> {
            StudentManagementUI studentManagementUI = new StudentManagementUI();
            studentManagementUI.setVisible(true);
        });

        btnAddLecturer.addActionListener(e -> {
            AddLecturerUI addLecturerUI = new AddLecturerUI();
            addLecturerUI.setVisible(true);
        });

        btnAddSubject.addActionListener(e -> {
            AddSubjectUI addSubjectUI = new AddSubjectUI();
            addSubjectUI.setVisible(true);
        });

        mainFrame.setVisible(true);
    }
}
