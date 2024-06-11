package main.java.com.university.ui;

import main.java.com.university.dao.StudentDAO;
import main.java.com.university.model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class EditStudentDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> genderComboBox;
    private JButton btnEditStudent;

    private Student student;

    public EditStudentDialog(Frame parent, Student student) {
        super(parent, "Edit Student", true);
        this.student = student;
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JLabel lblName = new JLabel("Name:");
        nameField = new JTextField(student.getName());
        add(lblName);
        add(nameField);

        JLabel lblDob = new JLabel("Date of Birth:");
        JPanel dobPanel = new JPanel(new FlowLayout());
        dayComboBox = new JComboBox<>(createNumberArray(1, 31));
        monthComboBox = new JComboBox<>(createNumberArray(1, 12));
        yearComboBox = new JComboBox<>(createNumberArray(1900, 2023));
        LocalDate dob = student.getDateOfBirth();
        dayComboBox.setSelectedItem(dob.getDayOfMonth());
        monthComboBox.setSelectedItem(dob.getMonthValue());
        yearComboBox.setSelectedItem(dob.getYear());
        dobPanel.add(dayComboBox);
        dobPanel.add(monthComboBox);
        dobPanel.add(yearComboBox);
        add(lblDob);
        add(dobPanel);

        JLabel lblGender = new JLabel("Gender:");
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderComboBox.setSelectedItem(student.getGender());
        add(lblGender);
        add(genderComboBox);

        btnEditStudent = new JButton("Edit Student");
        btnEditStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStudent();
            }
        });
        add(new JLabel());
        add(btnEditStudent);
    }

    private void editStudent() {
        String name = nameField.getText();
        int day = (Integer) dayComboBox.getSelectedItem();
        int month = (Integer) monthComboBox.getSelectedItem();
        int year = (Integer) yearComboBox.getSelectedItem();
        LocalDate dateOfBirth = LocalDate.of(year, month, day);
        String gender = (String) genderComboBox.getSelectedItem();

        student.setName(name);
        student.setDateOfBirth(dateOfBirth);
        student.setGender(gender);
        new StudentDAO().updateStudent(student);

        JOptionPane.showMessageDialog(this, "Student updated successfully!");
        dispose();
    }

    private Integer[] createNumberArray(int start, int end) {
        Integer[] numbers = new Integer[end - start + 1];
        for (int i = start; i <= end; i++) {
            numbers[i - start] = i;
        }
        return numbers;
    }
}

