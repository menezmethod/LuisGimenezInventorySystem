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
import Models.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * ModifyPart is responsible for "Modify Product" form scene of the inventory management application.
 * @author Luis J. Gimenez
 */
public class ModifyProduct implements Initializable {
    /**
     * the product object selected in the MainScreen.
     */
    Product selectedProduct;
    /**
     * A list of parts associated with the product.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * the associated parts table view.
     */
    @FXML private TableView<Part> assocPartTV;
    /**
     * the part ID column for associated parts table.
     */
    @FXML private TableColumn<Part, Integer> assocPartIdCol;
    /**
     * the part name column for associated parts table.
     */
    @FXML private TableColumn<Part, String> assocPartNameCol;
    /**
     * the inventory level column for associated parts table.
     */
    @FXML private TableColumn<Part, Integer> assocPartInventoryCol;
    /**
     * the part price column for associated parts table.
     */
    @FXML private TableColumn<Part, Double> assocPartPriceCol;
    /**
     * the all parts table view.
     */
    @FXML private TableView<Part> partTV;
    /**
     * the part ID column for all parts table.
     */
    @FXML private TableColumn<Part, Integer> partIdCol;
    /**
     * the part name column of all parts tableview.
     */
    @FXML private TableColumn<Part, String> partNameCol;
    /**
     * the inventory level column of all parts tableview.
     */
    @FXML private TableColumn<Part, Integer> partInventoryCol;
    /**
     * the part price column of all parts tableview.
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
     *  Second necessary part of "Initialization," it initiates the controller.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectedProduct = MainScreen.getProductToModify();
        associatedParts = selectedProduct.getAllAssociatedParts();

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTV.setItems(Inventory.getAllParts());

        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocPartTV.setItems(associatedParts);

        productIdTxt.setText(String.valueOf(selectedProduct.getId()));
        productNameTxt.setText(selectedProduct.getName());
        productInventoryTxt.setText(String.valueOf(selectedProduct.getStock()));
        productPriceTxt.setText(String.valueOf(selectedProduct.getPrice()));
        productMaxTxt.setText(String.valueOf(selectedProduct.getMax()));
        productMinTxt.setText(String.valueOf(selectedProduct.getMin()));
    }
    /**
     * Adds part selected in all parts table into the associated parts table.
     * Error message displayed if part is not selected.
     **/
    @FXML
    void addBonAct(ActionEvent event) {
        Part selectedPart = partTV.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            showAlert(5);
        } else {
            associatedParts.add(selectedPart);
            assocPartTV.setItems(associatedParts);
        }
    }
    /**
     * "Show and wait" confirmation dialog and loads MainScreen scene when answered.
     * @throws IOException From FXMLLoader.
     */
    @FXML void cancelBtnAct(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            ToMainScreen(event);
        }
    }
    /**
     * Prompts user for confirmation before removing selected part from associated parts table.
     * Error message displayed if part is not selected.
     */
    @FXML void removeBtnAct(ActionEvent event) {
        Part selectedPart = assocPartTV.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            showAlert(5);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Are you sure you want to remove the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                assocPartTV.setItems(associatedParts);
            }
        }
    }
    /**
     * Replaces product in inventory and loads MainScreen.
     * Text-fields are validated when user clicks Save button.
     * @throws IOException From FXMLLoader.
     */
    @FXML
    void saveBtnAct(ActionEvent event) throws IOException {
        try {
            int id = selectedProduct.getId();
            String name = productNameTxt.getText();
            double price = Double.parseDouble(productPriceTxt.getText());
            int stock = Integer.parseInt(productInventoryTxt.getText());
            int min = Integer.parseInt(productMinTxt.getText());
            int max = Integer.parseInt(productMaxTxt.getText());
            if (name.isEmpty()) {
                showAlert(7);
            } else {
                if (minValidate(min, max) && inventoryValid(min, max, stock)) {
                    Product newProduct = new Product(id, name, price, stock, min, max);
                    for (Part part : associatedParts) {
                        newProduct.addAssociatedPart(part);
                    }
                    Inventory.addProduct(newProduct);
                    Inventory.deleteProduct(selectedProduct);
                    ToMainScreen(event);
                }
            }
        } catch (Exception e){
            showAlert(1);
        }
    }
    /**
     * Begins a search based on parts search text-field and refreshes the parts table view with search results.
     * Parts can be searched for by ID or name.
     */
    @FXML void searchBtnAction(ActionEvent event) {
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
            showAlert(2);
        }
    }
    /**
     * Refresh part table-view when parts search text-field is empty.
     */
    @FXML void searchKeyPressed(KeyEvent event) {

        if (partSearchTxt.getText().isEmpty()) {
            partTV.setItems(Inventory.getAllParts());
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
            showAlert(3);
        }
        return v;
    }
    /**
     * Checks that inventory level is equal to or between min and max.
     * @param min the minimum value for part.
     * @param max the maximum value for part.
     * @param stock the inventory level for part.
     * @return Boolean validates inventory.
     */
    private boolean inventoryValid(int min, int max, int stock) {
        boolean v = true;
        if (stock < min || stock > max) {
            v = false;
            showAlert(4);
        }
        return v;
    }
    /**
     * Alert messages selector using switch statement.
     * @param alertCode selects alert to display.
     */
    private void showAlert(int alertCode) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
        switch (alertCode) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Product");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alertInfo.setTitle("Information");
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
                alert.setContentText("Click on a part in the table-view.");
                alert.showAndWait();
                break;
            case 7:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid: Name Empty");
                alert.setContentText("Name field cannot be empty.");
                alert.showAndWait();
                break;
        }
    }
}