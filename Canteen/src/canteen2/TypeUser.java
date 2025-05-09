/**
 * 
 */
package canteen2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;


import org.eclipse.persistence.annotations.ClassExtractor;

@ClassExtractor(UserClassExtractor.class)

@Entity
@Table(name = "TYPEUSER")
@Inheritance(strategy = InheritanceType.JOINED)

/**
 * Abstract base class for representing different types of users in the system.
 */

public abstract class TypeUser {
	/**
	 * @param password
	 * @param FirstName
	 * @param surname
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String firstName;
	private String surname;
	private String username;
	private String password;
	private String email;
	private String phoneNumber;
	
	public TypeUser() {
		
	}


	public TypeUser(String maName, String surname, String username, String password, int id,String email,String phoneNumber) {
        this.firstName = maName;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.id = id;
        this.email=email;
        this.phoneNumber=phoneNumber;
    }
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String maName) {
		this.firstName = maName;
	}
	
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}


	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	
	
	/**
     * Override of the toString method to provide a string representation of the user.
     *
     * @return A string representation of the user, including first name, surname, ID, username, and password.
     */
	
	
	@Override
	public String toString() {
		return "First name = " + firstName + ", surname = " + surname + ", id : " + id +", username = " + username + ", email:"+ email +", phone number: "+phoneNumber+", password = " + password;
	}






	
	

}
