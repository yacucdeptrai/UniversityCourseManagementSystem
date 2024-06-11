package main.java.com.university.model;

public class Subject {
    private int subjectID;
    private String subjectName;
    private Lecturer lecturer;
    private int credits; // Thêm trường này

    public Subject(int subjectID, String subjectName, Lecturer lecturer, int credits) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.lecturer = lecturer;
        this.credits = credits; // Cập nhật constructor
    }

    // Các phương thức getter và setter
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectID=" + subjectID +
                ", subjectName='" + subjectName + '\'' +
                ", lecturer=" + lecturer +
                ", credits=" + credits +
                '}';
    }
}
