public class InstructionDecoder {
    byte InstructionBuffer = 0;
    public InstructionDecoder(){}

    public byte ALUvalue = 0;
    public boolean statusIn = false;
    public byte Xvalue = 0;
    public byte Yvalue = 0;
    public byte counterValue = 0;
    public byte stackValue = 0;
    public boolean instructionIn = false;
    public boolean halt = false;
    public boolean ReadWrite = false;

    void setInstruction(byte input){
        InstructionBuffer = input;
    }

    void update(){

    }

    int ROM[];
}
