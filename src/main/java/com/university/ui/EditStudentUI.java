package main.java.com.university.ui;

import main.java.com.university.dao.PersonDAO;
import main.java.com.university.dao.StudentDAO;
import main.java.com.university.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class EditStudentUI extends JFrame {
    private JTextField nameField;
    private JTextField dobField;
    private JTextField genderField;
    private JTextField studentIDField;

    public EditStudentUI(Student student) {
        if (student == null) {
            JOptionPane.showMessageDialog(null, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setTitle("Edit Student");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(10, 20, 80, 25);
        getContentPane().add(lblName);

        nameField = new JTextField(student.getName());
        nameField.setBounds(100, 20, 250, 25);
        getContentPane().add(nameField);

        JLabel lblDob = new JLabel("Date of Birth (YYYY-MM-DD):");
        lblDob.setBounds(10, 60, 200, 25);
        getContentPane().add(lblDob);

        dobField = new JTextField(student.getDateOfBirth().toString());
        dobField.setBounds(220, 60, 130, 25);
        getContentPane().add(dobField);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(10, 100, 80, 25);
        getContentPane().add(lblGender);

        genderField = new JTextField(student.getGender());
        genderField.setBounds(100, 100, 250, 25);
        getContentPane().add(genderField);

        studentIDField = new JTextField(String.valueOf(student.getStudentID()));
        studentIDField.setBounds(100, 140, 250, 25);
        studentIDField.setVisible(false); // Ẩn trường studentID vì không cần chỉnh sửa
        getContentPane().add(studentIDField);

        JButton btnEditStudent = new JButton("Edit Student");
        btnEditStudent.setBounds(10, 180, 140, 25);
        getContentPane().add(btnEditStudent);

        btnEditStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                LocalDate dob = LocalDate.parse(dobField.getText());
                String gender = genderField.getText();
                int studentID = Integer.parseInt(studentIDField.getText());

                Student updatedStudent = new Student(name, dob, gender, studentID);
                updatedStudent.setId(student.getId());

                PersonDAO personDAO = new PersonDAO();
                StudentDAO studentDAO = new StudentDAO();

                personDAO.updatePerson(updatedStudent);
                studentDAO.updateStudent(updatedStudent);

                JOptionPane.showMessageDialog(null, "Student updated successfully!");
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Test with a sample student object (chỉnh sửa với studentID thực tế)
                    StudentDAO studentDAO = new StudentDAO();
                    Student student = studentDAO.getStudentById(999); // Thay thế bằng ID sinh viên thực tế
                    EditStudentUI frame = new EditStudentUI(student);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
