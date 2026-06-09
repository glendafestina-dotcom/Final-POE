import java.util.ArrayList;
import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipientCell;
    private String messageText;
    private String messageHash;
    private String flag;

    // Arrays to store different types of messages
    private static ArrayList<String> sentMessages = new ArrayList<>();
    private static ArrayList<String> disregardedMessages = new ArrayList<>();
    private static ArrayList<String> storedMessages = new ArrayList<>();
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();
    private static ArrayList<Message> allMessages = new ArrayList<>();

    private static int totalMessagesSent = 0;

    public Message(String recipientCell, String messageText, int messageNumber) {
        this.recipientCell = recipientCell;
        this.messageText = messageText;
        this.messageNumber = messageNumber;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        long id = (long)(rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipientCell.startsWith("+") && recipientCell.length() <= 13) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        messageHash = (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                flag = "Sent";
                totalMessagesSent++;
                sentMessages.add(messageText);
                messageHashes.add(messageHash);
                messageIDs.add(messageID);
                allMessages.add(this);
                return "Message successfully sent.";
            case 2:
                flag = "Disregard";
                disregardedMessages.add(messageText);
                allMessages.add(this);
                return "Press 0 to delete the message.";
            case 3:
                flag = "Stored";
                storedMessages.add(messageText);
                messageHashes.add(messageHash);
                messageIDs.add(messageID);
                allMessages.add(this);
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allMessages.size(); i++) {
            Message m = allMessages.get(i);
            if (m.flag != null && m.flag.equals("Sent")) {
                sb.append("--- Message ").append(i + 1).append(" ---\n");
                sb.append("Message ID: ").append(m.messageID).append("\n");
                sb.append("Message Hash: ").append(m.messageHash).append("\n");
                sb.append("Recipient: ").append(m.recipientCell).append("\n");
                sb.append("Message: ").append(m.messageText).append("\n");
            }
        }
        return sb.toString();
    }

    public static int returnTotalMessagess() {
        return totalMessagesSent;
    }

    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    public void storeMessage() {
        System.out.println("Message stored successfully.");
    }

    // -------------------------------------------------------
    // Part 3 Methods
    // -------------------------------------------------------

    // Display sender and recipient of all stored messages
    public static String displayStoredMessages() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- Stored Messages ---\n");
        for (Message m : allMessages) {
            if (m.flag != null && m.flag.equals("Stored")) {
                sb.append("Recipient: ").append(m.recipientCell).append("\n");
                sb.append("Message: ").append(m.messageText).append("\n\n");
            }
        }
        return sb.toString();
    }

    // Display the longest stored message
    public static String displayLongestMessage() {
        if (allMessages.isEmpty()) {
            return "No messages found.";
        }
        String longest = "";
        for (Message m : allMessages) {
            if (m.messageText.length() > longest.length()) {
                longest = m.messageText;
            }
        }
        return "Longest message: " + longest;
    }

    // Search for a message by message ID
    public static String searchByMessageID(String id) {
        for (Message m : allMessages) {
            if (m.messageID.equals(id)) {
                return "Recipient: " + m.recipientCell + "\nMessage: " + m.messageText;
            }
        }
        return "Message ID not found.";
    }

    // Search all messages for a particular recipient
    public static String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (Message m : allMessages) {
            if (m.recipientCell.equals(recipient)) {
                sb.append("Message: ").append(m.messageText).append("\n");
            }
        }
        if (sb.length() == 0) {
            return "No messages found for recipient: " + recipient;
        }
        return sb.toString();
    }

    // Delete a message using message hash
    public static String deleteMessageByHash(String hash) {
        for (int i = 0; i < allMessages.size(); i++) {
            Message m = allMessages.get(i);
            if (m.messageHash.equals(hash)) {
                String deletedText = m.messageText;
                allMessages.remove(i);
                messageHashes.remove(hash);
                messageIDs.remove(m.messageID);
                storedMessages.remove(deletedText);
                sentMessages.remove(deletedText);
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    // Display full report of all stored messages
    public static String displayReport() {
        if (allMessages.isEmpty()) {
            return "No messages to display.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========== FULL MESSAGE REPORT ==========\n");
        for (Message m : allMessages) {
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient: ").append(m.recipientCell).append("\n");
            sb.append("Message: ").append(m.messageText).append("\n");
            sb.append("Flag: ").append(m.flag).append("\n");
            sb.append("-----------------------------------------\n");
        }
        return sb.toString();
    }

    public static void resetMessages() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        allMessages.clear();
        totalMessagesSent = 0;
    }

    public String getMessageID()        { return messageID; }
    public String getMessageHash()      { return messageHash; }
    public String getRecipientCell()    { return recipientCell; }
    public String getMessageText()      { return messageText; }
    public int getMessageNumber()       { return messageNumber; }
    public String getFlag()             { return flag; }
    public static ArrayList<Message> getAllMessages() { return allMessages; }
    public static ArrayList<String> getSentMessages() { return sentMessages; }
    public static ArrayList<String> getStoredMessages() { return storedMessages; }
}
