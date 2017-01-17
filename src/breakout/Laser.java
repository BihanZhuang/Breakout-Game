package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Laser extends ImageView{
	public static final String BULLET = "bullet.jpg";
	public static int LASER_SPEED = 200;
	
	private double dy = LASER_SPEED;
	
	public Laser(double x, double y){
		super();
		Image bullet = new Image(getClass().getClassLoader().getResourceAsStream(BULLET));
		setImage(bullet);
		setX(x);
		setY(y);
	}
	
	public void shoot(double elapsedTime){
		setY(getY() - dy * elapsedTime);
	}
}
