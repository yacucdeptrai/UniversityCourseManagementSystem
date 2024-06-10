package main.java.com.university.ui;

import main.java.com.university.dao.StudentDAO;
import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Student;
import main.java.com.university.model.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class EditStudentUI extends JFrame {
    private JComboBox<Student> studentComboBox;
    private JTextField nameField;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> genderComboBox;
    private JList<Subject> subjectList;
    private DefaultListModel<Subject> subjectListModel;
    private JButton btnAddSubject;
    private JButton btnRemoveSubject;

    public EditStudentUI() {
        setTitle("Edit Student");
        setBounds(100, 100, 600, 500);  // Adjust size to fit new components
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblSelectStudent = new JLabel("Select Student:");
        lblSelectStudent.setBounds(10, 20, 100, 25);
        getContentPane().add(lblSelectStudent);

        studentComboBox = new JComboBox<>();
        studentComboBox.setBounds(120, 20, 250, 25);
        getContentPane().add(studentComboBox);

        studentComboBox.addActionListener(e -> loadStudentData());

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(10, 60, 80, 25);
        getContentPane().add(lblName);

        nameField = new JTextField();
        nameField.setBounds(120, 60, 250, 25);
        getContentPane().add(nameField);

        JLabel lblDob = new JLabel("Date of Birth:");
        lblDob.setBounds(10, 100, 200, 25);
        getContentPane().add(lblDob);

        dayComboBox = new JComboBox<>(createNumberArray(1, 31));
        dayComboBox.setBounds(120, 100, 70, 25);
        getContentPane().add(dayComboBox);

        monthComboBox = new JComboBox<>(createNumberArray(1, 12));
        monthComboBox.setBounds(200, 100, 70, 25);
        getContentPane().add(monthComboBox);

        yearComboBox = new JComboBox<>(createNumberArray(1900, 2024));
        yearComboBox.setBounds(280, 100, 90, 25);
        getContentPane().add(yearComboBox);

        JLabel lblGender = new JLabel("Gender:");
        lblGender.setBounds(10, 140, 80, 25);
        getContentPane().add(lblGender);

        genderComboBox = new JComboBox<>(new String[] {"Male", "Female"});
        genderComboBox.setBounds(120, 140, 250, 25);
        getContentPane().add(genderComboBox);

        JLabel lblSubjects = new JLabel("Subjects:");
        lblSubjects.setBounds(10, 180, 100, 25);
        getContentPane().add(lblSubjects);

        subjectListModel = new DefaultListModel<>();
        subjectList = new JList<>(subjectListModel);
        JScrollPane scrollPane = new JScrollPane(subjectList);
        scrollPane.setBounds(120, 180, 250, 100);
        getContentPane().add(scrollPane);

        btnAddSubject = new JButton("Enrolling Subject");
        btnAddSubject.setBounds(10, 290, 140, 25);
        getContentPane().add(btnAddSubject);

        btnRemoveSubject = new JButton("Remove Subject");
        btnRemoveSubject.setBounds(170, 290, 140, 25);
        getContentPane().add(btnRemoveSubject);

        btnAddSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSubject();
            }
        });

        btnRemoveSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSubject();
            }
        });

        JButton btnEditStudent = new JButton("Edit Student");
        btnEditStudent.setBounds(10, 330, 140, 25);
        getContentPane().add(btnEditStudent);

        JButton btnDeleteStudent = new JButton("Delete Student");
        btnDeleteStudent.setBounds(170, 330, 140, 25);
        getContentPane().add(btnDeleteStudent);

        btnEditStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStudent();
            }
        });

        btnDeleteStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        loadStudents();
    }

    private void loadStudentData() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent != null) {
            nameField.setText(selectedStudent.getName());
            LocalDate dob = selectedStudent.getDateOfBirth();
            dayComboBox.setSelectedItem(dob.getDayOfMonth());
            monthComboBox.setSelectedItem(dob.getMonthValue());
            yearComboBox.setSelectedItem(dob.getYear());
            genderComboBox.setSelectedItem(selectedStudent.getGender());

            loadStudentSubjects(selectedStudent.getStudentID());
        }
    }

    private void loadStudentSubjects(int studentID) {
        subjectListModel.clear();
        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects = subjectDAO.getSubjectsByStudentID(studentID);
        for (Subject subject : subjects) {
            subjectListModel.addElement(subject);
        }
    }

    private void loadStudents() {
        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            studentComboBox.addItem(student);
        }
    }

    private void addSubject() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent != null) {
            List<Subject> allSubjects = new SubjectDAO().getAllSubjects();
            Subject selectedSubject = (Subject) JOptionPane.showInputDialog(
                    this,
                    "Select Subject to Enrolling:",
                    "Enrolling Subject",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    allSubjects.toArray(),
                    allSubjects.get(0)
            );
            if (selectedSubject != null && !subjectListModel.contains(selectedSubject)) {
                subjectListModel.addElement(selectedSubject);
                new SubjectDAO().enrollStudentInSubject(selectedStudent.getStudentID(), selectedSubject.getSubjectID());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSubject() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        Subject selectedSubject = subjectList.getSelectedValue();
        if (selectedStudent != null && selectedSubject != null) {
            subjectListModel.removeElement(selectedSubject);
            new SubjectDAO().removeStudentFromSubject(selectedStudent.getStudentID(), selectedSubject.getSubjectID());
        } else {
            JOptionPane.showMessageDialog(this, "Please select a subject to remove.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editStudent() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent != null) {
            selectedStudent.setName(nameField.getText());
            int day = (int) dayComboBox.getSelectedItem();
            int month = (int) monthComboBox.getSelectedItem();
            int year = (int) yearComboBox.getSelectedItem();
            selectedStudent.setDateOfBirth(LocalDate.of(year, month, day));
            selectedStudent.setGender((String) genderComboBox.getSelectedItem());

            new StudentDAO().updateStudent(selectedStudent);

            JOptionPane.showMessageDialog(this, "Student updated successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent != null) {
            new StudentDAO().deleteStudent(selectedStudent.getStudentID());

            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
                EditStudentUI frame = new EditStudentUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
