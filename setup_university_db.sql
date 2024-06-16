-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS University;

-- Sử dụng cơ sở dữ liệu
USE University;

-- Tạo bảng 'persons'
CREATE TABLE IF NOT EXISTS persons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender ENUM('Male', 'Female') NOT NULL
);

-- Tạo bảng 'lecturers'
CREATE TABLE IF NOT EXISTS lecturers (
    lecturer_id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
);

-- Tạo bảng 'students'
CREATE TABLE IF NOT EXISTS students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
);

-- Bảng auto_subjects với ID tự sinh
CREATE TABLE IF NOT EXISTS auto_subjects (
    auto_subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(255) NOT NULL,
    credits INT NOT NULL,
    lecturer_id INT,
    FOREIGN KEY (lecturer_id) REFERENCES lecturers(lecturer_id) ON DELETE CASCADE
);

-- Bảng custom_subjects với ID tự nhập
CREATE TABLE IF NOT EXISTS custom_subjects (
    custom_subject_id INT PRIMARY KEY,
    auto_subject_id INT,
    FOREIGN KEY (auto_subject_id) REFERENCES auto_subjects(auto_subject_id) ON DELETE CASCADE
);

-- Tạo bảng 'enrollments'
CREATE TABLE IF NOT EXISTS enrollments (
    student_id INT,
    custom_subject_id INT,
    PRIMARY KEY (student_id, custom_subject_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (custom_subject_id) REFERENCES custom_subjects(custom_subject_id) ON DELETE CASCADE
);

-- Xóa dữ liệu cũ (Chỉ sử dụng khi cần làm sạch dữ liệu)
DELETE FROM enrollments;
DELETE FROM subjects;
DELETE FROM students;
DELETE FROM lecturers;
DELETE FROM persons;