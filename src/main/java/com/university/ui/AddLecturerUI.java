package main.java.com.university.ui;

import main.java.com.university.dao.LecturerDAO;
import main.java.com.university.dao.PersonDAO;
import main.java.com.university.model.Lecturer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;

public class AddLecturerUI extends JFrame {
    private JTextField nameField;
    private JTextField dobField;
    private JComboBox<String> genderComboBox;
    private JLabel lecturerIDLabel;

    public AddLecturerUI() {
        setTitle("Add New Lecturer");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(10, 20, 80, 25);
        getContentPane().add(lblName);

        nameField = new JTextField();
        nameField.setBounds(120, 20, 250, 25);
        getContentPane().add(nameField);

        JLabel lblDob = new JLabel("Date of Birth (YYYY-MM-DD):");
        lblDob.setBounds(10, 60, 200, 25);
        getContentPane().add(lblDob);

        dobField = new JTextField();
        dobField.setBounds(220, 60, 150, 25);
        getContentPane().add(dobField);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(10, 100, 80, 25);
        getContentPane().add(lblGender);

        genderComboBox = new JComboBox<>(new String[] {"Male", "Female"});
        genderComboBox.setBounds(120, 100, 250, 25);
        getContentPane().add(genderComboBox);

        JLabel lblLecturerID = new JLabel("Lecturer ID:");
        lblLecturerID.setBounds(10, 140, 80, 25);
        getContentPane().add(lblLecturerID);

        lecturerIDLabel = new JLabel();
        lecturerIDLabel.setBounds(120, 140, 250, 25);
        getContentPane().add(lecturerIDLabel);

        JButton btnAddLecturer = new JButton("Add Lecturer");
        btnAddLecturer.setBounds(10, 180, 140, 25);
        getContentPane().add(btnAddLecturer);

        btnAddLecturer.addActionListener(e -> addLecturer());

        setVisible(true);
    }

    private void addLecturer() {
        String name = nameField.getText();
        LocalDate dob = LocalDate.parse(dobField.getText());
        String gender = (String) genderComboBox.getSelectedItem();

        Lecturer lecturer = new Lecturer(name, dob, gender, 0); // Lecturer ID sẽ được tự động sinh bởi cơ sở dữ liệu
        PersonDAO personDAO = new PersonDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();

        int personId = personDAO.savePerson(lecturer);
        lecturer.setId(personId);
        int lecturerId = lecturerDAO.saveLecturerAndReturnID(lecturer);

        lecturerIDLabel.setText(String.valueOf(lecturerId)); // Hiển thị Lecturer ID sau khi thêm thành công

        JOptionPane.showMessageDialog(this, "Lecturer added successfully!");
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
