package main.java.com.university.dao;

import main.java.com.university.model.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonDAO {
    public int savePerson(Person person) {
        String query = "INSERT INTO persons (name, date_of_birth, gender) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, person.getName());
            stmt.setDate(2, java.sql.Date.valueOf(person.getDateOfBirth()));
            stmt.setString(3, person.getGender());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Trả về ID tự sinh của người
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updatePerson(Person person) {
        String query = "UPDATE persons SET name = ?, date_of_birth = ?, gender = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, person.getName());
            stmt.setDate(2, java.sql.Date.valueOf(person.getDateOfBirth()));
            stmt.setString(3, person.getGender());
            stmt.setInt(4, person.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(int personID) {
        String query = "DELETE FROM persons WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, personID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
