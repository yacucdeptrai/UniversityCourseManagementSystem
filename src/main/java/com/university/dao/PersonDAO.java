package main.java.com.university.dao;

import main.java.com.university.model.Person;

import java.sql.*;

public class PersonDAO {

    public Person getPersonById(int personID) {
        Person person = null;
        String sql = "SELECT * FROM persons WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, personID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    Date dateOfBirth = rs.getDate("date_of_birth");
                    String gender = rs.getString("gender");
                    // Create Person object based on retrieved data
                    person = new Person(name, dateOfBirth.toLocalDate(), gender) {
                        @Override
                        public void displayInfo() {
                            // Implement displayInfo
                        }
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }
}
