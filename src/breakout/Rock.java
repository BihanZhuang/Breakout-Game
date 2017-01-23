package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Rock extends ImageView {
	
	public static final String IMAGE = "brick9.gif";
	
	private int count = 0;
	
	public Rock() {
		super();
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(IMAGE));
		setImage(image);
	}
	
	public int getCount(){
		return count;
	}
	
	public void hitTime(){
		count++;
	}
}
