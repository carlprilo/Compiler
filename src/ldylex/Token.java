package ldylex;

public class Token {

    private String name;
    private String type;
    private int postion;
    private int row;

    public Token(String n, String t,int r,int p) {
        this.name = n;
        this.type = t;
        this.row = r;
        this.postion = p;
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
}
