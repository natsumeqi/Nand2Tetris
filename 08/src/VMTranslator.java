import java.io.File;

enum CType{
    C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION,
    C_RETURN, C_CALL
}



public class VMTranslator {

    public static void main(String[] args) {

        //String aa =  "StackArithmetic/SimpleAdd/SimpleAdd.vm";
        File f = new File(args[0]);
        if(f.isDirectory()){
            File[] files = f.listFiles(((dir1, name) -> name.endsWith(".vm") ));
            CodeWriter codeWriter = new CodeWriter(args[0]);
            codeWriter.writeInit();
            for(File file: files){
                translateOneFile(file.getAbsolutePath(), codeWriter);
            }
            codeWriter.close();
        }else{
            if(args[0].endsWith(".vm")){
                CodeWriter codeWriter = new CodeWriter(args[0]);
                codeWriter.writeInit();
                translateOneFile(args[0], codeWriter);
                codeWriter.close();

            }
        }
    }
    static void translateOneFile(String file, CodeWriter codeWriter){
        Parser parser = new Parser(file);
        codeWriter.setFileNameForParser(file.substring(file.lastIndexOf("/")+1, file.lastIndexOf(".vm")));
//        CodeWriter codeWriter = new CodeWriter(file);
        CType currentType;
        //String currentLine;
        String currentSegment;
        //String currentCommand;
        int currentIndex;

        while(parser.hasMoreCommands()){
            parser.advance();
            codeWriter.writeComment(parser.getLine());
            if(parser.getLine().trim().startsWith("//")){
                continue;
            }
            currentType = parser.getTypeOfCurrentCommand();
            //currentLine = parser.getLine();
            //System.out.println(currentLine);
            if(currentType == CType.C_RETURN){
                codeWriter.writeReturn();
            }

            currentSegment = parser.getArg1();
            switch (currentType){
                case C_ARITHMETIC:
                    codeWriter.writeArithmetic(currentSegment);
                    break;
                case C_PUSH: case C_POP:
                    currentIndex = parser.getArg2();
                    codeWriter.writePushPop(currentType, currentSegment, currentIndex);
                    break;
                case C_LABEL:
                    codeWriter.writeLabel(currentSegment);
                    break;
                case C_GOTO:
                    codeWriter.writeGoto(currentSegment);
                    break;
                case C_IF:
                    codeWriter.writeIf(currentSegment);
                    break;
                case C_FUNCTION:
                    currentIndex = parser.getArg2();
                    codeWriter.writeFunction(currentSegment, currentIndex);
                    break;
                case C_CALL:
                    currentIndex = parser.getArg2();
                    codeWriter.writeCall(currentSegment, currentIndex);
                    break;



            }
        }

        parser.close();
        //codeWriter.close();

    }
}
