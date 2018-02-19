//see: http://www.vergenet.net/~conrad/boids/pseudocode.html
package summative;

import java.util.Random;

public class Boid {
    Random r = new Random();
    public double xPos, yPos;
    public Vector v;
    
    public Boid(double x, double y) {
        xPos = x;
        yPos = y;
        v = new Vector(r.nextInt(100)-50,r.nextInt(100)-50);
    }
}