package canteen2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;



public class Test {
	private static final String PERSISTANCE_UNIT_NAME = "LibraryJPA";
	private static EntityManagerFactory emf;
	private static EntityManager em = null;
	

	
	public static void fill() {
		
		
        EntityManager em = getEM();
        List<Customer> customers = null;
		List<MaOrder> orders = null;
		List<MenuOptions> menus = null;
		List<Admin> admins = null;
		List<OperatingHours> hours = null;
		
		//NOVO
		List<Weekdays> weekdays = null;
		
		AdminService adminServ = new AdminService(getEM());
		OperatingHoursService opS = new OperatingHoursService(getEM());
        MenuOptionsService menuOptionsServ = new MenuOptionsService(getEM());
		CustomerService customerServ = new CustomerService(getEM());
        MaOrderService orderServ = new MaOrderService(getEM());
        
        //NOVO
        WeekdaysService weekDayServ = new WeekdaysService(getEM());

        
		
		/*
		em.getTransaction().begin();

        AdminService adminServ = new AdminService(getEM());
        List<Admin> adminList = adminServ.findAllAdmins();
		for (Admin a : adminList) {
			adminServ.removeAdmin(a.getAdminNumber());
		}
        
		
        MenuOptionsService menuOptionsServ = new MenuOptionsService(getEM());
        List<MenuOptions> menuOptionsList = menuOptionsServ.findAllMenuOptions();
		for (MenuOptions m : menuOptionsList) {
			menuOptionsServ.removeMenuOption(m.getMenuNumber());;
		}
        
		
		CustomerService customerServ = new CustomerService(getEM());
		List<Customer> customerList = customerServ.findAllCustomers();
		if (customerList != null) {
		    for (Customer c : customerList) {
		        customerServ.removeCustomer(c.getCustomersId());
		    }
		}
        
		
        MaOrderService orderServ = new MaOrderService(getEM());
        List<MaOrder> orderList = orderServ.findAllOrders();
		for (MaOrder o : orderList) {
			orderServ.removeOrder(o.getOrderId());;
		}
		
		
		OperatingHoursService opS = new OperatingHoursService(getEM());
		List<OperatingHours> opSList = opS.findAllOperatingHours();
		for (OperatingHours op : opSList) {
		    opS.removeOperatingHours(op.getId());
		}

		
		
		em.getTransaction().commit();
		// clean data base
		*/
        
        
        
        // Start adding data to our data base
		em.getTransaction().begin();
        
        // Add admin
        Admin admin1 = adminServ.updateAdmin("Ana", "Antonio", "admin","123", 1, 100,"antonio@gmail.com","94232123912");
        Admin admin2 = adminServ.updateAdmin("Abel","Luis","admin1","123",2,101,"Abel@gmail.com","9873217321");
        System.out.println("Admins added successfully!\n");
        
        // Only admins can add/change Operating hours
        
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm'h'");
            Date openingTimeDate = timeFormat.parse("8:00h");
            Date closingTimeDate = timeFormat.parse("19:00h");
            //Date openingTimeDate1 = timeFormat.parse("6:00h");
            //Date closingTimeDate1 = timeFormat.parse("21:00h");
            //Date openingTimeDate2 = timeFormat.parse("7:00h");
            //Date closingTimeDate2 = timeFormat.parse("20:00h");
            
            OperatingHours operatingH = opS.insertOperatingHours("Monday", openingTimeDate, closingTimeDate, 1);
            OperatingHours operatingH2 = opS.insertOperatingHours("Tuesday", openingTimeDate, closingTimeDate, 2);
            OperatingHours operatingH3 = opS.insertOperatingHours("Wednesday", openingTimeDate, closingTimeDate, 3);
            OperatingHours operatingH4 = opS.insertOperatingHours("Thursday", openingTimeDate, closingTimeDate, 4);
            OperatingHours operatingH5 = opS.insertOperatingHours("Friday", openingTimeDate, closingTimeDate, 5);
            OperatingHours operatingH6 = opS.insertOperatingHours("Saturday", openingTimeDate, closingTimeDate, 6);
            OperatingHours operatingH7 = opS.insertOperatingHours("Sunday", openingTimeDate, closingTimeDate, 7);
            //OperatingHours operatingH2 = opS.insertOperatingHours("Friday", openingTimeDate1, closingTimeDate1, 2);
            //OperatingHours operatingH3 = opS.insertOperatingHours("Sunday", openingTimeDate2, closingTimeDate2, 3);
            System.out.println("Operating hours added successfully!\n");
            
        } catch (Exception e) {
            System.out.println("Error!");
        }

        
        
        // Add menu
        MenuOptions menu1 = menuOptionsServ.updateMenuOptions("Fish", "Fish and chips", "Ice cream", "Water",1);
        MenuOptions menu2 = menuOptionsServ.updateMenuOptions("Meat", "Meat and potatos", "Apple", "Orange Juice",2);
        MenuOptions menu3 = menuOptionsServ.updateMenuOptions("Vegetarian", "Salad with cheese", "Cake", "Wine",3);
        menuOptionsServ.updateMenuOptions("Vegetarian", "Salad with bacon", "Cake", "Wine",3);
        
        System.out.println("Menus added successfully!\n");
        
        
        // Add customer
        
        Customer customer1 = customerServ.updateCustomer("Carlos", "Joao", "customer","123", 1,new ArrayList<>(), 132,"Carlos@gmail.com","984327372","Priority");
        Customer customer2 = customerServ.updateCustomer("Carla", "Manuel", "customer1","123", 2, new ArrayList<>(), 133,"Carla@gmail.com","8182821841","Non-Priority");
        Customer customer3 = customerServ.updateCustomer("Celia", "Joaquim", "customer2","123", 3, new ArrayList<>(), 134,"Celia@gmail.com","9282882822","Priority");
        
        System.out.println("Customers added successfully!\n");
        
        // Add order
        // remover orderId
        
        //double cost, int orderId, String paymentMethod, MenuOptions menu
        
        /*
       
        MaOrder order1 = orderServ.updateOrder(2,5,"Credit Card",menu2);
        MaOrder order2 = orderServ.updateOrder(2,6,"Cash",menu2);
        MaOrder order3 = orderServ.updateOrder(4,7,"Cash",menu3);
        System.out.println("Orders added successfully!\n");
       
        
        
        //MaOrder order4 = orderServ.updateOrder(4, 6,"Cash", menu3);
        //MaOrder order5 = orderServ.updateOrder(5.80, 7,"Cash", menu2);

        // Add order to customer        
        //customer2.getOrders().add(order1);
        //customer3.getOrders().add(order3);
        
        */
        
        /*
        MaOrder order1 = orderServ.updateOrder(2,customer1.getId(),"Credit Card",menu2);
        MaOrder order2 = orderServ.updateOrder(2,customer2.getId(),"Cash",menu2);
        MaOrder order3 = orderServ.updateOrder(4,customer3.getId(),"Cash",menu3);
        System.out.println("Orders added successfully!\n");
        */
        
        MaOrder order4 = orderServ.updateOrder(4, 7,"Cash", menu3);
        MaOrder order5 = orderServ.updateOrder(4, 6,"Cash", menu3);

        // Add order to customer       
        
        customer2.getOrders().add(order4);
        customer3.getOrders().add(order5);
        
        
        
        
     
        System.out.println("Orders added successfully to customers!\n");
        
        
        Weekdays weekday1 = weekDayServ.updateWeekdays(1, "Monday");
        Weekdays weekday2 = weekDayServ.updateWeekdays(2, "Tuesday");
        Weekdays weekday3 = weekDayServ.updateWeekdays(3, "Wednesday");
        Weekdays weekday4 = weekDayServ.updateWeekdays(4, "Thursday");
        Weekdays weekday5 = weekDayServ.updateWeekdays(5, "Friday");
        Weekdays weekday6 = weekDayServ.updateWeekdays(6, "Saturday");
        Weekdays weekday7 = weekDayServ.updateWeekdays(7, "Sunday");

        System.out.println("Weekdays added successfully!\n");
        
        /*
        weekday1.getMenus().add(menu1);
        weekday1.getMenus().add(menu2);
        weekday1.getMenus().add(menu3);
        */
        
        
        
        
        
        
        
		em.getTransaction().commit();

		
		
		
		// print the data in the database  : Customers , Admins , MenuOptions and Orders.
		
		
		
		//Admins
		System.out.println("------------------------");	
		admins = adminServ.findAllAdmins();
		System.out.println("Admins table:");
		for (Admin a : admins) {
			System.out.println(a);				}
		System.out.println("------------------------");	
		
		
		//Customers
		
		customers = customerServ.findAllCustomers();
		System.out.println("Customers table:");
		for (Customer c : customers) {
			System.out.println(c);
		}
		System.out.println("------------------------");
		
		
		//MenuOptions
			
		menus = menuOptionsServ.findAllMenuOptions();
		System.out.println("Menu table:");
		for (MenuOptions m : menus) {
			System.out.println(m);
		}
		System.out.println("------------------------");
		
		
		// Orders
		
		orders = orderServ.findAllOrders();
		System.out.println("Orders table:");
		for (MaOrder o : orders) {
			System.out.println(o);
		}
		System.out.println("------------------------");
		
		
		// Operating hours
		
		hours = opS.findAllOperatingHours();
		System.out.println("Operating hours: ");
		for (OperatingHours oh : hours) {
			System.out.println(oh);
		}
		System.out.println("------------------------");
        
		
		weekdays = weekDayServ.findAllWeekdays();
		System.out.println("Weekdays: ");
		for (Weekdays wk : weekdays) {
			System.out.println(wk);
		}
		System.out.println("------------------------");
        
	
	}
	
	
	
  
    
    
	public static EntityManager getEM() {
		if(em == null) {
			emf = Persistence.createEntityManagerFactory(PERSISTANCE_UNIT_NAME);
			em = emf.createEntityManager();
		}
		return em;
	}
	

	
	
	   // main test
    public static void main(String[] args) {
    	// principal
    	System.setProperty("derby.language.sequence.preallocator", "1");
	    	fill();
    }
    
    
    /*
     * Still adding the same customers ERROR!
     * 
     * I think we need an update for orders
     * 
     * 
     * VALIDATE USING IF´s or something like that
     * 
     * 
     */
    
    
	
}