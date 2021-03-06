package breakout;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Blocks extends ImageView {

	public static final String[] myBricks = {"brick2.gif", "brick3.gif", "brick1.gif"};
	
	public Blocks(){
		super();
		int option = new Random().nextInt(myBricks.length);
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(myBricks[option]));
		setImage(image);
	}
	
	public double setPUX(){
		return getX() + getBoundsInLocal().getWidth()/2;
	}
	
	public double setPUY(){
		return getY() + getBoundsInLocal().getHeight()/2;
	}
}
