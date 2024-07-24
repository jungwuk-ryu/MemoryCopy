package me.jungwuk.memcopy.dialog;

import javax.swing.*;
import java.awt.*;

public class TwoLineDialog extends JDialog {
    private JLabel firstLabel;
    private JLabel secondLabel;

    public TwoLineDialog(String title) {
        super(null, title, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(300, 100);
        this.setLocationRelativeTo(this);

        layoutComponents();
    }

    public void setFirstLine(String text) {
        safeTextEdit(firstLabel, text);
    }

    public void setSecondLine(String text) {
        safeTextEdit(secondLabel, text);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel(getTitle(), JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));

        firstLabel = new JLabel("", JLabel.CENTER);
        column.add(firstLabel);
        secondLabel = new JLabel("", JLabel.CENTER);
        column.add(secondLabel);

        add(column, BorderLayout.CENTER);
    }

    private void safeTextEdit(JLabel label, String text) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> safeTextEdit(label, text));
        } else {
            System.out.println(text);
            label.setText(text);
        }
    }

}
