package ldylex;

import java.util.Arrays;
import java.util.List;

public class Token {

    private String name;
    private TokenType type;
    private int postion;
    private int row;



    private int begin;
    private int end;

    public Token(String n, TokenType t,int r,int p,int f, int e) {
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

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
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

    public String getType4P(){
        if (this.type == TokenType.Delimiter||this.type==TokenType.Operator ) {
            return this.getName();
        }

        if (this.type == TokenType.Identifier) {
            return "ID";
        }

        if (this.type == TokenType.Number) {
            return "NUM";
            //对于给定的语法，只有int 和 real 两种常量
        }
        if (this.type == TokenType.Comment)
            return "COMMENT";
        if (this.type == TokenType.Keyword)
            return  this.getName();

        return  this.getName();
    }

    public static String getTokenDetailType(Token token) {

        if (token.type == TokenType.Delimiter||token.type==TokenType.Operator ) {
            return token.getName();
        }

        if (token.type == TokenType.Identifier) {
            return "ID";
        }

        if (token.type == TokenType.Number) {
            return "NUM";
            //对于给定的语法，只有int 和 real 两种常量
        }
        if (token.type == TokenType.Comment)
            return "COMMENT";
        if (token.type == TokenType.Keyword)
            return  "KEY";

        return token.getName();
    }

}
