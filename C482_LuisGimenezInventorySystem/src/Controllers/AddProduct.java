package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import Models.Inventory;
import Models.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddProduct is responsible for "Add Product" form scene of the inventory management application.
 * @author Luis J. Gimenez
 */
public class AddProduct implements Initializable {
    /**
     * the parts list associated with the product.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * Associated parts tableview.
     */
    @FXML private TableView<Part> assocPartTV;
    /**
     * the associated parts table part ID column.
     */
    @FXML private TableColumn<Part, Integer> assocPartIdCol;
    /**
     * the associated parts table part name column.
     */
    @FXML private TableColumn<Part, String> assocPartNameCol;
    /**
     * the associated parts table inventory level column.
     */
    @FXML private TableColumn<Part, Integer> assocPartInventoryCol;
    /**
     * the associated parts table price column.
     */
    @FXML private TableColumn<Part, Double> assocPartPriceCol;
    /**
     * the tableview of all parts.
     */
    @FXML private TableView<Part> partTV;
    /**
     * the part ID column of all parts tableview.
     */
    @FXML private TableColumn<Part, Integer> partIdCol;
    /**
     * the name column of all parts tableview.
     */
    @FXML private TableColumn<Part, String> partNameCol;
    /**
     * the inventory level column of all parts tableview.
     */
    @FXML private TableColumn<Part, Integer> partInventoryCol;
    /**
     * the price column of all parts tableview.
     */
    @FXML private TableColumn<Part, Double> partPriceCol;
    /**
     * the part search text-field.
     */
    @FXML private TextField partSearchTxt;
    /**
     * the product ID text-field.
     */
    @FXML private TextField productIdTxt;
    /**
     * the product name text-field.
     */
    @FXML private TextField productNameTxt;
    /**
     * the product inventory level text-field.
     */
    @FXML private TextField productInventoryTxt;
    /**
     * the product price text-field.
     */
    @FXML private TextField productPriceTxt;
    /**
     * the product maximum level text-field.
     */
    @FXML private TextField productMaxTxt;
    /**
     * the product minimum level text-field.
     */
    @FXML private TextField productMinTxt;

    /**
     * Second necessary part of "Initialization," it initiates the controller.
     * Displays table views.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTV.setItems(Inventory.getAllParts());

        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Adds selected part object from all parts table to the associated parts table.
     * Outputs error message if a part was not selected.
     */
    @FXML void addBtnAct(ActionEvent event) {
        Part selectPart = partTV.getSelectionModel().getSelectedItem();
        if (selectPart == null) {
            AlertBox(5);
        } else {
            associatedParts.add(selectPart);
            assocPartTV.setItems(associatedParts);
        }
    }
    /**
     * "Show and wait" confirmation dialog and loads MainScreen scene.
     * @throws IOException From FXMLLoader.
     */
    @FXML void cancelBtnAct(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ToMainScreen(event);
        }
    }
    /**
     * Initiates a search based on value in parts search text-field and refreshes the parts
     * table view with search results.
     *
     * Parts can be searched for by ID or name.
     */
    @FXML void partSearchBtnAct(ActionEvent event) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = partSearchTxt.getText();
        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().contains(searchString)) {
                partsFound.add(part);
            }
        }
        partTV.setItems(partsFound);
        if (partsFound.size() == 0) {
            AlertBox(1);
        }
    }
    /**
     * Refreshes part table view to show all parts when parts search text-field is empty.
     */
    @FXML void partSearchPressed(KeyEvent event) {
        if (partSearchTxt.getText().isEmpty()) {
            partTV.setItems(Inventory.getAllParts());
        }
    }
    /**
     * Prompts user for confirmation before removing selected part from associated parts table.
     * Error message displayed if part is not selected.
     */
    @FXML void removeBtnAct(ActionEvent event) {
        Part selectedPart = assocPartTV.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            AlertBox(5);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("Are you sure you want to remove the selected part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                assocPartTV.setItems(associatedParts);
            }
        }
    }
    /**
     * Adds a new product to inventory and loads MainScreen scene.
     * Text-fields are validated when user clicks Save button.
     * @throws IOException From FXMLLoader.
     */
    @FXML void saveBtnAct(ActionEvent event) throws IOException {
        try {
            int id = 0;
            String name = productNameTxt.getText();
            double price = Double.parseDouble(productPriceTxt.getText());
            int stock = Integer.parseInt(productInventoryTxt.getText());
            int min = Integer.parseInt(productMinTxt.getText());
            int max = Integer.parseInt(productMaxTxt.getText());
            if (name.isEmpty()) {
                AlertBox(7);
            } else {
                if (minValidate(min, max) && inventoryValid(min, max, stock)) {
                    Models.Product newProduct = new Models.Product(id, name, price, stock, min, max);
                    for (Part part : associatedParts) {
                        newProduct.addAssociatedPart(part);
                    }
                    newProduct.setId(Inventory.lookupProduct());
                    Inventory.addProduct(newProduct);
                    ToMainScreen(event);
                }
            }
        } catch (Exception e){
            AlertBox(1);
        }
    }
    /**
     * Loads MainScreen
     * @throws IOException From FXMLLoader.
     */
    private void ToMainScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../Views/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Checks that min is greater than 0 and less than max.
     * @param min the minimum value for part.
     * @param max the maximum value for part.
     * @return Boolean validates min.
     */
    private boolean minValidate(int min, int max) {
        boolean v = true;
        if (min <= 0 || min >= max) {
            v = false;
            AlertBox(3);
        }
        return v;
    }
    /**
     * Checks that inventory level is equal to or between min and max.
     * @param min the minimum value for part.
     * @param max the maximum value for part.
     * @param stock the inventory level for part.
     * @return Boolean validates inventory
     */
    private boolean inventoryValid(int min, int max, int stock) {
        boolean v = true;
        if (stock < min || stock > max) {
            v = false;
            AlertBox(4);
        }
        return v;
    }
    /**
     * Alert messages selector using switch statement.
     * @param alertCode selects alert message.
     */
    private void AlertBox(int alertCode) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
        switch (alertCode) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Product Add Error");
                alert.setContentText("Must not contain blank fields nor invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alertInfo.setTitle("Not Found");
                alertInfo.setHeaderText("Part was not found.");
                alertInfo.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid: Min");
                alert.setContentText("Min must be a number greater than 0 and less than Max.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid: Inventory");
                alert.setContentText("Can't add. Inventory must be equal to or between Min and Max. ");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("Please select a part.");
                alert.showAndWait();
                break;
            case 7:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid: Name is Empty");
                alert.setContentText("Name field cannot be empty.");
                alert.showAndWait();
                break;
        }
    }
}