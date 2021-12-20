package server;

import client.GuiController;

public class Server {

	private BitcoinAbuse bitcoin_abuse;
	private FileManager file_manager;
	private GuiController gui_controller;

	public Server(GuiController gui_controller_) {
		bitcoin_abuse = new BitcoinAbuse("api_json");
		file_manager = FileManager.getInstance();

		gui_controller = gui_controller_;
		gui_controller.event_manager = new EventManager("Scan Addresses", "Upload File", "Save Report", "Log");

		// event subscriptions
		bitcoin_abuse.event_manager.subscribe("Scan Results", gui_controller);
		bitcoin_abuse.event_manager.subscribe("Error", gui_controller);

		file_manager.event_manager.subscribe("Result Upload", gui_controller);
		file_manager.event_manager.subscribe("Result Save", gui_controller);
		file_manager.event_manager.subscribe("Error", gui_controller);

		gui_controller.event_manager.subscribe("Scan Addresses", bitcoin_abuse);
		gui_controller.event_manager.subscribe("Upload File", file_manager);
		gui_controller.event_manager.subscribe("Save Report", file_manager);
		gui_controller.event_manager.subscribe("Log", file_manager);
	}
}
