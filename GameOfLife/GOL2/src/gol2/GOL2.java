package gol2;
import java.io.IOException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*;

public class GOL2 extends JFrame {
    //Variables
    int screenLength = 800;
    int screenWidth = 800;
    int gridLength = 200;
    int cellDimen = screenLength / gridLength;
    boolean[][] alive = new boolean[gridLength][gridLength];
    boolean[][] aliveNext = new boolean[gridLength][gridLength];
    
    //See if a certain cell will be alive in the next frame
    public void getNext() {
        for (int i = 0; i < alive.length; i++) {
            for (int j = 0; j < alive.length; j++) {
                if (alive[i][j] == true) {
                    aliveNext[i][j] = localCheck(i,j) == 3 || localCheck(i,j) == 4;
                }
                else {
                    aliveNext[i][j] = localCheck(i,j) == 3;
                }
            }  
        }
    }
    
    //Overwrites current cells with cells of next frame
    public void overWrite() {
        for (int i = 0; i < alive.length; i++) {
            System.arraycopy(aliveNext[i], 0, alive[i], 0, alive.length);   
        }
    }

   //Counts number of alive cells around a cell
   public int localCheck(int a, int b) {
        int count = 0;
        int xmax = 2,xmin = -1,ymax = 2,ymin = -1;
        if (a == gridLength-1) {
            xmax = 1;
        }
        if (b == gridLength-1) {
            ymax = 1;
        }
        if (a == 0) {
            xmin = 0;
        }
        if (b == 0) {
            ymin = 0;
        }
        for (int i = xmin; i < xmax; i++) {
            for (int j = ymin; j < ymax; j++) {
                if (alive[a + i][b + j] == true) {
                    count ++;
                }
            }
        }
        return count;
    }
   
    //pauses frame so we can see each frame
    public static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } 
        catch (Exception e) {}
    }
    
    //draws the buffered image
    public void paint(Graphics g) {
        Image img = getImage();
        g.drawImage(img, 0, 0, rootPane);
    }
    
    //preloads an image to be drawn so there is no flicker
    public Image getImage() {
        BufferedImage bi = new BufferedImage(1000,1000,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        int x, y;
        x = 0;

        for (int i = 0; i < gridLength; i++) {
            y = 0;
            for (int j = 0; j < gridLength; j++) {
                
                if (alive[i][j]) {
                    g2.setColor(Color.blue);
                }
                
                else {
                    g2.setColor(Color.white);
                }
                
                g2.fillRect(x, y, cellDimen, cellDimen);
                g2.setColor(Color.green);
                g2.drawRect(x, y, cellDimen, cellDimen);
                
                y += cellDimen;
            }
            x += cellDimen;
        }
        return bi;
    }

    //sets up the JFrame screen
    public void initializeWindow() {
        setTitle("Conway's Game of Life");
        setSize(screenWidth, screenLength);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);
        setVisible(true);
    }
    
    //positions cells randomly
    public void setRandom() {
        Random r = new Random();
        for (int i = 0; i < alive.length; i++) {
            for (int j = 0; j < alive.length; j++) {
                alive[i][j] = r.nextBoolean();
            }
        }
    }
    
    //positions cells for cool pattern 
    public void setPattern() {
        for (int i = 0; i < alive.length; i++) {
            for (int j = 0; j < alive.length; j++) {
                if (j % 15 == 0) {
                    alive[i][j] = true;
                }
                else {
                    alive[i][j] = false;
                }
            }
        }
    }
    
    //main method
    public static void main(String args[]) throws IOException {
        GOL2 gs = new GOL2();
        gs.setPattern();
        gs.initializeWindow();
        
        while(true) { 
            gs.getNext();   
            gs.overWrite();     
            gs.sleep(100);                
            gs.repaint();               

        }
    }
}