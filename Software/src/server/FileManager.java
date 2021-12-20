package server;

import java.io.File;
import java.util.ArrayList;

import common.ReportEntry;

// Creational Patterns: Singleton
public class FileManager implements EventListener {

	private static FileManager instance = null;
	public EventManager event_manager;

	private TextFileManager text_file_manager;
	private ExcelFileManager excel_file_manager;

	private FileManager() {
		event_manager = new EventManager("Result Upload", "Result Save", "Error");

		text_file_manager = new TextFileManager("log.txt");
		excel_file_manager = new ExcelFileManager();
	}

	public static FileManager getInstance() {
		if (instance == null) {
			instance = new FileManager();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(String eventType, Object obj) {
		try {
			if (eventType.equals("Log")) {
				boolean ret = text_file_manager.writeToLogFile((String) obj);
				if (!ret) {
					throw new Exception();
				}

			} else if (eventType.equals("Upload File")) {
				ArrayList<String> arr = text_file_manager.readInputFile((File) obj);
				if (arr != null) {
					event_manager.notify("Result Upload", arr);
				} else {
					throw new Exception();
				}

			} else if (eventType.equals("Save Report")) {
				ArrayList<Object> list = (ArrayList<Object>) obj;
				boolean result_save = excel_file_manager.writeToExcelFile((String) list.get(0),
						(ArrayList<ReportEntry>) list.get(1));
				if (result_save) {
					event_manager.notify("Result Save", result_save);
				} else {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			event_manager.notify("Error", eventType);
		}
	}
}
