package server;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

public class RequestHandler {

	public RequestHandler() {}
	
	public String parseGetRequest(String request_url, String address, String api_token) {
		String requestString = null;
		if (!(request_url.isEmpty() || address.isEmpty() || api_token.isEmpty())) {
			String requestPrefix = request_url + "/api/reports/check";
			requestString = requestPrefix + "?address=" + address + "&api_token=" + api_token;
			System.out.println("HttpRequest: " + requestString);
		}
		return requestString;
	}

	public HttpRequest buildHttpRequest(String requestString) {
		HttpRequest request = null;
		try {
			URI myuri = new URI(requestString);
			request = HttpRequest.newBuilder().uri(myuri).GET().build();
		} catch (URISyntaxException e) {
			System.err.println("An error occurred while building the HTTP Request.");
		}
		return request;
	}
}
