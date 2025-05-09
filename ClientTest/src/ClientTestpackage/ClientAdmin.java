
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

// uma classe para cada
import canteen2.Admin;
import canteen2.MenuOptions;

public class ClientAdmin {
// é nesta classe que se faz a interface
	
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/Admin/").build();
	}

	private static ClientConfig config = new ClientConfig();
	private static Client client = ClientBuilder.newClient(config);
	private static WebTarget service = client.target(getBaseURI());

	public static void main(String[] args) {
		//getAdmins();
		//getAdminById(1);
		//deleteAdmin(1);
		addAdmin();
		
		// launch(args);
		
		
		// adicionar colunas em tabelas
		
		// TableView tableView = new TAbleView();
		// TableColumn <Book,String> clumn = new TableColumn<>("Is available?")
		
		
		// filltableVIew para preencher com os nomes , primeiro temos de limpar
		//
	}

	/**
	 * Add admin
	 * 
	 * 
	 */
	
	private static void addAdmin() {
		Admin newAdmin = new Admin("Andre","Salvador","Saltos","4444",145,1005,"Andre@gmail.com","918272382");
		Response response = service.path("addAdmin").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(newAdmin, MediaType.APPLICATION_JSON), Response.class);

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getAdmins();
		}

	}
	
	/**
	 *Delete admin with id 
	 * 
	 * @param adminId
	 */
	
	private static void deleteAdmin(int adminId) {
		Response response = service.path("deleteAdmin")
				.path(Integer.toString(adminId)).request().delete();

		if (response.getStatus() < 200 && response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} else {
			getAdmins();
		}

	}

	/**
	 *Get the admins 
	 * 
	 * 
	 */
	
	private static void getAdmins() {
		Gson gson = new Gson();

		String responseAdminsList = service.path("getAdmins").request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		List<Admin> admins = Arrays.asList(gson.fromJson(responseAdminsList, Admin[].class));
		System.out.println("\n Output JSON from Server getAdmins .... \n");
		for (Admin b : admins) {
			System.out.println(b);
		}

	}

	/**
	 * Get admin with id
	 * 
	 * @param adminId
	 */
	
	private static void getAdminById(int adminId) {
		String responseAdmin = service.path("getAdmin").path(Integer.toString(adminId)).request()
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		Gson gson = new Gson();
		Admin admin = gson.fromJson(responseAdmin, Admin.class);
		System.out.println("\n Output JSON from Server getAdminById .... \n");
		System.out.println(admin);

	}

}


