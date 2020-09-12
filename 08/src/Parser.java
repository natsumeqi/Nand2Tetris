import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
//import static Main.CommandType.*;

public class Parser {

    private Scanner reader;
    private String line;
    private String [] tokenArray;
    private CType typeOfCurrentCommand;
    private String arg1;
    private int arg2;
    //private String filename; //for parser

    public String getLine() {
        return line;
    }

    public CType getTypeOfCurrentCommand() {
        return typeOfCurrentCommand;
    }

    public String getArg1() {
        return arg1;
    }

    public int getArg2() {
        return arg2;
    }


    public void setTypeOfCurrentCommand(CType typeOfCurrentCommand) {
        this.typeOfCurrentCommand = typeOfCurrentCommand;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(int arg2) {
        this.arg2 = arg2;
    }


    public Parser(String arg) {
        File file = new File(arg);
        try {
            reader = new Scanner(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Boolean hasMoreCommands(){
        return reader.hasNextLine();
    }


    public void advance(){
        do {
            line = reader.nextLine();
           // System.out.println(line);
        }while(line.length() == 0);

        if(!line.trim().startsWith("//")){

        if(line.indexOf("//") != -1)
            line = line.substring(0, line.indexOf("//"));
        //System.out.println(line);
        line.trim();
        tokenArray = line.split(" ");
        setTypeOfCurrentCommand(commandType());
        if(typeOfCurrentCommand != CType.C_RETURN)
            setArg1(arg1());
        if(typeOfCurrentCommand == CType.C_PUSH || typeOfCurrentCommand == CType.C_POP ||
        typeOfCurrentCommand == CType.C_FUNCTION || typeOfCurrentCommand == CType.C_CALL)
            setArg2(arg2());
    }
    }

    public CType commandType(){
        switch (tokenArray[0].toLowerCase()){
            case "add": case "sub": case "neg": case "eq": case "gt":
            case "lt": case "and": case "or": case "not":
                return CType.C_ARITHMETIC;
            case "push":
                return CType.C_PUSH;
            case "pop":
                return CType.C_POP;
            case "label":
                return CType.C_LABEL;
            case "goto":
                return CType.C_GOTO;
            case "if-goto":
                return CType.C_IF;
            case "call":
                return CType.C_CALL;
            case "function":
                return CType.C_FUNCTION;
            case "return":
                return CType.C_RETURN;
            default:
                return null;
        }
    }

    public String arg1(){
        if(typeOfCurrentCommand == CType.C_ARITHMETIC){
            return tokenArray[0];
        }else
            return tokenArray[1];
    }

    public int arg2(){
        return Integer.parseInt(tokenArray[2].trim());
    }

    public void close(){
        reader.close();
    }
}
