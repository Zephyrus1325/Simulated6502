package Computer;

import processing.core.PGraphics;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@SuppressWarnings("PointlessBitwiseExpression")
public class IO {
    boolean RW = false;
    int startAddress;
    int updateCycles = 10;
    int updateCounter = 0;
    int key = 0;
    char[] data = new char[512];
    int pointer = 0;
    int config = 0;
    public IO(int startAddress){
        this.startAddress = startAddress;
        Arrays.fill(data, ' ');
        try {
            File output = new File("screen.txt");
            if (output.createNewFile()) {
                System.out.println("Had no file, made a new one!");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter("screen.txt"));
            for (int i = 0; i < 16; i++) {
                writer.write("                                ");
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int update(int addressBus, int bus, boolean clock){
        int output = bus;
        //Data Write Address
        if(addressBus == this.startAddress) {
            //output data
            if (clock && !RW) {
                if (bus == 0x0A) {
                    pointer += (32 - (pointer % 32));
                    if (pointer >= 512) {
                        pointer -= 512;
                    }
                } else {
                    data[pointer] = (char) bus;
                    pointer++;
                    if (pointer >= 512) {
                        pointer = 0;
                    }
                }
            } else if (RW) {
                //read data on pointer location
                output = data[pointer];
            }
        }
        //Pointer Address
        if(addressBus == this.startAddress+1){
            //output data
            if(RW){
                output = pointer;
            } else if(clock) {
                //change screen pointer
                pointer = bus;
            }
        }
        if(addressBus == this.startAddress+2){
            //input keyboard data
            if(RW) {
                output = key;
                config = config & ~(1<<2);//Deactivate new character flag after reading
            }
        }
        if(addressBus == this.startAddress+3){
            //config Register
            //bit0 clear display [W]
            //bit1 flush buffer  [W]
            //bit2 data available[R]
            //bit3 auto Update   [W]
            //bit4 force Update  [W]
            if(RW){//READING DATA
                output = config & (1<<2);
            } else { //WRITING DATA
                config = ((bus & (1<<0)) | (bus & (1<<1)) | (bus & (1<<3)) | (bus & (1<<4)));
            }
        }
        if((updateCounter > updateCycles) || ((config & (1<<4)) != 0)){
            updateFile();
            updateCounter = 0;
            config = config & ~(1<<4);
        }

        if((config & (1<<0)) == 1){
            Arrays.fill(data, ' ');
            config = config & ~(1<<0);
        }
        if(((config & (1<<3)) == 1)) {
            updateCounter++;
        }
        return output;
    }

    public void setRead(boolean RW){
        this.RW = RW;
    }
    public void updateFile(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("screen.txt"));

            for (int i = 0; i < 16; i++) {
                String line = new String();
                for(int c = 0; c < 32; c++){
                    line = line + data[32*i + c];
                }
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Scanner scanner = new Scanner(new File("input.txt"));
            int input = scanner.next().charAt(0);

            if(input != 0 ) {
                config = config | (1<<2);
                //Special cases
                if(input == 0x01){
                    this.key = 0x0A;
                } else if(input == 0x02){
                    this.key = 0x20;
                }else {
                    this.key = input;
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"));
                writer.write(0);
                writer.write('\n');
                writer.write(this.pointer);
                writer.close();
            }
            scanner.close();
        } catch (Exception ignored){}
    }

}
