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
    private int studentID;

    public DisplayAcademicRecordDialog(Frame parent, int studentID) {
        super(parent, "Academic Record", true);
        this.studentID = studentID;
        initializeUI();
        loadAcademicRecords();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(800, 500);
        setLocationRelativeTo(getParent());

        // Tạo bảng điểm
        gradeTableModel = new DefaultTableModel(new String[]{"ID", "Subject Name", "Credits", "Score", "Status"}, 0);
        gradeTable = new JTable(gradeTableModel);
        gradeTable.setRowHeight(25);

        // Tạo đường viền cho tiêu đề và các ô
        gradeTable.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gradeTable.setGridColor(Color.LIGHT_GRAY);
        gradeTable.setShowGrid(true);

        JScrollPane scrollPane = new JScrollPane(gradeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Định dạng các cột
        gradeTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        gradeTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Subject Name
        gradeTable.getColumnModel().getColumn(2).setPreferredWidth(50); // Credits
        gradeTable.getColumnModel().getColumn(3).setPreferredWidth(50); // Score
        gradeTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Status

        // Định dạng header và cell
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < gradeTable.getColumnCount(); i++) {
            gradeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Panel hiển thị GPA
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("GPA Summary"));
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
                totalScore += score;
                if (score >= 4) {
                    totalEarnedCredits += credits;
                    totalScoreEarned += score * credits;
                }
            }
        }

        double gpa = enrolledSubjects.size() > 0 ? totalScore / enrolledSubjects.size() : 0;
        double cumulativeGpa = totalEarnedCredits != 0 ? totalScoreEarned / totalEarnedCredits : 0;

        JLabel lblSummary = new JLabel(String.format(
                "<html><b>Total Credits:</b> %.2f<br/>" +
                        "<b>Earned Credits:</b> %.2f<br/>" +
                        "<b>GPA (10-scale):</b> %.2f<br/>" +
                        "<b>GPA (4-scale):</b> %.2f<br/>" +
                        "<b>Cumulative GPA (10-scale):</b> %.2f<br/>" +
                        "<b>Cumulative GPA (4-scale):</b> %.2f</html>",
                totalCredits,
                totalEarnedCredits,
                gpa,
                gpa / 2.5,
                cumulativeGpa,
                cumulativeGpa / 2.5
        ), SwingConstants.LEFT);

        lblSummary.setFont(new Font("Tahoma", Font.BOLD, 14));

        JPanel summaryPanel = (JPanel) getContentPane().getComponent(1);
        summaryPanel.removeAll();
        summaryPanel.add(lblSummary);
        summaryPanel.revalidate();
        summaryPanel.repaint();
    }

    private void updateGrade() {
        int selectedRow = gradeTable.getSelectedRow();
        if (selectedRow != -1) {
            // Lấy customSubjectID và hiện hộp thoại nhập điểm mới
            int customSubjectID = (int) gradeTableModel.getValueAt(selectedRow, 0);
            String subjectName = (String) gradeTableModel.getValueAt(selectedRow, 1);
            String newScoreString = JOptionPane.showInputDialog(this, "Enter new grade for " + subjectName + ":");
            if (newScoreString != null && !newScoreString.trim().isEmpty()) {
                try {
                    double newScore = Double.parseDouble(newScoreString);
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