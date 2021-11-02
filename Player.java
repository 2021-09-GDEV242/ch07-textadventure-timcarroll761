import java.util.ArrayList;
/**
 * The Player class has an inventory of Items and offers them to the Game class in convenient ways.
 *
 * @author Tim Carroll
 * @version 2021.11.01
 */
public class Player
{
    private ArrayList<Item> inventory;
    private int totalWeight;

    /**
     * Create the player and its inventory
     */
    public Player()
    {
        inventory = new ArrayList<Item>();
        totalWeight = 0;
    }
    
    /**
     * Print the contents of the player's inventory.
     */
    public void showInventory()
    {
        System.out.println("You are carrying:");
        if (inventory.size() == 0) {
            System.out.print("nothing");
            return;
        }
        
        for(Item item : inventory) {
            System.out.print(item.getDescription() + "  ");
        }
        System.out.println("\nTotal weight: " + totalWeight);
    }
    
    /**
     * Eat something, or maybe nothing.
     * @param thingToEat the name of the item to eat
     * @return a boolean that describes whether eating it killed you
     */
    public boolean eat(String thingToEat) {
        if (thingToEat.equals("")) {
            System.out.println("Eat what?");
            return false;
        }
        
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getDescription().equals(thingToEat)) {
                System.out.println("You eat the " + thingToEat + ".");
                if (inventory.get(i).isEdible()) {
                    totalWeight -= inventory.get(i).getWeight();
                    inventory.remove(i);
                    return false;
                }
                else {
                    System.out.println("Unfortunately, that was not edible. You died.");
                    return true;
                }
            }
        }
        System.out.println("You don't have a " + thingToEat + ".");
        return false;
    }
    
    /**
     * Take all items from the current room and add them to the inventory
     * @param roomItems the ArrayList of items in the current room
     */
    public void take(ArrayList<Item> roomItems)
    {
        if (roomItems.size() == 0) {
            System.out.println("There's nothing to take");
            return;
        }
        
        for (Item item : roomItems) {
            inventory.add(item);
            totalWeight += item.getWeight();
            System.out.println("You take the " + item.getDescription() + ".");
        }
    }
    
    /**
     * Toss a specified item from the inventory
     * @param name the name of the item to toss
     * @return the item that was dropped
     */
    public Item drop(String name)
    {
        if (name == "" || name == null) {
            System.out.println("Drop what?");
            return null;
        }
        
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getDescription().equals(name)) {
                totalWeight -= inventory.get(i).getWeight();
                System.out.println("You drop the " + name + ".");
                return inventory.remove(i);
            }
        }
        
        System.out.println("You don't have that.");
        return null;
    }
    
    /**
     * Look for an item in the inventory
     * @param name the name of the item to look for
     * @return a boolean that describes whether it's held or not
     */
    public boolean find(String name)
    {
        if (name == "" || name == null) {
            return false;
        }
        
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getDescription().equals(name)) {
                return true;
            }
        }
        
        return false;
    }
}
