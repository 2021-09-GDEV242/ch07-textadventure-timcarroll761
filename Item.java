/**
 * This class holds information about the items the player finds in the world.
 * @author Tim Carroll
 * @version 2021.11.01
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String description;
    private int weight;
    private boolean edible;

    /**
     * Create an item
     * @param description the name of the item
     * @param weight the weight of the item in lbs (roughly)
     */
    public Item(String description, int weight, boolean edible)
    {
        this.description = description;
        this.weight = weight;
        this.edible = edible;
    }

    /**
     * @return the name of the item
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * @return the weight of the item
     */
    public int getWeight()
    {
        return weight;
    }
    
    /**
     * @return the edibility of this item
     */
    public boolean isEdible()
    {
        return edible;
    }
}
