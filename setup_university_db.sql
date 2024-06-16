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
('Nguyen Van E', '1982-05-05', 'Male'),
('Vu Thi F', '1988-06-06', 'Female'),
('Hoang Van G', '1983-07-07', 'Male'),
('Do Thi H', '1984-08-08', 'Female'),
('Bui Van I', '1978-09-09', 'Male'),
('Nguyen Thi J', '1992-10-10', 'Female'),
('Pham Van K', '1987-11-11', 'Male'),
('Vu Thi L', '1986-12-12', 'Female'),
('Nguyen Van M', '1979-01-13', 'Male'),
('Tran Thi N', '1981-02-14', 'Female'),
('Le Van O', '1983-03-15', 'Male'),
('Pham Thi P', '1985-04-16', 'Female'),
('Nguyen Van Q', '1987-05-17', 'Male'),
('Vu Thi R', '1989-06-18', 'Female'),
('Hoang Van S', '1991-07-19', 'Male'),
('Do Thi T', '1993-08-20', 'Female');

INSERT INTO lecturers (person_id) VALUES
(1), (2), (3), (4), (5),
(6), (7), (8), (9), (10),
(11), (12), (13), (14), (15),
(16), (17), (18), (19), (20);

INSERT INTO students (person_id) VALUES
(1), (2), (3), (4), (5),
(6), (7), (8), (9), (10),
(11), (12), (13), (14), (15),
(16), (17), (18), (19), (20);

INSERT INTO auto_subjects (subject_name, credits, lecturer_id) VALUES
('Mathematics', 3, 1),
('Physics', 4, 2),
('Chemistry', 3, 3),
('Biology', 4, 4),
('Computer Science', 3, 5),
('History', 3, 6),
('Geography', 3, 7),
('Literature', 3, 8),
('Art', 2, 9),
('Music', 2, 10),
('Physical Education', 2, 11),
('Economics', 3, 12),
('Philosophy', 3, 13),
('Political Science', 3, 14),
('Sociology', 3, 15),
('Psychology', 3, 16),
('Engineering', 4, 17),
('Medicine', 5, 18),
('Law', 4, 19),
('Business Administration', 3, 20);

INSERT INTO custom_subjects (custom_subject_id, auto_subject_id) VALUES
(101, 1),
(102, 2),
(103, 3),
(104, 4),
(105, 5),
(106, 6),
(107, 7),
(108, 8),
(109, 9),
(110, 10),
(111, 11),
(112, 12),
(113, 13),
(114, 14),
(115, 15),
(116, 16),
(117, 17),
(118, 18),
(119, 19),
(120, 20);

INSERT INTO grades (student_id, custom_subject_id, score) VALUES
(1, 101, 8.5),
(2, 102, 7.0),
(3, 103, 9.0),
(4, 104, 8.5),
(5, 105, 9.0),
(6, 106, 8.2),
(7, 107, 7.6),
(8, 108, 8.9),
(9, 109, 9.5),
(10, 110, 7.0),
(11, 111, 8.8),
(12, 112, 8.5),
(13, 113, 7.5),
(14, 114, 8.3),
(15, 115, 9.1),
(16, 116, 8.4),
(17, 117, 7.7),
(18, 118, 8.5),
(19, 119, 9.3),
(20, 120, 8.6);

INSERT INTO enrollments (student_id, custom_subject_id) VALUES
(1, 101),
(2, 102),
(3, 103),
(4, 104),
(5, 105),
(6, 106),
(7, 107),
(8, 108),
(9, 109),
(10, 110),
(11, 111),
(12, 112),
(13, 113),
(14, 114),
(15, 115),
(16, 116),
(17, 117),
(18, 118),
(19, 119),
(20, 120);
