/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes and Tim Carroll
 * @version 2021.11.01
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Player player;
        
    /**
     * Create the game and initialise its internal map and the player.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        player = new Player();
    }
    
    /**
     * Main to make this executable
     */
    public static void main(String[] args)
    {
        Game game = new Game();
        game.play();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office, kitchen, freezer, lounge, hall, stairs0, stairs1, stairs2, closet, roof, boiler, chemCloset, backstage;
      
        // create the rooms
        outside = new Room(false, "outside the main entrance of the university");
        theater = new Room(false,"in a lecture theater");
        pub = new Room(false, "in the campus pub");
        lab = new Room(false, "in a chemistry lab");
        office = new Room(false, "in the computing admin office");
        kitchen = new Room(false, "in the pub's kitchen");
        freezer = new Room(false, "in a walk-in freezer");
        lounge = new Room(false, "in a student lounge");
        hall = new Room(false, "in a fancy entry hall");
        stairs0 = new Room(false, "at the bottom of the stairs");
        stairs1 = new Room(false, "at the middle level of the stairs");
        stairs2 = new Room(false, "at the top of the stairs. There is a ladder to the roof");
        closet = new Room(false, "in the janitor's closet");
        roof = new Room(false, "on the roof");
        boiler = new Room(true, "in the basement boiler room");
        chemCloset = new Room(false, "in the chemical storage room");
        backstage = new Room(false, "in the costume and scenery storage");
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", hall);
        outside.setExit("west", pub);

        theater.setExit("west", outside);
        theater.setExit("east", backstage);

        pub.setExit("east", outside);
        pub.setExit("west", kitchen);
        pub.setExit("south", lounge);

        hall.setExit("north", outside);
        hall.setExit("east", office);
        hall.setExit("west", lounge);
        hall.setExit("south", stairs1);

        office.setExit("west", hall);
        
        kitchen.setExit("east", pub);
        kitchen.setExit("south", freezer);
        
        freezer.setExit("north", kitchen);
        
        lounge.setExit("east", hall);
        lounge.setExit("north", pub);
        
        stairs0.setExit("up", stairs1);
        stairs0.setExit("north", boiler);
        
        stairs1.setExit("down", stairs0);
        stairs1.setExit("up", stairs2);
        stairs1.setExit("north", hall);
        stairs1.setExit("south", closet);
        
        stairs2.setExit("down", stairs1);
        stairs2.setExit("up", roof);
        stairs2.setExit("north", lab);
        
        roof.setExit("down", stairs2);
        roof.setExit("jump", outside);
        
        boiler.setExit("south", stairs0);
        
        closet.setExit("north", stairs1);
        
        lab.setExit("south", stairs2);
        lab.setExit("north", chemCloset);
        
        chemCloset.setExit("south", lab);
        
        backstage.setExit("west", theater);
        
        //initialize items
        outside.addItem(new Item("apple", 1, true));
        
        theater.addItem(new Item("script", 1, false));
        theater.addItem(new Item("microphone", 2, false));
        
        pub.addItem(new Item("pretzels", 1, true));
        
        lab.addItem(new Item("goggles", 1, false));
        lab.addItem(new Item("beakers", 2, false));
        lab.addItem(new Item("salt", 1, true));
        
        kitchen.addItem(new Item("knife", 1, false));
        kitchen.addItem(new Item("potato", 1, true));
        
        freezer.addItem(new Item("popsicle", 1, true));
        
        lounge.addItem(new Item("coffee", 1, true));
        
        roof.addItem(new Item("baseball", 1, false));
        
        closet.addItem(new Item("mop", 5, false));
        closet.addItem(new Item("bucket", 2, false));
        closet.addItem(new Item("keys", 1, false));
        
        chemCloset.addItem(new Item("scale", 2, false));
        chemCloset.addItem(new Item("NaOH", 1, false));
        chemCloset.addItem(new Item("HCl", 1, false));
        
        boiler.addItem(new Item("paint", 5, false));
        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                wantToQuit = goRoom(command);
                break;
            
            case LOOK:
                look();
                break;
                
            case EAT:
                wantToQuit = player.eat(command.getSecondWord());
                break;
            
            case BAG:
                player.showInventory();
                break;
            
            case DROP:
                currentRoom.addItem(player.drop(command.getSecondWord()));
                break;
                
            case TAKE:
                player.take(currentRoom.takeItems());
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * @param command the full command entered by the user
     * @return a boolean that describes whether the move killed you
     */
    private boolean goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return false;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
            return false;
        }
        else if (nextRoom.isLocked() && ! player.find("keys")) {
            System.out.println("That door is locked!");
            return false;
        }
        else if (nextRoom.isLocked()) {
            System.out.println("You unlock the door.");
            nextRoom.unlock();
            currentRoom = nextRoom;
            look();
            return false;
        }
        else if (direction.equals("up") && nextRoom.getShortDescription().equals("on the roof")) {
            System.out.println("The door locks behind you!");
            currentRoom.lock();
            currentRoom = nextRoom;
            look();
            return false;
        }
        else if (direction.equals("jump") && nextRoom.getShortDescription().equals("outside the main entrance of the university")) {
            System.out.println("You jumped off the roof. That was unwise.");
            return true;
        }
        else {
            currentRoom = nextRoom;
            look();
            return false;
        }
    }
    
    /**
     * Look around the current room.
     */
    private void look()
    {
        System.out.println(currentRoom.getLongDescription());
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
