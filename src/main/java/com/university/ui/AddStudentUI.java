package main.java.com.university.ui;

import main.java.com.university.dao.PersonDAO;
import main.java.com.university.dao.StudentDAO;
import main.java.com.university.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AddStudentUI extends JFrame {
    private JTextField nameField;
    private JTextField dobField;
    private JTextField genderField;
    private JTextField studentIDField;

    public AddStudentUI() {
        setTitle("Add New Student");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(10, 20, 80, 25);
        getContentPane().add(lblName);

        nameField = new JTextField();
        nameField.setBounds(100, 20, 250, 25);
        getContentPane().add(nameField);

        JLabel lblDob = new JLabel("Date of Birth (YYYY-MM-DD):");
        lblDob.setBounds(10, 60, 200, 25);
        getContentPane().add(lblDob);

        dobField = new JTextField();
        dobField.setBounds(220, 60, 130, 25);
        getContentPane().add(dobField);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(10, 100, 80, 25);
        getContentPane().add(lblGender);

        genderField = new JTextField();
        genderField.setBounds(100, 100, 250, 25);
        getContentPane().add(genderField);

        JLabel lblStudentID = new JLabel("Student ID:");
        lblStudentID.setBounds(10, 140, 80, 25);
        getContentPane().add(lblStudentID);

        studentIDField = new JTextField();
        studentIDField.setBounds(100, 140, 250, 25);
        getContentPane().add(studentIDField);

        JButton btnAddStudent = new JButton("Add Student");
        btnAddStudent.setBounds(10, 180, 140, 25);
        getContentPane().add(btnAddStudent);

        btnAddStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                LocalDate dob = LocalDate.parse(dobField.getText());
                String gender = genderField.getText();
                int studentID = Integer.parseInt(studentIDField.getText());

                Student student = new Student(name, dob, gender, studentID);
                PersonDAO personDAO = new PersonDAO();
                StudentDAO studentDAO = new StudentDAO();

                // Kiểm tra ID đã tồn tại
                if (studentDAO.getStudentById(studentID) != null) {
                    JOptionPane.showMessageDialog(null, "Student ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int personID = personDAO.savePerson(student);
                student.setId(personID);
                studentDAO.saveStudent(student);

                JOptionPane.showMessageDialog(null, "Student added successfully!");
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AddStudentUI frame = new AddStudentUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
