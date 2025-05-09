package ClientTestpackage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

import canteen2.Customer;
import canteen2.MaOrder;
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

// FUNCIONA CUSTOMER

public class ClientCustomer2 extends Application {
    private static final String BASE_URI = "http://localhost:8080/TomCatProject3/Customer/";
    private static final Client client = ClientBuilder.newClient();
    private static final String BASE_URI2 = "http://localhost:8080/TomCatProject3/MaOrder/";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (authenticate(username, password)) {
                openCustomerPage(primaryStage);
            } else {
                showAlert("Login Failed", "Invalid username or password");
            }
        });

        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20, 20, 20, 20));
        loginLayout.getChildren().addAll(new Label("Username:"), usernameField, new Label("Password:"), passwordField,
                loginButton);

        Scene loginScene = new Scene(loginLayout, 300, 200);
        primaryStage.setScene(loginScene);

        primaryStage.show();
    }

    private boolean authenticate(String username, String password) {
        URI uri = URI.create(BASE_URI + "getCustomers");
        String customerResponse = client.target(uri).request(MediaType.APPLICATION_JSON).get(String.class);

        Customer[] customers = new Gson().fromJson(customerResponse, Customer[].class);

        for (Customer cust : customers) {
            if (cust.getUsername().equals(username) && cust.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    private void openCustomerPage(Stage primaryStage) {
        primaryStage.close(); // Close the login screen

        Stage customerStage = new Stage();
        customerStage.setTitle("Customer Page");

        Button placeOrderButton = new Button("Place Order");
        Button seeOrderButton = new Button("See Orders");
        Button seeMenusButton = new Button("See Menus");

        VBox customerLayout = new VBox(10);
        customerLayout.setPadding(new Insets(20, 20, 20, 20));
        customerLayout.getChildren().addAll(placeOrderButton, seeOrderButton, seeMenusButton);

        // Set actions for the buttons
        placeOrderButton.setOnAction(e -> showPlaceOrderWindow(primaryStage));
        seeOrderButton.setOnAction(e -> showOrdersPage(primaryStage));
        seeMenusButton.setOnAction(e -> showMenusPage(primaryStage));

        Scene customerScene = new Scene(customerLayout, 300, 200);
        customerStage.setScene(customerScene);

        customerStage.show();
    }

    private void showPlaceOrderWindow(Stage primaryStage) {
        Stage placeOrderStage = new Stage();
        placeOrderStage.setTitle("Place Order");

        Label costLabel = new Label("Cost:");
        Label paymentMethodLabel = new Label("Payment Method:");

        ComboBox<MenuOptions> menuComboBox = new ComboBox<>();
        menuComboBox.setPromptText("Select a menu");
        fillMenuComboBox(menuComboBox);

        TextField costField = new TextField("4.0");
        costField.setEditable(false);

        TextField paymentMethodField = new TextField();

        Button saveOrderButton = new Button("Save Order");
        saveOrderButton.setId("saveOrderButton");
        saveOrderButton.setOnAction(
                e -> saveOrder(primaryStage, menuComboBox.getValue(), costField.getText(), paymentMethodField.getText()));

        VBox placeOrderLayout = new VBox(10);
        placeOrderLayout.setPadding(new Insets(20, 20, 20, 20));
        placeOrderLayout.getChildren().addAll(costLabel, costField, paymentMethodLabel, paymentMethodField,
                menuComboBox, saveOrderButton);

        Scene placeOrderScene = new Scene(placeOrderLayout, 300, 200);
        placeOrderStage.setScene(placeOrderScene);

        placeOrderStage.initModality(Modality.WINDOW_MODAL);
        placeOrderStage.initOwner(primaryStage);

        placeOrderStage.show();
    }

    private void saveOrder(Stage primaryStage, MenuOptions selectedMenu, String cost, String paymentMethod) {
        if (selectedMenu == null || cost.isEmpty() || paymentMethod.isEmpty()) {
            showAlert("Invalid Order", "Please fill in all fields.");
            return;
        }

        try {
            URI uri = URI.create("http://localhost:8080/TomCatProject3/MaOrder/addMaOrder");

            // Create a new MaOrder object
            MaOrder newOrder = new MaOrder();
            newOrder.setCost(Double.parseDouble(cost));
            newOrder.setPaymentMethod(paymentMethod);
            newOrder.setNewMenu(selectedMenu);

            // Send the new order to the server
            Response response = client.target(uri).request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(newOrder, MediaType.APPLICATION_JSON));

            System.out.println("Response Status: " + response.getStatus());
            System.out.println("Response Entity: " + response.readEntity(String.class));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                showAlert("Order Placed", "Your order has been placed successfully.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while processing the order.");
        }

        // Close the window after placing the order
        Stage placeOrderStage = (Stage) ((Button) primaryStage.getScene().lookup("#saveOrderButton")).getScene().getWindow();
        placeOrderStage.close();
    }
    
    private void fillMenuComboBox(ComboBox<MenuOptions> comboBox) {
        try {
            URI uri = URI.create("http://localhost:8080/TomCatProject3/MenuOptions/getMenuOptions");
            String menuResponse = client.target(uri).request(MediaType.APPLICATION_JSON).get(String.class);

            MenuOptions[] menus = new Gson().fromJson(menuResponse, MenuOptions[].class);

            comboBox.getItems().addAll(Arrays.asList(menus));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to retrieve menu data from the server.");
        }
    }

    private void showMenusPage(Stage primaryStage) {
        Stage menusStage = new Stage();
        menusStage.setTitle("Menus");

        TableView<MenuOptions> tableView = new TableView<>();

        TableColumn<MenuOptions, String> column1 = new TableColumn<>("Plate Name");
        column1.setMinWidth(200);
        column1.setCellValueFactory(new PropertyValueFactory<>("plateName"));

        TableColumn<MenuOptions, String> column2 = new TableColumn<>("Dessert Name");
        column2.setMinWidth(200);
        column2.setCellValueFactory(new PropertyValueFactory<>("dessertName"));

        TableColumn<MenuOptions, String> column3 = new TableColumn<>("Drink Name");
        column3.setMinWidth(200);
        column3.setCellValueFactory(new PropertyValueFactory<>("drinkName"));

        TableColumn<MenuOptions, String> column4 = new TableColumn<>("Type food");
        column4.setMinWidth(200);
        column4.setCellValueFactory(new PropertyValueFactory<>("typeFood"));

        tableView.getColumns().addAll(column1, column2, column3, column4);

        fillTableView(tableView);

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ObservableList<MenuOptions> selectedItems = tableView.getSelectionModel().getSelectedItems();
        selectedItems.addListener((ListChangeListener<MenuOptions>) change -> {
            System.out.println("Menus Selection changed: " + change.getList());
        });

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(tableView);

        Scene menusScene = new Scene(root, 600, 400);
        menusStage.setScene(menusScene);

        menusStage.initModality(Modality.WINDOW_MODAL);
        menusStage.initOwner(primaryStage);

        menusStage.show();
    }

    private void showOrdersPage(Stage primaryStage) {
        Stage ordersStage = new Stage();
        ordersStage.setTitle("Orders");

        TableView<MaOrder> ordersTableView = new TableView<>();

        TableColumn<MaOrder, String> column6 = new TableColumn<>("Cost");
        column6.setMinWidth(200);
        column6.setCellValueFactory(new PropertyValueFactory<>("cost"));

        TableColumn<MaOrder, String> column7 = new TableColumn<>("Payment Method");
        column7.setMinWidth(200);
        column7.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<MaOrder, String> column8 = new TableColumn<>("Menu");
        column8.setMinWidth(200);
        column8.setCellValueFactory(new PropertyValueFactory<>("newMenu"));

        ordersTableView.getColumns().addAll(column6, column7, column8);

        fillTableViewOrder(ordersTableView);

        // Add a listener for selection change
        ObservableList<MaOrder> selectedOrders = ordersTableView.getSelectionModel().getSelectedItems();
        selectedOrders.addListener((ListChangeListener<MaOrder>) change -> {
            System.out.println("Orders Selection changed: " + change.getList());
        });

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(ordersTableView);

        Scene ordersScene = new Scene(root, 600, 400);
        ordersStage.setScene(ordersScene);

        ordersStage.initModality(Modality.WINDOW_MODAL);
        ordersStage.initOwner(primaryStage);

        ordersStage.show();
    }

    private void fillTableView(TableView<MenuOptions> tableView) {
        try {
            URI uri = URI.create("http://localhost:8080/TomCatProject3/MenuOptions/getMenuOptions");
            String menuResponse = client.target(uri).request(MediaType.APPLICATION_JSON).get(String.class);

            MenuOptions[] menus = new Gson().fromJson(menuResponse, MenuOptions[].class);

            tableView.getItems().addAll(Arrays.asList(menus));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to retrieve menu data from the server.");
        }
    }

    private void fillTableViewOrder(TableView<MaOrder> tableView) {
        try {
            URI uri = URI.create("http://localhost:8080/TomCatProject3/MaOrder/getMaOrders");
            String menuResponse = client.target(uri).request(MediaType.APPLICATION_JSON).get(String.class);

            MaOrder[] orders = new Gson().fromJson(menuResponse, MaOrder[].class);

            tableView.getItems().addAll(Arrays.asList(orders));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to retrieve order data from the server.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
