package breakout;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball extends ImageView{
	public static final String BALL_IMAGE = "mini.gif";
	public static final int BOUNCER_SPEED = 260;
	public static final int FRAMES_PER_SECOND = 60;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final double SLOWDOWN = .5, SPEEDUP = 2, REVERSE = -1;;
	
	private double dx, dy;
	
	public Ball(Scene scene){
		super();
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		setImage(image);
		setX(200);
    	setY(465);
    	double angle = 0.25*Math.PI + 0.5*Math.PI*Math.random();
    	dx = Math.cos(angle) * BOUNCER_SPEED;
    	dy = Math.sin(angle) * BOUNCER_SPEED;
	}
	
	public void decreaseVelocity(){
		dx *= SLOWDOWN;
		dy *= SLOWDOWN;
	}
	
	public void increaseVelocity(){
		dx *= SPEEDUP;
		dy *= SPEEDUP;
	}
	
	public void bounce(Scene scene, double radius){
		if (getX() >= scene.getWidth() - 2*radius || getX() <= 0) {
			dx *= REVERSE;
		}
		if (getY() <= 0) {
			dy *= REVERSE;
		} 
		setX(getX() + dx * SECOND_DELAY);
		setY(getY() + dy * SECOND_DELAY);
	}
	
	public boolean fallen(Scene scene){
		return getY() > scene.getHeight();
	}
	
	public void reverseX(){
		dx *= REVERSE;
	}
	
	public void reverseY(){
		dy *= REVERSE;
	}
	
}
