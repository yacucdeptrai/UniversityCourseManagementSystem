package main.java.com.university.model;

import java.time.LocalDate;

public class Lecturer extends Person {
    private int lecturerID;

    public Lecturer(String name, LocalDate dateOfBirth, String gender, int lecturerID) {
        super(name, dateOfBirth, gender);
        this.lecturerID = lecturerID;
    }

    // Getters và Setters
    public int getLecturerID() {
        return lecturerID;
    }

    public void setLecturerID(int lecturerID) {
        this.lecturerID = lecturerID;
    }

    @Override
    public void displayInfo() {
        System.out.println("Lecturer ID: " + lecturerID);
        System.out.println("Name: " + getName());
        System.out.println("Date of Birth: " + getDateOfBirth());
        System.out.println("Gender: " + getGender());
    }
}
