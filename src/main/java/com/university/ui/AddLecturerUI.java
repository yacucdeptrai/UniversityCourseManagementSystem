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
    private JTextField dobField;
    private JTextField genderField;
    private JTextField lecturerIDField;

    public AddLecturerUI() {
        setTitle("Add New Lecturer");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(10, 20, 80, 25);
        getContentPane().add(lblName);

        nameField = new JTextField();
        nameField.setBounds(100, 20, 250, 25);
        getContentPane().add(nameField);

        JLabel lblDob = new JLabel("Date of Birth (YYYY-MM-DD):");
        lblDob.setBounds(10, 60, 200, 25);
        getContentPane().add(lblDob);

        dobField = new JTextField();
        dobField.setBounds(220, 60, 130, 25);
        getContentPane().add(dobField);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(10, 100, 80, 25);
        getContentPane().add(lblGender);

        genderField = new JTextField();
        genderField.setBounds(100, 100, 250, 25);
        getContentPane().add(genderField);

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
                LocalDate dob = LocalDate.parse(dobField.getText());
                String gender = genderField.getText();
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

