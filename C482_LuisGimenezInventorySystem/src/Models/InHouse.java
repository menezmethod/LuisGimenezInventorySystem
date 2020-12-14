package Models;
/**
 * Supplied class InHouse.java
 *
 * @author Luis J. Gimenez
 */

/***
 * InHouse Subclass. Subclass of abstract class Part but the difference is one additional field, machineId.
 */
public class InHouse extends Part {
    /**
     * @param machineId the machine ID for part
     */
    public int machineId;
    /**
     * Creates InHouse class
     * @param id ID of part
     * @param name Name of part
     * @param price Price of part
     * @param stock Inventory level of part
     * @param min Minimum level for part
     * @param max Maximum level for part
     * @param machineId Machine ID for part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    /**
     * machineId Getter
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }
    /**
     * machineId Setter
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}