package com.tea;

import java.awt.Color;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author toshiba
 */
public class MyImage {
    BufferedImage img;
    
    public MyImage(BufferedImage image) {
        img = image;
    }
            
    public MyImage(MyImage image) {
        img = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_INT_RGB);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                img.setRGB(i, j, image.getRGB(i, j));
            }
        }
    }
    
//    public Image(Image image, Square s) {
//        img = new BufferedImage(s.xmax - s.xmin + 1, s.ymax - s.ymin + 1, TYPE_INT_RGB);
//        for (int i = 0; i < this.getWidth(); i++) {
//            for (int j = 0; j < this.getHeight(); j++) {
//                img.setRGB(i, j, image.getRGB(i + s.xmin, j + s.ymin));
//            }
//        }
//    }
    
    public MyImage(int height, int width) {
        img = new BufferedImage(width, height, TYPE_INT_RGB);
    }
    
    public BufferedImage getData() {
        return img;
    }
    
    public int getHeight() {
        return img.getHeight();
    }
    
    public int getWidth() {
        return img.getWidth();
    }
    
    public int getRGB(int i, int j) {
        return img.getRGB(i, j);
    }
    
    public void setRGB(int i, int j, int c) {
        img.setRGB(i, j, c);
    }
    
    public static int getR(int rgb) {
        return (rgb >> 16) & 0xFF;                
    }
    
    public static int getG(int rgb) {
        return (rgb >> 8) & 0xFF;
    }
    
    public static int getB(int rgb) {
        return rgb & 0xFF;
    }
    
    public static int makeRGB(int r, int g, int b) {
        int rgb = r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }
    
    public static int convertGrayLevel(int rgb) {
        int gray = convertRGB(rgb, 0.2126,0.7152,0.0722);
        return makeRGB(gray,gray,gray);
    }
    
    public static int convertRGB(int rgb, double a, double b, double c) {
        return (int) (a*getR(rgb) + b*getG(rgb) + c*getB(rgb));
    }
    
    public static int RGBtoY(int rgb) {        
        return convertRGB(rgb, 0.299f, 0.587f, 0.114f);        
        //return 16 + convertRGB(rgb, 0.25, 0.5, 0.09);
    }
    
    public static int RGBtoCb(int rgb) {
        //return convertRGB(rgb, -0.169f, -0.332f, 0.5f);
        return getB(rgb) - RGBtoY(rgb);
        //return 128 + convertRGB(rgb, -0.15, -0.29, 0.44);
    }
    
    public static int RGBtoCr(int rgb) {
        //return convertRGB(rgb, 0.5f, -0.419f, -0.081f);
        return getR(rgb) - RGBtoY(rgb);
        //return 128 + convertRGB(rgb, 0.44, -0.37, -0.07);
    }
    
//    public Complex[][] toComplex() {
//        Complex[][] ret = new Complex[getWidth()][getHeight()];
//        for (int i = 0; i <getWidth(); i++) {
//            for (int j = 0; j < getHeight(); j++) {
//                ret[i][j] = new Complex(getR(getRGB(i,j)),0);
//            }
//        }
//        return ret;
//    }
//
//    public Complex[] getColumn(int j) {
//        Complex[] temp = new Complex[getHeight()];
//        for (int i = 0; i < getHeight(); i++) {
//            //System.out.println(getHeight() + " : " + j + " | " + getWidth() + " : " + i);
//            temp[i]= new Complex(getR(getRGB(j,i)),0);
//        }
//        return temp;
//    }
//
//    public Complex[] getRow(int i) {
//        Complex[] temp = new Complex[getWidth()];
//        for (int j = 0; j < getWidth(); j++) {
//            temp[j]= new Complex(getR(getRGB(j,i)),0);
//        }
//        return temp;
//    }
    
    public MyImage to2PowerSize() {
        int width = this.getWidth();
        int d = 99999;
        int two = 1;
        //System.out.println(n);
        while ( width - two*2 > 0) {
            //System.out.println("two : " + two + " | d : " + d);
            two *= 2;
            d = width - two;
        }
        width = two*2;
        int height = this.getHeight();
        d = 99999;
        two = 1;
        //System.out.println(n);
        while ( height - two*2 > 0) {
            //System.out.println("two : " + two + " | d : " + d);
            two *= 2;
            d = height - two;
        }
        height = two*2;
        
        MyImage temp = new MyImage(height, width);
        
        int dwidth = (width - this.getWidth())/2;
        int dheight = (height - this.getHeight())/2;
        
        for (int i = dwidth; i < width - dwidth; i++) {                        
            for (int j = dheight; j < height - dheight; j++) {
                temp.setRGB(i, j, this.getRGB(i - dwidth, j - dheight));
            }                         
        }
        
        return temp;
    }
    
    public MyImage cut(int width, int height) {
        MyImage temp = new MyImage(height, width);
        int dw = (this.getWidth() - width) / 2;
        int dh = (this.getHeight() - height) / 2;
        
        for (int i = 0; i < width; i++) {                        
            for (int j = 0; j < height; j++) {
                temp.setRGB(i, j, this.getRGB(i + dw, j + dh));
            }                         
        }
        
        return temp;
    }
    
    public double[][] toDoubleArray() {
        double[][] arr = new double[this.getHeight()][this.getWidth()*2];
        
        int iter = 0;
        for (int i = 0; i < this.getHeight(); i++) {
            for (int j = 0; j < this.getWidth(); j++) {
                //arr[i*2*this.getHeight()+j*2] = Image.convertGrayLevel(this.getRGB(i, j));
                //arr[i*2*this.getHeight()+j*2+1] = 0;
                arr[i][j*2] = MyImage.convertGrayLevel(this.getRGB(j, i));
                arr[i][j*2+1] = 0;
            }
        }        
        
        return arr;
    }
    public static MyImage fromDoubleArray(double[][] d){
        MyImage image = new MyImage(d.length,d[0].length/2);
        
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                double real = d[i][2*j];
                double imaj = d[i][2*j+1];
                int val = (int) Math.hypot(real, imaj);
                image.setRGB(j, i, MyImage.makeRGB(val, val, val));
            }
        }
        return image;
    }
}
