package canteen2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * This class represents the operating hours for a specific day of the week in the system.
 */

@Entity
public class OperatingHours {
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int id;
	private String dayOfTheWeek;
	private Date openingTime;
	private Date closingTime;

	
	public OperatingHours() {
	
	}


	
	public OperatingHours(String dayOfTheWeek, Date openingTime, Date closingTime, int id) {
	    this.dayOfTheWeek = dayOfTheWeek;
	    this.openingTime = openingTime;
	    this.closingTime = closingTime;
	    this.id = id;
	}
	

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the dayOfTheWeek
	 */
	public String getDayOfTheWeek() {
		return dayOfTheWeek;
	}


	/**
	 * @param dayOfTheWeek the dayOfTheWeek to set
	 */
	public void setDayOfTheWeek(String dayOfTheWeek) {
		this.dayOfTheWeek = dayOfTheWeek;
	}


	/**
	 * @return the openingTime
	 */
	public Date getOpeningTime() {
		return openingTime;
	}


	/**
	 * @param openingTime the openingTime to set
	 */
	public void setOpeningTime(Date openingTime) {
		this.openingTime = openingTime;
	}


	/**
	 * @return the closingTime
	 */
	public Date getClosingTime() {
		return closingTime;
	}


	/**
	 * @param closingTime the closingTime to set
	 */
	public void setClosingTime(Date closingTime) {
		this.closingTime = closingTime;
	}

	/**
	    * Override of the toString method to provide a formatted string representation of the operating hours.
	    *
	    * @return A string representation of the operating hours, including day of the week, opening time, and closing time.
	    */

	 @Override
	 public String toString() {
	    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	    String openingTimeFormatted = timeFormat.format(this.getOpeningTime());
	    String closingTimeFormatted = timeFormat.format(this.getClosingTime());

	    return dayOfTheWeek + ", Opening time: " + openingTimeFormatted +"h" + " , Closing time: " + closingTimeFormatted +"h.";
	 }
	
	
	
	
}
	
	