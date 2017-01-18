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
	
	private Scene welcome, help, level1, level2, level3, level4;
	private Group root1, root2, root3, root4;
	private LevelGenerator myLevelMaster;
	private int currentLevel;

	@Override
	public void start(Stage s) {
		makeWelcomeScene(s);
		makeHelpScene(s);
		//makeLevelOne(s);
		initialize();
		
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
		myLevelMaster.step(SECOND_DELAY);
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
	
	private void initialize(){
		myLevelMaster  = new LevelGenerator();
		currentLevel = 1;
		root1 = new Group();
		level1 = new Scene(root1, W, H, BACKGROUND);
		myLevelMaster.makeLevel(level1, root1, currentLevel);
	}
}
