package main.java.com.university.dao;

import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    // Lấy tất cả các môn học
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT s.subject_id, s.subject_name, s.credits, l.lecturer_id, p.name " +
                "FROM subjects s " +
                "JOIN lecturers l ON s.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int subjectID = rs.getInt("subject_id");
                String subjectName = rs.getString("subject_name");
                int credits = rs.getInt("credits");
                int lecturerID = rs.getInt("lecturer_id");
                String lecturerName = rs.getString("name");

                Lecturer lecturer = new LecturerDAO().getLecturerById(lecturerID); // Sử dụng Lecturer đơn giản
                Subject subject = new Subject(subjectID, subjectName, lecturer, credits);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    // Lấy môn học theo ID sinh viên
    public List<Subject> getSubjectsByStudentID(int studentID) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT s.subject_id, s.subject_name, s.credits, l.lecturer_id, p.name " +
                "FROM subjects s " +
                "JOIN enrollments e ON s.subject_id = e.subject_id " +
                "JOIN lecturers l ON s.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id " +
                "WHERE e.student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int subjectID = rs.getInt("subject_id");
                    String subjectName = rs.getString("subject_name");
                    int credits = rs.getInt("credits");
                    int lecturerID = rs.getInt("lecturer_id");
                    String lecturerName = rs.getString("name");

                    Lecturer lecturer = new LecturerDAO().getLecturerById(lecturerID);
                    Subject subject = new Subject(subjectID, subjectName, lecturer, credits);
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    // Lấy môn học theo ID
    public Subject getSubjectById(int subjectID) {
        Subject subject = null;
        String sql = "SELECT s.subject_id, s.subject_name, s.credits, l.lecturer_id, p.name " +
                "FROM subjects s " +
                "JOIN lecturers l ON s.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id " +
                "WHERE s.subject_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String subjectName = rs.getString("subject_name");
                    int credits = rs.getInt("credits");
                    int lecturerID = rs.getInt("lecturer_id");
                    String lecturerName = rs.getString("name");

                    Lecturer lecturer = new LecturerDAO().getLecturerById(lecturerID);
                    subject = new Subject(subjectID, subjectName, lecturer, credits);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subject;
    }

    public void updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET subject_name = ?, credits = ?, lecturer_id = ? WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, subject.getSubjectName());
            stmt.setInt(2, subject.getCredits());
            stmt.setInt(3, subject.getLecturer().getLecturerID());
            stmt.setInt(4, subject.getSubjectID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lưu môn học (cập nhật hoặc thêm mới)
    public void saveSubject(Subject subject) {
        String checkSql = "SELECT COUNT(*) FROM subjects WHERE subject_id = ?";
        boolean exists = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, subject.getSubjectID());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }

            if (exists) {
                // Cập nhật môn học nếu đã tồn tại
                String updateSql = "UPDATE subjects SET subject_name = ?, credits = ?, lecturer_id = ? WHERE subject_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, subject.getSubjectName());
                    updateStmt.setInt(2, subject.getCredits());
                    updateStmt.setInt(3, subject.getLecturer().getLecturerID());
                    updateStmt.setInt(4, subject.getSubjectID());
                    updateStmt.executeUpdate();
                }
            } else {
                // Thêm mới môn học nếu không tồn tại
                String insertSql = "INSERT INTO subjects (subject_id, subject_name, credits, lecturer_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, subject.getSubjectID());
                    insertStmt.setString(2, subject.getSubjectName());
                    insertStmt.setInt(3, subject.getCredits());
                    insertStmt.setInt(4, subject.getLecturer().getLecturerID());
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa môn học
    public void deleteSubject(int subjectID) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, subjectID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
