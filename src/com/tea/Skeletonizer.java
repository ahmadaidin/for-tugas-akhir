package com.tea;

import java.util.ArrayList;

public abstract class Skeletonizer {
    protected ArrayList<int[]> grayscale;
    public Skeletonizer(){

    }

    public Skeletonizer(ArrayList grayscale){
        this.grayscale= grayscale;
    }

    public void setGrayscale(ArrayList<int[]> grayscale) {
        this.grayscale = grayscale;
    }

    public ArrayList<int[]> getGrayscale() {
        return grayscale;
    }

}
