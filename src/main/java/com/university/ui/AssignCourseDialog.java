package main.java.com.university.ui;

import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Subject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AssignCourseDialog extends JDialog {
    private JComboBox<Subject> subjectComboBox;
    private JButton btnAssign;
    private int studentID;

    public AssignCourseDialog(JFrame parent, int studentID) {
        super(parent, "Assign Course", true);
        this.studentID = studentID;
        setLayout(new FlowLayout());
        setSize(400, 150);
        setLocationRelativeTo(parent);

        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects = subjectDAO.getAllSubjects();
        subjectComboBox = new JComboBox<>(subjects.toArray(new Subject[0]));

        btnAssign = new JButton("Assign");

        add(new JLabel("Select Course:"));
        add(subjectComboBox);
        add(btnAssign);

        btnAssign.addActionListener(e -> assignCourse());

        setVisible(true);
    }

    private void assignCourse() {
        Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
        if (selectedSubject != null) {
            SubjectDAO subjectDAO = new SubjectDAO();
            subjectDAO.assignCourseToStudent(studentID, selectedSubject.getSubjectID());
            JOptionPane.showMessageDialog(this, "Course assigned successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a course.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
