package canteen2;

import org.eclipse.persistence.descriptors.ClassExtractor;
import org.eclipse.persistence.sessions.Session;

/**
 * Custom class extractor used to determine the actual class type based on database row content.
 */

public class UserClassExtractor extends ClassExtractor{
	
	 /**
     * Extracts the class type based on the content of a database row.
     *
     * @param databaseRow 
     * @param sessions   
     * @return The appropriate class type based on the database row content.
     */
	
	@Override
	public Class<?> extractClassFromRow(org.eclipse.persistence.sessions.Record databaseRow , Session sessions){
		if(databaseRow.containsKey("Admin_Specific")) {
			return Admin.class;
		}
		else if(databaseRow.containsKey("Customer_Specific")) {
			return Customer.class;
		}
		else {
			return TypeUser.class;
		}
	}
	
}
