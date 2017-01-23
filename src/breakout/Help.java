package breakout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Help {
	
	public static final Paint BACKGROUND = Color.BLACK;
	public static final int H = 500;
	public static final int W = 420;
	
	public Help(){}
	
	public Scene makeHelpScene(Stage s, Scene level1, Scene help){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(5));
		vbox.setSpacing(5);
		vbox.setAlignment(Pos.TOP_LEFT);
		Text title = new Text("THE RULES");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		vbox.getChildren().add(title);
		
		Text[] rules = new Text[]{
		    new Text("0. Press SPACE bar to launch. Use LEFT/RIGHT to move paddle."),
			new Text("1. Rocks can only be destroyed with 4 hits!"),
			new Text("But they do not need to be cleared to level up."),
			new Text("2. Powerups: "),
			new Text("Red -- Double paddle length"),
			new Text("Yellow -- Decrease ball velocity"),
			new Text("Green -- Onx extra life"),
			new Text("Blue -- Enable laser power (Press D to shoot!)"),
			new Text("Press and hold C to stick ball to the paddle!"),
			new Text("3. Threats:"),
			new Text("Color Ball Alien -- Halve paddle length"),
			new Text("Snowflake -- Freeze paddle"),
			new Text("4. Cheat keys:"),
			new Text("Press 1-4 to jump to corresponding level"),
			new Text("Press R to reset current level"),
			new Text("Press U to generate umlimited laser"),
			new Text("Press A to clear all the blocks"),
			new Text("5. Fuel Stations:"),
			new Text("Appear at random places to charge the ball and add 100 points.")
		};
		
		vbox.getChildren().addAll(rules);
		
		Button button1 = new Button("START");
		button1.setOnAction(e -> s.setScene(level1));
		vbox.getChildren().add(button1);
		
		help = new Scene(vbox, W, H, BACKGROUND);
		return help;
	}	
}
