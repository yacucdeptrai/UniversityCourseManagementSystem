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

public class EditLecturerDialog extends JDialog {
    private JTextField nameField;
    private JDateChooser dateChooser;
    private JComboBox<String> genderComboBox;
    private JButton btnEditLecturer;

    private Lecturer lecturer;

    public EditLecturerDialog(Frame parent, Lecturer lecturer) {
        super(parent, "Edit Lecturer", true);
        this.lecturer = lecturer;
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JLabel lblName = new JLabel("Name:");
        nameField = new JTextField(lecturer.getName());
        add(lblName);
        add(nameField);

        JLabel lblDob = new JLabel("Date of Birth:");
        dateChooser = new JDateChooser();
        dateChooser.setDate(Date.from(lecturer.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        add(lblDob);
        add(dateChooser);

        JLabel lblGender = new JLabel("Gender:");
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderComboBox.setSelectedItem(lecturer.getGender());
        add(lblGender);
        add(genderComboBox);

        btnEditLecturer = new JButton("Edit Lecturer");
        btnEditLecturer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editLecturer();
            }
        });
        add(new JLabel());
        add(btnEditLecturer);
    }

    private void editLecturer() {
        String name = nameField.getText();
        Date selectedDate = dateChooser.getDate();
        LocalDate dateOfBirth = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String gender = (String) genderComboBox.getSelectedItem();

        lecturer.setName(name);
        lecturer.setDateOfBirth(dateOfBirth);
        lecturer.setGender(gender);
        new LecturerDAO().updateLecturer(lecturer);

        JOptionPane.showMessageDialog(this, "Lecturer updated successfully!");
        dispose();
    }
}
