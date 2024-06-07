package main.java.com.university.ui;

import main.java.com.university.dao.StudentDAO;
import main.java.com.university.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class EditStudentUI extends JFrame {
    private JComboBox<Student> studentComboBox;
    private JTextField nameField;
    private JTextField dobField;
    private JComboBox<String> genderComboBox;

    public EditStudentUI() {
        setTitle("Edit Student");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblSelectStudent = new JLabel("Select Student:");
        lblSelectStudent.setBounds(10, 20, 100, 25);
        getContentPane().add(lblSelectStudent);

        studentComboBox = new JComboBox<>();
        studentComboBox.setBounds(120, 20, 250, 25);
        getContentPane().add(studentComboBox);

        studentComboBox.addActionListener(e -> loadStudentData());

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(10, 60, 80, 25);
        getContentPane().add(lblName);

        nameField = new JTextField();
        nameField.setBounds(120, 60, 250, 25);
        getContentPane().add(nameField);

        JLabel lblDob = new JLabel("Date of Birth (YYYY-MM-DD):");
        lblDob.setBounds(10, 100, 200, 25);
        getContentPane().add(lblDob);

        dobField = new JTextField();
        dobField.setBounds(220, 100, 150, 25);
        getContentPane().add(dobField);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(10, 140, 80, 25);
        getContentPane().add(lblGender);

        genderComboBox = new JComboBox<>(new String[] {"Male", "Female"});
        genderComboBox.setBounds(120, 140, 250, 25);
        getContentPane().add(genderComboBox);

        JButton btnEditStudent = new JButton("Edit Student");
        btnEditStudent.setBounds(10, 180, 140, 25);
        getContentPane().add(btnEditStudent);

        btnEditStudent.addActionListener(e -> updateStudent());

        JButton btnDeleteStudent = new JButton("Delete Student");
        btnDeleteStudent.setBounds(170, 180, 140, 25);
        getContentPane().add(btnDeleteStudent);

        btnDeleteStudent.addActionListener(e -> deleteStudent());

        loadStudents();
    }

    private void loadStudents() {
        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            studentComboBox.addItem(student);
        }
    }

    private void loadStudentData() {
        Student student = (Student) studentComboBox.getSelectedItem();
        if (student != null) {
            nameField.setText(student.getName());
            dobField.setText(student.getDateOfBirth().toString());
            genderComboBox.setSelectedItem(student.getGender());
        }
    }

    private void updateStudent() {
        Student student = (Student) studentComboBox.getSelectedItem();
        if (student != null) {
            String name = nameField.getText();
            LocalDate dob = LocalDate.parse(dobField.getText());
            String gender = (String) genderComboBox.getSelectedItem();

            student.setName(name);
            student.setDateOfBirth(dob);
            student.setGender(gender);

            StudentDAO studentDAO = new StudentDAO();
            studentDAO.updateStudent(student);

            JOptionPane.showMessageDialog(this, "Student updated successfully!");
        }
    }

    private void deleteStudent() {
        Student student = (Student) studentComboBox.getSelectedItem();
        if (student != null) {
            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                StudentDAO studentDAO = new StudentDAO();
                studentDAO.deleteStudent(student.getStudentID());

                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                dispose();
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EditStudentUI frame = new EditStudentUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
