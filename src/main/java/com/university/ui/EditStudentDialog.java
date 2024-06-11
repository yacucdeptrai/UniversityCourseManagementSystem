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

public class EditStudentDialog extends JDialog {
    private JTextField nameField;
    private JDateChooser dateChooser;
    private JComboBox<String> genderComboBox;
    private JButton btnEditStudent;

    private Student student;

    public EditStudentDialog(Frame parent, Student student) {
        super(parent, "Edit Student", true);
        this.student = student;
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JLabel lblName = new JLabel("Name:");
        nameField = new JTextField(student.getName());
        add(lblName);
        add(nameField);

        JLabel lblDob = new JLabel("Date of Birth:");
        dateChooser = new JDateChooser();
        dateChooser.setDate(Date.from(student.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        add(lblDob);
        add(dateChooser);

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
        Date selectedDate = dateChooser.getDate();
        LocalDate dateOfBirth = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String gender = (String) genderComboBox.getSelectedItem();

        student.setName(name);
        student.setDateOfBirth(dateOfBirth);
        student.setGender(gender);
        new StudentDAO().updateStudent(student);

        JOptionPane.showMessageDialog(this, "Student updated successfully!");
        dispose();
    }
}
