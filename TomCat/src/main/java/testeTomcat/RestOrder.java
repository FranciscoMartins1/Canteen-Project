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

import canteen2.MaOrder;
import canteen2.MaOrderService;
import canteen2.MenuOptions;

@Path("/MaOrder")
public class RestOrder {
	
	private MaOrderService bks = new MaOrderService();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello(Request a) {
		return "REST Server :I'm the Order Controller";
	}

	/**
	 * get orders
	 * 
	 * @return
	 */
	
	@GET
	@Path("/getMaOrders")
	public Response getMaOrders() {		
		List<MaOrder> maOrders = bks.findAllOrders();

		return Response.status(Response.Status.OK)
				.entity(maOrders)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	/**
	 * get order by id
	 * 
	 * @param id
	 * @return
	 */
	
	@GET
	@Path("/getMaOrder/{id}")
	public Response getMaOrder(@PathParam("id") int id) {
		MaOrder maOrderResponse = bks.findOrder(id);
		
		return Response.status(Response.Status.OK)
				.entity(maOrderResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	/**
	 * adding a order
	 * 
	 * @param ordering
	 * @return
	 */
	
	@POST
	@Path("/addMaOrder")
	public Response addOrder(MaOrder ordering) {		
		MaOrder maOrderResponse = bks.updateOrderOption(ordering);
		
		return Response.status(Response.Status.CREATED)
				.entity(maOrderResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}	
	
	
	/**
	 * remove a order
	 * @param id
	 * @return
	 */
	
	@DELETE
	@Path("/deleteOrder/{id}")
	public Response deleteOrder(@PathParam("id") int id) {
		boolean orderRemoved = bks.removeMaOrder(id);
		
		return Response.status(Response.Status.OK)
				.entity(orderRemoved)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

	
	
	
	//NOVO
	


	
	
	
	
}