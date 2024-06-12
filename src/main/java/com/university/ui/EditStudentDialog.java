package main.java.com.university.ui;

import main.java.com.university.dao.StudentDAO;
import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Student;
import main.java.com.university.model.Subject;
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
    private JList<String> subjectList;
    private DefaultListModel<String> subjectListModel;
    private JButton btnSave;
    private JButton btnRemoveSubject;

    private Student student;

    public EditStudentDialog(Frame parent, Student student) {
        super(parent, "Edit Student", true);
        this.student = student;
        setLayout(new GridBagLayout());
        setSize(350, 350);
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
        nameField = new JTextField(student.getName());
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Date of Birth:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dateChooser = new JDateChooser();
        dateChooser.setDate(Date.from(student.getDateOfBirth().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        add(dateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Gender:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderComboBox.setSelectedItem(student.getGender());
        add(genderComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTH;
        add(new JLabel("Enrolled Subjects:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        subjectListModel = new DefaultListModel<>();
        subjectList = new JList<>(subjectListModel);
        subjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (Subject subject : new SubjectDAO().getSubjectsByStudentID(student.getStudentID())) {
            subjectListModel.addElement(subject.getSubjectID() + " - " + subject.getSubjectName());
        }
        add(new JScrollPane(subjectList), gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRemoveSubject = new JButton("Remove Subject");
        btnRemoveSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedSubject();
            }
        });
        buttonPanel.add(btnRemoveSubject);

        btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStudent();
            }
        });
        buttonPanel.add(btnSave);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(buttonPanel, gbc);
    }

    private void saveStudent() {
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

    private void removeSelectedSubject() {
        int selectedIndex = subjectList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedSubjectText = subjectListModel.getElementAt(selectedIndex);
            int subjectID = Integer.parseInt(selectedSubjectText.split(" - ")[0]);
            new StudentDAO().removeCourseFromStudent(student.getStudentID(), subjectID);
            subjectListModel.remove(selectedIndex);
            JOptionPane.showMessageDialog(this, "Subject removed successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a subject to remove.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}