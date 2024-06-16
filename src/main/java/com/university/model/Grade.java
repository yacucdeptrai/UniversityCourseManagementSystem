package main.java.com.university.model;

public class Grade {
    private int gradeID;
    private int studentID;
    private int customSubjectID;
    private double score;

    public Grade() {
    }

    public Grade(int gradeID, int studentID, int customSubjectID, double score) {
        this.gradeID = gradeID;
        this.studentID = studentID;
        this.customSubjectID = customSubjectID;
        this.score = score;
    }

    // Getters and setters
    public int getGradeID() {
        return gradeID;
    }

    public void setGradeID(int gradeID) {
        this.gradeID = gradeID;
    }

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