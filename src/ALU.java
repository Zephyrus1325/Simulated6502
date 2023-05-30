public class ALU {
    public ALU(){}
    public byte A = 0;
    public byte B = 0;
    final byte C = 0;
    final byte Z = 1;
    final byte D = 3;//<- chato pra caramba pra implementar, mas binary coded decimal.
    final byte V = 6;
    final byte N = 7;



    public void add(){
        //if C=1, A+B+1, else, A+B
        A = (byte) (A+B);
        //N = 1 if signed(A+B)<0; V=1 if Signed(A+B) > 127 or < -128; Z=1 if A+B = 0 ; C = 1 if A+B > 255
    }

    public void sub(){
        //if C=1, A-B-1, else, A-B
        A = (byte) (A-B);
        //N = 1 if signed(A-B)<0; V=1 if Signed(A-B) > 127 or < -128; Z=1 if A-B = 0 ; C = 1 if A-B < 0
    }

    public void left_shift(){
        A = (byte) (A<<1);
        //N = 1 if A<<1 < 0; Z=1 if A<<1 = 0; C=1 if A<<1 < 255
    }

    public void right_shift(){
        A = (byte)(A>>1);
        //N = 0 everytime; Z if A>>1 = 0
    }

    public void AND(){
        A = (byte)(A&B);
        //N=1 if A&B < 0; Z=1 if A&B = 0
    }

    public void OR(){
        A = (byte)(A|B);
        //N=1 if A|B < 0; Z=1 if A|B = 0
    }

    public void XOR(){
        A = (byte)(A^B);
        //N=1 if A^B < 0; Z=1 if A^B = 0
    }

    public void compare(){
        //Write N = 1 if A<B; Write Z = 1 if A=B; Write Carry 1 if A>B;
    }

    public void left_rotate(){
        A = (byte) ((A << 1) | (A >> 7));
        //N = 1 if A<<1 < 0; Z=1 if A<<1 = 0; C=1 if A<<1 < 255
    }

    public void right_rotate(){
       A = (byte) ((A >> 1) | (A << 7));
        //N = 1 if A>>1 < 0; Z=1 if A>>1 = 0; C=1 if A>>1 < 255
    }


}



