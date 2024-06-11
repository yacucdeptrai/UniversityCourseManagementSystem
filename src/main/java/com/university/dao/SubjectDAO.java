package main.java.com.university.dao;

import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {

    // Lấy danh sách tất cả các môn học
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String query = """
            SELECT s.subject_id, s.subject_name, s.credits, l.lecturer_id, l.name, l.date_of_birth, l.gender
            FROM subjects s
            JOIN lecturers l ON s.lecturer_id = l.lecturer_id
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int subjectID = resultSet.getInt("subject_id");
                String subjectName = resultSet.getString("subject_name");
                int credits = resultSet.getInt("credits");

                // Tạo đối tượng Lecturer với đầy đủ thông tin
                int lecturerID = resultSet.getInt("lecturer_id");
                String lecturerName = resultSet.getString("name");
                String lecturerDob = resultSet.getString("date_of_birth");
                String lecturerGender = resultSet.getString("gender");

                Lecturer lecturer = new Lecturer(lecturerName, java.time.LocalDate.parse(lecturerDob), lecturerGender, lecturerID);
                Subject subject = new Subject(subjectID, subjectName, lecturer, credits);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    // Lấy môn học theo ID
    public Subject getSubjectById(int subjectID) {
        Subject subject = null;
        String query = """
            SELECT s.subject_id, s.subject_name, s.credits, l.lecturer_id, l.name, l.date_of_birth, l.gender
            FROM subjects s
            JOIN lecturers l ON s.lecturer_id = l.lecturer_id
            WHERE s.subject_id = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, subjectID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String subjectName = resultSet.getString("subject_name");
                int credits = resultSet.getInt("credits");

                // Tạo đối tượng Lecturer với đầy đủ thông tin
                int lecturerID = resultSet.getInt("lecturer_id");
                String lecturerName = resultSet.getString("name");
                String lecturerDob = resultSet.getString("date_of_birth");
                String lecturerGender = resultSet.getString("gender");

                Lecturer lecturer = new Lecturer(lecturerName, java.time.LocalDate.parse(lecturerDob), lecturerGender, lecturerID);
                subject = new Subject(subjectID, subjectName, lecturer, credits);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subject;
    }

    // Lấy danh sách môn học theo ID sinh viên
    public List<Subject> getSubjectsByStudentID(int studentID) {
        List<Subject> subjects = new ArrayList<>();
        String query = """
            SELECT s.subject_id, s.subject_name, s.credits, l.lecturer_id, l.name, l.date_of_birth, l.gender
            FROM subjects s
            JOIN enrollments e ON s.subject_id = e.subject_id
            JOIN lecturers l ON s.lecturer_id = l.lecturer_id
            WHERE e.student_id = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, studentID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int subjectID = resultSet.getInt("subject_id");
                String subjectName = resultSet.getString("subject_name");
                int credits = resultSet.getInt("credits");

                // Tạo đối tượng Lecturer với đầy đủ thông tin
                int lecturerID = resultSet.getInt("lecturer_id");
                String lecturerName = resultSet.getString("name");
                String lecturerDob = resultSet.getString("date_of_birth");
                String lecturerGender = resultSet.getString("gender");

                Lecturer lecturer = new Lecturer(lecturerName, java.time.LocalDate.parse(lecturerDob), lecturerGender, lecturerID);
                Subject subject = new Subject(subjectID, subjectName, lecturer, credits);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    // Xóa môn học
    public boolean deleteSubject(int subjectID) {
        String query = "DELETE FROM subjects WHERE subject_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, subjectID);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật môn học
    public boolean updateSubject(Subject subject) {
        String query = """
            UPDATE subjects
            SET subject_name = ?, credits = ?, lecturer_id = ?
            WHERE subject_id = ?
        """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, subject.getSubjectName());
            preparedStatement.setInt(2, subject.getCredits());
            preparedStatement.setInt(3, subject.getLecturer().getLecturerID());
            preparedStatement.setInt(4, subject.getSubjectID());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lưu môn học mới
    public boolean saveSubject(Subject subject) {
        String query = """
            INSERT INTO subjects (subject_id, subject_name, credits, lecturer_id)
            VALUES (?, ?, ?, ?)
        """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, subject.getSubjectID());
            preparedStatement.setString(2, subject.getSubjectName());
            preparedStatement.setInt(3, subject.getCredits());
            preparedStatement.setInt(4, subject.getLecturer().getLecturerID());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
