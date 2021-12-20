package server;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;

import common.AddressEntry;
import common.ReportEntry;

// Structural Patterns: Facade
public final class BitcoinAbuse implements EventListener {

	public EventManager event_manager;

	private HttpHandler httpHandler;
	private RequestHandler requestHandler;
	private JsonHandler jsonHandler;
	private String domain_url;
	private String api_token;

	public BitcoinAbuse(String json_name) {
		event_manager = new EventManager("Scan Results", "Error");
		httpHandler = new HttpHandler();
		requestHandler = new RequestHandler();
		jsonHandler = new JsonHandler();

		ArrayList<String> params = jsonHandler.readJson(json_name); // {url_address, api_token}
		this.domain_url = params.get(0);
		this.api_token = params.get(1);
	}

	// Facade hides complexity of a task and provides a simple interface.
	// The method gets an address and sends a "GET" HttpRequest to
	// www.bitcoinAbuse.com and returns a report
	private ReportEntry checkAddress(String address) {
		HttpClient client;
		HttpRequest httpRequest;
		String request;
		ReportEntry retEntry = null;

		try {
			// connect and return http client
			client = httpHandler.getClient();

			// parse a string for the Get request
			request = requestHandler.parseGetRequest(domain_url, address, api_token);

			// build and return httpRequest
			httpRequest = requestHandler.buildHttpRequest(request);

			// reset response from the previous session (static param)
			JsonHandler.resetResponse();

			// send to httpClient the httpRequest
			httpHandler.sendRequest(client, httpRequest);

			// build and return reportEntry
			retEntry = httpHandler.createReportEntry(JsonHandler.getResponse(), domain_url);

		} catch (Exception e) {
			return null;
		}
		return retEntry;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(String eventType, Object obj) {
		if (eventType.equals("Scan Addresses")) {
			try {
				ArrayList<AddressEntry> address_list = (ArrayList<AddressEntry>) obj;
				ArrayList<ReportEntry> report_entry_list = new ArrayList<>();
				for (AddressEntry address_entry : address_list) {
					ReportEntry ret = checkAddress(address_entry.getAddress());
					if (ret != null) {
						report_entry_list.add(ret);
					} else {
						throw new Exception();
					}
				}
				event_manager.notify("Scan Results", report_entry_list);
			} catch (Exception e) {
				event_manager.notify("Error", eventType);
			}
		}
	}

}
