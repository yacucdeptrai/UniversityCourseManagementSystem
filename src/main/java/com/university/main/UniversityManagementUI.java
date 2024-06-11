package main.java.com.university.main;

import main.java.com.university.dao.*;
import main.java.com.university.model.*;
import main.java.com.university.ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UniversityManagementUI extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel studentPanel;
    private JPanel lecturerPanel;
    private JPanel subjectPanel;

    // Components for Student Management
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private JButton btnAddStudent;
    private JButton btnEditStudent;
    private JButton btnDeleteStudent;

    // Components for Lecturer Management
    private JTable lecturerTable;
    private DefaultTableModel lecturerTableModel;
    private JButton btnAddLecturer;
    private JButton btnEditLecturer;
    private JButton btnDeleteLecturer;

    // Components for Subject Management
    private JTable subjectTable;
    private DefaultTableModel subjectTableModel;
    private JButton btnAddSubject;
    private JButton btnEditSubject;
    private JButton btnDeleteSubject;

    public UniversityManagementUI() {
        setTitle("University Management System");
        setBounds(100, 100, 900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(10, 10, 860, 540);
        getContentPane().add(tabbedPane);

        studentPanel = new JPanel();
        studentPanel.setLayout(null);
        tabbedPane.addTab("Student Management", null, studentPanel, null);

        lecturerPanel = new JPanel();
        lecturerPanel.setLayout(null);
        tabbedPane.addTab("Lecturer Management", null, lecturerPanel, null);

        subjectPanel = new JPanel();
        subjectPanel.setLayout(null);
        tabbedPane.addTab("Subject Management", null, subjectPanel, null);

        initializeStudentPanel();
        initializeLecturerPanel();
        initializeSubjectPanel();
    }

    private void initializeStudentPanel() {
        studentTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Date of Birth", "Gender"}, 0);
        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(25);
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < studentTable.getColumnCount(); i++) {
            studentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBounds(10, 10, 600, 400);
        studentPanel.add(scrollPane);

        btnAddStudent = new JButton("Add Student");
        btnAddStudent.setBounds(620, 10, 200, 25);
        studentPanel.add(btnAddStudent);

        btnEditStudent = new JButton("Edit Student");
        btnEditStudent.setBounds(620, 50, 200, 25);
        studentPanel.add(btnEditStudent);

        btnDeleteStudent = new JButton("Delete Student");
        btnDeleteStudent.setBounds(620, 90, 200, 25);
        studentPanel.add(btnDeleteStudent);

        loadStudents();

        btnAddStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddStudentDialog(UniversityManagementUI.this).setVisible(true);
                loadStudents();
            }
        });

        btnEditStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editStudent();
                loadStudents();
            }
        });

        btnDeleteStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
                loadStudents();
            }
        });
    }

    private void initializeLecturerPanel() {
        lecturerTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Date of Birth", "Gender"}, 0);
        lecturerTable = new JTable(lecturerTableModel);
        lecturerTable.setRowHeight(25);
        lecturerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < lecturerTable.getColumnCount(); i++) {
            lecturerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(lecturerTable);
        scrollPane.setBounds(10, 10, 600, 400);
        lecturerPanel.add(scrollPane);

        btnAddLecturer = new JButton("Add Lecturer");
        btnAddLecturer.setBounds(620, 10, 200, 25);
        lecturerPanel.add(btnAddLecturer);

        btnEditLecturer = new JButton("Edit Lecturer");
        btnEditLecturer.setBounds(620, 50, 200, 25);
        lecturerPanel.add(btnEditLecturer);

        btnDeleteLecturer = new JButton("Delete Lecturer");
        btnDeleteLecturer.setBounds(620, 90, 200, 25);
        lecturerPanel.add(btnDeleteLecturer);

        loadLecturers();

        btnAddLecturer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddLecturerDialog(UniversityManagementUI.this).setVisible(true);
                loadLecturers();
            }
        });

        btnEditLecturer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editLecturer();
                loadLecturers();
            }
        });

        btnDeleteLecturer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteLecturer();
                loadLecturers();
            }
        });
    }

    private void initializeSubjectPanel() {
        subjectTableModel = new DefaultTableModel(new String[]{"ID", "Subject Name", "Lecturer"}, 0);
        subjectTable = new JTable(subjectTableModel);
        subjectTable.setRowHeight(25);
        subjectTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < subjectTable.getColumnCount(); i++) {
            subjectTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(subjectTable);
        scrollPane.setBounds(10, 10, 600, 400);
        subjectPanel.add(scrollPane);

        btnAddSubject = new JButton("Add Subject");
        btnAddSubject.setBounds(620, 10, 200, 25);
        subjectPanel.add(btnAddSubject);

        btnEditSubject = new JButton("Edit Subject");
        btnEditSubject.setBounds(620, 50, 200, 25);
        subjectPanel.add(btnEditSubject);

        btnDeleteSubject = new JButton("Delete Subject");
        btnDeleteSubject.setBounds(620, 90, 200, 25);
        subjectPanel.add(btnDeleteSubject);

        loadSubjects();

        btnAddSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddSubjectDialog(UniversityManagementUI.this).setVisible(true);
                loadSubjects();
            }
        });

        btnEditSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSubject();
                loadSubjects();
            }
        });

        btnDeleteSubject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSubject();
                loadSubjects();
            }
        });
    }

    private void loadStudents() {
        studentTableModel.setRowCount(0);
        StudentDAO studentDAO = new StudentDAO();
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            studentTableModel.addRow(new Object[]{
                    student.getStudentID(),
                    student.getName(),
                    student.getDateOfBirth(),
                    student.getGender()
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
                    subject.getLecturer().getName()
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
        EventQueue.invokeLater(() -> {
            try {
                UniversityManagementUI frame = new UniversityManagementUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
