package farthestpair;

import javax.swing.JFrame;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class FarthestPair extends JFrame {
     int numPoints = 500;
     Point2D[] S = new Point2D[ numPoints ]; //the set S
     Point2D[] farthestPair = new Point2D[ 2 ]; //the two points of the farthest pair
     ArrayList<Point2D> convexHull = new ArrayList(); //the vertices of the convex hull of S
     int L = convexHull.size(); //size of convex hull
     Color convexHullColour = Color.white;
     Color genericColour = Color.yellow;
   
    //fills S with random points
    public void makeRandomPoints() {
        Random rand = new Random();
 
        for (int i = 0; i < numPoints; i++) {
            int x = 50 + rand.nextInt(700);
            int y = 50 + rand.nextInt(700);
            S[i] = new Point2D( x, y );            
        }        
    }
    public void paint(Graphics g) {        
        //draw the points in S
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 800);
        g.setColor(genericColour);
        for (int i = 0; i < S.length; i++) {
            g.fillOval((int)S[i].x - 2, (int)S[i].y - 2, 4, 4);
        }
        //draw the sides of the polygon containing the points in the convex hull
        g.setColor(convexHullColour);
        for (int i = 1; i <= convexHull.size(); i++) {
            g.drawLine((int)convexHull.get(i%L).x, (int)convexHull.get(i%L).y, (int)convexHull.get((i-1)%L).x, (int)convexHull.get((i-1)%L).y);
        }
        //draw a red line connecting the farthest pair
        g.setColor(Color.red);
        g.drawLine((int)farthestPair[0].x, (int)farthestPair[0].y, (int)farthestPair[1].x, (int)farthestPair[1].y);
    }
    
    //finds convex hull
    public void findConvexHull() {
        int min = 0;
        for (int i = 0; i < S.length; i++) { //find lowest point of graph
            if (S[i].y > S[min].y) {
                min = i;
            }
        }
        convexHull.add(S[min]);
        Vector v = new Vector(1,0);
        Point2D curr;
        int num = 0;
        while(true) { //uses gift-wrapping algorithm to find convex hull
            min = 0;
            curr = convexHull.get(num);
            for (int i = 0; i < S.length; i++) {
                if (v.getAngle(curr.subtract(S[i]))<v.getAngle(curr.subtract(S[min])) && !curr.equals(S[i])) {
                        min = i;
                        
                }
            }
            if (S[min].equals(convexHull.get(0))) { //breaks loop when the first point of convex hull is reached
                break;
            }
            v = curr.subtract(S[min]);
            convexHull.add(S[min]);
            num++;
            L = convexHull.size();
        }
    }
    //uses rotating calipers algorithm to find farthest pair
    public void findFarthestPair_EfficientWay() {
        Vector v1 = new Vector(-1,0);
        Vector v2 = new Vector(1,0);
        Vector v3;
        Vector v4;
        int min = 0;
        int max = 0;
        double test;
        for (int i = 1; i < L; i++) { //finds lowest and highest point on graph
            test = convexHull.get(i).y;
            if (test < convexHull.get(min).y) {
                min = i;
            }
            else if (test > convexHull.get(max).y) {
                max = i;
            }
        }
        int end = max;
        test = 0;
        double dist;
        Point2D p1 = convexHull.get(min);
        Point2D p2 = convexHull.get(max);
        while(min != end) {//rotating calipers
            v3 = p1.subtract(convexHull.get((min+1)%L));
            v4 = p2.subtract(convexHull.get((max+1)%L));
            if (v1.getAngle(v3)<v2.getAngle(v4)) {
                v1 = v3;
                v2 = new Vector(-1*v1.xComponent,-1*v1.yComponent);
                min = (min+1)%L;
            }
            else {
                v2 = v4;
                v1 = new Vector(-1*v2.xComponent,-1*v2.yComponent);
                max = (max+1)%L;
            }
            p1 = convexHull.get(min);
            p2 = convexHull.get(max);
            dist = Math.sqrt(Math.pow(p1.x-p2.x,2)+Math.pow(p1.y-p2.y,2));
            if (dist > test) {
                test = dist;
                farthestPair[0] = p1;
                farthestPair[1] = p2;
            }
        }
    }
    //finds farthest pair using stupid brute force way
    public void findFarthestPair_BruteForceWay() {
        double longDist = 0;double dist;
        double x1;double y1;
        Point2D p;
        Point2D p2;
        for (int i = 0; i < L; i++) {
            p = convexHull.get(i);
            x1 = p.x;
            y1 = p.y;
            for (int j = i+1; j < L; j++) {
                p2 = convexHull.get(j);
                dist = Math.sqrt(Math.pow(p2.x-x1, 2)+Math.pow(p2.y-y1, 2));
                if (dist > longDist) {
                    farthestPair[0] = p;
                    farthestPair[1] = p2;
                    longDist = dist;
                }
            }
        }
    }
    
   
    public static void main(String[] args) {

        //no changes are needed in main().  Just code the blank methods above.
        
        FarthestPair fpf = new FarthestPair();
        
        fpf.setBackground(Color.BLACK);
        fpf.setSize(800, 800);
        fpf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fpf.makeRandomPoints();
        fpf.findConvexHull();
        fpf.findFarthestPair_EfficientWay();
        fpf.setVisible(true); 
    }
}
