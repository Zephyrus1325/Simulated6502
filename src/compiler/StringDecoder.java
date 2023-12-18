package compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
public class StringDecoder {

    /*
    todo:




     */



    private int lineNumber = 1;
    private int beginningAddress = 1;
    private int actualAddress = beginningAddress;
    private ArrayList<String> label = new ArrayList<String>();
    private ArrayList<String> variable = new ArrayList<String>();
    private int[] labelAddress = new int[256];
    private String[] variableValue = new String[256];
    private String[] variableValueLow = new String[256];
    private String[] variableValueHigh = new String[256];
    BufferedWriter writer;
    ArrayList<String> data;
    boolean firstPass = true;

    public StringDecoder(int StartAddress){
        beginningAddress = StartAddress;
        actualAddress = beginningAddress;
    }

    public StringDecoder(String StartAddress, ArrayList<String> inputData){
        beginningAddress = Integer.parseInt(StartAddress.replace("$",""), 16);
        actualAddress = beginningAddress;
        data = inputData;
    }

    public void startDecode() throws IOException {
        try {
            File output = new File("output.txt");
            if (output.createNewFile()) {
                System.out.println("Had no file, made a new one!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        for (int i = 0; i < data.size(); i++) {
            String out = data.get(i);
            String outT = decode(out);
        }
        lineNumber = 1;
        actualAddress = beginningAddress;
        firstPass = false;
        for (int i = 0; i < data.size(); i++) {
            String out = data.get(i);
            String outT = decode(out);
            if (!outT.equals("")) {
                writer.write(outT);
                writer.newLine();
            }
        }
        writer.close();
    }



    public String decode(String input){
        String[] line = input.split(" ");
        String start = line[0];
        String highByte = "";
        String lowByte = "";
        String outputLine = "";

        if(start.startsWith(";") || start.isEmpty()) { //if this activate, this means its a commentary line, or a blank one;
            lineNumber++;
            return "";
        } else if(start.endsWith(":")){
            if(firstPass) {
                for(int i = 0; i < line.length; i++){
                    line[i] = line[i].toUpperCase();
                }
                label.add(start.replace(":", "").toUpperCase());
                labelAddress[label.size() - 1] = actualAddress;
            }
            lineNumber++;
            return "";
        } else if(line.length <= 1) {
            lineNumber++;
            //return "";
        } else if(line[1].startsWith("#") || line[1].startsWith("$") ) {
            int number = Integer.parseInt(line[1].substring(1), 16);
            int low = number & (0xff);
            int high = number >> 8;
            if (line[1].startsWith("#") && number > 255) {
                throw new IllegalArgumentException("Value is too big: " + line[1] + " At line " + lineNumber);
            }
            if (line[1].startsWith("$") && number >= 1<<16) {
                throw new IllegalArgumentException("Value is too big: " + line[1] + " At line " + lineNumber);
            }
            highByte = String.format("%02x", high);
            lowByte = String.format("%02x", low);
        } else if(line[1].startsWith("($")){
            int number = Integer.parseInt(line[1].substring(2,6), 16);
            int low = number & (0xff);
            int high = number >> 8;
            if (number >= 1<<16) {
                throw new IllegalArgumentException("Value is too big: " + line[1] + " At line " + lineNumber);
            }
            highByte = String.format("%02x", high);
            lowByte = String.format("%02x", low);
        } else if(line[1].startsWith("'") || line[1].startsWith("\"") ){
            int low = line[1].charAt(1);
            int high = 0;
            highByte = String.format("%02x", high);
            lowByte = String.format("%02x", low);
            line[1] = String.format("#%02x",low);
        }
        for(int i = 0; i < line.length; i++){
            line[i] = line[i].toUpperCase();
        }
        start = line[0];
        //if its not a not a number, it may be a variable
        boolean pass = false;
            switch (line[0]) {
//----------------------------------------------------------------------------------------------------------------------
//Variable declaration
                case "VAR":
                    if(firstPass){
                        variable.add(line[1].toUpperCase()); //add a variable to variables list
                        variableValue[variable.size()-1] = line[2];
                        int number = Integer.parseInt(line[2].substring(1), 16);
                        int low = number & (0xff);
                        int high = number >> 8;
                        if (line[2].startsWith("#") && number > 255) {
                            throw new IllegalArgumentException("Value is too big: " + line[1] + " At line " + lineNumber);
                        }
                        if (line[2].startsWith("$") && number >= 1<<16) {
                            throw new IllegalArgumentException("Value is too big: " + line[1] + " At line " + lineNumber);
                        }
                        variableValueHigh[variable.size()-1] = String.format("%02x", high);
                        variableValueLow[variable.size()-1] = String.format("%02x", low);
                    }
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "ADC":
                    if(line[1].startsWith("(") && line[2].startsWith("X")){
                        outputLine = addressCode(line, "7D", highByte, lowByte);
                    } else if(line[1].startsWith("(") && line[2].startsWith("Y")){
                        outputLine = addressCode(line, "79", highByte, lowByte);
                    } else {
                        outputLine = absoluteCode(line, "69", "6D", highByte, lowByte);
                    }
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "AND":
                    if(line[1].startsWith("(") && line[2].startsWith("X")){
                        outputLine = addressCode(line, "3D", highByte, lowByte);
                    } else if(line[1].startsWith("(") && line[2].startsWith("Y")){
                        outputLine = addressCode(line, "39", highByte, lowByte);
                    } else {
                        outputLine = absoluteCode(line, "29", "2D", highByte, lowByte);
                    }
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "ASL":
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "BBR":
                    break;
                case "BBS":
                    break;
                case "BCC":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            //System.out.println(name);
                            //System.out.println(input);
                            //System.out.printf("%02x%n", labelAddress[i]);
                            //System.out.println("----------------");
                            outputLine = ("0x90" + ", " + "0x" + String.format("%02x", lowAddress) + ", " + "0x" + String.format("%02x", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }
                    if(firstPass & !pass){
                        actualAddress += 3;
                    }
                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);
//----------------------------------------------------------------------------------------------------------------------
                case "BCS":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            //System.out.println(name);
                            //System.out.println(input);
                            //System.out.printf("%02x%n", labelAddress[i]);
                            //System.out.println("----------------");
                            outputLine = ("0xB0" + ", " + "0x" + String.format("%02x", lowAddress) + ", " + "0x" + String.format("%02x", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }
                    if(firstPass & !pass){
                        actualAddress += 3;
                    }
                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);
//----------------------------------------------------------------------------------------------------------------------
                case "BEQ":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            outputLine = ("0xF0" + ", " + "0x" + String.format("%02x", lowAddress) + ", " + "0x" + String.format("%02x", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }
                    if(firstPass & !pass){
                        actualAddress += 3;
                    }
                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);
//----------------------------------------------------------------------------------------------------------------------
                case "BIT":
                    break;
                case "BMI":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            //System.out.println(name);
                            //System.out.println(input);
                            //System.out.printf("%02x%n", labelAddress[i]);
                            //System.out.println("----------------");
                            outputLine = ("0x30" + ", " + "0x" + String.format("%02x", lowAddress) + ", " + "0x" + String.format("%02x", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }
                    if(firstPass & !pass){
                        actualAddress += 3;
                    }
                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);
                case "BNE":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            //System.out.println(name);
                            //System.out.println(input);
                            //System.out.printf("%02x%n", labelAddress[i]);
                            //System.out.println("----------------");
                            outputLine = ("0xD0" + ", " + "0x" + String.format("%02x", lowAddress) + ", " + "0x" + String.format("%02x", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }
                    if(firstPass & !pass){
                        actualAddress += 3;
                    }
                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);
                case "BPL":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            //System.out.println(name);
                            //System.out.println(input);
                            //System.out.printf("%02x%n", labelAddress[i]);
                            //System.out.println("----------------");
                            outputLine = ("0x10" + ", " + "0x" + String.format("%02x", lowAddress) + ", " + "0x" + String.format("%02x", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }
                    if(firstPass & !pass){
                        actualAddress += 3;
                    }
                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);
                case "BRA":
                    break;
                case "BRK":
                    break;
                case "BVC":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            //System.out.println(name);
                            //System.out.println(input);
                            //System.out.printf("%02x%n", labelAddress[i]);
                            //System.out.println("----------------");
                            outputLine = ("0x50" + ", " + "0x" + String.format("%02x", lowAddress) + ", " + "0x" + String.format("%02x", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }
                    if(firstPass & !pass){
                        actualAddress += 3;
                    }
                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);
                case "CLC":
                    if(line.length > 1){
                        throw new IllegalArgumentException("Invalid Argument in a Implicit Instruction: " + line[1] + " At line: " + lineNumber);
                    }
                    outputLine = "0x18";
                    break;
                case "CLD":
                    break;
                case "CLI":
                    break;
                case "CLV":
                    break;
                case "CMP":
                    if(line[1].startsWith("(") && line[2].startsWith("X")){
                        outputLine = addressCode(line, "DD", highByte, lowByte);
                    } else if(line[1].startsWith("(") && line[2].startsWith("Y")){
                        outputLine = addressCode(line, "D9", highByte, lowByte);
                    } else {
                        outputLine = absoluteCode(line, "C9", "CE", highByte, lowByte);
                    }
                    break;
                case "CPX":
                    outputLine = absoluteCode(line, "E0", "EC", highByte, lowByte);
                    break;
                case "CPY":
                    break;
                case "DEC":
                    outputLine = "0x3A";
                    actualAddress += 1;
                    break;
                case "DEX":
                        outputLine = "0xCA";
                        actualAddress += 1;
                    break;
                case "DEY":
                        outputLine = "0x88";
                        actualAddress += 1;
                    break;
                case "EOR":
                    break;
                case "INC":
                    outputLine = "0x1A";
                    actualAddress += 1;
                    break;
                case "INX":
                        outputLine = "0xE8";
                        actualAddress += 1;
                    break;
                case "INY":
                        outputLine = "0xC8";
                        actualAddress += 1;
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "JMP":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            outputLine = ("0x4C" + ", " + "0x" + String.format("%02x", lowAddress) + ", " + "0x" +  String.format("%02x", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }
                    if(firstPass & !pass){
                        actualAddress += 3;
                    }
                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);

//----------------------------------------------------------------------------------------------------------------------
                case "JSR":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            outputLine = ("0x20" + ", " + "0x" + String.format("%02x", lowAddress) + ", " + "0x" +  String.format("%02x", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }
                    if(firstPass & !pass){
                        actualAddress += 3;
                    }
                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);
//----------------------------------------------------------------------------------------------------------------------
                case "LDA":
                    if(line[1].startsWith("(") && line[2].startsWith("X")){
                        outputLine = addressCode(line, "BD", highByte, lowByte);
                    } else if(line[1].startsWith("(") && line[2].startsWith("Y")){
                        outputLine = addressCode(line, "B9", highByte, lowByte);
                    } else {
                        outputLine = absoluteCode(line, "A9", "AD", highByte, lowByte);
                    }
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "LDX":
                    outputLine = absoluteCode(line, "A2", "AE", highByte, lowByte);
                    break;
                case "LDY":
                    outputLine = absoluteCode(line, "A0", "AC", highByte, lowByte);
                    break;
                case "LSR":
                    break;
                case "NOP":
                    outputLine = "0xEA";
                    actualAddress += 1;
                    break;
                case "ORA":
                    if(line[1].startsWith("(") && line[2].startsWith("X")){
                        outputLine = addressCode(line, "1D", highByte, lowByte);
                    } else if(line[1].startsWith("(") && line[2].startsWith("Y")){
                        outputLine = addressCode(line, "19", highByte, lowByte);
                    } else {
                        outputLine = absoluteCode(line, "09", "0D", highByte, lowByte);
                    }
                    break;
                case "PHA":
                    break;
                case "PHP":
                    break;
                case "PHX":
                    break;
                case "PHY":
                    break;
                case "PLA":
                    break;
                case "PLP":
                    break;
                case "RMP":
                    break;
                case "ROL":
                    break;
                case "ROR":
                    break;
                case "RTI":
                    break;
                case "RTS":
                    outputLine = "0x60";
                    actualAddress += 1;
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "SBC":
                    if(line[1].startsWith("(") && line[2].startsWith("X")){
                        outputLine = addressCode(line, "FD", highByte, lowByte);
                    } else if(line[1].startsWith("(") && line[2].startsWith("Y")){
                        outputLine = addressCode(line, "F9", highByte, lowByte);
                    } else {
                        outputLine = absoluteCode(line, "E9", "ED", highByte, lowByte);
                    }
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "SEC":
                    break;
                case "SED":
                    break;
                case "SEI":
                    break;
                case "SMB":
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "STA":
                    if(line[1].startsWith("(") && line[2].startsWith("X")){
                        outputLine = addressCode(line, "9D", highByte, lowByte);
                    } else if(line[1].startsWith("(") && line[2].startsWith("X")) {
                        outputLine = addressCode(line, "99", highByte, lowByte);
                    } else {
                        outputLine = addressCode(line, "8D", highByte, lowByte);
                    }
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "STP":
                    break;
                case "STX":
                        outputLine = addressCode(line, "8E", highByte, lowByte);
                    break;
                case "STY":
                    break;
                case "STZ":
                    break;
                case "TAX":
                    outputLine = "0xAA";
                    break;
                case "TAY":
                    outputLine = "0xA8";
                    break;
                case "TRB":
                    break;
                case "TSB":
                    break;
                case "TSX":
                    outputLine = "0xBA";
                    break;
                case "TXA":
                    outputLine = "0x8A";
                    break;
                case "TXS":
                    outputLine = "0x9A";
                    break;
                case "TYA":
                    outputLine = "0x98";
                    break;
                case "WAI":
                    outputLine = "0xCB";
                    break;


                default:
                    throw new IllegalStateException("Unexpected value: " + start + " At line " + lineNumber);
            }
            ;
        lineNumber++;
        if(!start.equals("VAR")) {
            outputLine = outputLine + ",";
        }
        //System.out.println(outputLine);
        return outputLine.strip(); // +  " | " + actualAddress;
    }

    String absoluteCode(String[] line, String opcodeAbsolute, String opcodeAddress,String highByte, String lowByte){
        ArrayList<String> output = new ArrayList<String>();
        if(line[1].startsWith("#")){
            output.add("0x" + hexToString(opcodeAbsolute));
            output.add("0x" + lowByte);
            actualAddress += 2;
            return Arrays.toString(output.toArray()).replace("[","").replace("]","").trim();
        } else if(line[1].startsWith("$")){
            output.add("0x" + hexToString(opcodeAddress));
            output.add("0x" + lowByte);
            output.add("0x" + highByte);
            actualAddress += 3;
            return Arrays.toString(output.toArray()).replace("[","").replace("]","").trim();
        } else {
            for(int i = 0; i < variable.size(); i++){
                String name = variable.get(i);
                if(name.equals(line[1])){ //if variable name equal to name on line
                    if(variableValue[i].startsWith("#")){
                        output.add("0x" + hexToString(opcodeAbsolute));
                        output.add("0x" + variableValueLow[i]);
                        actualAddress += 2;
                        return Arrays.toString(output.toArray()).replace("[","").replace("]","").trim();
                    } else if(variableValue[i].startsWith("$")){
                        output.add("0x" + hexToString(opcodeAddress));
                        output.add("0x" + variableValueLow[i]);
                        output.add("0x" + variableValueHigh[i]);
                        actualAddress += 3;
                        return Arrays.toString(output.toArray()).replace("[", "").replace("]", "").trim();
                    }
                }
            }
            throw new IllegalStateException("Not a valid number: " + line[1] + " At line " + lineNumber);
        }
    }
    String addressCode(String[] line,String opcodeAbsolute,String highByte, String lowByte){
        if(line[1].startsWith("$") || line[1].startsWith("($")){
            ArrayList<String> output = new ArrayList<String>();
            output.add("0x" + hexToString(opcodeAbsolute));
            output.add("0x" + lowByte);
            output.add("0x" + highByte);
            actualAddress += 3;
            return Arrays.toString(output.toArray()).replace("[","").replace("]","").trim();
        } else if(line[1].startsWith("(")) {
            for(int i = 0; i < variable.size(); i++){
                String name = variable.get(i);
                if(name.equals(line[1].substring(1,line[1].length()-1))){ //if variable name equal to name on line
                    ArrayList<String> output = new ArrayList<String>();
                    output.add("0x" + hexToString(opcodeAbsolute));
                    output.add("0x" + variableValueLow[i]);
                    output.add("0x" + variableValueHigh[i]);
                    actualAddress += 3;
                    return Arrays.toString(output.toArray()).replace("[","").replace("]","").trim();
                }
            }
            throw new IllegalStateException("Not a valid number: " + line[1] + " At line " + lineNumber);
        } else {
            for(int i = 0; i < variable.size(); i++){
                    String name = variable.get(i);
                    if(name.equals(line[1])){ //if variable name equal to name on line
                        ArrayList<String> output = new ArrayList<String>();
                        output.add("0x" + hexToString(opcodeAbsolute));
                        output.add("0x" + variableValueLow[i]);
                        output.add("0x" + variableValueHigh[i]);
                        actualAddress += 3;
                        return Arrays.toString(output.toArray()).replace("[","").replace("]","").trim();
                    }
                }
                throw new IllegalStateException("Not a valid number: " + line[1] + " At line " + lineNumber);
            }
    }

    String hexToString(String hex){
        return(hex);

        //return String.valueOf(Integer.parseInt(hex, 16));
    }


}
