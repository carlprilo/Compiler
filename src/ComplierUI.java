import ldylex.TextLex;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import parsing.TextParse;
import semantic.SemanticAnalyse;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ComplierUI extends JFrame{

    private Toolkit kit=Toolkit.getDefaultToolkit();
    private Dimension screenSize=kit.getScreenSize();
    //设置框架的宽高
    private int myWIDTH=screenSize.width;
    private int myHEIGHT=screenSize.height;

    private JPanel panel_south2 = new SymbolTable();//具体放置语法树、符号表、三地址指令的panel
    private JPanel panel_right;//放置上面组件的panel

    //private EditArea editArea;
    private JTextArea textArea; //编辑框
    private JPanel panel_east;
    //private JScrollPane scrollPane2;
    private DefaultTableModel modelResult;
    private DefaultTableModel modelDeduction;
    private DefaultTableModel modelSymbol;
    private DefaultTableModel modelTriple;
    private JTable tableResult;
    private JTable tableDeduction;
    private JTable tableSymbol;
    private JTable tableTriple;
    private JScrollPane paneResult;
    private JScrollPane paneDeduction;
    private JScrollPane paneSymbol;
    private JScrollPane paneTriple;

    private TextLex lex;
    private int partIndex;

    public ComplierUI()
    {
        BorderLayout layout=new BorderLayout();
        setLayout(layout);//给定布局方式



        //第一个菜单栏file
        JMenu fileMenu = new JMenu("File");

        //file的内容
        fileMenu.add(new TestAction("New"));
        JMenuItem openItem = fileMenu.add(new TestAction("Open"));
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));//快捷键

        fileMenu.addSeparator();//分割线

        //点击事件
        Action saveAction;
        Action saveAsAction;
        saveAction = new TestAction("Save");
        JMenuItem saveItem = fileMenu.add(saveAction);
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));

        saveAsAction = new TestAction("Save As");
        fileMenu.add(saveAsAction);
        fileMenu.addSeparator();

        fileMenu.add(new AbstractAction("Exit")//退出程序
        {
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0);
            }
        });



        //菜单栏edit
        //添加icon
        URL url = UITest.class.getResource("/cut.png");
        ImageIcon icon_cut = new ImageIcon(url);
        url = UITest.class.getResource("/copy.png");
        ImageIcon icon_copy = new ImageIcon(url);
        url = UITest.class.getResource("/paste.png");
        ImageIcon icon_paste = new ImageIcon(url);

        Action cutAction = new TestAction("Cut");
        cutAction.putValue(Action.SMALL_ICON, icon_cut);
        Action copyAction = new TestAction("Copy");
        copyAction.putValue(Action.SMALL_ICON, icon_copy);
        Action pasteAction = new TestAction("Paste");
        pasteAction.putValue(Action.SMALL_ICON, icon_paste);

        //菜单栏内容添加
        JMenu editMenu = new JMenu("Edit");
        editMenu.add(cutAction);
        editMenu.add(copyAction);
        editMenu.add(pasteAction);

        //菜单栏的嵌套
        //嵌套内容
        JCheckBoxMenuItem readonlyItem = new JCheckBoxMenuItem("Read-only");//复选框
        readonlyItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                boolean saveOk = !readonlyItem.isSelected();
                saveAction.setEnabled(saveOk);
                saveAsAction.setEnabled(saveOk);
            }
        });

        ButtonGroup group = new ButtonGroup();//单选框
        JRadioButtonMenuItem OptiontItem1 = new JRadioButtonMenuItem("Option1");
        OptiontItem1.setSelected(true);
        JRadioButtonMenuItem OptiontItem2 = new JRadioButtonMenuItem("Option2");
        group.add(OptiontItem1);
        group.add(OptiontItem2);

        //菜单栏嵌套
        JMenu optionMenu = new JMenu("Options");

        optionMenu.add(readonlyItem);
        optionMenu.addSeparator();//分割线
        optionMenu.add(OptiontItem1);
        optionMenu.add(OptiontItem2);

        editMenu.addSeparator();
        editMenu.add(optionMenu);



        //菜单栏help
        //为菜单栏选项设置单字母（下划线）
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');//设置下划线
        JMenuItem indexItem = new JMenuItem("Index");
        indexItem.setMnemonic('I');
        helpMenu.add(indexItem);
        //为菜单栏选项设置单字母快捷键
        Action aboutAction = new TestAction("About");
        aboutAction.putValue(Action.MNEMONIC_KEY, new Integer('A'));
        helpMenu.add(aboutAction);


        //将菜单栏内容添加配置
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setPreferredSize(new Dimension(myWIDTH,myHEIGHT/30));
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        menuBar.setBorder(BorderFactory.createEtchedBorder());//设置边框


        //配置icon
        url = UITest.class.getResource("/next.png");
        ImageIcon icon_next = new ImageIcon(url);
        url = UITest.class.getResource("/build.png");
        ImageIcon icon_build = new ImageIcon(url);
        url = UITest.class.getResource("/run.png");
        ImageIcon icon_run = new ImageIcon(url);
        url = UITest.class.getResource("/build_run.png");
        ImageIcon icon_b_r = new ImageIcon(url);
        url = UITest.class.getResource("/sync.png");
        ImageIcon icon_sync = new ImageIcon(url);



        Action nextAction = new toolAction("Next Step", icon_next);
        Action buildAction = new toolAction("Build", icon_build);
        Action runAction = new toolAction("Run", icon_run);
        Action brAction= new toolAction("Build and Run",icon_b_r);
        Action syncAction= new toolAction("Sync",icon_sync);




        //工具栏
        JToolBar bar = new JToolBar();
        bar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bar.add(nextAction);
        bar.add(buildAction);
        bar.add(runAction);
        bar.add(brAction);
        bar.addSeparator();
        bar.add(syncAction);
        add("North",bar);


        //右键弹出菜单
        JPopupMenu popup = new JPopupMenu();
        popup.add(cutAction);
        popup.addSeparator();
        popup.add(copyAction);
        popup.addSeparator();
        popup.add(pasteAction);


        //编辑框
//        final JTextArea textArea = new JTextArea(15, 40);
//        textArea.setComponentPopupMenu(popup);//添加右键菜单
//        JScrollPane scrollPane = new JScrollPane(textArea);
//        scrollPane.setPreferredSize(new Dimension(myWIDTH/3,3*myHEIGHT/5));//设置编辑框的大小
        //editArea = new EditArea();
        textArea = new JTextArea();
        //editArea.setComponentPopupMenu(popup);//添加右键菜单
        textArea.setPreferredSize(new Dimension(myWIDTH/3,3*myHEIGHT/5));//设置编辑框的大小
        pack();



        //显示词法分析表的内容
        panel_east = new LexicalTable();
        panel_east.setPreferredSize(new Dimension(myWIDTH/3-36,3*myHEIGHT/5));//全屏则-20
        panel_east.setBorder(BorderFactory.createEtchedBorder());
        //
        //
        //
        //待添加
        //
        //
        //


        //显示推导过程
        JPanel panel_south = new DeductionTable();
        panel_south.setPreferredSize(new Dimension(2*myWIDTH/3,2*myHEIGHT/5));
        panel_south.setBorder(BorderFactory.createEtchedBorder());
        //
        //
        //
        //待添加
        //
        //
        //


        //边框布局容器
        JPanel panel_con = new JPanel();
        add("Center",panel_con);
        panel_con.setPreferredSize(new Dimension(2*myWIDTH/3,0));
        panel_con.setBorder(BorderFactory.createEtchedBorder());
        panel_con.add("Center",textArea);
        panel_con.add("East",panel_east);
        panel_con.add("South",panel_south);
        pack();




        //输出语法树，符号表，三地址指令
        panel_right = new JPanel();
        panel_right.setBackground(Color.LIGHT_GRAY);
        add("East",panel_right);
        panel_right.setPreferredSize(new Dimension(myWIDTH/3,myHEIGHT));
        panel_right.setBorder(BorderFactory.createEtchedBorder());
        JPanel outputbar = new JPanel();
        outputbar.setPreferredSize(new Dimension(myWIDTH/3,myHEIGHT/27));
        outputbar.setBorder(BorderFactory.createEtchedBorder());
        outputbar.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton istree = new JButton("Tree");
        JButton istable = new JButton("Table");
        JButton isaddress = new JButton("Triples");
        ChangeAction stree = new ChangeAction("Tree");
        ChangeAction stable = new ChangeAction("Table");
        ChangeAction saddress = new ChangeAction("Triples");
        istree.addActionListener(stree);
        istable.addActionListener(stable);
        isaddress.addActionListener(saddress);

        outputbar.add(istree);
        outputbar.add(istable);
        outputbar.add(isaddress);
        panel_right.add("North",outputbar);

        panel_right.add("Center", panel_south2);

        //
        //
        //
        //待添加
        //
        //
        //



        // the following line is a workaround for bug 4966109
        panel_right.addMouseListener(new MouseAdapter() {});



        //底边，无作用
        JPanel panel_down = new JPanel();
        add("South",panel_down);
        panel_down.setPreferredSize(new Dimension(0,0));
    }



    //输出验证点击事件（菜单栏）
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
            String action = getValue(Action.NAME).toString();
            System.out.println(action + " selected.......");

        }
    }



    //toolbar按钮作用（工具栏）
    class toolAction extends AbstractAction
    {
        public toolAction(String name, Icon icon)
        {
            putValue(Action.NAME, name);
            putValue(Action.SMALL_ICON, icon);
            putValue(Action.SHORT_DESCRIPTION, name+"");//简介
        }

        public void actionPerformed(ActionEvent event)
        {
            String action = getValue(Action.NAME).toString();
            System.out.println(action + " selected.......");
            if(action.equals("Run")){
                String text = textArea.getText();
                System.out.println("lex start");
                System.out.println(text);
                lex = new TextLex(text,modelResult,null);
                lex.scannerAll();
                System.out.println("lex finish");
                ArrayList<String> lex_result_stack =lex.get_Lex_Result();
                TextParse textParse = new TextParse(lex_result_stack,modelDeduction);
                textParse.Parsing();
                System.out.println("parsing finsished");

                SemanticAnalyse semanticanalyse = new SemanticAnalyse(text, modelSymbol, null);
                semanticanalyse.Parsing();
            }
            else if(action.equals("Next Step")) {
                System.out.println("This is Next Step");
                if(lex==null){
                    String text = textArea.getText();
                    lex = new TextLex(text,modelResult,null);
                    partIndex=0;
                }

                // 将字符串延长一位，防止溢出
                lex.text = lex.text+'\0';
                if(partIndex<lex.text_length){
                    char c = lex.text.charAt(partIndex);
                    if(c==' '||c=='\t')
                        partIndex++;
                    else if (c=='\r'||c=='\n') {
                        lex.row_number++;
                        partIndex++;
                    }
                    else
                        partIndex=lex.scannerPart(partIndex);
                }
            }
            //按钮点击事件
            //
            //
            //
            //待添加
            //
            //
            //
        }
    }



    //切换三个显示
    class ChangeAction extends AbstractAction
    {
        public ChangeAction(String name)
        {
            putValue(Action.NAME, name);
        }

        public void actionPerformed(ActionEvent event)
        {
            String change = getValue(Action.NAME) +"";
            System.out.println(change+change.length());
            panel_right.remove(panel_south2);//必须要先移除，不然ui不会更新
            if (change.length() == 4)  panel_south2 = new JPanel();//该情况清空
            else if (change.length() == 5)  panel_south2 = new SymbolTable();
            else if (change.length() == 7) panel_south2 = new TriplesTable();
            panel_right.add("Center", panel_south2);//重新添加table
            panel_right.updateUI();//更新UI
            //按钮点击事件
            //
            //
            //
            //待添加
            //
            //
            //
        }
    }



    //词法分析
    class LexicalTable extends JPanel
    {

        private String[] columnNames = { "名称", "值"};
        private Object[][] cells = {
                { "这是", "词法分析"}
        };

        public LexicalTable()
        {
            modelResult = new DefaultTableModel(cells,columnNames);
            tableResult = new JTable(modelResult);
            tableResult.setAutoCreateRowSorter(true);
            paneResult = new JScrollPane(tableResult);
            //paneResult.setViewportView(tableResult);
            paneResult.setPreferredSize(new Dimension(myWIDTH/3-36,3*myHEIGHT/5));
            this.add(paneResult);
        }
    }



    //推导过程
    class DeductionTable extends JPanel
    {

        private String[] columnNames = { "推导", "运用的产生式"};
        private Object[][] cells =
                {
                        { "我是", "推导过程"},
                        //{ "Venus", 6052.0}, { "Earth", 6378.0},{ "Mars", 3397.0}, { "Jupiter", 71492.0},{ "Saturn", 60268.0},{ "Uranus", 25559.0}, { "Neptune", 24766.0},
                };

        public DeductionTable()
        {
            modelDeduction = new DefaultTableModel(cells,columnNames);
            tableDeduction = new JTable(modelDeduction);
            tableDeduction.setAutoCreateRowSorter(true);
            paneDeduction = new JScrollPane(tableDeduction);
            //scrollPane2.setViewportView(table);
            paneDeduction.setPreferredSize(new Dimension(2*myWIDTH/3-30,27*myHEIGHT/100));
            this.add(paneDeduction);
        }
    }


    //符号表
    class SymbolTable extends JPanel
    {
        private String[] columnNames = { "变量名称", "所属类型", "长度", "内存地址"};
        private Object[][] cells =
                {
                        { "我", "是", "符号", "表"},
                };

        public SymbolTable()
        {
            modelSymbol= new DefaultTableModel(null, columnNames);
            //modelSymbol.setAutoCreateRowSorter(true);
            tableSymbol = new JTable(modelSymbol);
            tableSymbol.setAutoCreateRowSorter(true);
            paneSymbol = new JScrollPane(tableSymbol);
            //scrollPane2.setViewportView(table);
            paneSymbol.setPreferredSize(new Dimension(myWIDTH/3-6,2*myHEIGHT/3));
            this.add(paneSymbol);
        }
    }


    //三地址指令
    class TriplesTable extends JPanel
    {
        private String[] columnNames = { "序号", "三地址码"};
        private Object[][] cells =
                {
                        { "我是", "三地址指令"},
                };

        public TriplesTable()
        {
            //JTable table = new JTable(cells, columnNames);
            modelTriple = new DefaultTableModel(null,columnNames);
            tableTriple = new JTable(modelTriple);
            tableTriple.setAutoCreateRowSorter(true);
            //JScrollPane scrollPane2 = new JScrollPane();
            //scrollPane2.setViewportView(table);
            paneTriple = new JScrollPane(tableTriple);
            paneTriple.setPreferredSize(new Dimension(myWIDTH/3-6,2*myHEIGHT/3));
            this.add(paneTriple);
        }
    }
}