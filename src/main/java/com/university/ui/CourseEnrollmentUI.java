package main.java.com.university.ui;

import main.java.com.university.dao.StudentDAO;
import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Student;
import main.java.com.university.model.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CourseEnrollmentUI {
    private JFrame frame;
    private JComboBox<Student> studentComboBox;
    private JComboBox<Subject> subjectComboBox;

    public CourseEnrollmentUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Course Enrollment");
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblSelectStudent = new JLabel("Select Student:");
        lblSelectStudent.setBounds(10, 20, 100, 25);
        frame.getContentPane().add(lblSelectStudent);

        studentComboBox = new JComboBox<>();
        studentComboBox.setBounds(120, 20, 300, 25);
        frame.getContentPane().add(studentComboBox);

        JLabel lblSelectSubject = new JLabel("Select Subject:");
        lblSelectSubject.setBounds(10, 60, 100, 25);
        frame.getContentPane().add(lblSelectSubject);

        subjectComboBox = new JComboBox<>();
        subjectComboBox.setBounds(120, 60, 300, 25);
        frame.getContentPane().add(subjectComboBox);

        JButton btnEnroll = new JButton("Enroll");
        btnEnroll.setBounds(10, 100, 100, 25);
        frame.getContentPane().add(btnEnroll);

        btnEnroll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student selectedStudent = (Student) studentComboBox.getSelectedItem();
                Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
                if (selectedStudent != null && selectedSubject != null) {
                    System.out.println("Enrolling " + selectedStudent.getName() + " in " + selectedSubject.getSubjectName());
                    // Logic để đăng ký sinh viên vào khóa học
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
                    CourseEnrollmentUI window = new CourseEnrollmentUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
