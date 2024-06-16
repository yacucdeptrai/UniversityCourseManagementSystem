package main.java.com.university.model;

public class Grade {
    private int studentID;
    private int customSubjectID;
    private double score;

    public Grade(int studentID, int customSubjectID, double score) {
        this.studentID = studentID;
        this.customSubjectID = customSubjectID;
        this.score = score;
    }

    // Getters and Setters
    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getCustomSubjectID() {
        return customSubjectID;
    }

    public void setCustomSubjectID(int customSubjectID) {
        this.customSubjectID = customSubjectID;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}