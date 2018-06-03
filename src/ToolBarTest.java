import javax.swing.*;
import java.awt.*;

public class ToolBarTest {
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                ToolBarFrame frame = new ToolBarFrame();
                frame.setTitle("ToolBarTest");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
