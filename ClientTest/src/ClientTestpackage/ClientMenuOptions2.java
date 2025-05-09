package ClientTestpackage;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.google.gson.Gson;

import canteen2.Admin;
import canteen2.MenuOptions;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ClientMenuOptions2 extends Application {
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/MenuOptions/").build();
		
	}

	private static ClientConfig config = new ClientConfig();
	private static Client client = ClientBuilder.newClient(config);
	private static WebTarget service = client.target(getBaseURI());
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		
		//FlowPane root = new FlowPane();
		GridPane root = new GridPane();
		
		
		/* We will use the TableView component to list the books */
		
		
		TableView tableView = new TableView();

		
		/* Each of the columns must be linked to a colum in your model class (Book) - 
		 * Pay attention to the type
		 */	
		
        TableColumn<MenuOptions, String> column1 = new TableColumn<>("Plate Name");
        column1.setMinWidth(200);
        column1.setCellValueFactory(new PropertyValueFactory<>("plateName"));

        TableColumn<MenuOptions, String> column2 = new TableColumn<>("Dessert Name");
        column1.setMinWidth(200);
        column2.setCellValueFactory(new PropertyValueFactory<>("dessertName"));
        
        TableColumn<MenuOptions, String> column3 = new TableColumn<>("Drink Name");
        column1.setMinWidth(200);
        column3.setCellValueFactory(new PropertyValueFactory<>("drinkName"));
        
        TableColumn<MenuOptions, String> column4 = new TableColumn<>("Type food");
        column1.setMinWidth(200);
        column4.setCellValueFactory(new PropertyValueFactory<>("typeFood"));
        

        /* Add the columns to the table view */
        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        
        
        // Load objects into table calling the REST service
        fillTableView(tableView);

        
        /*This command line gets the selected row and the corresponding Model Instance (Book)*/
        TableView.TableViewSelectionModel<MenuOptions> selectionModel = tableView.getSelectionModel();

        
        /* You can choose between single and multiple selection*/
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        //selectionModel.setSelectionMode(SelectionMode.MULTIPLE);

        
        /* Here is a list for the selected items in the table view */
        ObservableList<MenuOptions> selectedItems = selectionModel.getSelectedItems();

        /* In case you need to check the selected item when it is changed */
        selectedItems.addListener(new ListChangeListener<MenuOptions>() {
            @Override
            public void onChanged(Change<? extends MenuOptions> change) {
                System.out.println("Selection changed: " + change.getList());
            }
        });

        /* Select the first item of the table view */
        selectionModel.select(0);

        /* In case if you need to get the index of the table view */
        ObservableList<Integer> selectedIndices = selectionModel.getSelectedIndices();
        
        //tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        
        //root.getChildren().add(tableView);  
        root.add(tableView, 0, 0, 4, 1);
		
        Button btnNew = new Button("New");
		Button btnEdit = new Button("Edit");	
		Button btnDelete = new Button("Delete");
		Button btnCancel = new Button("Cancel");
		
		btnNew.setOnAction(ae -> { 
			System.out.println("New menu data... ");
			showAddUpdateMenuStage(primaryStage, new MenuOptions()); 
			fillTableView(tableView);
        });
						
		btnEdit.setOnAction(ae -> {
			System.out.println("Editing data... ");			
			showAddUpdateMenuStage(primaryStage, selectedItems.get(0));
			fillTableView(tableView);
		});
		
		btnDelete.setOnAction(ae -> {
			System.out.println("Deleting data... ");				

			if (showConfirmationDialog("Are you sure you want to delete the menu? ")) {
				deleteMenuOption(selectedItems.get(0).getMenuNumber());
				fillTableView(tableView);
			}			
			
		});
		
		btnCancel.setOnAction(ae -> {
			System.out.println("Cancelling...");
			fillTableView(tableView);
		});
		
				
		//root.getChildren().addAll(new Button[] { btnNew, btnEdit, btnDelete, btnCancel});
		root.add(btnNew, 0, 1, 1, 1);
		root.add(btnEdit, 1, 1, 1, 1);
		root.add(btnDelete, 2, 1, 1, 1);
		root.add(btnCancel, 3, 1, 1, 1);
		
		root.setHgap(5);
		root.setVgap(5);
						
		Scene scene = new Scene(root);
		primaryStage.setTitle("Simple Menu CRUD example");
		primaryStage.setScene(scene);
		
		primaryStage.setX(300);
		primaryStage.setY(300);
		primaryStage.setWidth(500);
		primaryStage.setHeight(500);
		
		primaryStage.show();
	}
	
	private void fillTableView(TableView tableView) {
        tableView.getItems().clear();
        List<MenuOptions> menus = getMenuOptions();
        for (MenuOptions b : menus) {
        	tableView.getItems().add(b);
		}
	}

	private List<MenuOptions> getMenuOptions() {

		// Get the Books
		Gson gson = new Gson();

		String responseAdminsList = service.path("getMenuOptions")
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get(String.class);

		List<MenuOptions> menus = Arrays.asList(gson.fromJson(responseAdminsList, MenuOptions[].class));
				
		return menus;

	}
	
	
	
	private boolean deleteMenuOption(int menuOptionId) {

		// Delete menu with id 1
		Response response = service.path("deleteMenuOption")
				.path(Integer.toString(menuOptionId)).request().delete();

		if (response.getStatus() < 200 || response.getStatus() > 299) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		} 
		
		return response.getStatus() == 200;

	}
	

	
    private void showAddUpdateMenuStage(Stage primaryStage, MenuOptions menu) {
    	    	   	
    	Stage stage = new Stage();
        
    	stage.setTitle("Add or Update menu - in Modal Mode");
        stage.setX(300);
        stage.setY(300);
        stage.setWidth(300);
        stage.setHeight(300);
        
		//FlowPane root = new FlowPane();
		GridPane root = new GridPane();

		
		
		Label lblDrink = new Label("Drink");
		
		root.add(lblDrink, 0, 1);
		//root.getChildren().add(lblTitle);

		TextField txtDrinkName = new TextField();
		lblDrink.setId("lblDrink");
		root.add(txtDrinkName, 1,1);
		//root.getChildren().add(txtTitle);
		

		Label lblPlateName = new Label("Plate name");
		root.add(lblPlateName, 0, 2);
		//root.getChildren().add(lblTitle);

		TextField txtPlateName = new TextField();
		lblPlateName.setId("lblPlateName");
		root.add(txtPlateName, 1,2);
		//root.getChildren().add(txtTitle);
		

		Label lblDessert = new Label("Dessert");
		//root.getChildren().add(lblAuthor);
		root.add(lblDessert, 0,3);

		TextField txtDessert = new TextField();
		txtDessert.setId("txtDesert");
		root.add(txtDessert, 1,3);
		//root.getChildren().add(txtAuthor);
		

		Label lblTypeFood = new Label("Type food");
		root.add(lblTypeFood, 0, 4);
		//root.getChildren().add(lblIsAvailable);

		TextField txtTypeFood = new TextField();
		txtTypeFood.setId("typeFood");
		root.add(txtTypeFood,  1,4);
		//root.getChildren().add(ckIsAvailable);
		


		Button btnSave = new Button("Save");
		Button btnCancel = new Button("Cancel");
		root.add(btnSave, 0, 5);
		root.add(btnCancel, 1,5);
		
		root.setHgap(5);
		root.setVgap(5);
		
		
		if (menu.getMenuNumber()!= 0) {
			txtDrinkName.setText(menu.getDrinkName());
			txtDessert.setText(menu.getDessertName());
			txtTypeFood.setText(menu.getTypeFood());
			txtPlateName.setText(menu.getPlateName());
			
		}

		btnSave.setOnAction(ae -> {
			System.out.println("Saving data... " + menu);
			
			menu.setDrinkName(txtDrinkName.getText());
			menu.setDessertName(txtDessert.getText());
			menu.setTypeFood(txtTypeFood.getText());
			menu.setPlateName(txtPlateName.getText());
			
			saveData(menu);
			stage.close();
		});

		btnCancel.setOnAction(ae -> {
			System.out.println("Cancelling...");
			cleanFields(root);
			stage.close();
		});

		//root.getChildren().addAll(new Button[] { btnSave, btnCancel });
		
		root.setAlignment(Pos.CENTER);

		Scene scene = new Scene(root);
		stage.setTitle("Simple Input Screen Example");
		stage.setScene(scene);
		      
        
        stage.initOwner(primaryStage);
        //stage.initModality(Modality.NONE);
        stage.initModality(Modality.WINDOW_MODAL);
        //stage.initModality(Modality.APPLICATION_MODAL);


        stage.showAndWait();
    }
    
    private void saveData(MenuOptions menu) {		
		try {
			
			Response response;
			String message = "Menu added successfully.";
			
			if (menu.getMenuNumber() != 0) {
				
				message = "Menu updated successfully.";				
				response = service.path("updateMenuOption").request(MediaType.APPLICATION_JSON)
						.put(Entity.entity(menu, MediaType.APPLICATION_JSON), Response.class);
			} else {
				
				message = "Menu added successfully.";
				response = service.path("addMenuOptions").request(MediaType.APPLICATION_JSON)
						.post(Entity.entity(menu, MediaType.APPLICATION_JSON), Response.class);
			}		
			
			if (response.getStatus() < 200 || response.getStatus() > 299) {			
				showMessage("Failed : HTTP error code : " + response.getStatus(), AlertType.ERROR);
				//throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}else {
				showMessage(message, AlertType.INFORMATION);
			}
		} catch (Exception e) {
			showMessage("Error while saving the menu.", AlertType.ERROR);
			//throw new RuntimeException("Failed to save the menu.");
		}
	}
	
	private void cleanFields(GridPane root) {
		
		for (Node node : root.getChildren()) {
		    System.out.println("Id: " + node.getId());
		    if (node instanceof TextField) {
		        // clear
		        ((TextField)node).setText("");
		    } else if (node instanceof CheckBox) {
		        // clear
		        ((CheckBox)node).setSelected(false);
		    }
		}
		
	}
	
	private void showMessage(String message, AlertType alertType) {
		Alert alert = new Alert(alertType);
        alert.setTitle("Menu Application");
        alert.setHeaderText(message);
        //alert.setContentText(message);
        alert.showAndWait();
	}
	
	
	private boolean showConfirmationDialog(String confirmationMessage) {
		ButtonType okBtn = new ButtonType("Yes", ButtonData.OK_DONE);
		ButtonType closeBtn = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(AlertType.WARNING,
		        confirmationMessage,
		        okBtn,
		        closeBtn);

		alert.setTitle("Delete menu warning");
		Optional<ButtonType> result = alert.showAndWait();

		return (result.orElse(closeBtn) == okBtn);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}