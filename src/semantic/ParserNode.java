package semantic;

public class ParserNode {
    public int[] child;//孩子节点标号
    public int father;//父亲节点标号
    public boolean finish;//以此节点为父节点的子树是否完成
    public String name;//符号名字（终结符或非终结符)
    public int fomular;//节点的展开式
    public int level;//节点所在树的层级
    public double value;//节点的非终结符的值
    public String place; //三地址要用到的，表示变量的物理地址
    public int label; //三地址要用到的，表示语句的标号
    public int ParserCount = 0;//语法树节点编号

    public ParserNode(String n, int f, boolean fin, int l)
    {
        child = new int[8];
        father = -1;
        finish = fin;
        name = n;
        fomular = f;
        level = l;
        value = -9999999;
        place = "";
        label = -1;
    }
}
