package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TextFileManager {
	
	private String log_path;

	public TextFileManager(String log_file_name_) {
		log_path = "./txt/" + log_file_name_;
		try {
			File fid = new File(log_path);
			if (fid.createNewFile()) {
				System.out.println("File created: " + log_file_name_);
			}
		} catch (IOException e) {
			System.err.println("An error occurred while opening: "+log_path+" .");			
		}
	}

	public ArrayList<String> readInputFile(File fid) {
		ArrayList<String> str_list = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(fid);
			while (scanner.hasNextLine()) {
				str_list.add(scanner.nextLine());
			}
			scanner.close();
			System.out.println("readInputFile success.");
		} catch (FileNotFoundException e) {
			System.err.println("An error occurred while reading: "+fid.getName()+" .");
			return null;	
		}
		return str_list;
	}

	public boolean writeToLogFile(String logstr) throws IOException {
		try {
			FileWriter log_writer = new FileWriter(log_path, true);
			BufferedWriter bw = new BufferedWriter(log_writer);
			bw.write(logstr);
			bw.close();
			log_writer.close();
			System.out.println("writeToLogFile success.");
		} catch (IOException e) {
			System.err.println("An error occurred while writing to log.");
			return false;
		}
		return true;
	}
}
