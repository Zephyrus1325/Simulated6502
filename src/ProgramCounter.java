public class ProgramCounter {
    char Counter = 0;
    public ProgramCounter(){
    }

    void setCounter(char value){
        this.Counter = value;
    }

    char read(){
        return Counter;
    }
}
