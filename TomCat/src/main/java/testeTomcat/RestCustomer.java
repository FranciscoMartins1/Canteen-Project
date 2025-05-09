package testeTomcat;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import canteen2.Customer;
import canteen2.CustomerService;
import canteen2.MenuOptions;

@Path("/Customer")
public class RestCustomer {
	
	private CustomerService bks = new CustomerService();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello(Request a) {
		return "REST Server :I'm the Customers Controller";
	}
	
	/**
	 * get customer
	 * 
	 * @return
	 */
	
	@GET
	@Path("/getCustomers")
	public Response getCustomers() {		
		List<Customer> customers = bks.findAllCustomers();

		return Response.status(Response.Status.OK)
				.entity(customers)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	/**
	 * get customer by id
	 * 
	 * @param id
	 * @return
	 */
	
	@GET
	@Path("/getCustomer/{id}")
	public Response getCustomer(@PathParam("id") int id) {
		Customer customerResponse = bks.findCustomer(id);
		
		return Response.status(Response.Status.OK)
				.entity(customerResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	/**
	 * adding a Customer
	 * 
	 * @param customer
	 * @return
	 */
	
	@POST
	@Path("/addCustomer")
	public Response addCustomer(Customer customer) {		
		Customer customerResponse = bks.updateCustomer(customer);
		
		return Response.status(Response.Status.CREATED)
				.entity(customerResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	/**
	 * delete customer
	 * 
	 * @param id
	 * @return
	 */
	
	@DELETE
	@Path("/deleteCustomer/{id}")
	public Response deleteCustomer(@PathParam("id") int id) {
		boolean customerRemoved = bks.removeCustomers(id);
		
		return Response.status(Response.Status.OK)
				.entity(customerRemoved)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

	
	/**
	 * update Customer
	 * 
	 * @param newCustomer
	 * @return
	 */
	
	@PUT
	@Path("/updateCustomer")
	public Response updateOrder(Customer newCustomer) {
		Customer customerResponse = bks.updateCustomer(newCustomer);
		
		return Response.status(Response.Status.OK)
				.entity(customerResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
}