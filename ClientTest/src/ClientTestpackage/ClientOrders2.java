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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClientOrders2 extends Application {
    private ComboBox<MenuOptions> comboBox;
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/TomCatProject3/MaOrder/").build();
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
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        // TableView
        TableView<MaOrder> tableView = new TableView<>();

        TableColumn<MaOrder, String> column1 = new TableColumn<>("Cost");
        column1.setMinWidth(200);
        column1.setCellValueFactory(new PropertyValueFactory<>("cost"));

        TableColumn<MaOrder, String> column2 = new TableColumn<>("Payment method");
        column2.setMinWidth(200);
        column2.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<MaOrder, String> column3 = new TableColumn<>("Menu");
        column3.setMinWidth(200);
        column3.setCellValueFactory(new PropertyValueFactory<>("newMenu"));

        tableView.getColumns().addAll(column1, column2, column3);

        fillTableView(tableView);

        TableView.TableViewSelectionModel<MaOrder> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        ObservableList<MaOrder> selectedItems = selectionModel.getSelectedItems();

        selectedItems.addListener(new ListChangeListener<MaOrder>() {
            @Override
            public void onChanged(Change<? extends MaOrder> change) {
                System.out.println("Selection changed: " + change.getList());
            }
        });

        selectionModel.select(0);

        ObservableList<Integer> selectedIndices = selectionModel.getSelectedIndices();

        root.add(tableView, 0, 0, 4, 1);

        Button btnNew = new Button("New");
        Button btnEdit = new Button("Edit");
        Button btnDelete = new Button("Delete");
        Button btnCancel = new Button("Cancel");

        btnNew.setOnAction(ae -> {
            System.out.println("New order data... ");
            showAddUpdateOrderStage(primaryStage, new MaOrder());
            fillTableView(tableView);
        });

        btnEdit.setOnAction(ae -> {
            System.out.println("Editing data... ");
            showAddUpdateOrderStage(primaryStage, selectedItems.get(0));
            fillTableView(tableView);
        });

        btnDelete.setOnAction(ae -> {
            System.out.println("Deleting data... ");

            if (showConfirmationDialog("Are you sure you want to delete the order? ")) {
                deleteMaOrder(selectedItems.get(0).getOrderId());
                fillTableView(tableView);
            }
        });

        btnCancel.setOnAction(ae -> {
            System.out.println("Cancelling...");
            fillTableView(tableView);
        });

        root.add(btnNew, 0, 1);
        root.add(btnEdit, 1, 1);
        root.add(btnDelete, 2, 1);
        root.add(btnCancel, 3, 1);

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Simple order CRUD example");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void fillTableView(TableView<MaOrder> tableView) {
        tableView.getItems().clear();
        List<MaOrder> maOrders = getMaOrders();
        for (MaOrder ord : maOrders) {
            tableView.getItems().add(ord);
        }
    }

    private void fillTableViewCombo(ComboBox<MenuOptions> comboBox) {
        comboBox.getItems().clear();
        List<MenuOptions> menus = getMenuOptions();
        comboBox.getItems().addAll(menus);
    }

    private List<MaOrder> getMaOrders() {
        Gson gson = new Gson();

        String responseOrdersList = service.path("getMaOrders").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).get(String.class);

        List<MaOrder> maOrders = Arrays.asList(gson.fromJson(responseOrdersList, MaOrder[].class));

        return maOrders;
    }

    private List<MenuOptions> getMenuOptions() {
        Gson gson = new Gson();

        String responseMenusList = serviceMenu.path("getMenuOptions").request(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).get(String.class);

        List<MenuOptions> menus = Arrays.asList(gson.fromJson(responseMenusList, MenuOptions[].class));

        return menus;
    }

    private boolean deleteMaOrder(int maOrderId) {
        Response response = service.path("deleteOrder").path(Integer.toString(maOrderId)).request().delete();

        if (response.getStatus() < 200 || response.getStatus() > 299) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }

        return response.getStatus() == 200;
    }

    private void showAddUpdateOrderStage(Stage primaryStage, MaOrder maOrder) {
        Stage stage = new Stage();
        comboBox = new ComboBox<>();
        fillTableViewCombo(comboBox);

        stage.setTitle("Add or Update order - in Modal Mode");
        stage.setX(300);
        stage.setY(300);
        stage.setWidth(300);
        stage.setHeight(300);

        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        // Set column constraints to ensure columns have enough width
        ColumnConstraints column1 = new ColumnConstraints(100); // Adjust the width as needed
        ColumnConstraints column2 = new ColumnConstraints(100);
        root.getColumnConstraints().addAll(column1, column2);

        Label lblCost = new Label("Cost");
        root.add(lblCost, 0, 1);

        TextField txtCost = new TextField();
        lblCost.setId("lblCost");
        root.add(txtCost, 1, 1);

        Label lblPaymentMethod = new Label("Payment Method");
        root.add(lblPaymentMethod, 0, 2);

        TextField txtPaymentMethod = new TextField();
        lblPaymentMethod.setId("lblPaymentMethod");
        root.add(txtPaymentMethod, 1, 2);

        Label lblMenus = new Label("Menu");
        root.add(lblMenus, 0, 3);

        ComboBox<MenuOptions> comboBox = new ComboBox<>();
        fillTableViewCombo(comboBox);
        root.add(comboBox, 1, 3);

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

            // Set the selected Menu in the ComboBox
            comboBox.setValue(maOrder.getNewMenu());
        }

        btnSave.setOnAction(ae -> {
            System.out.println("Saving data... " + maOrder);

            String costString = txtCost.getText();

            try {
                double cost = Double.parseDouble(costString);

                maOrder.setCost(cost);
                maOrder.setPaymentMethod(txtPaymentMethod.getText());

                // Get the selected MenuOptions from the ComboBox
                MenuOptions selectedMenu = comboBox.getValue();

                // Set the menu in the MaOrder object
                maOrder.setNewMenu(selectedMenu);

                saveData(maOrder);
                stage.close();
            } catch (NumberFormatException e) {
                System.out.println("Invalid cost format: " + costString);
            }
        });

        btnCancel.setOnAction(ae -> {
            System.out.println("Cancelling...");
            cleanFields(root);
            stage.close();
        });

        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        stage.setTitle("Add or Update Order");
        stage.setScene(scene);

        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    private void saveData(MaOrder maOrder) {
        try {
            Response response;
            String message = " added successfully.";

            response = service.path("addMaOrder").request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(maOrder, MediaType.APPLICATION_JSON), Response.class);

            if (response.getStatus() < 200 || response.getStatus() > 299) {
                showMessage("Failed : HTTP error code : " + response.getStatus(), AlertType.ERROR);
            } else {
                showMessage(message, AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showMessage("Error while saving the order.", AlertType.ERROR);
        }
    }

    private void cleanFields(GridPane root) {
        for (Node node : root.getChildren()) {
            System.out.println("Id: " + node.getId());
            if (node instanceof TextField) {
                ((TextField) node).setText("");
            } else if (node instanceof CheckBox) {
                ((CheckBox) node).setSelected(false);
            }
        }
    }

    private void showMessage(String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Order Application");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationDialog(String confirmationMessage) {
        ButtonType okBtn = new ButtonType("Yes", ButtonData.OK_DONE);
        ButtonType closeBtn = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.WARNING, confirmationMessage, okBtn, closeBtn);

        alert.setTitle("Delete order warning");
        Optional<ButtonType> result = alert.showAndWait();

        return (result.orElse(closeBtn) == okBtn);
    }
}