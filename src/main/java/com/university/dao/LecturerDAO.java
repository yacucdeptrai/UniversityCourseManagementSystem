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

    public void addLecturer(Lecturer lecturer) {
        String personSql = "INSERT INTO persons (name, date_of_birth, gender) VALUES (?, ?, ?)";
        String lecturerSql = "INSERT INTO lecturers (person_id) VALUES (?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement personStmt = connection.prepareStatement(personSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement lecturerStmt = connection.prepareStatement(lecturerSql)) {

            personStmt.setString(1, lecturer.getName());
            personStmt.setDate(2, Date.valueOf(lecturer.getDateOfBirth()));
            personStmt.setString(3, lecturer.getGender());
            personStmt.executeUpdate();

            try (ResultSet generatedKeys = personStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int personId = generatedKeys.getInt(1);
                    lecturerStmt.setInt(1, personId);
                    lecturerStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void deleteLecturer(int lecturerID) {
        String sql = "DELETE FROM lecturers WHERE lecturer_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, lecturerID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
