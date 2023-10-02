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
            0xA2, 0x00,
            0xA9, 0x01,
            0x8D, 0x05, 0x02,
            0xA2, 0x01,
            0xAD, 0x02, 0x02,
            0x6D, 0x05, 0x02,
            0x8D, 0x08, 0x02,
            0xAD, 0x01, 0x02,
            0x6D, 0x04, 0x02,
            0x8D, 0x07, 0x02,
            0xAD, 0x00, 0x02,
            0x6D, 0x03, 0x02,
            0x8D, 0x06, 0x02,
            0xB0, 0x54, 0xf0,
            0xA2, 0x02,
            0xAD, 0x03, 0x02,
            0x8D, 0x00, 0x02,
            0xAD, 0x04, 0x02,
            0x8D, 0x01, 0x02,
            0xAD, 0x05, 0x02,
            0x8D, 0x02, 0x02,
            0xA2, 0x03,
            0xAD, 0x06, 0x02,
            0x8D, 0x03, 0x02,
            0xAD, 0x07, 0x02,
            0x8D, 0x04, 0x02,
            0xAD, 0x08, 0x02,
            0x8D, 0x05, 0x02,
            0xA2, 0x04,
            0x4C, 0x07, 0xf0,
            0x4C, 0x54, 0xf0,











    };
}
