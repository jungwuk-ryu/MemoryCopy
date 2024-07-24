package me.jungwuk.memcopy;

import me.jungwuk.memcopy.dialog.TwoLineDialog;

import javax.swing.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileCopier {
    private final File[] files;
    private final String toPath;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final TwoLineDialog progressDialog;
    private final ThreadSafeInteger jobCount = new ThreadSafeInteger(0);
    private boolean readFinished = false;
    private int writtenCount = 0;
    private int fileCount = 0;

    public FileCopier(File[] files, String toPath) {
        this.files = files;
        this.toPath = toPath;
        progressDialog = new TwoLineDialog("복사 진행 상황 " + toPath);
    }

    public void start() {
        File toRootDir = new File(toPath);
        toRootDir.mkdir();

        SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));
        Thread t = new Thread(() -> {
            for (File file : files) {
                copy(file, toRootDir);
            }

            readFinished = true;
            progressDialog.setFirstLine("읽기 완료");
        });
        t.start();
    }

    private void copy(File file, File toDir) {
        if (!toDir.isDirectory()) {
            progressDialog.setFirstLine("파일이 복사될 경로가 디렉터리가 아닙니다.");
            throw new IllegalArgumentException("Target must be a directory");
        }

        if (file.isDirectory()) {
            File newToDir = new File(toDir, file.getName());

            if (!newToDir.exists()) {
                newToDir.mkdir();
            }

            for (File childFile : file.listFiles()) {
                copy(childFile, newToDir);
            }
            return;
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(new File(toDir, file.getName()));
            ScatteringByteChannel sbc = fis.getChannel();
            GatheringByteChannel gbc = fos.getChannel();
            long fileSize = file.length();

            progressDialog.setFirstLine("읽는 중 : " + file.getAbsolutePath());
            fileCount++;

            for (long offset = 0; offset < fileSize; ) {
                ByteBuffer _buffer;
                long readSize = fileSize - offset;
                progressDialog.setFirstLine("읽는 중 : " + file.getAbsolutePath() + " ( " + offset + " / " + fileSize + " )");

                try {
                    _buffer = read(readSize, sbc);
                } catch (OutOfMemoryError e) {
                    progressDialog.setFirstLine("메모리가 부족하여 다른 작업이 끝나길 기다립니다.");
                    try {
                        jobCount.awaitValue(0);
                        _buffer = read(readSize, sbc);
                    } catch (InterruptedException ex) {
                        progressDialog.setFirstLine("오류가 발생하였습니다.");
                        throw new RuntimeException(ex);
                    } catch (OutOfMemoryError ex) {
                        progressDialog.setFirstLine("메모리가 부족하여 진행할 수 없습니다.");
                        return;
                    }
                }

                final ByteBuffer data = _buffer;
                final long fOffset = offset;

                jobCount.increment();
                executor.execute(() -> {
                    progressDialog.setSecondLine("쓰는 중 : ( " + fOffset + " / " + fileSize + "bytes ) ( " + writtenCount + " / " + fileCount + "개 완료 )");
                    try {
                        write(gbc, data);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        jobCount.decrement();
                    }
                });

                offset += data.capacity();
            }

            executor.execute(() -> {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    writtenCount++;
                    if (readFinished && writtenCount == fileCount) {
                        SwingUtilities.invokeLater(() -> {
                            progressDialog.setVisible(false);
                            TwoLineDialog dialog = new TwoLineDialog("복사 완료");
                            dialog.setVisible(true);
                        });
                    }
                }
            });
            sbc.close();
            fis.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ByteBuffer read(long size, ScatteringByteChannel sbc) throws OutOfMemoryError, IOException {
        final ByteBuffer data;
        data = ByteBuffer.allocateDirect((int) Math.min(size, Integer.MAX_VALUE - 8));
        sbc.read(data);

        return data;
    }

    private void write(GatheringByteChannel gbc, ByteBuffer data) throws IOException {
        data.flip();
        gbc.write(data);
    }
}
