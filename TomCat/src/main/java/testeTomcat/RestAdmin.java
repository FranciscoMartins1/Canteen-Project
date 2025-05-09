package testeTomcat;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import canteen2.Admin;
import canteen2.AdminService;

@Path("/Admin")
public class RestAdmin {

    private AdminService adminService = new AdminService();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "REST Server: I'm the Admin Controller";
    }

    /**
     * Get all admins
     * 
     * @return Response with a list of all admins in JSON format
     */
    @GET
    @Path("/getAdmins")
    public Response getAdmins() {
        List<Admin> admins = adminService.findAllAdmins();
        return Response.status(Response.Status.OK)
                .entity(admins)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * Get admin by ID
     * 
     * @param id Admin ID
     * @return Response with the admin details in JSON format
     */
    @GET
    @Path("/getAdmin/{id}")
    public Response getAdmin(@PathParam("id") int id) {
        Admin adminResponse = adminService.findAdmin(id);

        if (adminResponse != null) {
            return Response.status(Response.Status.OK)
                    .entity(adminResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Admin not found for ID: " + id)
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }

    /**
     * Add a new admin
     * 
     * @param admin Admin object to be added
     * @return Response with the added admin details in JSON format
     */
    @POST
    @Path("/addAdmin")
    public Response addAdmin(Admin admin) {
        Admin addedAdmin = adminService.updateAdmins(admin);

        return Response.status(Response.Status.CREATED)
                .entity(addedAdmin)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    /**
     * Delete an admin by ID
     * 
     * @param id Admin ID
     * @return Response indicating success or failure
     */
    @DELETE
    @Path("/deleteAdmin/{id}")
    public Response deleteAdmin(@PathParam("id") int id) {
        boolean adminRemoved = adminService.removeAdmins(id);

        if (adminRemoved) {
            return Response.status(Response.Status.OK)
                    .entity("Admin with ID " + id + " deleted successfully")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Admin not found for ID: " + id)
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
}
