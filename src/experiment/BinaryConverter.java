package experiment;
import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 17/10/2016.
 */

public class BinaryConverter {

    public static MyImage convertImage(MyImage grayscaleImg, int threshold){
        MyImage result = new MyImage(grayscaleImg.getHeight(),grayscaleImg.getWidth());
        for(int y = 0; y<grayscaleImg.getHeight();y++) {
            for(int x = 0; x<grayscaleImg.getWidth();x++) {
                int val;
                int rgb = grayscaleImg.getRGB(x,y);
                if(MyImage.getGraylevel(rgb)<threshold) {
                    val = 0;
                } else {
                    val = 255;
                }
                int color = MyImage.makeRGB(val,val,val);
                result.setRGB(x,y,color);
            }
        }
        return result;
    }

    public static MyImage convertImageInvers(MyImage grayscaleImg, int threshold){
        MyImage result = new MyImage(grayscaleImg.getHeight(),grayscaleImg.getWidth());
        for(int y = 0; y<grayscaleImg.getHeight();y++) {
            for(int x = 0; x<grayscaleImg.getWidth();x++) {
                int val;
                int rgb = grayscaleImg.getRGB(x,y);
                if(MyImage.getGraylevel(rgb)>=threshold) {
                    val = 0;
                } else {
                    val = 255;
                }

                int color = MyImage.makeRGB(val,val,val);
                result.setRGB(x,y,color);
            }
        }
        return result;
    }
}
