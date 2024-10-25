package org.example;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360; // Chiều rộng của bảng trò chơi
    int boardHeight = 640; // Chiều cao của bảng trò chơi

    // Hình ảnh
    Image backgroundImg; // Hình nền
    Image birdImg; // Hình của chim
    Image topPipeImg; // Hình ống trên
    Image bottomPipeImg; // Hình ống dưới

    // Thông tin của chim
    int birdX = boardWidth / 8; // Tọa độ X của chim
    int birdY = boardWidth / 2; // Tọa độ Y của chim
    int birdWidth = 34; // Chiều rộng của chim
    int birdHeight = 24; // Chiều cao của chim

    class Bird {
        int x = birdX; // Tọa độ X của chim
        int y = birdY; // Tọa độ Y của chim
        int width = birdWidth; // Chiều rộng của chim
        int height = birdHeight; // Chiều cao của chim
        Image img; // Hình ảnh của chim

        Bird(Image img) {
            this.img = img; // Khởi tạo hình ảnh cho chim
        }
    }

    // Thông tin của ống
    int pipeX = boardWidth; // Tọa độ X của ống
    int pipeY = 0; // Tọa độ Y của ống
    int pipeWidth = 64; // Chiều rộng của ống
    int pipeHeight = 512; // Chiều cao của ống

    class Pipe {
        int x = pipeX; // Tọa độ X của ống
        int y = pipeY; // Tọa độ Y của ống
        int width = pipeWidth; // Chiều rộng của ống
        int height = pipeHeight; // Chiều cao của ống
        Image img; // Hình ảnh của ống
        boolean passed = false; // Kiểm tra xem chim đã vượt qua ống hay chưa

        Pipe(Image img) {
            this.img = img; // Khởi tạo hình ảnh cho ống
        }
    }

    // Logic trò chơi
    Bird bird; // Đối tượng chim
    int velocityX = -4; // Tốc độ di chuyển của ống sang trái (giả lập chim di chuyển sang phải)
    int velocityY = 0; // Tốc độ di chuyển của chim lên/xuống
    int gravity = 1; // Lực hấp dẫn ảnh hưởng đến chim

    ArrayList<Pipe> pipes; // Danh sách các ống
    Random random = new Random(); // Đối tượng ngẫu nhiên

    Timer gameLoop; // Bộ đếm thời gian cho vòng lặp trò chơi
    Timer placePipeTimer; // Bộ đếm thời gian để đặt ống mới
    boolean gameOver = false; // Kiểm tra trạng thái trò chơi
    double score = 0; // Điểm số

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight)); // Đặt kích thước cho bảng
        setFocusable(true); // Đảm bảo bảng có thể nhận sự kiện bàn phím
        addKeyListener(this); // Thêm lắng nghe sự kiện bàn phím

        // Tải hình ảnh
        backgroundImg = new ImageIcon(getClass().getResource("/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("/flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("/bottompipe.png")).getImage();

        // Tạo đối tượng chim
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>(); // Khởi tạo danh sách ống

        // Bộ đếm thời gian để đặt ống
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes(); // Gọi phương thức để đặt ống mới
            }
        });
        placePipeTimer.start(); // Bắt đầu bộ đếm

        // Bộ đếm thời gian cho vòng lặp trò chơi
        gameLoop = new Timer(1000 / 60, this); // Đặt thời gian giữa các khung hình
        gameLoop.start(); // Bắt đầu bộ đếm
    }

    // Phương thức để đặt ống mới
    void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2)); // Tọa độ Y ngẫu nhiên cho ống
        int openingSpace = boardHeight / 4; // Khoảng trống giữa các ống

        Pipe topPipe = new Pipe(topPipeImg); // Tạo ống trên
        topPipe.y = randomPipeY; // Đặt tọa độ Y cho ống trên
        pipes.add(topPipe); // Thêm ống trên vào danh sách

        Pipe bottomPipe = new Pipe(bottomPipeImg); // Tạo ống dưới
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace; // Đặt tọa độ Y cho ống dưới
        pipes.add(bottomPipe); // Thêm ống dưới vào danh sách
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g); // Gọi phương thức vẽ
    }

    public void draw(Graphics g) {
        // Vẽ nền
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);

        // Vẽ chim
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        // Vẽ các ống
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Vẽ điểm số
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35); // Hiển thị điểm số khi trò chơi kết thúc
        } else {
            g.drawString(String.valueOf((int) score), 10, 35); // Hiển thị điểm số
        }
    }

    public void move() {
        // Cập nhật vị trí chim
        velocityY += gravity; // Áp dụng lực hấp dẫn
        bird.y += velocityY; // Cập nhật tọa độ Y của chim
        bird.y = Math.max(bird.y, 0); // Đảm bảo chim không rơi ra ngoài trên bảng

        // Cập nhật vị trí các ống
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX; // Di chuyển ống sang trái

            // Kiểm tra xem chim có vượt qua ống không
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; // Tăng điểm khi chim vượt qua ống
                pipe.passed = true; // Đánh dấu ống là đã vượt qua
            }

            // Kiểm tra va chạm
            if (collision(bird, pipe)) {
                gameOver = true; // Kết thúc trò chơi khi va chạm
            }
        }

        // Kiểm tra nếu chim rơi ra ngoài dưới bảng
        if (bird.y > boardHeight) {
            gameOver = true; // Kết thúc trò chơi
        }
    }

    // Phương thức kiểm tra va chạm
    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&   // Kiểm tra góc trên bên trái của chim không vượt qua góc trên bên phải của ống
                a.x + a.width > b.x &&   // Kiểm tra góc trên bên phải của chim vượt qua góc trên bên trái của ống
                a.y < b.y + b.height &&  // Kiểm tra góc trên bên trái của chim không vượt qua góc dưới bên trái của ống
                a.y + a.height > b.y;    // Kiểm tra góc dưới bên trái của chim vượt qua góc trên bên trái của ống
    }

    @Override
    public void actionPerformed(ActionEvent e) { // Được gọi mỗi x mili giây bởi bộ đếm thời gian gameLoop
        move(); // Cập nhật vị trí
        repaint(); // Vẽ lại
        if (gameOver) {
            placePipeTimer.stop(); // Dừng bộ đếm thời gian đặt ống
            gameLoop.stop(); // Dừng vòng lặp trò chơi
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { // Kiểm tra nếu phím cách được nhấn
            velocityY = -9; // Đặt tốc độ di chuyển lên của chim

            if (gameOver) { // Nếu trò chơi kết thúc
                // Khởi động lại trò chơi bằng cách đặt lại các điều kiện
                bird.y = birdY; // Đặt lại vị trí Y của chim
                velocityY = 0; // Đặt lại tốc độ di chuyển
                pipes.clear(); // Xóa danh sách ống
                gameOver = false; // Đặt lại trạng thái trò chơi
                score = 0; // Đặt lại điểm số
                gameLoop.start(); // Bắt đầu lại vòng lặp trò chơi
                placePipeTimer.start(); // Bắt đầu lại bộ đếm đặt ống
            }
        }
    }

    // Không cần thiết
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
