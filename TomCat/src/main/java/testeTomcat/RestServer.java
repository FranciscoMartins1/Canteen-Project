package testeTomcat;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/")

public class RestServer {
@GET
@Produces (MediaType.TEXT_PLAIN)
public String sayPlainTextHello() {
	return "REST Server : Hello Canteen!";
}

}
