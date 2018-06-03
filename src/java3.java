import javax.swing.*;
import java.awt.*;

public class java3 extends JFrame

{

    public static JTextPane textPane;

    public static JScrollPane scrollPane;

    JPanel panel;

    public java3()

    {

        super("java3()");

        panel=new JPanel();

        panel.setLayout(new BorderLayout());

        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        textPane=new JTextPane();

        textPane.setFont(new Font("monospaeed",

                Font.PLAIN,12));

        scrollPane=new JScrollPane(textPane);

        panel.add(scrollPane);

        scrollPane.setPreferredSize(new Dimension(300,250));

        setContentPane(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent jcom=null;
        LineNumber lineNumber=new LineNumber(jcom);

        scrollPane.setRowHeaderView(lineNumber);

    }

    public static void main(String[]args)

    {

        java3 ttp=new java3();

        ttp.pack();

        ttp.setVisible(true);

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

    public void setLineHeight(int lineHeight)

    {

        if(lineHeight>=0)

        this.lineHeight=lineHeight;

    }

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

        g.fillRect(drawHere.x,drawHere.y,drawHere.

                width,drawHere.height);

        g.setColor(getForeground());

        int startLineNumber=(drawHere.y/lineHeight)+1;

        int endLineNumber = startLineNumber+

                (drawHere.height/lineHeight);

        int start=(drawHere.y/lineHeight)*lineHeight+startOffset;

        for(int i=startLineNumber;i<=endLineNumber;i++)

        {

            String lineNumber=String.valueOf(i);

            int width=fontMetrics.stringWidth(lineNumber

            );

            g.drawString(lineNumber,MARGIN+currentRowWidth-width,start);

            start+=lineHeight;

        }

        setPreferredWidth(endLineNumber);

    }

}

