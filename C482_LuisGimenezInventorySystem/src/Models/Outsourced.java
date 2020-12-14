package Models;

/**
 * Supplied class Outsourced.java
 *
 * @author Luis J. Gimenez
 */

/**
 * Outsourced Subclass. Subclass of abstract class Part but the difference is one additional field, companyName.
 */
public class Outsourced extends Part {
    /**
     * the name of the company outsourcing the part
     */
    private String companyName;
    /**
     * Constructor for a new outsourced part.
     * @param id the ID for part
     * @param name the name of the part
     * @param price the price of the part
     * @param stock the inventory level of the part
     * @param min the minimum level for part
     * @param max the maximum level for part
     * @param companyName the company name for part
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /**
     * companyName setter
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    /**
     * companyName getter
     */
    public String getCompanyName() {
        return companyName;
    }
}