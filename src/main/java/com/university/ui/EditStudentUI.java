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

        JButton btnDeleteStudent = new JButton("Delete Student");
        btnDeleteStudent.setBounds(170, 180, 140, 25);
        getContentPane().add(btnDeleteStudent);

        btnEditStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student selectedStudent = (Student) studentComboBox.getSelectedItem();
                if (selectedStudent != null) {
                    selectedStudent.setName(nameField.getText());
                    selectedStudent.setDateOfBirth(LocalDate.parse(dobField.getText()));
                    selectedStudent.setGender((String) genderComboBox.getSelectedItem());

                    StudentDAO studentDAO = new StudentDAO();
                    studentDAO.updateStudent(selectedStudent);

                    JOptionPane.showMessageDialog(null, "Student updated successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a student", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnDeleteStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student selectedStudent = (Student) studentComboBox.getSelectedItem();
                if (selectedStudent != null) {
                    StudentDAO studentDAO = new StudentDAO();
                    studentDAO.deleteStudent(selectedStudent.getStudentID());

                    JOptionPane.showMessageDialog(null, "Student deleted successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a student", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadStudents();
    }

    private void loadStudentData() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent != null) {
            nameField.setText(selectedStudent.getName());
            dobField.setText(selectedStudent.getDateOfBirth().toString());
            genderComboBox.setSelectedItem(selectedStudent.getGender());
        }
    }

    private void loadStudents() {
        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            studentComboBox.addItem(student);
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
