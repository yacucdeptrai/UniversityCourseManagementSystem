package main.java.com.university.model;

public class Grade {
    private int gradeID;
    private int studentID;
    private int customSubjectID;
    private Double score; // Nullable

    // Getters and Setters
    public int getGradeID() { return gradeID; }
    public void setGradeID(int gradeID) { this.gradeID = gradeID; }

    public int getStudentID() { return studentID; }
    public void setStudentID(int studentID) { this.studentID = studentID; }

    public int getCustomSubjectID() { return customSubjectID; }
    public void setCustomSubjectID(int customSubjectID) { this.customSubjectID = customSubjectID; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
}
