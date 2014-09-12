package com.jbp.randommaster.draft.javafx;

import java.util.LinkedList;
import java.util.Random;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TestJavaFx3 extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		Random rand=new Random();
		LinkedList<Person> allData = new LinkedList<>();
		for (int i = 1; i <= 5000; i++)
			allData.add(new Person("First " + i, "Last " + i, "dummy" + i + "@abc.com", rand.nextDouble()));
		ObservableList<Person> data = FXCollections.observableArrayList(allData);

		TableView<Person> table = new TableView<>();
		table.setEditable(true);

		TableColumn<Person, String> firstNameCol = new TableColumn<>("First Name");
		TableColumn<Person, String> lastNameCol = new TableColumn<>("Last Name");
		TableColumn<Person, String> emailCol = new TableColumn<>("e-mail");
		TableColumn<Person, Double> strengthCol = new TableColumn<>("Strength");
		firstNameCol.setMinWidth(100);
		lastNameCol.setMinWidth(100);
		emailCol.setMinWidth(200);
		strengthCol.setMinWidth(150);

		firstNameCol.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getFirstName()));
		lastNameCol.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getLastName()));
		emailCol.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getEmail()));
		strengthCol.setCellValueFactory(new PropertyValueFactory<>("strength"));

        firstNameCol.setCellFactory(TextFieldTableCell.<Person>forTableColumn());
        firstNameCol.setOnEditCommit(t -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setFirstName(t.getNewValue()));		
		
        strengthCol.setCellFactory(ProgressBarTableCell.<Person>forTableColumn());

        
		table.getColumns().add(firstNameCol);
		table.getColumns().add(lastNameCol);
		table.getColumns().add(emailCol);
		table.getColumns().add(strengthCol);

		table.setItems(data);

		HBox topBox = new HBox();
		Label label = new Label("Address Book");
		label.setFont(new Font("Arial", 22));
		topBox.setPadding(new Insets(3, 3, 3, 3));
		topBox.setSpacing(5.0);
		topBox.getChildren().add(label);

		TextField firstNameInput = new TextField();
		firstNameInput.setPromptText("First Name Input");
		firstNameInput.setPrefWidth(120);
		TextField lastNameInput = new TextField();
		lastNameInput.setPromptText("Last Name Input");
		lastNameInput.setPrefWidth(120);
		TextField emailInput = new TextField();
		emailInput.setPromptText("e-mail Input");
		emailInput.setPrefWidth(220);

		Button addPersonButton = new Button("Add");
		addPersonButton.setOnAction(ev -> {
			String firstName = firstNameInput.getText().trim();
			String lastName = lastNameInput.getText().trim();
			String email = emailInput.getText().trim();
			if (!"".equals(firstName) && !"".equals(lastName) && !"".equals(email)) {
				data.add(new Person(firstName, lastName, email, rand.nextDouble()));
				firstNameInput.clear();
				lastNameInput.clear();
				emailInput.clear();
			}
		});

		HBox bottomBox = new HBox();
		bottomBox.setPadding(new Insets(3, 3, 3, 3));
		bottomBox.setSpacing(5.0);
		bottomBox.getChildren().addAll(firstNameInput, lastNameInput, emailInput, addPersonButton);

		BorderPane panel = new BorderPane();
		panel.setCenter(table);
		panel.setTop(topBox);
		panel.setBottom(bottomBox);

		Scene scene = new Scene(panel);
		primaryStage.setWidth(600);
		primaryStage.setHeight(400);
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	public static void main(String[] args) {

		launch(args);

	}

	// helper class
	public static class Person {

		private String firstName;
		private String lastName;
		private String email;
		private double strength;

		public Person(String firstName, String lastName, String email, double strength) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.strength=strength;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
		
		public double getStrength() {
			return strength;
		}
		
		public void setStrength(double s) {
			strength = s;
		}
	}

}
