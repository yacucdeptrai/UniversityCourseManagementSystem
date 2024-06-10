package main.java.com.university.ui;

import main.java.com.university.dao.PersonDAO;
import main.java.com.university.dao.LecturerDAO;
import main.java.com.university.model.Lecturer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AddLecturerUI extends JFrame {
    private JTextField nameField;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JTextField lecturerIDField;
    private JComboBox<String> genderComboBox;

    public AddLecturerUI() {
        setTitle("Add New Lecturer");
        setBounds(100, 100, 400, 260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(10, 20, 80, 25);
        getContentPane().add(lblName);

        nameField = new JTextField();
        nameField.setBounds(100, 20, 250, 25);
        getContentPane().add(nameField);

        JLabel lblDob = new JLabel("Date of Birth:");
        lblDob.setBounds(10, 60, 200, 25);
        getContentPane().add(lblDob);

        dayComboBox = new JComboBox<>(createNumberArray(1, 31));
        dayComboBox.setBounds(100, 60, 70, 25);
        getContentPane().add(dayComboBox);

        monthComboBox = new JComboBox<>(createNumberArray(1, 12));
        monthComboBox.setBounds(180, 60, 70, 25);
        getContentPane().add(monthComboBox);

        yearComboBox = new JComboBox<>(createNumberArray(1900, 2024));
        yearComboBox.setBounds(260, 60, 90, 25);
        getContentPane().add(yearComboBox);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(10, 100, 80, 25);
        getContentPane().add(lblGender);

        genderComboBox = new JComboBox<>(new String[] {"Male", "Female"});
        genderComboBox.setBounds(100, 100, 250, 25);
        getContentPane().add(genderComboBox);

        JLabel lblLecturerID = new JLabel("Lecturer ID:");
        lblLecturerID.setBounds(10, 140, 80, 25);
        getContentPane().add(lblLecturerID);

        lecturerIDField = new JTextField();
        lecturerIDField.setBounds(100, 140, 250, 25);
        getContentPane().add(lecturerIDField);

        JButton btnAddLecturer = new JButton("Add Lecturer");
        btnAddLecturer.setBounds(10, 180, 140, 25);
        getContentPane().add(btnAddLecturer);

        btnAddLecturer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int day = (int) dayComboBox.getSelectedItem();
                int month = (int) monthComboBox.getSelectedItem();
                int year = (int) yearComboBox.getSelectedItem();
                LocalDate dob = LocalDate.of(year, month, day);
                String gender = (String) genderComboBox.getSelectedItem();
                int lecturerID = Integer.parseInt(lecturerIDField.getText());

                Lecturer lecturer = new Lecturer(name, dob, gender, lecturerID);
                PersonDAO personDAO = new PersonDAO();
                LecturerDAO lecturerDAO = new LecturerDAO();

                // Kiểm tra ID đã tồn tại
                if (lecturerDAO.getLecturerById(lecturerID) != null) {
                    JOptionPane.showMessageDialog(null, "Lecturer ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int personID = personDAO.savePerson(lecturer);
                lecturer.setId(personID);
                lecturerDAO.saveLecturer(lecturer);

                JOptionPane.showMessageDialog(null, "Lecturer added successfully!");
                dispose();
            }
        });
    }

    private Integer[] createNumberArray(int start, int end) {
        Integer[] numbers = new Integer[end - start + 1];
        for (int i = start; i <= end; i++) {
            numbers[i - start] = i;
        }
        return numbers;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AddLecturerUI frame = new AddLecturerUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

