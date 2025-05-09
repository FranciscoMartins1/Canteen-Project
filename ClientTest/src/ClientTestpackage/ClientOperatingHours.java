package ClientTestpackage;


import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;




import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.google.gson.Gson;

// uma classe para cada
import canteen2.OperatingHours;

public class ClientOperatingHours {

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/OperatingHours/").build();
	}
	
	private static ClientConfig config = new ClientConfig();
	private static Client client = ClientBuilder.newClient(config);
	private static WebTarget service = client.target(getBaseURI());

	public static void main(String[] args) {
		getOperatingHoursById(1);
		getOperatingHours();
		addOperatingHours();
		deleteOperatingHours(1);
		
		
	}
	
	
	/**
	 * Create one operating hour
	 * 
	 */
	
	private static void addOperatingHours() {
		// String dayOfWeek, Date openingTime, Date closingTime , int id

		try {
		    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm'h'");
		    Date openingTimeDate = timeFormat.parse("9:00h");
		    Date closingTimeDate = timeFormat.parse("19:00h");
		    OperatingHours operatingHours = new OperatingHours("Monday", openingTimeDate, closingTimeDate, 3);

		    Response response = service.path("addOperatingHours").request(MediaType.APPLICATION_JSON)
		            .post(Entity.entity(operatingHours, MediaType.APPLICATION_JSON), Response.class);

		    if (response.getStatus() < 200 || response.getStatus() >= 300) {
		        System.out.println("Failed to add operating hours. HTTP error code: " + response.getStatus());
		    } else {
		        getOperatingHours();
		    }

		} catch (Exception e) {
		    e.printStackTrace();
		}
		
	}
	

	/**
	 * Delete Operating hours with id 
	 * 
	 * @param operatingHoursId
	 */
	
	private static void deleteOperatingHours(int operatingHoursId) {
		Response response = service.path("deleteOperatingHours")
				.path(Integer.toString(operatingHoursId)).request().delete();

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getOperatingHours();
		}

	}

	
	/**
	 * Get the operating hours
	 * 
	 */
	
	private static void getOperatingHours() {
		Gson gson = new Gson();

		String responseOperatingHoursList = service.path("getOperatingHours").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		List<OperatingHours> operatingHours = Arrays.asList(gson.fromJson(responseOperatingHoursList, OperatingHours[].class));
		System.out.println("\n Output JSON from Server getOperatingHours .... \n");
		for (OperatingHours b : operatingHours) {
			System.out.println(b);
		}

	}
	
	/**
	 * Get Operating hours with id
	 * 
	 * @param operatingHoursId
	 */
	
	private static void getOperatingHoursById(int operatingHoursId) {
		String responseOperatingHours = service.path("getOperatingHour").path(Integer.toString(operatingHoursId)).request()
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		Gson gson = new Gson();
		OperatingHours operatingHours = gson.fromJson(responseOperatingHours, OperatingHours.class);
		System.out.println("\n Output JSON from Server getOperatingHoursById .... \n");
		System.out.println(operatingHours);

	}

}
