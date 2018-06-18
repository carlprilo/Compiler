package semantic;

import ldylex.TokenType;

import java.util.List;

public class Symbol
{
    public String name;
    public TokenType tokentype;
    public int attributevalue;
    public int[] linenumbers;
    public int[] linepositions;
    public int count;
    //Symbol[] SymbolTable = new Symbol[100];//符号表，暂定最多100项


    public Symbol()
    {
        name = "";
        tokentype = TokenType.Identifier;
        attributevalue = -9999999;
        linenumbers = new int[100];
        linepositions = new int[100];
        count = 0;
    }

    public Symbol(String idname, int initialline, int initialcolumn)
    {
        name = idname;
        tokentype = TokenType.Identifier;
        attributevalue = -9999999;
        linenumbers = new int[100];
        linepositions = new int[100];
        linenumbers[0] = initialline;
        linepositions[0] = initialcolumn;
        count = 1;
    }
    public static Symbol IsInSymbolTable(String str, List<Symbol>SymbolTable)
    {
        int i = 0;
        Symbol found = null;
        for (Symbol item : SymbolTable)
        {
            i++;
            if (str.equals(item.name.toString()))
            {
                found = item;
                break;
            }
        }
        return found;
    }
    public static boolean IsInSymbolTableb(String str, List<Symbol>SymbolTable)
    {
        int i = 0;
        for (Symbol item : SymbolTable)
        {
            i++;
            if (str.equals(item.name.toString()))
            {
                return true;
            }
        }
        return false;
    }
}