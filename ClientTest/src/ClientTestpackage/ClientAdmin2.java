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


// javafx para admin



import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

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

public class ClientAdmin2 extends Application {

    private static final String BASE_URI = "http://localhost:8080/TomCatProject3/Admin/";
    private static final Client client = ClientBuilder.newClient();

    public static void main(String[] args) {
        launch(args);
    }
// CLASSE ADMIN FUNCIONAVEL
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

        VBox adminLayout = new VBox(10);
        adminLayout.setPadding(new Insets(40, 40, 40, 40));
        adminLayout.getChildren().addAll(addMenuButton, addOperatingHoursButton, addWeekdaysButton);

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

        adminStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}