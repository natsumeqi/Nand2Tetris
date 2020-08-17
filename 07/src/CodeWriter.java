
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

    private FileWriter writer;
    static private int numOfIf = 0;
    static private int numOfAddr = 0;
    static private String fileName;

    //Opens the output file/stream and gets ready to write into it
    public CodeWriter(String arg) {
        fileName = arg.substring(0, arg.length() - 3);
        String outFile = fileName + ".asm";
        try {
            File file = new File(outFile);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            writer = new FileWriter(outFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Writes to the output file the assembly code that implements the given arithmetic command
    public void writeArithmetic (String command){
        String operator = command.trim();
        String asmCode = "// " + operator + "\n";

        String labelT = "IFTRUE" + numOfIf;
        String labelF = "IFFALSE" + numOfIf;

                switch (operator){
                    case "add":
                        asmCode += "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=D+M\nM=D\n@SP\nM=M+1\n";
                        break;
                    case "sub":
                        asmCode += "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\nM=D\n@SP\nM=M+1\n";
                        break;
                    case "eq":
                        asmCode += "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\n@" + labelT + "\n" +
                                "D;JEQ\n@SP\nA=M\nM=0\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelT + ")\n@SP\nA=M\nM=-1\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelF + ")\n";
                        numOfIf++;
                        break;
                    case "gt":
                        asmCode += "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\n@" + labelT + "\n" +
                                "D;JGT\n@SP\nA=M\nM=0\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelT + ")\n@SP\nA=M\nM=-1\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelF + ")\n";
                        numOfIf++;
                        break;
                    case "lt":
                        asmCode += "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\n@" + labelT + "\n" +
                                "D;JLT\n@SP\nA=M\nM=0\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelT + ")\n@SP\nA=M\nM=-1\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelF + ")\n";
                        numOfIf++;
                        break;
                    case "and":
                        asmCode += "@SP\nM=M-1\n@SP\nA=M\nD=M\n@SP\nM=M-1\nA=M\nM=D&M\n@SP\nM=M+1\n";
                        break;
                    case "or":
                        asmCode += "@SP\nM=M-1\n@SP\nA=M\nD=M\n@SP\nM=M-1\nA=M\nM=D|M\n@SP\nM=M+1\n";
                        break;
                    case "neg":
                        asmCode += "@SP\nM=M-1\n@SP\nA=M\nD=M\nM=-D\n@SP\nM=M+1\n";
                        break;
                    case "not":
                        asmCode += "@SP\nM=M-1\n@SP\nA=M\nD=M\nM=!D\n@SP\nM=M+1\n";
                        break;
                    default:
                        break;

            }
        try {
            writer.write(asmCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Writes to the output file the assembly code that implements C_PUSH or C_POP
    public void writePushPop(CType command, String segment, int index){
        segment = segment.toLowerCase();
        String asmCode = "//" + command.name() + " " + segment + " " + index + "\n";
        String varAddr = "addr" + numOfAddr;

        //PUSH
        if(command == CType.C_PUSH){
            switch (segment){
                case "constant":
                    asmCode +=  "@" + index + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    break;
                case "local":
                    asmCode += "@" + index + "\nD=A\n@LCL\nD=D+M\n@" + varAddr + "\nM=D\n" +
                            "A=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                    numOfAddr++;
                    break;
                case "argument":
                    asmCode += "@" + index + "\nD=A\n@ARG\nD=D+M\n@" + varAddr + "\nM=D\n" +
                            "A=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                    numOfAddr++;
                    break;
                case "this":
                    asmCode += "@" + index + "\nD=A\n@THIS\nD=D+M\n@" + varAddr + "\nM=D\n" +
                            "A=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                    numOfAddr++;
                    break;
                case "that":
                    asmCode += "@" + index + "\nD=A\n@THAT\nD=D+M\n@" + varAddr + "\nM=D\n" +
                            "A=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                    numOfAddr++;
                    break;
                case "temp":
                    asmCode += "@" + index + "\nD=A\n@5\nD=A+D\nD=D+M\n@" + varAddr + "\nM=D\n" +
                            "A=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
                    numOfAddr++;
                    break;
                case "pointer":
                    if(index == 0){
                        asmCode += "@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    }else
                        if(index == 1){
                            asmCode += "@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                        }
                    break;
                case "static":
                    asmCode += "@" + fileName.substring((fileName.lastIndexOf("/"))+1) + "." + index + "\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    break;
                default:
                    break;
            }
        }
        else{ //POP
            switch (segment){
            case "local":
                asmCode += "@" + index + "\nD=A\n@LCL\nD=D+M\n@" + varAddr + "\nM=D\n@SP\nM=M-1\n" +
                        "A=M\nD=M\n@" + varAddr + "\nA=M\nM=D\n";
                numOfAddr++;
                break;
            case "argument":
                asmCode += "@" + index + "\nD=A\n@ARG\nD=D+M\n@" + varAddr + "\nM=D\n@SP\nM=M-1\n" +
                        "A=M\nD=M\n@" + varAddr + "\nA=M\nM=D\n";
                numOfAddr++;
                break;
            case "this":
                asmCode += "@" + index + "\nD=A\n@THIS\nD=D+M\n@" + varAddr + "\nM=D\n@SP\nM=M-1\n" +
                        "A=M\nD=M\n@" + varAddr + "\nA=M\nM=D\n";
                numOfAddr++;
                break;
            case "that":
                asmCode += "@" + index + "\nD=A\n@THAT\nD=D+M\n@" + varAddr + "\nM=D\n@SP\nM=M-1\n" +
                        "A=M\nD=M\n@" + varAddr + "\nA=M\nM=D\n";
                numOfAddr++;
                break;
                case "temp":
                    asmCode += "@" + index + "\nD=A\n@5\nD=A+D\nD=D+M\n@" + varAddr + "\nM=D\n@SP\nM=M-1\n" +
                            "A=M\nD=M\n@" + varAddr + "\nA=M\nM=D\n";
                    numOfAddr++;
                    break;
                case "pointer":
                    if(index == 0){
                        asmCode += "@SP\nM=M-1\n@SP\nA=M\nD=M\n@THIS\nM=D\n";
                    }else
                    if(index == 1){
                        asmCode += "@SP\nM=M-1\n@SP\nA=M\nD=M\n@THAT\nM=D\n";
                    }
                    break;
                case "static":
                    asmCode += "@SP\nM=M-1\n@SP\nA=M\nD=M\n@" + fileName.substring((fileName.lastIndexOf("/"))+1)+ "." + index + "\nM=D\n";
                    break;
                default:
                    break;
            }
        }
        try {
            writer.write(asmCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Closes the output file
    public void close(){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
