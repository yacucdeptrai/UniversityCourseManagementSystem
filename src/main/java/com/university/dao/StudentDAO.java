package main.java.com.university.dao;

import main.java.com.university.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public void saveStudent(Student student) {
        String query = "INSERT INTO students (student_id, person_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, student.getStudentID());
            stmt.setInt(2, student.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student getStudentById(int studentID) {
        String query = "SELECT students.*, persons.* FROM students JOIN persons ON students.person_id = persons.id WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getString("name"),
                            rs.getDate("date_of_birth").toLocalDate(),
                            rs.getString("gender"),
                            rs.getInt("student_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT students.*, persons.* FROM students JOIN persons ON students.person_id = persons.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getString("name"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("gender"),
                        rs.getInt("student_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void updateStudent(Student student) {
        String query = "UPDATE students SET person_id = ? WHERE student_id = ?";
        String personQuery = "UPDATE persons SET name = ?, date_of_birth = ?, gender = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(personQuery)) {
                stmt.setString(1, student.getName());
                stmt.setDate(2, java.sql.Date.valueOf(student.getDateOfBirth()));
                stmt.setString(3, student.getGender());
                stmt.setInt(4, student.getId());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, student.getId());
                stmt.setInt(2, student.getStudentID());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int studentID) {
        String query = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
