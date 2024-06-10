-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS University;

-- Sử dụng cơ sở dữ liệu
USE University;

-- Tạo bảng 'persons'
CREATE TABLE IF NOT EXISTS persons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    date_of_birth DATE,
    gender VARCHAR(10)
);


-- Tạo bảng 'students'
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY,
    person_id INT,
    FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
);

-- Tạo bảng 'lecturers'
CREATE TABLE IF NOT EXISTS lecturers (
    lecturer_id INT PRIMARY KEY,
    person_id INT,
    FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
);


-- Tạo bảng 'subjects'
CREATE TABLE IF NOT EXISTS subjects (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(255) NOT NULL,
    lecturer_id INT,
    FOREIGN KEY (lecturer_id) REFERENCES lecturers(lecturer_id) ON DELETE CASCADE
);

-- Tạo bảng 'enrollments'
CREATE TABLE IF NOT EXISTS enrollments (
    student_id INT,
    subject_id INT,
    PRIMARY KEY (student_id, subject_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)
);


DELETE FROM enrollments;
DELETE FROM subjects;
DELETE FROM students;
DELETE FROM lecturers;
DELETE FROM persons;