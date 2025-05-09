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

import canteen2.Admin;
import canteen2.MaOrder;
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



import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ClientAdmin3 extends Application {

    private static final String BASE_URI = "http://localhost:8080/TomCatProject3/Admin/";
    private static final Client client = ClientBuilder.newClient();

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
                openAdminPage(primaryStage);
            } else {
                showAlert("Login Failed", "Invalid username or password");
            }
        });

        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20, 20, 20, 20));
        loginLayout.getChildren().addAll(new Label("Username:"), usernameField, new Label("Password:"), passwordField, loginButton);

        Scene loginScene = new Scene(loginLayout, 300, 200);
        primaryStage.setScene(loginScene);

        primaryStage.show();
    }

    private boolean authenticate(String username, String password) {
        URI uri = URI.create(BASE_URI + "getAdmins");
        String adminsResponse = client.target(uri)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

        Admin[] admins = new Gson().fromJson(adminsResponse, Admin[].class);

        for (Admin admin : admins) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    private void openAdminPage(Stage primaryStage) {
        primaryStage.close(); // Close the login screen

        Stage adminStage = new Stage();
        adminStage.setTitle("Admin Page");

        Button addMenuButton = new Button("Add Menu Option");
        Button addOperatingHoursButton = new Button("Add Operating Hours");
        Button addWeekdaysButton = new Button("Add Weekdays");
        Button seeOrdersButton = new Button("See Orders"); // New button for "See Orders"

        VBox adminLayout = new VBox(10);
        adminLayout.setPadding(new Insets(40, 40, 40, 40));
        adminLayout.getChildren().addAll(addMenuButton, addOperatingHoursButton, addWeekdaysButton, seeOrdersButton);

        Scene adminScene = new Scene(adminLayout, 300, 200);
        adminStage.setScene(adminScene);

        addMenuButton.setOnAction(e -> {
            try {
                new ClientMenuOptions2().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace(); // Handle the exception appropriately
            }
        });

        addOperatingHoursButton.setOnAction(e -> {
            try {
                new ClientOperatingHours2().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace(); // Handle the exception appropriately
            }
        });

        addWeekdaysButton.setOnAction(e -> {
            try {
                new ClientWeekdays3().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace(); // Handle the exception appropriately
            }
        });

        seeOrdersButton.setOnAction(e -> showOrdersPage(primaryStage));

        adminStage.show();
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



