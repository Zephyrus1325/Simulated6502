package Computer;

import java.util.ArrayList;

public class ROM {
    private final int max;
    private final int min;
    private boolean RW = false;
    private ArrayList<Integer> data = new ArrayList<Integer>();

    public ROM(int address_min, int address_max) {
        this.max = address_max;
        this.min = address_min;
        int size = address_max - address_min;
        for(int i = 0; i <= size; i++){
            if(i < ROM.length) {
                data.add((int)ROM[i]);
            } else {
                data.add((int) 0);
            }
        }
    }

    public int update(int addressBus, int bus){
        int output = bus;
        if(addressBus >= this.min & addressBus <= this.max){
            int localAddress = addressBus - this.min;
            //output data
            if(RW) {
                output = data.get(localAddress);
            }
        }
        return output;
    }
    public void setRead(boolean input){
        this.RW = input;
    }

    int ROM[] = {
            0xA2, 0xff,
            0xCA,
            0xD0, 0x02, 0xf0,
            0xA9, 0xe1,
            0x4C, 0x06, 0xf0,
    };
}
