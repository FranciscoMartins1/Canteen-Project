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

// uma classe para cada
import canteen2.Customer;
import canteen2.CustomerService;
import canteen2.MaOrder;
import canteen2.MaOrderService;
import canteen2.MenuOptions;
import canteen2.MenuOptionsService;


public class ClientCustomer {

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/Customer/").build();
	}

	private static ClientConfig config = new ClientConfig();
	private static Client client = ClientBuilder.newClient(config);
	private static WebTarget service = client.target(getBaseURI());

	public static void main(String[] args) {
		//getCustomers();
		//getCustomerById(5);
		//deleteCustomer(7);
		//addCustomer();
		//addOrder(getCustomerById(8));
		// add order ; search for the  customer and updated it with a order
	}

	
	/**
	 *create one customer 
	 * 
	 */
	
	private static void addCustomer() {
		Customer newCustomer = new Customer("Anibal","Ferreira","anibalinho","4999",55,null,99,"Anibal@gmail.com","92347234723","Priority");
		Response response = service.path("addCustomer").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(newCustomer, MediaType.APPLICATION_JSON), Response.class);

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getCustomers();
		}

	}
	
	
	// NOVO
	
	private static void addOrder(Customer newCustomer) {
		
		Gson gson = new Gson();
		
		MenuOptions menuOption = new MenuOptions(96,"Batatas", "peixe", "banana", "vinho branco");
	
		MaOrder newOrder = new MaOrder(15.30,1,"Cartao de debito",menuOption);
		List<MaOrder> orders = new ArrayList<MaOrder>();
		orders.add(newOrder);
		
		Response maOrderResponse = service.path("addMaOrder").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(newOrder, MediaType.APPLICATION_JSON), Response.class);
		
		
		
		
		if (maOrderResponse.getStatus() < 200 && maOrderResponse.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + maOrderResponse.getStatus());
		} else {
			newCustomer.setOrders(orders);
			updateCustomer(newOrder);
			//getCustomers();
		}


	}
	
	
	
	private static void updateCustomer(MaOrder newOrder) {
		Gson gson = new Gson();
		List<MaOrder> orders = new ArrayList<MaOrder>();
		orders.add(newOrder);
		
		Customer newCustomer = new Customer("Anibal","Ferreira","anibalinho","4999",55,orders,99,"Anibal@gmail.com","92347234723","Priority");
		Response response = service.path("updateCustomer").request(MediaType.APPLICATION_JSON).put(Entity.entity(newCustomer, MediaType.APPLICATION_JSON), Response.class);

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			//getCustomers();
		}

	}
	
	
	// FIM DE NOVO
	
	/**
	 *
	 * add customer used for ClientOrders
	 * 
	 * @param customer
	 */
	
	public static void addCustomer(Customer customer) {
		Response response = service.path("addCustomer").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(customer, MediaType.APPLICATION_JSON), Response.class);

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getCustomers();
		}

	}
	

	/**
	 * Delete Customer with id 
	 * 
	 * @param customerId
	 */
	
	private static void deleteCustomer(int customerId) {
		Response response = service.path("deleteCustomer")
				.path(Integer.toString(customerId)).request().delete();

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getCustomers();
		}

	}

	/**
	 * Get the Customer
	 * 
	 */
	
	private static void getCustomers() {
		Gson gson = new Gson();

		String responseCustomersList = service.path("getCustomers").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		List<Customer> customers = Arrays.asList(gson.fromJson(responseCustomersList, Customer[].class));
		System.out.println("\n Output JSON from Server getCustomer .... \n");
		for (Customer b : customers) {
			System.out.println(b);
		}

	}

	
	/**
	 * Get Customer with id
	 * 
	 * @param customersId
	 */
	// ADICIONEI O CUSTOMER EM VEZ DE VOID
	private static Customer getCustomerById(int customersId) {
		String responseCustomer = service.path("getCustomer").path(Integer.toString(customersId)).request()
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		Gson gson = new Gson();
		Customer customer = gson.fromJson(responseCustomer, Customer.class);
		System.out.println("\n Output JSON from Server getCusomerById .... \n");
		System.out.println(customer);
		//NOVO
		return customer;

	}
	
	
	

}