package canteen2;

import java.util.List;

/**
 * 
 */

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * 
 */
public class WeekdaysService {

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
     * Constructor for creating a Weekdays instance with an EntityManager.
     *
     * @param em The EntityManager to be used for database interactions.
     */
	
	
	public WeekdaysService(EntityManager em) {
		this.em = em;
	}
	
	public WeekdaysService() {
		this.em = getEM();
	}
	
	/**
     * Update an existing weekday or create a new one if it doesn't exist.
     *
     * @param
     * @return The updated or newly created MenuOptions instance.
     */
	
	
	public Weekdays updateWeekdays(int dayID, String dayName) {
		Weekdays newWeekday = em.find(Weekdays.class, dayID);
		if(newWeekday == null) {
			newWeekday = new Weekdays();			
			em.persist(newWeekday);
		}
		newWeekday.setDayID(dayID);
		newWeekday.setDayName(dayName);
		
		em.persist(newWeekday);
		
		return newWeekday;
	}
	
	
	/**
	 * Persists the data
	 * @param weekDayopt
	 * @return
	 */
	
	private boolean saveData(Weekdays weekDayopt) {
		
		try {
			em.getTransaction().begin();
			em.persist(weekDayopt);

			em.getTransaction().commit();
		} catch (Exception ex) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Update an existing weekday or create a new one if it doesn't exist.
	 * @param weekday
	 * @return
	 */
	public Weekdays updateWeekdays(Weekdays weekday) {
		Weekdays b = em.find(Weekdays.class, weekday.getDayID());

		if (b == null) {
			saveData(weekday);

			return weekday;
		}

		
		b.setDayID(weekday.getDayID());
		b.setDayName(weekday.getDayName());
		b.setMenus(weekday.getMenus());

		saveData(b);

		return b;
	}
	
	/**
     * Remove a weekday from the database by the unique day number.
     *
     * @param dayId The unique identifier of the weekday to be removed.
     */
	
	public void removeWeekday(int dayID) {
		Weekdays newWeekday = findWeekDays(dayID);
		if(newWeekday != null) {
			em.remove(newWeekday);
		}
	}

	
	/**
	 * Remove a weekday from the database by the unique day number.
	 * 
	 * @param dayId
	 * @return
	 */
	public boolean removeWeekdays(int dayId) {
		Weekdays b = findWeekDays(dayId);
		if (b != null) {
			em.getTransaction().begin();
			em.remove(b);

			em.getTransaction().commit();

			return true;
		}

		return false;
	}
	
	
	 /**
     * Find a weekday by its unique day number.
     *
     * @param dayId The unique weekday to search for.
     * @return The found Weekdays instance, or null if not found.
     */

	public Weekdays findWeekDays(int dayID) {
		return em.find(Weekdays.class, dayID);
	}
	
	 /**
     * Retrieve a list of all weekdays options in the system.
     *
     * @return A list of all Weekdays instances in the database.
     */
	
	@SuppressWarnings("unchecked")
	public List<Weekdays> findAllWeekdays() {
		Query qd = em.createQuery("SELECT a FROM Weekdays a ORDER BY a.dayID",Weekdays.class);
		return qd.getResultList();
	}
}
