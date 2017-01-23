package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Station extends ImageView {
	
	public static final String STATION = "station.png";

	public Station() {
		super();
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(STATION));
		setImage(image);
	}
	
	

}
