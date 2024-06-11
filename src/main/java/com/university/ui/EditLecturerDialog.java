package main.java.com.university.ui;

import main.java.com.university.dao.LecturerDAO;
import main.java.com.university.model.Lecturer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class EditLecturerDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> yearComboBox;
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
        JPanel dobPanel = new JPanel(new FlowLayout());
        dayComboBox = new JComboBox<>(createNumberArray(1, 31));
        monthComboBox = new JComboBox<>(createNumberArray(1, 12));
        yearComboBox = new JComboBox<>(createNumberArray(1900, 2023));
        LocalDate dob = lecturer.getDateOfBirth();
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
        int day = (Integer) dayComboBox.getSelectedItem();
        int month = (Integer) monthComboBox.getSelectedItem();
        int year = (Integer) yearComboBox.getSelectedItem();
        LocalDate dateOfBirth = LocalDate.of(year, month, day);
        String gender = (String) genderComboBox.getSelectedItem();

        lecturer.setName(name);
        lecturer.setDateOfBirth(dateOfBirth);
        lecturer.setGender(gender);
        new LecturerDAO().updateLecturer(lecturer);

        JOptionPane.showMessageDialog(this, "Lecturer updated successfully!");
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
