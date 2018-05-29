package experiment;


import java.awt.Point;

import java.util.ArrayList;

public class Feature {
    private ArrayList<Integer> direction;
    private ArrayList<Point> pixelPos;

    public Feature(){
        direction = new ArrayList<>();
        pixelPos = new ArrayList<>();
    }

    public Feature(ArrayList<Integer> direction, ArrayList<Point> pixelPos) {
        this.direction = direction;
        this.pixelPos = pixelPos;
    }

    public ArrayList<Integer> getDirection() {
        return direction;
    }

    public ArrayList<Point> getPixelPos() {
        return pixelPos;
    }

    public void addDirection(Integer elmt) {
        direction.add(elmt);
    }

    public void addPixelPos(Point elmt) {
        Point p = new Point(elmt);
        pixelPos.add(p);
    }

    public Integer getDirectionElmt(int idx) {
        return direction.get(idx);
    }

    public Point getPixelPosElmt(int idx) {
        return pixelPos.get(idx);
    }

    public void setDirectionElmt(int idx, Integer elmt) {
        direction.set(idx, elmt);
    }

    public void setPixelPosElmt(int idx, Point elmt) {
        Point p = new Point(elmt);
        pixelPos.add(p);pixelPos.set(idx, p);
    }

    public int directionSize(){
        return direction.size();
    }

    public int pixelPosSize() {
        return pixelPos.size();
    }

    public void clear() {
        direction.clear();
        pixelPos.clear();
    }
}
