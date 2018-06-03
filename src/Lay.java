import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;

public class Lay extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;//这一行必须要写！！！
    private JButton[] contro=new JButton[9];//创建一个九个控件的控件数组  
    private String[] name={"5","01","02","03","04","1","2","3","4"};
    //private Container container;//创建容器；
    private JPanel container=new JPanel();
    private BorderLayout layout;//创建一个borderlayout布局控制器
    private JPanel In=new JPanel();
    public Lay()//构造函数  
    {
        add(container);
        //super("实例");
        layout=new BorderLayout();
        //container=getContentPane();
        container.setLayout(layout);//给定布局方式
        for(int i=0;i<=8;i++)
        {
            contro[i]=new JButton(name[i]);
            //container.add(contro[i]);  
        }
        //container.add("Center", contro[0]);//中间的区域会空出来  
        container.add("North", contro[1]);
        container.add("South", contro[2]);
        container.add("East", contro[3]);
        container.add("West", contro[4]);
        In.setLayout(new BorderLayout());//每个容器都用其自己本身的布局管理器  
        //上述代码很重要！！！  

        In.add("North", contro[5]);
        In.add("Center", contro[0]);
        In.add("South", contro[6]);
        In.add("East", contro[7]);
        In.add("West", contro[8]);
        container.add("Center",In);//将两个布局进行叠加  


        setVisible(true);
        setSize(800,600);
    }


    public static void main(String[] args) {
        Lay newpane=new Lay();
        newpane.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}