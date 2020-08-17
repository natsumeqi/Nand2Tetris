enum CType{
    C_ARITHMETIC, C_PUSH, C_POP, C_LABEL, C_GOTO, C_IF, C_FUNCTION,
    C_RETURN, C_CALL
}



public class VMTranslator {

    public static void main(String[] args) {

        //String aa =  "StackArithmetic/SimpleAdd/SimpleAdd.vm";
        Parser parser = new Parser(args[0]);
        CodeWriter codeWriter = new CodeWriter(args[0]);
        CType currentType;
        String currentLine;
        String currentSegment;
        int currentIndex;

        while(parser.hasMoreCommands()){
            parser.advance();
            currentType = parser.getTypeOfCurrentCommand();
            currentLine = parser.getLine();
            //System.out.println(currentLine);
            if(currentType == CType.C_ARITHMETIC){
                codeWriter.writeArithmetic(currentLine);
            }else
                if(currentType == CType.C_PUSH || currentType == CType.C_POP){
                    currentSegment = parser.getArg1();
                    currentIndex = parser.getArg2();
                    codeWriter.writePushPop(currentType, currentSegment, currentIndex);
                }

        }

        parser.close();
        codeWriter.close();


    }
}
