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
        setLayout(new GridBagLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        nameField = new JTextField(lecturer.getName());
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Date of Birth:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dateChooser = new JDateChooser();
        dateChooser.setDate(Date.from(lecturer.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        add(dateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Gender:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderComboBox.setSelectedItem(lecturer.getGender());
        add(genderComboBox, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnEditLecturer = new JButton("Edit Lecturer");
        btnEditLecturer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editLecturer();
            }
        });
        buttonPanel.add(btnEditLecturer);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);
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