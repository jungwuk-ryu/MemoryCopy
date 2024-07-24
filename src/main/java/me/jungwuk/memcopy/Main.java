package me.jungwuk.memcopy;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import me.jungwuk.memcopy.controller.MainViewController;
import me.jungwuk.memcopy.dialog.TwoLineDialog;
import me.jungwuk.memcopy.view.MainView;

import javax.swing.*;

public class Main {
    public static final long REQUIRED_MEMORY = Integer.MAX_VALUE + 1024L * 1024L * 1024L;

    public static void main(String[] args) {
        FlatLightLaf.setup(new FlatMacDarkLaf());
        long heapSize = Runtime.getRuntime().maxMemory();
        if (heapSize < REQUIRED_MEMORY) {
            TwoLineDialog dialog = new TwoLineDialog("실행 불가");
            dialog.setFirstLine("프로그램에 할당된 메모리가 너무 적습니다.");
            dialog.setSecondLine("실행을 위해선 최소 " + REQUIRED_MEMORY / 1024 / 1024 + "MB가 필요합니다.");
            dialog.setVisible(true);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            MainViewController mainViewController = new MainViewController();
            MainView mainView = new MainView(mainViewController);
        });
    }
}