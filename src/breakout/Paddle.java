package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Paddle extends ImageView {
	public static final String PADDLE = "paddle.gif";
	public static final int KEY_INPUT_SPEED = 25;
	
	public boolean freeze;
	
	public Paddle(){
		super();
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE));
		setImage(image);
		setX(180);
		setY(485);
	}
	
	public void doubleLength() {
		setFitWidth(120);
	}
	
	public void halveLength(){
		setFitWidth(30);
	}
	
	public void normalLength(){
		setFitWidth(60);
	}

	public void handleKeyInput(KeyCode code){
		if (!freeze){
			if (code == KeyCode.RIGHT) {
	            setX(getX() + KEY_INPUT_SPEED);
	        }
	        else if (code == KeyCode.LEFT) {
	            setX(getX() - KEY_INPUT_SPEED);
	        }
		}
	}
	
	public int checkRegion(ImageView obj){
		int ret = 1;
		if (obj.getX() >= getX() && obj.getX() < (getX() + getBoundsInLocal().getWidth()/5)){
			ret = 1;
		} else if (obj.getX() >= (getX() + getBoundsInLocal().getWidth()/5) && 
				   obj.getX() < (getX() + 4*getBoundsInLocal().getWidth()/5)) {
			ret = 2;
		} else if (obj.getX() > (getX() + 4*getBoundsInLocal().getWidth()/5) && 
				   obj.getX() < (getX() + getBoundsInLocal().getWidth())){
			ret = 3;
		}
		return ret;
	}
	
}
