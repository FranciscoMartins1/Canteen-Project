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

import canteen2.MenuOptions;
import canteen2.OperatingHours;
import canteen2.OperatingHoursService;

@Path("/OperatingHours")
public class RestOperatingHours {
	
	private OperatingHoursService bks = new OperatingHoursService();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello(Request a) {
		return "REST Server :I'm the Operating Hours Controller";
	}

	/**
	 * get Operating hour 
	 * 
	 * @return
	 */
	
	@GET
	@Path("/getOperatingHours")
	public Response getOperatingHours() {		
		List<OperatingHours> operatingHours = bks.findAllOperatingHours();

		return Response.status(Response.Status.OK)
				.entity(operatingHours)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	/**
	 * get Operating hour by id
	 * 
	 * @param id
	 * @return
	 */
	
	@GET
	@Path("/getOperatingHour/{id}")
	public Response getOperatingHour(@PathParam("id") int id) {
		OperatingHours operatingHoursResponse = bks.findOperatingHours(id);
		
		return Response.status(Response.Status.OK)
				.entity(operatingHoursResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	/**
	 * adding a Operating hour
	 * 
	 * @param operatingHours
	 * @return
	 */
	
	@POST
	@Path("/addOperatingHours")
	public Response addOperatingHours(OperatingHours operatingHours) {		
		OperatingHours operatingHoursResponse = bks.updateOperatingHours(operatingHours);
		
		return Response.status(Response.Status.CREATED)
				.entity(operatingHoursResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	
	
	/**
	 * delete operating  hours
	 * 
	 * @param id
	 * @return
	 */
	
	@DELETE
	@Path("/deleteOperatingHours/{id}")
	public Response deleteOperatingHours(@PathParam("id") int id) {
		boolean operatingHoursRemoved = bks.removeOperatingHour(id);
		
		return Response.status(Response.Status.OK)
				.entity(operatingHoursRemoved)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

	
	
	
	// TESTE
	
	
	@PUT
	@Path("/updateOperatingHours")
	public Response updateOperatingHours(OperatingHours opr) {
		OperatingHours operatingResponse = bks.updateOperatingHours(opr);
		
		return Response.status(Response.Status.OK)
				.entity(operatingResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	
	
	
}