package compiler;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
public class Compiler {

    public static void main(String[] args){

        try {
            scanCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static void scanCode() throws IOException {
        ArrayList<String> data = new ArrayList<String>();
        Scanner code;

        try{
            code = new Scanner(new File("code.txt"));
            while(code.hasNextLine()){
                String line = code.nextLine();
                if(!Objects.equals(line, "")){
                    System.out.println(line);
                    data.add(line);
                }
            }
            code.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Algo deu Errado");
        }
        StringDecoder decoder = new StringDecoder("$F000", data);
        decoder.startDecode();
    }
        

}


