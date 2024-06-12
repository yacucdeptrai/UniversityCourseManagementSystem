package main.java.com.university.dao;

import main.java.com.university.model.Lecturer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {
    public List<Lecturer> getAllLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.date_of_birth, p.gender " +
                "FROM persons p " +
                "JOIN lecturers l ON p.id = l.person_id";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                String gender = resultSet.getString("gender");

                Lecturer lecturer = new Lecturer(name, dateOfBirth, gender, id);
                lecturers.add(lecturer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturers;
    }

    public Lecturer getLecturerById(int lecturerID) {
        String sql = "SELECT p.id, p.name, p.date_of_birth, p.gender " +
                "FROM persons p " +
                "JOIN lecturers l ON p.id = l.person_id " +
                "WHERE p.id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, lecturerID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
        String sql = "UPDATE persons SET name = ?, date_of_birth = ?, gender = ? WHERE id = ?";

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
        String sql = "DELETE FROM persons WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, lecturerID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
