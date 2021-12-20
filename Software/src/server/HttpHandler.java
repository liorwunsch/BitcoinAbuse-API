package server;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import common.ReportEntry;

public class HttpHandler {

	public HttpHandler() {}

	public HttpClient getClient() {
		HttpClient client = null;
		try {
			client = HttpClient.newHttpClient();
		} catch (Exception e) {
			System.err.println("Somthing went wrong, Cant create HTTP Client.");
		}
		return client;
	}

	public void sendRequest(HttpClient client, HttpRequest request) {
		try {
			client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body)
					.thenAccept(JsonHandler::parse).join();
		} catch (Exception e) {
			System.err.println("Somthing went wrong while sending a request\ncheck if address is legal.");
		}
	}

	public ReportEntry createReportEntry(ArrayList<String> response,String url) {
		ReportEntry ret = null;
		if (response.size() == 3) {
			String address = response.get(0);
			String report_count = response.get(1);
			String link = url+"/reports/" + address;
			ret = new ReportEntry(address, report_count, link);
		}
		return ret;
	}
}
