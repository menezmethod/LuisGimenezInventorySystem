package Controllers;

import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddPart is responsible for "Add Part" form scene of the inventory management application.
 * @author Luis J. Gimenez
 */
    public class AddPart implements Initializable {
    @FXML private TextField partId;
    @FXML private TextField partIdName;
    @FXML private TextField partName;
    @FXML private TextField partInventory;
    @FXML private TextField partPrice;
    @FXML private TextField partMax;
    @FXML private TextField partMin;
    @FXML private Label machineID;
    @FXML private RadioButton inHouseBtn;
    @FXML private RadioButton OutsourcedBtn;
    @FXML private ToggleGroup partType;

    /**
     * Second necessary part of "Initialization," it initiates the controller.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inHouseBtn.setSelected(true);
    }

    /**
     * Save Button action. Text-fields are validated when user clicks Save button.
     * Adds new part to inventory and returns to MainScreen
     */
    @FXML void saveBtnAction(ActionEvent event) {
        try {
            int id = 0;
            String name = partName.getText();
            double price = Double.parseDouble(partPrice.getText());
            int stock = Integer.parseInt(partInventory.getText());
            int min = Integer.parseInt(partMin.getText());
            int max = Integer.parseInt(partMax.getText());
            int machineId;
            boolean partAddSuccess = false;
            String companyName;
            if (name.isEmpty()) {
                AlertBox(5);
            } else {
                if (minValidate(min, max) && inventoryValid(min, max, stock)) {
                    if (inHouseBtn.isSelected()) {
                        try {
                            machineId = Integer.parseInt(partIdName.getText());
                            InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
                            newInHousePart.setId(Inventory.lookupPart());
                            Inventory.addPart(newInHousePart);
                            partAddSuccess = true;
                        } catch (Exception e) {
                            AlertBox(2);
                        }
                    }
                    if (OutsourcedBtn.isSelected()) {
                        companyName = partIdName.getText();
                        Outsourced newOutsourcedPart = new Outsourced(id, name, price, stock, min, max,
                                companyName);
                        newOutsourcedPart.setId(Inventory.lookupPart());
                        Inventory.addPart(newOutsourcedPart);
                        partAddSuccess = true;
                    }

                    if (partAddSuccess) {
                        ToMainScreen(event);
                    }
                }
            }
        } catch(Exception e) {
            AlertBox(1);
        }
    }
    /**
     * Sets machineID/company name label to "Machine ID".
     */
    @FXML void inHouseBtnAction(ActionEvent event) {
        machineID.setText("Machine ID");
    }
    /**
     * machine ID/company name label set to "Company Name".
     */
    @FXML void outsourcedBtnAction(ActionEvent event) {

        machineID.setText("Company Name");
    }
    /**
     * Loads MainScreen scene
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
     * @return Boolean validation of min.
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
     * Check that inventory level is equal to or between min and max.
     * @param min the minimum value for part.
     * @param max the maximum value for part.
     * @param stock the inventory level for part.
     * @return Boolean validation inventory.
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
     * "Show and wait" confirmation dialog and loads MainScreen scene when answered.
     * @throws IOException From FXMLLoader.
     */
    public void cancelBtnAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Cancel changes and go back to main screen?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ToMainScreen(event);
        }
    }
    /**
     * Alert messages selector using switch statement.
     * @param alertCode selects alert message.
     */
    private void AlertBox(int alertCode) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        switch (alertCode) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Part Add Error");
                alert.setContentText("Must not contain blank fields nor invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Empty Name Field");
                alert.setContentText("Must enter a name.");
                alert.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Inventory Value");
                alert.setContentText("Can't add. Inventory must be equal to or between Min and Max. .");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Min Must Be Greater");
                alert.setContentText("Min must be greater than the Max number");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("Machine ID Invalid");
                alert.setContentText("Machine ID must only contain numbers.");
                alert.showAndWait();
                break;
        }
    }}