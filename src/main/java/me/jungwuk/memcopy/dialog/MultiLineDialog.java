package me.jungwuk.memcopy.dialog;

import me.jungwuk.memcopy.util.SwingUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MultiLineDialog extends JDialog {
    private final ArrayList<JLabel> labels;
    private JPanel column;

    public MultiLineDialog(String title, int size) {
        super(null, title, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(300, 100);
        this.setLocationRelativeTo(this);

        this.labels = new ArrayList<>(size);
        initLayout();
        initLabels(size);
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel(getTitle(), JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        add(column, BorderLayout.CENTER);
    }

    private void initLabels(int size) {
        for (int i = 0; i < size; i++) {
            JLabel label = new JLabel("", SwingConstants.CENTER);
            labels.add(label);
            column.add(label);
        }
    }

    public int getLabelCount() {
        return labels.size();
    }

    public JLabel getLabel(int lineIndex) {
        return labels.get(lineIndex);
    }

    public void setText(int lineIndex, String text) {
        JLabel label = getLabel(lineIndex);

        SwingUtil.ThreadSafeRunnable(() -> label.setText(text));
    }

    public String getText(int lineIndex) {
        JLabel label = getLabel(lineIndex);
        return label.getText();
    }
}
