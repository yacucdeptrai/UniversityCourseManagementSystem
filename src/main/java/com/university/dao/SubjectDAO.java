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
        String sql = "SELECT s.subject_id, s.subject_name, s.credits, " +
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
        String sql = "SELECT s.subject_id, s.subject_name, s.credits, " +
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
        String sql = "SELECT s.subject_id, s.subject_name, s.credits, l.lecturer_id, p.name " +
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
        String sql = "SELECT s.subject_id, s.subject_name, s.credits, " +
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


    public boolean addSubject(Subject subject) {
        String sql = "INSERT INTO subjects (subject_name, credits, lecturer_id) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, subject.getSubjectName());
            statement.setInt(2, subject.getCredits());
            statement.setInt(3, subject.getLecturer().getLecturerID());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET subject_name = ?, credits = ?, lecturer_id = ? WHERE subject_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, subject.getSubjectName());
            statement.setInt(2, subject.getCredits());
            statement.setInt(3, subject.getLecturer().getLecturerID());
            statement.setInt(4, subject.getSubjectID());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lưu môn học (cập nhật hoặc thêm mới)
    public void saveSubject(Subject subject) {
        String checkSql = "SELECT COUNT(*) FROM subjects WHERE subject_id = ?";
        boolean exists = false;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, subject.getSubjectID());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
            if (exists) {
                // Cập nhật môn học nếu đã tồn tại
                String updateSql = "UPDATE subjects SET subject_name = ?, credits = ?, lecturer_id = ? WHERE subject_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, subject.getSubjectName());
                    updateStmt.setInt(2, subject.getCredits());
                    updateStmt.setInt(3, subject.getLecturer().getLecturerID());
                    updateStmt.setInt(4, subject.getSubjectID());
                    updateStmt.executeUpdate();
                }
            } else {
                // Thêm mới môn học nếu không tồn tại
                String insertSql = "INSERT INTO subjects (subject_id, subject_name, credits, lecturer_id) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, subject.getSubjectID());
                    insertStmt.setString(2, subject.getSubjectName());
                    insertStmt.setInt(3, subject.getCredits());
                    insertStmt.setInt(4, subject.getLecturer().getLecturerID());
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa môn học
    public boolean deleteSubject(int subjectID) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, subjectID);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void assignCourseToStudent(int studentID, int subjectID) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Kiểm tra xem sinh viên đã đăng ký khóa học này chưa
            String checkSql = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND subject_id = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSql);
            checkStatement.setInt(1, studentID);
            checkStatement.setInt(2, subjectID);
            ResultSet rs = checkStatement.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                // Nếu sinh viên đã đăng ký khóa học này, hiển thị thông báo và không thêm mới
                System.out.println("Student has already enrolled in this course.");
                return;
            }
            // Nếu chưa đăng ký, thêm mới vào bảng enrollments
            String sql = "INSERT INTO enrollments (student_id, subject_id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, studentID);
            statement.setInt(2, subjectID);
            statement.executeUpdate();
            System.out.println("Course assigned successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
