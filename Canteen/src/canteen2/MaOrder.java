/**
 * 
 */
package canteen2;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;





/**
 * This class represents an order in the system.
 */

@Entity
public class MaOrder {
	/**
	 * 
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderId;

	private double cost;
	private String paymentMethod;
	
	@OneToOne
	MenuOptions newMenu;
	

	
	public MaOrder() {
	
	}


	
	public MaOrder(double cost, int orderId, String paymentMethod, MenuOptions newMenu) {
        this.cost = cost;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.newMenu = newMenu;
    }
	

	
	
	/**
	 * @return the orderId
	 */
	public int getOrderId() {
		return orderId;
	}



	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}



	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}



	/**
	 * @param cost the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}



	/**
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}



	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}



	/**
	 * @return the newMenu
	 */
	public MenuOptions getNewMenu() {
		return newMenu;
	}



	/**
	 * @param newMenu the newMenu to set
	 */
	public void setNewMenu(MenuOptions newMenu) {
		this.newMenu = newMenu;
	}
	

	/**
     * Override of the toString method to provide a string representation of the order.
     *
     * @return A string representation of the order, including order ID, menu, cost, and payment method.
     */

	@Override
	public String toString() {
		return "Order "+orderId + "- Menu: "+ newMenu + " Cost: " + cost + ", payment method: " + paymentMethod;
	}









}