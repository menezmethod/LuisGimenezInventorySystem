package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;
import Models.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * ModifyPart is responsible for "Modify Part" form scene of the inventory management application.
 * @author Luis J. Gimenez
 */
public class ModifyPart implements Initializable {
    /**
     * the part selected in the MainScreen.
     */
    private Part selectedPart;
    /**
     * the inhouse radio button.
     */
    @FXML private RadioButton inHouseBtn;
    /**
     * the outsourced radio button.
     */
    @FXML private RadioButton OutsourcedBtn;
    /**
     * the machine ID -or- company name text-field.
     */
    @FXML private TextField partIdName;
    /**
     * the part ID text-field.
     */
    @FXML private TextField partId;
    /**
     * the part name text-field.
     */
    @FXML private TextField partName;
    /**
     * the inventory level text-field.
     */
    @FXML private TextField partInventory;
    /**
     * the part price text-field.
     */
    @FXML private TextField partPrice;
    /**
     * the maximum level text-field.
     */
    @FXML private TextField partMax;
    /**
     * the minimum level text-field.
     */
    @FXML private TextField partMin;
    /**
     * the machineId -or- company name label for part.
     */
    @FXML private Label machineId;
    /**
     *  Second necessary part of "Initialization," it initiates the controller.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedPart = MainScreen.getPartToModify();
        if (selectedPart instanceof InHouse) {
            inHouseBtn.setSelected(true);
            machineId.setText("Machine ID");
            partIdName.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }
        if (selectedPart instanceof Outsourced){
            OutsourcedBtn.setSelected(true);
            machineId.setText("Company Name");
            partIdName.setText(((Outsourced) selectedPart).getCompanyName());
        }
        partId.setText(String.valueOf(selectedPart.getId()));
        partName.setText(selectedPart.getName());
        partInventory.setText(String.valueOf(selectedPart.getStock()));
        partPrice.setText(String.valueOf(selectedPart.getPrice()));
        partMax.setText(String.valueOf(selectedPart.getMax()));
        partMin.setText(String.valueOf(selectedPart.getMin()));
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
     * Sets machineId / company name label to "Machine ID".
     */
    @FXML void inHouseRadioBtnAct(ActionEvent event) {
        machineId.setText("Machine ID");
    }
    /**
     * Sets machineId / company name label to "Company Name".
     */
    @FXML void outsourcedRadioBtmAct(ActionEvent event) {
        machineId.setText("Company Name");
    }
    /**
     * Replaces part in inventory and loads MainScreen.
     * Text-fields are validated when user clicks Save button.
     * @throws IOException From FXMLLoader.
     */
    @FXML void saveBtnAct(ActionEvent event) throws IOException {
        try {
            int id = selectedPart.getId();
            String name = partName.getText();
            double price = Double.parseDouble(partPrice.getText());
            int stock = Integer.parseInt(partInventory.getText());
            int min = Integer.parseInt(partMin.getText());
            int max = Integer.parseInt(partMax.getText());
            int machineId;
            String companyName;
            boolean partAddSuccess = false;
            if (minValidate(min, max) && inventoryValid(min, max, stock)) {
                if (inHouseBtn.isSelected()) {
                    try {
                        machineId = Integer.parseInt(partIdName.getText());
                        InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
                        Inventory.addPart(newInHousePart);
                        partAddSuccess = true;
                    } catch (Exception e) {
                        showAlert(2);
                    }
                }
                if (OutsourcedBtn.isSelected()) {
                    companyName = partIdName.getText();
                    Outsourced newOutsourcedPart = new Outsourced(id, name, price, stock, min, max,
                            companyName);
                    Inventory.addPart(newOutsourcedPart);
                    partAddSuccess = true;
                }
                if (partAddSuccess) {
                    Inventory.deletePart(selectedPart);
                    ToMainScreen(event);
                }
            }
        } catch(Exception e) {
            showAlert(1);
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
        switch (alertCode) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid: Machine ID");
                alert.setContentText("Machine ID may only contain numbers.");
                alert.showAndWait();
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
                alert.setContentText("Inventory must be equal to or between Min and Max. ");
                alert.showAndWait();
                break;
        }
    }
}