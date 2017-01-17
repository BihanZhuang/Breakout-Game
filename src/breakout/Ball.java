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
		dx *= 0.8;
		dy *= 0.8;
	}
	
	public void increaseVelocity(){
		dx *= 1.2;
		dy *= 1.2;
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
}
