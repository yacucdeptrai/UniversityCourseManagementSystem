package main.java.com.university.main;

import main.java.com.university.dao.*;
import main.java.com.university.model.*;

import java.time.LocalDate;

public class UniversityManagementSystem {
    public static void main(String[] args) {
        PersonDAO personDAO = new PersonDAO();
        StudentDAO studentDAO = new StudentDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();
        SubjectDAO subjectDAO = new SubjectDAO();

        // Tạo dữ liệu mẫu
        Person person1 = new Person("Nguyen Van A", LocalDate.of(1995, 5, 20), "Nam");
        int person1ID = personDAO.savePerson(person1);

        Person person2 = new Person("Tran Thi B", LocalDate.of(1980, 10, 10), "Nu");
        int person2ID = personDAO.savePerson(person2);

        Student student = new Student("Nguyen Van A", LocalDate.of(1995, 5, 20), "Nam", 1);
        student.setId(person1ID);
        studentDAO.saveStudent(student);

        Lecturer lecturer = new Lecturer("Tran Thi B", LocalDate.of(1980, 10, 10), "Nu", 1);
        lecturer.setId(person2ID);
        lecturerDAO.saveLecturer(lecturer);

        // Thêm subject sau khi chắc chắn rằng lecturer đã được thêm
        Subject subject = new Subject(1, "Lap trinh Java", lecturer);
        subjectDAO.saveSubject(subject);

        // Lấy dữ liệu mẫu
        Student fetchedStudent = studentDAO.getStudentById(1);
        if (fetchedStudent != null) {
            System.out.println("Student: " + fetchedStudent.getName() + ", ID: " + fetchedStudent.getStudentID());
        } else {
            System.out.println("Student not found");
        }

        Lecturer fetchedLecturer = lecturerDAO.getLecturerById(1);
        if (fetchedLecturer != null) {
            System.out.println("Lecturer: " + fetchedLecturer.getName() + ", ID: " + fetchedLecturer.getLecturerID());
        } else {
            System.out.println("Lecturer not found");
        }

        Subject fetchedSubject = subjectDAO.getSubjectById(1);
        if (fetchedSubject != null) {
            System.out.println("Subject: " + fetchedSubject.getSubjectName() + ", Lecturer: " + fetchedSubject.getLecturer().getName());
        } else {
            System.out.println("Subject not found");
        }
    }
}
