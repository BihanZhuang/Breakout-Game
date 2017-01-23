package breakout;

import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelGenerator {
	
	public static final int NUMBER_BLOCKS = 6;
	public static final int NUMBER_LAYERS = 6;
	public static final int BLOCK_INIT_HEIGHT = 50;
	public static final int PUCOUNT = 8;
	public static final int POWERUP_DURATION = 5;
	public static final double CHANCE_OF_PU = 0.5;
	public static final int BLOCK_POINTS = 5;
	public static final int ROCK_POINTS = 20;
	public static final int STATION_POINTS = 100;
	public static final int W = 420;
	
	private Scene scene;
	private Group root;
	private Status status;
	private int level, puCount = PUCOUNT, numBouncers = 1, total = 0;
	private Paddle p;
	private Station station;
	private Ball[] balls;
	private ArrayList<Laser> myLasers;
	private ArrayList<Blocks> myBlocks;
	private ArrayList<PowerUp> myPUs = new ArrayList<PowerUp>();
	private ArrayList<Rock> myRocks = new ArrayList<Rock>();
	private boolean restart, shoot, launchBall, laserpowerup, catchBall;

	public LevelGenerator(){}
	
	/**
	 * Main loop of one particular level in the game.
	 * Takes card of ball bounce, ball fall, block removal, rock removal 
	 * and power up effects.
	 * Repeats itself every "elapsedTime" seconds.
	 * 
	 * @param elapsedTime
	 */
	public void step(double elapsedTime){
		double radius = balls[0].getBoundsInLocal().getWidth()/2;
		ArrayList<Blocks> removeBlocks = new ArrayList<Blocks>();
		ArrayList<Laser> removeLasers = new ArrayList<Laser>();
		ArrayList<Rock> removeRocks = new ArrayList<Rock>();
  		
		if (launchBall){
				for (int i=0; i<numBouncers; i++){
					balls[i].bounce(scene, radius);
					
					// Clear rocks
					for (int j=0; j<myRocks.size(); j++){
						Rock r = myRocks.get(j);
						if (contacted(balls[i], r)){
							balls[i].reverseY();
							if (r.getCount() < 3){
								r.hitTime();
							} else {
								root.getChildren().remove(r);
								removeRocks.add(r);
								status.addRPoints();
								total += ROCK_POINTS;
							}	
						}
					}
					myRocks.removeAll(removeRocks);
					
					// Basic bounce
					if (contacted(balls[i], p)){
						int region = p.checkRegion(balls[i]);
						if (region == 1 || region == 3) {
							balls[i].reverseX();;
						}
						balls[i].reverseY();
					}
					// When ball falls
					else if (balls[i].fallen(scene)){
						root.getChildren().remove(balls[i]);
						balls[i] = new Ball(scene);
						p.reset();
						root.getChildren().add(balls[i]);
						launchBall = false;
						status.subtractCount();
						status.updateNums();
						if (status.getCount() == 0){
							System.out.println("You lost!");
							restart = true;
							resetConstants();
							break;
						}
					} 
					
					// CLearing blocks
					for (int j=0; j<myBlocks.size(); j++){
						Blocks b = myBlocks.get(j);
						if (contacted(balls[i], b)){
							balls[i].reverseY();
							if (puCount != 0 && Math.random() < CHANCE_OF_PU){
								PowerUp pu = new PowerUp(b.setPUX(), b.setPUY(), level);
								myPUs.add(pu);
								root.getChildren().add(pu);
								puCount--;
							}
							root.getChildren().remove(b);
							removeBlocks.add(b);
							status.addBPoints();
							total += BLOCK_POINTS;
							status.updateNums();
						}
					}
					myBlocks.removeAll(removeBlocks);
					
					// Power up moving and taking effects
					for (int k=0; k<myPUs.size(); k++){
						myPUs.get(k).drop();
						powerupEffect(myPUs.get(k));
					}
					
					// Catch ball
					if (catchBall){
						balls[i].setX(p.getX() + p.getBoundsInLocal().getWidth()/2 - radius);
						balls[i].setY(p.getY() - radius*2);
					}
					
					// Check if entering station
					if (station != null){
						if (contacted(station, balls[i]) && station.getCount() == 0){
						Ball thisBall = balls[i];
						PauseTransition delay = new PauseTransition(Duration.seconds(2));
						delay.setOnFinished(e -> {
							thisBall.move();
							station.resetCount();
							thisBall.bounce(scene, radius);
						});
						delay.play();
						balls[i].pause();
						balls[i].bounce(scene, radius);
						status.addSPoints();
						total += STATION_POINTS;
						station.addCount();
					}
					}
				}
				
				// Laser moving and clearing blocks
				if (shoot){
					for (int i=0; i<myLasers.size(); i++){
						Laser l = myLasers.get(i);
						l.shoot(elapsedTime);
						for (int j=0; j<myBlocks.size(); j++){
							Blocks b =  myBlocks.get(j);
							if (contacted(l, b)){
								root.getChildren().removeAll(l, b);
								myBlocks.remove(j);
								status.addBPoints();
								total += BLOCK_POINTS;
								status.updateNums(); 
								removeLasers.add(l);
							}
						}
					}
					myLasers.removeAll(removeLasers);
				}
				
		}
	}
	
	/**
	 * Return cumulative points obtained
	 * 
	 * @return
	 */
	public int getTotal(){
		return total;
	}
	
	public boolean getRestartState(){
		return restart;
	}
	
	/**
	 * Check if allowed to level up
	 * 
	 * @return
	 */
	public boolean levelUp(){
		return myBlocks.size() == 0 && status.getCount() > 0;
	}
		
	/**
	 * Generic method to set up the scene for any level
	 * 
	 * @param scene
	 * @param root
	 * @param level
	 */
	public void makeLevel(Scene scene, Group root, int level){
		this.scene = scene;
		this.root = root;
		this.level = level;
		this.status = new Status(level, W);
		this.balls = setupBouncers(level, scene);
		this.p = new Paddle();
		this.myLasers = new ArrayList<Laser>();
		this.myBlocks = setupBlocks(NUMBER_BLOCKS, NUMBER_LAYERS, level);
		
		root.getChildren().add(status);
    	root.getChildren().addAll(balls);
    	root.getChildren().add(p);
    	root.getChildren().addAll(myBlocks);
    	
		if (level == 3 || level == 4){
			this.station = new Station();
			root.getChildren().add(station);
		}
	}
	
	/**
	 * Make the end scene that displays total points 
	 * 
	 * @param s
	 * @param scene
	 * @param root
	 * @param points
	 */
	public void makeEndScene(Stage s, Scene scene, Group root, int points){
		this.scene = scene;
		this.root = root;
		
		Text won = new Text("You won!");
		won.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		won.setFill(Color.WHITE);
		Text show = new Text("Total points earned: " + points);
		show.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		show.setFill(Color.WHITE);
		Text anyKey = new Text("Press any key to restart game!");
		anyKey.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		anyKey.setFill(Color.WHITE);
		
		won.relocate(W/3, scene.getHeight()/3);
		show.relocate(W/4, scene.getHeight()/2);
		anyKey.relocate(W/4, scene.getHeight()/1.5);
		
		root.getChildren().add(won);
		root.getChildren().add(show);
		root.getChildren().add(anyKey);
	}
	
	public void handleKeyInput(KeyCode code) {
		
		// Launch Ball(s)
		if (code == KeyCode.SPACE){
			launchBall = true;
		}
		
		if (launchBall) p.handleKeyInput(code);
		
		// Enable shooting lasers
		if (code == KeyCode.D && laserpowerup){
			setTimer(POWERUP_DURATION).setOnFinished(e -> laserpowerup = false);
			Laser l = new Laser(p.getX()+p.getBoundsInLocal().getWidth()/2, 
					p.getY()-p.getBoundsInLocal().getHeight()*1.5);
			myLasers.add(l);
			root.getChildren().add(l);
			shoot = true;
		} 
		
		// Cheat key -- Unlimited laser
		if (code == KeyCode.U){
			Laser l = new Laser(p.getX()+p.getBoundsInLocal().getWidth()/2, 
					p.getY()-p.getBoundsInLocal().getHeight()*1.5);
			myLasers.add(l);
			root.getChildren().add(l);
			shoot = true;
		}
		
		// Cheat key -- Reset the current level
		if (code == KeyCode.R){
			reset(scene, root, level);
		}
		
		// Cheat key -- Clear all blocks
		if (code == KeyCode.A){
			root.getChildren().removeAll(myBlocks);
			myBlocks.removeAll(myBlocks);
		}
		
		// Cheat key -- catch ball with paddle
		if (code == KeyCode.C){
			catchBall = true;
		}
	}
	
	public void handleKeyReleased(KeyCode code) {
		if(code == KeyCode.C) {
			catchBall = false;
		}
	}
	
	public void jumpLevel(int levelNum){
		level = levelNum;
		reset(scene, root, level);
	}
	
	public void resetConstants(){
		puCount = PUCOUNT;
		shoot = false;
		launchBall = false;
		laserpowerup = false;
	}
	
	private void reset(Scene scene, Group root, int level){
		root.getChildren().clear();
		resetConstants();
		makeLevel(scene, root, level);
	}
	
	/**
	 * Take different kinds of power up and make them effective.
	 * A timer was set to control the time of effects.
	 * 
	 * @param pu
	 */
	private void powerupEffect(PowerUp pu){
		if (contacted(pu, p)){
			int optionNum = pu.getOption();
			root.getChildren().remove(pu);
			myPUs.remove(pu);
			
			int time = POWERUP_DURATION;
			// Double paddle length
			if (optionNum == 0){
				setTimer(time).setOnFinished(e -> p.normalLength());
				p.doubleLength();
			} 
			// Decrease ball velocity
			else if (optionNum == 1){
				setTimer(time).setOnFinished(e -> {
					for (Ball b : balls){
						b.increaseVelocity();
					}
				});
				for (Ball b : balls){
					b.decreaseVelocity();
				}
			} 
			// One extra life
			else if (optionNum == 2){
				status.addCount();
				status.updateNums();
			} 
			// Laser enabled
			else if (optionNum == 3){
				setTimer(time).setOnFinished(e -> laserpowerup = false);
				laserpowerup = true;
			}
			// Alien -- halve paddle length
			else if (optionNum == 4){
				setTimer(time).setOnFinished(e -> p.normalLength());
				p.halveLength();
			} 
			// Ice -- freeze paddle
			else if (optionNum == 5){
				setTimer(time).setOnFinished(e -> p.reverseFreezeState());
				p.reverseFreezeState();
			}
		}
	}
	
	/**
	 * Timer used to time power up effects.
	 * 
	 * @return
	 */
	private PauseTransition setTimer(double time){
		PauseTransition delay = new PauseTransition(Duration.seconds(time));
		delay.playFromStart();
		return delay;
	}
		
	/**
	 * Check if two ImageView objects come in contact.
	 * Used at many places throughout the class.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean contacted(ImageView a, ImageView b){
		return a.getBoundsInLocal().intersects(b.getBoundsInLocal());
	}
	
	private Ball[] setupBouncers(int level, Scene scene){
		Ball[] setup = new Ball[numBouncers];
		for (int i=0; i<numBouncers; i++){
			setup[i] = new Ball(scene);
		}
		return setup;
	}
	
	private ArrayList<Blocks> setupBlocks(int r, int c, int level){
		ArrayList<Blocks> setup = new ArrayList<Blocks>();
		for (int i=0; i<r; i++){
			if (level== 2 && i == level){
				for (int j=0; j<c; j++){
					if (j == 1 || j == 4){
						makeRock(i, j);
					} else{
						makeBlock(i, j, setup);
					}
				}
			} else if (level == 4 && (i == 0 || i == 2 || i ==5)){
				for (int j=0; j<c; j++){
					if (j == 0 || j == 4){
						makeRock(i, j);
					} else{
						makeBlock(i, j, setup);
					}
				}
			} else if (level == 3 && (i == 0 || i == 5)){
				for (int j=0; j<c; j++){
					if (j % 3 == 0){
						makeRock(i, j);
					} else{
						makeBlock(i, j, setup);
					}
				}
			} else{
				for (int j=0; j<c; j++){
					makeBlock(i, j, setup);
				}
			}
		}
		return setup;
	}
	
	private void makeBlock(int i, int j, ArrayList<Blocks> setup){
		Blocks b = new Blocks();
		b.setX(i*b.getBoundsInLocal().getWidth());
		b.setY(BLOCK_INIT_HEIGHT + j*b.getBoundsInLocal().getHeight());
		setup.add(b);
	}
	
	private void makeRock(int i, int j){
		Rock rock = new Rock();
		rock.setX(i*rock.getBoundsInLocal().getWidth());
		rock.setY(BLOCK_INIT_HEIGHT + j*rock.getBoundsInLocal().getHeight());
		root.getChildren().add(rock);
		myRocks.add(rock);
	}
	
}
