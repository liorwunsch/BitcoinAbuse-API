package server;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JsonHandler {

	private static ArrayList<String> response;

	public JsonHandler() {
		response = new ArrayList<>();
	}

	public ArrayList<String> readJson(String json_name) {
		ArrayList<String> ret = null;
		String json_path = "./txt/" + json_name + ".json";
		File file = new File(json_path);
		if (file != null) {
			try {
				ret = new ArrayList<>();
				String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
				JSONObject jsonContent = new JSONObject(content);
				String url = (String) jsonContent.get("url");
				String api_token = (String) jsonContent.get("api_token");
				ret.add(url);
				ret.add(api_token);
			} catch (IOException e) {
				System.err.println("Error occurred opening file: "+json_path+"\n please check if the file exsit.");
			}
		}
		return ret;
	}

	public static void parse(String response_body) {
		System.out.println("HttpResponse: " + response_body);
		JSONArray data = new JSONArray("[" + response_body + "]");
		JSONObject entry = data.getJSONObject(0);

		String address = entry.getString("address");
		String report_count = Integer.toString(entry.getInt("count"));
		String link = address;
		response.add(address);
		response.add(report_count);
		response.add(link);
	}

	public static ArrayList<String> getResponse() {
		return response;
	}

	public static void resetResponse() {
		response.clear();
	}
}
