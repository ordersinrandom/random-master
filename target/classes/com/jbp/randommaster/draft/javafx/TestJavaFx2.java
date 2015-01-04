package com.jbp.randommaster.draft.javafx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TestJavaFx2 extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		ListView<String> listView = new ListView<>();

		ObservableList<String> dataModel = FXCollections.observableArrayList();

		listView.setItems(dataModel);
		for (int i = 1; i < 10; i++) {
			dataModel.add("List Item " + i);
		}

		HBox hbox1 = new HBox();
		hbox1.setPadding(new Insets(5, 5, 10, 10));
		hbox1.setSpacing(5);
		TextField newLabelInput = new TextField("");
		Button newLabelBut = new Button("Add Item");
		newLabelBut.setOnAction((ActionEvent ev) -> {
			String inputLabel = newLabelInput.getText().trim();
			if (!"".equals(inputLabel)) {
				dataModel.add(inputLabel);
				newLabelInput.setText("");
			}
		});

		Button removeLabelBut = new Button("Remove Item");
		removeLabelBut.setOnAction((ActionEvent ev) -> {
			int selectedIndex = listView.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0)
				dataModel.remove(selectedIndex);
		});

		hbox1.getChildren().addAll(newLabelInput, newLabelBut, removeLabelBut);

		BorderPane panel = new BorderPane();
		panel.setTop(hbox1);
		panel.setCenter(listView);

		Scene scene = new Scene(panel, 400, 300);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
