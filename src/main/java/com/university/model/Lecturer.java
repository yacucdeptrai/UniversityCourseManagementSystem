package main.java.com.university.model;

import java.time.LocalDate;

public class Lecturer extends Person {
    private int lecturerID;

    public Lecturer(String name, LocalDate dateOfBirth, String gender, int lecturerID) {
        super(name, dateOfBirth, gender);
        this.lecturerID = lecturerID;
    }

    @Override
    public void displayInfo() {
        System.out.println("Lecturer Name: " + getName());
        System.out.println("Lecturer ID: " + getLecturerID());
    }

    @Override
    public String toString() {
        return getName() + " (ID: " + lecturerID + ")";
    }

    // Getters và Setters
    public int getLecturerID() {
        return lecturerID;
    }

    public void setLecturerID(int lecturerID) {
        this.lecturerID = lecturerID;
    }
}
