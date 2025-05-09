package canteen2;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


/**
 * This class provides services for managing and interacting with orders in the system.
 */

public class MaOrderService {

    /**
     * Constructor for creating a MaOrderService instance with an EntityManager.
     *
     * @param em The EntityManager to be used for database interactions.
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
	
	public MaOrderService() {
		this.em = getEM();
	}
    
    
    public MaOrderService(EntityManager em) {
        this.em = em;
    }
    
    /**
     * Add an existing order or create a new one if it doesn't exist.
     *
     * @param cost        
     * @param orderId       
     * @param paymentMethod
     * @param menu          
     * @return The updated or newly created MaOrder instance.
     */

    public MaOrder updateOrder(double cost, int orderId, String paymentMethod, MenuOptions menu) {
        MaOrder newOrder = em.find(MaOrder.class, orderId);
        if (newOrder == null) {
            newOrder = new MaOrder();
            em.persist(newOrder);
        }
        newOrder.setCost(cost);
        newOrder.setOrderId(orderId);
        newOrder.setNewMenu(menu);
        newOrder.setPaymentMethod(paymentMethod);
        
        
        em.persist(newOrder);
        
        return newOrder;
    }
    
	
    /**
     * 
     * Persists the data
     * @param maOrderopt
     * @return
     */
	private boolean saveData(MaOrder maOrderopt) {
		
		try {
			em.getTransaction().begin();
			em.persist(maOrderopt);

			em.getTransaction().commit();
		} catch (Exception ex) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Add an existing order or create a new one if it doesn't exist.
	 * @param maOrderopt
	 * @return
	 */
	
	
	
	public MaOrder updateOrderOption(MaOrder maOrderopt) {
		MaOrder b = em.find(MaOrder.class, maOrderopt.getOrderId());

		if (b == null) {
			saveData(maOrderopt);

			return maOrderopt;
		}

		
		b.setCost(maOrderopt.getCost());
		b.setPaymentMethod(maOrderopt.getPaymentMethod());
		b.setNewMenu(maOrderopt.getNewMenu());
		b.setOrderId(maOrderopt.getOrderId());


		saveData(b);

		return b;
	}
    

	

    /**
     * Remove an order from the database by the unique order identifier.
     *
     * @param orderId The unique identifier of the order to be removed.
     */
    

    public void removeOrder(int orderId) {
        MaOrder newOrder = findOrder(orderId);
        if (newOrder != null) {
            em.remove(newOrder);
        }
    }
    
    /**
     * Remove an order from the database by the unique order identifier.
     * @param id
     * @return
     */
    
	
	public boolean removeMaOrder(int id) {
		MaOrder b = findOrder(id);
		if (b != null) {
			em.getTransaction().begin();
			em.remove(b);

			em.getTransaction().commit();

			return true;
		}

		return false;
	}
	
    // NOVO
	
	public List<MaOrder> findOrdersForCustomer(int customerId) {
	    try {
	        em.getTransaction().begin();

	        Query query = em.createQuery("SELECT o FROM MaOrder o WHERE o.customer.id = :customerId");
	        query.setParameter("customerId", customerId);

	        List<MaOrder> maOrders = query.getResultList();

	        em.getTransaction().commit();

	        return maOrders;
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return Collections.emptyList();
	    }
	}
	
	
	
	
	// FIM NOVO

    /**
     * Find an order by its unique order identifier.
     *
     * @param orderId The unique order identifier to search for.
     * @return The found MaOrder instance, or null if not found.
     */
    
    public MaOrder findOrder(int orderId) {
        return em.find(MaOrder.class, orderId);
    }

    /**
     * Retrieve a list of all orders in the system, ordered by order identifier.
     *
     * @return A list of all MaOrder instances in the database, ordered by order identifier.
     */
    
    @SuppressWarnings("unchecked")
    public List<MaOrder> findAllOrders() {
    	Query qd = em.createQuery("SELECT a FROM MaOrder a ORDER BY a.orderId");
        return qd.getResultList();
    }
}

