package main.java.com.university.dao;

import main.java.com.university.model.Lecturer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {
    public List<Lecturer> getAllLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT l.lecturer_id, p.name, p.date_of_birth, p.gender " +
                "FROM lecturers l " +
                "JOIN persons p ON l.person_id = p.id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int lecturerID = resultSet.getInt("lecturer_id");
                String name = resultSet.getString("name");
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                String gender = resultSet.getString("gender");

                Lecturer lecturer = new Lecturer(name, dateOfBirth, gender, lecturerID);
                lecturers.add(lecturer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturers;
    }

    public Lecturer getLecturerById(int lecturerID) {
        String sql = "SELECT l.lecturer_id, p.name, p.date_of_birth, p.gender " +
                "FROM lecturers l " +
                "JOIN persons p ON l.person_id = p.id " +
                "WHERE l.lecturer_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lecturerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                    String gender = resultSet.getString("gender");

                    return new Lecturer(name, dateOfBirth, gender, lecturerID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveLecturer(Lecturer lecturer) {
        String personSql = "INSERT INTO persons (name, date_of_birth, gender) VALUES (?, ?, ?)";
        String lecturerSql = "INSERT INTO lecturers (lecturer_id, person_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement personStmt = conn.prepareStatement(personSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement lecturerStmt = conn.prepareStatement(lecturerSql)) {

            conn.setAutoCommit(false);

            // Lưu thông tin vào bảng persons
            personStmt.setString(1, lecturer.getName());
            personStmt.setDate(2, Date.valueOf(lecturer.getDateOfBirth()));
            personStmt.setString(3, lecturer.getGender());
            personStmt.executeUpdate();

            ResultSet rs = personStmt.getGeneratedKeys();
            if (rs.next()) {
                int personID = rs.getInt(1);

                // Lưu thông tin vào bảng lecturers
                lecturerStmt.setInt(1, lecturer.getLecturerID());
                lecturerStmt.setInt(2, personID);
                lecturerStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLecturer(Lecturer lecturer) {
        String sql = "UPDATE persons SET name = ?, date_of_birth = ?, gender = ? " +
                "WHERE id = (SELECT person_id FROM lecturers WHERE lecturer_id = ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, lecturer.getName());
            preparedStatement.setDate(2, Date.valueOf(lecturer.getDateOfBirth()));
            preparedStatement.setString(3, lecturer.getGender());
            preparedStatement.setInt(4, lecturer.getLecturerID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa giảng viên
    public boolean deleteLecturer(int lecturerID) {
        String checkStudentEnrollmentsSQL = "SELECT COUNT(*) " +
                "FROM enrollments e " +
                "JOIN custom_subjects cs ON e.custom_subject_id = cs.custom_subject_id " +
                "JOIN auto_subjects a ON cs.auto_subject_id = a.auto_subject_id " +
                "WHERE a.lecturer_id = ?";
        String deleteEnrollmentsSQL = "DELETE FROM enrollments WHERE custom_subject_id IN (SELECT custom_subject_id FROM custom_subjects WHERE auto_subject_id IN (SELECT auto_subject_id FROM auto_subjects WHERE lecturer_id = ?))";
        String deleteCustomSubjectsSQL = "DELETE FROM custom_subjects WHERE auto_subject_id IN (SELECT auto_subject_id FROM auto_subjects WHERE lecturer_id = ?)";
        String deleteAutoSubjectsSQL = "DELETE FROM auto_subjects WHERE lecturer_id = ?";
        String deleteLecturerSQL = "DELETE FROM lecturers WHERE lecturer_id = ?";
        String deletePersonSQL = "DELETE FROM persons WHERE id = (SELECT person_id FROM lecturers WHERE lecturer_id = ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStudentEnrollmentsStatement = connection.prepareStatement(checkStudentEnrollmentsSQL);
             PreparedStatement deleteEnrollmentsStatement = connection.prepareStatement(deleteEnrollmentsSQL);
             PreparedStatement deleteCustomSubjectsStatement = connection.prepareStatement(deleteCustomSubjectsSQL);
             PreparedStatement deleteAutoSubjectsStatement = connection.prepareStatement(deleteAutoSubjectsSQL);
             PreparedStatement deleteLecturerStatement = connection.prepareStatement(deleteLecturerSQL);
             PreparedStatement deletePersonStatement = connection.prepareStatement(deletePersonSQL)) {

            // Kiểm tra nếu có sinh viên đăng ký môn học của giảng viên
            checkStudentEnrollmentsStatement.setInt(1, lecturerID);
            try (ResultSet resultSet = checkStudentEnrollmentsStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return false; // Không xóa giảng viên nếu có sinh viên đăng ký môn học của giảng viên
                }
            }

            // Xóa các đăng ký môn học liên quan từ bảng enrollments
            deleteEnrollmentsStatement.setInt(1, lecturerID);
            deleteEnrollmentsStatement.executeUpdate();

            // Xóa các môn học liên quan từ bảng custom_subjects
            deleteCustomSubjectsStatement.setInt(1, lecturerID);
            deleteCustomSubjectsStatement.executeUpdate();

            // Xóa các môn học liên quan từ bảng auto_subjects
            deleteAutoSubjectsStatement.setInt(1, lecturerID);
            deleteAutoSubjectsStatement.executeUpdate();

            // Xóa giảng viên từ bảng lecturers
            deleteLecturerStatement.setInt(1, lecturerID);
            deleteLecturerStatement.executeUpdate();

            // Xóa thông tin cá nhân của giảng viên từ bảng persons
            deletePersonStatement.setInt(1, lecturerID);
            deletePersonStatement.executeUpdate();

            return true; // Xóa giảng viên thành công

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
