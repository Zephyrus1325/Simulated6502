package Computer;

public class Stack {
    private int value = 0;
    private int valueOutput = 0;
    private boolean input = false;
    private boolean output = false;
    private boolean outputAddress = false;
    private boolean enable = false;
    private boolean down = true;
    public Stack(){}

    public int update(int bus, boolean clock){
        int busValue = bus;
        if(enable && clock){
            if(this.down){
                value--;
            } else {
                value++;
            }
        }
        if(input && !clock){
            value = busValue;
        }
        if(output && !clock){
            busValue = value;
        }

        return busValue;
    }

    public int addressUpdate(int addressBus, boolean clock){
        int busValue = addressBus;



        if(outputAddress){ // && !clock
            valueOutput = (1<<8)|value;
            busValue = valueOutput;
        }
        return busValue;
    }

    public void setInput(boolean input){
        this.input = input;
    }
    public void setOutput(boolean input){
        this.output = input;
    }

    public void setOutputAddress(boolean input){
        this.outputAddress = input;
    }

    public void setCountEnable(boolean input){
        this.enable = input;

    }
    public int getValue(){
        return this.value;
    }

    public void setDown(boolean input){
        this.down = input;
    }
}
