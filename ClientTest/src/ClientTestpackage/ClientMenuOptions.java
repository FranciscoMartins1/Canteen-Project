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

import canteen2.MaOrder;
// uma classe para cada
import canteen2.MenuOptions;

public class ClientMenuOptions {

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/MenuOptions/").build();
	}

	
	private static ClientConfig config = new ClientConfig();
	private static Client client = ClientBuilder.newClient(config);
	private static WebTarget service = client.target(getBaseURI());

	public static void main(String[] args) {
		//getMenuOptionById(2);
		//addMenuOption();
		//getMenuOptions();	
		//deleteMenuOption(77);
		updateMenuOption();
		
	}

	
	/**
	 *create one menu Option
	 * 
	 */
	
	private static void addMenuOption() {
		MenuOptions menuOption = new MenuOptions(77, "cenouras", "coentros", "pera", "sumol sem gas");
		Response response = service.path("addMenuOptions").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(menuOption, MediaType.APPLICATION_JSON), Response.class);

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getMenuOptions();
		}

	}
	
	/**
	 * add Menu option used for ClientOrders
	 * 
	 * @param menuOpt
	 */
	public static void addMenuOption(MenuOptions menuOpt) {
		Response response = service.path("addMenuOptions").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(menuOpt, MediaType.APPLICATION_JSON), Response.class);

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getMenuOptions();
		}

	}
	
	
	/**
	 *Delete Menu option with id  
	 * 
	 * @param menuOptionId
	 */
	
	private static void deleteMenuOption(int menuOptionId) {
		Response response = service.path("/deleteMenuOption")
				.path(Integer.toString(menuOptionId)).request().delete();

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getMenuOptions();
		}

	}

	
	// Get the Menu Options
	private static void getMenuOptions() {
		Gson gson = new Gson();

		String responseMenuOptionsList = service.path("getMenuOptions").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		List<MenuOptions> menuOptions = Arrays.asList(gson.fromJson(responseMenuOptionsList, MenuOptions[].class));
		System.out.println("\n Output JSON from Server getMenuOptions .... \n");
		for (MenuOptions b : menuOptions) {
			System.out.println(b);
		}

	}

	
	/**
	 * Update Menu option
	 * 
	 */
	
	private static void updateMenuOption() {
		Gson gson = new Gson();
		
		MenuOptions newMenuOption = new MenuOptions(1,"Fish and chips","Fish and chips","Ice cream strawberry","sparkling water");
		Response response = service.path("updateMenuOption").request(MediaType.APPLICATION_JSON).put(Entity.entity(newMenuOption, MediaType.APPLICATION_JSON), Response.class);

		System.out.println(response.getStatus());

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getMenuOptions();
		}

	}
	
	
	/**
	 * Get menu option with id
	 * 
	 * @param menuOptionId
	 * @return
	 */
	
	private static MenuOptions getMenuOptionById(int menuOptionId) {
		String responseMenuOption = service.path("getMenuOption").path(Integer.toString(menuOptionId)).request()
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		Gson gson = new Gson();
		MenuOptions menuOption = gson.fromJson(responseMenuOption, MenuOptions.class);
		System.out.println("\n Output JSON from Server getMenuOptionById .... \n");
		System.out.println(menuOption);
		return menuOption;
	
	}


	
	
	
}