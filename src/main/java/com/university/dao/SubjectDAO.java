package main.java.com.university.dao;

import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT cs.custom_subject_id, a.auto_subject_id, a.subject_name, a.credits, l.lecturer_id, p.name AS lecturer_name, p.date_of_birth, p.gender " +
                "FROM custom_subjects cs " +
                "JOIN auto_subjects a ON cs.auto_subject_id = a.auto_subject_id " +
                "JOIN lecturers l ON a.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Subject subject = new Subject();
                subject.setCustomSubjectID(resultSet.getInt("custom_subject_id"));
                subject.setAutoSubjectID(resultSet.getInt("auto_subject_id"));
                subject.setSubjectName(resultSet.getString("subject_name"));
                subject.setCredits(resultSet.getInt("credits"));

                Lecturer lecturer = new Lecturer(
                        resultSet.getString("lecturer_name"),
                        resultSet.getDate("date_of_birth").toLocalDate(),
                        resultSet.getString("gender"),
                        resultSet.getInt("lecturer_id")
                );
                subject.setLecturer(lecturer);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public Subject getSubjectByCustomID(int customSubjectID) {
        String sql = "SELECT cs.custom_subject_id, a.auto_subject_id, a.subject_name, a.credits, " +
                "l.lecturer_id, p.name AS lecturer_name, p.date_of_birth, p.gender " +
                "FROM custom_subjects cs " +
                "JOIN auto_subjects a ON cs.auto_subject_id = a.auto_subject_id " +
                "JOIN lecturers l ON a.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id " +
                "WHERE cs.custom_subject_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customSubjectID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Subject subject = new Subject();
                    subject.setCustomSubjectID(resultSet.getInt("custom_subject_id"));
                    subject.setAutoSubjectID(resultSet.getInt("auto_subject_id"));
                    subject.setSubjectName(resultSet.getString("subject_name"));
                    subject.setCredits(resultSet.getInt("credits"));

                    Lecturer lecturer = new Lecturer(
                            resultSet.getString("lecturer_name"),
                            resultSet.getDate("date_of_birth").toLocalDate(),
                            resultSet.getString("gender"),
                            resultSet.getInt("lecturer_id")
                    );
                    subject.setLecturer(lecturer);
                    return subject;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Subject getSubjectByName(String subjectName) {
        String sql = "SELECT cs.custom_subject_id, a.auto_subject_id, a.subject_name, a.credits, l.lecturer_id, p.name AS lecturer_name, p.date_of_birth, p.gender " +
                "FROM custom_subjects cs " +
                "JOIN auto_subjects a ON cs.auto_subject_id = a.auto_subject_id " +
                "JOIN lecturers l ON a.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id " +
                "WHERE a.subject_name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, subjectName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Subject subject = new Subject();
                    subject.setCustomSubjectID(resultSet.getInt("custom_subject_id"));
                    subject.setAutoSubjectID(resultSet.getInt("auto_subject_id"));
                    subject.setSubjectName(resultSet.getString("subject_name"));
                    subject.setCredits(resultSet.getInt("credits"));

                    Lecturer lecturer = new Lecturer(
                            resultSet.getString("lecturer_name"),
                            resultSet.getDate("date_of_birth").toLocalDate(),
                            resultSet.getString("gender"),
                            resultSet.getInt("lecturer_id")
                    );
                    subject.setLecturer(lecturer);
                    return subject;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String generateUniqueSubjectName(String baseName) {
        String sql = "SELECT subject_name FROM auto_subjects WHERE subject_name LIKE ?";
        int counter = 1;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String candidateName = baseName;
            statement.setString(1, baseName + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String existingName = resultSet.getString("subject_name");
                    if (existingName.equals(candidateName)) {
                        counter++;
                        candidateName = baseName + " " + counter;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return baseName + (counter > 1 ? " " + counter : "");
    }

    // Lấy môn học theo ID sinh viên
    public List<Subject> getSubjectsByStudentID(int studentID) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT cs.custom_subject_id, a.auto_subject_id, a.subject_name, a.credits, l.lecturer_id, p.name AS lecturer_name, p.date_of_birth, p.gender " +
                "FROM enrollments e " +
                "JOIN custom_subjects cs ON e.custom_subject_id = cs.custom_subject_id " +
                "JOIN auto_subjects a ON cs.auto_subject_id = a.auto_subject_id " +
                "JOIN lecturers l ON a.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id " +
                "WHERE e.student_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Subject subject = new Subject();
                    subject.setCustomSubjectID(resultSet.getInt("custom_subject_id"));
                    subject.setAutoSubjectID(resultSet.getInt("auto_subject_id"));
                    subject.setSubjectName(resultSet.getString("subject_name"));
                    subject.setCredits(resultSet.getInt("credits"));

                    Lecturer lecturer = new Lecturer(
                            resultSet.getString("lecturer_name"),
                            resultSet.getDate("date_of_birth").toLocalDate(),
                            resultSet.getString("gender"),
                            resultSet.getInt("lecturer_id")
                    );
                    subject.setLecturer(lecturer);
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public List<Subject> getSubjectsByLecturerID(int lecturerID) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT cs.custom_subject_id, a.auto_subject_id, a.subject_name, a.credits, l.lecturer_id, p.name AS lecturer_name, p.date_of_birth, p.gender " +
                "FROM custom_subjects cs " +
                "JOIN auto_subjects a ON cs.auto_subject_id = a.auto_subject_id " +
                "JOIN lecturers l ON a.lecturer_id = l.lecturer_id " +
                "JOIN persons p ON l.person_id = p.id " +
                "WHERE l.lecturer_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lecturerID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Subject subject = new Subject();
                    subject.setCustomSubjectID(resultSet.getInt("custom_subject_id"));
                    subject.setAutoSubjectID(resultSet.getInt("auto_subject_id"));
                    subject.setSubjectName(resultSet.getString("subject_name"));
                    subject.setCredits(resultSet.getInt("credits"));

                    Lecturer lecturer = new Lecturer(
                            resultSet.getString("lecturer_name"),
                            resultSet.getDate("date_of_birth").toLocalDate(),
                            resultSet.getString("gender"),
                            resultSet.getInt("lecturer_id")
                    );
                    subject.setLecturer(lecturer);
                    subjects.add(subject);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public void updateSubject(Subject subject) {
        String sql = "UPDATE auto_subjects SET subject_name = ?, credits = ?, lecturer_id = ? WHERE auto_subject_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, subject.getSubjectName());
            statement.setInt(2, subject.getCredits());
            statement.setInt(3, subject.getLecturer().getLecturerID());
            statement.setInt(4, subject.getAutoSubjectID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveSubject(Subject subject) {
        String sqlAutoSubject = "INSERT INTO auto_subjects (subject_name, credits, lecturer_id) VALUES (?, ?, ?)";
        String sqlCustomSubject = "INSERT INTO custom_subjects (custom_subject_id, auto_subject_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection()) {
            try (PreparedStatement autoSubjectStatement = connection.prepareStatement(sqlAutoSubject, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement customSubjectStatement = connection.prepareStatement(sqlCustomSubject)) {
                connection.setAutoCommit(false);

                // Lưu thông tin vào bảng auto_subjects
                autoSubjectStatement.setString(1, subject.getSubjectName());
                autoSubjectStatement.setInt(2, subject.getCredits());
                autoSubjectStatement.setInt(3, subject.getLecturer().getLecturerID());
                autoSubjectStatement.executeUpdate();

                // Lấy auto_subject_id tự sinh
                try (ResultSet generatedKeys = autoSubjectStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int autoSubjectID = generatedKeys.getInt(1);

                        // Lưu vào bảng custom_subjects
                        customSubjectStatement.setInt(1, subject.getCustomSubjectID());
                        customSubjectStatement.setInt(2, autoSubjectID);
                        customSubjectStatement.executeUpdate();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa môn học
    public boolean deleteSubject(int customSubjectID) {
        String checkLecturerSQL = "SELECT COUNT(*) " +
                "FROM auto_subjects a " +
                "JOIN custom_subjects cs ON a.auto_subject_id = cs.auto_subject_id " +
                "WHERE cs.custom_subject_id = ? AND a.lecturer_id IS NOT NULL";
        String deleteCustomSQL = "DELETE FROM custom_subjects WHERE custom_subject_id = ?";
        String deleteAutoSQL = "DELETE FROM auto_subjects WHERE auto_subject_id = (SELECT auto_subject_id FROM custom_subjects WHERE custom_subject_id = ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkLecturerSQL);
             PreparedStatement deleteCustomStatement = connection.prepareStatement(deleteCustomSQL);
             PreparedStatement deleteAutoStatement = connection.prepareStatement(deleteAutoSQL)) {

            // Kiểm tra nếu có giảng viên dạy môn học
            checkStatement.setInt(1, customSubjectID);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return false; // Không xóa môn học nếu có giảng viên dạy
                }
            }

            // Xóa môn học từ bảng custom_subjects trước
            deleteCustomStatement.setInt(1, customSubjectID);
            deleteCustomStatement.executeUpdate();

            // Xóa môn học từ bảng auto_subjects
            deleteAutoStatement.setInt(1, customSubjectID);
            deleteAutoStatement.executeUpdate();

            return true; // Xóa môn học thành công

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
