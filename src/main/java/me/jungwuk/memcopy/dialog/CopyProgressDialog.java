package me.jungwuk.memcopy.dialog;

public class CopyProgressDialog extends MultiLineDialog {
    public CopyProgressDialog(String path) {
        super("복사 중 " + path, 2);
    }

    public void setReadingStatus(String text) {
        setText(0, text);
    }

    public void setWritingStatus(String text) {
        setText(1, text);
    }

}
