package Computer;

import java.util.ArrayList;

public class ROM {
    private final int max;
    private final int min;
    private boolean RW = false;
    private ArrayList<Integer> code_data = new ArrayList<Integer>();
    private ArrayList<Integer> ROM_data = new ArrayList<Integer>();
    private final int startCode = 0xF000;
    public ROM(int address_min, int address_max) {
        this.max = address_max;
        this.min = address_min;
        //int size = address_max - address_min;
        for(int i = 0; i <= startCode-min; i++){
            if(i < ROM.length) {
                ROM_data.add(ROM[i]);
            } else {
                ROM_data.add(0);
            }
        }
        for(int i = 0; i <= 0x0FFF; i++){
            if(i < Code.length) {
                code_data.add(Code[i]);
            } else {
                code_data.add(0);
            }
        }
    }

    public int update(int addressBus, int bus){
        int output = bus;
        if(addressBus >= this.min && addressBus < this.startCode){
            int localAddress = addressBus - this.min;
            //output data
            if(RW) {
                output = ROM_data.get(localAddress);
            }
        } else if(addressBus >= this.startCode && addressBus <= this.max){
            int localAddress = addressBus - this.startCode;
            //output data
            if(RW) {
                output = code_data.get(localAddress);
            }
        }
        return output;
    }
    public void setRead(boolean input){
        this.RW = input;
    }

    int Code[] = {
            0xA9, 0x0f,
            0x8D, 0x00, 0x02,
            0xA9, 0x08,
            0x8D, 0x03, 0x10,
            0xA9, 0x01,
            0x8D, 0x03, 0x10,
            0xA9, 0x80,
            0x8D, 0x01, 0x10,
            0xA9, 0x00,
            0x8D, 0xff, 0x02,
            0xA9, 0x23,
            0x8D, 0x00, 0x10,
            0xAD, 0xff, 0x02,
            0x1A,
            0x8D, 0xff, 0x02,
            0xCE, 0x00, 0x02,
            0x30, 0x19, 0xf0,
            0xA9, 0x18,
            0x8D, 0x03, 0x10,
            0xAD, 0x03, 0x10,
            0xC9, 0x00,
            0xD0, 0x3b, 0xf0,
            0x4C, 0x0a, 0xf0,
            0xAD, 0x02, 0x10,
            0xC9, 0x61,
            0xF0, 0x55, 0xf0,
            0xC9, 0x64,
            0xF0, 0x4b, 0xf0,
            0x4C, 0x0a, 0xf0,
            0xAD, 0x00, 0x02,
            0x1A,
            0x8D, 0x00, 0x02,
            0x4C, 0x0a, 0xf0,
            0xAD, 0x00, 0x02,
            0x3A,
            0x8D, 0x00, 0x02,
            0x4C, 0x0a, 0xf0,

    };

    int ROM[] = {
            '1',' ','-',' ','I','N','S','E','R','I','R',' ','U','M',' ','N','O','V','O',' ','A','L','U','N','O','\n',
            '2',' ','-',' ','A','L','T','E','R','A','R',' ','U','M',' ','A','L','U','N','O','\n',
            '3',' ','-',' ','E','X','C','L','U','I','R',' ','U','M',' ','A','L','U','N','O','\n',
            '4',' ','-',' ','E','X','C','L','U','I','R',' ','T','O','D','O','S',' ','O','S',' ','A','L','U','N','O','S','\n',
            '5',' ','-',' ','C','O','N','S','U','L','T','A','R',' ','U','M',' ','A','L','U','N','O','\n',
            '6',' ','-',' ','L','I','S','T','A','R',' ','T','O','D','O','S',' ','O','S',' ','A','L','U','N','O','S','\n',
            '7',' ','-',' ','L','I','S','T','A','R',' ','O','S',' ','A','L','U','N','O','S',' ','A','P','R','O','V','A','D','O','S','\n',
            '8',' ','-',' ','L','I','S','T','A','R',' ','O','S',' ','A','L','U','N','O','S',' ','R','E','P','R','O','V','A','D','O','S','\n',
            '9',' ','-',' ','E','X','I','B','I','R',' ','D','A','D','O','S',' ','E','S','T','A','T','Í','S','T','I','C','O','S','\n',
            '0',' ','-',' ','S','A','I','R','\n',
            'S','e','l','e','c','i','o','n','e',':','\n',
            '-',3

    };
}
