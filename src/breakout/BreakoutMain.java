package breakout;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BreakoutMain extends Application {
	public static final String TITLE = "Sushi Land";
	public static final Paint BACKGROUND = Color.BLACK;
	public static final int H = 500;
	public static final int W = 420;
	public static final int FRAMES_PER_SECOND = 60;
	public static final double MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 20;
	public static final int BOUNCER_SPEED = 370;
	public static final int NUMBER_BLOCKS = 6;
	public static final int NUMBER_LAYERS = 8;
	
	private Scene welcome, level1, level2, level3, level4, help;
	private Paddle p = new Paddle();
	private Ball[] balls = new Ball[3];
	private ArrayList<Laser> myLasers = new ArrayList<Laser>();
	private int numBouncers = 1;
	private Group root1, root2, root3, root4;
	private boolean shoot, launchBall;
	private ArrayList<Blocks> myBlocks = new ArrayList<Blocks>();
	

	@Override
	public void start(Stage s) {
		makeWelcomeScene(s);
		makeHelpScene(s);
		makeLevelOne(s);
		
		s.setResizable(false);
		s.setScene(welcome);
        s.setTitle(TITLE);
        s.show();
        
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> loop());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	// Main game loop
	private void loop(){
		levelOneLoop();
	}
	
	// Main method to make the welcome scene 
	private void makeWelcomeScene (Stage s){
		BorderPane root = new BorderPane();
		root.setCenter(addVBox(s));
		root.setTop(addHBox());
		root.setLeft(addQuitButton());
		root.setBottom(addCopyRight());
		welcome = new Scene(root, W, H, BACKGROUND);
	}
	
	// Some helpers
	private HBox addCopyRight() {
		HBox cr = new HBox();
		cr.setPadding(new Insets(20));
		cr.setSpacing(10);
		
		Text cont = new Text("Copyright Bihan Zhuang 2017");
		cont.setFont(Font.font("Arial", 16));
		
		cr.getChildren().add(cont);
		return cr;
	}

	private VBox addQuitButton() {
		VBox quit = new VBox();
		quit.setPadding(new Insets(20));
		quit.setSpacing(10);
		return quit;
	}

	private HBox addHBox() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(20));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: #336699;");
		
		Text title = new Text("Sushi Land");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		hbox.getChildren().add(title);
		return hbox;
	}

	private VBox addVBox(Stage s) {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20, 20, 20, 20));
		vbox.setSpacing(10);
		
		Button button1 = new Button("START");
		button1.setOnAction(e -> s.setScene(level1));
		
		Button button2 = new Button("HELP");
		button2.setOnAction(e -> s.setScene(help));
		
		vbox.getChildren().addAll(button1, button2);
		return vbox;
	}
	
	// Main method to make the help scene 
	private void makeHelpScene(Stage s){
		VBox vbox = new VBox();
		Text title = new Text("RULES");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		vbox.getChildren().add(title);
		
		Text[] rules = new Text[]{
			new Text("1. Clear all the sushi blocks to level up."),
			new Text("2. "),
			new Text("3. ")
			};
		vbox.getChildren().addAll(rules);
		
		Button button1 = new Button("START");
		button1.setOnAction(e -> s.setScene(level1));
		
		Button button2 = new Button("BACK");
		button2.setOnAction(e -> s.setScene(welcome));
		
		vbox.getChildren().addAll(button1, button2);
		
		help = new Scene(vbox, W, H, BACKGROUND);
	}
	
	// Method to make the level1 scene
	private void makeLevelOne(Stage s){
		root1 = new Group();
		level1 = new Scene(root1, W, H, BACKGROUND);
		
		balls[0] = new Ball(level1);
		
		setBlockConfigL1();
    	
    	root1.getChildren().add(balls[0]);
    	root1.getChildren().add(p);
    	root1.getChildren().addAll(myBlocks);
    	
    	level1.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

	}
	
	// Loop for level 1
	private void levelOneLoop(){
		double radius = balls[0].getBoundsInLocal().getWidth()/2;
		if (launchBall){
			for (int i=0; i<numBouncers; i++){
				balls[i].bounce(level1, radius);
				if (balls[i].getBoundsInLocal().intersects(p.getBoundsInLocal())){
					int region = p.checkRegion(balls[i]);
					if (region == 1 || region == 3) {
						balls[i].dx *= -1;
					}
					balls[i].dy *= -1;
				}
				ArrayList<Blocks> removeList = new ArrayList<Blocks>();
				for (int j=0; j<myBlocks.size(); j++){
					Blocks b = myBlocks.get(j);
					if (balls[i].getBoundsInLocal().intersects(b.getBoundsInLocal())){
						balls[i].dy *= -1;
						root1.getChildren().remove(b);
						removeList.add(b);
					}
				}	
				myBlocks.removeAll(removeList);
			}
			
			if (shoot){
				for (int i=0; i<myLasers.size(); i++){
					myLasers.get(i).shoot(SECOND_DELAY);
				}
			}
			
		}
	}
	
	// Algorithmically generate different block configurations
	private void setBlockConfigL1(){
		for (int i=0; i<NUMBER_BLOCKS; i++){
			for (int j=0; j<NUMBER_LAYERS; j++){
				Blocks b = new Blocks();
				b.setX(i*b.getBoundsInLocal().getWidth());
				b.setY(80 + j*b.getBoundsInLocal().getHeight());
				myBlocks.add(b);
			}
		}
	}
	
	
	// Method to make the level 2 scene
	private void makeLevelTwo(){
		
	}
	
	// Loop for level 2
	private void levelTwoLoop(){
		
	}
	
	private boolean laserpu = true;
	
	// Main method to handle key input
	private void handleKeyInput(KeyCode code){
		p.handleKeyInput(code);
		
		if (code == KeyCode.SPACE){
			launchBall = true;
		}
		
		if (code == KeyCode.L && laserpu ){
			PauseTransition delay = new PauseTransition(Duration.seconds(5));
			delay.setOnFinished(e -> laserpu = false);
			delay.play();
			Laser l = new Laser(p.getX()+p.getBoundsInLocal().getWidth()/2, p.getY()-p.getBoundsInLocal().getHeight()*1.5);
			myLasers.add(l);
			root1.getChildren().add(l);
			shoot = true;
		}	
	}
	
	private void takePowerUp(int optionNum){
		if (optionNum == 0){
			PauseTransition delay = new PauseTransition(Duration.seconds(5));
			delay.setOnFinished(e -> p.normalLength());
			delay.play();
			p.doubleLength();
		} else if (optionNum == 1){
			PauseTransition delay = new PauseTransition(Duration.seconds(5));
			delay.setOnFinished(e -> {
				for (Ball b : balls){
					b.increaseVelocity();
				}
			});
			delay.play();
			for (Ball b : balls){
				b.decreaseVelocity();
			}
		} else if (optionNum == 3){
			PauseTransition delay = new PauseTransition(Duration.seconds(5));
			delay.setOnFinished(e -> p.normalLength());
			delay.play();
			p.halveLength();
		} else if (optionNum == 4){
			PauseTransition delay = new PauseTransition(Duration.seconds(2));
			delay.setOnFinished(e -> p.freeze = false);
			delay.play();
			p.freeze = true;
		}
	}
}
