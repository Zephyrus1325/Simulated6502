package Computer;

import java.util.ArrayList;

public class RAM {
    private final int max;
    private final int min;
    private boolean RW = false;
    private ArrayList<Integer> data = new ArrayList<Integer>();

    public RAM(int address_min, int address_max) {
        this.max = address_max;
        this.min = address_min;
        int size = address_max - address_min;
        for(int i = 0; i <= size; i++){
           data.add((int) 0);
        }
    }

    public int update(int addressBus, int bus, boolean clock){
        int output = bus;
        if(addressBus >= this.min & addressBus <= this.max){
            int localAddress = addressBus - this.min;
            //output data
            if(RW){
                output = data.get(localAddress);
            } else if(clock) {
                data.set(localAddress, bus);
            }
        }
        return output;
    }

    public void setRead(boolean input){
        this.RW = input;
    }

    public int getValue(int address){
        int output = 0;
        if(address >= this.min & address <= this.max){
            int localAddress = address - this.min;
            output = data.get(localAddress);
        }
        return output;
    }
}
