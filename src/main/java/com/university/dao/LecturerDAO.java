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

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int lecturerID = rs.getInt("lecturer_id");
                String name = rs.getString("name");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String gender = rs.getString("gender");

                lecturers.add(new Lecturer(name, dateOfBirth, gender, lecturerID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturers;
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
        String personSql = "UPDATE persons SET name = ?, date_of_birth = ?, gender = ? " +
                "WHERE id = (SELECT person_id FROM lecturers WHERE lecturer_id = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement personStmt = conn.prepareStatement(personSql)) {
            personStmt.setString(1, lecturer.getName());
            personStmt.setDate(2, Date.valueOf(lecturer.getDateOfBirth()));
            personStmt.setString(3, lecturer.getGender());
            personStmt.setInt(4, lecturer.getLecturerID());
            personStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLecturer(int lecturerID) {
        String sql = "DELETE FROM lecturers WHERE lecturer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lecturerID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Lecturer getLecturerById(int lecturerID) {
        String sql = "SELECT l.lecturer_id, p.name, p.date_of_birth, p.gender " +
                "FROM lecturers l " +
                "JOIN persons p ON l.person_id = p.id " +
                "WHERE l.lecturer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lecturerID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                    String gender = rs.getString("gender");
                    return new Lecturer(name, dateOfBirth, gender, lecturerID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
