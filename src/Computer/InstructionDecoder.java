package Computer;

import static java.lang.Thread.onSpinWait;

public class InstructionDecoder {
    public int InstructionBuffer = 0;
    public InstructionDecoder(){}

    public int counterValue = 0;
    public boolean instructionIn = false;
    public volatile boolean halt = false;

    final int C = (1<<0);
    final int Z = (1<<1);
    final int D = (1<<3);//<- chato pra caramba pra implementar, mas binary coded decimal.
    final int V = (1<<6);
    final int N = (1<<7);

    void setInstruction(int input,boolean clock){
        if(!clock & instructionIn) {
            InstructionBuffer = ((int)input)&0xff;
        }
    }

    public int setStatus(int status){
        int output = status;
        if(this.InstructionBuffer == 0x18){
            output = status & ~this.C;
        }
        return output;
    }
    int checkBranch(int control, int status){
        int output = control;
        if((counterValue > 0) && (counterValue < 6)) {
            switch (this.InstructionBuffer) {
                case 0x10: //BPL
                    if ((status & N) == N) {
                        output = output & ~(LIL | LIH | LOL | LOH | PCL | PCH | PCO | RW); //disables input sequence
                    }
                    break;

                case 0x30: //BMI
                    if ((status & N) != N) {
                        output = output & ~(LIL | LIH | LOL | LOH | PCL | PCH | PCO | RW); //disables input sequence
                    }
                    break;

                case 0x50: //BVC
                    if ((status & V) != V) {
                        output = output & ~(LIL | LIH | LOL | LOH | PCL | PCH | PCO | RW); //disables input sequence
                    }
                    break;

                case 0x70: //BVS
                    if ((status & V) == V) {
                        output = output & ~(LIL | LIH | LOL | LOH | PCL | PCH | PCO | RW); //disables input sequence
                    }
                    break;

                case 0x80: //BRA
                    break;
                case 0x90: //BCC
                    if ((status & C) == C) {
                        output = output & ~(LIL | LIH | LOL | LOH | PCL | PCH | PCO | RW); //disables input sequence
                    }
                    break;
                case 0xB0: //BCS
                    if ((status & C) != C) {
                        output = output & ~(LIL | LIH | LOL | LOH | PCL | PCH | PCO | RW); //disables input sequence
                    }
                    break;
                case 0xD0: //BNE
                    if ((status & Z) == Z) {
                        output = output & ~(LIL | LIH | LOL | LOH | PCL | PCH | PCO | RW); //disables input sequence
                    }
                    break;
                case 0xF0: //BEQ
                    if ((status & Z) != Z) {
                        output = output & ~(LIL | LIH | LOL | LOH | PCL | PCH | PCO | RW); //disables input sequence
                    }
                    break;
                default:
                    break;
            }
        }
        return output;
    }

    void setInstructionIn(boolean input){instructionIn = input;}

    public int update(int status,boolean clock){
        if(halt){
            System.out.println("Computer Halted");
            while (halt) {
                onSpinWait();
            }
        }
        int place = (InstructionBuffer<<3)|counterValue;
        int output = ROM[place];
        if(clock){
            counterValue++;
            counterValue = counterValue > 7 ? 0 : counterValue;

        }
        if((output&RS)==RS) {
            counterValue = 0;
            place = (InstructionBuffer << 3) | counterValue;
            output = ROM[place];
        }
        return output;
    }

    //----------------------BIG CODE SIZE ALERT--------------------------------------//
    static int AI  = (1<<0);
    static int AO  = (1<<1);
    static int BI  = (1<<2);
    static int RS  = (1<<3);
    static int XI  = (1<<4);
    static int XO  = (1<<5);
    static int YI  = (1<<6);
    static int YO  = (1<<7);
    static int XY  = (1<<8);
    static int II  = (1<<9);
    static int STI = (1<<10);
    static int A0  = (1<<11);
    static int A1  = (1<<12);
    static int A2  = (1<<13);
    static int A3  = (1<<14);
    static int AOT = (1<<15);
    static int PCL = (1<<16);
    static int PCH = (1<<17);
    static int PCE = (1<<18);
    static int PCO  = (1<<19);
    static int SI  = (1<<20);
    static int SO  = (1<<21);
    static int SA  = (1<<22);
    static int SU  = (1<<23);
    static int SE  = (1<<24);
    static int LOL = (1<<25);
    static int LOH = (1<<26);
    static int LIL = (1<<27);
    static int LIH = (1<<28);
    static int LO  = (1<<29);
    static int RW  = (1<<30);

    private int[] ROM = {
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //00 - BRK s
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //01 - ORA (zp, x)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //02 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //03 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //04 - TSB zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //05 - ORA zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //06 - ASL zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //07 - RMB0 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //08 - PHP s
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //09 - ORA #
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //0a - ASL A
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //0b -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //0c - TSB a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //0d - ORA a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //0e - ASL a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //0f - BBR0 r
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //10 - BPL r
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //11 - ORA (zp), y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //12 - ORA (zp)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //13 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //14 - TRB zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //15 - ORA zp,x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //16 - ASL zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //17 - RMB1 zp
PCE|PCO|II|RW   ,0                ,RS               ,0                ,0                ,0   ,0   ,0    , //18 - CLC
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //19 - ORA a,y
PCE|PCO|II|RW   ,BI|AO            ,AI|AOT|A0|A1|A3  ,RS               ,0                ,0   ,0   ,0    , //1a - INC A
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //1b -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //1c - TRB a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //1d - ORA a,x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //1e - ASL a,x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //1f - BBR1 r
PCE|PCO|II|RW   ,LIL|PCE|PCO      ,LIH|PCE|PCO      ,PCO|PCL|SE|SA|RW ,PCO|PCH|SE|SA|RW ,LOL|PCL,LOH|PCH,RS, //20 - JSR
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //21 - AND (zp, x)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //22 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //23 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //24 - BIT zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //25 - AND zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //26 - ROL zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //27 - RMB2 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //28 - PLP s
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //29 - AND #
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //2a - ROL A
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //2b -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //2c - BIT a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //2d - AND a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //2e - ROL a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //2f - BBR2 r
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //30 - BMI r
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //31 - AND (zp), y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //32 - AND (zp)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //33 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //34 - BIT zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //35 - AND zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //36 - ROL zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //37 - RMB3 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //38 - SEC I
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //39 - AND a, y
PCE|PCO|II|RW   ,BI|AO            ,AI|AOT|A2|A3     ,RS               ,0                ,0   ,0   ,0    , //3a - DEC A
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //3b -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //3c - BIT a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //3d - AND a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //3e - ROL a ,x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //3f - BBR3 r
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //40 - RTI s
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //41 - EOR (zp, x)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //42 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //43 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //44 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //45 - EOR zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //46 - LSR zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //47 - RMB4 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //48 - PHA s
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //49 - EOR #
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //4a - LSR A
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //4b -
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,LOL|PCL          ,LOH|PCH          ,RS  ,0   ,0    , //4c - JMP a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //4d - EOR a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //4e - LSR a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //4f - BBR4 r
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //50 - BVC r (a)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //51 - ERO (zp), y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //52 - EOR (zp)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //53 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //54 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //55 - EOR zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //56 - LSR zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //57 - RMB5 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //58 - CLI i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //59 - EOR a, y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //5a - PHY s
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //5b -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //5c -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //5d - EOR a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //5e - LSR a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //5f - BBR5 r
PCE|PCO|II|RW   ,SE|SU            ,SA|SE|SU|PCH     ,SA|PCL           ,RS               ,0   ,0   ,0    , //60 - RTS s
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //61 - ADC (zp ,x)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //62 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //63 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //64 - STZ zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //65 - ADC zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //66 - ROR zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //67 - RMB6 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //68 - PLA s
PCE|PCO|II|RW   ,PCE|PCO|BI|RW    ,AI|AOT|A0        ,RS               ,0                ,0   ,0   ,0    , //69 - ADC #
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //6a - ROR A
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //6b -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //6c - JMP (a)
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCO|PCE|RW   ,BI|LO|RW         ,AI|AOT|A0        ,RS  ,0   ,0    , //6d - ADC a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //6e - ROR a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //6f - BBR6 r
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //70 - BVS r
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //71 - ADC (zp), y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //72 - ADC (zp)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //73 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //74 - STZ zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //75 - ADC zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //76 - ROR zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //77 - RMB7 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //78 - SEI i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //79 - ADC z,y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //7a - PLY s
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //7b -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //7c - JMP (a, x)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //7d - ADC a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //7e - ROR a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //7f - BBR6 r
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,LOL|PCL          ,LOH|PCH          ,RS  ,0   ,0    , //80 - BRA r (a)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //81 - STA (zp, x)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //82 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //83 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //84 - STY zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //85 - STA zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //86 - STX zp, y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //87 - SMB1 zp
PCE|PCO|II|RW   ,BI|AO            ,AI|AOT|A2|A3     ,RS               ,0                ,0   ,0   ,0    , //88 - TYA i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //89 - STA a, y
PCE|PCO|II|RW   ,AI|XO            ,RS               ,0                ,0                ,0   ,0   ,0    , //8a - TXA i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //8b - STY a
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,YO|LO            ,RS               ,0   ,0   ,0    , //8c - STY a
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,AO|LO            ,RS               ,0   ,0   ,0    , //8d - STA a
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,XO|LO            ,RS               ,0   ,0   ,0    , //8e - STX a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //8f - BBS0 r
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,LOL|PCL          ,LOH|PCH          ,RS  ,0   ,0    , //90 - BCC r (a)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //91 - STA (zp), y ???
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //92 - STA (zp), y ???
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //93 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //94 - STY zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //95 - STA zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //96 - STX zp, y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //97 - SMB1 zp
PCE|PCO|II|RW   ,AI|YO            ,RS               ,0                ,0                ,0   ,0   ,0    , //98 - TYA i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //99 - STA a, y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //9a - TXS i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //9b -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //9c - STZ a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //9d - STA a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //9e - STZ a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //9f - BBS1 r
PCE|PCO|II|RW   ,PCE|PCO|YI|RW    ,RS               ,0                ,0                ,0   ,0   ,0    , //a0 - LDY #
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //a1 - LDA (zp, x)
PCE|PCO|II|RW   ,PCE|PCO|XI|RW    ,RS               ,0                ,0                ,0   ,0   ,0    , //a2 - LDX #
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //a3 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //a4 - LDY zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //a5 - LDA zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //a6 - LDX zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //a7 - SMB2 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //a8 - TAY i
PCE|PCO|II|RW   ,PCE|PCO|AI|RW    ,RS               ,0                ,0                ,0   ,0   ,0    , //a9 - LDA #
PCE|PCO|II|RW   ,AO|XI            ,RS               ,0                ,0                ,0   ,0   ,0    , //aa - TAX i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //ab -
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,YI|LO|RW         ,RS               ,0   ,0   ,0    , //ac - LDY a
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,AI|LO|RW         ,RS               ,0   ,0   ,0    , //ad - LDA a
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,XI|LO|RW         ,RS               ,0   ,0   ,0    , //ae - LDX a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //af - BBS2 r
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,LOL|PCL          ,LOH|PCH          ,RS  ,0   ,0    , //b0 - BCS r (a)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //b1 - LDA (zp), y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //b2 - LDA (zp)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //b3 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //b4 - LDY zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //b5 - LDA zp, X
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //b6 - LDX zp, y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //b7 - SMB3 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //b8 - CLV i
PCE|PCO|II|RW   ,PCE|PCO|BI|RW    ,AI|YO            ,LIL|AOT|A0       ,LIH|PCE|PCO|RW ,AI|LO|RW  ,RS,0  , //b9 - LDA a,y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //ba - TSX i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //bb -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //bc - LDY a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //bd - LDA a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //be - LDX a, y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //bf - BBS3 r
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //c0 - CPY #
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //c1 - CMP (zp, x)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //c2 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //c3 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //c4 - CPY zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //c5 - CMP zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //c6 - DEC zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //c7 - SMB4 zp
PCE|PCO|II|RW   ,BI|YO            ,YI|AOT|A0|A1|A3  ,RS               ,0                ,0   ,0   ,0    , //c8 - INY i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //c9 - CMP #
PCE|PCO|II|RW   ,BI|XO            ,XI|AOT|A2|A3     ,RS               ,0                ,0   ,0   ,0    , //ca - DEX i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //cb - WAI i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //cc - CPY a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //cd - CPY a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //ce - CMP a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //cf - DEC a
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,LOL|PCL          ,LOH|PCH          ,RS  ,0   ,0    , //d0 - BNE r (a)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //d1 - CMP (zp), y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //d2 - CMP (zp)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //d3 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //d4 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //d5 - CMP zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //d6 - DEC zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //d7 - SMB5 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //d8 - CLD i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //d9 - CMP a, y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //da - PHX s
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //db - STP I
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //dc -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //dd - CMP a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //de - DEC a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //df - BBS5 r
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //e0 - CPX #
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //e1 - SBC (zp, x)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //e2 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //e3 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //e4 - CPX zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //e5 - SBC zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //e6 - INC zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //e7 - SMB6 zp
PCE|PCO|II|RW   ,BI|XO            ,XI|AOT|A0|A1|A3  ,RS               ,0                ,0   ,0   ,0    , //e8 - INX i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //e9 - SBC #
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //ea - NOP i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //eb -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //ec - CPX a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //ed - SBC a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //ee - INC a
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //ef - BBS6 r
PCE|PCO|II|RW   ,LIL|PCE|PCO|RW   ,LIH|PCE|PCO|RW   ,LOL|PCL          ,LOH|PCH          ,RS  ,0   ,0    , //f0 - BEQ r (a)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //f1 - SBC (zp), y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //f2 - SBC (zp)
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //f3 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //f4 -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //f5 - SBC zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //f6 - INC zp, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //f7 - SMB7 zp
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //f8 - SED i
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //f9 - SBC a, y
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //fa - PLX s
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //fb -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //fc -
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //fd - SBC a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //fe - INC a, x
PCE|PCO|II|RW   ,0                ,0                ,0                ,0                ,0   ,0   ,0    , //ff - BBS7 r
    };
}
