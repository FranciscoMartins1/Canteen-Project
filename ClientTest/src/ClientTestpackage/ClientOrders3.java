package ClientTestpackage;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.glassfish.jersey.client.ClientConfig;

import com.google.gson.Gson;

import canteen2.MaOrder;
import canteen2.MenuOptions;

public class ClientOrders3 extends Application {

    // MaOrder service URI
    private static URI getMaOrderBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/MaOrder").build();
    }

    // MenuOptions service URI
    private static URI getMenuOptionsBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/MenuOptions").build();
    }

    private static ClientConfig config = new ClientConfig();

    private static Client maOrderClient = ClientBuilder.newClient(config);
    private static WebTarget maOrderService = maOrderClient.target(getMaOrderBaseURI());

    private static Client menuOptionsClient = ClientBuilder.newClient(config);
    private static WebTarget menuOptionsService = menuOptionsClient.target(getMenuOptionsBaseURI());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();

        TableView<MaOrder> tableView = new TableView<>();

        TableColumn<MaOrder, Double> column1 = new TableColumn<>("Cost");
        column1.setMinWidth(200);
        column1.setCellValueFactory(new PropertyValueFactory<>("cost"));

        TableColumn<MaOrder, String> column2 = new TableColumn<>("Payment method");
        column2.setMinWidth(200);
        column2.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        tableView.getColumns().addAll(column1, column2);

        ComboBox<MenuOptions> menuOptionsComboBox = new ComboBox<>();
        menuOptionsComboBox.setPromptText("Select Menu Option");

        // Fetch menu options from the server and populate the ComboBox

        root.add(tableView, 0, 0, 2, 1);
       
        Button btnNew = new Button("New");
        Button btnEdit = new Button("Edit");
        Button btnDelete = new Button("Delete");
        Button btnCancel = new Button("Cancel");

        btnNew.setOnAction(ae -> {
            MaOrder newOrder = new MaOrder();
            showAddUpdateOrderStage(primaryStage, newOrder, menuOptionsComboBox);
            fillTableView(tableView);
        });

        btnEdit.setOnAction(ae -> {
            MaOrder selectedOrder = tableView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                showAddUpdateOrderStage(primaryStage, selectedOrder, menuOptionsComboBox);
                fillTableView(tableView);
            } else {
                showMessage("Please select an order to edit.", Alert.AlertType.INFORMATION);
            }
        });

        btnDelete.setOnAction(ae -> {
            MaOrder selectedOrder = tableView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                if (showConfirmationDialog("Are you sure you want to delete the order?")) {
                    deleteMaOrder(selectedOrder.getOrderId());
                    fillTableView(tableView);
                }
            } else {
                showMessage("Please select an order to delete.", Alert.AlertType.INFORMATION);
            }
        });

        btnCancel.setOnAction(ae -> fillTableView(tableView));

        root.add(btnNew, 0, 1, 1, 1);
        root.add(btnEdit, 1, 1, 1, 1);
        root.add(btnDelete, 2, 1, 1, 1);
        root.add(btnCancel, 3, 1, 1, 1);

        root.setHgap(5);
        root.setVgap(5);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Simple order CRUD example");
        primaryStage.setScene(scene);

        primaryStage.setX(300);
        primaryStage.setY(300);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);

        primaryStage.show();
    }

    private void fillTableView(TableView<MaOrder> tableView) {
        tableView.getItems().clear();
        List<MaOrder> orders = getMaOrders();
        tableView.getItems().addAll(orders);
    }

    private void fillMenuOptionsComboBox(ComboBox<MenuOptions> comboBox) {
        List<MenuOptions> menuOptions = getMenuOptions();
        comboBox.getItems().addAll(menuOptions);

        comboBox.setCellFactory(param -> new ListCell<MenuOptions>() {
            @Override
            protected void updateItem(MenuOptions item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        comboBox.getSelectionModel().selectFirst();
    }

    private List<MaOrder> getMaOrders() {
        Gson gson = new Gson();
        String responseOrdersList = maOrderService.path("getMaOrders")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        return Arrays.asList(gson.fromJson(responseOrdersList, MaOrder[].class));
    }

	private List<MenuOptions> getMenuOptions() {
        Gson gson = new Gson();
        String responseMenuOptionsList = menuOptionsService.path("getMenuOptions")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        return Arrays.asList(gson.fromJson(responseMenuOptionsList, MenuOptions[].class));
    }

	private boolean deleteMaOrder(int maOrderId) {
	    Response response = maOrderService.path("deleteOrder").path(Integer.toString(maOrderId)).request().delete();
	    return response.getStatus() == 200;
	}

	private void showAddUpdateOrderStage(Stage primaryStage, MaOrder maOrder,
			ComboBox<MenuOptions> menuOptionsComboBox) {
		Stage stage = new Stage();
		stage.setTitle("Add or Update order - in Modal Mode");
		stage.setX(300);
		stage.setY(300);
		stage.setWidth(300);
		stage.setHeight(300);

		GridPane root = new GridPane();

		Label lblCost = new Label("Cost");
		root.add(lblCost, 0, 1);
		TextField txtCost = new TextField();
		root.add(txtCost, 1, 1);

		Label lblPaymentMethod = new Label("Payment Method");
		root.add(lblPaymentMethod, 0, 2);
		TextField txtPaymentMethod = new TextField();
		root.add(txtPaymentMethod, 1, 2);

		Button btnSave = new Button("Save");
		Button btnCancel = new Button("Cancel");
		root.add(btnSave, 0, 5);
		root.add(btnCancel, 1, 5);

		root.setHgap(5);
		root.setVgap(5);

		if (maOrder.getOrderId() != 0) {
			double cost = maOrder.getCost();
			txtCost.setText(String.valueOf(cost));
			txtPaymentMethod.setText(maOrder.getPaymentMethod());
		}

		btnSave.setOnAction(ae -> {
			try {
				double cost = Double.parseDouble(txtCost.getText());
				maOrder.setCost(cost);
				maOrder.setPaymentMethod(txtPaymentMethod.getText());

				// Set the selected menu option to the MaOrder
				maOrder.setNewMenu(menuOptionsComboBox.getValue());

				saveData(maOrder);
				stage.close();
			} catch (NumberFormatException e) {
				showMessage("Invalid cost format.", Alert.AlertType.ERROR);
			}
		});

		btnCancel.setOnAction(ae -> {
			cleanFields(root);
			stage.close();
		});

		root.setAlignment(Pos.CENTER);

		Scene scene = new Scene(root);
		stage.setScene(scene);

		stage.initOwner(primaryStage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.showAndWait();
	}

	private void saveData(MaOrder maOrder) {
	    try {
	        Response response;
	        String message = "Order added successfully.";

	        response = maOrderService.path("addMaOrder").request(MediaType.APPLICATION_JSON)
	                .post(Entity.entity(maOrder, MediaType.APPLICATION_JSON), Response.class);

	        if (response.getStatus() < 200 || response.getStatus() > 299) {
	            showMessage("Failed : HTTP error code : " + response.getStatus(), Alert.AlertType.ERROR);
	        } else {
	            showMessage(message, Alert.AlertType.INFORMATION);
	        }
	    } catch (Exception e) {
	        showMessage("Error while saving the order.", Alert.AlertType.ERROR);
	    }
	}

	private void cleanFields(GridPane root) {
		for (Node node : root.getChildren()) {
			if (node instanceof TextField) {
				((TextField) node).setText("");
			}
		}
	}

	private void showMessage(String message, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle("Order Application");
		alert.setHeaderText(message);
		alert.showAndWait();
	}

	private boolean showConfirmationDialog(String confirmationMessage) {
		ButtonType okBtn = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
		ButtonType closeBtn = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(Alert.AlertType.WARNING, confirmationMessage, okBtn, closeBtn);
		alert.setTitle("Delete order warning");
		Optional<ButtonType> result = alert.showAndWait();
		return result.orElse(closeBtn) == okBtn;
	}

}
