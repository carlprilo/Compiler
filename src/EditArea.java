import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

import static javax.swing.text.DefaultEditorKit.cutAction;

public class EditArea extends JPanel
{
    private Toolkit kit=Toolkit.getDefaultToolkit();
    private Dimension screenSize=kit.getScreenSize();
    //设置框架的宽高
    private int myWIDTH=screenSize.width;
    private int myHEIGHT=screenSize.height;

    public static JTextPane textPane;
    public static JScrollPane scrollPane;
    JPanel panel;
    public EditArea()
    {
        panel=new JPanel();
        panel.setLayout(new BorderLayout());
        //panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        textPane=new JTextPane();




        //添加icon
        URL url = UITest.class.getResource("/cut.png");
        ImageIcon icon_cut = new ImageIcon(url);
        url = UITest.class.getResource("/copy.png");
        ImageIcon icon_copy = new ImageIcon(url);
        url = UITest.class.getResource("/paste.png");
        ImageIcon icon_paste = new ImageIcon(url);

        Action cutAction = new EditArea.TestAction("Cut");
        cutAction.putValue(Action.SMALL_ICON, icon_cut);
        Action copyAction = new EditArea.TestAction("Copy");
        copyAction.putValue(Action.SMALL_ICON, icon_copy);
        Action pasteAction = new EditArea.TestAction("Paste");
        pasteAction.putValue(Action.SMALL_ICON, icon_paste);

        //右键弹出菜单
        JPopupMenu popup = new JPopupMenu();
        popup.add(cutAction);
        popup.addSeparator();
        popup.add(copyAction);
        popup.addSeparator();
        popup.add(pasteAction);

        textPane.setComponentPopupMenu(popup);//右键弹出菜单



        textPane.setFont(new Font("monospaeed", Font.PLAIN,12));
        scrollPane=new JScrollPane(textPane);
        panel.add(scrollPane);
        scrollPane.setPreferredSize(new Dimension(myWIDTH/3,3*myHEIGHT/5));
        add(panel);
        JComponent jcom=null;
        LineNumber lineNumber=new LineNumber(jcom);
        scrollPane.setRowHeaderView(lineNumber);
    }


    //复制粘贴等点击事件
    class TestAction extends AbstractAction
    {
        public TestAction(String name)
        {
            super(name);
        }

        public void actionPerformed(ActionEvent event)
        {
            //按钮点击事件
            //
            //
            //
            //待添加
            //
            //
            //
            System.out.println(getValue(Action.NAME) + " selected.");
        }
    }
}

class LineNumber extends JTextPane
{
    private final static Color DEFAULT_BACKGROUND=Color.gray;
    private final static Color DEFAULT_FOREGROUND=Color.black;
    private final static Font DEFAUlT_FONT=new Font("monospaced",Font.PLAIN,12);
    private final static int HEIGHT=Integer.MAX_VALUE-1000000;
    private final static int MARGIN=5;
    private FontMetrics fontMetrics;
    private int lineHeight;
    private int currentRowWidth;
    private JComponent component;
    private int componentFontHeight;
    private int componentFontAscent;
    public LineNumber(JComponent component)
    {
        if(component==null)
        {
            setBackground(DEFAULT_BACKGROUND);
            setForeground(DEFAULT_FOREGROUND);
            setFont(DEFAUlT_FONT);
            component = this;
            this.component=this;
        }
        else
        {
            setBackground(DEFAULT_BACKGROUND);
            setForeground(component.getForeground());
            setFont(component.getFont());
            this.component=component;
        }
        componentFontHeight=component.getFontMetrics(component.getFont()).getHeight();
        componentFontAscent=component.getFontMetrics(component.getFont()).getAscent();
        setPreferredWidth(9999);
    }

    public void setPreferredWidth(int row)
    {
        int width=fontMetrics.stringWidth(String.valueOf(row));
        if(currentRowWidth<=width)
        {
            currentRowWidth=width;
            setPreferredSize(new Dimension(2 * MARGIN+width,HEIGHT));
        }
    }

    public void setFont(Font font)
    {
        super.setFont(font);
        fontMetrics=getFontMetrics(getFont());
    }

    public int getLineHeight()
    {
        if(lineHeight==0)
        return componentFontHeight;
        else
        return lineHeight;
    }

//    public void setLineHeight(int lineHeight)
//    {
//        if(lineHeight>=0)
//        this.lineHeight=lineHeight;
//    }

    public int getStartOffset()
    {
        return component.getInsets().top+componentFontAscent;
    }

    public void paintComponent(Graphics g)
    {
        int lineHeight=getLineHeight();
        int startOffset=getStartOffset();
        Rectangle drawHere=g.getClipBounds();
        g.setColor(getBackground());
        g.fillRect(drawHere.x,drawHere.y,drawHere.width,drawHere.height);
        g.setColor(getForeground());
        int startLineNumber=(drawHere.y/lineHeight)+1;
        int endLineNumber = startLineNumber+(drawHere.height/lineHeight);
        int start=(drawHere.y/lineHeight)*lineHeight+startOffset;
        for(int i=startLineNumber;i<=endLineNumber;i++)
        {
            String lineNumber=String.valueOf(i);
            int width=fontMetrics.stringWidth(lineNumber);
            g.drawString(lineNumber,MARGIN+currentRowWidth-width,start);
            start+=lineHeight;
        }
        setPreferredWidth(endLineNumber);
    }
}