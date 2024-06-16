package main.java.com.university.dao;

import main.java.com.university.model.Grade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDAO {
    public List<Grade> getGradesByStudentID(int studentID) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE student_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Grade grade = new Grade();
                    grade.setGradeID(resultSet.getInt("grade_id"));
                    grade.setStudentID(resultSet.getInt("student_id"));
                    grade.setCustomSubjectID(resultSet.getInt("custom_subject_id"));
                    grade.setScore(resultSet.getDouble("score"));
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
