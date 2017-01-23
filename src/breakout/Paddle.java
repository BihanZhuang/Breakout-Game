package breakout;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Paddle extends ImageView {
	public static final String PADDLE = "paddle.gif";
	public static final int KEY_INPUT_SPEED = 25;
	public static final int INIT_X = 180;
	public static final int INIT_Y = 490;
	public static final int DOUBLE = 120, HALF = 30, NORMAL = 60;
	public static final int DIVIDE = 5;
	
	private boolean freeze;
	
	public Paddle(){
		super();
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(PADDLE));
		setImage(image);
		setX(INIT_X);
		setY(INIT_Y);
	}
	
	/**
	 * When freeze is true, the paddle will not be able to move.
	 */
	public void reverseFreezeState(){
		freeze = !freeze;
	}
	
	public void doubleLength() {
		setFitWidth(DOUBLE);
	}
	
	public void halveLength(){
		setFitWidth(HALF);
	}
	
	public void normalLength(){
		setFitWidth(NORMAL);
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
	
	/**
	 * Check which region of the paddle the ball lands on.
	 * 
	 * @param obj
	 * @return
	 */
	public int checkRegion(ImageView obj){
		int ret = 1;
		if (obj.getX() >= getX() && obj.getX() < (getX() + getBoundsInLocal().getWidth()/5)){
			ret = 1;
		} else if (obj.getX() >= (getX() + getBoundsInLocal().getWidth()/DIVIDE) && 
				   obj.getX() < (getX() + 4*getBoundsInLocal().getWidth()/DIVIDE)) {
			ret = 2;
		} else if (obj.getX() > (getX() + 4*getBoundsInLocal().getWidth()/DIVIDE) && 
				   obj.getX() < (getX() + getBoundsInLocal().getWidth())){
			ret = 3;
		}
		return ret;
	}
	
	public void reset(){
		setX(INIT_X);
		setY(INIT_Y);
	}
	
}
