package main.java.com.university.ui;

import main.java.com.university.dao.StudentDAO;
import main.java.com.university.dao.PersonDAO;
import main.java.com.university.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AddStudentUI extends JFrame {
    private JTextField nameField;
    private JTextField dobField;
    private JComboBox<String> genderComboBox;
    private JLabel studentIDLabel;

    public AddStudentUI() {
        setTitle("Add New Student");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(10, 20, 80, 25);
        getContentPane().add(lblName);

        nameField = new JTextField();
        nameField.setBounds(120, 20, 250, 25);
        getContentPane().add(nameField);

        JLabel lblDob = new JLabel("Date of Birth (YYYY-MM-DD):");
        lblDob.setBounds(10, 60, 200, 25);
        getContentPane().add(lblDob);

        dobField = new JTextField();
        dobField.setBounds(220, 60, 150, 25);
        getContentPane().add(dobField);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(10, 100, 80, 25);
        getContentPane().add(lblGender);

        genderComboBox = new JComboBox<>(new String[] {"Male", "Female"});
        genderComboBox.setBounds(120, 100, 250, 25);
        getContentPane().add(genderComboBox);

        JLabel lblStudentID = new JLabel("Student ID:");
        lblStudentID.setBounds(10, 140, 80, 25);
        getContentPane().add(lblStudentID);

        studentIDLabel = new JLabel();
        studentIDLabel.setBounds(120, 140, 250, 25);
        getContentPane().add(studentIDLabel);

        JButton btnAddStudent = new JButton("Add Student");
        btnAddStudent.setBounds(10, 180, 140, 25);
        getContentPane().add(btnAddStudent);

        btnAddStudent.addActionListener(e -> addStudent());

        setVisible(true);
    }

    private void addStudent() {
        String name = nameField.getText();
        LocalDate dob = LocalDate.parse(dobField.getText());
        String gender = (String) genderComboBox.getSelectedItem();

        Student student = new Student(name, dob, gender, 0); // Student ID sẽ được tự động sinh bởi cơ sở dữ liệu
        PersonDAO personDAO = new PersonDAO();
        StudentDAO studentDAO = new StudentDAO();

        int personId = personDAO.savePerson(student);
        student.setId(personId);
        int studentId = studentDAO.saveStudentAndReturnID(student);

        studentIDLabel.setText(String.valueOf(studentId)); // Hiển thị Student ID sau khi thêm thành công

        JOptionPane.showMessageDialog(this, "Student added successfully!");
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
