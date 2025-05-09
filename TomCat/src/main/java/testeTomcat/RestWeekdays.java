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



import canteen2.Weekdays;
import canteen2.WeekdaysService;


@Path("/Weekdays")
public class RestWeekdays {

	
	private WeekdaysService bks = new WeekdaysService();

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello(Request a) {
		return "REST Server :I'm the Weekdays Controller";
	}

	/**
	 * get Weekday
	 * @return
	 */
	
	@GET
	@Path("/getWeekdays")
	public Response getWeekdays() {		
		List<Weekdays> weekDaysopt = bks.findAllWeekdays();

		return Response.status(Response.Status.OK)
				.entity(weekDaysopt)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	/**
	 * get Weekday by id
	 * @param id
	 * @return
	 */
	
	@GET
	@Path("/getWeekday/{id}")
	public Response getWeekday(@PathParam("id") int id) {
		Weekdays weekDaysopt = bks.findWeekDays(id);
		
		return Response.status(Response.Status.OK)
				.entity(weekDaysopt)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	/**
	 * adding a Weekday
	 * @param menuOption
	 * @return
	 */
	
	@POST
	@Path("/addWeekdays")
	public Response addWeekdays(Weekdays menuOption) {		
		Weekdays weekDaysopt = bks.updateWeekdays(menuOption);
		
		return Response.status(Response.Status.CREATED)
				.entity(weekDaysopt)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	
	/**
	 * update Weekdays
	 * @param weekday
	 * @return
	 */
	
	@PUT
	@Path("/updateWeekdays")
	public Response updateOrder(Weekdays weekday) {
		Weekdays weekDaysopt = bks.updateWeekdays(weekday);
		
		return Response.status(Response.Status.OK)
				.entity(weekDaysopt)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
	

	
	/**
	 * delete a Weekday
	 * @param dayId
	 * @return
	 */
	
	@DELETE
	@Path("/deleteWeekdays/{id}")
	public Response deleteMenuOption(@PathParam("id") int dayId) {
		boolean menuOptionresponse = bks.removeWeekdays(dayId);
		
		return Response.status(Response.Status.OK)
				.entity(menuOptionresponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
