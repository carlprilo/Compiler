package parse;

import ldylex.Token;
import semantic.ParserTree;

import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import static ldylex.TokenType.Keyword;

public class TextParse{
    private HashMap<String, String> predictmap;
    private ArrayList<Token> tokenArrayList;
    public  ArrayList<String> deduceArrayList;
    private DefaultTableModel resultModel;
    private DefaultTableModel errorModel;

    private Token preToken;
    private ParserTree parserTree =new ParserTree();

    public TextParse(ArrayList<Token> tokenArrayList, DefaultTableModel resultModel,DefaultTableModel errorModel){
        predictmap = new HashMap<String,String>();
        this.tokenArrayList = tokenArrayList;
        deduceArrayList = new ArrayList<String>();
        this.resultModel = resultModel;
        this.errorModel = errorModel;
        getPredictMap();
    }

    // 句法分析
    public void parsing(){
        // 初始符号压入栈
        deduceArrayList.add("program");
        String right;
        String leftandinput;
        String process="";

        while (deduceArrayList.size()>0 && tokenArrayList.size()>0 ) {

            // 输入缓冲区与推导符号串第一个字符相等的话，删掉
            try {
                if(tokenArrayList.get(0).getType4P().equals(deduceArrayList.get(deduceArrayList.size()-1))){
                    tokenArrayList.remove(0);
                    deduceArrayList.remove(deduceArrayList.size()-1);
                    continue;
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // 匹配字符
            leftandinput = deduceArrayList.get(deduceArrayList.size()-1)+"-"+ tokenArrayList.get(0).getType4P();

            // 能够找到匹配的
            if((right=predictmap.get(leftandinput))!=null){
                // 输出产生式和推导过程
                process = "";
                for (int i = deduceArrayList.size()-1; i>-1; i--) {
                    process = process + deduceArrayList.get(i)+" ";
                }
                resultModel.addRow(new String[]{tokenArrayList.get(0).getRow()+","+ tokenArrayList.get(0).getPostion(),process, deduceArrayList.get(deduceArrayList.size()-1)+" -> "+right});
                // 删掉产生的字符，压入堆栈
                deduceArrayList.remove(deduceArrayList.size()-1);
                if(right.equals("$")){
                    // 只弹不压
                }
                else {
                    String[] arg = right.split(" ");
                    for(int i=arg.length-1;i>-1;i--){
                        // 反向压入堆栈
                        deduceArrayList.add(arg[i]);
                    }
                }
            }
            // 否则的话报错
            else {
                // 重新书写process
                process="";
                for (int i = deduceArrayList.size()-1; i>-1; i--) {
                    process = process+ deduceArrayList.get(i)+" ";
                }
                resultModel.addRow(new String[]{tokenArrayList.get(0).getRow()+","+ tokenArrayList.get(0).getPostion(),process, "ERROR!  无法识别的字符"+ tokenArrayList.get(0).getType4P()+"产生式"+leftandinput});
                tokenArrayList.remove(0);
            }
        }
    }

    public String parsingStep(){
        // 初始符号压入栈
        //deduceArrayList.add("program");
        String right;
        String leftandinput;
        String process="";
        boolean tag = true;

        StringBuffer res = new StringBuffer();

        while(deduceArrayList.size()>0 && tokenArrayList.size()>0 &&tag) {

            // 输入缓冲区与推导符号串第一个字符相等的话，删掉
            try {
                if(tokenArrayList.get(0).getType4P().equals(deduceArrayList.get(deduceArrayList.size()-1))){
                    tokenArrayList.remove(0);
                    deduceArrayList.remove(deduceArrayList.size()-1);
                    continue;
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // 匹配字符
            leftandinput = deduceArrayList.get(deduceArrayList.size()-1)+"-"+ tokenArrayList.get(0).getType4P();

            // 能够找到匹配的
            if((right=predictmap.get(leftandinput))!=null){
                // 输出产生式和推导过程
                process = "";
                for (int i = deduceArrayList.size()-1; i>-1; i--) {
                    process = process+ deduceArrayList.get(i)+" ";
                }
                resultModel.addRow(new String[]{tokenArrayList.get(0).getRow()+","+ tokenArrayList.get(0).getPostion(),process, deduceArrayList.get(deduceArrayList.size()-1)+" -> "+right});
                // 删掉产生的字符，压入堆栈
                res.append(tokenArrayList.get(0).getRow()+","+ tokenArrayList.get(0).getPostion()+" "+process+" "+ deduceArrayList.get(deduceArrayList.size()-1)+" -> "+right).append('\n');
                deduceArrayList.remove(deduceArrayList.size()-1);
                if(right.equals("$")){
                    // 只弹不压
                }
                else {
                    String[] arg = right.split(" ");
                    for(int i=arg.length-1;i>-1;i--){
                        // 反向压入堆栈
                        deduceArrayList.add(arg[i]);
                    }
                }
            }
            // 否则的话报错
            else {
                /*
                // 重新书写process
                process="";
                for (int i=deduceArrayList.size()-1;i>-1;i--) {
                    process = process+deduceArrayList.get(i)+" ";
                }
                resultModel.addRow(new String[]{tokenArrayList.get(0).getRow()+","+tokenArrayList.get(0).getPostion(),process, "ERROR!  无法识别的字符"+tokenArrayList.get(0).getType4P()+"产生式"+leftandinput});
                tokenArrayList.remove(0);
                tag = false;
                */
                {
                    if (deduceArrayList.get(deduceArrayList.size()-1).equals( "=" )) {
                        LOGGER.info( "Error, 不能单独输入 ID, 请删除 多余的ID 或 补全。    " + "\n" );
                        errorModel.addRow(new String[]{tokenArrayList.get(0).getRow()+","+ tokenArrayList.get(0).getPostion(),"Error", "不能单独输入ID,请删除多余的ID或补全."  });
                        //Error error = new Error( "单独输入 ID, 请删除 多余的ID 或 补全", tokenArrayList.get(0).getName()  );
                        //errorList.add( error );
                        //todo 可以这样做么
                        while ((right = predictmap.get( leftandinput )) == null) {
                            deduceArrayList.remove(deduceArrayList.size()-1);
                            //stack.pop();
                            if (deduceArrayList.size() > 0) {
                                leftandinput = deduceArrayList.get(deduceArrayList.size()-1) + "-" + tokenArrayList.get(0).getType4P();
                            } else {
                                break;
                            }
                        }
                        continue;
                    }

                    //todo 笨方法
                    List<String> absenceList = Arrays.asList( ";", "else", "then" );


                    //todo tokenArrayList.get( 0 ) or preToken
                    if (absenceList.contains( deduceArrayList.get(deduceArrayList.size()-1) ) ) {
                        String absenceString = deduceArrayList.get(deduceArrayList.size()-1);
                        LOGGER.info( String.format( "Error, 缺少 %s 符号。    " + "\n", absenceString));
                        LOGGER.info( "Error, 不能单独输入 ID, 请删除 多余的ID 或 补全。    " + "\n" );
                        errorModel.addRow(new String[]{tokenArrayList.get(0).getRow()+","+ tokenArrayList.get(0).getPostion(),"Error"," 不能单独输入ID,请删除多余的ID或补全. "  });

                        //todo 这里应该向 input_cache增加一个 token
                        tokenArrayList.add( 0, new Token( absenceString, Keyword, tokenArrayList.get( 0 ).getRow(), tokenArrayList.get( 0 ).getRow(),0,0) );
//
                        continue;
                    }

                    //确保丢失的分号可以被发现
                    if (deduceArrayList.size() > 0) {
                        String tokenValue = tokenArrayList.get(0).getName();
                        if (deduceArrayList.get(deduceArrayList.size()-1).equals( "arithexprprime" ) && !(tokenValue.equals( "+" ) || tokenValue.equals( "+" ))) {
                            deduceArrayList.remove(deduceArrayList.size()-1);
                            continue;
                        } else if (deduceArrayList.get(deduceArrayList.size()-1).equals( "multexprprime" ) && !(tokenValue.equals( "*" ) || tokenValue.equals( "/" ))) {
                            deduceArrayList.remove(deduceArrayList.size()-1);
                            continue;
                        }
                    }


                //todo 这里应该可以pop ,因为 ; 已经是结束了

                //todo tokenArrayList.get( 0 ) or preToken

                //todo 这里应该向 input_cache增加一个 else token

                    LOGGER.info( "Error,多余 token：    " + leftandinput + "  ,token info: " + tokenArrayList.get( 0 ) + "\n" );
                    errorModel.addRow(new String[]{tokenArrayList.get(0).getRow()+","+ tokenArrayList.get(0).getPostion(),"Error","多余token"});
                    tokenArrayList.remove(0);

                    //todo 调试方便，直接break
                }
            }
        }

        return res.toString();
    }

    // 获得预测分析表中的产生式以及对应的select集
    // 存储方式为键值对的形式
    public void getPredictMap(){
        String text_line;
        String left;
        String symbol;
        String right;
        try {
            // 初始化
            predictmap = new HashMap<String, String>();
            // 采用随机读取方式
            File file = new File("predictldy.txt");
            RandomAccessFile predictfile = new RandomAccessFile(file,"r");
            while ((text_line = predictfile.readLine())!=null){
                left = text_line.split("#")[0];
                symbol = (text_line.split("#")[1]).split("->")[0].trim();
                right = (text_line.split("#")[1]).split("->")[1].trim();
                predictmap.put(left+"-"+symbol, right);
            }
            predictfile.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}