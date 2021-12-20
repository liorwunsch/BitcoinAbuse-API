package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import server.Server;

public class MainGUI extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("patterns_gui.fxml"));

			AnchorPane root = loader.load();
			GuiController gui_controller = loader.getController();

			Scene scene = new Scene(root);
			primaryStage.setTitle("Abuse Address Checker");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

			gui_controller.initGuiController(primaryStage);

			new Server(gui_controller);

		} catch (Exception e) {
			System.err.println("Exceotion at starting app, Please check javaFX is working correctly");
			return;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
