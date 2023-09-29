package Computer;

public class Register {
    public int value = 0;
    private boolean input = false;
    private boolean output = false;

    public Register(){}

    public int update(int bus, boolean clock){
        int busValue = bus;
        if(input && !clock){
            value = busValue;
        }
        if(output && !clock){
            busValue = value;
        }
        return busValue;
    }

    public void setInput(boolean input){
        this.input = input;
    }

    public void setOutput(boolean input){
        this.output = input;
    }

    public int getValue(){return this.value;}

    public void setValue(int input){this.value = input;}
}
