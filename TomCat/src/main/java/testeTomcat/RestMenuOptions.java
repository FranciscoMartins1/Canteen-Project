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
import canteen2.MenuOptionsService;

@Path("/MenuOptions")
public class RestMenuOptions {
	
	private MenuOptionsService bks = new MenuOptionsService();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello(Request a) {
		return "REST Server :I'm the Menu Options Controller";
	}

	/**
	 * get Menu options
	 * 
	 * @return
	 */
	
	@GET
	@Path("/getMenuOptions")
	public Response getMenuOptions() {		
		List<MenuOptions> menuOption = bks.findAllMenuOptions();

		return Response.status(Response.Status.OK)
				.entity(menuOption)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	/**
	 * get Menu option by id
	 * 
	 * @param id
	 * @return
	 */
	
	@GET
	@Path("/getMenuOption/{id}")
	public Response getMenuOption(@PathParam("id") int id) {
		MenuOptions menuOptionsResponse = bks.findMenuOption(id);
		
		return Response.status(Response.Status.OK)
				.entity(menuOptionsResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	/**
	 * adding a menu option
	 * 
	 * @param menuOption
	 * @return
	 */
	
	@POST
	@Path("/addMenuOptions")
	public Response addMenuOption(MenuOptions menuOption) {		
		MenuOptions menuOptionresponse = bks.updateMenuOption(menuOption);
		
		return Response.status(Response.Status.CREATED)
				.entity(menuOptionresponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	

	/**
	 * update menu Option
	 * 
	 * @param menuOption
	 * @return
	 */
	
	@PUT
	@Path("/updateMenuOption")
	public Response updateOrder(MenuOptions menuOption) {
		MenuOptions menuOptionResponse = bks.updateMenuOption(menuOption);
		
		return Response.status(Response.Status.OK)
				.entity(menuOptionResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	

	
	/**
	 * delete a menu option
	 * 
	 * @param id
	 * @return
	 */
	
	@DELETE
	@Path("/deleteMenuOption/{id}")
	public Response deleteMenuOption(@PathParam("id") int id) {
		boolean menuOptionresponse = bks.removeMenuOptions(id);
		
		return Response.status(Response.Status.OK)
				.entity(menuOptionresponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
}