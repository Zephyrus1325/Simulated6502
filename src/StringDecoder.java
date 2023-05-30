import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
public class StringDecoder {
    private int lineNumber = 1;
    private int beginningAddress = 1;
    private int actualAddress = beginningAddress;
    private ArrayList<String> label = new ArrayList<String>();
    private int[] labelAddress = new int[256];
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
            if (!output.createNewFile()) {
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
        String uppercase = input.toUpperCase();
        String[] line = uppercase.split(" ");
        String start = line[0];
        String highByte = "";
        String lowByte = "";
        String outputLine = "";

        if(start.startsWith(";") || start.isEmpty()) { //if this activate, this means its a commentary line, or a blank one;
            lineNumber++;
            return "";
        } else if(start.endsWith(":")){
            if(firstPass) {
                label.add(start.replace(":", ""));
                labelAddress[label.size() - 1] = actualAddress;
            }
            lineNumber++;
            return "";
        } else if(line[1].startsWith("#") || line[1].startsWith("$")) {
            int number = Integer.parseInt(line[1].substring(1), 16);
            int low = number & (0xff);
            int high = number >> 8;
            if (line[1].startsWith("#") && number > 255) {
                throw new IllegalArgumentException("Value is too big: " + line[1] + " At line " + lineNumber);
            }
            highByte = String.format("%02x", high);
            lowByte = String.format("%02x", low);
        }
        boolean pass = false;
            switch (start) {
//----------------------------------------------------------------------------------------------------------------------
                case "ADC":
                    outputLine = absoluteCode(line, "69", "6D", highByte, lowByte);
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "AND":
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
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "BCS":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            outputLine = ("B0" + ", " + String.format("%02d", lowAddress) + ", " + String.format("%02d", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
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
                            outputLine = ("F0" + ", " + String.format("%02d", lowAddress) + ", " + String.format("%02d", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }
                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);
//----------------------------------------------------------------------------------------------------------------------
                case "BIT":
                    break;
                case "BMI":
                    break;
                case "BNE":
                    break;
                case "BPL":
                    break;
                case "BRA":
                    break;
                case "BRK":
                    break;
                case "BVC":
                    break;
                case "CLC":
                    break;
                case "CLD":
                    break;
                case "CLI":
                    break;
                case "CLV":
                    break;
                case "CMP":
                    break;
                case "CPX":
                    break;
                case "CPY":
                    break;
                case "DEC":
                    break;
                case "DEX":
                    break;
                case "DEY":
                    break;
                case "EOR":
                    break;
                case "INC":
                    break;
                case "INX":
                    break;
                case "INY":
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "JMP":
                    for (int i = 0; i < label.size(); i++) {
                        String name = label.get(i);
                        input = line[1].toUpperCase();
                        if (name.equals(input)) {
                            int lowAddress = labelAddress[i] & (0xff);
                            int highAddress = labelAddress[i] >> 8;
                            outputLine = ("4C" + ", " + String.format("%02d", lowAddress) + ", " + String.format("%02d", highAddress));
                            actualAddress += 3;
                            pass = true;
                            break;
                        }
                    }

                    if(pass | firstPass){
                        break;
                    }
                    throw new IllegalArgumentException("Wrong label: " + line[1] + " At line: " + lineNumber);

//----------------------------------------------------------------------------------------------------------------------
                case "JSR":
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "LDA":
                    outputLine = absoluteCode(line, "A9", "AD", highByte, lowByte);
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "LDX":
                    break;
                case "LDY":
                    break;
                case "LSR":
                    break;
                case "NOP":
                    break;
                case "ORA":
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
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "SBC":
                    outputLine = absoluteCode(line, "E9", "ED", highByte, lowByte);
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
                    outputLine = addressCode(line, "8D", highByte, lowByte);
                    break;
//----------------------------------------------------------------------------------------------------------------------
                case "STP":
                    break;
                case "STX":
                    break;
                case "STY":
                    break;
                case "STZ":
                    break;
                case "TAX":
                    break;
                case "TAY":
                    break;
                case "TRB":
                    break;
                case "TSB":
                    break;
                case "TSX":
                    break;
                case "TXA":
                    break;
                case "TXS":
                    break;
                case "TYA":
                    break;
                case "WAI":
                    break;


                default:
                    throw new IllegalStateException("Unexpected value: " + start + " At line " + lineNumber);
            }
            ;
        lineNumber++;
        System.out.println(outputLine);
        return outputLine.strip(); // +  " | " + actualAddress;
    }

    String absoluteCode(String[] line, String opcodeAbsolute, String opcodeAddress,String highByte, String lowByte){
        ArrayList<String> output = new ArrayList<String>();
        if(line[1].startsWith("#")){
            output.add(hexToString(opcodeAbsolute));
            output.add(lowByte);
            actualAddress += 2;
            return Arrays.toString(output.toArray()).replace("[","").replace("]","").trim();
        } else if(line[1].startsWith("$")){
            output.add(hexToString(opcodeAddress));
            output.add(lowByte);
            output.add(highByte);
            actualAddress += 3;
            return Arrays.toString(output.toArray()).replace("[","").replace("]","").trim();
        } else {
            throw new IllegalStateException("Not a valid number: " + line[1] + " At line " + lineNumber);
        }
    }
    String addressCode(String[] line,String opcodeAbsolute,String highByte, String lowByte){
        if(line[1].startsWith("$")){
            ArrayList<String> output = new ArrayList<String>();
            output.add(hexToString(opcodeAbsolute));
            output.add(lowByte);
            output.add(highByte);
            actualAddress += 3;
            return Arrays.toString(output.toArray()).replace("[","").replace("]","").trim();
        } else {
            throw new IllegalStateException("Not a valid number: " + line[1] + " At line " + lineNumber);
        }
    }

    String hexToString(String hex){
        return(hex);

        //return String.valueOf(Integer.parseInt(hex, 16));
    }


}
