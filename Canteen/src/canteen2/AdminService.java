package canteen2;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;




/**
 * Service class for managing and interacting with admin users in the system.
 */

public class AdminService {

	/**
     * Constructor for creating an AdminService instance with an EntityManager
     *
     * @param em The EntityManager to be used for database interactions
     */
	
	private static final String PERSISTENCE_UNIT_NAME = "LibraryJPA";
	private static EntityManagerFactory factory;
	private static EntityManager em = null;

	private static EntityManager getEM() {
		if (em == null) {
			factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			em = factory.createEntityManager();
		}
		return em;
	}
	
	public AdminService() {
		this.em = getEM();
	}
	
	public AdminService(EntityManager em) {
		this.em = em;
	}
	
	
	/**
     * Update an existing admin user or create a new one if it doesn't exist.
     *
     * @param firstName    
     * @param surname      
     * @param username     
     * @param password   
     * @param adminNumber  
     * @return The updated or newly created Admin instance.
     */
	
	public Admin updateAdmin(String maName, String surname, String username, String password,int id,int adminNumber,String email, String phoneNumber) {
		Admin newAdmin = em.find(Admin.class, id);
		if(newAdmin == null) {
			newAdmin = new Admin();
			em.persist(newAdmin);
		}

		newAdmin.setFirstName(maName);
        newAdmin.setSurname(surname);
        newAdmin.setUsername(username);
        newAdmin.setPassword(password);
        newAdmin.setId(id);
        newAdmin.setAdminNumber(adminNumber);
        newAdmin.setEmail(email);
        newAdmin.setPhoneNumber(phoneNumber);
		
		em.persist(newAdmin);
		
		return newAdmin;
	}
	
	
	/**
	 * Persists the data
	 * @param adminOpt
	 * @return
	 */
		private boolean saveData(Admin adminOpt) {
			
			try {
				em.getTransaction().begin();
				em.persist(adminOpt);

				em.getTransaction().commit();
			} catch (Exception ex) {
				return false;
			}
			
			return true;
		}
		
		
		/**
		 * 
		 * Add an existing admin user or create a new one if it doesn't exist.
		 * @param adminOpt
		 * @return
		 */
		public Admin updateAdmins(Admin adminOpt) {
			Admin b = em.find(Admin.class, adminOpt.getId());

			if (b == null) {
				saveData(adminOpt);

				return adminOpt;
			}

			
			b.setAdminNumber(adminOpt.getAdminNumber());
			b.setFirstName(adminOpt.getFirstName());
			b.setId(adminOpt.getId());
			b.setPassword(adminOpt.getFirstName());
			b.setSurname(adminOpt.getFirstName());
			b.setUsername(adminOpt.getFirstName());
			b.setEmail(adminOpt.getEmail());
			b.setPhoneNumber(adminOpt.getPhoneNumber());



			saveData(b);

			return b;
		}
		

	/**
     * Remove an admin user from the database by admin number.
     *
     * @param adminNumber The admin number of the admin user to be removed.
     */
	
	
	public void removeAdmin(int adminNumber) {
		Admin newAdmin = findAdmin(adminNumber);
		if(newAdmin != null) {
			em.remove(newAdmin);
		}
	}
	
	
	
	/**
     * Remove an admin user from the database by id.
     *
     * @param id The admin number of the admin user to be removed.
     */
	
	public boolean removeAdmins(int id) {
		Admin b = findAdmin(id);
		if (b != null) {

			em.getTransaction().begin();
			em.remove(b);

			em.getTransaction().commit();

			return true;
		}

		return false;
	}
	
	
	
	// NOVO

	

	
	
	
	
	private boolean authenticate(String username, String password) {
	    // Your authentication logic goes here

	    // Create an instance of AdminService
	    AdminService adminService = new AdminService();

	    // Find the admin by username
	    Admin admin = adminService.findAdminByUsername(username);

	    // Check if the admin exists and the password matches
	    if (admin != null && admin.getPassword().equals(password)) {
	        return true;  // Authentication successful
	    } else {
	        return false; // Authentication failed
	    }
	}
	
	
	
	
	public Admin findAdminByUsername(String username) {
	    try {
	        Query query = em.createQuery("SELECT a FROM Admin a WHERE a.username = :username", Admin.class);
	        query.setParameter("username", username);
	        return (Admin) query.getSingleResult();
	    } catch (NoResultException e) {
	        return null; // Admin with the given username not found
	    }
	}
	
	
	// FIM DE NOVO
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
     * Find an admin user by admin number.
     *
     * @param adminNumber 
     * @return The found Admin instance, or null if not found.
     */
	
	public Admin findAdmin(int adminNumber) {
		return em.find(Admin.class, adminNumber);
	}
	
	/**
     * Retrieve a list of all admin users in the system.
     *
     * @return A list of all Admin instances in the database.
     */
	
	@SuppressWarnings("unchecked")
	public List<Admin> findAllAdmins() {
		Query qd = em.createQuery("SELECT a FROM Admin a ORDER BY a.adminNumber");
		return qd.getResultList();
	}
}
