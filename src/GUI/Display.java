package GUI;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import java.util.Scanner;
import java.io.*;

public class Display extends PGraphics {
    /*
    Useful notes:
    Using font 50!!
    - border distance x = 20 from border
    - border distance y = 15 from top
    - interline distance = 35 from each line
    - max characters per line = 30
    - max lines = 16
     */
    boolean dot = false;
    PFont font;
    Scanner fileData;
    PGraphics screen;
    int keyBuffer = 0;
    int keyPointer = 0;
    String[] lastLines = new String[16];
    public Display(PGraphics graphics, PFont font) {
        this.screen = graphics;
        this.font = font;

    }
    //Do processing methods
    public void drawScreen(){
        drawBackground();
        screen.fill(255);
        screen.textFont(font);
        screen.textAlign(LEFT,TOP);
        String[] lines = new String[16];
        try {
            File output = new File("input.txt");
            if (output.createNewFile()) {
                System.out.println("Had no file, made a new one!");
            }

        } catch (Exception ignored) {
        }
        try {
            fileData = new Scanner(new File("screen.txt"));
            for(int i = 0; i < lines.length; i++){
                try{
                    lines[i] = fileData.nextLine();
                } catch (Exception ignored){}
            }
            fileData.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 16; i++) {
            //screen.text("Hello, World! 1234567890123456", (screen.width-560)/2, 115+35*i);
            try {
                screen.text(lines[i], (float) (screen.width - 560) / 2, 115 + 35 * i);
                lastLines[i] = lines[i];
            } catch (Exception e){
                try {
                    screen.text(lastLines[i], (float) (screen.width - 560) / 2, 115 + 35 * i);
                } catch (Exception ignored){}
            }
        }
        if(dot){
            screen.fill(255,255,255);
            screen.rect(220,20,18,30);
        }
        //delay(500);
        //dot = !dot;
    }

    public void addKey(char key){

        if(key == PApplet.BACKSPACE){
            keyBuffer = 0x08;
        } else if(key == PApplet.TAB){
            keyBuffer = 0x09;
        }else if(key == PApplet.DELETE){
            keyBuffer = 0x7F;
        }else if(key == PApplet.ENTER){
            keyBuffer = 0x01;
        }else if(key == PApplet.RETURN){
            keyBuffer = 0x08;
        }else if(key == '\uFFFF'){
            return;
        }else if(key == 0x20){
            keyBuffer = 0x02;
        } else {
            keyBuffer = key;
        }
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt"));
            writer.write(keyBuffer);
            writer.close();
            keyBuffer = 0;
        } catch (Exception ignored){}
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawBackground(){
        screen.background(0,16,255);
        screen.rectMode(CORNER);
        screen.fill(0,0,170);
        screen.noStroke();
        screen.rect(0,0,(screen.width-600)/2,screen.height); //left
        screen.rect(screen.width+35,0,-(screen.width-600)/2,screen.height); //right
        screen.rect(0,0,screen.width,100); //top
        screen.rect(0,700,screen.width,screen.height); //bottom
    }
}
