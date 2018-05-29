package com.tea;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 17/10/2016.
 */

public class BinaryConverter {
    private int threshold;

    public BinaryConverter(){
        this.threshold = 0;
    }

    public BinaryConverter(int threshold){
        this.threshold = threshold;
    }

    public void setThreshold(int threshold){
        this.threshold = threshold;
    }

    public int getThreshold(){
        return threshold;
    }

    public MyImage convertImage(MyImage grayscaleImg){
        MyImage result = new MyImage(grayscaleImg.getHeight(),grayscaleImg.getWidth());
        for(int y = 0; y<grayscaleImg.getHeight();y++) {
            for(int x = 0; x<grayscaleImg.getWidth();x++) {
                int val;
                if(grayscaleImg.getRGB(x,y)<threshold) {
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

    public MyImage convertImageInvers(MyImage grayscaleImg){
        MyImage result = new MyImage(grayscaleImg.getHeight(),grayscaleImg.getWidth());
        for(int y = 0; y<grayscaleImg.getHeight();y++) {
            for(int x = 0; x<grayscaleImg.getWidth();x++) {
                int val;
                if(grayscaleImg.getRGB(x,y)>=threshold) {
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
