package me.jungwuk.memcopy.controller;

import me.jungwuk.memcopy.FileCopier;

import javax.swing.*;
import java.io.File;

public class MainViewController {
    private final JFileChooser fileChooser = new JFileChooser();
    private final JFileChooser dirChooser = new JFileChooser();

    public MainViewController() {
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setDialogTitle("파일 또는 디렉터리 선택");

        dirChooser.setMultiSelectionEnabled(false);
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setDialogTitle("파일이 복사될 디렉터리를 선택해주세요");
    }

    public void startCopy() {
        if (!openFileChooser()) return;
        if (!openTargetDirectoryChooser()) return;

        File[] files = fileChooser.getSelectedFiles();
        FileCopier fileCopier = new FileCopier(files, dirChooser.getSelectedFile().getAbsolutePath());
        fileCopier.start();
    }

    public boolean openFileChooser() {
        int result = fileChooser.showOpenDialog(null);
        return result == JFileChooser.APPROVE_OPTION;
    }

    public boolean openTargetDirectoryChooser() {
        int result = dirChooser.showOpenDialog(null);
        return result == JFileChooser.APPROVE_OPTION;
    }
}

