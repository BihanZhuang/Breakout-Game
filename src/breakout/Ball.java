package breakout;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Ball extends ImageView{
	public static final String BALL_IMAGE = "mini.gif";
	public static final int BOUNCER_SPEED = 370;
	public static final int FRAMES_PER_SECOND = 60;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public double dx, dy;
	public static final int DIFF = 50;
	
	private double angle = 0.25*Math.PI + 0.5*Math.PI*Math.random();
	private int speed = BOUNCER_SPEED;
	
	public Ball(Scene scene){
		super();
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE));
		setImage(image);
		setX(200);
    	setY(465);
    	dx = Math.cos(angle) * speed;
    	dy = Math.sin(angle) * speed;
	}
	
	public void decreaseVelocity(){
		speed -= DIFF;
	}
	
	public void increaseVelocity(){
		speed += DIFF;
	}
	
	public void bounce(Scene scene, double radius){
		if (getX() >= scene.getWidth() - 2*radius || getX() <= 0) {
			dx *= -1;
		}
		if (getY() <= 0 || getY() >= scene.getHeight() - 2*radius) {
			dy *= -1;
		} 
		setX(getX() + dx * SECOND_DELAY);
		setY(getY() + dy * SECOND_DELAY);
	}
	
	public void reverseX(){
		dx *= -1;
	}
	
	public void reverseY(){
		dy *= -1;
	}
}
