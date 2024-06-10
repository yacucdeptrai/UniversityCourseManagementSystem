package main.java.com.university.dao;

import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    public void saveSubject(Subject subject) {
        String query = "INSERT INTO subjects (subject_id, subject_name, lecturer_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, subject.getSubjectID());
            stmt.setString(2, subject.getSubjectName());
            stmt.setInt(3, subject.getLecturer().getLecturerID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Subject getSubjectById(int subjectID) {
        String query = "SELECT * FROM subjects WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, subjectID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Subject(
                            rs.getInt("subject_id"),
                            rs.getString("subject_name"),
                            new LecturerDAO().getLecturerById(rs.getInt("lecturer_id"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT * FROM subjects";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                subjects.add(new Subject(
                        rs.getInt("subject_id"),
                        rs.getString("subject_name"),
                        new LecturerDAO().getLecturerById(rs.getInt("lecturer_id"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    // Các phương thức mới để quản lý đăng ký môn học

    public List<Subject> getSubjectsByStudentID(int studentID) {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT s.* FROM subjects s JOIN enrollments e ON s.subject_id = e.subject_id WHERE e.student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    subjects.add(new Subject(
                            rs.getInt("subject_id"),
                            rs.getString("subject_name"),
                            new LecturerDAO().getLecturerById(rs.getInt("lecturer_id"))
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public void enrollStudentInSubject(int studentID, int subjectID) {
        String query = "INSERT INTO enrollments (student_id, subject_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            stmt.setInt(2, subjectID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeStudentFromSubject(int studentID, int subjectID) {
        String query = "DELETE FROM enrollments WHERE student_id = ? AND subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            stmt.setInt(2, subjectID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
