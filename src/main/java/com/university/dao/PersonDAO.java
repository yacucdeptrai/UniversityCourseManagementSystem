package main.java.com.university.dao;

import main.java.com.university.model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PersonDAO {
    public Person getPersonById(int id) {
        String query = "SELECT * FROM persons WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                String gender = rs.getString("gender");
                // Trả về đối tượng tạm thời vì Person là abstract
                return new Person(name, dateOfBirth, gender) {
                    @Override
                    public void displayInfo() {
                        System.out.println("Person: " + getName());
                    }
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int savePerson(Person person) {
        String query = "INSERT INTO persons (name, date_of_birth, gender) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, person.getName());
            stmt.setDate(2, java.sql.Date.valueOf(person.getDateOfBirth()));
            stmt.setString(3, person.getGender());
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
