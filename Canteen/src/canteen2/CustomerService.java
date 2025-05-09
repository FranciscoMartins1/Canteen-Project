package canteen2;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;


/**
 * Service class for managing and interacting with customer users in the system.
 */

public class CustomerService {
	
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
	
	/**
     * Constructor for creating a CustomerService instance with an EntityManager.
     *
     * @param em The EntityManager to be used for database interactions.
     */
	
	public CustomerService(EntityManager em) {
		this.em = em;
	}
	
	public CustomerService() {
		this.em = getEM();
	}
	
	/**
     * Update an existing customer user or create a new one if it doesn't exist.
     *
     * @param firstName    
     * @param surname       
     * @param username      
     * @param password     
     * @param orders        
     * @param customersId   
     * @return The updated or newly created Customer instance.
     */
	
	public Customer updateCustomer(String maName, String surname, String username,String password, int id,List<MaOrder>orders,int customersId,String email, String phoneNumber,String typeCustomer) {
		Customer newCustomer = em.find(Customer.class, id);
		if(newCustomer == null) {
			newCustomer = new Customer();			
			em.persist(newCustomer);
		}
	    newCustomer.setFirstName(maName);
	    newCustomer.setSurname(surname);


	    newCustomer.setUsername(username);
	    newCustomer.setPassword(password);
	    newCustomer.setId(id);
	    newCustomer.setCustomersId(customersId);
	    newCustomer.setEmail(email);
	    newCustomer.setPhoneNumber(phoneNumber);
	    newCustomer.setTypeCustomer(typeCustomer);
	    
	    //NOVO
	    newCustomer.setOrders(orders);
	    //FIM NOVO
	    
	    //newCustomer.getOrders().clear();
	    //newCustomer.getOrders().addAll(orders);

	    em.persist(newCustomer);
		
		return newCustomer;
	}
	
	/**
	 * Persists the data
	 * @param customer
	 * @return
	 */

	private boolean saveData(Customer customer) {
		
		try {
			em.getTransaction().begin();
			em.persist(customer);
			em.getTransaction().commit();
			
		} catch (Exception ex) {
			throw new RuntimeException(ex);
			//return false;
		}
		
		return true;
	}

	/**
	 * Add an existing customer user or create a new one if it doesn't exist.
	 * @param customer
	 * @return
	 */

	public Customer updateCustomer(Customer customer) {
		Customer newCustomer = em.find(Customer.class, customer.getId());
		if(newCustomer == null) {
			saveData(customer);
			return customer;
		}
		newCustomer.setFirstName(customer.getFirstName());
		newCustomer.setSurname(customer.getSurname());
		newCustomer.setUsername(customer.getUsername());
		newCustomer.setPassword(customer.getPassword());
		newCustomer.setId(customer.getId());
		newCustomer.setCustomersId(customer.getCustomersId());
		newCustomer.setEmail(customer.getEmail());
		newCustomer.setPhoneNumber(customer.getPhoneNumber());
		newCustomer.setTypeCustomer(customer.getTypeCustomer());
		
		//NOVO
		newCustomer.setOrders(customer.getOrders());
		//FIM DE NOVO
		newCustomer.getOrders().addAll(customer.getOrders());
		
		saveData(newCustomer);
		return newCustomer;
	}

	
	
	

	/**
     * Remove a customer user from the database by customer-specific identifier.
     *
     * @param customersId The customer-specific identifier of the customer user to be removed.
     */
	
	
	public void removeCustomer(int customersId) {
		Customer newCustomer = findCustomer(customersId);
		if(newCustomer != null) {
			em.remove(newCustomer);
		}
	}
	
	/**
	 * Remove a customer user from the database by customer-specific identifier.
	 * @param customersId
	 * @return
	 */
	
	public boolean removeCustomers(int customersId) {
		Customer newCustomer = findCustomer(customersId);
		if(newCustomer != null) {
			em.getTransaction().begin();
			em.remove(newCustomer);
			em.getTransaction().commit();
			return true;
		}
		return false;
	}
	
	
	
	//NOVO

	
	
	
	private boolean authenticate(String username, String password) {
	    // Your authentication logic goes here

	    // Create an instance of AdminService
	    CustomerService customerService = new CustomerService();

	    // Find the admin by username
	    Customer customer = customerService.findCustomerByUsername(username);

	    // Check if the admin exists and the password matches
	    if (customer != null && customer.getPassword().equals(password)) {
	        return true;  // Authentication successful
	    } else {
	        return false; // Authentication failed
	    }
	}
	
	
	
	
	public Customer findCustomerByUsername(String username) {
	    try {
	        Query query = em.createQuery("SELECT a FROM Customer a WHERE a.username = :username", Customer.class);
	        query.setParameter("username", username);
	        return (Customer) query.getSingleResult();
	    } catch (NoResultException e) {
	        return null; // Admin with the given username not found
	    }
	}
	

	
	
	
	//FIM NOVO
	
	/**
     * Find a customer user by customer-specific identifier.
     *
     * @param customersId The customer-specific identifier to search for.
     * @return The found Customer instance, or null if not found.
     */
	
	
	public Customer findCustomer(int customersId) {
		return em.find(Customer.class, customersId);
	}
	
	/**
     * Retrieve a list of all customer users in the system.
     *
     * @return A list of all Customer instances in the database.
     */
	
	
	@SuppressWarnings("unchecked")
	public List<Customer> findAllCustomers() {
		Query qd = em.createQuery("SELECT a FROM Customer a");
		return qd.getResultList();
	}
	
}
