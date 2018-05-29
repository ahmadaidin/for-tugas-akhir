package experiment;


import java.awt.Point;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 05/10/2016.
 */

public class FeatureExtractor {

    private int[][] grayscale;
//    private final int maxRadius=5;
//    private final Point[][] nearPixel = generateNearPixelMtx(maxRadius);
    public FeatureExtractor(MyImage grayImage) {
        grayscale = ImgFilterer.getGrayscale(grayImage);
        System.out.println(grayscale.length+" "+grayscale[0].length);
    }

    public static final int VISITED_PIXEL = 200;



//    public FeatureExtractor(int[][] grayscale) {
//        this.grayscale = grayscale;
//    }

//    public void setGrayscale(ArrayList<int[]> grayscale) {
//        this.grayscale = grayscale;
//    }




    public void iterate(ArrayList<Feature> features, Point iterator){
        setVisited(iterator);
        Feature paths = findPath(iterator);
        for(int i = 0; i<paths.pixelPosSize(); i++) {
            Feature feature = new Feature();
            feature.addPixelPos(iterator);
            feature.addDirection(paths.getDirectionElmt(i));
            Point start = new Point(paths.getPixelPosElmt(i));
            addPixelToFeature(feature, start);
            if(feature.pixelPosSize()!=0){
                features.add(feature);
            }
        }
    }

    public void addPixelToFeature(Feature feature, Point iterator) {
        feature.addPixelPos(iterator);
        Feature paths = findPath(iterator);
        while(paths.pixelPosSize()==1) {
            setVisited(iterator);
            iterator = new Point(paths.getPixelPosElmt(0));
            feature.addDirection(paths.getDirectionElmt(0));
            feature.addPixelPos(paths.getPixelPosElmt(0));
//            paths = findPath(iterator);
//            int radius = 2;
//            if(paths.pixelPosSize()==0) {
//                boolean found = false;
//                while (!found && radius<=maxRadius) {
//                    int start = maxRadius-radius;
//                    int end = start + 2* radius;
//                    for (int i = start; i <= end; i++) {
//                        int j = start;
//                        while (j<=end){
//                            int newX = iterator.x + nearPixel[i][j].x;
//                            int newY = iterator.y + nearPixel[i][j].y;
//                            if (newY < 0 || newY >= grayscale.size())
//                                continue;
//                            if (newX < 0 || newX >= grayscale.get(newY).length)
//                                continue;
//                            if (grayscale.get(newY)[newX] == 255) {
//                                found = true;
//                                Point p1 = new Point(iterator);
//                                Point p2 = new Point(newX, newY);
//                                Line line = new Line(p1, p2);
//                                ArrayList<Point> linePoints = line.getAllPoints();
//                                for (int k = 1; k < linePoints.size(); k++) {
//                                    grayscale.get(linePoints.get(k).y)[linePoints.get(k).x] = 255;
//                                }
//                            }
//                            if (i !=start && i !=end)
//                                j+=radius*2;
//                            else j++;
//                            //if(found) break;
//                        }
//                        // if(found) break;
//                    }
//                    radius++;
//                }
//                if (!found){
//
//                }
//                    Log.d("tidak ketemu","radius: "+radius);
//            }
            paths = findPath(iterator);
        }
    }

    public void setVisited(Point p) {
        grayscale[p.y][p.x]=VISITED_PIXEL;
    }

//    public Point[][] generateNearPixelMtx(int maxRadius){
//        int size = maxRadius*2+1;
//        Point[][] nearPixel = new Point[size][size];
//        for(int i = 0; i<size; i++){
//            for(int j = 0; j<size;j++) {
//                Point p = new Point(j-maxRadius,i-maxRadius);
//                nearPixel[i][j] = p;
//            }
//        }
//        return nearPixel;
//    }

    public Point getIterator(Point lastIterator){
        Point iterator = new Point(lastIterator);
        while(iterator.y<grayscale.length){
            while(iterator.x<grayscale[0].length){
                if(grayscale[iterator.y][iterator.x]==255) {
                    return iterator;
                } else iterator.x++;
            }
            iterator.x = 0;
            iterator.y++;
        }
        return lastIterator;
    }

    public ArrayList<Feature> findFeatures() {
        ArrayList<Feature> features = new ArrayList<>();
        Point lastIterator = new Point(0,0);
        Point iterator = getIterator(lastIterator);
        while (!iterator.equals(lastIterator)){
            setVisited(iterator);
            lastIterator.setLocation(iterator.x, iterator.y);
            iterate(features,iterator);
            iterator = getIterator(lastIterator);
        }
        return features;
    }

    public Feature findPath(Point iterator) {
        Feature paths = new Feature();

        Point searcher;
        if(iterator.y>0 && iterator.x>0)
        if(grayscale[iterator.y-1][iterator.x-1]==255){
            searcher = new Point(iterator.x-1,iterator.y-1);
            paths.addPixelPos(searcher);
            paths.addDirection(1);
        }

        if(iterator.y>0)
        if (grayscale[iterator.y-1][iterator.x]==255 ) {
            searcher = new Point(iterator.x,iterator.y-1);
            paths.addPixelPos(searcher);
            paths.addDirection(2);
        }

        if(iterator.y>0 && iterator.x<grayscale[0].length)
        if (grayscale[iterator.y-1][iterator.x+1]==255 ) {
            searcher = new Point(iterator.x+1,iterator.y-1);
            paths.addPixelPos(searcher);
            paths.addDirection(3);
        }

        if(iterator.x<grayscale[0].length)
        if (grayscale[iterator.y][iterator.x+1]==255 ) {
            searcher = new Point(iterator.x+1,iterator.y);
            paths.addPixelPos(searcher);
            paths.addDirection(4);
        }

        if(iterator.y<grayscale.length && iterator.x < grayscale[0].length)
        if (grayscale[iterator.y+1][iterator.x+1]==255) {
            searcher = new Point(iterator.x+1,iterator.y+1);
            paths.addPixelPos(searcher);
            paths.addDirection(5);
        }

        if(iterator.y<grayscale.length)
        if (grayscale[iterator.y+1][iterator.x]==255) {
            searcher = new Point(iterator.x,iterator.y+1);
            paths.addPixelPos(searcher);
            paths.addDirection(6);
        }

        if(iterator.y<grayscale.length && iterator.x>0)
        if (grayscale[iterator.y+1][iterator.x-1]==255 ) {
            searcher = new Point(iterator.x-1,iterator.y+1);
            paths.addPixelPos(searcher);
            paths.addDirection(7);
        }

        if(iterator.x>0)
        if (grayscale[iterator.y][iterator.x-1]==255) {
            searcher = new Point(iterator.x-1,iterator.y);
            paths.addPixelPos(searcher);
            paths.addDirection(8);
        }
        return paths;
    }

    public ArrayList<Feature> cleareNoise(ArrayList<Feature> features){
        ArrayList<Feature> result = new ArrayList<>();
        for(int i=0; i<features.size(); i++){
            if(features.get(i).directionSize()>0){
                result.add(features.get(i));
            }
        }
        return result;
    }

}
