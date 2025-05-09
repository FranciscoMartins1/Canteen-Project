package canteen2;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

/**
 * 
 */

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * 
 */
@Entity
public class Weekdays {
	
	/**
	 * @param dayID
	 * @param dayName
	 * @param menus
	 */


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dayID;
	private String dayName;

	
	@OneToMany (fetch=FetchType.LAZY) 
	List<MenuOptions> menus = new ArrayList<MenuOptions>();

	
	public Weekdays() {
		
	}
	
	public Weekdays(int dayID, String dayName, List<MenuOptions> menus) {
		this.dayID = dayID;
		this.dayName = dayName;
		this.menus = menus;
	}

	
	/**
	 * @return the dayID
	 */
	
	public int getDayID() {
		return dayID;
	}

	/**
	 * @param dayID the dayID to set
	 */
	public void setDayID(int dayID) {
		this.dayID = dayID;
	}

	/**
	 * @return the dayName
	 */
	public String getDayName() {
		return dayName;
	}

	/**
	 * @param dayName the dayName to set
	 */
	public void setDayName(String dayName) {
		this.dayName = dayName;
	}

	/**
	 * @return the menus
	 */
	public List<MenuOptions> getMenus() {
		return menus;
	}

	/**
	 * @param menus the menus to set
	 */
	public void setMenus(List<MenuOptions> menus) {
		this.menus = menus;
	}
	
	@Override
	public String toString() {
		return "dayId= " + dayID + ", dayName = " + dayName;
	}
	
}
