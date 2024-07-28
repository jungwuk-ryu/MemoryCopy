package me.jungwuk.memcopy.util;

import javax.swing.*;

public class SwingUtil {
    public static void ThreadSafeRunnable(Runnable runnable) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(runnable);
        } else {
            runnable.run();
        }
    }
}
