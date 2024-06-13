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
}
