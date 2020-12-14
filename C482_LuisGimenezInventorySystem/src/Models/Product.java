 package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Supplied class Product.java
 *
 * @author Luis J. Gimenez
 */
public class Product {
    /**
     * Product class. Contains the constructor for product, and includes an array list of associated parts.
     * @param id the ID for product
     * @param name the name of the product
     * @param price the price of the product
     * @param inventory the inventory level of the product
     * @param min the minimum level for product
     * @param max the maximum level for product
     */
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    /**
     * Associated parts for product listed
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * Constructor for a new product
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }
    /**
     * id getter
     */
    public int getId() {
        return id;
    }
    /**
     * id setter
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * name getter
     */
    public String getName() {
        return name;
    }
    /**
     * name setter
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * price getter
     */
    public double getPrice() {
        return price;
    }
    /**
     * price setter
     */
    public void setPrice(double price) {
        this.price = price;
    }
    /**
     * stock getter
     */
    public int getStock() {
        return stock;
    }
    /**
     * stock setter
     */
    public void setStock(int stock) {
        this.stock = stock;
    }
    /**
     * min getter
     */
    public int getMin() {
        return min;
    }
    /**
     * min setter
     * */
    public void setMin(int min) {
        this.min = min;
    }
    /**
     * max getter
     */
    public int getMax() {
        return max;
    }
    /**
     * max setter
     */
    public void setMax(int max) {
        this.max = max;
    }
    /**
     * Adds a part to the product's associated parts list.
     */
    public void  addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    /**
     * Deletes a part from the products associated parts list.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        if (associatedParts.contains(selectedAssociatedPart)) {
            associatedParts.remove(selectedAssociatedPart);
            return true;
        }
        else
            return false;
    }
    /**
     * List of products associated parts.
     */
    public ObservableList<Part> getAllAssociatedParts() {return associatedParts;}
}