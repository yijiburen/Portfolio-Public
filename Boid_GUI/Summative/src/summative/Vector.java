/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package summative;

public class Vector {
    
    public double xPart, yPart;
    
    public Vector(double x, double y) {
        this.xPart = x;
        this.yPart = y;
    }
    
    //needed as part of the convex hull algorithm and for finding the farthest pair within the vertices of the convex hull
    public Vector add( Vector other ) {
        return new Vector( this.xPart + other.xPart, this.yPart + other.yPart);
    }
    
    public Vector subtract( Vector other ) {
        return new Vector( this.xPart - other.xPart, this.yPart - other.yPart);
    }
    
    public Vector divide(double num) {
        xPart /= num;
        yPart /= num;
        return this;
    }
    
    //needed as part of the convex hull algorithm and for finding the farthest pair within the vertices of the convex hull
    public double getAngle( Vector other ) {  
        double vDotw = this.dotProduct( other );
        double magV = this.magnitude();
        double magW = other.magnitude();
        
        return Math.acos( vDotw / (magV*magW) );
    }
    
    //only used inside getAngle()
    private double dotProduct( Vector other ) {
        return this.xPart*other.xPart + this.yPart*other.yPart;
    }
    
    //only used inside getAngle()
    public double magnitude() {
        return Math.sqrt( this.xPart*this.xPart + this.yPart*this.yPart );
    }
}
