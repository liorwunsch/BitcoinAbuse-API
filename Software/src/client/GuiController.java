package client;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import common.AddressEntry;
import common.ReportEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import server.EventListener;
import server.EventManager;

public class GuiController implements EventListener {

	@FXML
	private TextField tf_newAddress;
	@FXML
	private TextArea ta_log;

	@FXML
	private Button btn_addToList;
	@FXML
	private Button btn_scan;
	@FXML
	private Button btn_saveResults;
	@FXML
	private Button btn_clearTable;

	@FXML
	private VBox vb_uploadFile;

	@FXML
	private TableView<AddressEntry> tv_address;
	@FXML
	private TableView<ReportEntry> tv_results;

	public EventManager event_manager;

	private ObservableList<AddressEntry> address_table = FXCollections.observableArrayList();
	private ObservableList<ReportEntry> report_result = FXCollections.observableArrayList();

	private final DirectoryChooser directoryChooser = new DirectoryChooser();
	private final FileChooser fileChooser = new FileChooser();
	private final StringBuilder sb_log = new StringBuilder();
	private Stage primaryStage;
	private AddressEntry editEntry;
	private boolean editFlag;

	public void initGuiController(Stage primaryStage_) {
		primaryStage = primaryStage_;
		editEntry = null;
		editFlag = false;
		initAddressTableView();
		initResultTableView();
		initDirectoryChooser();
		initFileChooser();
	}

	private void initAddressTableView() {
		TableColumn<AddressEntry, String> column1 = new TableColumn<>("Address");
		column1.setCellValueFactory(new PropertyValueFactory<>("address"));
		tv_address.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tv_address.getColumns().add(column1);
	}

	private void initResultTableView() {
		TableColumn<ReportEntry, String> column1 = new TableColumn<>("Address");
		column1.setCellValueFactory(new PropertyValueFactory<>("address"));
		tv_results.getColumns().add(column1);

		TableColumn<ReportEntry, String> column2 = new TableColumn<>("Report Count");
		column2.setCellValueFactory(new PropertyValueFactory<>("reportCount"));
		tv_results.getColumns().add(column2);

		TableColumn<ReportEntry, ImageView> column3 = new TableColumn<>("Status");
		column3.setCellValueFactory(new PropertyValueFactory<>("img"));
		tv_results.getColumns().add(column3);

		TableColumn<ReportEntry, String> column4 = new TableColumn<>("Link");
		column4.setCellValueFactory(new PropertyValueFactory<>("link"));
		tv_results.getColumns().add(column4);

		column1.setPrefWidth(270);
		column2.setPrefWidth(80);
		column3.setPrefWidth(50);
		column4.setPrefWidth(155);
	}

	private void initDirectoryChooser() {
		directoryChooser.setTitle("Choose Output Folder");
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
	}

	private void initFileChooser() {
		fileChooser.setTitle("Open Input File");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
	}

	private void updateAddressTableView() {
		tf_newAddress.clear();
		editEntry = null;
		editFlag = false;
		btn_addToList.setText("Add to list");
		tv_address.setItems(address_table);
	}

	private void updateResultTableView() {
		tv_results.setItems(report_result);
	}

	private void updateLogTextArea(String str) {
		String timeStamp = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(Calendar.getInstance().getTime());
		String logstr = timeStamp + " " + str + "\n";
		sb_log.append(logstr);
		ta_log.setText(sb_log.toString());
		event_manager.notify("Log", logstr);
	}

	@FXML
	void addToList(ActionEvent event) {
		String new_address = tf_newAddress.getText();
		if (!new_address.isEmpty()) {
			if (editFlag && editEntry != null) {
				ObservableList<AddressEntry> address_table_temp = FXCollections.observableArrayList();
				AddressEntry newEditedEntry = new AddressEntry(new_address);
				for (AddressEntry entry : address_table) {
					if (entry.equals(editEntry)) {
						address_table_temp.add(newEditedEntry);
					} else {
						address_table_temp.add(entry);
					}
				}
				address_table.clear();
				address_table = address_table_temp;
			} else {
				AddressEntry address_entry = new AddressEntry(new_address);
				address_table.add(address_entry);
			}
			updateAddressTableView();
			tf_newAddress.clear();
		} else {
			editEntry = null;
			editFlag = false;
			btn_addToList.setText("Add to list");
		}

	}

	@FXML
	void removeRow(ActionEvent event) {
		AddressEntry address_entry = tv_address.getSelectionModel().getSelectedItem();
		if (address_entry != null) {
			address_table.remove(address_entry);
			updateAddressTableView();
		}
	}

	@FXML
	void clearTable(ActionEvent event) {
		address_table.clear();
		updateAddressTableView();
	}

	@FXML
	void editRow(ActionEvent event) {
		AddressEntry address_entry = tv_address.getSelectionModel().getSelectedItem();
		if (address_entry != null) {
			editEntry = address_entry;
			tf_newAddress.setText(address_entry.getAddress());
			btn_addToList.setText("Update");
			editFlag = true;
		}
	}

	@FXML
	void uploadFile(MouseEvent event) {
		File fid = fileChooser.showOpenDialog(primaryStage);
		if (fid != null) {
			event_manager.notify("Upload File", fid);
		}
	}

	@FXML
	void scanAddresses(ActionEvent event) {
		event_manager.notify("Scan Addresses", new ArrayList<AddressEntry>(address_table));
	}

	@FXML
	void saveResults(ActionEvent event) {
		final File selectedDirectory = directoryChooser.showDialog(primaryStage);
		if (selectedDirectory != null) {
			ArrayList<Object> list = new ArrayList<>();
			list.add(selectedDirectory.getPath());
			list.add(new ArrayList<ReportEntry>(report_result));
			event_manager.notify("Save Report", list);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(String eventType, Object obj) {
		if (eventType.equals("Scan Results")) {
			try {
				ArrayList<ReportEntry> report_entry_list = (ArrayList<ReportEntry>) obj;
				if (report_entry_list == null) {
					throw new Exception();
				}
				report_result.clear();
				for (ReportEntry report_entry : report_entry_list) {
					Integer report_count = Integer.valueOf(report_entry.getReportCount());
					if (report_count > 0) {
						report_entry.setImg(new ImageView(new Image(getClass().getResourceAsStream("gui_bad.png"))));
					} else {
						report_entry.setImg(new ImageView(new Image(getClass().getResourceAsStream("gui_good.png"))));
					}
					report_result.add(report_entry);
				}
				updateResultTableView();
				updateLogTextArea("Scan Completed Successfully.");
			} catch (Exception e) {
				updateLogTextArea("Scan Failed. Exception..");
			}
		} else if (eventType.equals("Result Upload")) {
			try {
				ArrayList<String> address_list = (ArrayList<String>) obj;
				address_table.clear();
				for (String address : address_list) {
					address_table.add(new AddressEntry(address));
				}
				updateAddressTableView();
			} catch (Exception e) {
				System.err.println("Error occurred while upload results.");
			}
		} else if (eventType.equals("Result Save")) {
			try {
				boolean is_saved = (Boolean) obj;
				String is_saved_str;
				if (is_saved) {
					is_saved_str = "Report Saved Successfully.";
				} else {
					is_saved_str = "Report Save Failed.";
				}
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Save Results");
				alert.setHeaderText(is_saved_str);
				alert.showAndWait();
			} catch (Exception e) {
				System.err.println("Error occurred while saving results.");
			}
		} else if (eventType.equals("Error")) {
			String errorMsg = "Error occurred during " + (String) obj + ".";
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(errorMsg);
			alert.showAndWait();
		}
	}
}
