package breakout;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PowerUp extends ImageView{
	public static final String[] myPUs = {"sizepower.gif", "pointspower.gif", "extraballpower.gif", "laserpower.gif", "snowflake.gif"};
	public static final int POWERUP_SPEED = 100;
	public static final int FRAMES_PER_SECOND = 60;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	
	private int option;
	
	// Initialize power-up position at the block that is being cleared
	public PowerUp(double x, double y){
		super();
		option =  new Random().nextInt(myPUs.length);
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(myPUs[option]));
		setImage(image);
		setX(x);
		setY(y);
	}
	
	public int getOption(){
		return option;
	}
	
	public void drop(){
		setY(getY() + POWERUP_SPEED * SECOND_DELAY);
	}
	
}
