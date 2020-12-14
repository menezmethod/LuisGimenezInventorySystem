package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Supplied class Inventory.java
 *
 * Run-time error corrected 12/09/2020
 * @author Luis J. Gimenez
 */

/***
 * Inventory Class. Corresponds to the main object, the inventory of parts and products.
 */
public class Inventory {
    /**
     * Holds all parts in inventory.
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    /**
     * Holds all products in inventory.
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    /**
     * Adds a new part to inventory.
     * @param newPart the part to be added.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    /**
     * Adds a new product to inventory.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    /**
     * Searches parts by ID,
     * @param partId the part ID.
     * @return the part if found, null if not.
     */
    public Part lookupPart(int partId) {
        Part f = null;
        for (Part p : allParts) {
            if (p.getId() == partId) {
                f = p;
            }
        }
        return f;
    }
    /**
     * Searches list of products by ID.
     *
     * @param productId the product ID.
     * @return product object if found, null if not.
     *
     * Had an run-time error in this section "A Non-static method cannot be referenced from a static context". Fixed it by
     * adding the alerts (Product was not found.) directly on the MainScreen.java instead using a switch statement.
     */
    public static Product lookupProduct(int productId) {
        Product f = null;
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                f = product;
                // Error occurred here when attempting to add a reference to an outside method.
            }
        }
        // Error occurred here when attempting to add a reference to an outside method.
        return f;
    }
    /**
     * Searches the list of parts by name.
     * @return search results if found.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> partFound = FXCollections.observableArrayList();
        for (Part p : allParts) {
            if (p.getName().equals(partName)) {
                partFound.add(p);
            }
        }
        return partFound;
    }
    /**
     * Searches the list of products by name.*
     * @return search results if any.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> productFound = FXCollections.observableArrayList();
        for (Product p : allProducts) {
            if (p.getName().equals(productName)) {
                productFound.add(p);
            }
        }
        return productFound;
    }
    public static void updatePart(int index, Part selectedPart) {

        allParts.set(index, selectedPart);
    }
    /**
     * Updates a product in the product list.
     * @param selectedProduct the product used for replacement.
     */
    public static void updateProduct(int index, Product selectedProduct) { allProducts.set(index, selectedProduct); }
     /**
     * Removes part from the parts list.
     *
     * @param selectedPart the part to be removed.
     * @return A boolean indicating status of part removal.
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Removes a product from parts list.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Displays a list of all parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    /**
     * Displays a list of all products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    private static int partId = 0;
    private static int prodID = 0;
    public static int lookupPart() { return ++partId;}
    public static int lookupProduct() { return ++prodID;
    }
}