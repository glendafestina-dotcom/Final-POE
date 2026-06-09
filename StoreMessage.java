import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 

public class StoreMessage {

    private static final String FILE_NAME = "stored_messages.json";

    // -------------------------------------------------------
    // Store a single message into the JSON file
    // -------------------------------------------------------
    @SuppressWarnings("unchecked")
    public static String storeMessage(String messageID, String messageHash,
                                       String recipient, String message) {
        JSONArray messageList = readExistingMessages();

        JSONObject messageObj = new JSONObject();
        messageObj.put("messageID", messageID);
        messageObj.put("messageHash", messageHash);
        messageObj.put("recipient", recipient);
        messageObj.put("message", message);

        messageList.add(messageObj);

        try (FileWriter file = new FileWriter(FILE_NAME)) {
            file.write(messageList.toJSONString());
            file.flush();
            return "Message successfully stored.";
        } catch (IOException e) {
            return "Error storing message: " + e.getMessage();
        }
    }

    // -------------------------------------------------------
    // Read all stored messages from the JSON file
    // -------------------------------------------------------
    public static JSONArray readExistingMessages() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Object obj = parser.parse(reader);
            return (JSONArray) obj;
        } catch (IOException | ParseException e) {
            return new JSONArray();
        }
    }

    // -------------------------------------------------------
    // Get all stored messages as an ArrayList of strings
    // -------------------------------------------------------
    public static ArrayList<String> getStoredMessages() {
        ArrayList<String> messages = new ArrayList<>();
        JSONArray jsonArray = readExistingMessages();
        for (Object obj : jsonArray) {
            JSONObject jsonObj = (JSONObject) obj;
            messages.add((String) jsonObj.get("message"));
        }
        return messages;
    }

    // -------------------------------------------------------
    // Display all stored messages
    // -------------------------------------------------------
    public static String displayStoredMessages() {
        JSONArray jsonArray = readExistingMessages();
        if (jsonArray.isEmpty()) {
            return "No stored messages found.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- Stored Messages ---\n");
        for (Object obj : jsonArray) {
            JSONObject jsonObj = (JSONObject) obj;
            sb.append("Message ID: ").append(jsonObj.get("messageID")).append("\n");
            sb.append("Message Hash: ").append(jsonObj.get("messageHash")).append("\n");
            sb.append("Recipient: ").append(jsonObj.get("recipient")).append("\n");
            sb.append("Message: ").append(jsonObj.get("message")).append("\n\n");
        }
        return sb.toString();
    }

    // -------------------------------------------------------
    // Delete a message by hash from the JSON file
    // -------------------------------------------------------
    @SuppressWarnings("unchecked")
    public static String deleteMessageByHash(String hash) {
        JSONArray jsonArray = readExistingMessages();
        JSONArray updatedArray = new JSONArray();
        boolean found = false;
        String deletedMessage = "";

        for (Object obj : jsonArray) {
            JSONObject jsonObj = (JSONObject) obj;
            if (jsonObj.get("messageHash").equals(hash)) {
                found = true;
                deletedMessage = (String) jsonObj.get("message");
            } else {
                updatedArray.add(jsonObj);
            }
        }

        if (!found) {
            return "Message hash not found.";
        }

        try (FileWriter file = new FileWriter(FILE_NAME)) {
            file.write(updatedArray.toJSONString());
            file.flush();
            return "Message: \"" + deletedMessage + "\" successfully deleted.";
        } catch (IOException e) {
            return "Error deleting message: " + e.getMessage();
        }
    }
}
