package ClientTestpackage;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import canteen2.OperatingHours;
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

//FUNCIONA
public class ClientOperatingHours2 extends Application {

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/OperatingHours/").build();
    }

    private static ClientConfig config = new ClientConfig();
    private static Client client = ClientBuilder.newClient(config);
    private static WebTarget service = client.target(getBaseURI());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane root = new GridPane();

        TableView tableView = new TableView();

        TableColumn<MenuOptions, String> column1 = new TableColumn<>("Day of the Week");
        column1.setMinWidth(200);
        column1.setCellValueFactory(new PropertyValueFactory<>("dayOfTheWeek"));

        TableColumn<MenuOptions, String> column2 = new TableColumn<>("Opening Time");
        column2.setMinWidth(200);
        column2.setCellValueFactory(new PropertyValueFactory<>("openingTime"));

        TableColumn<MenuOptions, String> column3 = new TableColumn<>("Closing Time");
        column3.setMinWidth(200);
        column3.setCellValueFactory(new PropertyValueFactory<>("closingTime"));

        tableView.getColumns().addAll(column1, column2, column3);

        fillTableView(tableView);

        TableView.TableViewSelectionModel<OperatingHours> selectionModel = tableView.getSelectionModel();

        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        ObservableList<OperatingHours> selectedItems = selectionModel.getSelectedItems();

        selectedItems.addListener(new ListChangeListener<OperatingHours>() {
            @Override
            public void onChanged(Change<? extends OperatingHours> change) {
                System.out.println("Selection changed: " + change.getList());
            }
        });

        selectionModel.select(0);

        Button btnEdit = new Button("Edit");
        Button btnDelete = new Button("Delete");
        Button btnCancel = new Button("Cancel");

        btnEdit.setOnAction(ae -> {
            System.out.println("Editing data... ");
            showAddUpdateHoursStage(primaryStage, selectedItems.get(0));
            fillTableView(tableView);
        });

        btnDelete.setOnAction(ae -> {
            System.out.println("Deleting data... ");

            if (showConfirmationDialog("Are you sure you want to delete the time? ")) {
                deleteOperatingHours(selectedItems.get(0).getId());
                fillTableView(tableView);
            }

        });

        btnCancel.setOnAction(ae -> {
            System.out.println("Cancelling...");
            fillTableView(tableView);
        });

        root.add(tableView, 0, 0, 3, 1);
        root.add(btnEdit, 0, 1, 1, 1);
        root.add(btnDelete, 1, 1, 1, 1);
        root.add(btnCancel, 2, 1, 1, 1);

        root.setHgap(5);
        root.setVgap(5);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Simple Operating hours CRUD example");
        primaryStage.setScene(scene);

        primaryStage.setX(300);
        primaryStage.setY(300);
        primaryStage.setWidth(600); // Adjusted width to accommodate the buttons
        primaryStage.setHeight(500);

        primaryStage.show();
    }

    private void fillTableView(TableView tableView) {
        tableView.getItems().clear();
        List<OperatingHours> operatingHours = getOperatingHours();
        for (OperatingHours op : operatingHours) {
            tableView.getItems().add(op);
        }
    }

    private List<OperatingHours> getOperatingHours() {
        Gson gson = new Gson();

        String responseOperatingHoursList = service.path("getOperatingHours")
                .request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .get(String.class);

        List<OperatingHours> operatingHours = Arrays.asList(gson.fromJson(responseOperatingHoursList, OperatingHours[].class));

        return operatingHours;
    }

    private boolean deleteOperatingHours(int operatingHoursId) {
        Response response = service.path("deleteOperatingHours")
                .path(Integer.toString(operatingHoursId)).request().delete();

        if (response.getStatus() < 200 || response.getStatus() > 299) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        return response.getStatus() == 200;
    }

    private void showAddUpdateHoursStage(Stage primaryStage, OperatingHours op) {
        Stage stage = new Stage();

        stage.setTitle("Add or Update Operating Hours - in Modal Mode");
        stage.setX(300);
        stage.setY(300);
        stage.setWidth(400);
        stage.setHeight(300);

        GridPane root = new GridPane();

        Label lblDayWeek = new Label("Day of the Week");
        root.add(lblDayWeek, 0, 1);

        ComboBox<String> dayOfWeekComboBox = new ComboBox<>();
        dayOfWeekComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        root.add(dayOfWeekComboBox, 1, 1);

        Label lblOpeningTime = new Label("Opening Time");
        root.add(lblOpeningTime, 0, 2);

        ComboBox<String> openingTimeComboBox = new ComboBox<>();
        fillTimeComboBox(openingTimeComboBox);
        root.add(openingTimeComboBox, 1, 2);

        Label lblClosingTime = new Label("Closing Time");
        root.add(lblClosingTime, 0, 3);

        ComboBox<String> closingTimeComboBox = new ComboBox<>();
        fillTimeComboBox(closingTimeComboBox);
        root.add(closingTimeComboBox, 1, 3);

        Button btnSave = new Button("Save");
        Button btnCancel = new Button("Cancel");
        root.add(btnSave, 0, 5);
        root.add(btnCancel, 1, 5);

        root.setHgap(5);
        root.setVgap(10);

        if (op.getId() != 0) {
            dayOfWeekComboBox.setValue(op.getDayOfTheWeek());

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            openingTimeComboBox.setValue(dateFormat.format(op.getOpeningTime()));
            closingTimeComboBox.setValue(dateFormat.format(op.getClosingTime()));
        }

        btnSave.setOnAction(ae -> {
            System.out.println("Saving data... " + op);

            op.setDayOfTheWeek(dayOfWeekComboBox.getValue());

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

            try {
                Date openingTime = dateFormat.parse(openingTimeComboBox.getValue());
                Date closingTime = dateFormat.parse(closingTimeComboBox.getValue());

                if (openingTime != null && closingTime != null) {
                    op.setOpeningTime(openingTime);
                    op.setClosingTime(closingTime);
                } else {
                    System.out.println("Invalid date format or null date.");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            saveData(op);
            stage.close();
        });

        btnCancel.setOnAction(ae -> {
            System.out.println("Cancelling...");
            stage.close();
        });

        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    private void fillTimeComboBox(ComboBox<String> comboBox) {
        comboBox.getItems().addAll(
                "08:00:00", "09:00:00", "10:00:00", "11:00:00",
                "12:00:00", "13:00:00", "14:00:00", "15:00:00",
                "16:00:00", "17:00:00", "18:00:00", "19:00:00", "20:00:00"
        );
    }

    private void saveData(OperatingHours op) {
        try {
            Response response;
            String message;

            if (op.getId() != 0) {
                message = "Operating hours updated successfully.";
                response = service.path("updateOperatingHours").request(MediaType.APPLICATION_JSON)
                        .put(Entity.entity(op, MediaType.APPLICATION_JSON), Response.class);
            } else {
                message = "Operating hours added successfully.";
                response = service.path("addOperatingHours").request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(op, MediaType.APPLICATION_JSON), Response.class);
            }

            if (response.getStatus() < 200 || response.getStatus() > 299) {
                showMessage("Failed : HTTP error code : " + response.getStatus(), AlertType.ERROR);
            } else {
                showMessage(message, AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showMessage("Error while saving the menu.", AlertType.ERROR);
        }
    }

    private void cleanFields(GridPane root) {
        for (Node node : root.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setText("");
            }
        }
    }

    private void showMessage(String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Menu Application");
        alert.setHeaderText(message);
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