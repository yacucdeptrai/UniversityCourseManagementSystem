package main.java.com.university.ui;

import main.java.com.university.dao.LecturerDAO;
import main.java.com.university.model.Lecturer;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddLecturerDialog extends JDialog {
    private JTextField nameField;
    private JDateChooser dateChooser;
    private JComboBox<String> genderComboBox;
    private JTextField lecturerIdField;
    private JButton btnAddLecturer;

    public AddLecturerDialog(Frame parent) {
        super(parent, "Add Lecturer", true);
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(300, 200);
        setLocationRelativeTo(parent);

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

        JLabel lblLecturerId = new JLabel("Lecturer ID:");
        lecturerIdField = new JTextField();
        add(lblLecturerId);
        add(lecturerIdField);

        btnAddLecturer = new JButton("Add Lecturer");
        btnAddLecturer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLecturer();
            }
        });
        add(new JLabel());
        add(btnAddLecturer);
    }

    private void addLecturer() {
        String name = nameField.getText();
        Date selectedDate = dateChooser.getDate();
        LocalDate dateOfBirth = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String gender = (String) genderComboBox.getSelectedItem();
        int lecturerId = Integer.parseInt(lecturerIdField.getText());

        Lecturer lecturer = new Lecturer(name, dateOfBirth, gender, lecturerId);
        new LecturerDAO().saveLecturer(lecturer);

        JOptionPane.showMessageDialog(this, "Lecturer added successfully!");
        dispose();
    }
}
