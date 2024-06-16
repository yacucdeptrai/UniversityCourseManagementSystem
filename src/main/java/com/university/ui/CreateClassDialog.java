package main.java.com.university.ui;

import main.java.com.university.dao.LecturerDAO;
import main.java.com.university.dao.SubjectDAO;
import main.java.com.university.model.Lecturer;
import main.java.com.university.model.Subject;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CreateClassDialog extends JDialog {
    private JTextField subjectNameField;
    private JTextField creditsField;
    private JTextField customSubjectIDField;
    private JComboBox<String> lecturerComboBox;
    private JButton btnAddSubject;

    public CreateClassDialog(Frame parent) {
        super(parent, "Create Class", true);
        setLayout(new GridLayout(5, 2, 10, 10)); // Tạo bố cục lưới với 5 hàng, 2 cột, khoảng cách giữa các phần tử là 10
        setSize(300, 200); // Thiết lập kích thước cho cửa sổ
        setLocationRelativeTo(parent); // Đặt vị trí cửa sổ giữa màn hình

        JLabel lblID = new JLabel("Subject ID:");
        customSubjectIDField = new JTextField();
        add(lblID);
        add(customSubjectIDField);

        JLabel lblName = new JLabel("Subject Name:");
        subjectNameField = new JTextField();
        add(lblName);
        add(subjectNameField);

        JLabel lblCredits = new JLabel("Credits:");
        creditsField = new JTextField();
        add(lblCredits);
        add(creditsField);

        JLabel lblLecturer = new JLabel("Lecturer:");
        lecturerComboBox = new JComboBox<>();
        List<Lecturer> lecturers = new LecturerDAO().getAllLecturers();
        for (Lecturer lecturer : lecturers) {
            lecturerComboBox.addItem(lecturer.getName() + " (ID: " + lecturer.getLecturerID() + ")");
        }
        add(lblLecturer);
        add(lecturerComboBox);

        btnAddSubject = new JButton("Create Subject");
        btnAddSubject.addActionListener(e -> addSubject());
        add(new JLabel()); // Thêm nhãn trống để làm bộ đệm
        add(btnAddSubject);
    }

    private void addSubject() {
        try {
            int customSubjectID = Integer.parseInt(customSubjectIDField.getText()); // Lấy ID tự nhập từ trường văn bản
            String subjectName = subjectNameField.getText(); // Lấy tên môn học
            int credits = Integer.parseInt(creditsField.getText()); // Lấy số tín chỉ
            String lecturerInfo = (String) lecturerComboBox.getSelectedItem(); // Lấy thông tin giảng viên từ hộp chọn
            int lecturerID = Integer.parseInt(lecturerInfo.substring(lecturerInfo.indexOf("ID: ") + 4, lecturerInfo.indexOf(")"))); // Trích xuất ID giảng viên từ chuỗi thông tin giảng viên

            Lecturer lecturer = new LecturerDAO().getLecturerById(lecturerID); // Lấy đối tượng giảng viên từ ID
            Subject subject = new Subject(customSubjectID, 0, subjectName, credits, lecturer); // Tạo đối tượng môn học với ID tự nhập và các thông tin khác
            new SubjectDAO().saveSubject(subject); // Lưu đối tượng môn học vào cơ sở dữ liệu

            JOptionPane.showMessageDialog(this, "Subject added successfully!"); // Hiển thị thông báo thành công
            dispose(); // Đóng cửa sổ
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE); // Hiển thị thông báo lỗi nếu đầu vào không hợp lệ
        }
    }
}