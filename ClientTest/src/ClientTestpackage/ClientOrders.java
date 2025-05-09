package ClientTestpackage;


import java.net.URI;
import java.util.ArrayList;
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

import canteen2.Customer;
// uma classe para cada
import canteen2.MaOrder;
import canteen2.MenuOptions;
import canteen2.MenuOptionsService;

public class ClientOrders {

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/MaOrder/").build();
	}

	private static ClientConfig config = new ClientConfig();
	private static Client client = ClientBuilder.newClient(config);
	private static WebTarget service = client.target(getBaseURI());

	public static void main(String[] args) {
		//getOrderById(2);
		//getOrders();
		//deleteOrder(2);
		//addOrder();
		

		
	}
	
	
	/*
	
	private static void addOrder() {
		
		Gson gson = new Gson();
		
		MenuOptions menuOption = new MenuOptions(96,"Batatas", "peixe", "banana", "vinho branco");
		
		ClientMenuOptions.addMenuOption(menuOption);
		
		
		MaOrder newOrder = new MaOrder(15.30,653,"Cartao de debito",menuOption);
		List<MaOrder> orders = new ArrayList<MaOrder>();
		orders.add(newOrder);
		
		Response maOrderResponse = service.path("addMaOrder").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(newOrder, MediaType.APPLICATION_JSON), Response.class);

		
		
		Customer newCustomer = new Customer("Anibal","Ferreira","anibalinho","4999",55,orders,99,"Anibalzinho@gmail.com","912831283","Teacher");
		ClientCustomer.addCustomer(newCustomer);

		if (maOrderResponse.getStatus() < 200 && maOrderResponse.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + maOrderResponse.getStatus());
		} else {
			getOrders();
		}


	}
	
	*/
	
	/**
	 *  Add order method
	 * 
	 */
	
	private static void addOrder() {
		
		Gson gson = new Gson();
		
		MenuOptions menuOption = new MenuOptions(96,"Batatas", "peixe", "banana", "vinho branco");
	
		MaOrder newOrder = new MaOrder(15.30,653,"Cartao de debito",menuOption);
		List<MaOrder> orders = new ArrayList<MaOrder>();
		orders.add(newOrder);
		
		Response maOrderResponse = service.path("addMaOrder").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(newOrder, MediaType.APPLICATION_JSON), Response.class);
		

		if (maOrderResponse.getStatus() < 200 && maOrderResponse.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + maOrderResponse.getStatus());
		} else {
			getOrders();
		}


	}
	

	/**
	 * Delete order with id
	 * 
	 * @param maOrderId
	 */
	
	private static void deleteOrder(int maOrderId) {
		Response response = service.path("/deleteOrder")
				.path(Integer.toString(maOrderId)).request().delete();
		
		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getOrders();
		}

	}

	
	/**
	 * Get the order
	 * 
	 */
	
	private static void getOrders() {
		Gson gson = new Gson();

		String responseMaOrderList = service.path("getMaOrders").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		List<MaOrder> orders = Arrays.asList(gson.fromJson(responseMaOrderList, MaOrder[].class));
		System.out.println("\n Output JSON from Server getOrders .... \n");
		for (MaOrder b : orders) {
			System.out.println(b);
		}

	}

	
	/**
	 * Get order with id 
	 * 
	 * @param orderId
	 */
	
	private static void getOrderById(int orderId) {
		String responseOrder = service.path("getMaOrder").path(Integer.toString(orderId)).request()
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		Gson gson = new Gson();
		MaOrder order = gson.fromJson(responseOrder, MaOrder.class);
		System.out.println("\n Output JSON from Server getOrderById .... \n");
		System.out.println(order);

	}

}
