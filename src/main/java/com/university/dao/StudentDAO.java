package main.java.com.university.dao;

import main.java.com.university.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.student_id, p.name, p.date_of_birth, p.gender " +
                "FROM students s " +
                "JOIN persons p ON s.person_id = p.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int studentID = rs.getInt("student_id");
                String name = rs.getString("name");
                Date dob = rs.getDate("date_of_birth");
                String gender = rs.getString("gender");

                Student student = new Student(studentID, name, dob.toLocalDate(), gender);
                students.add(student);
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
            // Them vao bang persons
            personStmt.setString(1, student.getName());
            personStmt.setDate(2, Date.valueOf(student.getDateOfBirth()));
            personStmt.setString(3, student.getGender());
            personStmt.executeUpdate();
            try (ResultSet generatedKeys = personStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int personID = generatedKeys.getInt(1);
                    // Them vao bang sinh vien
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

    private boolean isStudentEnrolledInAnyClass(int studentID) {
        String sql = "SELECT COUNT(*) AS count FROM enrollments WHERE student_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteStudent(int studentID) {
        if (isStudentEnrolledInAnyClass(studentID)) {
            return false; // Không thể xóa sinh viên đang tham gia lớp học
        }
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentID);
            statement.executeUpdate();
            return true; // Xóa thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Xóa không thành công do lỗi
        }
    }

    public Student getStudentById(int studentID) {
        Student student = null;
        String sql = "SELECT s.student_id, p.name, p.date_of_birth, p.gender " +
                "FROM students s " +
                "JOIN persons p ON s.person_id = p.id " +
                "WHERE s.student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                Date dob = rs.getDate("date_of_birth");
                String gender = rs.getString("gender");

                student = new Student(studentID, name, dob.toLocalDate(), gender);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public List<Student> getStudentsBySubjectID(int subjectID) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.student_id, p.name, p.date_of_birth, p.gender " +
                "FROM enrollments e " +
                "JOIN students s ON e.student_id = s.student_id " +
                "JOIN persons p ON s.person_id = p.id " +
                "WHERE e.custom_subject_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subjectID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int studentID = rs.getInt("student_id");
                    String name = rs.getString("name");
                    Date dob = rs.getDate("date_of_birth");
                    String gender = rs.getString("gender");

                    Student student = new Student(studentID, name, dob.toLocalDate(), gender);
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void assignCourseToStudent(int studentID, int customSubjectID) {
        String sql = "INSERT INTO enrollments (student_id, custom_subject_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentID);
            statement.setInt(2, customSubjectID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCourseFromStudent(int studentID, int customSubjectID) {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND custom_subject_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentID);
            statement.setInt(2, customSubjectID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
