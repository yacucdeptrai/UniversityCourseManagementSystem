package main.java.com.university.ui;

import main.java.com.university.dao.EnrollmentDAO;
import main.java.com.university.dao.StudentDAO;
import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Student;
import main.java.com.university.model.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageEnrollmentUI extends JFrame {
    private JComboBox<Student> studentComboBox;
    private JComboBox<Subject> subjectComboBox;

    public ManageEnrollmentUI() {
        setTitle("Manage Enrollments");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblSelectStudent = new JLabel("Select Student:");
        lblSelectStudent.setBounds(10, 20, 100, 25);
        getContentPane().add(lblSelectStudent);

        studentComboBox = new JComboBox<>();
        studentComboBox.setBounds(120, 20, 250, 25);
        getContentPane().add(studentComboBox);

        JLabel lblSelectSubject = new JLabel("Select Subject:");
        lblSelectSubject.setBounds(10, 60, 100, 25);
        getContentPane().add(lblSelectSubject);

        subjectComboBox = new JComboBox<>();
        subjectComboBox.setBounds(120, 60, 250, 25);
        getContentPane().add(subjectComboBox);

        JButton btnEnroll = new JButton("Enroll");
        btnEnroll.setBounds(10, 100, 100, 25);
        getContentPane().add(btnEnroll);

        btnEnroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student selectedStudent = (Student) studentComboBox.getSelectedItem();
                Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
                if (selectedStudent != null && selectedSubject != null) {
                    EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
                    enrollmentDAO.enrollStudent(selectedStudent.getStudentID(), selectedSubject.getSubjectID());
                    JOptionPane.showMessageDialog(null, "Enrolled successfully!");
                }
            }
        });

        loadStudents();
        loadSubjects();

        frame.setVisible(true);
    }

    private void loadStudents() {
        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            studentComboBox.addItem(student);
        }
    }

    private void loadSubjects() {
        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects = subjectDAO.getAllSubjects();
        for (Subject subject : subjects) {
            subjectComboBox.addItem(subject);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManageEnrollmentUI frame = new ManageEnrollmentUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

