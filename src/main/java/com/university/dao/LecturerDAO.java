package main.java.com.university.dao;

import main.java.com.university.model.Lecturer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LecturerDAO {
    public Lecturer getLecturerById(int id) {
        String query = "SELECT * FROM lecturers INNER JOIN persons ON lecturers.person_id = persons.id WHERE lecturer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Lecturer(
                        rs.getString("name"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("gender"),
                        rs.getInt("lecturer_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsById(int id) {
        String query = "SELECT 1 FROM lecturers WHERE lecturer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveLecturer(Lecturer lecturer) {
        if (existsById(lecturer.getLecturerID())) {
            System.out.println("Lecturer with ID " + lecturer.getLecturerID() + " already exists.");
            return;
        }

        String query = "INSERT INTO lecturers (lecturer_id, person_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, lecturer.getLecturerID());
            stmt.setInt(2, lecturer.getId()); // Chú ý: ID của person đã được tạo
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
