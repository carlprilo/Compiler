import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class UITest {
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new ComplierUI();

                //当框架被关闭的时候结束程序
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //设置框架的大小
                //获取当前屏幕的宽高设置框架
                Toolkit kit=Toolkit.getDefaultToolkit();
                Dimension screenSize=kit.getScreenSize();
                //设置框架的宽高
                int WIDTH=screenSize.width;
                int HEIGHT=screenSize.height;
                frame.setSize(WIDTH,HEIGHT);

                //设置框架的位置
                frame.setLocation(0,0);

                //设置Icon、标题、位置
                Toolkit tool=frame.getToolkit(); //得到一个Toolkit对象
                URL url =  UITest.class.getResource("/Icon.png");
                Image myimage=tool.getImage(url); //由tool获取图像
                frame.setIconImage(myimage);
                frame.setTitle("这是一个厉害的编译器");

                //框架最初不可见，手动设置可见性
                frame.setVisible(true);
            }
        });
    }
}
