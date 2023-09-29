package GUI;

import processing.core.*;

public class processingMain extends PApplet {
    Display display;
    Computer_GUI computer_GUI;
    PFont screenFont;
    PFont dataFont;
    //Start everything
    public void settings(){
        size(1400,800); //intended use for 1400x800
    }
    //Do processing methods
    public void setup(){
        screenFont = createFont("src/MozartNbp-93Ey.ttf", 50);
        dataFont = createFont("src/Courier_New.ttf",50);
        display = new Display(this.g, screenFont);
        computer_GUI = new Computer_GUI(this.g, dataFont);
        surface.setResizable(true);
        background(0);
        surface.setTitle("6502 Simulator");
    }
    public void draw(){
        //display.drawScreen();
        computer_GUI.drawDiagram();
    }

    public static void main(String args[]){
        PApplet.main("GUI.processingMain");
    }

}
