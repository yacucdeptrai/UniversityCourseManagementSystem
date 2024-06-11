package main.java.com.university.dao;

import main.java.com.university.model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT students.student_id, persons.name, persons.date_of_birth, persons.gender " +
                "FROM students JOIN persons ON students.person_id = persons.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int studentID = rs.getInt("student_id");
                String name = rs.getString("name");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String gender = rs.getString("gender");
                students.add(new Student(studentID, name, dateOfBirth, gender));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void saveStudent(Student student) {
        String personSql = "INSERT INTO persons (name, date_of_birth, gender) VALUES (?, ?, ?)";
        String studentSql = "INSERT INTO students (student_id, person_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement personStmt = conn.prepareStatement(personSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement studentStmt = conn.prepareStatement(studentSql)) {
            // Insert into persons table
            personStmt.setString(1, student.getName());
            personStmt.setDate(2, Date.valueOf(student.getDateOfBirth()));
            personStmt.setString(3, student.getGender());
            personStmt.executeUpdate();
            try (ResultSet generatedKeys = personStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int personID = generatedKeys.getInt(1);
                    // Insert into students table
                    studentStmt.setInt(1, student.getStudentID());
                    studentStmt.setInt(2, personID);
                    studentStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(Student student) {
        String personSql = "UPDATE persons SET name = ?, date_of_birth = ?, gender = ? " +
                "WHERE id = (SELECT person_id FROM students WHERE student_id = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement personStmt = conn.prepareStatement(personSql)) {
            personStmt.setString(1, student.getName());
            personStmt.setDate(2, Date.valueOf(student.getDateOfBirth()));
            personStmt.setString(3, student.getGender());
            personStmt.setInt(4, student.getStudentID());
            personStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int studentID) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student getStudentById(int studentID) {
        Student student = null;
        String sql = "SELECT students.student_id, persons.name, persons.date_of_birth, persons.gender " +
                "FROM students JOIN persons ON students.person_id = persons.id WHERE students.student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                    String gender = rs.getString("gender");
                    student = new Student(studentID, name, dateOfBirth, gender);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public void enrollStudentInSubject(int studentID, int subjectID) {
        String sql = "INSERT INTO enrollments (student_id, subject_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentID);
            pstmt.setInt(2, subjectID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeStudentFromSubject(int studentID, int subjectID) {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentID);
            pstmt.setInt(2, subjectID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
