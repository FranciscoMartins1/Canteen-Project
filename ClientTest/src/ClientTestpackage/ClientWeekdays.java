package ClientTestpackage;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.google.gson.Gson;

import canteen2.Weekdays;
import canteen2.WeekdaysService;
import canteen2.MenuOptions;

public class ClientWeekdays {

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/Weekdays/").build();
	}

	
	private static ClientConfig config = new ClientConfig();
	private static Client client = ClientBuilder.newClient(config);
	private static WebTarget service = client.target(getBaseURI());

	public static void main(String[] args) {

		addWeekday();
		//deleteWeekday(5);
		//getWeekdays();
		//updateWeekdays();
		//getWeekdaysById(3);
	}

	
	/**
	 * Create weekday
	 * 
	 */
	
	private static void addWeekday() {
		Weekdays weekday = new Weekdays(6,"Sunday",null);
		Response response = service.path("addWeekdays").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(weekday, MediaType.APPLICATION_JSON), Response.class);

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getWeekdays();
		}

	}
	
	
	/**
	 * Delete weekday with id
	 * 
	 * @param weekdayId
	 */
	
	private static void deleteWeekday(int weekdayId) {
		Response response = service.path("/deleteWeekdays")
				.path(Integer.toString(weekdayId)).request().delete();

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getWeekdays();
		}

	}

	
	/**
	 * Get the weekdays
	 * 
	 */
	
	private static void getWeekdays() {
		Gson gson = new Gson();

		String responseWeekday = service.path("getWeekdays").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		List<Weekdays> weekday = Arrays.asList(gson.fromJson(responseWeekday, Weekdays[].class));
		System.out.println("\n Output JSON from Server getWeekdays .... \n");
		for (Weekdays b : weekday) {
			System.out.println(b);
		}

	}

	
	/**
	 * Update the weekdays
	 * 
	 */
	
	private static void updateWeekdays() {
		Gson gson = new Gson();
		
		Weekdays newMenuOption = new Weekdays(4,"Saturday",null);
		Response response = service.path("updateWeekdays").request(MediaType.APPLICATION_JSON).put(Entity.entity(newMenuOption, MediaType.APPLICATION_JSON), Response.class);


		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getWeekdays();
		}

	}

	
	
	/**
	 * Get weekdays with id
	 * 
	 * @param dayId
	 * @return
	 */
	
	private static Weekdays getWeekdaysById(int dayId) {
		String responseMenuOption = service.path("getWeekday").path(Integer.toString(dayId)).request()
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		Gson gson = new Gson();
		Weekdays weekday = gson.fromJson(responseMenuOption, Weekdays.class);
		System.out.println("\n Output JSON from Server getWeekdaysById .... \n");
		System.out.println(weekday);
		return weekday;
	
	}


	
	
	
}