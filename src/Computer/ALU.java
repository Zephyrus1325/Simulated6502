package Computer;

public class ALU {
    public ALU(){}
    private boolean output = false;
    public int A = 0;
    public int B = 0;
    final int C = (1<<0);
    final int Z = (1<<1);
    final int D = (1<<3);//<- chato pra caramba pra implementar, mas binary coded decimal.
    final int V = (1<<6);
    final int N = (1<<7);
    private int status = 0x30; //0b00110000

    private boolean A0 = false;
    private boolean A1 = false;
    private boolean A2 = false;
    private boolean A3 = false;


    public void add(){ //0001
        //if C=1, A+B+1, else, A+B
        A = (A+B);
        if((status & C) == C){
            A += 1;
        }

        if(A > 255){
            A -= 256;
            status = (status | C);
        } else {
            status = (status & ~C);
        }

        if(A < 0){
            A += 256;
            status = (status | N);
        } else {
            status = (status & ~N);
        }

        if(A == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }


        //N = 1 if signed(A+B)<0; V=1 if Signed(A+B) > 127 or < -128; Z=1 if A+B = 0 ; C = 1 if A+B > 255
    }

    public void sub(){ //0010
        //if C=1, A-B-1, else, A-B
        A = (A-B);

        if(A > 255){
            A -= 256;
            status = (status | C);
        } else {
            status = (status & ~C);
        }

        if(A < 0){
            A += 256;
            status = (status | N);
        } else {
            status = (status & ~N);
        }

        if(A == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }
        //N = 1 if signed(A-B)<0; V=1 if Signed(A-B) > 127 or < -128; Z=1 if A-B = 0 ; C = 1 if A-B < 0
    }

    public void left_shift(){ //0011
        A = (A<<1);

        if(A > 255){
            A -= 256;
            status = (status | C);
        } else {
            status = (status & ~C);
        }

        if(A < 0){
            A += 256;
            status = (status | N);
        } else {
            status = (status & ~N);
        }

        if(A == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }

        //N = 1 if A<<1 < 0; Z=1 if A<<1 = 0; C=1 if A<<1 < 255
    }

    public void right_shift(){ //0100
        A = (A>>1);

        status = (status & ~N);

        if(A > 255){
            A -= 256;
            status = (status | C);
        } else {
            status = (status & ~C);
        }

        if(A == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }
        //N = 0 everytime; Z if A>>1 = 0
    }

    public void AND(){ //0101
        A = (A&B);

        if(A < 0){
            A += 256;
            status = (status | N);
        } else {
            status = (status & ~N);
        }

        if(A == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }
        //N=1 if A&B < 0; Z=1 if A&B = 0
    }

    public void OR(){ //0110
        A = (A|B);
        if(A < 0){
            A += 256;
            status = (status | N);
        } else {
            status = (status & ~N);
        }

        if(A == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }
        //N=1 if A|B < 0; Z=1 if A|B = 0
    }

    public void XOR(){ //0111
        A = (A^B);
        if(A < 0){
            A += 256;
            status = (status | N);
        } else {
            status = (status & ~N);
        }

        if(A == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }
        //N=1 if A^B < 0; Z=1 if A^B = 0
    }

    public void compare(){ //1000
        if(A > B){
            status = (status | C);
        } else {
            status = (status & ~C);
        }

        if(A < B){
            status = (status | N);
        } else {
            status = (status & ~N);
        }

        if(A == B){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }
        //Write N = 1 if A<B; Write Z = 1 if A=B; Write Carry 1 if A>B;
    }

    public void left_rotate(){ //1001
        A = (int) ((A << 1) | (A >> 7));

        if(A > 255){
            A -= 256;
            status = (status | C);
        } else {
            status = (status & ~C);
        }

        if(A == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }
        //N = 1 if A<<1 < 0; Z=1 if A<<1 = 0; C=1 if A<<1 < 255
    }

    public void right_rotate(){ //1010
       A = (int) ((A >> 1) | (A << 7));
        if(A > 255){
            A -= 256;
            status = (status | C);
        } else {
            status = (status & ~C);
        }

        if(A == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }
        //N = 1 if A>>1 < 0; Z=1 if A>>1 = 0; C=1 if A>>1 < 255
    }

    public void quickSum(){ //1011
        B += 1;

        if(B > 255){
            B -= 256;
        }

        if(B < 0){
            B += 256;
            status = (status | N);
        } else {
            status = (status & ~N);
        }

        if(B == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }
    }
    public void quickSub(){ //1100
        B = B - 1;

        if(B > 255){
            B -= 256;
        }

        if(B < 0){
            B += 256;
            status = (status | N);
        } else {
            status = (status & ~N);
        }

        if(B == 0){
            status = (status | Z);
        } else {
            status = (status & ~Z);
        }
    }

    public int update(int bus, int A_value, int B_value, boolean clock){
        int outputValue = bus;
        if(!clock) {
            A = A_value;
            B = B_value;
            if (A0 || A1 || A2 || A3) {
                updateSignals();
                if (output) {
                    if((!A0 && !A1 && A2 && A3) || (A0 && A1 && !A2 && A3)){
                        outputValue = B;
                    } else {
                        outputValue = A;
                    }
                }
            }
        }
        return outputValue;
    }

    public int updateStatus(int status){
        int output = status;
        if(A0||A1||A2||A3) {
            output = this.status;
        }
        return output;
    }

    private void updateSignals(){
        if(A0 && !A1 && !A2 && !A3) {
            add();
        }

        if(!A0 && A1 && !A2 && !A3) {
            sub();
        }

        if(!A3 && !A2 && A1 && A0) {//0011
            left_shift();
        }

        if(!A3 && A2 && !A1 && !A0) {//0100
            right_shift();
        }
        if(!A3 && A2 && !A1 && A0) {//0101
            AND();
        }
        if(!A3 && A2 && A1 && !A0) {//0110
            OR();
        }
        if(!A3 && A2 && A1 && A0) {//0111
            XOR();
        }
        if(A3 && !A2 && !A1 && !A0){//1000
            compare();
        }
        if(A3 && !A2 && !A1 && A0) {//1001
            left_rotate();
        }
        if(A3 && !A2 && A1 && !A0) {//1010
            right_rotate();
        }
        if(A0 && A1 && !A2 && A3){ //1011
            quickSum();
        }
        if(!A0 && !A1 && A2 && A3){ //1100
            quickSub();
        }
    }

    public int updateStatus(){
        return status;
    }

    public void setA0(boolean input){
        this.A0 = input;
    }

    public void setA1(boolean input){
        this.A1 = input;
    }

    public void setA2(boolean input){
        this.A2 = input;
    }

    public void setA3(boolean input){
        this.A3 = input;
    }

    public void setOutput(boolean input){
        this.output = input;
    }

    public void setStatus(int status){
        this.status = status;
    }
}



