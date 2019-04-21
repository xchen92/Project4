import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class Game extends JPanel implements KeyListener, ActionListener {
	private int height, width;
	private Timer t = new Timer(5, this);
	private Timer t2 = new Timer(1000, new TimerHandeler());
	private boolean first;
	private HashSet<String> keys = new HashSet<String>();
	// pad
	private double SPEED = 4, vx=1, vy=1;
	private int padH = 10, padW = 140;
	Random rand = new Random();
	private int blockX = 200, blockY = 200;
	private int block2X = 400, block2Y = 300;
	private int block3X = 600, block3Y = 200;
	private int bottomPadX;
	private int inset = 10;
	// ball
	private double angle = 35*Math.PI/180;
	private double ballX, ballY, ballSize = 20;
	// score
	private int scoreBottom, lev=1, countDown = 20;
	double time=0;
	int lives=3;
	
	public static Clip clp;
	
	JLabel L1 = new JLabel("Press left or right key to start the game!");
	
	public Game() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		first = true;
		t.setInitialDelay(100);//after timer started delay before the first event
		add(L1,BorderLayout.NORTH);
		

	}
	protected class TimerHandeler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			countDown--;
			repaint();
			if(countDown==0){
				t2.stop();

			}
		}
	}
	
		
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		height = getHeight();
		width = getWidth();
		// initial positioning
		if (first) {
			bottomPadX = width / 2 - padW / 2;
			ballX = 20;
			ballY = 20;
			//ballX = width / 2 - ballSize / 2;
			//ballY = height / 3 - ballSize / 2;
			first = false;
		}
		// bottom pad
		Rectangle2D bottomPad = new Rectangle(bottomPadX, height - padH - inset, padW, padH);
		g2d.fill(bottomPad);

		// ball
		Ellipse2D ball = new Ellipse2D.Double(ballX, ballY, ballSize, ballSize);
		g2d.fill(ball);

		// scores
		String score = "SCORE: " + new Integer(scoreBottom).toString();
		g2d.drawString(score, 10, height / 2);
		
		// lives
		String life = "Number of lives: " + new Integer(lives).toString();
		g2d.drawString(life, 10, height / 2+15);
		
		// Level
		String level = "LEVEL: " + new Integer(lev).toString();
		g2d.drawString(level, 10, height / 25);
		
		// countDown
		String count = "time remaining: " + new Integer(countDown).toString();
		g2d.drawString(count, 630, 30);
		
		
		// Block1
		Rectangle2D block = new Rectangle(blockX, blockY, 75, 75);
		g2d.fill(block);
		
		Rectangle2D block2 = new Rectangle(block2X, block2Y, 50, 50);
		g2d.fill(block2);
		
		Rectangle2D block3 = new Rectangle(block3X, block3Y, 100, 100);
		g2d.fill(block3);
		
		// Game over
		if(lives<=0) {
			String over = "Game Over! Your final score is: " + new Integer(scoreBottom).toString();
			g2d.drawString(over, width/2-100, height / 20);
			String again = "Press left or right to start another game!";
			g2d.drawString(again, width/2-100, height / 20+20);
			velX=3*Math.cos(angle);
			velY=0.7;
			ballX = 20;
			ballY = 20;
			t.stop();
			t2.stop();
			countDown = 20;
			lives = 3;
			scoreBottom = 0;
		}
		if(countDown==20&&lev<=3&&lives!=0&&velY!=0.7) {
			String nextLev = "Next Level: " + new Integer(lev).toString() + ", ball speed will be faster! Get ready!";
			g2d.drawString(nextLev, width/2-100, height / 20);
		}
		if(lev>3&&lives!=0) {
			String Beat = "You have beaten the game! Your final score is: "+ new Integer(scoreBottom).toString();
			g2d.drawString(Beat, width/2-100, height / 20);
			String again = "Press left or right to start another game!";
			g2d.drawString(again, width/2-100, height / 20+20);
			scoreBottom = 0;
		}

	}
	double velX=3*Math.cos(angle), velY=0.7;
	
	public void actionPerformed(ActionEvent e) {
		double g=0.0098;
		velY+=g;
		//block1
		if (ballY - blockY + 20 <= 3 && velY > 0 && ballY - blockY+20 >= 0) {
			if (ballX + ballSize >= blockX && ballX <= blockX + 75) {
				velY = -velY;
			}
		}
		if (ballY - blockY - 75 <= 3 && velY < 0 && ballY - blockY - 75 >=0) {
			if (ballX + ballSize >= blockX && ballX <= blockX + 75) {
				velY = -velY;
			}
		}
		if (ballX - blockX - 75 <= 3 && ballX - blockX - 75 >=0) {
			if (ballY + ballSize >= blockY && ballY <= blockY + 75) {
				velX = -velX;
			}
		}
		if (ballX - blockX +20 <= 3  && ballX - blockX +20 >=0) {
			if (ballY + ballSize >= blockY && ballY <= blockY + 75) {
				velX = -velX;
			}
		}
		
		//block2
				if (ballY - block2Y + 20 <= 3 && velY > 0 && ballY - block2Y+20 >= 0) {
					if (ballX + ballSize >= block2X && ballX <= block2X + 50) {
						velY = -velY;
					}
				}
				if (ballY - block2Y - 50 <= 3 && velY < 0 && ballY - block2Y - 50 >=0) {
					if (ballX + ballSize >= block2X && ballX <= block2X + 50) {
						velY = -velY;
					}
				}
				if (ballX - block2X - 50 <= 3 && ballX - block2X - 50 >=0) {
					if (ballY + ballSize >= block2Y && ballY <= block2Y + 50) {
						velX = -velX;
					}
				}
				if (ballX - block2X +20 <= 3  && ballX - block2X +20 >=0) {
					if (ballY + ballSize >= block2Y && ballY <= block2Y + 50) {
						velX = -velX;
					}
				}
			
				//block3
				if (ballY - block3Y + 20 <= 3 && velY > 0 && ballY - block3Y+20 >= 0) {
					if (ballX + ballSize >= block3X && ballX <= block3X + 100) {
						velY = -velY;
					}
				}
				if (ballY - block3Y - 100 <= 3 && velY < 0 && ballY - block3Y - 100 >=0) {
					if (ballX + ballSize >= block3X && ballX <= block3X + 100) {
						velY = -velY;
					}
				}
				if (ballX - block3X - 100 <= 3 && ballX - block3X - 100 >=0) {
					if (ballY + ballSize >= block3Y && ballY <= block3Y + 100) {
						velX = -velX;
					}
				}
				if (ballX - block3X +20 <= 3  && ballX - block3X +20 >=0) {
					if (ballY + ballSize >= block3Y && ballY <= block3Y + 100) {
						velX = -velX;
					}
				}
		
		// side walls
		
		if (ballX < 0 || ballX > width - ballSize) {
			velX = -velX;
		}

		// top / down walls
		if (ballY < 0) {
			velY = -velY;

		}

		if (ballY + ballSize > height) {
			ballX = 20;
			ballY = 20;
			lives-=1;
			t.stop();
			t2.stop();
			
			if(lev==1) {
				velX=3*Math.cos(angle);
				velY=0.7;
			}else if(lev==2) {
				velX=3*Math.cos(angle)+0.2;
				velY=0.7+0.2;
			}else if(lev==3) {
				velX=3*Math.cos(angle)+0.4;
				velY=0.7+0.4;
			}
		}
		
		//beaten the game
		if(lev>3) {
			velX=3*Math.cos(angle);
			velY=0.7;
			ballX = 20;
			ballY = 20;
			lev=1;
			lives = 3;
			countDown=20;
			t.stop();
			t2.stop();
			blockX = 100+rand.nextInt(300);
			blockY = 200+rand.nextInt(50);
			block2X = 100+rand.nextInt(200);
			block2Y = 300+rand.nextInt(80);
			block3X = 600+rand.nextInt(50);
			block3Y = 200+rand.nextInt(100);
		}
		
		if(countDown<=0) {
			velX=3*Math.cos(angle);
			velY=0.7;
			ballX = 20;
			ballY = 20;
			lev+=1;
			velX+=0.2; //increase speed for each level
			velY+=0.2;
			countDown=20;
			t.stop();
			t2.stop();
			blockX = 100+rand.nextInt(300);
			blockY = 200+rand.nextInt(50);
			block2X = 100+rand.nextInt(200);
			block2Y = 300+rand.nextInt(80);
			block3X = 600+rand.nextInt(50);
			block3Y = 200+rand.nextInt(100);
		}
		

		// bottom pad
		if (ballY + ballSize >= height - padH - inset && velY > 0) {
			if (ballX + ballSize >= bottomPadX && ballX <= bottomPadX + padW) {
				++ scoreBottom;
				velY = -velY;

				
			}
		}
		ballX += velX;
		ballY += velY;

		// pressed keys
		if (keys.size() == 1) {
			if (keys.contains("LEFT")) {
				bottomPadX -= (bottomPadX > 0) ? SPEED : 0;
			}
			else if (keys.contains("RIGHT")) {

				bottomPadX += (bottomPadX < width - padW) ? SPEED : 0;
			}
		}
		time += 0.0000001;
		repaint();

	}


	@Override

	public void keyTyped(KeyEvent e) {}



	@Override

	public void keyPressed(KeyEvent e) {
		t.start();
		t2.start();
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			keys.add("LEFT");
			break;

		case KeyEvent.VK_RIGHT:
			keys.add("RIGHT");
			break;

		}

	}

	@Override

	public void keyReleased(KeyEvent e) {

		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			keys.remove("LEFT");
			break;

		case KeyEvent.VK_RIGHT:
			keys.remove("RIGHT");
			break;
		}

	}
	
	public static void sound(File s) { //method to play sounds 
		
		try {
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(s);              
	         clp = AudioSystem.getClip();
	         clp.open(audioIn);
	         clp.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }

	}
	public static void main(String[] args) {
		JFrame test = new JFrame("Game");
		Game graph = new Game();
		
		test.add(graph);
		test.setSize(800, 800);
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setVisible(true);
	}

}
