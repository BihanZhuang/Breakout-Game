package breakout;

import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Blocks extends ImageView {

	public static final String[] myBricks = {"brick2.gif", "brick3.gif", "brick1.gif", "brick8.gif"};
	
	public Blocks(int level){
		super();
		int option = 0;
		if (level != 1){
			option = new Random().nextInt(myBricks.length);
		} else{
			option = new Random().nextInt(myBricks.length-1);
		}
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(myBricks[option]));
		setImage(image);
	}
}
