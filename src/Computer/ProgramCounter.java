package Computer;

public class ProgramCounter {
    private int counter = 0;
    private boolean loadLow;
    private boolean loadHigh;
    private boolean outputLow;
    private boolean outputHigh;
    private boolean countEnable;
    private boolean outputAddress;

    private boolean counterLow = false;
    private boolean counterHigh = false;
    private boolean counterEnable = false;
    private boolean counterOutput = false;


    /*
    0000 - Nothing
    0001 - Output to bus
    0010 - Enable Count on Clock UP
    0011 - Enable Count and Output to bus
    0100 - Input high int from data bus
    0101 - Output High int to data Bus
    0110 - input high int and count up
    0111 - Output high int and count up
    1000 - Input low int from data bus
    1001 - Output low int from data bus
    1010 - Input low int and count
    1011 - Output low int and count
    1100 - DONT YOU DARE
    1101 - DONT YOU DARE
    1110 - DONT YOU DARE
    1111 - DONT YOU DARE

     */
    public ProgramCounter(int startValue){
        counter = startValue;
    }

    public int update(int bus, boolean clock){
        updateSignals();
        int output = bus;
        if(clock && countEnable){
            this.counter++;
            if(counter > 65536){
                counter = 0;
            }
        }
        if(outputLow){
            output = (int)(counter & 0xFF);
        }
        if(outputHigh){
            output = (int)(counter >> 8);
        }
        if(loadLow){
            int highValue = counter & 0xff00;
            counter = bus;
            counter += highValue;
        }
        if(loadHigh){
            int lowValue = counter & 0x00ff;
            counter = bus << 8;
            counter += lowValue;
        }
        return output;
    }

    public int addressUpdate(int addressBus){
        updateSignals();
        int output = addressBus;
        if(outputAddress){
            output = counter;
        }
        return output;
    }

    private void updateSignals(){
        loadLow = counterLow & !counterOutput;
        loadHigh = counterHigh & !counterOutput;
        outputLow = counterLow & counterOutput;
        outputHigh = counterHigh & counterOutput;
        outputAddress = !counterLow & !counterHigh & counterOutput;
        countEnable = counterEnable;
    }

    public void setCounterLow(boolean input){
        this.counterLow = input;
    }
    public void setCounterHigh(boolean input){
        this.counterHigh = input;
    }
    public void setCounterEnable(boolean input){
        this.counterEnable = input;
    }
    public void setCounterOutput(boolean input){
        this.counterOutput = input;
    }
    public int getCounter() {return this.counter;}

}
