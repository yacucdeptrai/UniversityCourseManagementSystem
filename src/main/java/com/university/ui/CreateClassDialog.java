package main.java.com.university.ui;

import main.java.com.university.dao.LecturerDAO;
import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Subject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CreateClassDialog extends JDialog {
    private JTextField subjectNameField;
    private JTextField creditsField;
    private JTextField customSubjectIDField;
    private JComboBox<String> lecturerComboBox;
    private JButton btnAddSubject;

    public CreateClassDialog(Frame parent) {
        super(parent, "Create Class", true);
        initializeUI(parent);
    }

    private void initializeUI(Frame parent) {
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(330, 200);
        setLocationRelativeTo(parent);

        // Components
        JLabel lblID = new JLabel("Subject ID:");
        customSubjectIDField = new JTextField();
        add(lblID);
        add(customSubjectIDField);

        JLabel lblName = new JLabel("Subject Name:");
        subjectNameField = new JTextField();
        add(lblName);
        add(subjectNameField);

        JLabel lblCredits = new JLabel("Credits:");
        creditsField = new JTextField();
        add(lblCredits);
        add(creditsField);

        JLabel lblLecturer = new JLabel("Lecturer:");
        lecturerComboBox = new JComboBox<>();
        List<Lecturer> lecturers = new LecturerDAO().getAllLecturers();

        if (lecturers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No lecturers available. Please add lecturers before creating a class.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        for (Lecturer lecturer : lecturers) {
            lecturerComboBox.addItem(lecturer.getName() + " (ID: " + lecturer.getLecturerID() + ")");
        }
        add(lblLecturer);
        add(lecturerComboBox);

        btnAddSubject = new JButton("Create Class");
        btnAddSubject.addActionListener(e -> addSubject());
        add(new JLabel()); // Buffer
        add(btnAddSubject);
    }

    private void addSubject() {
        try {
            int customSubjectID = Integer.parseInt(customSubjectIDField.getText());
            String subjectName = subjectNameField.getText();
            int credits = Integer.parseInt(creditsField.getText());
            String lecturerInfo = (String) lecturerComboBox.getSelectedItem();
            int lecturerID = Integer.parseInt(lecturerInfo.substring(lecturerInfo.indexOf("ID: ") + 4, lecturerInfo.indexOf(")")));

            Lecturer lecturer = new LecturerDAO().getLecturerById(lecturerID);

            SubjectDAO subjectDAO = new SubjectDAO();
            String uniqueSubjectName = subjectDAO.generateUniqueSubjectName(subjectName);

            Subject subject = new Subject(customSubjectID, 0, uniqueSubjectName, credits, lecturer);
            subjectDAO.saveSubject(subject);

            JOptionPane.showMessageDialog(this, "Subject added successfully!");
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error occurred while adding the subject. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
