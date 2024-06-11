package main.java.com.university.dao;

import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int subjectID = rs.getInt("subject_id");
                String subjectName = rs.getString("subject_name");
                int lecturerID = rs.getInt("lecturer_id");
                Lecturer lecturer = new LecturerDAO().getLecturerById(lecturerID);
                subjects.add(new Subject(subjectID, subjectName, lecturer));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public Subject getSubjectById(int subjectID) {
        Subject subject = null;
        String sql = "SELECT * FROM subjects WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subjectID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String subjectName = rs.getString("subject_name");
                    int lecturerID = rs.getInt("lecturer_id");
                    Lecturer lecturer = new LecturerDAO().getLecturerById(lecturerID);
                    subject = new Subject(subjectID, subjectName, lecturer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subject;
    }

    public void saveSubject(Subject subject) {
        String sql = "INSERT INTO subjects (subject_id, subject_name, lecturer_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subject.getSubjectID());
            pstmt.setString(2, subject.getSubjectName());
            pstmt.setInt(3, subject.getLecturer().getLecturerID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET subject_name = ?, lecturer_id = ? WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subject.getSubjectName());
            pstmt.setInt(2, subject.getLecturer().getLecturerID());
            pstmt.setInt(3, subject.getSubjectID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubject(int subjectID) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, subjectID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
