package main.java.com.university.model;

import java.time.LocalDate;

public class Student extends Person {
    private int studentID;

    public Student(int studentID, String name, LocalDate dateOfBirth, String gender) {
        super(name, dateOfBirth, gender);
        this.studentID = studentID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    @Override
    public void displayInfo() {
        System.out.println("Student ID: " + studentID);
        System.out.println("Name: " + getName());
        System.out.println("Date of Birth: " + getDateOfBirth());
        System.out.println("Gender: " + getGender());
    }
}
