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

-- Tạo bảng 'Grades'
CREATE TABLE IF NOT EXISTS grades (
    student_id INT,
    custom_subject_id INT,
    score DECIMAL(3, 2),
    PRIMARY KEY (student_id, custom_subject_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (custom_subject_id) REFERENCES custom_subjects(custom_subject_id) ON DELETE CASCADE
);

-- Tạo bảng 'enrollments'
CREATE TABLE IF NOT EXISTS enrollments (
    student_id INT,
    custom_subject_id INT,
    PRIMARY KEY (student_id, custom_subject_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (custom_subject_id) REFERENCES custom_subjects(custom_subject_id) ON DELETE CASCADE
);

INSERT INTO persons (name, date_of_birth, gender) VALUES
('Nguyen Van A', '1980-01-01', 'Male'),
('Tran Thi B', '1985-02-02', 'Female'),
('Le Van C', '1975-03-03', 'Male'),
('Pham Thi D', '1990-04-04', 'Female'),
('Nguyen Van E', '1982-05-05', 'Male');

INSERT INTO lecturers (person_id) VALUES
(1), (2), (3), (4), (5);


INSERT INTO students (person_id) VALUES
(1), (2), (3), (4), (5);

INSERT INTO auto_subjects (subject_name, credits, lecturer_id) VALUES
('Mathematics', 3, 1),
('Physics', 4, 2),
('Chemistry', 3, 3),
('Biology', 4, 4),
('Computer Science', 3, 5);

INSERT INTO custom_subjects (custom_subject_id, auto_subject_id) VALUES
(101, 1),
(102, 2),
(103, 3),
(104, 4),
(105, 5);

INSERT INTO grades (student_id, custom_subject_id, score) VALUES
(1, 101, 8.5),
(2, 102, 7.0),
(3, 103, 9.0),
(4, 104, 8.5),
(5, 105, 9.0);

INSERT INTO enrollments (student_id, custom_subject_id) VALUES
(1, 101),
(2, 102),
(3, 103),
(4, 104),
(5, 105);
