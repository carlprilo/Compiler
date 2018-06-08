//import ldylex.TextLex;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.net.URL;
//import javax.swing.*;
//
//public class MenuFrame extends JFrame{
//    //点击事件
//    private Action saveAction;
//    private Action saveAsAction;
//    private JCheckBoxMenuItem readonlyItem; //复选框
//    private JPopupMenu popup;//右键弹出菜单
//    private JPanel panel_east;//panel
//    private BorderLayout layout;
//    private JScrollPane inputPane;//输入框
//    private JTextArea textArea;
//    private Toolkit kit=Toolkit.getDefaultToolkit();
//    private Dimension screenSize=kit.getScreenSize();
//    //设置框架的宽高
//    private int WIDTH=screenSize.width;
//    private int HEIGHT=screenSize.height;
//
//
//
//    //输出验证点击事件
//    class TestAction extends AbstractAction
//    {
//        public TestAction(String name)
//        {
//            super(name);
//        }
//
//        public void actionPerformed(ActionEvent event)
//        {
//            System.out.println(getValue(Action.NAME) + " selected.");
//        }
//    }
//
//    public MenuFrame()
//    {
//        layout=new BorderLayout();
//        setLayout(layout);//给定布局方式
//
//
//
//        //第一个菜单栏file
//        JMenu fileMenu = new JMenu("File");
//
//        //file的内容
//        fileMenu.add(new TestAction("New"));
//        JMenuItem openItem = fileMenu.add(new TestAction("Open"));
//        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));//快捷键
//
//        fileMenu.addSeparator();//分割线
//
//        saveAction = new TestAction("Save");
//        JMenuItem saveItem = fileMenu.add(saveAction);
//        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
//
//        saveAsAction = new TestAction("Save As");
//        fileMenu.add(saveAsAction);
//        fileMenu.addSeparator();
//
//        fileMenu.add(new AbstractAction("Exit")//退出程序
//        {
//            public void actionPerformed(ActionEvent event)
//            {
//                System.exit(0);
//            }
//        });
//
//
//
//        //菜单栏edit
//        //添加icon
//        URL url = ToolBarFrame.class.getResource("/cut.gif");
//        ImageIcon icon_cut = new ImageIcon(url);
//        url = ToolBarFrame.class.getResource("/copy.gif");
//        ImageIcon icon_copy = new ImageIcon(url);
//        url = ToolBarFrame.class.getResource("/paste.gif");
//        ImageIcon icon_paste = new ImageIcon(url);
//
//        Action cutAction = new TestAction("Cut");
//        cutAction.putValue(Action.SMALL_ICON, icon_cut);
//        Action copyAction = new TestAction("Copy");
//        copyAction.putValue(Action.SMALL_ICON, icon_copy);
//        Action pasteAction = new TestAction("Paste");
//        pasteAction.putValue(Action.SMALL_ICON, icon_paste);
//
//        //菜单栏内容添加
//        JMenu editMenu = new JMenu("Edit");
//        editMenu.add(cutAction);
//        editMenu.add(copyAction);
//        editMenu.add(pasteAction);
//
//        //菜单栏的嵌套
//        //嵌套内容
//        readonlyItem = new JCheckBoxMenuItem("Read-only");//复选框
//        readonlyItem.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent event)
//            {
//                boolean saveOk = !readonlyItem.isSelected();
//                saveAction.setEnabled(saveOk);
//                saveAsAction.setEnabled(saveOk);
//            }
//        });
//
//        ButtonGroup group = new ButtonGroup();//单选框
//        JRadioButtonMenuItem OptiontItem1 = new JRadioButtonMenuItem("Option1");
//        OptiontItem1.setSelected(true);
//        JRadioButtonMenuItem OptiontItem2 = new JRadioButtonMenuItem("Option2");
//        group.add(OptiontItem1);
//        group.add(OptiontItem2);
//
//        //菜单栏嵌套
//        JMenu optionMenu = new JMenu("Options");
//
//        optionMenu.add(readonlyItem);
//        optionMenu.addSeparator();//分割线
//        optionMenu.add(OptiontItem1);
//        optionMenu.add(OptiontItem2);
//
//        editMenu.addSeparator();
//        editMenu.add(optionMenu);
//
//
//
//        //菜单栏help
//        //为菜单栏选项设置单字母（下划线）
//        JMenu helpMenu = new JMenu("Help");
//        helpMenu.setMnemonic('H');//设置下划线
//        JMenuItem indexItem = new JMenuItem("Index");
//        indexItem.setMnemonic('I');
//        helpMenu.add(indexItem);
//        //为菜单栏选项设置单字母快捷键
//        Action aboutAction = new TestAction("About");
//        aboutAction.putValue(Action.MNEMONIC_KEY, new Integer('A'));
//        helpMenu.add(aboutAction);
//
//
//        //将菜单栏内容添加配置
//        JMenuBar menuBar = new JMenuBar();
//        setJMenuBar(menuBar);
//
//        menuBar.add(fileMenu);
//        menuBar.add(editMenu);
//        menuBar.add(helpMenu);
//        menuBar.setBorder(BorderFactory.createEtchedBorder());
//
//
//        //配置icon
//        url = ToolBarFrame.class.getResource("/blue-ball.gif");
//        ImageIcon icon_blue = new ImageIcon(url);
//        url = ToolBarFrame.class.getResource("/yellow-ball.gif");
//        ImageIcon icon_yellow = new ImageIcon(url);
//        url = ToolBarFrame.class.getResource("/red-ball.gif");
//        ImageIcon icon_red = new ImageIcon(url);
//        url = ToolBarFrame.class.getResource("/exit.gif");
//        ImageIcon icon_exit = new ImageIcon(url);
//
//
//        Action blueAction = new ColorAction("Blue", icon_blue, Color.BLUE);
//        Action yellowAction = new ColorAction("Yellow", icon_yellow, Color.YELLOW);
//        Action redAction = new ColorAction("Red", icon_red, Color.RED);
//        Action exitAction= new ColorAction("White",icon_exit,Color.WHITE);
//
////        Action exitAction = new AbstractAction("Exit", icon_exit)
////        {
////            public void actionPerformed(ActionEvent event)
////            {
////                System.exit(0);
////            }
////        };
////        exitAction.putValue(Action.SHORT_DESCRIPTION, "Exit");
//
//
//
//        //工具栏
//        JToolBar bar = new JToolBar();
//        bar.add(blueAction);
//        bar.add(yellowAction);
//        bar.add(redAction);
//        bar.addSeparator();
//        bar.add(exitAction);
//        add("North",bar);
//
//
//
//        //弹出菜单
//        popup = new JPopupMenu();
//        popup.add(cutAction);
//        popup.add(copyAction);
//        popup.add(pasteAction);
//
//        //输入框
//        textArea = new JTextArea(15, 40);
//        textArea.setComponentPopupMenu(popup);
//        inputPane = new JScrollPane(textArea);
//        add("Center",inputPane);
//
//
//
//        //输出栏
//        panel_east = new JPanel();
//        panel_east.setBackground(Color.WHITE);
//        add("East",panel_east);
//        panel_east.setPreferredSize(new Dimension(WIDTH/3,0));
//        panel_east.setBorder(BorderFactory.createEtchedBorder());
//        JPanel outputbar = new JPanel();
//        outputbar.setPreferredSize(new Dimension(WIDTH/3,HEIGHT/30));
//        outputbar.setBorder(BorderFactory.createEtchedBorder());
//        outputbar.setLayout(new FlowLayout(FlowLayout.RIGHT));
//        JButton tree = new JButton("Tree");
//        JButton Table = new JButton("Table");
//        outputbar.add(tree);
//        outputbar.add(Table);
//        panel_east.add("North",outputbar);
//
//
//
//
//        // the following line is a workaround for bug 4966109
//        panel_east.addMouseListener(new MouseAdapter() {});
//
//        JPanel panel_south = new JPanel();
//        add("South",panel_south);
//        panel_south.setPreferredSize(new Dimension(WIDTH,HEIGHT/4));
//
//    }
//
//
//    //在没有添加textfield的情况下使背景变色
//    class ColorAction extends AbstractAction
//    {
//        public ColorAction(String name, Icon icon, Color c)
//        {
//            putValue(Action.NAME, name);
//            putValue(Action.SMALL_ICON, icon);
//            putValue(Action.SHORT_DESCRIPTION, name + " background");//简介
//            putValue("Color", c);
//        }
//
//        public void actionPerformed(ActionEvent event)
//        {
//            Color c = (Color) getValue("Color");
//            panel_east.setBackground(c);
//
//            if(c.equals(Color.blue))
//            {
//                TextLex lex = new TextLex(textArea.getText(),null,null);
//                System.out.println("This is blue button"); // add by cp
//                lex.scannerAll();
//
//            }
//        }
//    }
//}
