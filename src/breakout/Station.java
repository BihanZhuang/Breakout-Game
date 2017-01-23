package breakout;

import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Station extends ImageView {
	
	public static final String STATION = "station.gif";
	public static final int[] X_POS = {20, 40, 60, 80, 100, 300, 320, 340};
	public static final int[] Y_POS = {200, 220, 240, 260, 280, 300, 320, 340, 360, 380, 400};
	public static final int W = 10;
	public static final int H = 10;
	
	private int count = 0;

	public Station() {
		super();
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(STATION));
		setImage(image);
		this.setFitHeight(H);
		this.setFitWidth(W);
		int option1 = new Random().nextInt(X_POS.length);
		int option2 = new Random().nextInt(Y_POS.length);
		setX(X_POS[option1]);
		setY(Y_POS[option2]);
		System.out.println(X_POS[option1]);
		System.out.println(Y_POS[option2]);
	}
	
	public int getCount(){
		return count;
	}
	
	public void addCount(){
		count++;
	}
	
	public void resetCount(){
		count = 0;
	}
}
