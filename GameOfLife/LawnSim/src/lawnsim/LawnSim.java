package lawnsim;
import java.io.IOException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class LawnSim extends JFrame {
    
    //Variables
    Random r = new Random();
    static int frame;
    int screenLength = 800;
    int screenWidth = 800;
    int gridLength = 200;
    int cellDimen = screenLength / gridLength;
    int[][] alive = new int[gridLength][gridLength];
    int[][] aliveNext = new int[gridLength][gridLength];
    int[][] weeds = new int[gridLength][gridLength];
    int[][] fireLife = new int[gridLength][gridLength];
    
    //Randomly generate warm colours to get a flicker
    public Color fireColor() {
        int num = r.nextInt(4);
        switch (num) {
            case 0:
                return Color.red;
            case 1:
                return Color.orange;
            case 2:
                return Color.yellow;
            default:
                return Color.red;
        }
    }
    
    //When called, starts a fire on a random pixel on screen
    public void spawnFire(){
        aliveNext[r.nextInt(gridLength)][r.nextInt(gridLength)] = 6;
    }
    
    //Determine what a cell will become in the next frame
    public void getNext() {
        for (int i = 0; i < alive.length; i++) {
            for (int j = 0; j < alive.length; j++) {
                if (alive[i][j] != 6 && alive[i][j] != 7) {
                    if (check(true,1,6,i,j) == 0) {
                        if (check(true,1,4,i,j) == 0) {
                            if (spawnWeed(i,j) == false) {
                                if (check(true,2,5,i,j) == 0) {
                                    if (check(true,3,5,i,j) == 0) {
                                        aliveNext[i][j] = 1;//light green
                                    }
                                    else {
                                        aliveNext[i][j] = 2;//mid green
                                    }
                                }
                                else{
                                    aliveNext[i][j] = 3;//dark green
                                }
                            }
                            else {
                                aliveNext[i][j] = 4;//yellow
                            }
                        }
                        else {
                            aliveNext[i][j] = 5;//brown
                        }
                    }
                    else {
                        if (r.nextBoolean()) {
                            aliveNext[i][j] = 6; //orange,red,yellow
                            fireLife[i][j] = 20; //fire lasts for 20 frames
                        }
                    }
                }
                else{
                    if (fireLife[i][j] == 0) {
                        aliveNext[i][j] = 7; //black
                    }
                }
            }
        }
    }
    
    //Spawns weeds if conditions are met
    public boolean spawnWeed(int i,int j) {
        if (weeds[i][j] > 0) { //checks if cell already is a weed
            return true;
        }
        if (check(true,3,4,i,j) > 0 && check(false,1,4,i,j) < 1) { //checks current frame for nearby weeds (increase growing chance), checks next frame so weeds dont grow side by side
            if (r.nextInt(200)== 1) {
                weeds[i][j] = 20;
                return true;
            }
            else {
                return false;
            }
        }
        else if (r.nextInt(500000)==1 && check(false,1,4,i,j) < 1) { //weeds have a small chance to spawn on any cell
            weeds[i][j] = 20;
            return true;
        }
        else {
            return false;
        }
    }
    
    //Depreciates weeds life and fire length
    public void Life() {
        for (int i = 0; i < weeds.length; i++) {
            for (int j = 0; j < weeds.length; j++) {
                if (weeds[i][j] > 0) {
                    weeds[i][j] --;
                }
                if (fireLife[i][j] > 0) {
                    fireLife[i][j] --;
                }
                
            }
            
        }
    }
    
    //counts the number of specified cells nearby
    public int check(boolean field,int size,int type,int a, int b) {
        int count = 0;
        int xmax = size + 1,xmin = -1 * size,ymax = size + 1,ymin = -1 * size;
        if (a + size >= gridLength) {
            xmax = gridLength - a;
        }
        if (b + size >= gridLength) {
            ymax = gridLength - b;
        }
        if (a - size <= 0) {
            xmin = 0 - a;
        }
        if (b - size <=0) {
            ymin = 0 - b;
        }
        if (field == true) {
            for (int i = xmin; i < xmax; i++) {
                for (int j = ymin; j < ymax; j++) {
                    if (alive[a + i][b + j] == type) {
                        count ++;
                    }
                }
            }
        }
        else { //use this so that weeds dont grow next to each other (debug)
            for (int i = xmin; i < xmax; i++) {
                for (int j = ymin; j < ymax; j++) {
                    if (aliveNext[a + i][b + j] == type) {
                        count ++;
                    }
                }
            }
        }
        if (alive[a][b] == type) { //Does not count the original cell
            count --;
        }
        return count;
    }
    
    //Overwrites current cells with cells of next frame
    public void overWrite() {
        for (int i = 0; i < alive.length; i++) {
            System.arraycopy(aliveNext[i], 0, alive[i], 0, alive.length);   
        }
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
        int x=0, y;

        for (int i = 0; i < gridLength; i++) {
            y = 0;
            for (int j = 0; j < gridLength; j++) {
                
                switch (alive[i][j]) {
                    case 1:
                        g2.setColor(Color.green);//light green grass
                        break;
                    case 2:
                        g2.setColor(new Color(0,230,0));//mid green grass
                        break;
                    case 3:
                        g2.setColor(new Color(0,180,0));//dark green grass
                        break;
                    case 4:
                        g2.setColor(Color.yellow);//yellow weeds
                        break;
                    case 5:
                        g2.setColor(new Color(102,102,0));//brown dirt
                        break;
                    case 6:
                        g2.setColor(fireColor());//fire
                        break;
                    default:
                        g2.setColor(Color.black);//ash
                        break;
                }
                
                g2.fillRect(x, y, cellDimen, cellDimen);
                //g2.setColor(Color.black);
                //g2.drawRect(x, y, cellDimen, cellDimen);
                
                y += cellDimen;
            }
            x += cellDimen;
        }
        return bi;
    }

    //sets up the JFrame screen
    public void initializeWindow() {
        setTitle("Lawn Simulator");
        setSize(screenWidth, screenLength);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.white);
        setVisible(true);
    }
    
    //positions cells randomly
    public void setRandom() {
        for (int[] alive1 : alive) {
            for (int j = 0; j < alive.length; j++) {
                alive1[j] = r.nextInt(4) + 1;
            }
        }
    }
    
    //sets up alive array
    public void first() {
        for (int i = 0; i < alive.length; i++) {
            for (int j = 0; j < alive.length; j++) {
                alive[i][j] = 1;
                weeds[i][j] = 0;
            }
        }
    }
    
    //main method
    public static void main(String args[]) throws IOException {
        LawnSim gs = new LawnSim();
        gs.first();
        gs.initializeWindow();
        while(true) {
            frame ++;
            gs.Life();
            gs.getNext(); 
            if (frame == 350) {//Spawns fire at frame 350
                gs.spawnFire();
            }
            if (frame > 400) {//Starts counting the number of ash cells after frame 400
                int number = 0;
                for (int i = 0; i < gs.alive.length; i++) {
                    for (int j = 0; j < gs.alive.length; j++) {
                        if (gs.alive[i][j] == 7) {
                            number++;
                        }
                    }
                }
                if (number >= Math.pow(gs.gridLength, 2)) {
                    gs.setVisible(false);
                    gs.dispose();
                    break;//if entire screen is black, end program
                }
            }
            gs.overWrite();     
            gs.sleep(150);                
            gs.repaint();               

        }
    }
}