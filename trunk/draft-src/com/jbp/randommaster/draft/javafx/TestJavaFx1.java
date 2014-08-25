package com.jbp.randommaster.draft.javafx;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
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
		
		
		Circle circle = new Circle(150, Color.rgb(r,g,b, 0.3));
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
		Scene scene = new Scene(root, width, height, Color.BLACK);

		int circlesCount = 30;
		Group circles = new Group();
		for (int i = 0; i < circlesCount; i++) {
			Circle circle = generateCircle();
			
			circle.setEffect(new BoxBlur(10, 10, 3));
			circles.getChildren().add(circle);
		}

		/*
		Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(), new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE,
				new Stop[] { new Stop(0, Color.web("#f8bd55")), new Stop(0.14, Color.web("#c0fe56")), new Stop(0.28, Color.web("#5dfbc1")),
						new Stop(0.43, Color.web("#64c2f8")), new Stop(0.57, Color.web("#be4af7")), new Stop(0.71, Color.web("#ed5fc2")),
						new Stop(0.85, Color.web("#ef504c")), new Stop(1, Color.web("#f2660f")), }));
		colors.widthProperty().bind(scene.widthProperty());
		colors.heightProperty().bind(scene.heightProperty());

		Group blendModeGroup = new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(), Color.BLACK), circles), colors);
		colors.setBlendMode(BlendMode.OVERLAY);
		root.getChildren().add(blendModeGroup);*/

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
		        new KeyFrame(new Duration(5000), // set end position at 40s
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
