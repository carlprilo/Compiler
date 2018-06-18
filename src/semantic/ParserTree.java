package semantic;

import ldylex.Token;
import ldylex.TokenType;
import java.util.ArrayList;
import java.util.List;

/*class TreeNode{
        String node;
        TreeNode leftchild,rightchild;
        TreeNode(String str) {
                node = str;
                leftchild = rightchild = null;
        }
        boolean inVN(){
                for ()
                        if()
                                return true;
                return false;
        }//判断是否为非终结符
}*/
public class ParserTree {
        public int ParserCount = 0;//语法树节点编号
        public List<ParserNode> ParserTree = new ArrayList<>(); //语法树
        public List<List<Integer>> Level = new ArrayList<>();//层级数组，level[i],[j]表示语法树的第i层从左到右数第j个节点
        //public PositionNode[] position = new PositionNode[500];
        public List<String> ThreeAddress = new ArrayList<>(); //三地址语句数组
        public int Plevel;//当前层级

        public boolean EndIsEmpty = false;

        boolean SemanticHaveError = false;
        public int ThreeAddressCount = 0;
        public int RegisterNo = 0;
        public List<Symbol> symbolTable=new ArrayList<>();
        boolean AllFinish = true;//所有子节点都完成
        boolean IsThisLevel = false;//本层所有结点都完成


        String[][] GrammarForSema = new String[][]{
                new String[] { "program","compoundstmt" },
                        new String[] { "stmt","ifstmt" },
                        new String[] { "stmt","whilestmt" },
                        new String[] { "stmt","assgstmt" },
                        new String[] { "stmt","compoundstmt" },
                        new String[] { "compoundstmt" ,"{", "stmts", "}" },
                        new String[] { "stmts","stmt", "stmts" },
                        new String[] { "stmts","ε" },
                        new String[] { "ifstmt","if", "(", "boolexpr", ")", "then", "stmt", "else", "stmt" },
                        new String[] { "whilestmt","while", "(", "boolexpr", ")", "stmt" },
                        new String[] { "assgstmt","ID", "=", "arithexpr", ";" },
                        new String[] {  "boolexpr","arithexpr", "boolop", "arithexpr" },
                        new String[] { "boolop","<" },
                        new String[] { "boolop",">" },
                        new String[] { "boolop","<=" },
                        new String[] { "boolop",">=" },
                        new String[] { "boolop","==" },
                        new String[] { "arithexpr","multexpr", "arithexprprime" },
                        new String[] { "arithexprprime","+", "multexpr", "arithexprprime" },
                        new String[] { "arithexprprime","-", "multexpr", "arithexprprime" },
                        new String[] { "arithexprprime","ε" },
                        new String[] { "multexpr","simpleexpr", "multexprprime" },
                        new String[] { "multexprprime","*", "simpleexpr", "multexprprime" },
                        new String[] { "multexprprime","/", "simpleexpr", "multexprprime" },
                        new String[] { "multexprprime","ε" },
                        new String[] { "simpleexpr","ID" },
                        new String[] { "simpleexpr","NUM" },
                        new String[] { "simpleexpr","(","arithexpr", ")" }
        };
        String[] T = new String[] //终结符数组
        {
                "{", "}", "if", "(", ")", "then", "else", "while", "ID", "=", ";", "<", ">", "<=", ">=", "==",
                        "+", "-", "*", "/", "NUM", "$"
        };

        String[] NT = new String[]  //非终结符数组
        {
                "program", "stmt", "compoundstmt", "stmts", "ifstmt", "whilestmt", "assgstmt", "boolexpr",
                        "boolop", "arithexpr", "arithexprprime", "multexpr", "multexprprime", "simpleexpr"
        };

        public static void main(String[] args) {
                semantic.ParserTree tree = new ParserTree();
                tree.BuildParserTree(new Token("3",TokenType.Number,1,1,0,1),4,true);
        }

        public void ClearParserTree(){
                ParserTree.clear();//清空语法树
                Level.clear();
                ParserCount = 0;
                RegisterNo = 0;
        }
        public void BuildParserTree(Token tk, int FomularNumber, boolean isroot)//建立抽象语法树
        {
                if (isroot) //建立根节点
                {
                        ParserTree.add(new ParserNode(GrammarForSema[FomularNumber][0], FomularNumber, false, 0));
                        Level.add(new ArrayList<>());
                        Level.get(0).add(0);
                        Plevel = 0;
                }

                Level.add(new ArrayList<>());
                //找到根节点并根据传入的产生式建立子树
                for (int i = 0; i < Level.get(Plevel).size(); i++)//遍历上一次访问的层级
                {
                        int fatherindex = Level.get(Plevel).get(i);
                        IsThisLevel = false;
                        if (ParserTree.get(fatherindex).finish == false)//找出从左到右数第一个未完成的节点,将这个节点作为本次的父节点
                        {
                                ParserTree.get(fatherindex).fomular = FomularNumber;
                                int TempCount = ParserCount;
                                int j = 1;
                                for (j = 1; j < GrammarForSema[FomularNumber].length; j++)//遍历产生式右边，生成孩子节点
                                {
                                        TempCount = TempCount + 1;
                                        //GrammarForSema[FomularNumber][j]是孩子节点的名字
                                        //Plevel + 1 表示孩子节点层级为父亲节点层级加一
                                        ParserTree.add(new ParserNode(GrammarForSema[FomularNumber][j], FomularNumber, false, Plevel + 1));

                                        Level.get(Plevel + 1).add(TempCount); //将这个节点添加到level[][]数组的Plevel+1层的第Lcount[Plevel+1]个
                                        ParserTree.get(fatherindex).child[j - 1] = TempCount;//连接孩子节点
                                        ParserTree.get(TempCount).father = fatherindex;//连接父节点

                                        boolean isT = false; //是否为终结符
                                        for (int n = 0; n < T.length; n++) {
                                                if (ParserTree.get(TempCount).name == T[n]) {
                                                        isT = true;
                                                        break;
                                                }
                                        }

                                        if (isT || ParserTree.get(TempCount).name == "ε") //如果是终结符
                                        {
                                                ParserTree.get(TempCount).finish = true;
                                                ParserTree.get(TempCount).child[0] = -1;//表明它是叶子节点
                                                if ((FomularNumber + 1) == 26)//获取ID或NUM的值
                                                {
                                                        Symbol found = Symbol.IsInSymbolTable(tk.getName(),symbolTable);
                                                        ParserTree.get(TempCount).name = tk.getName();
                                                        //ParserTree.get(TempCount).value = found.attributevalue;
                                                } else if ((FomularNumber + 1) == 27) {
                                                        ParserTree.get(TempCount).name = tk.getName();
                                                        ParserTree.get(TempCount).value = Double.parseDouble(tk.getName());
                                                } else if ((FomularNumber + 1) == 11 && ParserTree.get(TempCount).name == "ID")//赋值语句
                                                {
                                                        ParserTree.get(TempCount).name = tk.getName();
                                                }
                                        }//终结符处理完毕
                                }//孩子节点生成完毕

                                /*if (radioButton1.Checked && tree.IsDisposed == false) {
                                        DrawTree(fatherindex, ParserCount + 1, TempCount);//画出新生成的节点
                                }*/

                                ParserCount = ParserCount + j - 1;//更新ParserCount的值
                                int tempfather = fatherindex;
                                AllFinish = true;
                                while (tempfather != 0)//从下到上递归更新father的finish值
                                {
                                        int m = 1;
                                        while (m < GrammarForSema[ParserTree.get(tempfather).fomular].length) {
                                                if (ParserTree.get(ParserTree.get(tempfather).child[m - 1]).finish == false) {
                                                        AllFinish = false;
                                                        break;
                                                }
                                                m++;
                                        }
                                        if (AllFinish == true)
                                                ParserTree.get(tempfather).finish = true;
                                        else
                                                break;
                                        tempfather = ParserTree.get(tempfather).father;
                                }
                                IsThisLevel = true;//这个产生式在这一层被处理了，不用上移一层
                                Plevel++;//下一次从下一层开始处理
                                break;
                        }//父节点处理完毕
                }//本层节点扫描完毕
                if (IsThisLevel == false)//如果本层所有结点都是完成的，则上移一层处理
                {
                        if (Plevel > 0)//此时不是第0层
                        {
                                Plevel--;//上移一层
                                BuildParserTree(tk, FomularNumber, false);//递归建立子节点
                        } else//此时是第0层，语法树构建完成
                                return;
                }
        }
        public void CalculateParserTree(int root)
        {
                Token oldtk; //用来存上一个token
                boolean HaveUnknowToken = false;
                int fomular = ParserTree.get(root).fomular + 1;
                switch (fomular)
                {
                        case 9://ifstmt -> if ( boolexpr ) then stmt else stmt
                        {
                                CalculateParserTree(ParserTree.get(root).child[2]);
                                if (ParserTree.get(ParserTree.get(root).child[2]).value > 0)
                                        CalculateParserTree(ParserTree.get(root).child[5]);
                                else
                                        CalculateParserTree(ParserTree.get(root).child[7]);
                                break;
                        }
                        case 10://whilestmt -> while ( boolexpr ) stmt
                        {
                                CalculateParserTree(ParserTree.get(root).child[2]);
                                while (ParserTree.get(ParserTree.get(root).child[2]).value > 0)
                                {
                                        CalculateParserTree(ParserTree.get(root).child[4]);
                                        CalculateParserTree(ParserTree.get(root).child[2]);
                                }
                                break;
                        }
                        case 26://simpleexpr -> ID
                        {
                                Symbol found = Symbol.IsInSymbolTable(ParserTree.get(ParserTree.get(root).child[0]).name,symbolTable);
                                if (HaveUnknowToken && found.attributevalue == -9999999)
                                {
                                        HaveUnknowToken = false;
                                }
                                else if (found.attributevalue == -9999999)//error 该标示符未定义
                                {
                                        System.out.println("变量" + found.name + "未赋值，请为变量" + found.name + "赋值后再次编译"+ "错误行数:"+found.linenumbers[0]+"错误位置:"+found.linepositions[0]);
                                }
                                ParserTree.get(root).value = found.attributevalue;

                                //UpdateValue(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).value);
                                break;
                        }
                        case 27://simpleexpr -> NUM
                        {
                                ParserTree.get(root).value = ParserTree.get(ParserTree.get(root).child[0]).value;

                                //UpdateValue(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).value);
                                break;
                        }
                        case 11://assgstmt -> ID = arithexpr ;
                        {
                                CalculateParserTree(ParserTree.get(root).child[2]);
                                ParserTree.get(ParserTree.get(root).child[0]).value = ParserTree.get(ParserTree.get(root).child[2]).value;
                                //UpdateValue_forID(sema, position[ParserTree.get(root).child[0]].x, position[ParserTree.get(root).child[0]].y, ParserTree.get(ParserTree.get(root).child[0]).name, ParserTree.get(ParserTree.get(root).child[0]).value, skyBrush);
                                Symbol found = Symbol.IsInSymbolTable(ParserTree.get(ParserTree.get(root).child[0]).name,symbolTable);
                                found.attributevalue = (int)ParserTree.get(ParserTree.get(root).child[0]).value;
                                break;
                        }
                        case 1://program -> compoundstmt
                        {
                                CalculateParserTree(ParserTree.get(root).child[0]);
                                break;
                        }
                        case 2://stmt -> ifstmt
                        {
                                CalculateParserTree(ParserTree.get(root).child[0]);
                                break;
                        }
                        case 3://stmt -> whilestmt
                        {
                                CalculateParserTree(ParserTree.get(root).child[0]);
                                break;
                        }
                        case 4://stmt -> assgstmt
                        {
                                CalculateParserTree(ParserTree.get(root).child[0]);
                                break;
                        }
                        case 5://stmt -> compoundstmt
                        {
                                CalculateParserTree(ParserTree.get(root).child[0]);
                                break;
                        }
                        case 6://compoundstmt -> { stmts }
                        {
                                CalculateParserTree(ParserTree.get(root).child[1]);
                                break;
                        }
                        case 7://stmts -> stmt stmts
                        {
                                CalculateParserTree(ParserTree.get(root).child[0]);
                                CalculateParserTree(ParserTree.get(root).child[1]);
                                break;
                        }
                        case 8://stmts -> ε
                                break;
                        case 12://boolexpr -> arithexpr boolop arithexpr
                        {
                                CalculateParserTree(ParserTree.get(root).child[0]);
                                CalculateParserTree(ParserTree.get(root).child[1]);
                                CalculateParserTree(ParserTree.get(root).child[2]);
                                if (ParserTree.get(ParserTree.get(root).child[1]).name == ">")
                                {
                                        if (ParserTree.get(ParserTree.get(root).child[0]).value > ParserTree.get(ParserTree.get(root).child[2]).value)
                                                ParserTree.get(root).value = 1;
                                        else
                                                ParserTree.get(root).value = 0;
                                }
                                else if (ParserTree.get(ParserTree.get(root).child[1]).name == ">=")
                                {
                                        if (ParserTree.get(ParserTree.get(root).child[0]).value >= ParserTree.get(ParserTree.get(root).child[2]).value)
                                                ParserTree.get(root).value = 1;
                                        else
                                                ParserTree.get(root).value = 0;
                                }
                                else if (ParserTree.get(ParserTree.get(root).child[1]).name == "==")
                                {
                                        if (ParserTree.get(ParserTree.get(root).child[0]).value == ParserTree.get(ParserTree.get(root).child[2]).value)
                                                ParserTree.get(root).value = 1;
                                        else
                                                ParserTree.get(root).value = 0;
                                }
                                else if (ParserTree.get(ParserTree.get(root).child[1]).name == "<=")
                                {
                                        if (ParserTree.get(ParserTree.get(root).child[0]).value <= ParserTree.get(ParserTree.get(root).child[2]).value)
                                                ParserTree.get(root).value = 1;
                                        else
                                                ParserTree.get(root).value = 0;
                                }
                                else if (ParserTree.get(ParserTree.get(root).child[1]).name == "<")
                                {
                                        if (ParserTree.get(ParserTree.get(root).child[0]).value < ParserTree.get(ParserTree.get(root).child[2]).value)
                                                ParserTree.get(root).value = 1;
                                        else
                                                ParserTree.get(root).value = 0;
                                }
                                //UpdateValue(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).value);
                                break;
                        }
                        case 13://boolop -> <
                        {
                                ParserTree.get(root).name = "<";
                               // Rectangle rect_1 = new Rectangle(position.get(root).x - w / 2, position.get(root).y - h / 2, w, h);
                               // sema.FillRectangle(pinkBrush, rect_1);
                               // WriteNode(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).name, ParserTree.get(root).fomular, ParserTree.get(root).value, blackBrush);
                                break;
                        }
                        case 14://boolop -> >
                        {
                                ParserTree.get(root).name = ">";
                               // Rectangle rect_2 = new Rectangle(position.get(root).x - w / 2, position.get(root).y - h / 2, w, h);
                               // sema.FillRectangle(pinkBrush, rect_2);
                               // WriteNode(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).name, ParserTree.get(root).fomular, ParserTree.get(root).value, blackBrush);
                                break;
                        }
                        case 15://boolop -> <=
                        {
                                ParserTree.get(root).name = "<=";
                               // Rectangle rect_3 = new Rectangle(position.get(root).x - w / 2, position.get(root).y - h / 2, w, h);
                               // sema.FillRectangle(pinkBrush, rect_3);
                               // WriteNode(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).name, ParserTree.get(root).fomular, ParserTree.get(root).value, blackBrush);
                                break;
                        }
                        case 16://boolop -> >=
                        {
                                ParserTree.get(root).name = ">=";
                               // Rectangle rect_4 = new Rectangle(position.get(root).x - w / 2, position.get(root).y - h / 2, w, h);
                               // sema.FillRectangle(pinkBrush, rect_4);
                               // WriteNode(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).name, ParserTree.get(root).fomular, ParserTree.get(root).value, blackBrush);
                                break;
                        }
                        case 17://boolop -> ==
                        {
                                ParserTree.get(root).name = "==";
                               // Rectangle rect_5 = new Rectangle(position.get(root).x - w / 2, position.get(root).y - h / 2, w, h);
                               // sema.FillRectangle(pinkBrush, rect_5);
                              //  WriteNode(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).name, ParserTree.get(root).fomular, ParserTree.get(root).value, blackBrush);
                                break;
                        }
                        case 18://arithexpr -> multexpr arithexprprime
                        {
                                CalculateParserTree(ParserTree.get(root).child[0]);
                                CalculateParserTree(ParserTree.get(root).child[1]);
                                if ((ParserTree.get(ParserTree.get(root).child[1]).fomular + 1) == 21)//arithexprprime为空
                                {
                                        ParserTree.get(root).value = ParserTree.get(ParserTree.get(root).child[0]).value;
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        //把if(19)elseif(20)删掉了
                                        ParserTree.get(root).value = ParserTree.get(ParserTree.get(root).child[0]).value + ParserTree.get(ParserTree.get(root).child[1]).value;
                                }
                              //  UpdateValue(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).value);
                                break;
                        }
                        case 19://arithexprprime -> + multexpr arithexprprime
                        {
                                CalculateParserTree(ParserTree.get(root).child[1]);
                                CalculateParserTree(ParserTree.get(root).child[2]);
                                if ((ParserTree.get(ParserTree.get(root).child[2]).fomular + 1) == 21)//arithexprprime为空
                                {
                                        ParserTree.get(root).value = ParserTree.get(ParserTree.get(root).child[1]).value;
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        //把if(19)elseif(20)删掉了
                                        ParserTree.get(root).value = ParserTree.get(ParserTree.get(root).child[1]).value + ParserTree.get(ParserTree.get(root).child[2]).value;
                                }
                                //UpdateValue(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).value);
                                break;
                        }
                        case 20://arithexprprime -> - multexpr arithexprprime
                        {
                                CalculateParserTree(ParserTree.get(root).child[1]);
                                CalculateParserTree(ParserTree.get(root).child[2]);
                                if ((ParserTree.get(ParserTree.get(root).child[2]).fomular + 1) == 21)//arithexprprime为空
                                {
                                        ParserTree.get(root).value = -ParserTree.get(ParserTree.get(root).child[1]).value;
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        //把if(19)elseif(20)删掉了
                                        ParserTree.get(root).value = -ParserTree.get(ParserTree.get(root).child[1]).value + ParserTree.get(ParserTree.get(root).child[2]).value;
                                }
                               // UpdateValue(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).value);
                                break;
                        }
                        case 21://arithexprprime -> ε
                                break;
                        case 22://multexpr -> simpleexpr multexprprime
                        {
                                CalculateParserTree(ParserTree.get(root).child[0]);
                                CalculateParserTree(ParserTree.get(root).child[1]);
                                if ((ParserTree.get(ParserTree.get(root).child[1]).fomular + 1) == 25)//arithexprprime为空
                                {
                                        ParserTree.get(root).value = ParserTree.get(ParserTree.get(root).child[0]).value;
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        ParserTree.get(root).value = ParserTree.get(ParserTree.get(root).child[0]).value * ParserTree.get(ParserTree.get(root).child[1]).value;
                                }
                              //  UpdateValue(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).value);
                                break;
                        }
                        case 23://multexprprime -> * simpleexpr multexprprime
                        {
                                CalculateParserTree(ParserTree.get(root).child[1]);
                                CalculateParserTree(ParserTree.get(root).child[2]);
                                if ((ParserTree.get(ParserTree.get(root).child[2]).fomular + 1) == 25)//arithexprprime为空
                                {
                                        ParserTree.get(root).value = ParserTree.get(ParserTree.get(root).child[1]).value;
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        ParserTree.get(root).value = ParserTree.get(ParserTree.get(root).child[1]).value * ParserTree.get(ParserTree.get(root).child[2]).value;
                                }
                             //   UpdateValue(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).value);
                                break;
                        }
                        case 24://multexprprime -> / simpleexpr multexprprime
                        {
                                CalculateParserTree(ParserTree.get(root).child[1]);
                                CalculateParserTree(ParserTree.get(root).child[2]);
                                if (ParserTree.get(ParserTree.get(root).child[1]).value == 0)
                                {
                                        System.out.println("被除数不能为0！请修改算式后再次编译");
                                        SemanticHaveError = true;
                                        ParserTree.get(ParserTree.get(root).child[1]).value = 1;
                                }
                                if ((ParserTree.get(ParserTree.get(root).child[2]).fomular + 1) == 25)//arithexprprime为空
                                {
                                        ParserTree.get(root).value = 1 / ParserTree.get(ParserTree.get(root).child[1]).value;
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        ParserTree.get(root).value = 1 / ParserTree.get(ParserTree.get(root).child[1]).value * ParserTree.get(ParserTree.get(root).child[2]).value;
                                }
                                //UpdateValue(sema, position.get(root).x, position.get(root).y, ParserTree.get(root).value);
                                break;
                        }
                        case 25://multexprprime -> ε
                                break;
                        case 28://simpleexpr -> ( arithexpr )
                        {
                                CalculateParserTree(ParserTree.get(root).child[1]);
                                ParserTree.get(root).value = ParserTree.get(ParserTree.get(root).child[1]).value;
                                break;
                        }
                }
                return;
        }

        public void CalculateThreeAddress(int root)
        {
                EndIsEmpty = false;
                int fomular = ParserTree.get(root).fomular + 1;
                switch (fomular)
                {
                        case 9://if ( boolexpr ) then stmt else stmt
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[2]);
                                ParserTree.get(root).label = newLabel();
                                gen(ParserTree.get(root).label, "JMPF    " + ParserTree.get(ParserTree.get(root).child[2]).place + "," + "UNKWON");
                                CalculateThreeAddress(ParserTree.get(root).child[5]);
                                int endIfLabel = newLabel();
                                gen(endIfLabel, "JMP    " + "UNKWON");
                                CalculateThreeAddress(ParserTree.get(root).child[7]);
                                gen(endIfLabel, "JMP    " + ThreeAddress.size());
                                EndIsEmpty = true;
                                gen(ParserTree.get(root).label, "JMPF    " + ParserTree.get(ParserTree.get(root).child[2]).place + "," + ParserTree.get(ParserTree.get(root).child[7]).label);
                                break;
                        }
                        case 10://whilestmt -> while ( boolexpr ) stmt
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[2]);
                                ParserTree.get(root).label = newLabel();
                                gen(ParserTree.get(root).label, "JMPF    " + ParserTree.get(ParserTree.get(root).child[2]).place + "," + "UNKWON");
                                CalculateThreeAddress(ParserTree.get(root).child[4]);
                                gen(newLabel(), "JMP    " + ParserTree.get(ParserTree.get(root).child[2]).label);
                                gen(ParserTree.get(root).label, "JMPF    " + ParserTree.get(ParserTree.get(root).child[2]).place + "," + ThreeAddress.size());
                                EndIsEmpty = true;
                                break;
                        }
                        case 26://simpleexpr -> ID
                        {
                                Symbol found = Symbol.IsInSymbolTable(ParserTree.get(ParserTree.get(root).child[0]).name,symbolTable);
                                ParserTree.get(root).place = found.name;
                                break;
                        }
                        case 27://simpleexpr -> NUM
                        {
                                ParserTree.get(root).place = Double.toString((ParserTree.get(ParserTree.get(root).child[0]).value));
                                break;
                        }
                        case 11://ID = arithexpr ;
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[2]);
                                Symbol found = Symbol.IsInSymbolTable(ParserTree.get(ParserTree.get(root).child[0]).name,symbolTable);
                                ParserTree.get(root).label = newLabel();//新建标号
                                gen(ParserTree.get(root).label, "MOV    " + found.name + "," + ParserTree.get(ParserTree.get(root).child[2]).place);
                                break;
                        }
                        case 1: //program -> compoundstmt
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[0]);
                                ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[0]).place;
                                ParserTree.get(root).label = ParserTree.get(ParserTree.get(root).child[0]).label;
                                break;
                        }
                        case 2: //stmt -> ifstmt
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[0]);
                                ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[0]).place;
                                ParserTree.get(root).label = ParserTree.get(ParserTree.get(root).child[0]).label;
                                break;
                        }
                        case 3: //stmt -> whilestmt
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[0]);
                                ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[0]).place;
                                ParserTree.get(root).label = ParserTree.get(ParserTree.get(root).child[0]).label;
                                break;
                        }
                        case 4: //stmt -> assgstmt
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[0]);
                                ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[0]).place;
                                ParserTree.get(root).label = ParserTree.get(ParserTree.get(root).child[0]).label;
                                break;
                        }
                        case 5: //stmt -> compoundstmt
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[0]);
                                ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[0]).place;
                                ParserTree.get(root).label = ParserTree.get(ParserTree.get(root).child[0]).label;
                                break;
                        }
                        case 6: //compoundstmt -> { stmts }
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[1]);
                                ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[1]).place;
                                ParserTree.get(root).label = ParserTree.get(ParserTree.get(root).child[1]).label;
                                break;
                        }
                        case 7: //stmts -> stmt stmts
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[0]);
                                CalculateThreeAddress(ParserTree.get(root).child[1]);
                                ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[0]).place;
                                ParserTree.get(root).label = ParserTree.get(ParserTree.get(root).child[0]).label;
                                break;
                        }
                        case 8: //stmts -> ε
                                break;
                        case 12: //boolexpr -> arithexpr boolop arithexpr
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[0]);
                                CalculateThreeAddress(ParserTree.get(root).child[1]);
                                CalculateThreeAddress(ParserTree.get(root).child[2]);
                                ParserTree.get(root).label = newLabel();
                                ParserTree.get(root).place = newRegister();
                                gen(ParserTree.get(root).label, ParserTree.get(ParserTree.get(root).child[1]).place + "    " + ParserTree.get(root).place + "," + ParserTree.get(ParserTree.get(root).child[0]).place + "," + ParserTree.get(ParserTree.get(root).child[2]).place);
                                break;
                        }
                        case 13: //boolop -> <
                        {
                                ParserTree.get(root).place = "LT";
                                break;
                        }
                        case 14: //boolop -> >
                        {
                                ParserTree.get(root).place = "GT";
                                break;
                        }
                        case 15: //boolop -> <=
                        {
                                ParserTree.get(root).place = "LE";
                                break;
                        }
                        case 16: //boolop -> >=
                        {
                                ParserTree.get(root).place = "GE";
                                break;
                        }
                        case 17: //boolop -> ==
                        {
                                ParserTree.get(root).place = "EQ";
                                break;
                        }
                        case 18: //arithexpr -> multexpr arithexprprime
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[0]);
                                CalculateThreeAddress(ParserTree.get(root).child[1]);
                                if ((ParserTree.get(ParserTree.get(root).child[1]).fomular + 1) == 21)//arithexprprime为空
                                {
                                        ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[0]).place;//如果为空，则把multexpr的物理地址传入即可
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        ParserTree.get(root).label = newLabel();
                                        ParserTree.get(root).place = newRegister();//给arithexpr一个新的临时变量地址
                                        gen(ParserTree.get(root).label, "ADD    " + ParserTree.get(root).place + "," + ParserTree.get(ParserTree.get(root).child[0]).place + "," + ParserTree.get(ParserTree.get(root).child[1]).place);
                                }
                                break;
                        }
                        case 19: //arithexprprime -> + multexpr arithexprprime
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[1]);
                                CalculateThreeAddress(ParserTree.get(root).child[2]);
                                if ((ParserTree.get(ParserTree.get(root).child[2]).fomular + 1) == 21)//arithexprprime为空
                                {
                                        ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[1]).place;
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        ParserTree.get(root).label = newLabel();
                                        ParserTree.get(root).place = newRegister();//给arithexprprime一个新的临时寄存器
                                        gen(ParserTree.get(root).label, "ADD    " + ParserTree.get(root).place + "," + ParserTree.get(ParserTree.get(root).child[1]).place + "," + ParserTree.get(ParserTree.get(root).child[2]).place);
                                }
                                break;
                        }
                        case 20: //arithexprprime -> - multexpr arithexprprime
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[1]);
                                CalculateThreeAddress(ParserTree.get(root).child[2]);
                                if ((ParserTree.get(ParserTree.get(root).child[2]).fomular + 1) == 21)//arithexprprime为空
                                {
                                        ParserTree.get(root).label = newLabel();
                                        ParserTree.get(root).place = newRegister(); //给arithexprprime一个新的临时寄存器
                                        gen(ParserTree.get(root).label, "UMINUS    " + ParserTree.get(root).place + "," + ParserTree.get(ParserTree.get(root).child[1]).place);
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        ParserTree.get(root).label = newLabel();
                                        ParserTree.get(root).place = newRegister(); //给arithexprprime一个新的临时寄存器
                                        gen(ParserTree.get(root).label, "UMINUS    " + ParserTree.get(root).place + ",," + ParserTree.get(ParserTree.get(root).child[1]).place);
                                        gen(newLabel(), "ADD    " + ParserTree.get(root).place + "," + ParserTree.get(root).place + "," + ParserTree.get(ParserTree.get(root).child[2]).place);
                                }
                                break;
                        }
                        case 21: //arithexprprime -> ε
                                break;
                        case 22: //multexpr -> simpleexpr multexprprime
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[0]);
                                CalculateThreeAddress(ParserTree.get(root).child[1]);
                                if ((ParserTree.get(ParserTree.get(root).child[1]).fomular + 1) == 25)//arithexprprime为空
                                {
                                        ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[0]).place;//如果为空，则把simpleexpr的物理地址传入即可
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        ParserTree.get(root).label = newLabel();
                                        ParserTree.get(root).place = newRegister();//给multexpr一个新的临时变量地址
                                        gen(ParserTree.get(root).label, "MULT    " + ParserTree.get(root).place + "," + ParserTree.get(ParserTree.get(root).child[0]).place + "," + ParserTree.get(ParserTree.get(root).child[1]).place);
                                }
                                break;
                        }
                        case 23: //multexprprime -> * simpleexpr multexprprime
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[1]);
                                CalculateThreeAddress(ParserTree.get(root).child[2]);
                                if ((ParserTree.get(ParserTree.get(root).child[2]).fomular + 1) == 25)//arithexprprime为空
                                {
                                        ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[1]).place;//如果为空，则把simpleexpr的物理地址传入即可
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        ParserTree.get(root).label = newLabel();
                                        ParserTree.get(root).place = newRegister();//给multexpr一个新的临时变量地址
                                        gen(ParserTree.get(root).label, "MULT    " + ParserTree.get(root).place + "," + ParserTree.get(ParserTree.get(root).child[1]).place + "," + ParserTree.get(ParserTree.get(root).child[2]).place);
                                }
                                break;
                        }
                        case 24: //multexprprime -> / simpleexpr multexprprime
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[1]);
                                CalculateThreeAddress(ParserTree.get(root).child[2]);
                                if ((ParserTree.get(ParserTree.get(root).child[2]).fomular + 1) == 25)//arithexprprime为空
                                {
                                        ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[1]).place;//如果为空，则把simpleexpr的物理地址传入即可
                                }
                                else//不为空,根据产生式进行计算
                                {
                                        ParserTree.get(root).label = newLabel();
                                        ParserTree.get(root).place = newRegister(); //给multexprprime一个新的临时寄存器
                                        gen(ParserTree.get(root).label, "DIV    " + ParserTree.get(root).place + ",1," + ParserTree.get(ParserTree.get(root).child[1]).place); //被除数取倒数
                                        gen(newLabel(), "MULT    " + ParserTree.get(root).place + "," + ParserTree.get(root).place + "," + ParserTree.get(ParserTree.get(root).child[2]).place);
                                }
                                break;
                        }
                        case 25: //multexprprime -> ε
                                break;
                        case 28: //simpleexpr -> ( arithexpr )
                        {
                                CalculateThreeAddress(ParserTree.get(root).child[1]);
                                ParserTree.get(root).place = ParserTree.get(ParserTree.get(root).child[1]).place;
                                break;
                        }
                }
                return;
        }
        

        //新建标号
        public int newLabel()
        {
                return ++ThreeAddressCount;
        }

        //增删改三地址数组
        public void gen(int i, String s)
        {
                String labelNo = "";
                if (i < 10)
                {
                        labelNo = "0" + i + ": ";
                }
                else
                {
                        labelNo = i + ": ";
                }

                while (ThreeAddress.size() < i)
                {
                        ThreeAddress.add("");
                }
                ThreeAddress.set(i-1,labelNo + s);
        }

        public String newRegister()
        {
                return "R" + RegisterNo++;
        }
}