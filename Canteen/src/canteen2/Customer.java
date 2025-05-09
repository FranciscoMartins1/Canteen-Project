package canteen2;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * This class represents a customer user in the system.
 */

@Entity
@Table(name = "CUSTOMER")	


public class Customer extends TypeUser{
	/**
	 * @param customersId
	 * @param orders
	 */


	/**
     * The list of orders associated with the customer.
     */


	@Column(name="Customer_Specific")
	@Id
	private int customersId;
	private String typeCustomer;
	
	@OneToMany (fetch=FetchType.LAZY) 
	List<MaOrder> orders = new ArrayList<MaOrder>();
	
	/**
	 * Default constructor for creating an instance of the Customer class.
	 */
	
	
	public Customer() {
		
	}


	/**
	 * 
	 * @param maName
	 * @param surname
	 * @param username
	 * @param password
	 * @param id
	 * @param orders
	 * @param customersId
	 * @param email
	 * @param phoneNumber
	 * @param typeCustomer
	 */
	
	public Customer(String maName, String surname, String username, String password, int id, List<MaOrder> orders, int customersId,String email, String phoneNumber,String typeCustomer) {
        super(maName, surname, username, password, id,email,phoneNumber);
        this.orders = orders;
        this.customersId = customersId;
        this.typeCustomer=typeCustomer;
    }
	
	
	
	

	/**
	 * @return the customersId
	 */
	public int getCustomersId() {
		return customersId;
	}



	/**
	 * @param customersId the customersId to set
	 */
	public void setCustomersId(int customersId) {
		this.customersId = customersId;
	}



	/**
	 * @return the orders
	 */
	public List<MaOrder> getOrders() {
		return orders;
	}



	/**
	 * @param orders the orders to set
	 */
	public void setOrders(List<MaOrder> orders) {
		this.orders = orders;
	}

	/**
     * Override of the toString method to provide a string representation of the Customer user.
     *
     * @return A string representation of the Customer user, including the customer-specific identifier and orders.
     */
	
	
	/**
	 * @return the typeCustomer
	 */
	public String getTypeCustomer() {
		return typeCustomer;
	}




	/**
	 * @param typeCustomer the typeCustomer to set
	 */
	public void setTypeCustomer(String typeCustomer) {
		this.typeCustomer = typeCustomer;
	}
	

	@Override
	public String toString() {
		return super.toString()+ " ,"+typeCustomer+", customers Id=" + customersId + ", orders= " + orders;
	}



}