package BrickBreaker;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer  timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;
    private MapGenerator map;

    public GamePlay(){
        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        timer = new Timer(delay,this);
        timer.start();
    }

    public void paint(Graphics g){

        //background
        Color myBG = new Color (228, 228, 220);
        g.setColor(myBG);
        g.fillRect(1,1,700,600);

        //bricks
        map.draw((Graphics) g);

        // the scores
        g.setColor(Color.black);
        g.setFont(new Font("serif",Font.BOLD, 25));
        g.drawString(""+score, 590,30);


        //paddle
        Color myPaddle = new Color(20,44,84);
        g.setColor(myPaddle);
        g.fillRect(playerX,550,100,8);


        //ball
        Color myBall = new Color(140,29,63);
        g.setColor(myBall);
        g.fillOval(ballposX,ballposY,20,20);


        if (ballposY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.black);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: "+score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter to restart", 190, 340);
        }

        if( totalBricks == 0){
            play = false;
            ballYdir = -2;
            ballXdir = -1;
            g.setColor(Color.black);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("You won!",250,300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter to play again", 190, 340);

        }
        g.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play==true){

            if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,100,8))){
                ballYdir = -ballYdir;
            }

            A: for(int i=0 ; i< map.map.length ; i++){
                for (int j=0 ; j <map.map[0].length ; j++ ){
                    if(map.map[i][j] > 0){

                        int brickX = j*map.brickWidth + 80;
                        int brickY = i*map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect = new Rectangle (ballposX,ballposY,20,20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)){
                            map.setBrickValue(0,i,j);
                            score+=5;
                            totalBricks--;


                            if(ballposX + 19 <= brickRect.x || ballposX+1 >= brickRect.x + brickRect.width ){
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
            ballposX += ballXdir;
            ballposY += ballYdir;
            if(ballposX<0){
                ballXdir = -ballXdir;
            }
            if(ballposY<0){
                ballYdir = -ballYdir;
            }
            if(ballposX>670){
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            if (playerX>=600){
                playerX=600;
            }
            else {
                moveRight();
            }
        }
        if (e.getKeyCode()==KeyEvent.VK_LEFT){
            if (playerX<10){
                playerX=10;
            }
            else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                score = 0;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                totalBricks = 21;
                map = new MapGenerator(3,7);
                repaint();

            }
        }
    }

    public void moveRight(){
        play= true;
        playerX+=20;
    }
    public void moveLeft(){
        play= true;
        playerX-=20;
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
