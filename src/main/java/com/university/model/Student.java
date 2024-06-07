package main.java.com.university.model;

import java.time.LocalDate;

public class Student extends Person {
    private int studentID;

    public Student(String name, LocalDate dateOfBirth, String gender, int studentID) {
        super(name, dateOfBirth, gender);
        this.studentID = studentID;
    }

    @Override
    public void displayInfo() {
        System.out.println("Student Name: " + getName());
        System.out.println("Student ID: " + getStudentID());
    }

    @Override
    public String toString() {
        return getName() + " (ID: " + studentID + ")";
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }
}
