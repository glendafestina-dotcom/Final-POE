import java.util.Scanner;

public class QuickChat {

    private static String registeredUsername = "";
    private static String registeredPassword = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("====== QUICKCHAT FINAL POE =====");

        // Registration
        System.out.println("\n===== REGISTRATION =====");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Enter username: ");
        registeredUsername = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        registeredPassword = scanner.nextLine().trim();

        System.out.println("Account successfully created for " 
                + firstName + " " + lastName + "!");

        // Login
        System.out.println("\n===== LOGIN =====");
        if (!login(scanner)) {
            System.out.println("Login failed. Exiting QuickChat.");
            scanner.close();
            return;
        }

        System.out.println("\nWelcome to QuickChat, " + firstName + "!");

        System.out.print("\nHow many messages would you like to send? ");
        int numMessages = Integer.parseInt(scanner.nextLine().trim());

        boolean running = true;
        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    sendMessages(scanner, numMessages);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    break;
                case "4":
                    storedMessagesMenu(scanner);
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }

        System.out.println("\nTotal messages sent: " 
                + Message.returnTotalMessagess());
        System.out.println("\nAll sent messages:");
        System.out.println(Message.printMessages());

        scanner.close();
    }

    private static boolean login(Scanner scanner) {
        System.out.print("Enter username: ");
        String user = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine().trim();

        if (user.equals(registeredUsername) && pass.equals(registeredPassword)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Incorrect username or password.");
            return false;
        }
    }

    private static void sendMessages(Scanner scanner, int numMessages) {
        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

            System.out.print("Enter recipient cell number (e.g. +27834557896): ");
            String recipient = scanner.nextLine().trim();

            System.out.print("Enter message (max 250 characters): ");
            String text = scanner.nextLine().trim();

            Message msg = new Message(recipient, text, i);

            if (!msg.checkMessageID()) {
                System.out.println("Error: Message ID exceeds 10 characters.");
                i--;
                continue;
            }
            System.out.println("Message ID generated: " + msg.getMessageID());

            String cellCheck = msg.checkRecipientCell();
            System.out.println(cellCheck);
            if (!cellCheck.contains("successfully")) {
                i--;
                continue;
            }

            String lengthCheck = msg.checkMessageLength();
            System.out.println(lengthCheck);
            if (!lengthCheck.equals("Message ready to send.")) {
                i--;
                continue;
            }

            System.out.println("Message Hash: " + msg.getMessageHash());

            System.out.println("\n1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message");
            System.out.print("Select option: ");
            int sendChoice = Integer.parseInt(scanner.nextLine().trim());

            System.out.println(msg.SentMessage(sendChoice));

            if (sendChoice == 1) {
                System.out.println("\n--- Message Details ---");
                System.out.println("Message ID: "   + msg.getMessageID());
                System.out.println("Message Hash: " + msg.getMessageHash());
                System.out.println("Recipient: "    + msg.getRecipientCell());
                System.out.println("Message: "      + msg.getMessageText());
            }
        }

        System.out.println("\nTotal messages sent: " 
                + Message.returnTotalMessagess());
    }

    private static void storedMessagesMenu(Scanner scanner) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest message");
            System.out.println("c) Search for a message by Message ID");
            System.out.println("d) Search messages for a particular recipient");
            System.out.println("e) Delete a message using message hash");
            System.out.println("f) Display full report of all messages");
            System.out.println("x) Back to main menu");
            System.out.print("Select option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "a":
                    System.out.println(Message.displayStoredMessages());
                    break;
                case "b":
                    System.out.println(Message.displayLongestMessage());
                    break;
                case "c":
                    System.out.print("Enter Message ID to search: ");
                    String id = scanner.nextLine().trim();
                    System.out.println(Message.searchByMessageID(id));
                    break;
                case "d":
                    System.out.print("Enter recipient number to search: ");
                    String recipient = scanner.nextLine().trim();
                    System.out.println(Message.searchByRecipient(recipient));
                    break;
                case "e":
                    System.out.print("Enter message hash to delete: ");
                    String hash = scanner.nextLine().trim();
                    System.out.println(Message.deleteMessageByHash(hash));
                    break;
                case "f":
                    System.out.println(Message.displayReport());
                    break;
                case "x":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }
}
