import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

public class DocAttribute {
    //返回光标所在列
    public static int getColumnAtCaret(JTextComponent component) {
        int caretPosition = component.getCaretPosition();
        Element root = component.getDocument().getDefaultRootElement();
        int line = root.getElementIndex(caretPosition);
        int lineStart = root.getElement(line).getStartOffset();

        return caretPosition - lineStart + 1;
    }

    //获取指定行的第一个字符位置
    public static int getLineStart(JTextComponent component, int line) {
        int lineNumber = line - 1;
        Element root = component.getDocument().getDefaultRootElement();
        int lineStart = root.getElement(lineNumber).getStartOffset();
        return lineStart;
    }

    //返回选中的字符数
    public static int getSelectedNumber(JTextComponent component) {
        if (component.getSelectedText() == null)
            return 0;
        else
            return component.getSelectedText().length();
    }

    //返回光标所在行
    public static int getLineAtCaret(JTextComponent component) {
        int caretPosition = component.getCaretPosition();
        Element root = component.getDocument().getDefaultRootElement();

        return root.getElementIndex(caretPosition) + 1;
    }

    //返回文本行数
    public static int getLines(JTextComponent component) {
        Element root = component.getDocument().getDefaultRootElement();
        return root.getElementCount();
    }

    //返回文本框的字符总数
    public static int getCharNumber(JTextComponent component) {
        Document doc = component.getDocument();
        return doc.getLength();
    }
}