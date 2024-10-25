package org.example;

import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        // Định nghĩa kích thước của bảng trò chơi
        int boardWidth = 360;  // Chiều rộng của bảng
        int boardHeight = 640; // Chiều cao của bảng

        // Tạo một cửa sổ (JFrame) cho trò chơi
        JFrame frame = new JFrame("Flappy Bird"); // Tạo một JFrame mới với tiêu đề "Flappy Bird"

        // Thiết lập kích thước cho cửa sổ
        frame.setSize(boardWidth, boardHeight); // Đặt kích thước cho JFrame bằng với kích thước bảng
        frame.setLocationRelativeTo(null); // Đặt vị trí cửa sổ ở giữa màn hình
        frame.setResizable(false); // Không cho phép thay đổi kích thước cửa sổ
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Đặt hành động khi đóng cửa sổ là thoát chương trình

        // Tạo một đối tượng FlappyBird (lớp trò chơi)
        FlappyBird flappyBird = new FlappyBird(); // Khởi tạo trò chơi FlappyBird
        frame.add(flappyBird); // Thêm đối tượng FlappyBird vào JFrame

        frame.pack(); // Tự động điều chỉnh kích thước của JFrame dựa trên nội dung
        flappyBird.requestFocus(); // Yêu cầu cửa sổ trò chơi nhận sự chú ý (để nhận phím nhấn)
        frame.setVisible(true); // Đặt cửa sổ thành có thể nhìn thấy
    }
}
