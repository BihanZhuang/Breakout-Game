package breakout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Status extends HBox{
	
	public static final String HEART = "heart.gif";
	public static final int NUM_LIFE = 3;
	public static final int W = 420;
	public static final int ROCK_POINTS = 20;
	public static final int BLOCK_POINTS = 5;
	public static final int INSET = 4;
	public static final int SPACING = 15;
	
	private int count = NUM_LIFE, points = 0;
	private Image image = new Image(getClass().getClassLoader().getResourceAsStream(HEART));
	private Text numLives;

	public Status (int level, double width){
		setPadding(new Insets(INSET));
		setSpacing(SPACING);
		setStyle("-fx-background-color: #336699;");
		setPrefWidth(W);
		
		Text title = new Text("Level " + level);
		title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		ImageView heart = new ImageView(image);
		numLives = new Text("" + count + "      Points: " + points);
		
		getChildren().add(title);
		getChildren().add(heart);
		getChildren().add(numLives);
		setAlignment(Pos.CENTER);
	}
	
	public void updateNums(){
		getChildren().remove(numLives);
		numLives = new Text("" + count + "      Points: " + points);
		getChildren().add(numLives);
	}
	
	public void addRPoints(){
		points+=ROCK_POINTS;
	}
	
	public void addBPoints(){
		points+=BLOCK_POINTS;
	}
	
	public void subtractCount(){
		count--;
	}
	
	public void addCount(){
		count++;
	}
	
	public int getCount(){
		return count;
	}
}
