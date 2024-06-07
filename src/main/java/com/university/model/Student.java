package main.java.com.university.model;

import java.time.LocalDate;

public class Student extends Person {
    private int studentID;

    public Student(String name, LocalDate dateOfBirth, String gender, int studentID) {
        super(name, dateOfBirth, gender);
        this.studentID = studentID;
    }

    // Getter và Setter
    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }
}
