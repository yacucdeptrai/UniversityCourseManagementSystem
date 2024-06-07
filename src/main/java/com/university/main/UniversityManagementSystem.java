package main.java.com.university.main;

import main.java.com.university.dao.*;
import main.java.com.university.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class UniversityManagementSystem {
    private static ArrayList<Student> students = new ArrayList<>();
    private static ArrayList<Lecturer> lecturers = new ArrayList<>();
    private static ArrayList<Subject> subjects = new ArrayList<>();
    private static HashMap<Integer, ArrayList<Integer>> enrollments = new HashMap<>();

    public static void main(String[] args) {
        PersonDAO personDAO = new PersonDAO();
        StudentDAO studentDAO = new StudentDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();
        SubjectDAO subjectDAO = new SubjectDAO();

        // Tạo dữ liệu mẫu
        Student student = new Student("Nguyen Van A", LocalDate.of(1995, 5, 20), "Nam", 1);
        int person1ID = personDAO.savePerson(student);
        student.setId(person1ID);
        studentDAO.saveStudent(student);
        students.add(student);

        Lecturer lecturer = new Lecturer("Tran Thi B", LocalDate.of(1980, 10, 10), "Nu", 1);
        int person2ID = personDAO.savePerson(lecturer);
        lecturer.setId(person2ID);
        lecturerDAO.saveLecturer(lecturer);
        lecturers.add(lecturer);

        // Thêm subject sau khi chắc chắn rằng lecturer đã được thêm
        Subject subject = new Subject(1, "Lap trinh Java", lecturer);
        subjectDAO.saveSubject(subject);
        subjects.add(subject);

        // Đăng ký sinh viên vào khóa học
        enrollStudentInSubject(student.getStudentID(), subject.getSubjectID());

        // Hiển thị dữ liệu
        displayData();
    }

    public static void enrollStudentInSubject(int studentID, int subjectID) {
        if (!enrollments.containsKey(studentID)) {
            enrollments.put(studentID, new ArrayList<>());
        }
        enrollments.get(studentID).add(subjectID);
    }

    public static void displayData() {
        for (Student student : students) {
            student.displayInfo();
        }

        for (Lecturer lecturer : lecturers) {
            lecturer.displayInfo();
        }

        for (Subject subject : subjects) {
            System.out.println("Subject: " + subject.getSubjectName());
        }

        for (Integer studentID : enrollments.keySet()) {
            System.out.println("Student ID " + studentID + " is enrolled in subjects: " + enrollments.get(studentID));
        }
    }
}
