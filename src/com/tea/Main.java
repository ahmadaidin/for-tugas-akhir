package com.tea;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

public class Main{
    private static BufferedImage bmp;
    private static MyImage image;
    private static final String SOBEL = "Sobel";
    private static final String PREWIT = "Prewit";
    private static final String PREWIT_8 = "Prewit8";
    private static final String KIRSCH = "Kirsch";
    private static final String ROBINSON_3 = "Robinson3";
    private static final String ROBINSON_5 = "Robinson5";

    public static void readImage(String filepath){
        int width = 963;    //width of the image
        int height = 640;   //height of the image

        bmp = null;
        File f = null;
        try{
            f = new File(filepath); //image file path
            bmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bmp = ImageIO.read(f);
            image = new MyImage(bmp);

            System.out.println("Reading complete.");
        }catch(IOException e){
            System.out.println("Error: "+e);
        }

    }
    public static void printImage(MyImage img){
            ImageIcon icon = new ImageIcon(img.getData());
            JLabel label = new JLabel(icon);
            JOptionPane.showMessageDialog(null, label);
    }

    public static void main(String[] args) {
        // write your code here
        ImageProcessor bitmapEditor;
        int featuresNum;
        String s = "";
        File file = new File("matrix.json");
        OtsuConverter otsu;
        InputStream inputStream;
        try {
            inputStream = Files.newInputStream(file.toPath());
            Convolution c = new Convolution(inputStream);

            readImage("D:\\Image\\in.jpg");
            bitmapEditor = new ImageProcessor(image);
            bitmapEditor.smoothImage();

//            bitmapEditor.edgeDetectLv1(c,SOBEL);
//            otsu = new OtsuConverter();
           // bitmapEditor.binaryConvert(otsu);
//            System.out.println(otsu.getThreshold());
//            bitmapEditor.skeletonize();
            printImage(bitmapEditor.bitmap());

        } catch (IOException e){
            System.out.print(e.getMessage());
        };

    }
}