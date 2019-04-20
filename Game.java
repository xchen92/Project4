import java.awt.*;
import java.awt.event.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.io.*;
import java.util.Random;
public class Game extends JFrame implements ActionListener   {

	protected Timer timer;
	protected Timer timer2;
	int speed;
	int circlespeed=10;
	protected int xdimension=150;
	protected int ydimension=10;
	protected int rectangleLocationX=0;
	protected int rectangleLocationY;
	protected GraphicCanvas drawRectangle=new GraphicCanvas();
	protected JButton button;
	protected JButton button2;
	int fontsize;
	int g=1;
	double time=0.5;
	protected int diameter = 20;
	int vx=2, vy = 0, x=10, y=100;
	protected JPanel panel;
	protected JLabel label;
	int score;
	int scorekeeper=0;
	int countdown=60;
	int numlives=3;
	Timer timer3;
	Timer timer4;
	int j;
	int height;
	protected Random random;
	protected Color c;

	String s=scorekeeper+"";
	int levels=1;
	int counter=0;
	public Game(){
		setTitle("Pong Game");

		setSize(1000,1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		drawRectangle.addKeyListener(new KeyHandeler());
		add(drawRectangle);
		rectangleLocationY=getHeight()-300;


		JPanel panel=new JPanel();
		JButton button=new JButton("Start");
		button.addActionListener(this);
		panel.add(button);

		JButton button2=new JButton("Reset");
		button2.addActionListener(new ResetHandeler());
		panel.add(button2);
		button.setFocusable(false);
		button2.setFocusable(false);
		System.out.println(height);		
		Random random=new Random();
		timer=new Timer(50,new TimerCallback());
		timer.start();
		timer2=new Timer(30, new BallHandeler());

		timer3=new Timer(1000, new TimerHandeler());
		c=Color.DARK_GRAY;


		Container contentPane = this.getContentPane();
		contentPane.add(panel, BorderLayout.NORTH);

		panel.setBackground(c);
		drawRectangle.setBackground(c);
		add(drawRectangle,BorderLayout.CENTER);

	}


	public void play(){
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Super_Mario_Medley.wav"));
			Clip clip = AudioSystem.getClip( );
			clip.open(audioInputStream);
			clip.start( );
		}
		catch(Exception e)  {
			e.printStackTrace( );
		}
	}

	protected class BallHandeler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			time += 0.01;

			vy +=g;
			System.out.println(vy);
			y += vy;


			if (y >=getHeight()) {
				vy=0;
				x=0;
				y=100;
				rectangleLocationX=0;
				rectangleLocationY=700;
				timer.stop();
				timer2.stop();
				timer3.stop();
				numlives-=1;


			}

			x+=vx;
			if(x+diameter<=0||x+diameter>getWidth())
			{

				vx*=-1;
			}



			if(y+diameter>=rectangleLocationY&&x>=rectangleLocationX&&x<rectangleLocationX+xdimension){
				scorekeeper++;

				vy += 1;
				vy*=-1;

			}


			repaint();
		}
	}

	protected class TimerHandeler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			countdown--;
			repaint();
			if(countdown==0){

				timer3.stop();

			}
		}
	}
	protected class KeyHandeler implements KeyListener{
		boolean leftPressed = false;
		boolean rightPressed = false;
		public void keyPressed(KeyEvent e) {
			int key=e.getKeyCode();
			if(key==KeyEvent.VK_LEFT){
				leftPressed=true;
				speed=-5;
			}
			else if(key==KeyEvent.VK_RIGHT){
				rightPressed=true;
				speed=5;
			}

		}

		public void keyReleased(KeyEvent e){
			int key=e.getKeyCode();
			if(key==KeyEvent.VK_LEFT){
				leftPressed=false;
				if(rightPressed){
					speed=5;
				}
				else
				{
					speed=0;
				}
			}
			else if(key==KeyEvent.VK_RIGHT){
				rightPressed=false;
				if(leftPressed){
					speed=-5;
				}
				else{
					speed=0;
				}
			}


		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}
	}

	protected class TimerCallback implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			rectangleLocationX += speed;


			if(rectangleLocationX>getWidth()-xdimension || rectangleLocationY>getHeight()-ydimension){
				rectangleLocationX=getWidth()-xdimension;
				speed=0;
			}
			else if(rectangleLocationX<0 || rectangleLocationY<0){
				rectangleLocationX=0;
				speed=0;
			}	

			repaint();
		}}
	public void actionPerformed(ActionEvent e) {
		play();
		timer2.start();
		timer3.start();

	}

	protected class ResetHandeler implements ActionListener{
		public void actionPerformed(ActionEvent e) {


			timer.start();
			timer2.start();
			timer3.start();
			repaint();
		}
	}

	public static void main(String[] args) {
		Game game=new Game();
		game.setVisible(true);

	}
	protected class GraphicCanvas extends JPanel{
		public GraphicCanvas(){
			setFocusable(true);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);


			g.setColor(Color.BLUE);
			g.fillOval(x, y, diameter, diameter);
			fontsize = 20;
			Font f = new Font("American Typewriter", Font.BOLD, fontsize);
			g.setFont(f);
			g.setColor(Color.RED);
			g.drawString(""+scorekeeper,100,100);
			g.drawString(""+countdown, 800, 100);
			g.drawString("Level:"+" "+levels,450, 100);



			for(int i=0;i<=3;i++){
				if(numlives==3){
					g.setColor(Color.GREEN);
					g.fillOval(85, 110, 10, 10);
					g.fillOval(105, 110, 10, 10);
					g.fillOval(125,110,10,10);
				}
				if(numlives==2){
					g.setColor(Color.GREEN);
					g.fillOval(105, 110, 10, 10);
					g.fillOval(125,110,10,10);
				}
				if(numlives==1){
					g.setColor(Color.GREEN);
					g.fillOval(125,110,10,10);
				}
			}

			for(int j=0;j<=3;j++){
				if(levels==1){
					g.setColor(Color.ORANGE);
					g.fillRect(rectangleLocationX, rectangleLocationY, xdimension, ydimension);

				}
				if(levels==2){
					g.setColor(Color.MAGENTA);
					g.fillRect(rectangleLocationX, rectangleLocationY, xdimension, ydimension);

				}
				if(levels==3){
					g.setColor(Color.YELLOW);
					g.fillRect(rectangleLocationX, rectangleLocationY, xdimension, ydimension);

				}

				if(countdown==0){
					counter+=1;


					if(counter==1)
					{
						scorekeeper+=5;
						levels+=1;
						xdimension=100;
						countdown=60;
						timer2.setDelay(25);
						timer3.restart();
					}
					if(counter==2){
						scorekeeper+=5;
						levels+=1;
						xdimension=75;
						countdown=60;
						timer2.setDelay(20);
						timer3.restart();
					}


				}
				if(numlives==0){
					fontsize=50;
					Font f1 = new Font("American Typewriter", Font.ITALIC, fontsize);
					g.setFont(f1);
					g.drawString("GAME OVER", getWidth()/2-200,getHeight()/2);
					g.drawString("Your final score is: "+scorekeeper, getWidth()/2-200,getHeight()/2+100);
					timer.stop();
					timer2.stop();
					timer3.stop();
				}
				if(counter>3){
					Font f2 = new Font("American Typewriter", Font.ITALIC, fontsize);
					g.setFont(f2);
					g.drawString("YOU WON", getWidth()/2-200,getHeight()/2);
					g.drawString("Your score is"+" "+scorekeeper, getWidth()/2,getHeight()/2+25);
					timer.stop();
					timer2.stop();
					timer3.stop();
				}

				repaint();

			}}


	}
}






