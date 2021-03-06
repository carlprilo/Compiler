import ldylex.TextLex;
import ldylex.Token;
import parse.TextParse;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import static java.lang.Thread.sleep;

public class ComplierUI extends JFrame {

    private Toolkit kit = Toolkit.getDefaultToolkit();
    private Dimension screenSize = kit.getScreenSize();
    //设置框架的宽高
    private int myWIDTH = screenSize.width;
    private int myHEIGHT = screenSize.height;


    private JPanel panel_right;//放置上面组件的panel
    private JPanel panel_symbol = new SymbolTable();//具体放置语法树、符号表、三地址指令的panel
    private JPanel panel_message = new MessageTable();
    private JPanel panel_triple = new TriplesTable();

    private EditArea textArea;
    private JPanel panel_east;
    private DefaultTableModel modelResult;
    private DefaultTableModel modelDeduction;
    private DefaultTableModel modelSymbol;
    private DefaultTableModel modelTriple;
    private DefaultTableModel modelMessage;
    private JTable tableResult;
    private JTable tableDeduction;
    private JTable tableSymbol;
    private JTable tableTriple;
    private JTable tableMessage;
    private JScrollPane paneResult;
    private JScrollPane paneDeduction;
    private JScrollPane paneSymbol;
    private JScrollPane paneTriple;
    private JScrollPane paneMessage;

    private TextLex lex;
    private int partIndex;
    private TextParse textParse;
    private Token select;
    private Token old_token;
    private String rowNumber;
    private StringBuffer output_buffer;

    private JFileChooser chooser = new JFileChooser();//选择对话框

    private void updaetUI() {
        panel_right.remove(panel_symbol);//必须要先移除，不然ui不会更新
        panel_right.remove(panel_message);//不然顺序会出错
        panel_symbol = new SymbolTable();
        panel_right.add("Center", panel_symbol);//重新添加table
        panel_right.add("South", panel_message);
        panel_right.updateUI();//更新UI

    }


    public void next() {
        System.out.println("This is Next Step");
        if (lex == null) {
            String text = textArea.textPane.getText();
            lex = new TextLex(text, modelResult, modelSymbol);
            partIndex = 0;
        } else {
            partIndex = 1;
        }

        if (select != null) {
            old_token = select;
            //getAttrFromToken(old_token);
            StyledDocument d = textArea.textPane.getStyledDocument();
            SimpleAttributeSet attr = new SimpleAttributeSet();

            System.out.println(old_token.getType());

            switch (Token.getTokenDetailType(old_token)) {
                case "NUM":
                    StyleConstants.setForeground(attr, Color.blue);
                    break;
                case "ID":
                    StyleConstants.setForeground(attr, Color.MAGENTA);
                    break;
                case "=":
                case "+":
                case "-":
                case "*":
                case "/":
                    StyleConstants.setForeground(attr, Color.green);
                    break;
                case "{":
                case "}":
                case "(":
                case ")":
                case "[":
                case "]":
                    StyleConstants.setForeground(attr, Color.red);
                    break;
                case "int":
                case "real":
                case "if":
                case "then":
                case "else":
                case "while":
                    StyleConstants.setForeground(attr, Color.ORANGE);
                    break;
                case "KEY":
                    StyleConstants.setForeground(attr, Color.darkGray);
                    break;
            }
            d.setCharacterAttributes(old_token.getBegin(), old_token.getName().length(), attr, false);

        }
        select = lex.scannerStep(partIndex);

        if (select != null) {
            textArea.textPane.select(select.getBegin(), select.getEnd());
            textArea.textPane.requestFocus();


            if (textParse == null) {
                ArrayList<Token> lex_result_stack = lex.get_Lex_Result();
                textParse = new TextParse(lex_result_stack, modelDeduction,modelMessage);
                textParse.deduceArrayList.add("program");
            }
            String temp = textParse.parsingStep();
            if (output_buffer == null)
                if (output_buffer == null)
                    output_buffer = new StringBuffer();
            output_buffer.append(temp);
        }
        JScrollBar bar = paneResult.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());
        bar = paneDeduction.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());
    }

    public ComplierUI() {
        //配置文件对话框
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt", "java", "...");
        chooser.setFileFilter(filter);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                myWIDTH = getWidth();//frame的宽
                myHEIGHT = getHeight();//frame的高
                //System.out.println(myHEIGHT);
                //System.out.println(myWIDTH);
                updaetUI();
            }
        });

        BorderLayout layout = new BorderLayout();
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
            public void actionPerformed(ActionEvent event) {
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
        readonlyItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
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
        //为菜单栏选项设置单字母快捷键
        Action aboutAction = new TestAction("About");
        aboutAction.putValue(Action.MNEMONIC_KEY, new Integer('A'));
        helpMenu.add(aboutAction);


        //将菜单栏内容添加配置
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setPreferredSize(new Dimension(myWIDTH, myHEIGHT / 30));
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        menuBar.setBorder(BorderFactory.createEtchedBorder());//设置边框


        //配置icon
        url = UITest.class.getResource("/next.png");
        ImageIcon icon_next = new ImageIcon(url);
        url = UITest.class.getResource("/run.png");
        ImageIcon icon_run = new ImageIcon(url);
        url = UITest.class.getResource("/build_run.png");
        ImageIcon icon_b_r = new ImageIcon(url);
        url = UITest.class.getResource("/sync.png");
        ImageIcon icon_sync = new ImageIcon(url);


        Action nextAction = new toolAction("Next Step", icon_next);
        Action runAction = new toolAction("Run", icon_run);
        Action brAction = new toolAction("Build and Run", icon_b_r);
        Action syncAction = new toolAction("Sync", icon_sync);


        //工具栏
        JToolBar bar = new JToolBar();
        bar.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bar.add(nextAction);
        bar.add(runAction);
        bar.add(brAction);
        bar.addSeparator();
        bar.add(syncAction);
        add("North", bar);


        //右键弹出菜单
        JPopupMenu popup = new JPopupMenu();
        popup.add(cutAction);
        popup.addSeparator();
        popup.add(copyAction);
        popup.addSeparator();
        popup.add(pasteAction);


        textArea = new EditArea();
        textArea.setComponentPopupMenu(popup);//添加右键菜单
        textArea.setPreferredSize(new Dimension(myWIDTH / 3, 39 * myHEIGHT / 100));//设置编辑框的大小
        pack();


        //显示词法分析表的内容
        panel_east = new LexicalTable();
        panel_east.setPreferredSize(new Dimension(myWIDTH / 3 - 36, 2 * myHEIGHT / 5));//全屏则-20
        panel_east.setBorder(BorderFactory.createEtchedBorder());

        //显示推导过程
        JPanel panel_south = new DeductionTable();
        panel_south.setPreferredSize(new Dimension(2 * myWIDTH / 3, myHEIGHT / 2));
        panel_south.setBorder(BorderFactory.createEtchedBorder());

        //边框布局容器
        JPanel panel_con = new JPanel();
        add("Center", panel_con);
        panel_con.setPreferredSize(new Dimension(2 * myWIDTH / 3, 0));
        panel_con.setBorder(BorderFactory.createEtchedBorder());
        panel_con.add("Center", textArea);
        panel_con.add("East", panel_east);
        panel_con.add("South", panel_south);
        pack();


        //输出语法树，符号表，三地址指令
        panel_right = new JPanel();
        panel_right.setBackground(Color.LIGHT_GRAY);
        add("East", panel_right);
        panel_right.setPreferredSize(new Dimension(myWIDTH / 3, myHEIGHT));
        //panel_right.setBorder(BorderFactory.createEtchedBorder());


        panel_symbol.setPreferredSize(new Dimension(myWIDTH / 3, myHEIGHT / 3));

        panel_right.add("North", panel_symbol);


        panel_triple.setBackground(Color.WHITE);//三地址
        panel_triple.setPreferredSize(new Dimension(myWIDTH / 3 - 6, myHEIGHT / 4));
        panel_right.add("Center",panel_triple);



        panel_message.setBackground(Color.WHITE);//报错栏
        panel_message.setPreferredSize(new Dimension(myWIDTH / 3 - 6, myHEIGHT / 5));
        panel_message.setBorder(BorderFactory.createEtchedBorder());


        panel_right.add("South", panel_message);


        // the following line is a workaround for bug 4966109
        panel_right.addMouseListener(new MouseAdapter() {
        });


        //底边，无作用
        JPanel panel_down = new JPanel();
        add("South", panel_down);
        panel_down.setPreferredSize(new Dimension(0, 10));
    }


    //输出验证点击事件（菜单栏）
    class TestAction extends AbstractAction {
        public TestAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent event) {
            //按钮点击事件

            String action = getValue(Action.NAME).toString();
            System.out.println(action + " selected.......");

            if (action == "Open") {
                System.out.println("读取文件");

                String pathname = ".\\input.txt";//默认路径//绝对路径"D:\\test.txt"

                chooser.setCurrentDirectory(new File("."));
                int result = chooser.showOpenDialog(ComplierUI.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    pathname = chooser.getSelectedFile().getPath();
                }


                try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw

                    /* 读入TXT文件 */
                    File filename = new File(pathname); // 要读取以上路径的input。txt文件
                    InputStreamReader reader = new InputStreamReader(
                            new FileInputStream(filename)); // 建立一个输入流对象reader
                    BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
                    String line = "";
                    line = br.readLine();
                    String text = "";


                    while (line != null) {
                        text = text + line + "\n";
                        line = br.readLine(); // 一次读入一行数据
                    }
                    textArea.textPane.setText(text);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (action == "Save") {
                System.out.println("存储文件");
                try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw

                    String pathname = ".\\output.txt";

                    chooser.setCurrentDirectory(new File("."));
                    // show file chooser dialog
                    int result = chooser.showOpenDialog(ComplierUI.this);
                    // if image file accepted, set it as icon of the label
                    if (result == JFileChooser.APPROVE_OPTION) {
                        pathname = chooser.getSelectedFile().getPath();
                    }

                    /* 写入Txt文件 */

                    File writename = new File(pathname); // 相对路径，如果没有则要建立一个新的output.txt文件
                    writename.createNewFile(); // 创建新文件
                    BufferedWriter out = new BufferedWriter(new FileWriter(writename));
                    out.write(output_buffer.toString()); // \r\n即为换行
                    out.flush(); // 把缓存区内容压入文件
                    out.close(); // 最后记得关闭文件

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (action == "About") {
                JOptionPane.showMessageDialog(null,
                        "     陈  攀      10152510149\n    李国辉     10152510179\n" +
                                "    陈思睿     10152510246\n     李  政      10152510250\n    王铁林     10152510276",
                        "小组成员", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    //toolbar按钮作用（工具栏）
    class toolAction extends AbstractAction {
        public toolAction(String name, Icon icon) {
            putValue(Action.NAME, name);
            putValue(Action.SMALL_ICON, icon);
            putValue(Action.SHORT_DESCRIPTION, name + "");//简介
        }

        public void actionPerformed(ActionEvent event) {
            String action = getValue(Action.NAME).toString();
            System.out.println(action + " selected.......");
            if (action.equals("Run")) {


                textArea.textPane.requestFocus();
                String text = textArea.textPane.getText();
                System.out.println("lex start");
                System.out.println(text);
                lex = new TextLex(text, modelResult, modelSymbol);
                lex.scannerAll();
                System.out.println("lex finish");
                ArrayList<Token> lex_result_stack = lex.get_Lex_Result();
                textParse = new TextParse(lex_result_stack, modelDeduction,modelMessage);
                textParse.parsing();
                System.out.println("parse finsished");

                //todo semantic

            } else if (action.equals("Next Step")) {
                next();
            } else if (action.equals("Sync")) {
                System.out.println("Touch sync");
                while (modelResult.getRowCount() > 0)
                    modelResult.removeRow(modelResult.getRowCount() - 1);
                while (modelDeduction.getRowCount() > 0)
                    modelDeduction.removeRow(modelDeduction.getRowCount() - 1);
                lex = null;
                textParse = null;
                output_buffer = new StringBuffer();
                textArea.textPane.setForeground(Color.black);
            } else if (action.equals("Build and Run")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("in");
                        do {
                            next();
                            try {
                                sleep(200);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (old_token != select && select != null);
                        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
                            String pathname = ".\\output.txt";
                            chooser.setCurrentDirectory(new File("."));
                            int result = chooser.showOpenDialog(ComplierUI.this);
                            if (result == JFileChooser.APPROVE_OPTION) {
                                pathname = chooser.getSelectedFile().getPath();
                            }
                            File writename = new File(pathname);
                            writename.createNewFile();
                            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
                            out.write(output_buffer.toString());
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        }
    }


    //词法分析
    class LexicalTable extends JPanel {
        private String[] columnNames = {"Token", "Type", "Line", "Position"};
        private Object[][] cells = {
        };

        public LexicalTable() {
            modelResult = new DefaultTableModel(cells, columnNames);
            tableResult = new JTable(modelResult);
            tableResult.setAutoCreateRowSorter(true);
            paneResult = new JScrollPane(tableResult);
            //paneResult.setViewportView(tableResult);
            paneResult.setPreferredSize(new Dimension(myWIDTH / 3 - 36, 39 * myHEIGHT / 100));
            this.add(paneResult);
        }
    }

    //推导过程
    class DeductionTable extends JPanel {
        private String[] columnNames = {"位置", "推导", "运用的产生式"};
        private Object[][] cells = {};

        public DeductionTable() {
            modelDeduction = new DefaultTableModel(cells, columnNames);
            tableDeduction = new JTable(modelDeduction);
            tableDeduction.setAutoCreateRowSorter(true);
            paneDeduction = new JScrollPane(tableDeduction);
            //scrollPane2.setViewportView(table);
            paneDeduction.setPreferredSize(new Dimension(2 * myWIDTH / 3 - 30, 2 * myHEIGHT / 5));
            this.add(paneDeduction);
        }
    }


    //符号表
    private class SymbolTable extends JPanel {
        private String[] columnNames = {"变量名称", "行号", "位置"};

        private SymbolTable() {
            modelSymbol = new DefaultTableModel(null, columnNames);
            //modelSymbol.setAutoCreateRowSorter(true);
            tableSymbol = new JTable(modelSymbol);
            tableSymbol.setAutoCreateRowSorter(true);
            paneSymbol = new JScrollPane(tableSymbol);
            //scrollPane2.setViewportView(table);
            paneSymbol.setPreferredSize(new Dimension(myWIDTH / 3 - 6, myHEIGHT / 3));
            this.add(paneSymbol);
        }
    }


    //三地址指令
    private class TriplesTable extends JPanel {
        private String[] columnNames = {"序号", "三地址码"};

        private TriplesTable() {
            //JTable table = new JTable(cells, columnNames);
            modelTriple = new DefaultTableModel(null, columnNames);
            tableTriple = new JTable(modelTriple);
            tableTriple.setAutoCreateRowSorter(true);
            paneTriple = new JScrollPane(tableTriple);
            paneTriple.setPreferredSize(new Dimension(myWIDTH / 3 - 6, myHEIGHT / 4));
            this.add(paneTriple);
        }
    }

    private class MessageTable extends JPanel {
        private String[] columnNames = {"位置","错误","类型"};

        private MessageTable() {
            //JTable table = new JTable(cells, columnNames);
            modelMessage = new DefaultTableModel(null, columnNames);
            tableMessage = new JTable(modelMessage);
            tableMessage.setAutoCreateRowSorter(true);
            paneMessage = new JScrollPane(tableMessage);
            paneMessage.setPreferredSize(new Dimension(myWIDTH / 3 - 6, myHEIGHT / 5));
            this.add(paneMessage);
            tableMessage.setForeground(Color.RED);
        }
    }

}