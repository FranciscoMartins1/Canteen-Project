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
import javafx.beans.property.SimpleStringProperty;

import org.glassfish.jersey.client.ClientConfig;

import com.google.gson.Gson;

import canteen2.Admin;
import canteen2.MenuOptions;
import canteen2.Weekdays;
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
import javafx.scene.control.ComboBox;
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

// CLASSE WEEKDAYS
public class ClientWeekdays3 extends Application {

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/Weekdays/").build();
    }

    private static URI getBaseURIMenu() {
        return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/MenuOptions/").build();
    }

    private static ClientConfig config = new ClientConfig();
    private static Client client = ClientBuilder.newClient(config);
    private static WebTarget service = client.target(getBaseURI());
    private static WebTarget serviceMenu = client.target(getBaseURIMenu());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane root = new GridPane();

        TableView<Weekdays> tableView = new TableView<>();

        TableColumn<Weekdays, String> column1 = new TableColumn<>("Day name");
        column1.setMinWidth(200);
        column1.setCellValueFactory(new PropertyValueFactory<>("dayName"));

        TableColumn<Weekdays, String> column2 = new TableColumn<>("Menu Name");
        column2.setMinWidth(200);
        column2.setCellValueFactory(param -> {
            List<MenuOptions> menus = param.getValue().getMenus();
            if (menus != null && !menus.isEmpty()) {
                return new SimpleStringProperty(menus.get(0).getPlateName());
            } else {
                return new SimpleStringProperty("");
            }
        });

        tableView.getColumns().addAll(column1, column2);

        fillTableView(tableView);

        TableView.TableViewSelectionModel<Weekdays> selectionModel = tableView.getSelectionModel();

        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        ObservableList<Weekdays> selectedItems = selectionModel.getSelectedItems();

        selectedItems.addListener(new ListChangeListener<Weekdays>() {
            @Override
            public void onChanged(Change<? extends Weekdays> change) {
                System.out.println("Selection changed: " + change.getList());
            }
        });

        selectionModel.select(0);

        root.add(tableView, 0, 0, 4, 1);

        Button btnEdit = new Button("Edit");
        Button btnDelete = new Button("Delete");
        Button btnCancel = new Button("Cancel");

        btnEdit.setOnAction(ae -> {
            System.out.println("Editing data... ");
            showAddUpdateWeekdaysStage(primaryStage, selectedItems.get(0));
            fillTableView(tableView);
        });

        btnDelete.setOnAction(ae -> {
            System.out.println("Deleting data... ");

            if (showConfirmationDialog("Are you sure you want to delete the menu? ")) {
                deleteWeekdays(selectedItems.get(0).getDayID());
                fillTableView(tableView);
            }
        });

        btnCancel.setOnAction(ae -> {
            System.out.println("Cancelling...");
            fillTableView(tableView);
        });

        root.add(btnEdit, 0, 1, 1, 1);
        root.add(btnDelete, 1, 1, 1, 1);
        root.add(btnCancel, 2, 1, 1, 1);

        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Simple Menu CRUD example");
        primaryStage.setScene(scene);

        primaryStage.setX(300);
        primaryStage.setY(300);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);

        primaryStage.show();
    }

    private void fillTableView(TableView<Weekdays> tableView) {
        tableView.getItems().clear();
        List<Weekdays> weekday = getWeekdays();
        for (Weekdays w : weekday) {
            tableView.getItems().add(w);
        }
    }

    private List<Weekdays> getWeekdays() {
        Gson gson = new Gson();

        String responseWeekdaysList = service.path("getWeekdays").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).get(String.class);

        List<Weekdays> weekdays = Arrays.asList(gson.fromJson(responseWeekdaysList, Weekdays[].class));

        return weekdays;
    }

    private boolean deleteWeekdays(int weekdaysId) {
        Response response = service.path("deleteWeekdays").path(Integer.toString(weekdaysId)).request().delete();

        if (response.getStatus() < 200 || response.getStatus() > 299) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        return response.getStatus() == 200;
    }

    private void showAddUpdateWeekdaysStage(Stage primaryStage, Weekdays weekdays) {
        Stage stage = new Stage();
        stage.setTitle("Add or Update menu - in Modal Mode");
        stage.setX(300);
        stage.setY(300);
        stage.setWidth(300);
        stage.setHeight(300);

        GridPane root = new GridPane();

        Label lblWeekday = new Label("Weekday");
        root.add(lblWeekday, 0, 1);

        TextField txtWeekday = new TextField();
        root.add(txtWeekday, 1, 1);

        Label lblMenu = new Label("Menu name");
        root.add(lblMenu, 0, 2);

        ComboBox<MenuOptions> comboBox = new ComboBox<>();
        fillTableViewCombo(comboBox);
        root.add(comboBox, 1, 2);

        Button btnSave = new Button("Save");
        Button btnCancel = new Button("Cancel");
        root.add(btnSave, 0, 5);
        root.add(btnCancel, 1, 5);

        if (weekdays.getDayID() != 0) {
            txtWeekday.setText(weekdays.getDayName());

            // Check if menus list is not empty before selecting the menu
            if (!weekdays.getMenus().isEmpty()) {
                comboBox.getSelectionModel().select(weekdays.getMenus().get(0));
            }
        }

        btnSave.setOnAction(ae -> {
            weekdays.setDayName(txtWeekday.getText());
            MenuOptions selectedMenu = comboBox.getValue();
            weekdays.setMenus(Arrays.asList(selectedMenu));
            saveData(weekdays);
            stage.close();
        });

        btnCancel.setOnAction(ae -> stage.close());

        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    private void fillTableViewCombo(ComboBox<MenuOptions> comboBox) {
        comboBox.getItems().clear();
        List<MenuOptions> menus = getMenuOptions();
        comboBox.getItems().addAll(menus);
    }

    private List<MenuOptions> getMenuOptions() {
        Gson gson = new Gson();

        String responseMenusList = serviceMenu.path("getMenuOptions").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).get(String.class);

        List<MenuOptions> menus = Arrays.asList(gson.fromJson(responseMenusList, MenuOptions[].class));

        return menus;
    }

    private void saveData(Weekdays weekdays) {
        Response response;
        String message;

        if (weekdays.getDayID() != 0) {
            message = "Weekday updated successfully.";
            response = service.path("updateWeekdays").request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(weekdays, MediaType.APPLICATION_JSON), Response.class);
        } else {
            message = "Weekday added successfully.";
            response = service.path("addWeekdays").request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(weekdays, MediaType.APPLICATION_JSON), Response.class);
        }

        if (response.getStatus() < 200 || response.getStatus() > 299) {
            showMessage("Failed : HTTP error code : " + response.getStatus(), AlertType.ERROR);
        } else {
            showMessage(message, AlertType.INFORMATION);
        }
    }

    private void showMessage(String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Weekday Application");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String confirmationMessage) {
        ButtonType okBtn = new ButtonType("Yes", ButtonData.OK_DONE);
        ButtonType closeBtn = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.WARNING, confirmationMessage, okBtn, closeBtn);

        alert.setTitle("Delete menu warning");
        Optional<ButtonType> result = alert.showAndWait();

        return result.orElse(closeBtn) == okBtn;
    }
}
