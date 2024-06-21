package main.java.com.university.ui;

import main.java.com.university.dao.StudentDAO;
import main.java.com.university.model.Student;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddStudentDialog extends JDialog {
    private JTextField nameField;
    private JDateChooser dateChooser;
    private JComboBox<String> genderComboBox;
    private JTextField studentIdField;
    private JButton btnAddStudent;

    public AddStudentDialog(Frame parent) {
        super(parent, "Add Student", true);
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JLabel lblStudentId = new JLabel("Student ID:");
        studentIdField = new JTextField();
        add(lblStudentId);
        add(studentIdField);

        JLabel lblName = new JLabel("Name:");
        nameField = new JTextField();
        add(lblName);
        add(nameField);

        JLabel lblDob = new JLabel("Date of Birth:");
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        add(lblDob);
        add(dateChooser);

        JLabel lblGender = new JLabel("Gender:");
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        add(lblGender);
        add(genderComboBox);

        btnAddStudent = new JButton("Add Student");
        btnAddStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });
        add(new JLabel());
        add(btnAddStudent);
    }

    private void addStudent() {
        String name = nameField.getText();
        Date selectedDate = dateChooser.getDate();
        LocalDate dateOfBirth = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String gender = (String) genderComboBox.getSelectedItem();
        int studentId = Integer.parseInt(studentIdField.getText());

        Student student = new Student(studentId, name, dateOfBirth, gender);
        new StudentDAO().saveStudent(student);

        JOptionPane.showMessageDialog(this, "Student added successfully!");
        dispose();
    }
}
