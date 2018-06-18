package parse;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringStack;
import ldylex.Token;

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
    private ArrayList<Token> input_cache;
    public  ArrayList<String> deduce_str;
    private DefaultTableModel tbmodel_lex_result;


    public TextParse(ArrayList<Token> input_cache, DefaultTableModel tbmodel_lex_result){
        predictmap = new HashMap<String,String>();
        this.input_cache = input_cache;
        deduce_str = new ArrayList<String>();
        this.tbmodel_lex_result = tbmodel_lex_result;
        getPredictMap();
    }

    // 句法分析
    public void parsing(){
        // 初始符号压入栈
        deduce_str.add("program");
        String right;
        String leftandinput;
        String process="";

        while (deduce_str.size()>0 && input_cache.size()>0 ) {

            // 输入缓冲区与推导符号串第一个字符相等的话，删掉
            try {
                if(input_cache.get(0).getType4P().equals(deduce_str.get(deduce_str.size()-1))){
                    input_cache.remove(0);
                    deduce_str.remove(deduce_str.size()-1);
                    continue;
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // 匹配字符
            leftandinput = deduce_str.get(deduce_str.size()-1)+"-"+input_cache.get(0).getType4P();

            // 能够找到匹配的
            if((right=predictmap.get(leftandinput))!=null){
                // 输出产生式和推导过程
                process = "";
                for (int i=deduce_str.size()-1;i>-1;i--) {
                    process = process + deduce_str.get(i)+" ";
                }
                tbmodel_lex_result.addRow(new String[]{input_cache.get(0).getRow()+","+input_cache.get(0).getPostion(),process, deduce_str.get(deduce_str.size()-1)+" -> "+right});
                // 删掉产生的字符，压入堆栈
                deduce_str.remove(deduce_str.size()-1);
                if(right.equals("$")){
                    // 只弹不压
                }
                else {
                    String[] arg = right.split(" ");
                    for(int i=arg.length-1;i>-1;i--){
                        // 反向压入堆栈
                        deduce_str.add(arg[i]);
                    }
                }
            }
            // 否则的话报错
            else {
                // 重新书写process
                process="";
                for (int i=deduce_str.size()-1;i>-1;i--) {
                    process = process+deduce_str.get(i)+" ";
                }
                tbmodel_lex_result.addRow(new String[]{input_cache.get(0).getRow()+","+input_cache.get(0).getPostion(),process, "ERROR!  无法识别的字符"+input_cache.get(0).getType4P()+"产生式"+leftandinput});
                input_cache.remove(0);
            }
        }
        //tbmodel_lex_result.addRow(new String[]{});
    }

    public String parsingStep(){
        // 初始符号压入栈
        //deduce_str.add("program");
        String right;
        String leftandinput;
        String process="";
        boolean tag = true;

        StringBuffer res = new StringBuffer();

        while(deduce_str.size()>0 && input_cache.size()>0 &&tag) {

            // 输入缓冲区与推导符号串第一个字符相等的话，删掉
            try {
                if(input_cache.get(0).getType4P().equals(deduce_str.get(deduce_str.size()-1))){
                    input_cache.remove(0);
                    deduce_str.remove(deduce_str.size()-1);
                    continue;
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            // 匹配字符
            leftandinput = deduce_str.get(deduce_str.size()-1)+"-"+input_cache.get(0).getType4P();

            // 能够找到匹配的
            if((right=predictmap.get(leftandinput))!=null){
                // 输出产生式和推导过程
                process = "";
                for (int i=deduce_str.size()-1;i>-1;i--) {
                    process = process+deduce_str.get(i)+" ";
                }
                tbmodel_lex_result.addRow(new String[]{input_cache.get(0).getRow()+","+input_cache.get(0).getPostion(),process, deduce_str.get(deduce_str.size()-1)+" -> "+right});
                // 删掉产生的字符，压入堆栈
                res.append(input_cache.get(0).getRow()+","+input_cache.get(0).getPostion()+" "+process+" "+deduce_str.get(deduce_str.size()-1)+" -> "+right).append('\n');
                deduce_str.remove(deduce_str.size()-1);
                if(right.equals("$")){
                    // 只弹不压
                }
                else {
                    String[] arg = right.split(" ");
                    for(int i=arg.length-1;i>-1;i--){
                        // 反向压入堆栈
                        deduce_str.add(arg[i]);
                    }
                }
            }
            // 否则的话报错
            else {
                /*
                // 重新书写process
                process="";
                for (int i=deduce_str.size()-1;i>-1;i--) {
                    process = process+deduce_str.get(i)+" ";
                }
                tbmodel_lex_result.addRow(new String[]{input_cache.get(0).getRow()+","+input_cache.get(0).getPostion(),process, "ERROR!  无法识别的字符"+input_cache.get(0).getType4P()+"产生式"+leftandinput});
                input_cache.remove(0);
                tag = false;
                */
                {

//                input_cache.get( 0 ).getTokenDetailType().equals( deduce_str.get(deduce.size()-1) );
//                (right = predictMap.get( leftandinput )) != null
                    if (deduce_str.get(deduce_str.size()-1).equals( "=" )) {
                        LOGGER.info( "Error, 不能单独输入 ID, 请删除 多余的ID 或 补全。    " + "\n" );
                        tbmodel_lex_result.addRow(new String[]{input_cache.get(0).getRow()+","+input_cache.get(0).getPostion(),process,"Error, 不能单独输入 ID, 请删除 多余的ID 或 补全。    "  });
                        //Error error = new Error( "单独输入 ID, 请删除 多余的ID 或 补全", input_cache.get(0).getName()  );
                        //errorList.add( error );
                        //todo 可以这样做么
                        while ((right = predictmap.get( leftandinput )) == null) {
                            deduce_str.remove(deduce_str.size()-1);
                            //stack.pop();
                            if (deduce_str.size() > 0) {
                                leftandinput = deduce_str.get(deduce_str.size()-1) + "-" + input_cache.get(0).getType4P();
                            } else {
                                break;
                            }
                        }
                        continue;
                    }

                    //todo 笨方法
                    List<String> absenceList = Arrays.asList( ";", "else", "then" );


                    //todo input_cache.get( 0 ) or preToken
                    if (absenceList.contains( deduce_str.get(deduce_str.size()-1) ) ) {
                        String absenceString = deduce_str.get(deduce_str.size()-1);
                        LOGGER.info( String.format( "Error, 缺少 %s 符号。    " + "\n", absenceString));
//                        Error error = new Error( String.format( "缺少 %s 符号。", absenceString), input_cache.get( 0 ) );
//                        errorList.add( error );
                        LOGGER.info( "Error, 不能单独输入 ID, 请删除 多余的ID 或 补全。    " + "\n" );
                        tbmodel_lex_result.addRow(new String[]{input_cache.get(0).getRow()+","+input_cache.get(0).getPostion(),process,"Error, 不能单独输入 ID, 请删除 多余的ID 或 补全。    "  });

                        //todo 这里应该向 input_cache增加一个 token
                        input_cache.add( 0, new Token( absenceString, Keyword, input_cache.get( 0 ).getRow(), input_cache.get( 0 ).getRow(),0,0) );
//                    stack.pop();

//                    while ((right = predictMap.get( leftandinput )) == null) {
//                        stack.pop();
//                        leftandinput = deduce_str.get(deduce.size()-1) + "-" + input_cache.get( 0 ).getTokenDetailType();
//                    }
                        continue;
                    }

                    //确保丢失的分号可以被发现
                    if (deduce_str.size() > 0) {
                        String tokenValue = input_cache.get(0).getName();
                        if (deduce_str.get(deduce_str.size()-1).equals( "arithexprprime" ) && !(tokenValue.equals( "+" ) || tokenValue.equals( "+" ))) {
                            deduce_str.remove(deduce_str.size()-1);
                            continue;
                        } else if (deduce_str.get(deduce_str.size()-1).equals( "multexprprime" ) && !(tokenValue.equals( "*" ) || tokenValue.equals( "/" ))) {
                            deduce_str.remove(deduce_str.size()-1);
                            continue;
                        }
                    }

//                if (deduce_str.get(deduce.size()-1).equals( ";" )) {
//                    LOGGER.info( "Error, 缺少 ; 符号, 请删除语句 或 补全 ;。    " + "\n" );
//                    Error error = new Error( "缺少 ; 符号, 请删除语句 或 补全 ;。", preToken );
//                    errorList.add( error );
//
//                    //todo 这里应该可以pop ,因为 ; 已经是结束了
//                    stack.pop();
////                    while ((right = predictMap.get( leftandinput )) == null) {
////                        stack.pop();
////                        leftandinput = deduce_str.get(deduce.size()-1) + "-" + input_cache.get( 0 ).getTokenDetailType();
////                    }
//                    continue;
//                }
//
//                //todo input_cache.get( 0 ) or preToken
//                if (deduce_str.get(deduce.size()-1).equals( "else" )) {
//                    LOGGER.info( "Error, 缺少 else 符号。    " + "\n" );
//                    Error error = new Error( "Error, 缺少 else 符号。", input_cache.get( 0 ) );
//                    errorList.add( error );
//
//                    //todo 这里应该向 input_cache增加一个 else token
//                    input_cache.add( 0, new Token( "else", Keyword, input_cache.get( 0 ).getTokenLine(), input_cache.get( 0 ).getTokenLine() ) );
////                    stack.pop();
//
////                    while ((right = predictMap.get( leftandinput )) == null) {
////                        stack.pop();
////                        leftandinput = deduce_str.get(deduce.size()-1) + "-" + input_cache.get( 0 ).getTokenDetailType();
////                    }
//                    continue;
//                }


//                LOGGER.info( "Error, 不存在该表项, 恐慌模式将删除该token：    " + leftandinput + "  ,token info: " + input_cache.get( 0 ) + "\n" );
//                Error error = new Error( "Error, 不存在该表项, 恐慌模式将删除该token", input_cache.get( 0 ) );
                    LOGGER.info( "Error,多余 token：    " + leftandinput + "  ,token info: " + input_cache.get( 0 ) + "\n" );
                    tbmodel_lex_result.addRow(new String[]{input_cache.get(0).getRow()+","+input_cache.get(0).getPostion(),"Error","多余token"});
                    input_cache.remove(0);
                    //Error error = new Error( "多余 token", input_cache.get( 0 ) );

                    //errorList.add( error );
                    //恐慌模式
                   // preToken = input_cache.remove( 0 );
                    //todo 调试方便，直接break
//                break;


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