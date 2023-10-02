package Computer;/*
    Loading Order:

    Clock = 0
    Change address and RW pins
    read data
    store data
    Clock = 1
    write data
    readData
    updateDataPins
 */


import static java.lang.Thread.sleep;

public class main_class {

    public static void main(String[] args) throws InterruptedException {
        int counter = 0;
        //Initialize Classes
        Register A = new Register();
        Register B = new Register();
        Register statusRegister = new Register();
        statusRegister.setValue(0x30);
        ALU ALU = new ALU();
        ProgramCounter programCounter = new ProgramCounter(0xf000);
        InstructionDecoder decoder = new InstructionDecoder();
        Stack stack = new Stack();
        AddressedRegister X_Y = new AddressedRegister();
        AddressedRegister latch = new AddressedRegister();
        RAM RAM = new RAM(0x0000,0x0fff);
        ROM ROM = new ROM(0xf000,0xffff);
        boolean clock = false;
        int dataBus = 0;
        int addressBus = 0;
        int control = 0;
        boolean readWrite = false;
        //State Constants
        int AI  = (1<<0);
        int AO  = (1<<1);
        int BI  = (1<<2);
        int RS  = (1<<3);
        int XI  = (1<<4);
        int XO  = (1<<5);
        int YI  = (1<<6);
        int YO  = (1<<7);
        int XY  = (1<<8);
        int II  = (1<<9);
        int STI = (1<<10);
        int A0  = (1<<11);
        int A1  = (1<<12);
        int A2  = (1<<13);
        int A3  = (1<<14);
        int AOT = (1<<15);
        int PCL = (1<<16);
        int PCH = (1<<17);
        int PCE = (1<<18);
        int PCO  = (1<<19);
        int SI  = (1<<20);
        int SO  = (1<<21);
        int SA  = (1<<22);
        int SU  = (1<<23);
        int SE  = (1<<24);
        int LOL = (1<<25);
        int LOH = (1<<26);
        int LIL = (1<<27);
        int LIH = (1<<28);
        int LO  = (1<<29);
        int RW  = (1<<30);
        while(true){
            long startTime = System.nanoTime();
            addressBus = 0;
            control = decoder.update(statusRegister.getValue(), clock);
            control = decoder.checkBranch(control, statusRegister.getValue());
            A.setInput((control&AI) == AI);
            A.setOutput((control&AO)==AO);
            B.setInput((control&BI)==BI);
            B.setOutput(false);
            X_Y.setInputLow((control&XI)==XI);
            X_Y.setOutputLow((control&XO)==XO);
            X_Y.setInputHigh((control&YI)==YI);
            X_Y.setOutputHigh((control&YO)==YO);
            X_Y.setOutputAddress((control&XY)==XY);
            decoder.setInstructionIn((control&II)==II);
            statusRegister.setInput((control&STI)==STI);
            ALU.setA0((control&A0)==A0);
            ALU.setA1((control&A1)==A1);
            ALU.setA2((control&A2)==A2);
            ALU.setA3((control&A3)==A3);
            ALU.setOutput((control&AOT)==AOT);
            programCounter.setCounterLow((control&PCL)==PCL);
            programCounter.setCounterHigh((control&PCH)==PCH);
            programCounter.setCounterEnable((control&PCE)==PCE);
            programCounter.setCounterOutput((control&PCO)==PCO);
            stack.setInput((control&SI)==SI);
            stack.setOutput((control&SO)==SO);
            stack.setOutputAddress((control&SA)==SA);
            stack.setDown((control&SU)==SU);
            stack.setCountEnable((control&SE)==SE);
            latch.setOutputLow((control&LOL)==LOL);
            latch.setOutputHigh((control&LOH)==LOH);
            latch.setInputLow((control&LIL)==LIL);
            latch.setInputHigh((control&LIH)==LIH);
            latch.setOutputAddress((control&LO)==LO);
            readWrite = (control&RW)==RW;
            RAM.setRead(readWrite);
            ROM.setRead(readWrite);
//-------------------------------------------------------------------------------
            addressBus = programCounter.addressUpdate(addressBus);
            addressBus = stack.addressUpdate(addressBus,clock);
            addressBus = X_Y.addressUpdate(addressBus,clock);
            addressBus = latch.addressUpdate(addressBus,clock);
            dataBus = RAM.update(addressBus,dataBus,clock);
            dataBus = ROM.update(addressBus,dataBus);
            dataBus = programCounter.update(dataBus,clock);
            statusRegister.setValue(decoder.setStatus(statusRegister.getValue()));
            ALU.setStatus(statusRegister.getValue());
            dataBus = X_Y.update(dataBus,clock);
            dataBus = ALU.update(dataBus, A.getValue(), B.getValue(), clock);
            dataBus = A.update(dataBus,clock);
            dataBus = B.update(dataBus,clock);
            dataBus = X_Y.update(dataBus,clock);
            dataBus = latch.update(dataBus,clock);
            dataBus = statusRegister.update(dataBus,clock);
            statusRegister.setValue(ALU.updateStatus(statusRegister.getValue()));
            dataBus = stack.update(dataBus,clock);
            //-------------------------------------------
            decoder.setInstruction(dataBus,clock);

            //System Variables check

            if(X_Y.getLowValue()==0x04){
                if(counter == 0) {
                    System.out.println((RAM.getValue(0x0206) << 16) + (RAM.getValue(0x0207) << 8) + (RAM.getValue(0x0208)));
                }
                counter++;
                if (counter >= 14){
                    counter = 0;
                }
            }
            /*
            System.out.printf("%02x",A.getValue());
            System.out.print(" | ");
            if((control&AI) == AI){
                System.out.print("AI");
            } else if((control&AO) == AO){
                System.out.print("AO");
            } else {
                System.out.print("--");
            }
            System.out.print(" | ");
            System.out.printf("%02x",B.getValue());
            System.out.print(" | ");
            System.out.print(((control&BI) == BI) ? "BI" : "--");
            System.out.print(" | ");
            System.out.print(((control&AOT) == AOT) ? "AOT" : "---");
            System.out.print(" | ");

             */
            /*
            System.out.printf("%02x",A.getValue());
            System.out.print(" | ");
            System.out.printf("%02x",B.getValue());
            System.out.print(" | ");
            System.out.printf("%02x",X_Y.getLowValue());
            System.out.print(" | ");
            System.out.printf("%02x",X_Y.getHighValue());
            System.out.print(" | ");
            System.out.printf("%04x",programCounter.getCounter());
            System.out.print(" | ");
            System.out.printf("%02x",statusRegister.getValue());
            System.out.print(" | ");
            System.out.printf("%02x",dataBus);
            System.out.print(" | ");
            System.out.printf("%04x",addressBus);
            System.out.print(" |A: ");
            System.out.printf("%02x", RAM.getValue(0x0200));
            System.out.print(" | ");
            System.out.printf("%02x", RAM.getValue(0x0201));
            System.out.print(" | ");
            System.out.printf("%02x", RAM.getValue(0x0202));
            System.out.print(" |B: ");
            System.out.printf("%02x", RAM.getValue(0x0203));
            System.out.print(" | ");
            System.out.printf("%02x", RAM.getValue(0x0204));
            System.out.print(" | ");
            System.out.printf("%02x", RAM.getValue(0x0205));
            System.out.print(" |C: ");
            System.out.printf("%02x", RAM.getValue(0x0206));
            System.out.print(" | ");
            System.out.printf("%02x", RAM.getValue(0x0207));
            System.out.print(" | ");
            System.out.printf("%02x", RAM.getValue(0x0208));
            //System.out.print(" | ");
            //System.out.printf("%02x", decoder.counterValue);
            System.out.print(" |I: ");
            System.out.printf("%02x",decoder.InstructionBuffer);
            System.out.print(" | ");
            System.out.println(statusRegister.getValue()&(1<<0);
             */
            /*
            System.out.print(readWrite);
            System.out.print(" | ");
            System.out.println(clock);
            */
            /*
            System.out.printf("%03d", Computer.RAM.getValue(0x0200));
            System.out.print(" | ");
            System.out.printf("%03d", Computer.RAM.getValue(0x0201));
            System.out.print(" | ");
            System.out.printf("%03d\n", Computer.RAM.getValue(0x0202));
            */


            //long endTime = System.nanoTime();
            //sleep(500);
            //sleep(0, (int) Math.max(0,(totalTime - (endTime - startTime))));
            //endTime = System.nanoTime();
            //System.out.println((endTime - startTime)/1000);
            clock = !clock;

        }
    }

}


