package main.java.com.university.ui;

import main.java.com.university.dao.LecturerDAO;
import main.java.com.university.model.Lecturer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AddLecturerDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> genderComboBox;
    private JTextField lecturerIdField;
    private JButton btnAddLecturer;

    public AddLecturerDialog(Frame parent) {
        super(parent, "Add Lecturer", true);
        setLayout(new GridLayout(6, 2, 10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JLabel lblName = new JLabel("Name:");
        nameField = new JTextField();
        add(lblName);
        add(nameField);

        JLabel lblDob = new JLabel("Date of Birth:");
        JPanel dobPanel = new JPanel(new FlowLayout());
        dayComboBox = new JComboBox<>(createNumberArray(1, 31));
        monthComboBox = new JComboBox<>(createNumberArray(1, 12));
        yearComboBox = new JComboBox<>(createNumberArray(1900, 2023));
        dobPanel.add(dayComboBox);
        dobPanel.add(monthComboBox);
        dobPanel.add(yearComboBox);
        add(lblDob);
        add(dobPanel);

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
        int day = (Integer) dayComboBox.getSelectedItem();
        int month = (Integer) monthComboBox.getSelectedItem();
        int year = (Integer) yearComboBox.getSelectedItem();
        LocalDate dateOfBirth = LocalDate.of(year, month, day);
        String gender = (String) genderComboBox.getSelectedItem();
        int lecturerId = Integer.parseInt(lecturerIdField.getText());

        Lecturer lecturer = new Lecturer(name, dateOfBirth, gender, lecturerId);
        new LecturerDAO().saveLecturer(lecturer);

        JOptionPane.showMessageDialog(this, "Lecturer added successfully!");
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
