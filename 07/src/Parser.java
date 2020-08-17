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
            //System.out.println(line);
        }while(line.contains("//") || line.length() == 0);

        tokenArray = line.split(" ");
        setTypeOfCurrentCommand(commandType());
        setArg1(arg1());
        if(typeOfCurrentCommand == CType.C_PUSH || typeOfCurrentCommand == CType.C_POP ||
        typeOfCurrentCommand == CType.C_FUNCTION || typeOfCurrentCommand == CType.C_CALL)
            setArg2(arg2());
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
        return Integer.parseInt(tokenArray[2]);
    }

    public void close(){
        reader.close();
    }
}
