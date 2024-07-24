package me.jungwuk.memcopy.view;

import me.jungwuk.memcopy.controller.MainViewController;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private final MainViewController controller;

    public MainView(MainViewController controller) {
        this.controller = controller;

        setTitle("Memcopy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);
        setLocationRelativeTo(null);
        setResizable(true);
        setLayout(new BorderLayout());

        layoutComponents();

        setVisible(true);
    }

    private void layoutComponents() {
        //JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Memory Copy", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JLabel desc = new JLabel("<html> <p style=\"text-align: center;\">Memory Copy는 SSD -> HDD 처럼 성능이 느린 저장 장치로 파일을 복사할 때 유용합니다.<br>SSD -> RAM -> HDD 경로로 복사가 진행됩니다.</p></html>", JLabel.CENTER);
        add(desc, BorderLayout.CENTER);

        JButton btnCopy = new JButton("디렉터리 또는 파일 선택");
        btnCopy.addActionListener((ignore) -> controller.startCopy());
        add(btnCopy, BorderLayout.SOUTH);

        //add(panel, BorderLayout.CENTER);
    }


}
