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
    public Subject getSubjectById(int subjectId) {
        String query = "SELECT subjects.*, lecturers.* FROM subjects JOIN lecturers ON subjects.lecturer_id = lecturers.lecturer_id WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LecturerDAO lecturerDAO = new LecturerDAO();
                    Lecturer lecturer = lecturerDAO.getLecturerById(rs.getInt("lecturer_id"));
                    return new Subject(
                            rs.getInt("subject_id"),
                            rs.getString("name"),
                            rs.getInt("credit"),
                            lecturer
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
        String query = "SELECT subjects.*, lecturers.* FROM subjects JOIN lecturers ON subjects.lecturer_id = lecturers.lecturer_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LecturerDAO lecturerDAO = new LecturerDAO();
                Lecturer lecturer = lecturerDAO.getLecturerById(rs.getInt("lecturer_id"));
                subjects.add(new Subject(
                        rs.getInt("subject_id"),
                        rs.getString("name"),
                        rs.getInt("credit"),
                        lecturer
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

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

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String query = "SELECT * FROM subjects";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                LecturerDAO lecturerDAO = new LecturerDAO();
                Lecturer lecturer = lecturerDAO.getLecturerById(rs.getInt("lecturer_id"));
                Subject subject = new Subject(
                        rs.getInt("subject_id"),
                        rs.getString("subject_name"),
                        lecturer
                );
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public void updateSubject(Subject subject) {
        String query = "UPDATE subjects SET subject_name = ?, lecturer_id = ? WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, subject.getSubjectName());
            stmt.setInt(2, subject.getLecturer().getLecturerID());
            stmt.setInt(3, subject.getSubjectID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubject(int subjectID) {
        String query = "DELETE FROM subjects WHERE subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, subjectID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
