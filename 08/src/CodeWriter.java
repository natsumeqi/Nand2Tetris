
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {

    private FileWriter writer;
    static private int numOfIf = 0;
    static private int numOfAddr = 0;
    static private int numOfRe = 0;
    static private String fileName;
    private String fileNameForParser;
    private String functionNameAll;


    //Informs the code writer that the translation of a new VM ﬁle is started
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileNameForParser(String fileNameForParser) {
        this.fileNameForParser = fileNameForParser;
    }

    //Opens the output file/stream and gets ready to write into it
    public CodeWriter(String arg) {
        String outFile;
        if(arg.endsWith(".vm")) {
            outFile = arg.substring(0, arg.length() - 3) + ".asm";
            setFileName(arg.substring(arg.lastIndexOf("/") + 1, arg.length() - 3));
        }else{
            setFileName(arg.substring(arg.lastIndexOf("/") + 1));
            outFile = arg + "/" + fileName + ".asm";

        }
        try {
            File file = new File(outFile);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            writer = new FileWriter(outFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Writes VM source lines as comments in the ASM output
    public void writeComment(String line){
        line = "    // " + line + "\n";
        try {
            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Writes to the output file the assembly code that implements the given arithmetic command
    public void writeArithmetic(String command) {
        //String operator = command.trim();
        String asmCode = "";

        String labelT = "IFTRUE" + numOfIf;
        String labelF = "IFFALSE" + numOfIf;

        switch (command) {
            case "add":
                asmCode = "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=D+M\nM=D\n@SP\nM=M+1\n";
                break;
            case "sub":
                asmCode = "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\nM=D\n@SP\nM=M+1\n";
                break;
            case "eq":
                asmCode = "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\n@" + labelT + "\n" +
                        "D;JEQ\n@SP\nA=M\nM=0\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelT + ")\n@SP\nA=M\nM=-1\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelF + ")\n";
                numOfIf++;
                break;
            case "gt":
                asmCode = "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\n@" + labelT + "\n" +
                        "D;JGT\n@SP\nA=M\nM=0\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelT + ")\n@SP\nA=M\nM=-1\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelF + ")\n";
                numOfIf++;
                break;
            case "lt":
                asmCode = "@SP\nM=M-1\nA=M\nD=M\n@SP\nM=M-1\nA=M\nD=M-D\n@" + labelT + "\n" +
                        "D;JLT\n@SP\nA=M\nM=0\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelT + ")\n@SP\nA=M\nM=-1\n@SP\nM=M+1\n@" + labelF + "\n0;JMP\n(" + labelF + ")\n";
                numOfIf++;
                break;
            case "and":
                asmCode = "@SP\nM=M-1\n@SP\nA=M\nD=M\n@SP\nM=M-1\nA=M\nM=D&M\n@SP\nM=M+1\n";
                break;
            case "or":
                asmCode = "@SP\nM=M-1\n@SP\nA=M\nD=M\n@SP\nM=M-1\nA=M\nM=D|M\n@SP\nM=M+1\n";
                break;
            case "neg":
                asmCode = "@SP\nM=M-1\n@SP\nA=M\nD=M\nM=-D\n@SP\nM=M+1\n";
                break;
            case "not":
                asmCode = "@SP\nM=M-1\n@SP\nA=M\nD=M\nM=!D\n@SP\nM=M+1\n";
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
    public void writePushPop(CType command, String segment, int index) {
        segment = segment.toLowerCase();
        String asmCode = "";
        String varAddr = "addr" + numOfAddr;

        //PUSH
        if (command == CType.C_PUSH) {
            switch (segment) {
                case "constant":
                    asmCode = "@" + index + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    break;
                case "local":
                    asmCode = "@" + index + "\nD=A\n@LCL\nD=D+M\n@" + varAddr + "\nM=D\n" +
                            "A=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    numOfAddr++;
                    break;
                case "argument":
                    asmCode = "@" + index + "\nD=A\n@ARG\nD=D+M\n@" + varAddr + "\nM=D\n" +
                            "A=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    numOfAddr++;
                    break;
                case "this":
                    asmCode = "@" + index + "\nD=A\n@THIS\nD=D+M\n@" + varAddr + "\nM=D\n" +
                            "A=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    numOfAddr++;
                    break;
                case "that":
                    asmCode = "@" + index + "\nD=A\n@THAT\nD=D+M\n@" + varAddr + "\nM=D\n" +
                            "A=M\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    numOfAddr++;
                    break;
                case "temp":
                    asmCode = "@" + index + "\nD=A\n@5\nD=A+D\n@D\nD=M\n" +
                            "@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    numOfAddr++;
                    break;
                case "pointer":
                    if (index == 0) {
                        asmCode = "@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    } else if (index == 1) {
                        asmCode = "@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    }
                    break;
                case "static":
                    asmCode = "@" + fileNameForParser.substring((fileNameForParser.lastIndexOf("/")) + 1) + "." + index + "\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
                    break;
                default:
                    break;
            }
        } else { //POP
            switch (segment) {
                case "local":
                    asmCode = "@" + index + "\nD=A\n@LCL\nD=D+M\n@" + varAddr + "\nM=D\n@SP\nM=M-1\n" +
                            "A=M\nD=M\n@" + varAddr + "\nA=M\nM=D\n";
                    numOfAddr++;
                    break;
                case "argument":
                    asmCode = "@" + index + "\nD=A\n@ARG\nD=D+M\n@" + varAddr + "\nM=D\n@SP\nM=M-1\n" +
                            "A=M\nD=M\n@" + varAddr + "\nA=M\nM=D\n";
                    numOfAddr++;
                    break;
                case "this":
                    asmCode = "@" + index + "\nD=A\n@THIS\nD=D+M\n@" + varAddr + "\nM=D\n@SP\nM=M-1\n" +
                            "A=M\nD=M\n@" + varAddr + "\nA=M\nM=D\n";
                    numOfAddr++;
                    break;
                case "that":
                    asmCode = "@" + index + "\nD=A\n@THAT\nD=D+M\n@" + varAddr + "\nM=D\n@SP\nM=M-1\n" +
                            "A=M\nD=M\n@" + varAddr + "\nA=M\nM=D\n";
                    numOfAddr++;
                    break;
                case "temp":
                    asmCode = "@" + index + "\nD=A\n@5\nD=A+D\n@" + varAddr + "\nM=D\n@SP\nM=M-1\n" +
                            "A=M\nD=M\n@" + varAddr + "\nA=M\nM=D\n";
                    numOfAddr++;
                    break;
                case "pointer":
                    if (index == 0) {
                        asmCode = "@SP\nM=M-1\n@SP\nA=M\nD=M\n@THIS\nM=D\n";
                    } else if (index == 1) {
                        asmCode = "@SP\nM=M-1\n@SP\nA=M\nD=M\n@THAT\nM=D\n";
                    }
                    break;
                case "static":
                    asmCode = "@SP\nM=M-1\n@SP\nA=M\nD=M\n@" + fileNameForParser.substring((fileNameForParser.lastIndexOf("/")) + 1) + "." + index + "\nM=D\n";
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

    //Writes assembly code that effects the VM initialization, also called bootstrap code
    public void writeInit() {
        String asmCode = "@256\nD=A\n@SP\nM=D\n";
        try {
            writer.write(asmCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeCall("Sys.init", 0);
    }

    //Writes assembly code that effects the label command
    public void writeLabel(String label) {
        String functionNameLabel = functionNameAll + "$" + label;
        String asmCode = "(" + functionNameLabel + ")\n";
        try {
            writer.write(asmCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Writes assembly code that effects the goto command
    public void writeGoto(String label) {
        String functionNameLabel = functionNameAll + "$" + label;
        String asmCode = "@" + functionNameLabel + "\n0;JMP\n";
        try {
            writer.write(asmCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Writes assembly code that effects the if-goto command
    public void writeIf(String label) {
        String functionNameLabel = functionNameAll + "$" + label;
        String asmCode = "@SP\nM=M-1\n@SP\nA=M\nD=M\n@" + functionNameLabel + "\nD;JNE\n";
        try {
            writer.write(asmCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Writes assembly code that effects the call command
    public void writeCall(String functionName, int numArgs) {
      String retAddrLabel = fileName + "." + functionName + "$ret" + numOfRe;
      String functionNameToCall = fileName + "." + functionName;
      String asmCode = "@" + retAddrLabel + "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n" + // Using a translator-generated label
                        "@LCL\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n" +  // Saves LCL of the caller
                        "@ARG\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n" +  // Saves ARG of the caller
                        "@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n" +  // Saves THIS of the caller
                        "@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n" +  // Saves THAT of the caller
                        "@SP\nD=M\n@5\nD=D-A\n@" + numArgs + "\nD=D-A\n@ARG\nM=D\n" + // Repositions ARG
                        "@SP\nD=M\n@LCL\nM=D\n" + // Repositions LCL
                        "@" + functionNameToCall + "\n0;JMP\n" + //goto functionName
                        "(" + retAddrLabel + ")\n";  // the same translator-generated label
        numOfRe++;
        try {
            writer.write(asmCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Writes assembly code that effects the function command
    public void writeFunction(String functionName, int numLocals) {
        functionNameAll =  fileName + "." + functionName;
        String asmCode = "(" + functionNameAll + ")\n";
        while(numLocals != 0){
            asmCode += "@SP\nA=M\nM=0\n@SP\nM=M+1\n";
            numLocals--;
        }
        try {
            writer.write(asmCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Writes assembly code that effects the return command
    public void writeReturn() {
        String retAddr = "retAddr" + numOfRe;
        String endFrame = "endFrame" + numOfRe;
        String asmCode = "@LCL\nD=M\n@" + endFrame + "\nM=D\n@5\nD=A\n@" + endFrame + "\nD=M-D\nA=D\nD=M\n" +
                "@" + retAddr + "\nM=D\n@SP\nM=M-1\nA=M\nD=M\n@ARG\nA=M\nM=D\n" +       //*ARG=pop()
                "@ARG\nD=M+1\n@SP\nM=D\n"   + // SP=ARG+1
                "@" + endFrame + "\nD=M-1\nA=D\nD=M\n@THAT\nM=D\n" + //THAT=*(endFrame-1)
                "@2\nD=A\n@" + endFrame + "\nD=M-D\nA=D\nD=M\n@THIS\nM=D\n" +
                "@3\nD=A\n@" + endFrame + "\nD=M-D\nA=D\nD=M\n@ARG\nM=D\n" +
                "@4\nD=A\n@" + endFrame + "\nD=M-D\nA=D\nD=M\n@LCL\nM=D\n" +
                "@" + retAddr + "\nA=M\n0;JMP\n";
        //numOfRe++;
        try {
            writer.write(asmCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Closes the output file
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
