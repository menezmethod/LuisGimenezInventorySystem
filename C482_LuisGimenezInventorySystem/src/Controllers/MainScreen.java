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
 * MediaScreen is responsible for the main screen of the inventory management application.
 * @author Luis J. Gimenez
 */
public class MainScreen implements Initializable {
    /**
     * the selected part in table-view by user
     */
    private static Part partToModify;
    /**
     * the selected product in table-view by user
     */
    private static Product productToModify;
    /**
     * the parts search text-field.
     */
    @FXML private TextField partSearch;
    /**
     * the parts table-view.
     */
    @FXML private TableView<Part> partTV;
    /**
     * the ID column for parts table
     */
    @FXML private TableColumn<Part, Integer> partIdColumn;
    /**
     * the part name column for parts table.
     */
    @FXML private TableColumn<Part, String> partNameColumn;
    /**
     * the inventory column for parts table.
     */
    @FXML private TableColumn<Part, Integer> partInventoryColumn;

    /**
     * the price column for parts table.
     */
    @FXML private TableColumn<Part, Double> partPriceColumn;
    /**
     * Table text-field for product search.
     */
    @FXML private TextField productSearchTxt;
    /**
     * Table view for products.
     */
    @FXML private TableView<Product> productTV;
    /**
     * the ID column for product's table.
     */
    @FXML private TableColumn<Product, Integer> productIdCol;
    /**
     * the name column for product's table.
     */
    @FXML private TableColumn<Product, String> productNameCol;
    /**
     * the inventory column for product's table.
     */
    @FXML private TableColumn<Product, Integer> productInventoryCol;

    /**
     * the price column for product's table.
     */
    @FXML private TableColumn<Product, Double> productPriceCol;
    /**
     * Gets part selected by user in part table.
     * @return a part, null if no part is selected.
     */
    public static Part getPartToModify() {
        return partToModify;
    }
    /**
     * Gets product selected by user in the product table.
     * @return a product, null if no product is selected.
     */
    public static Product getProductToModify() {
        return productToModify;
    }

    /**
     *  Second necessary part of "Initialization," it initiates the controller.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Show parts tableview
        partTV.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        //Show products tableview
        productTV.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    /**
     * Exits the application.
     */
    @FXML
    void exitBtnAct(ActionEvent event) {
        System.exit(0);
    }
    /**
     * Loads AddPart.
     * @throws IOException From FXMLLoader.
     */
    @FXML void partAddAct(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../Views/AddPartScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Deletes part selected by user in the part table.
     * Displays an error message if part is not selected, confirmation dialog is displayed before delete is executed.
     */
    @FXML void partDeleteAct(ActionEvent event) {
        Part selectedPart = partTV.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            AlertBox(3);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Are you sure you want to delete the selected part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
            }
        }
    }
    /**
     * Initiates a search based on value in parts search text-field and refreshes the parts
     * table view with search results. Parts can be searched for by ID or name.
     */
    @FXML void partSearchBtnAct(ActionEvent event) {

        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = partSearch.getText();

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
     * Refresh part table when parts search text-field is empty.
     */
    @FXML void partSearchTxtPressed(KeyEvent event) {
        if (partSearch.getText().isEmpty()) {
            partTV.setItems(Inventory.getAllParts());
        }

    }
    /**
     * Loads ModifyPart. Displays an error message if no part is selected.
     * @throws IOException From FXMLLoader.
     */
    @FXML void partModAct(ActionEvent event) throws IOException {

        partToModify = partTV.getSelectionModel().getSelectedItem();
        if (partToModify == null) {
            AlertBox(3);
        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("../Views/ModifyPartScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
    /**
     * Loads AddProduct
     * @throws IOException From FXMLLoader.
     */
    @FXML void productAddAct(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../Views/AddProductScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Deletes the product selected by the user in the product's table.
     * The method displays an error message if no product is selected and a confirmation dialog before deleting the
     * selected product. Also prevents user from deleting a product with one or more associated parts.
     */
    /**
     * Loads ModifyProduct. The method displays an error message if no product is selected.
     * @throws IOException From FXMLLoader.
     */
    @FXML void productModAct(ActionEvent event) throws IOException {
        productToModify = productTV.getSelectionModel().getSelectedItem();
        if (productToModify == null) {
            AlertBox(4);
        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("../Views/ModifyProductScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
    @FXML void productDeleteAct(ActionEvent event) {
        Product selectedProduct = productTV.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            AlertBox(4);
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to delete the selected product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ObservableList<Part> assocParts = selectedProduct.getAllAssociatedParts();
                if (assocParts.size() >= 1) {
                    AlertBox(5);
                } else {
                    Inventory.deleteProduct(selectedProduct);
                }
            }
        }
    }
    /**
     * Begins a search based on product text-field and refreshes the products table view with search results.
     * Products can be searched for by ID or name.
     */
    @FXML void productSearchBtnAct(ActionEvent event) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String searchString = productSearchTxt.getText();
        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchString) ||
                    product.getName().contains(searchString)) {
                productsFound.add(product);
            }
        }
        productTV.setItems(productsFound);
        if (productsFound.size() == 0) {
            AlertBox(2);
        }
    }
    /**
     * Refreshes product's table view to show all products when search text-field is empty.
     */
    @FXML void productSearchTxtPressed(KeyEvent event) {
        if (productSearchTxt.getText().isEmpty()) {
            productTV.setItems(Inventory.getAllProducts());
        }
    }
    /**
     * Alert messages selector using switch statement.
     * @param alertCode selects alert to display.
     */
    private void AlertBox(int alertCode) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        switch (alertCode) {
            case 1:
                alert.setTitle("Information");
                alert.setHeaderText("Part was not found");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Information");
                alert.setHeaderText("Product was not found");
                alert.showAndWait();
                break;
            case 3:
                alertError.setTitle("Error");
                alertError.setHeaderText("Please select a part");
                alert.setContentText("Click on a part in the table-view.");
                alertError.showAndWait();
                break;
            case 4:
                alertError.setTitle("Error");
                alertError.setHeaderText("Please select a product");
                alertError.showAndWait();
                break;
            case 5:
                alertError.setTitle("Error");
                alertError.setHeaderText("Associated Parts");
                alertError.setContentText("All parts must be removed from products prior to deletion.");
                alertError.showAndWait();
                break;
        }
    }
}