package main.java.com.university.dao;

import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Subject;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT s.subject_id, s.name AS subject_name, s.credits, " +
                "l.lecturer_id, p.name AS lecturer_name, p.date_of_birth, p.gender " +
                "FROM subjects s " +
                "JOIN lecturers l ON s.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int subjectID = resultSet.getInt("subject_id");
                String subjectName = resultSet.getString("subject_name");
                int credits = resultSet.getInt("credits");

                int lecturerID = resultSet.getInt("lecturer_id");
                String lecturerName = resultSet.getString("lecturer_name");
                LocalDate lecturerDOB = resultSet.getDate("date_of_birth").toLocalDate();
                String lecturerGender = resultSet.getString("gender");

                Lecturer lecturer = new Lecturer(lecturerName, lecturerDOB, lecturerGender, lecturerID);
                Subject subject = new Subject(subjectID, subjectName, lecturer, credits);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public Subject getSubjectByName(String subjectName) {
        String sql = "SELECT s.subject_id, s.name AS subject_name, s.credits, " +
                "l.lecturer_id, p.name AS lecturer_name, p.date_of_birth, p.gender " +
                "FROM subjects s " +
                "JOIN lecturers l ON s.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id " +
                "WHERE s.subject_name = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, subjectName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int subjectID = resultSet.getInt("subject_id");
                    int credits = resultSet.getInt("credits");

                    int lecturerID = resultSet.getInt("lecturer_id");
                    String lecturerName = resultSet.getString("lecturer_name");
                    LocalDate lecturerDOB = resultSet.getDate("date_of_birth").toLocalDate();
                    String lecturerGender = resultSet.getString("gender");

                    Lecturer lecturer = new Lecturer(lecturerName, lecturerDOB, lecturerGender, lecturerID);
                    return new Subject(subjectID, subjectName, lecturer, credits);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy môn học theo ID sinh viên
    public List<Subject> getSubjectsByStudentID(int studentID) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT s.subject_id, s.name AS subject_name, s.credits, l.lecturer_id, p.name " +
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
        String sql = "SELECT s.subject_id, s.name AS subject_name, s.credits, " +
                "l.lecturer_id, p.name AS lecturer_name, p.date_of_birth, p.gender " +
                "FROM subjects s " +
                "JOIN lecturers l ON s.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id " +
                "WHERE s.subject_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, subjectID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String subjectName = resultSet.getString("subject_name");
                    int credits = resultSet.getInt("credits");

                    int lecturerID = resultSet.getInt("lecturer_id");
                    String lecturerName = resultSet.getString("lecturer_name");
                    LocalDate lecturerDOB = resultSet.getDate("date_of_birth").toLocalDate();
                    String lecturerGender = resultSet.getString("gender");

                    Lecturer lecturer = new Lecturer(lecturerName, lecturerDOB, lecturerGender, lecturerID);
                    return new Subject(subjectID, subjectName, lecturer, credits);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET name = ?, credits = ?, lecturer_id = ? WHERE subject_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, subject.getSubjectName());
            statement.setInt(2, subject.getCredits());
            statement.setInt(3, subject.getLecturer().getLecturerID());
            statement.setInt(4, subject.getSubjectID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveSubject(Subject subject) {
        String sql = "INSERT INTO subjects (name, credits, lecturer_id) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, subject.getSubjectName());
            statement.setInt(2, subject.getCredits());
            statement.setInt(3, subject.getLecturer().getLecturerID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubject(int subjectID) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, subjectID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void assignCourseToStudent(int studentID, int subjectID) {
        String sql = "INSERT INTO enrollments (student_id, subject_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentID);
            statement.setInt(2, subjectID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCourseFromStudent(int studentID, int subjectID) {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND subject_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentID);
            statement.setInt(2, subjectID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
