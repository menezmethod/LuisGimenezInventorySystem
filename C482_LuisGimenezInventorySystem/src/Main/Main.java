package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;
import Models.Product;

/***
  The Inventory Management Software is an application designed to maintain an inventory of parts and products and
  it's associated parts.
  <p> Course: Software I, C482 - Performance Assessment: Software I - QKM1</p>
  <p>For future releases I would consider implementing keyboard shortcuts. For example if somebody wants to
  add a part they can press Ctrl+A and that would take them to the add part scene automatically. The use of shortcuts
 in JavaFX is supported with various methods and not difficult to implement. 
  @author Luis J. Gimenez
 */
public class Main extends Application {
    /***
     * the start method. Loads the initial scene and creates the FXML stage.
     * @throws Exception
     */
    @Override
    public void start(Stage mainStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Views/MainScreen.fxml"));
        mainStage.setTitle("Inventory Management System");
        mainStage.setScene(new Scene(root));
        mainStage.show();
    }
    /***
     * the Main method is the entry point of the application, it launches the application and adds the sample data.
     */
    public static void main(String[] args) {
        //Add sample parts
        int partId = Inventory.lookupPart();
        InHouse pa1 = new InHouse(partId,"Brakes", 15.99, 8, 1, 20,
                101);
        partId = Inventory.lookupPart();
        InHouse pa2 = new InHouse(partId,"Wheel", 11.99, 15, 1, 20,
                101);
        partId = Inventory.lookupPart();
        Outsourced pa3 = new Outsourced(partId, "Chain",19.99, 22, 30,
                100, "Shimano");
        Inventory.addPart(pa1);
        Inventory.addPart(pa2);
        Inventory.addPart(pa3);

        //Add sample products
        int prodId = Inventory.lookupProduct();
        Product prod = new Product(prodId, "Mountain Bike", 699.99, 20, 20,
                100);
        prodId = Inventory.lookupProduct();
        Product prod2 = new Product(prodId, "City Bike", 399.99, 20, 20,
                100);
        prod.addAssociatedPart(pa1);
        prod.addAssociatedPart(pa2);
        prod.addAssociatedPart(pa3);
        Inventory.addProduct(prod);
        Inventory.addProduct(prod2);
        launch(args);
    }
}