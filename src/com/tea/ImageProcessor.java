package com.tea;
import java.awt.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Ahmad Aidin on 06/09/2016.
 */
public class ImageProcessor {
    @SuppressWarnings("unused")
    private static final String TAG = "image.filter.ImageProcessor";
    private MyImage bitmap;
    private MyImage oriBitmap;
    private MyImage grayscale;
    private MyImage oriGrayscale;
    private int[] grayHistogram;
    private Hashtable<Integer, Integer> convolusionMatrix;
    public int featuresNum;

    public ImageProcessor() {

    }

    public ImageProcessor(MyImage bitmap) {
        this.bitmap = bitmap;
        this.oriBitmap = bitmap;
        setGrayscale();
        oriGrayscale = grayscale;
        //setGreylevelHistogram();
        featuresNum=0;
    }

    public MyImage getGrayscale() {
        return grayscale;
    }

    public int[] getGrayHistogram() {
        return grayHistogram;
    }

    public void setBitmap(MyImage bitmap) {
        this.bitmap = bitmap;
        this.oriBitmap =bitmap;
        setGrayscale();
        oriGrayscale = grayscale;
        setGreylevelHistogram();
        featuresNum=0;
    }

    public MyImage bitmap(){
        return bitmap;
    }

    public void resetBitmap() {
        bitmap = oriBitmap;
        resetGrayscale();
        featuresNum=0;
    }

    private void resetGrayscale() {
            grayscale = new MyImage(oriGrayscale);
    }

    public static MyImage getGrayscale(MyImage bmp) {

        MyImage grayscale = new MyImage(bmp.getHeight(),bmp.getWidth());

        for (int y=0; y< bmp.getHeight(); y++) {
            for (int x=0; x< bmp.getWidth(); x++) {
                int color = bmp.getRGB(x,y);
                grayscale.setRGB(x,y, MyImage.convertGrayLevel(color));
            }
        }
        return grayscale;
    }

    public static int[] getGreylevelHistogram(MyImage bmp) {
        MyImage grayscale = getGrayscale(bmp);

        int histogram[] = new int[256];
        for(int i=0; i<histogram.length; i++) histogram[i]=0;

        for(int y = 0; y < grayscale.getHeight(); y++) {
            for (int x = 0; x<grayscale.getWidth(); x++) {
                int gray = grayscale.getRGB(x,y);
                histogram[gray]++;
            }
        }
        return histogram;
    }


    private  static Hashtable<Integer,Integer> getGrayCDF(MyImage bmp){
        int histogram[] = getGreylevelHistogram(bmp);

        Hashtable<Integer,Integer> cdfTable = new Hashtable<>();

        int temp=0;
        for (int i = 0; i<histogram.length;i++) {
            if (histogram[i] != 0) {
                temp += histogram[i];
                cdfTable.put(i,temp);
            }
        }
        return cdfTable;
    }



    private static Hashtable<Integer, Integer> grayLvHistogramEqualizationLUT(MyImage bmp) {
        Hashtable<Integer,Integer> cdfTable = getGrayCDF(bmp);
        Hashtable<Integer,Integer> imageLUT = new Hashtable<>();

        Enumeration<Integer> cdfEnum = cdfTable.elements();
        Enumeration<Integer> keyEnum = cdfTable.keys();

        int cdfMin = cdfEnum.nextElement();
        while(cdfEnum.hasMoreElements()){
            int val = cdfEnum.nextElement();
            if(val<cdfMin){
                cdfMin = val;
            }
        }

        long numPixel = bmp.getWidth() * bmp.getHeight();

        long denumerator = numPixel-cdfMin;
        while (keyEnum.hasMoreElements()) {
            int key = keyEnum.nextElement();
            int numerator = (cdfTable.get(key) - cdfMin) * (256 - 1);
            long val = (long) (Math.round(numerator/denumerator) + 0.5);
            if (val > 255) {
                imageLUT.put(key, 255);
            } else {
                imageLUT.put(key, (int) val);
            }
        }

        return imageLUT;
    }


    public static MyImage grayLvHistogramEqualization(MyImage bmp){

        Hashtable<Integer,Integer> histLUT = grayLvHistogramEqualizationLUT(bmp);
        MyImage grayscale = getGrayscale(bmp);

        MyImage histogramEQ = new MyImage(bmp.getWidth(), bmp.getHeight());

        for(int y=0; y<bmp.getHeight(); y++){
            for(int x=0; x<bmp.getWidth(); x++){
                int oldGray = grayscale.getRGB(x,y);
                int newGray = histLUT.get(oldGray);
                int deltaGray = newGray - oldGray;
                Color p = new Color(bmp.getRGB(x,y));

                int r = p.getRed()+deltaGray;
                int g = p.getGreen()+deltaGray;
                int b = p.getBlue()+deltaGray;

                if (r<0){ r = 0;}
                else if(r>255){ r = 255; }

                if (g<0){ g = 0;}
                else if(g>255){ g = 255; }

                if (b<0){ b = 0;}
                else if(b>255){ b = 255; }
                Color c = new Color(p.getAlpha(),r,g,b);
                histogramEQ.setRGB(x, y, c.getRGB());
            }
        }

        return histogramEQ;
    }



    private void setGrayscale() {
        grayscale = getGrayscale(bitmap);
    }

    private void setGreylevelHistogram() {
        grayHistogram = new int[256];
        for(int i=0; i<grayHistogram.length; i++) grayHistogram[i]=0;

        for(int y = 0; y < grayscale.getHeight(); y++) {
            for (int x = 0; x<grayscale.getWidth(); x++) {
                int gray = grayscale.getRGB(x,y);
                grayHistogram[gray]++;
            }
        }
    }

    private Hashtable<Integer,Integer> getGrayCDF(){
        Hashtable<Integer,Integer> grayCDF = new Hashtable<>();
        int temp=0;
        for (int i = 0; i<grayHistogram.length;i++) {
            if (grayHistogram[i] != 0) {
                temp += grayHistogram[i];
                grayCDF.put(i,temp);
            }
        }

        return grayCDF;
    }

    private Hashtable<Integer, Integer> grayLvHistogramEqualizationLUT() {
        Hashtable<Integer,Integer> imageLUT = new Hashtable<>();

        Hashtable<Integer,Integer> grayCDF = getGrayCDF();

        Enumeration<Integer> cdfEnum = grayCDF.elements();
        Enumeration<Integer> keyEnum = grayCDF.keys();

        int cdfMin = cdfEnum.nextElement();
        while(cdfEnum.hasMoreElements()){
            int val = cdfEnum.nextElement();
            if(val<cdfMin){
                cdfMin = val;
            }
        }

        long numPixel = bitmap.getWidth() * bitmap.getHeight();

        long denumerator = numPixel-cdfMin;
        while (keyEnum.hasMoreElements()) {
            int key = keyEnum.nextElement();
            int numerator = (grayCDF.get(key) - cdfMin) * (256 - 1);
            long val = (long) (Math.round(numerator/denumerator) + 0.5);
            if (val > 255) {
                imageLUT.put(key, 255);
            } else {
                imageLUT.put(key, (int) val);
            }
        }

        return imageLUT;
    }

    public void grayLvHistogramEqualization(){

        Hashtable<Integer,Integer> histLUT = grayLvHistogramEqualizationLUT();

        for(int y=0; y<bitmap.getHeight(); y++){
            for(int x=0; x<bitmap.getWidth(); x++){
                int oldGray = grayscale.getRGB(x,y);
                int newGray = histLUT.get(oldGray);
                int deltaGray = newGray - oldGray;
                int p = bitmap.getRGB(x,y);

                bitmap.setRGB(x, y, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public void smoothImage() {

        for(int y=1; y<bitmap.getHeight()-1; y++){
            for(int x=1; x<bitmap.getWidth()-1; x++){
                int sum = 0;
                for (int dy = -1; dy<2; dy++) {
                    for(int dx = -1; dx<2; dx++) {
                        sum += grayscale.getRGB(x+dx,y+dy);
                    }
                }
                int avg = sum / 9;
                int oldGray = grayscale.getRGB(x,y);
                int deltaGray = avg - oldGray;
                Color p = new Color(bitmap.getRGB(x,y));

                int r = p.getRed()+deltaGray;
                int g = p.getGreen()+deltaGray;
                int b = p.getBlue()+deltaGray;

                if (r<0){ r = 0;}
                else if(r>255){ r = 255; }

                if (g<0){ g = 0;}
                else if(g>255){ g = 255; }

                if (b<0){ b = 0;}
                else if(b>255){ b = 255; }
                Color c = new Color(p.getAlpha(),r,g,b);
                bitmap.setRGB(x, y, c.getRGB());

            }
        }
        setGrayscale();
    }

    public void sharpImage(){
        int[][] filter  = {
                {0,-1,0},
                {-1,5,-1},
                {0,-1,0}
        };

        for(int y=1; y<bitmap.getHeight()-1; y++){
            for(int x=1; x<bitmap.getWidth()-1; x++){
                int sum = 0;
                for (int dy = -1; dy<2; dy++) {
                    for(int dx = -1; dx<2; dx++) {
                        int c = grayscale.getRGB(x+dx,y+dy);
                        sum += filter[dx+1][dy+1]*c;
                    }
                }
                int oldGray = grayscale.getRGB(x,y);
                int deltaGray = sum - oldGray;
                int p = bitmap.getRGB(x,y);
                bitmap.setRGB(x, y, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public void blurImage(){
        double[][] filter  = {
                {0.0,0.2,0.0},
                {0.2,0.2,0.2},
                {0.0,0.2,0.0}
        };

        for(int y=1; y<bitmap.getHeight()-1; y++){
            for(int x=1; x<bitmap.getWidth()-1; x++){
                int sum = 0;
                for (int dy = -1; dy<2; dy++) {
                    for(int dx = -1; dx<2; dx++) {
                        int c = grayscale.getRGB(x+dx,y+dy);
                        sum += filter[dx+1][dy+1]*c;
                    }
                }
                int oldGray = grayscale.getRGB(x,y);
                int deltaGray = sum - oldGray;
                int p = bitmap.getRGB(x,y);
                bitmap.setRGB(x, y, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public void crossNbrs(){
        for(int y=1; y<bitmap.getHeight()-1; y++){
            for(int x=1; x<bitmap.getWidth()-1; x++){
                int Opt1 = Math.abs(grayscale.getRGB(x-1,y-1)-grayscale.getRGB(x+1,y+1));
                int Opt2 = Math.abs(grayscale.getRGB(x-1,y)-grayscale.getRGB(x+1,y));
                int Opt3 = Math.abs(grayscale.getRGB(x-1,y+1)-grayscale.getRGB(x+1,y-1));
                int Opt4 = Math.abs(grayscale.getRGB(x-1,y-1)-grayscale.getRGB(x+1,y+1));

                int newGray = Math.max(Math.max(Math.max(Opt1,Opt2),Opt3),Opt4);

                int oldGray = grayscale.getRGB(x,y);
                int deltaGray = newGray - oldGray;
                int p = bitmap.getRGB(x,y);
                bitmap.setRGB(x, y, manipulatePixel(p,deltaGray));
            }
        }
        setGrayscale();
    }

    public void centerNbrs(){
        for(int y=1; y<bitmap.getHeight()-1; y++){
            for(int x=1; x<bitmap.getWidth()-1; x++){
                int max = 0;
                for (int dy = -1; dy<2; dy++) {
                    for(int dx = -1; dx<2; dx++) {
                        int opt = Math.abs(grayscale.getRGB(x,y)-grayscale.getRGB(x+dx,y+dy));
                        max = Math.max(max,opt);
                    }
                }
                int newGray = max;
                bitmap.setRGB(x, y, newGray);//manipulatePixel(p,225));
            }
        }
        setGrayscale();
    }

    public  void robert(Convolution c) {
        ArrayList<ArrayList<Integer>> hMatrix;
        ArrayList<ArrayList<Integer>> vMatrix;
        try {
            hMatrix = c.getMatrix("Robert");
            vMatrix = MatrixOperator.rotateLeft(hMatrix);

            for(int y=1; y<bitmap.getHeight()-1; y++){
                for(int x=1; x<bitmap.getWidth()-1; x++){
                    int sumH = 0, sumV = 0;
                    int size = hMatrix.size();
                    for (int dy = -1; dy<size-1; dy++) {
                        for(int dx = -1; dx<size-1; dx++) {
                            sumH += grayscale.getRGB(x+dx,y+dy)*hMatrix.get(dy+1).get(dx+1);
                            sumV += grayscale.getRGB(x+dx,y+dy)*vMatrix.get(dy+1).get(dx+1);
                        }
                    }
                    int newGray =toPositive(sumH)+toPositive(sumV);
                    bitmap.setRGB(x, y, newGray);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setGrayscale();
    }

    public void edgeDetectLv1(Convolution c, String Operator) {
        ArrayList<ArrayList<Integer>> hMatrix;
        ArrayList<ArrayList<Integer>> vMatrix;
        try {
            hMatrix = c.getMatrix(Operator);
            vMatrix = MatrixOperator.rotateLeft(hMatrix);

            for(int y=1; y<bitmap.getHeight()-1; y++){
                for(int x=1; x<bitmap.getWidth()-1; x++){
                    int sumH = 0, sumV = 0;
                    int size = hMatrix.size();
                    for (int dy = -1; dy<size-1; dy++) {
                        for(int dx = -1; dx<size-1; dx++) {
                            sumH += grayscale.getRGB(x+dx,y+dy)*hMatrix.get(dy+1).get(dx+1);
                            sumV += grayscale.getRGB(x+dx,y+dy)*vMatrix.get(dy+1).get(dx+1);
                        }
                    }
                    int newGray = toPositive(sumH) + toPositive(sumV);
                    bitmap.setRGB(x, y, newGray);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setGrayscale();
    }

    public  void edgeDetectLv2(Convolution c, String Operator) {
        ArrayList<ArrayList<Integer>> north;
        ArrayList<ArrayList<Integer>> west;
        ArrayList<ArrayList<Integer>> south;
        ArrayList<ArrayList<Integer>> east;
        ArrayList<ArrayList<Integer>> northEast;
        ArrayList<ArrayList<Integer>> southEast;
        ArrayList<ArrayList<Integer>> southWest;
        ArrayList<ArrayList<Integer>> northWest;


        try {
            north = c.getMatrix(Operator);
            west = MatrixOperator.rotateLeft(north);
            south = MatrixOperator.rotateLeft(west);
            east = MatrixOperator.rotateLeft(south);
            northEast = MatrixOperator.rotate1Right(north);
            southEast = MatrixOperator.rotate1Right(east);
            southWest = MatrixOperator.rotate1Right(south);
            northWest = MatrixOperator.rotate1Right(west);

            for(int y=1; y<bitmap.getHeight()-1; y++){
                for(int x=1; x<bitmap.getWidth()-1; x++){
                    int sumN = 0, sumE = 0, sumS=0, sumW=0, sumNE=0, sumSE=0, sumSW=0, sumNW=0;
                    int size = north.size();
                    for (int dy = -1; dy<size-1; dy++) {
                        for(int dx = -1; dx<size-1; dx++) {
                            sumN += grayscale.getRGB(x+dx,y+dy)*north.get(dy+1).get(dx+1);
                            sumE += grayscale.getRGB(x+dx,y+dy)*east.get(dy+1).get(dx+1);
                            sumS += grayscale.getRGB(x+dx,y+dy)*south.get(dy+1).get(dx+1);
                            sumW += grayscale.getRGB(x+dx,y+dy)*west.get(dy+1).get(dx+1);
                            sumNE += grayscale.getRGB(x+dx,y+dy)*northEast.get(dy+1).get(dx+1);
                            sumSE += grayscale.getRGB(x+dx,y+dy)*southEast.get(dy+1).get(dx+1);
                            sumSW += grayscale.getRGB(x+dx,y+dy)*southWest.get(dy+1).get(dx+1);
                            sumNW += grayscale.getRGB(x+dx,y+dy)*northWest.get(dy+1).get(dx+1);
                        }
                    }
                    int newGray =toPositive(sumN)+toPositive(sumE)+toPositive(sumS)+toPositive(sumW)+toPositive(sumNE)+toPositive(sumSE)+toPositive(sumSW)+toPositive(sumNW);
                    bitmap.setRGB(x, y, newGray);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setGrayscale();
    }

    private int manipulatePixel(int pixel, int addition) {
        int newPixel;

        int r = MyImage.getR(pixel) + addition;
        int g = MyImage.getG(pixel) + addition;
        int b = MyImage.getB(pixel) + addition;

        r = Math.min(Math.max(0, r), 255);
        g = Math.min(Math.max(0, g), 255);
        b = Math.min(Math.max(0, b), 255);

        newPixel = MyImage.makeRGB(r, g, b);
        return newPixel;
    }

   public void binaryConvert(BinaryConverter bC){
       MyImage newGray= bC.convertImage(this.bitmap());
       this.bitmap = newGray;
//       for(int y=1; y<bitmap.getHeight()-1; y++) {
//           for (int x = 1; x < bitmap.getWidth() - 1; x++) {
//               int val = grayscale.getRGB(x,y);
//               bitmap.setRGB(x,y,val);
//           }
//       }
   }

    public void binaryConvertInvers(BinaryConverter bC){
        this.bitmap = bC.convertImageInvers(this.bitmap());
//        for(int y=1; y<bitmap.getHeight()-1; y++) {
//            for (int x = 1; x < bitmap.getWidth() - 1; x++) {
//                int val = grayscale.getRGB(x,y);
//
//                Color c = new Color(val,val,val);
//                bitmap.setRGB(x,y,c.getRGB());
//            }
//        }
    }

//    public void skeletonize(){
//        ZhangSuenSkeletonizer skeletonizer = new ZhangSuenSkeletonizer(grayscale);
//        skeletonizer.skeletonize();
//        for(int y=1; y<bitmap.getHeight()-1; y++) {
//            for (int x = 1; x < bitmap.getWidth() - 1; x++) {
//                int val = grayscale.getRGB(x,y);
//
//                Color c = new Color(val,val,val);
//                bitmap.setRGB(x,y,c.getRGB());
//            }
//        }
//    }

//    public void extractFeature(){
//        FeatureFinder featureFinder = new FeatureFinder(this.grayscale);
//        ArrayList<Feature> features = featureFinder.findFeatures();
//        featuresNum = features.size();
//        for(int y=0; y<bitmap.getHeight(); y++) {
//            for (int x = 0; x < bitmap.getWidth(); x++) {
//                Color c = new Color(0,0,0);
//                bitmap.setRGB(x,y,c.getRGB());
//            }
//        }
//        for(int i = 0; i<features.size(); i++) {
//            printFeatures(features.get(i));
//        }
//    }
//
//    public void printFeatures(Feature feature) {
//        for(int i =0; i<feature.pixelPosSize(); i++) {
//            Color c = new Color(255,255,0);
//
//            bitmap.setRGB(feature.getPixelPosElmt(i).x,feature.getPixelPosElmt(i).y,c.getRGB());
//        }
//    }

    private  int toPositive(int a){
        return Math.max(a,-1*a);
    }

}
