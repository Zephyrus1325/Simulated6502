package GUI;
import java.io.*;
import java.util.HashMap;
//_-------------------------------------------POR FAVOR NÂO USA ESSE ARQUIVO SEU BOCO-------------------------------
@SuppressWarnings("CallToPrintStackTrace")
public class Console {
    private final int min;
    private final int max;

    public int  charPerLine;
    public int  totalLines;

    char[][] characters;
    int[] intBuffer = new int[16];
    boolean RW = false;
    private int config = 0;
    public Console(int chars, int lines, int minAddress) {
        this.min = minAddress;
        this.max = minAddress + (chars*lines) + 2;

        charPerLine = chars;
        totalLines = lines;
        characters = new char[totalLines][charPerLine];
        for (int i = 0; i < totalLines; i++) {
            for (int j = 0; j < charPerLine; j++) {
                characters[i][j] = ' ';
            }
        }
        outputConfigs();
        outputChars();
    }
//--------------------------------JA FALEI PRA NÃO EDITAR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!---------------------------
    public int update(int addressBus, int bus, boolean clock){
        int output = bus;
        if(addressBus >= this.min & addressBus <= this.max - 2){
            //output data
            if(RW){
                output = characters[(addressBus-min)/totalLines][(addressBus-min)%charPerLine];
            } else if(clock) {
                characters[(addressBus-min)/totalLines][(addressBus-min)%charPerLine] = (char) bus;
                outputChars();
            }
        } else if(addressBus == this.max - 1) {
            //keyboard buffer
            if(RW){//IF READING
                output = intBuffer[0];
            }
        } else if(addressBus == this.max){
            //config Register
            //bit0 clear display [W]
            //bit1 flush buffer  [W]
            //bit2 data available[R]
            if(RW){//READING DATA
                output = config & (1<<2);
            } else { //WRITING DATA
                //noinspection PointlessBitwiseExpression
                config = (bus & (1<<0)) | (bus & (1<<1));
            }


            }
        return output;
    }


    public void setRead(boolean input){
        this.RW = input;
    }

    private void outputConfigs() {
        try {
            File output = new File("consoleConfigs.txt");
            if (output.createNewFile()) {
                System.out.println("Had no file, made a new one!");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter("consoleConfigs.txt"));
            writer.write(this.charPerLine);
            writer.newLine();
            writer.write(this.totalLines);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//-------------------AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    private void outputChars(){
        try {
            File output = new File("text.txt.tmp");
            File file = new File("text.txt");
            if (output.createNewFile()) {
                System.out.println("Had no file, made a new one!");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter("text.txt.tmp"));
            for(int i = 0; i < this.totalLines; i++){
                writer.write(characters[i]);
                if(i != this.totalLines-1){writer.newLine();}
            }
            writer.close();
            output.renameTo(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
