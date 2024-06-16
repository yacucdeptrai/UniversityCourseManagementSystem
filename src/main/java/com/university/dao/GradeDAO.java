package main.java.com.university.dao;

import main.java.com.university.model.Grade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {
    public void addGrade(Grade grade) {
        String sql = "INSERT INTO grades (student_id, custom_subject_id, score) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, grade.getStudentID());
            stmt.setInt(2, grade.getCustomSubjectID());
            stmt.setDouble(3, grade.getScore());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Grade> getGradesByStudentID(int studentID) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Grade grade = new Grade();
                    grade.setGradeID(rs.getInt("grade_id"));
                    grade.setStudentID(rs.getInt("student_id"));
                    grade.setCustomSubjectID(rs.getInt("custom_subject_id"));
                    grade.setScore(rs.getDouble("score"));
                    grades.add(grade);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    public void updateGrade(int studentID, int customSubjectID, double newScore) {
        String sql = "UPDATE grades SET score = ? WHERE student_id = ? AND custom_subject_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newScore);
            statement.setInt(2, studentID);
            statement.setInt(3, customSubjectID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
