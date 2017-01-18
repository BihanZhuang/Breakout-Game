package breakout;

import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class LevelGenerator {
	
	public static final int H = 500;
	public static final int W = 420;
	public static final int NUMBER_BLOCKS = 6;
	public static final int NUMBER_LAYERS = 8;
	public static final int BLOCK_INIT_HEIGHT = 80;
	public static final int LEVEL_THREE = 3;
	public static final int PUCOUNT = 24;
	
	private Scene scene;
	private Group root;
	private int level, puCount = PUCOUNT;
	private Paddle p;
	private Ball[] balls;
	private ArrayList<Laser> myLasers;
	private ArrayList<Blocks> myBlocks;
	private ArrayList<PowerUp> myPUs = new ArrayList<PowerUp>();
	
	private int numBouncers = 1;
	private boolean shoot, launchBall, laserpowerup, canTakePowerup = true;
	

	public LevelGenerator(){}
	
	public void step(double elapsedTime){
		double radius = balls[0].getBoundsInLocal().getWidth()/2;
		ArrayList<Blocks> removeList = new ArrayList<Blocks>();
		
		if (launchBall){
			for (int i=0; i<numBouncers; i++){
				balls[i].bounce(scene, radius);
				if (balls[i].getBoundsInLocal().intersects(p.getBoundsInLocal())){
					int region = p.checkRegion(balls[i]);
					if (region == 1 || region == 3) {
						balls[i].reverseX();;
					}
					balls[i].reverseY();
				}
				
				for (int j=0; j<myBlocks.size(); j++){
					Blocks b = myBlocks.get(j);
					if (balls[i].getBoundsInLocal().intersects(b.getBoundsInLocal())){
						balls[i].reverseY();
						if (puCount != 0){
							PowerUp pu = new PowerUp(setPUX(b), setPUY(b), level);
							myPUs.add(pu);
							root.getChildren().add(pu);
							puCount--;
						}
						root.getChildren().remove(b);
						removeList.add(b);
					}
				}
				
				for (int k=0; k<myPUs.size(); k++){
					myPUs.get(k).drop();
					powerupEffect(myPUs.get(k));
				}
				
				myBlocks.removeAll(removeList);
				removeList.clear();
			}
			
			if (shoot){
				for (int i=0; i<myLasers.size(); i++){
					myLasers.get(i).shoot(elapsedTime);
				}
			}
		}
	}
	
	public void makeLevel(Scene scene, Group root, int level){
		this.scene = scene;
		this.root = root;
		this.level = level;
		this.balls = setupBouncers(level, scene);
		this.p = new Paddle();
		this.myLasers = new ArrayList<Laser>();
		this.myBlocks = setupBlocks(NUMBER_BLOCKS, NUMBER_LAYERS, level);
    	
    	root.getChildren().addAll(balls);
    	root.getChildren().add(p);
    	root.getChildren().addAll(myBlocks);
    	
    	scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
	}
	
	private void handleKeyInput(KeyCode code) {
		p.handleKeyInput(code);
		
		if (code == KeyCode.SPACE){
			launchBall = true;
		}
		
		if (code == KeyCode.L && laserpowerup){
			PauseTransition delay = new PauseTransition(Duration.seconds(5));
			delay.setOnFinished(e -> laserpowerup = false);
			delay.play();
			Laser l = new Laser(p.getX()+p.getBoundsInLocal().getWidth()/2, 
					p.getY()-p.getBoundsInLocal().getHeight()*1.5);
			myLasers.add(l);
			root.getChildren().add(l);
			shoot = true;
		}	
	}
	
	private void powerupEffect(PowerUp pu){
		if (touchedPowerup(pu)){
			int optionNum = pu.getOption();
			System.out.println(optionNum);
			// Double paddle length
			if (optionNum == 0){
				PauseTransition delay = new PauseTransition(Duration.seconds(5));
				delay.setOnFinished(e -> p.normalLength());
				delay.play();
				p.doubleLength();
			} 
			// Decrease ball velocity
			else if (optionNum == 1){
				PauseTransition delay = new PauseTransition(Duration.seconds(5));
				delay.setOnFinished(e -> {
					for (Ball b : balls){
						b.increaseVelocity();
					}
					canTakePowerup = true;
				});
				delay.play();
				canTakePowerup = false;
				for (Ball b : balls){
					b.decreaseVelocity();
				}
			} 
			// One extra life
			else if (optionNum == 2){
				
				
				
				
				
			} 
			// Laser enabled
			else if (optionNum == 3){
				PauseTransition delay = new PauseTransition(Duration.seconds(5));
				delay.setOnFinished(e -> laserpowerup = false);
				delay.play();
				laserpowerup = true;
			}
			// Alien -- halve paddle length
			else if (optionNum == 4){
				PauseTransition delay = new PauseTransition(Duration.seconds(5));
				delay.setOnFinished(e -> p.normalLength());
				delay.play();
				p.halveLength();
			} 
			// Ice -- freeze paddle
			else if (optionNum == 5){
				PauseTransition delay = new PauseTransition(Duration.seconds(2));
				delay.setOnFinished(e -> p.freeze = false);
				delay.play();
				p.freeze = true;
			}
		}
	}
	
	private boolean touchedPowerup(PowerUp pu){
		return pu.getBoundsInLocal().intersects(p.getBoundsInLocal()) && canTakePowerup;
	}
	
	private PauseTransition setTimer(int second){
		PauseTransition delay = new PauseTransition(Duration.seconds(second));
		delay.play();
		return delay;
	}
	
	private Ball[] setupBouncers(int level, Scene scene){
		if (level >= LEVEL_THREE) {
			numBouncers = LEVEL_THREE;
		}
		Ball[] setup = new Ball[numBouncers];
		for (int i=0; i<numBouncers; i++){
			setup[i] = new Ball(scene);
		}
		return setup;
	}
	
	private ArrayList<Blocks> setupBlocks(int r, int c, int level){
		ArrayList<Blocks> setup = new ArrayList<Blocks>();
		for (int i=0; i<r; i++){
			for (int j=0; j<c; j++){
				Blocks b = new Blocks(level);
				b.setX(i*b.getBoundsInLocal().getWidth());
				b.setY(BLOCK_INIT_HEIGHT + j*b.getBoundsInLocal().getHeight());
				setup.add(b);
			}
		}
		return setup;
	}
	
	private double setPUX(Blocks b){
		return b.getX() + b.getBoundsInLocal().getWidth()/2;
	}
	
	private double setPUY(Blocks b){
		return b.getY() + b.getBoundsInLocal().getHeight()/2;
	}
	
}
