package main.java.com.university.main;

import main.java.com.university.dao.LecturerDAO;
import main.java.com.university.dao.StudentDAO;
import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Student;
import main.java.com.university.model.Subject;
import main.java.com.university.ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class UniversityManagementUI extends JFrame {
    private JTable studentTable, lecturerTable, subjectTable;
    private DefaultTableModel studentTableModel, lecturerTableModel, subjectTableModel;
    private JTextField studentSearchField, lecturerSearchField, subjectSearchField;
    private JButton btnEnrollStudent, btnEditStudent, btnDeleteStudent;
    private JButton btnAddLecturer, btnEditLecturer, btnDeleteLecturer;
    private JButton btnAddSubject, btnEditSubject, btnDeleteSubject;
    private JLabel lblStudentInfo;

    public UniversityManagementUI() {
        setTitle("University Management System");
        setLayout(new BorderLayout());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Student Management", createStudentManagementPanel());
        tabbedPane.add("Lecturer Management", createLecturerManagementPanel());
        tabbedPane.add("Subject Management", createSubjectManagementPanel());

        add(tabbedPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createStudentManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        studentTableModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(25);
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        centerTableData(studentTable);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentSearchField = new JTextField(20);
        studentSearchField.setToolTipText("Search...");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(studentSearchField);

        TableRowSorter<DefaultTableModel> studentSorter = new TableRowSorter<>(studentTableModel);
        studentTable.setRowSorter(studentSorter);
        studentSearchField.getDocument().addDocumentListener(new SearchListener(studentSearchField, studentSorter));

        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnEnrollStudent = new JButton("Enroll Student");
        btnDeleteStudent = new JButton("Delete Student");

        buttonPanel.add(btnEnrollStudent);
        buttonPanel.add(btnDeleteStudent);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel studentInfoPanel = createStudentInfoPanel();
        panel.add(studentInfoPanel, BorderLayout.EAST);

        loadStudents();

        btnEnrollStudent.addActionListener(e -> {
            new AddStudentDialog(UniversityManagementUI.this).setVisible(true);
            loadStudents();
        });

        btnDeleteStudent.addActionListener(e -> {
            deleteStudent();
            loadStudents();
        });

        studentTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                updateStudentInfoPanel();
            }
        });

        return panel;
    }

    private JPanel createStudentInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Info"));
        panel.setPreferredSize(new Dimension(300, 0));

        lblStudentInfo = new JLabel("<html><br/><br/><br/>Select a student to see details.</html>", SwingConstants.CENTER);
        panel.add(lblStudentInfo, BorderLayout.CENTER);

        btnEditStudent = new JButton("Edit Student");
        panel.add(btnEditStudent, BorderLayout.SOUTH);

        btnEditStudent.addActionListener(e -> {
            editStudent();
            loadStudents();
        });

        return panel;
    }

    private void updateStudentInfoPanel() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentID = (int) studentTableModel.getValueAt(selectedRow, 0);
            Student student = new StudentDAO().getStudentById(studentID);

            StringBuilder info = new StringBuilder("<html>");
            info.append("ID: ").append(student.getStudentID()).append("<br/>");
            info.append("Name: ").append(student.getName()).append("<br/>");
            info.append("Date of Birth: ").append(student.getDateOfBirth()).append("<br/>");
            info.append("Gender: ").append(student.getGender()).append("<br/>");
            info.append("Enrolled Subjects: ").append("<br/>");

            List<Subject> subjects = new SubjectDAO().getSubjectsByStudentID(studentID);
            for (Subject subject : subjects) {
                info.append(subject.getSubjectName()).append(" (").append(subject.getCredits()).append(" credits)").append("<br/>");
            }
            info.append("</html>");
            lblStudentInfo.setText(info.toString());
        } else {
            lblStudentInfo.setText("<html><br/><br/><br/>Select a student to see details.</html>");
        }
    }

    private JPanel createLecturerManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        lecturerTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Date of Birth", "Gender"}, 0);
        lecturerTable = new JTable(lecturerTableModel);
        lecturerTable.setRowHeight(25);
        lecturerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        centerTableData(lecturerTable);

        JScrollPane scrollPane = new JScrollPane(lecturerTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lecturerSearchField = new JTextField(20);
        lecturerSearchField.setToolTipText("Search...");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(lecturerSearchField);

        TableRowSorter<DefaultTableModel> lecturerSorter = new TableRowSorter<>(lecturerTableModel);
        lecturerTable.setRowSorter(lecturerSorter);
        lecturerSearchField.getDocument().addDocumentListener(new SearchListener(lecturerSearchField, lecturerSorter));

        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAddLecturer = new JButton("Add Lecturer");
        btnEditLecturer = new JButton("Edit Lecturer");
        btnDeleteLecturer = new JButton("Delete Lecturer");

        buttonPanel.add(btnAddLecturer);
        buttonPanel.add(btnEditLecturer);
        buttonPanel.add(btnDeleteLecturer);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadLecturers();

        btnAddLecturer.addActionListener(e -> {
            new AddLecturerDialog(UniversityManagementUI.this).setVisible(true);
            loadLecturers();
        });

        btnEditLecturer.addActionListener(e -> {
            editLecturer();
            loadLecturers();
        });

        btnDeleteLecturer.addActionListener(e -> {
            deleteLecturer();
            loadLecturers();
        });

        return panel;
    }

    private JPanel createSubjectManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        subjectTableModel = new DefaultTableModel(new String[]{"ID", "Subject Name", "Lecturer", "Credits"}, 0);
        subjectTable = new JTable(subjectTableModel);
        subjectTable.setRowHeight(25);
        subjectTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        centerTableData(subjectTable);

        JScrollPane scrollPane = new JScrollPane(subjectTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subjectSearchField = new JTextField(20);
        subjectSearchField.setToolTipText("Search...");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(subjectSearchField);

        TableRowSorter<DefaultTableModel> subjectSorter = new TableRowSorter<>(subjectTableModel);
        subjectTable.setRowSorter(subjectSorter);
        subjectSearchField.getDocument().addDocumentListener(new SearchListener(subjectSearchField, subjectSorter));

        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAddSubject = new JButton("Add Subject");
        btnEditSubject = new JButton("Edit Subject");
        btnDeleteSubject = new JButton("Delete Subject");

        buttonPanel.add(btnAddSubject);
        buttonPanel.add(btnEditSubject);
        buttonPanel.add(btnDeleteSubject);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadSubjects();

        btnAddSubject.addActionListener(e -> {
            new AddSubjectDialog(UniversityManagementUI.this).setVisible(true);
            loadSubjects();
        });

        btnEditSubject.addActionListener(e -> {
            editSubject();
            loadSubjects();
        });

        btnDeleteSubject.addActionListener(e -> {
            deleteSubject();
            loadSubjects();
        });

        return panel;
    }

    private void centerTableData(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void loadStudents() {
        studentTableModel.setRowCount(0);
        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            studentTableModel.addRow(new Object[]{
                    student.getStudentID(),
                    student.getName()
            });
        }
    }

    private void loadLecturers() {
        lecturerTableModel.setRowCount(0);
        LecturerDAO lecturerDAO = new LecturerDAO();
        List<Lecturer> lecturers = lecturerDAO.getAllLecturers();
        for (Lecturer lecturer : lecturers) {
            lecturerTableModel.addRow(new Object[]{
                    lecturer.getLecturerID(),
                    lecturer.getName(),
                    lecturer.getDateOfBirth(),
                    lecturer.getGender()
            });
        }
    }

    private void loadSubjects() {
        subjectTableModel.setRowCount(0);
        SubjectDAO subjectDAO = new SubjectDAO();
        List<Subject> subjects = subjectDAO.getAllSubjects();
        for (Subject subject : subjects) {
            subjectTableModel.addRow(new Object[]{
                    subject.getSubjectID(),
                    subject.getSubjectName(),
                    subject.getLecturer().getName(),
                    subject.getCredits()
            });
        }
    }

    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentID = (int) studentTableModel.getValueAt(selectedRow, 0);
            Student student = new StudentDAO().getStudentById(studentID);
            new EditStudentDialog(this, student).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentID = (int) studentTableModel.getValueAt(selectedRow, 0);
            new StudentDAO().deleteStudent(studentID);
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            loadStudents();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editLecturer() {
        int selectedRow = lecturerTable.getSelectedRow();
        if (selectedRow != -1) {
            int lecturerID = (int) lecturerTableModel.getValueAt(selectedRow, 0);
            Lecturer lecturer = new LecturerDAO().getLecturerById(lecturerID);
            new EditLecturerDialog(this, lecturer).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a lecturer to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteLecturer() {
        int selectedRow = lecturerTable.getSelectedRow();
        if (selectedRow != -1) {
            int lecturerID = (int) lecturerTableModel.getValueAt(selectedRow, 0);
            new LecturerDAO().deleteLecturer(lecturerID);
            JOptionPane.showMessageDialog(this, "Lecturer deleted successfully!");
            loadLecturers();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a lecturer to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSubject() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow != -1) {
            int subjectID = (int) subjectTableModel.getValueAt(selectedRow, 0);
            Subject subject = new SubjectDAO().getSubjectById(subjectID);
            new EditSubjectDialog(this, subject).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a subject to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSubject() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow != -1) {
            int subjectID = (int) subjectTableModel.getValueAt(selectedRow, 0);
            new SubjectDAO().deleteSubject(subjectID);
            JOptionPane.showMessageDialog(this, "Subject deleted successfully!");
            loadSubjects();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a subject to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UniversityManagementUI());
    }
}
