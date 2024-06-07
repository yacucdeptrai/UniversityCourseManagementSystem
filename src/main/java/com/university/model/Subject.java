package main.java.com.university.model;

public class Subject {
    private int subjectID;
    private String subjectName;
    private Lecturer lecturer;

    public Subject(int subjectID, String subjectName, Lecturer lecturer) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.lecturer = lecturer;
    }

    @Override
    public String toString() {
        return subjectName + " (ID: " + subjectID + ")";
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }
}
