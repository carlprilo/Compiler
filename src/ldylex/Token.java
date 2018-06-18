package ldylex;

public class Token {

    private String name;
    private String type;
    private int postion;
    private int row;



    private int begin;
    private int end;

    public Token(String n, String t,int r,int p,int f, int e) {
        this.name = n;
        this.type = t;
        this.row = r;
        this.postion = p;
        this.begin = f;
        this.end = e;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

}
