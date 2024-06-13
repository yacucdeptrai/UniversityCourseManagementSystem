package main.java.com.university.model;

public class Subject {
    private int customSubjectID;
    private int autoSubjectID;
    private String subjectName;
    private int credits;
    private Lecturer lecturer;

    public Subject() {
    }

    public Subject(int customSubjectID, int autoSubjectID, String subjectName, int credits, Lecturer lecturer) {
        this.customSubjectID = customSubjectID;
        this.autoSubjectID = autoSubjectID;
        this.subjectName = subjectName;
        this.credits = credits;
        this.lecturer = lecturer;
    }

    // Các getter và setter
    public int getCustomSubjectID() {
        return customSubjectID;
    }

    public void setCustomSubjectID(int customSubjectID) {
        this.customSubjectID = customSubjectID;
    }

    public int getAutoSubjectID() {
        return autoSubjectID;
    }

    public void setAutoSubjectID(int autoSubjectID) {
        this.autoSubjectID = autoSubjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public String toString() {
        return String.format("%d - %s - %s", customSubjectID, subjectName, lecturer.getName());
    }
}