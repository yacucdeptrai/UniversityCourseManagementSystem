package main.java.com.university.ui;

import main.java.com.university.dao.GradeDAO;
import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Grade;
import main.java.com.university.model.Subject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DisplayAcademicRecordDialog extends JDialog {
    private JTable gradeTable;
    private DefaultTableModel gradeTableModel;
    private JLabel lblSummary;
    private int studentID;

    public DisplayAcademicRecordDialog(Frame parent, int studentID) {
        super(parent, "Academic Record", true);
        this.studentID = studentID;
        initializeUI();
        loadAcademicRecords();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(getParent());

        // Tạo bảng điểm
        gradeTableModel = new DefaultTableModel(new String[]{"ID", "Subject Name", "Credits", "Score", "Status"}, 0);
        gradeTable = new JTable(gradeTableModel);
        gradeTable.setRowHeight(25);
        gradeTable.setGridColor(Color.LIGHT_GRAY);
        gradeTable.setShowGrid(true);

        JScrollPane scrollPane = new JScrollPane(gradeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Định dạng các cột
        gradeTable.getColumnModel().getColumn(0).setPreferredWidth(20); // ID
        gradeTable.getColumnModel().getColumn(1).setPreferredWidth(80); // Subject Name
        gradeTable.getColumnModel().getColumn(2).setPreferredWidth(30); // Credits
        gradeTable.getColumnModel().getColumn(3).setPreferredWidth(30); // Score
        gradeTable.getColumnModel().getColumn(4).setPreferredWidth(50); // Status

        // Định dạng header và cell
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < gradeTable.getColumnCount(); i++) {
            gradeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Panel hiển thị GPA
        lblSummary = new JLabel("", SwingConstants.LEFT);
        lblSummary.setFont(new Font("Tahoma", Font.BOLD, 14));
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("GPA Summary"));
        summaryPanel.add(lblSummary);
        add(summaryPanel, BorderLayout.SOUTH);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnUpdateGrade = new JButton("Update Grade");
        JButton btnClose = new JButton("Close");

        btnUpdateGrade.addActionListener(e -> updateGrade());
        btnClose.addActionListener(e -> dispose());

        buttonPanel.add(btnUpdateGrade);
        buttonPanel.add(btnClose);
        add(buttonPanel, BorderLayout.NORTH);
    }

    private void loadAcademicRecords() {
        List<Subject> enrolledSubjects = new SubjectDAO().getSubjectsByStudentID(studentID);
        List<Grade> grades = new GradeDAO().getGradesByStudentID(studentID);

        // Tạo bản đồ để tra cứu điểm dựa trên customSubjectID
        Map<Integer, Double> gradeMap = grades.stream()
                .collect(Collectors.toMap(Grade::getCustomSubjectID, Grade::getScore));

        // Điền dữ liệu vào bảng
        double totalCredits = 0;
        double totalScore = 0;
        double totalEarnedCredits = 0;
        double totalScoreEarned = 0;

        gradeTableModel.setRowCount(0); // Xóa tất cả các dòng hiện có

        for (Subject subject : enrolledSubjects) {
            int customSubjectID = subject.getCustomSubjectID();
            double credits = subject.getCredits();
            Double score = gradeMap.get(customSubjectID); // Sử dụng map để tra cứu điểm
            String status = score != null && score >= 4 ? "Passed" : (score != null ? "Failed" : "");

            gradeTableModel.addRow(new Object[]{
                    customSubjectID,
                    subject.getSubjectName(),
                    credits,
                    score != null ? score : "", // Hiển thị điểm hoặc để trống
                    status
            });

            totalCredits += credits;
            if (score != null) {
                totalScore += score * credits;
                if (score >= 4) {
                    totalEarnedCredits += credits;
                    totalScoreEarned += credits;
                }
            }
        }

        double gpa = totalCredits > 0 ? totalScore / totalCredits : 0;
        double gpa4 = gpa / 2.5; // Giả định chuyển đổi thang điểm 10 sang 4 là chia cho 2.5

        lblSummary.setText(String.format(
                "<html>Total Credits: %.2f<br/>" +
                        "Earned Credits: %.2f<br/>" +
                        "GPA (10-scale): %.2f<br/>" +
                        "GPA (4-scale): %.2f<br/>" +
                        "Cumulative GPA (10-scale): %.2f<br/>" +
                        "Cumulative GPA (4-scale): %.2f</html>",
                totalCredits,
                totalEarnedCredits,
                gpa,
                gpa4,
                gpa,
                gpa4
        ));
    }

    private void updateGrade() {
        int selectedRow = gradeTable.getSelectedRow();
        if (selectedRow != -1) {
            // Lấy customSubjectID và hiện hộp thoại nhập điểm mới
            int customSubjectID = (int) gradeTableModel.getValueAt(selectedRow, 0);
            String subjectName = (String) gradeTableModel.getValueAt(selectedRow, 1);
            String currentScore = gradeTableModel.getValueAt(selectedRow, 3).toString();

            UpdateGradeDialog dialog = new UpdateGradeDialog(this, subjectName, currentScore);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                try {
                    double newScore = dialog.getGrade();
                    if (newScore < 0 || newScore > 10) {
                        JOptionPane.showMessageDialog(this, "Grade must be between 0 and 10.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Cập nhật điểm trong CSDL
                    new GradeDAO().updateGrade(studentID, customSubjectID, newScore);

                    // Cập nhật bảng hiển thị
                    gradeTableModel.setValueAt(newScore, selectedRow, 3);
                    String status = newScore >= 4 ? "Passed" : "Failed";
                    gradeTableModel.setValueAt(status, selectedRow, 4);
                    JOptionPane.showMessageDialog(this, "Grade updated successfully!");

                    // Tính lại GPA và cập nhật tổng kết
                    loadAcademicRecords();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid grade format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a subject to update grade.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}