package com.jbp.randommaster.draft.javafx;

import java.util.Random;





import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestJavaFx1 extends Application {
	
	private Circle generateCircle() {
		
		Random rand=new Random();
		
		int r = rand.nextInt(256);
		int g = rand.nextInt(256);
		int b = rand.nextInt(256);
		
		
		Circle circle = new Circle(50, Color.rgb(r,g,b, 0.3));
		circle.setStrokeType(StrokeType.OUTSIDE);
		circle.setStroke(Color.rgb(r,g,b, 0.16));
		circle.setStrokeWidth(4);

		return circle;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		int width = 1280;
		int height = 950;
		
		Group root = new Group();
		Scene scene = new Scene(root, width, height, Color.WHITE);
		

		int circlesCount = 150;
		Group circles = new Group();

		LinearGradient lg = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE,
				new Stop[] { new Stop(0, Color.web("#f8bd55", 0.5)), new Stop(0.14, Color.web("#c0fe56", 0.5)), new Stop(0.28, Color.web("#5dfbc1", 0.5)),
						new Stop(0.43, Color.web("#64c2f8", 0.5)), new Stop(0.57, Color.web("#be4af7", 0.5)), new Stop(0.71, Color.web("#ed5fc2", 0.5)),
						new Stop(0.85, Color.web("#ef504c", 0.5)), new Stop(1, Color.web("#f2660f", 0.5)), });
		
		for (int i = 0; i < circlesCount; i++) {
			Circle circle = generateCircle();
			
			circle.setEffect(new BoxBlur(10, 10, 3));	
			circle.setOnMouseClicked((MouseEvent ev) -> {
				circle.toBack();
				circle.setFill(lg);
				circle.setEffect(null);
			});
			circles.getChildren().add(circle);
		}

		root.getChildren().add(circles);
		
		primaryStage.setScene(scene);
		primaryStage.show();

		
		Timeline timeline = new Timeline();
		for (Node circle: circles.getChildren()) {
		    timeline.getKeyFrames().addAll(
		        new KeyFrame(Duration.ZERO, // set start position at 0
		            new KeyValue(circle.translateXProperty(), Math.random() * scene.getWidth()),
		            new KeyValue(circle.translateYProperty(), Math.random() * scene.getHeight())
		        ),
		        new KeyFrame(new Duration(45000), // set end position at 40s
		            new KeyValue(circle.translateXProperty(), Math.random() * scene.getWidth()),
		            new KeyValue(circle.translateYProperty(), Math.random() * scene.getHeight())
		        )
		    );
		}
		// play 40s of animation
		timeline.play();		

	}

	public static void main(String[] args) {

		launch(args);

	}

}
