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
    private JButton btnEnrollStudent, btnAssignCourse, btnEditStudent, btnDeleteStudent;
    private JButton btnAddLecturer, btnEditLecturer, btnDeleteLecturer;
    private JButton btnAddSubject, btnEditSubject, btnDeleteSubject;
    private JLabel lblStudentInfo, lblLecturerInfo;

    public UniversityManagementUI() {
        setTitle("University Management System");
        setLayout(new BorderLayout());
        setSize(685, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Student Management", createStudentManagementPanel());
        tabbedPane.add("Lecturer Management", createLecturerManagementPanel());
        tabbedPane.add("Class Management", createClassManagementPanel());

        add(tabbedPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createStudentManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tạo bảng sinh viên
        studentTableModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(25);
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Cố định kích thước cột ID
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        studentTable.getColumnModel().getColumn(0).setMinWidth(50);
        studentTable.getColumnModel().getColumn(0).setMaxWidth(50);

        // Căn giữa nội dung bảng
        centerTableData(studentTable);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Thêm thanh tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentSearchField = new JTextField(20);
        studentSearchField.setToolTipText("Search...");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(studentSearchField);

        // Bộ lọc bảng sinh viên
        TableRowSorter<DefaultTableModel> studentSorter = new TableRowSorter<>(studentTableModel);
        studentTable.setRowSorter(studentSorter);
        studentSearchField.getDocument().addDocumentListener(new SearchListener(studentSearchField, studentSorter));

        panel.add(searchPanel, BorderLayout.NORTH);

        // Thêm panel chứa các nút ở phía dưới
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnEnrollStudent = new JButton("Enroll Student");
        btnDeleteStudent = new JButton("Delete Student");
        btnAssignCourse = new JButton("Assign Course");

        buttonPanel.add(btnEnrollStudent);
        buttonPanel.add(btnDeleteStudent);
        buttonPanel.add(btnAssignCourse);

        panel.add(buttonPanel, BorderLayout.SOUTH); // Đặt panel chứa nút vào vị trí dưới cùng

        // Tạo bảng thông tin sinh viên
        JPanel studentInfoPanel = createStudentInfoPanel();
        panel.add(studentInfoPanel, BorderLayout.EAST);

        // Nạp dữ liệu
        loadStudents();

        // Thêm sự kiện
        btnEnrollStudent.addActionListener(e -> {
            new AddStudentDialog(UniversityManagementUI.this).setVisible(true);
            loadStudents(); // Làm mới bảng sau khi thêm
        });

        btnDeleteStudent.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = studentTable.convertRowIndexToModel(selectedRow);
                int StudentID = (int) studentTableModel.getValueAt(modelRow, 0);
                boolean success = new StudentDAO().deleteStudent(StudentID);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                    loadStudents(); // Làm mới bảng sau khi xóa
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot delete student as now being attended in one or more classes", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAssignCourse.addActionListener(e -> {
            assignCourseToStudent();
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

        // Sử dụng JPanel với FlowLayout để căn trái thông tin
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblStudentInfo = new JLabel();
        lblStudentInfo.setVerticalAlignment(SwingConstants.TOP); // Đảm bảo văn bản bắt đầu từ đỉnh
        lblStudentInfo.setFont(new Font("Tahoma", Font.PLAIN, 14)); // Đặt phông chữ cho nhãn
        infoPanel.add(lblStudentInfo);
        panel.add(infoPanel, BorderLayout.CENTER);

        // Tạo JPanel con để chứa các nút
        JPanel buttonPanel = new JPanel(new GridLayout(2,1,5,5));

        JButton btnDisplayAcademicRecord = new JButton("Display Academic Record");
        btnDisplayAcademicRecord.addActionListener(e -> displayAcademicRecord());
        buttonPanel.add(btnDisplayAcademicRecord);

        btnEditStudent = new JButton("Edit Student");
        panel.add(btnEditStudent, BorderLayout.SOUTH);

        btnEditStudent.addActionListener(e -> {
            editStudent();
            loadStudents();
        });
        buttonPanel.add(btnEditStudent);

        panel.add(buttonPanel, BorderLayout.SOUTH); // Thêm JPanel con vào phía nam của BorderLayout

        return panel;
    }

    // Phiên bản không tham số
    private void updateStudentInfoPanel() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            // Chuyển đổi chỉ số hàng trong bảng sang chỉ số mô hình thực tế
            int modelRow = studentTable.convertRowIndexToModel(selectedRow);
            int studentID = (int) studentTableModel.getValueAt(modelRow, 0);
            updateStudentInfoPanel(studentID); // Sử dụng phiên bản có tham số
        } else {
            lblStudentInfo.setText("<html><br/><br/><br/>Select a student to see details.</html>");
        }
    }

    // Phiên bản nhận studentID
    private void updateStudentInfoPanel(int studentID) {
        Student student = new StudentDAO().getStudentById(studentID);

        StringBuilder info = new StringBuilder("<html>");
        info.append("<b>ID:</b> ").append(student.getStudentID()).append("<br/>");
        info.append("<b>Name:</b> ").append(student.getName()).append("<br/>");
        info.append("<b>Date of Birth:</b> ").append(student.getDateOfBirth()).append("<br/>");
        info.append("<b>Gender:</b> ").append(student.getGender()).append("<br/>");
        info.append("<b>Enrolled Subjects:</b> ").append("<br/>");

        List<Subject> subjects = new SubjectDAO().getSubjectsByStudentID(studentID);
        for (Subject subject : subjects) {
            info.append(subject.getSubjectName()).append(" (").append(subject.getCredits()).append(" credits)").append("<br/>");
        }
        info.append("</html>");
        lblStudentInfo.setText(info.toString());
    }

    private void assignCourseToStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            // Chuyển đổi chỉ số hàng trong bảng sang chỉ số mô hình thực tế
            int modelRow = studentTable.convertRowIndexToModel(selectedRow);
            int studentID = (int) studentTableModel.getValueAt(modelRow, 0);

            // Lấy danh sách các môn học
            List<Subject> allSubjects = new SubjectDAO().getAllSubjects();
            if (allSubjects.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No subjects available for enrollment.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Chuyển danh sách môn học thành mảng
            Subject[] subjectsArray = allSubjects.toArray(new Subject[0]);

            // Hiển thị hộp thoại chọn môn học
            JComboBox<Subject> subjectComboBox = new JComboBox<>(subjectsArray);
            subjectComboBox.setRenderer(new ListCellRenderer<Subject>() {
                @Override
                public Component getListCellRendererComponent(JList<? extends Subject> list, Subject value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = new JLabel(value.toString());
                    if (isSelected) {
                        label.setBackground(list.getSelectionBackground());
                        label.setForeground(list.getSelectionForeground());
                    } else {
                        label.setBackground(list.getBackground());
                        label.setForeground(list.getForeground());
                    }
                    label.setOpaque(true);
                    return label;
                }
            });

            int result = JOptionPane.showConfirmDialog(
                    this,
                    subjectComboBox,
                    "Select Subject to Enroll",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
                if (selectedSubject != null) {
                    // Kiểm tra xem sinh viên đã đăng ký môn học chưa
                    boolean success = new StudentDAO().assignCourseToStudent(studentID, selectedSubject.getCustomSubjectID());
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Subject enrolled successfully!");
                        updateStudentInfoPanel(studentID); // Cập nhật thông tin sinh viên
                    } else {
                        JOptionPane.showMessageDialog(this, "Student is already enrolled in this subject.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No subject selected.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to assign a course.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayAcademicRecord() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = studentTable.convertRowIndexToModel(selectedRow);
            int studentID = (int) studentTableModel.getValueAt(modelRow, 0);
            new DisplayAcademicRecordDialog(this, studentID).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createLecturerManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tạo bảng giảng viên
        lecturerTableModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        lecturerTable = new JTable(lecturerTableModel);
        lecturerTable.setRowHeight(25);
        lecturerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        lecturerTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        lecturerTable.getColumnModel().getColumn(0).setMinWidth(50);
        lecturerTable.getColumnModel().getColumn(0).setMaxWidth(50);

        // Căn giữa nội dung bảng
        centerTableData(lecturerTable);

        JScrollPane scrollPane = new JScrollPane(lecturerTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Thêm thanh tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lecturerSearchField = new JTextField(20);
        lecturerSearchField.setToolTipText("Search...");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(lecturerSearchField);

        // Bộ lọc bảng giảng viên
        TableRowSorter<DefaultTableModel> lecturerSorter = new TableRowSorter<>(lecturerTableModel);
        lecturerTable.setRowSorter(lecturerSorter);
        lecturerSearchField.getDocument().addDocumentListener(new SearchListener(lecturerSearchField, lecturerSorter));

        panel.add(searchPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAddLecturer = new JButton("Add Lecturer");
        btnDeleteLecturer = new JButton("Delete Lecturer");

        buttonPanel.add(btnAddLecturer);
        buttonPanel.add(btnDeleteLecturer);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Tạo bảng thông tin giảng viên
        JPanel lecturerInfoPanel = createLecturerInfoPanel();
        panel.add(lecturerInfoPanel, BorderLayout.EAST);

        // Nạp dữ liệu
        loadLecturers();

        // Thêm sự kiện
        btnAddLecturer.addActionListener(e -> {
            new AddLecturerDialog(UniversityManagementUI.this).setVisible(true);
            loadLecturers(); // Làm mới bảng sau khi thêm
        });

        btnDeleteLecturer.addActionListener(e -> {
            int selectedRow = lecturerTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = lecturerTable.convertRowIndexToModel(selectedRow);
                int lecturerID = (int) lecturerTableModel.getValueAt(modelRow, 0);
                boolean success = new LecturerDAO().deleteLecturer(lecturerID);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Lecturer and related subjects deleted successfully!");
                    loadLecturers(); // Làm mới bảng giảng viên sau khi xóa
                    loadSubjects(); // Làm mới bảng môn học sau khi xóa môn học liên quan
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot delete the lecturer as there are students enrolled in subjects taught by them.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a lecturer to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        lecturerTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                updateLecturerInfoPanel();
            }
        });

        return panel;
    }

    private JPanel createLecturerInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lecturer Info"));
        panel.setPreferredSize(new Dimension(300, 0));

        // Sử dụng JPanel với FlowLayout để căn trái thông tin
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblLecturerInfo = new JLabel();
        lblLecturerInfo.setVerticalAlignment(SwingConstants.TOP); // Đảm bảo văn bản bắt đầu từ đỉnh
        lblLecturerInfo.setFont(new Font("Tahoma", Font.PLAIN, 14)); // Đặt phông chữ cho nhãn
        infoPanel.add(lblLecturerInfo);
        panel.add(infoPanel, BorderLayout.CENTER);

        // Tạo JPanel con để chứa các nút
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5)); // 2 hàng, 1 cột, khoảng cách giữa các nút là 5 pixel

        JButton btnDisplayClasses = new JButton("Display Teaching Classes");
        btnDisplayClasses.addActionListener(e -> displayTeachingClasses());
        buttonPanel.add(btnDisplayClasses);

        btnEditLecturer = new JButton("Edit Lecturer");
        btnEditLecturer.addActionListener(e -> {
            editLecturer();
            loadLecturers();
        });
        buttonPanel.add(btnEditLecturer);

        panel.add(buttonPanel, BorderLayout.SOUTH); // Thêm JPanel con vào phía nam của BorderLayout

        return panel;
    }

    // Phiên bản không tham số
    private void updateLecturerInfoPanel() {
        int selectedRow = lecturerTable.getSelectedRow();
        if (selectedRow != -1) {
            // Chuyển đổi chỉ số hàng trong bảng sang chỉ số mô hình thực tế
            int modelRow = lecturerTable.convertRowIndexToModel(selectedRow);
            int lecturerID = (int) lecturerTableModel.getValueAt(modelRow, 0);
            updateLecturerInfoPanel(lecturerID); // Sử dụng phiên bản có tham số
        } else {
            lblLecturerInfo.setText("<html><br/><br/><br/>Select a lecturer to see details.</html>");
        }
    }

    // Phiên bản nhận lecturerID
    private void updateLecturerInfoPanel(int lecturerID) {
        Lecturer lecturer = new LecturerDAO().getLecturerById(lecturerID);

        StringBuilder info = new StringBuilder("<html>");
        info.append("<b>ID:</b> ").append(lecturer.getLecturerID()).append("<br/>");
        info.append("<b>Name:</b> ").append(lecturer.getName()).append("<br/>");
        info.append("<b>Date of Birth:</b> ").append(lecturer.getDateOfBirth()).append("<br/>");
        info.append("<b>Gender:</b> ").append(lecturer.getGender()).append("<br/>");
        info.append("</html>");
        lblLecturerInfo.setText(info.toString());
    }

    private void displayTeachingClasses() {
        int selectedRow = lecturerTable.getSelectedRow();
        if (selectedRow != -1) {
            // Chuyển đổi chỉ số hàng trong bảng sang chỉ số mô hình thực tế
            int modelRow = lecturerTable.convertRowIndexToModel(selectedRow);
            int lecturerID = (int) lecturerTableModel.getValueAt(modelRow, 0);

            // Lấy danh sách các môn học giảng viên đang dạy
            List<Subject> subjects = new SubjectDAO().getSubjectsByLecturerID(lecturerID);

            if (subjects.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No classes found for this lecturer.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Chuyển danh sách môn học thành mảng String để hiển thị trong bảng
            String[] columnNames = {"ID", "Subject Name", "Credits"};
            Object[][] data = new Object[subjects.size()][3];
            for (int i = 0; i < subjects.size(); i++) {
                data[i][0] = subjects.get(i).getCustomSubjectID();
                data[i][1] = subjects.get(i).getSubjectName();
                data[i][2] = subjects.get(i).getCredits();
            }

            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable table = new JTable(model);
            table.setRowHeight(25);

            // Căn giữa nội dung bảng
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(300, 200));

            JButton btnShowStudents = new JButton("Show Students");
            JButton btnClose = new JButton("Close");

            btnShowStudents.addActionListener(e -> {
                int selectedSubjectRow = table.getSelectedRow();
                if (selectedSubjectRow != -1) {
                    int modelSubjectRow = table.convertRowIndexToModel(selectedSubjectRow);
                    int customSubjectID = (int) model.getValueAt(modelSubjectRow, 0);
                    displayStudentsInClass(customSubjectID);
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a class to view students.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Nút "Close" để đóng hộp thoại
            btnClose.addActionListener(e -> {
                // Đóng hộp thoại
                Window window = SwingUtilities.getWindowAncestor(btnClose);
                if (window != null) {
                    window.dispose();
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(btnShowStudents);
            buttonPanel.add(btnClose);

            Dimension buttonSize = btnShowStudents.getPreferredSize();
            btnClose.setPreferredSize(buttonSize);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            JDialog dialog = new JDialog(this, "Classes Taught by Lecturer", true);
            dialog.getContentPane().add(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a lecturer to view their classes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayStudentsInClass(int customSubjectID) {
        List<Student> students = new StudentDAO().getStudentsBySubjectID(customSubjectID);

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students found for this class.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"ID", "Name"};
        Object[][] data = new Object[students.size()][2];
        for (int i = 0; i < students.size(); i++) {
            data[i][0] = students.get(i).getStudentID();
            data[i][1] = students.get(i).getName();
        }

        JTable table = new JTable(data, columnNames);
        table.setRowHeight(25);

        // Căn giữa nội dung bảng
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, panel, "Students in Class", JOptionPane.PLAIN_MESSAGE);
    }

    private JPanel createClassManagementPanel() {
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAddSubject = new JButton("Create Class");
        btnEditSubject = new JButton("Edit Class");
        btnDeleteSubject = new JButton("Delete Class");

        buttonPanel.add(btnAddSubject);
        buttonPanel.add(btnEditSubject);
        buttonPanel.add(btnDeleteSubject);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadSubjects();

        btnAddSubject.addActionListener(e -> {
            new CreateClassDialog(UniversityManagementUI.this).setVisible(true);
            loadSubjects(); // Làm mới bảng sau khi thêm
        });

        btnEditSubject.addActionListener(e -> {
            editSubject();
            loadSubjects();
        });

        btnDeleteSubject.addActionListener(e -> {
            int selectedRow = subjectTable.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = subjectTable.convertRowIndexToModel(selectedRow);
                int customSubjectID = (int) subjectTableModel.getValueAt(modelRow, 0);
                boolean success = new SubjectDAO().deleteSubject(customSubjectID);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Subject deleted successfully!");
                    loadSubjects(); // Làm mới bảng sau khi xóa
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot delete the subject as it is currently being taught by a lecturer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a subject to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
                    subject.getCustomSubjectID(),
                    subject.getSubjectName(),
                    subject.getLecturer().getName(),
                    subject.getCredits()
            });
        }
    }

    private void editStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            // Chuyển đổi chỉ số hàng trong bảng sang chỉ số mô hình thực tế
            int modelRow = studentTable.convertRowIndexToModel(selectedRow);
            int studentID = (int) studentTableModel.getValueAt(modelRow, 0);
            Student student = new StudentDAO().getStudentById(studentID);
            new EditStudentDialog(this, student).setVisible(true);
            // Cập nhật bảng sau khi chỉnh sửa
            loadStudents();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editLecturer() {
        int selectedRow = lecturerTable.getSelectedRow();
        if (selectedRow != -1) {
            // Chuyển đổi chỉ số hàng trong bảng sang chỉ số mô hình thực tế
            int modelRow = lecturerTable.convertRowIndexToModel(selectedRow);
            int lecturerID = (int) lecturerTableModel.getValueAt(modelRow, 0);
            Lecturer lecturer = new LecturerDAO().getLecturerById(lecturerID);
            new EditLecturerDialog(this, lecturer).setVisible(true);
            // Cập nhật bảng sau khi chỉnh sửa
            loadLecturers();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a lecturer to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSubject() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow != -1) {
            // Chuyển đổi chỉ số hàng trong bảng sang chỉ số mô hình thực tế
            int modelRow = subjectTable.convertRowIndexToModel(selectedRow);
            int customSubjectID = (int) subjectTableModel.getValueAt(modelRow, 0);  // Lấy ID của môn học
            Subject subject = new SubjectDAO().getSubjectByCustomID(customSubjectID);
            new EditClassDialog(this, subject).setVisible(true);
            loadSubjects();  // Làm mới bảng sau khi chỉnh sửa
        } else {
            JOptionPane.showMessageDialog(this, "Please select a subject to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UniversityManagementUI());
    }
}
