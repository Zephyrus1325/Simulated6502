import java.util.ArrayList;

public class IO {
    int startAddress;
    int endAddress;
    int horizontalSize;
    int verticalSize;
    ArrayList<String> data = new ArrayList<String>();
    public IO(int start, int end, int hor, int vert){
        startAddress = start;
        endAddress = end;
        horizontalSize = hor;
        verticalSize = vert;
        for(int i = 0; i < verticalSize; i++){
            data.add("");
        }
    }
}
