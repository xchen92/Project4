import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.*;

public class Game extends JPanel implements KeyListener, ActionListener {
	private int height, width;
	private Timer t = new Timer(5, this);
	private boolean first;
	private HashSet<String> keys = new HashSet<String>();
	// pad
	private double SPEED = 1, vx=1, vy=1;
	private int padH = 10, padW = 80;
	private int bottomPadX;
	private int inset = 10;
	// ball
	private double angle = 45*Math.PI/180;
	private double ballX, ballY, ballSize = 20;
	// score
	private int scoreBottom;
	double time=0;
	
	public Game() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		first = true;
		t.setInitialDelay(100);
		t.start();
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
			ballX = width / 2 - ballSize / 2;
			ballY = height / 2 - ballSize / 2;
			first = false;
		}
		// bottom pad
		Rectangle2D bottomPad = new Rectangle(bottomPadX, height - padH - inset, padW, padH);
		g2d.fill(bottomPad);

		// ball
		Ellipse2D ball = new Ellipse2D.Double(ballX, ballY, ballSize, ballSize);
		g2d.fill(ball);

		// scores
		String scoreB = "Bottom: " + new Integer(scoreBottom).toString();
		g2d.drawString(scoreB, 10, height / 2);

	}
	double velX=1*Math.cos(angle), velY=0;
	@Override
	
	public void actionPerformed(ActionEvent e) {
		double g=0.0098;
		velY+=g;
		// side walls
		
		if (ballX < 0 || ballX > width - ballSize) {
			velX = -velX;
		}

		// top / down walls
		if (ballY < 0) {
			velY = -velY;

		}

		if (ballY + ballSize > height) {
			velY = -velY;

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
	public static void main(String[] args) {
		JFrame test = new JFrame("test");
		Game graph = new Game();
		
		test.add(graph);
		test.setSize(800, 800);
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setVisible(true);
	}

}
