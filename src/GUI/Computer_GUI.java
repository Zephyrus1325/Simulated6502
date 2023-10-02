package GUI;

import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

public class Computer_GUI {
    int partInfo = 0; //more info at checkClickable()'s switch-case
    PGraphics screen;
    PFont font;
    private float height;
    private float width;
    public Computer_GUI(PGraphics graphics, PFont font){
        this.screen = graphics;
        this.font = font;

    }
    public void drawDiagram(){
        this.height = screen.height;
        this.width = screen.width;
        drawBackground();

    }
    private void drawBackground(){
        screen.background(180);
        screen.stroke(0);
        screen.strokeWeight(2);
        screen.fill(222,228,255);
        screen.rectMode(PConstants.CORNER);
        screen.rect(width/16,height/32,width/32,30*height/32); //Address Bus
        screen.rect(2*width/6,height/32,width/32,30*height/32); //Data Bus
        screen.rect(5*width/32,height/16,width/9,height/12); //X
        screen.rect(5*width/32,height/16+1*height/10,width/9,height/12); //Y
        screen.rect(5*width/32,height/16+2*height/10,width/9,height/12); //Stack
        screen.rect(5*width/32,height/16+3*height/10,width/9,height/12); //PCH
        screen.rect(5*width/32,height/16+4*height/10,width/9,height/12); //PCL
        screen.rect(5*width/32,height/16+5*height/10,width/9,height/12); //DLH
        screen.rect(5*width/32,height/16+6*height/10,width/9,height/12); //DLL
        screen.rect(5*width/32,height/16+7*height/10,width/9,height/12); //RAM
        screen.rect(5*width/32,height/16+8*height/10,width/9,height/12); //ROM
        screen.rect(14*width/32,height/16+2*height/10,width/9,height/12); //A
        screen.rect(14*width/32,height/16+3*height/10,width/9,height/12); //ALU
        screen.rect(14*width/32,height/16+4*height/10,width/9,height/12); //B
        screen.rect(14*width/32,height/16+5*height/10,width/9,height/12); //STATUS
        screen.rect(14*width/32,height/16+6*height/10,width/9,height/12); //INSTRUCTION
        screen.rect(19*width/32,height/16+5*height/10,width/9,height/5); //DECODER

        screen.textSize(width/150);
        screen.textAlign(PConstants.CENTER,PConstants.TOP);
        screen.fill(0);
        screen.text("X REGISTER",5*width/32,height/16,width/9,height/12); //X
        screen.text("Y REGISTER",5*width/32,height/16+1*height/10,width/9,height/12); //Y
        screen.text("STACK",5*width/32,height/16+2*height/10,width/9,height/12); //Stack
        screen.text("PROGRAM COUNTER HIGH",5*width/32,height/16+3*height/10,width/9,height/12); //PCH
        screen.text("PROGRAM COUNTER LOW",5*width/32,height/16+4*height/10,width/9,height/12); //PCL
        screen.text("ADDRESS LATCH HIGH",5*width/32,height/16+5*height/10,width/9,height/12); //DLH
        screen.text("ADDRESS LATCH LOW",5*width/32,height/16+6*height/10,width/9,height/12); //DLL
        screen.text("RANDOM ACCESS MEMORY",5*width/32,height/16+7*height/10,width/9,height/12); //RAM
        screen.text("READ-ONLY MEMORY",5*width/32,height/16+8*height/10,width/9,height/12); //ROM
        screen.text("ACCUMULATOR",14*width/32,height/16+2*height/10,width/9,height/12); //A
        screen.text("ARITHMETIC LOGIC UNIT",14*width/32,height/16+3*height/10,width/9,height/12); //ALU
        screen.text("B REGISTER",14*width/32,height/16+4*height/10,width/9,height/12); //B
        screen.text("STATUS REGISTER",14*width/32,height/16+5*height/10,width/9,height/12); //Status
        screen.text("INSTRUCTION REGISTER",14*width/32,height/16+6*height/10,width/9,height/12); //INSTRUCTION
        screen.text("INSTRUCTION DECODER",19*width/32,height/16+5*height/10,width/9,height/5); //DECODER

        screen.textAlign(PConstants.CENTER,PConstants.CENTER);
        screen.text("00",5*width/32,height/16,width/9,height/12); //X
        screen.text("00",5*width/32,height/16+1*height/10,width/9,height/12); //Y
        screen.text("01 00",5*width/32,height/16+2*height/10,width/9,height/12); //Stack
        screen.text("00",5*width/32,height/16+3*height/10,width/9,height/12); //PCH
        screen.text("00",5*width/32,height/16+4*height/10,width/9,height/12); //PCL
        screen.text("00",5*width/32,height/16+5*height/10,width/9,height/12); //DLH
        screen.text("00",5*width/32,height/16+6*height/10,width/9,height/12); //DLL
        screen.text("0000 00",5*width/32,height/16+7*height/10,width/9,height/12); //RAM
        screen.text("0000 00",5*width/32,height/16+8*height/10,width/9,height/12); //ROM
        screen.text("00",14*width/32,height/16+2*height/10,width/9,height/12); //A
        screen.text("00",14*width/32,height/16+3*height/10,width/9,height/12); //ALU
        screen.text("00",14*width/32,height/16+4*height/10,width/9,height/12); //B
        screen.text("00",14*width/32,height/16+5*height/10,width/9,height/12); //Status
        screen.text("00",14*width/32,height/16+6*height/10,width/9,height/12); //INSTRUCTION
        screen.text("00 1",19*width/32,height/16+5*height/10,width/9,height/5); //DECODER

        screen.parent.pushMatrix();
        screen.parent.translate(width/16,height/32+30*height/32);
        screen.parent.rotate(-PConstants.HALF_PI);
        screen.text("0000",0,0,30*height/32,width/32);//Address bus Text
        screen.text("0000",0,69*width/256,30*height/32,width/32); //Data bus Text
        screen.textAlign(PConstants.LEFT,PConstants.CENTER);
        screen.text("Address",0,0,30*height/32,width/32);//Address bus Text
        screen.text("Data",0,69*width/256,30*height/32,width/32); //Data bus Text
        screen.textAlign(PConstants.RIGHT,PConstants.CENTER);
        screen.text("Address",0,0,30*height/32,width/32);//Address bus Text
        screen.text("Data",0,69*width/256,30*height/32,width/32); //Data bus Text
        screen.parent.popMatrix();

        screen.parent.cursor(PConstants.ARROW);
        checkClickable(5*width/32,height/16,width/9,height/12,1); //X
        checkClickable(5*width/32,height/16+1*height/10,width/9,height/12,2); //Y
        checkClickable(5*width/32,height/16+2*height/10,width/9,height/12,3); //Stack
        checkClickable(5*width/32,height/16+3*height/10,width/9,height/12,4); //PCH
        checkClickable(5*width/32,height/16+4*height/10,width/9,height/12,5); //PCL
        checkClickable(5*width/32,height/16+5*height/10,width/9,height/12,6); //DLH
        checkClickable(5*width/32,height/16+6*height/10,width/9,height/12,7); //DLL
        checkClickable(5*width/32,height/16+7*height/10,width/9,height/12,8); //RAM
        checkClickable(5*width/32,height/16+8*height/10,width/9,height/12,9); //ROM

        checkClickable(14*width/32,height/16+2*height/10,width/9,height/12,10); //A
        checkClickable(14*width/32,height/16+3*height/10,width/9,height/12,11); //ALU
        checkClickable(14*width/32,height/16+4*height/10,width/9,height/12,12); //B
        checkClickable(14*width/32,height/16+5*height/10,width/9,height/12,13); //STATUS
        checkClickable(14*width/32,height/16+6*height/10,width/9,height/12,14); //INSTRUCTION
        checkClickable(19*width/32,height/16+5*height/10,width/9,height/5,15); //DECODER


        //Right Side Bar
        screen.fill(120);
        screen.rect(3*width/4, 0,width/4,height);

        switch(partInfo){
            case 1:
                infoX(); //X
                break;
            case 2:
                //; //Y
                break;
            case 3:
               //Stack
                break;
            case 4:
               //PCH
                break;
            case 5:
                 //PCL
                break;
            case 6:
                 //DLH
                break;
            case 7:
                 //DLL
                break;
            case 8:
                 //RAM
                break;
            case 9:
                 //ROM
                break;
            case 10:
                 //A
                break;
            case 11:
                 //ALu
                break;
            case 12:
                 //B
                break;
            case 13:
                 //STATUS
                break;
            case 14:
                 //INSTRUCTION
                break;
            case 15:
                //DECODER
                break;
        }
    }

    private void checkClickable(float x, float y, float width, float height, int function){
        if(screen.parent.mouseX > x && screen.parent.mouseX < x+width && screen.parent.mouseY > y && screen.parent.mouseY < y+height){
                screen.parent.cursor(PConstants.HAND);
                if(screen.parent.mousePressed){
                    partInfo = function;
                }
            }

    }
    private void infoX(){
        screen.textAlign(PConstants.CENTER,PConstants.CENTER);
        screen.fill(0);
        screen.textSize(width/50);
        screen.text("X REGISTER",12*width/16,height/5,4*width/16,height/16);
        screen.strokeWeight(5);
        screen.stroke(0);
        screen.fill(0,255,0); //READING INPUT
        screen.rect(25*width/32,12*height/27,1*width/52,height/27);
        screen.fill(255,0,0); //OUTPUTTING
        screen.rect(25*width/32,14*height/27,1*width/52,height/27);
    }
}
