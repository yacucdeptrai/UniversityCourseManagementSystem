package main.java.com.university.dao;

import main.java.com.university.model.Grade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {
    public List<Grade> getGradesByStudentID(int studentID) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT student_id, custom_subject_id, score FROM grades WHERE student_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Grade grade = new Grade(
                            resultSet.getInt("student_id"),
                            resultSet.getInt("custom_subject_id"),
                            resultSet.getDouble("score")
                    );
                    grades.add(grade);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    public void updateGrade(int studentID, int customSubjectID, double score) {
        String sql = "INSERT INTO grades (student_id, custom_subject_id, score) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE score = VALUES(score)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentID);
            statement.setInt(2, customSubjectID);
            statement.setDouble(3, score);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
