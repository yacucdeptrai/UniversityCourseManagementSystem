package main.java.com.university.dao;

import main.java.com.university.model.Lecturer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {

    public List<Lecturer> getAllLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT lecturers.lecturer_id, persons.name, persons.date_of_birth, persons.gender " +
                "FROM lecturers JOIN persons ON lecturers.person_id = persons.id";
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
            // Insert into persons table
            personStmt.setString(1, lecturer.getName());
            personStmt.setDate(2, Date.valueOf(lecturer.getDateOfBirth()));
            personStmt.setString(3, lecturer.getGender());
            personStmt.executeUpdate();
            try (ResultSet generatedKeys = personStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int personID = generatedKeys.getInt(1);
                    // Insert into lecturers table
                    lecturerStmt.setInt(1, lecturer.getLecturerID());
                    lecturerStmt.setInt(2, personID);
                    lecturerStmt.executeUpdate();
                }
            }
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
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, lecturerID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Lecturer getLecturerById(int lecturerID) {
        Lecturer lecturer = null;
        String sql = "SELECT lecturers.lecturer_id, persons.name, persons.date_of_birth, persons.gender " +
                "FROM lecturers JOIN persons ON lecturers.person_id = persons.id WHERE lecturers.lecturer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, lecturerID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                    String gender = rs.getString("gender");
                    lecturer = new Lecturer(name, dateOfBirth, gender, lecturerID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturer;
    }
}
