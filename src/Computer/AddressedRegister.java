package Computer;

public class AddressedRegister {
    private int valueLow = 0;
    private int valueHigh = 0;
    private int value = 0;
    private boolean inputLow = false;
    private boolean inputHigh = false;
    private boolean outputLow = false;
    private boolean outputHigh = false;
    private boolean outputAddress = false;
    public AddressedRegister(){}


    public int update(int bus, boolean clock){
        int busValue = bus;
        if(inputLow && !clock){
            valueLow = busValue;
        }
        if(inputHigh && !clock){
            valueHigh = busValue;
        }
        if(outputLow && !clock){
            busValue = valueLow;
        }
        if(outputHigh && !clock){
            busValue = valueHigh;
        }
        return busValue;
    }

    public int addressUpdate(int addressBus, boolean clock){
        int busValue = addressBus;
        this.value = (valueHigh<<8)+valueLow;
        if(outputAddress){// && !clock){
            busValue = value;
        }
        return busValue;
    }

        public void setInputLow(boolean input){
            this.inputLow = input;
        }
        public void setInputHigh(boolean input){
         this.inputHigh = input;
     }
        public void setOutputLow(boolean input){
            this.outputLow = input;
        }
    public void setOutputHigh(boolean input){
        this.outputHigh = input;
    }
        public void setOutputAddress(boolean input){
            this.outputAddress = input;
        }
        public int getLowValue(){return this.valueLow;}
        public int getHighValue(){return this.valueHigh;}
}
