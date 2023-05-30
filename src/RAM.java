public class RAM {
    public int max;
    public int min;
    public byte[]data;
    public RAM(int address_min, int address_max) {
        this.max = address_max;
        this.min = address_min;
    }
    public byte read(int address){
        return data[address];
    }

    public void write(byte value, int address){
        data[address] = value;
    }
}
