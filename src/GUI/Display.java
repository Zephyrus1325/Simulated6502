package GUI;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.ArrayList;

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
    ArrayList<String> lines = new ArrayList<String>();
    PGraphics screen;

    public Display(PGraphics graphics, PFont font){
        this.screen = graphics;
        this.font = font;
    }
    //Do processing methods
    public void drawScreen(){
        drawBackground();
        screen.fill(255);
        screen.textFont(font);
        screen.textAlign(LEFT,TOP);
        for(int i = 0; i < 16; i++) {
            screen.text("Hello, World! 1234567890123456", 420, 115+35*i);
        }
        if(dot){
            screen.fill(255,255,255);
            screen.rect(220,20,18,30);
        }
        //delay(500);
        //dot = !dot;

    }

    void drawText(){

    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private void drawBackground(){
        screen.background(0,16,255);
        screen.rectMode(CORNER);
        screen.fill(0,0,170);
        screen.noStroke();
        screen.rect(0,0,(screen.width-600)/2,screen.height); //left
        screen.rect(screen.width,0,-(screen.width-600)/2,screen.height); //right
        screen.rect(0,0,screen.width,screen.height/8); //top
        screen.rect(0,7*screen.height/8,screen.width,screen.height); //bottom
    }
}
