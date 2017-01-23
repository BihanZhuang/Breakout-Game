package breakout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Welcome {
	
	public static final Paint BACKGROUND = Color.BLACK;
	public static final int H = 500;
	public static final int W = 420;
	public static final int TITLE = 5;
	public static final int BTN_WIDTH = 120;
	public static final int BTN_HEIGHT = 40;
	public static final int INSET = 20;
	public static final int SPACING = 20;
	
	public Welcome(){}
	
	public Scene makeWelcomeScene (Stage s, Scene welcome, Scene level1, Scene help){
		BorderPane root = new BorderPane();
		root.setCenter(addVBox(s, welcome, level1, help));
		welcome = new Scene(root, W, H, BACKGROUND);
		return welcome;
	}

	private VBox addVBox(Stage s, Scene welcome, Scene level1, Scene help) {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(INSET));
		vbox.setSpacing(SPACING);
		vbox.setStyle("-fx-background-color: #000000;");
		vbox.setAlignment(Pos.CENTER);
		
		Text title = new Text("Breakout if you can!");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 35));
		title.setFill(Color.WHITE);
		title.relocate(W/2, TITLE);
		
		Button button1 = new Button("START");
		button1.setOnAction(e -> s.setScene(level1));
		button1.setPrefWidth(BTN_WIDTH);
		button1.setPrefHeight(BTN_HEIGHT);
		
		Button button2 = new Button("HELP");
		button2.setOnAction(e -> s.setScene(help));
		button2.setPrefWidth(BTN_WIDTH);
		button2.setPrefHeight(BTN_HEIGHT);
		
		vbox.getChildren().addAll(title, button1, button2);
		return vbox;
	}
}
