package breakout;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BreakoutMain extends Application {
	public static final String TITLE = "Breakout if you can!";
	public static final Paint BACKGROUND = Color.BLACK;
	public static final int H = 500;
	public static final int W = 420;
	public static final int FRAMES_PER_SECOND = 60;
	public static final double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	
	private Stage s;
	private Scene welcome, help, level, end;
	private Group root, endRoot;
	private LevelGenerator myLevelMaster;
	private int currentLevel = 1;

	@Override
	public void start(Stage s) {
		initialize(s);
        
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void initialize(Stage s){
		this.s = s;
		myLevelMaster  = new LevelGenerator();
		currentLevel = 1;
		root = new Group();
		endRoot = new Group();
		level = new Scene(root, W, H, BACKGROUND);
		myLevelMaster.makeLevel(level, root, currentLevel);
		Welcome w = new Welcome();
		Help h = new Help();
		help = h.makeHelpScene(s, level, help);
		welcome = w.makeWelcomeScene(s, welcome, level, help);
		end = new Scene(endRoot, W, H, BACKGROUND);
		
		s.setResizable(false);
		s.setScene(welcome);
        s.setTitle(TITLE);
        s.show();
        
        level.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        level.setOnKeyReleased(e -> myLevelMaster.handleKeyReleased(e.getCode()));
	}
	
	
	/**
	 * Main loop of the game. 
	 * Checks for restart and level up signals to trigger corresponding actions. 
	 * Goes to the end scene if necessary and start game again from there.
	 *
	 */
	private void step(){
		myLevelMaster.step(SECOND_DELAY);
		if (myLevelMaster.getRestartState()){
			restartGame();
			myLevelMaster.reverseRestartState();
		}
		if (myLevelMaster.levelUp()){
			if (currentLevel < 4){
				currentLevel++;
				root.getChildren().clear();
				myLevelMaster.resetConstants();
				myLevelMaster.makeLevel(level, root, currentLevel);
			} else{
				myLevelMaster.makeEndScene(s, end, endRoot, myLevelMaster.getTotal());
				end.setOnKeyPressed(e -> restartGame());
				s.setScene(end);
				s.show();
			}
		}
	}
	
	private void restartGame(){
		root.getChildren().clear();
		initialize(s);
	}
	
	/**
	 * Main key input handle method of the game. 
	 * Contains handle key input method from the LevelGenerator class.
	 * 
	 * @param code
	 */
	private void handleKeyInput(KeyCode code){
		myLevelMaster.handleKeyInput(code);
		// Cheat key -- Jump to indicated level
		if (code == KeyCode.DIGIT1 || code == KeyCode.DIGIT2 || 
				code == KeyCode.DIGIT3 || code == KeyCode.DIGIT4){
			int i = Integer.parseInt(code.getName());
			myLevelMaster.jumpLevel(i);
			currentLevel = i;
		}
	}
}
