import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    // -------------------------------------------------------
    // Test Data from Part 3 assignment:
    // Message 1: +27834557896 - "Did you get the cake?" - Sent
    // Message 2: +27838884567 - "Where are you? You are late! I have asked you to be on time." - Stored
    // Message 3: +27834484567 - "Yohoooo, I am at your gate." - Disregard
    // Message 4: 0838884567   - "It is dinner time!" - Sent (no international code - invalid)
    // Message 5: +27838884567 - "Ok, I am leaving without you." - Stored
    // -------------------------------------------------------

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @BeforeEach
    public void setUp() {
        Message.resetMessages();
        message1 = new Message("+27834557896", "Did you get the cake?", 1);
        message2 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", 2);
        message3 = new Message("+27834484567", "Yohoooo, I am at your gate.", 3);
        message4 = new Message("0838884567", "It is dinner time!", 4);
        message5 = new Message("+27838884567", "Ok, I am leaving without you.", 5);

        // Populate arrays with flags
        message1.SentMessage(1);    // Sent
        message2.SentMessage(3);    // Stored
        message3.SentMessage(2);    // Disregard
        message4.SentMessage(1);    // Sent
        message5.SentMessage(3);    // Stored
    }

    // -------------------------------------------------------
    // Part 1 & 2 Tests
    // -------------------------------------------------------

    @Test
    public void testCheckMessageID_Success() {
        assertTrue(message1.checkMessageID());
    }

    @Test
    public void testCheckMessageID_IDIsExactlyTenChars() {
        assertEquals(10, message1.getMessageID().length());
    }

    @Test
    public void testCheckRecipientCell_Success() {
        assertEquals("Cell phone number successfully captured.",
                message1.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCell_Failure() {
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            message4.checkRecipientCell()
        );
    }

    @Test
    public void testCheckMessageLength_Success() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    public void testCheckMessageLength_Failure() {
        Message longMsg = new Message("+27834557896", "A".repeat(260), 6);
        String result = longMsg.checkMessageLength();
        assertTrue(result.contains("exceeds 250 characters"));
    }

    @Test
    public void testCreateMessageHash_HasThreeParts() {
        String[] parts = message1.getMessageHash().split(":");
        assertEquals(3, parts.length);
    }

    @Test
    public void testCreateMessageHash_IsUpperCase() {
        String hash = message1.getMessageHash();
        assertEquals(hash.toUpperCase(), hash);
    }

    @Test
    public void testCreateMessageHash_ContainsFirstAndLastWord() {
        // Message: "Did you get the cake?"
        // First word: DID, Last word: CAKE
        String hash = message1.getMessageHash();
        assertTrue(hash.contains("DID"));
        assertTrue(hash.contains("CAKE"));
    }

    @Test
    public void testSentMessage_Send() {
        Message.resetMessages();
        Message m = new Message("+27834557896", "Did you get the cake?", 1);
        assertEquals("Message successfully sent.", m.SentMessage(1));
    }

    @Test
    public void testSentMessage_Disregard() {
        Message.resetMessages();
        Message m = new Message("+27834557896", "Did you get the cake?", 1);
        assertEquals("Press 0 to delete the message.", m.SentMessage(2));
    }

    @Test
    public void testSentMessage_Store() {
        Message.resetMessages();
        Message m = new Message("+27834557896", "Did you get the cake?", 1);
        assertEquals("Message successfully stored.", m.SentMessage(3));
    }

    @Test
    public void testReturnTotalMessagess_TwoSent() {
        // message1 (Sent) and message4 (Sent) = 2
        assertEquals(2, Message.returnTotalMessagess());
    }

    @Test
    public void testReturnTotalMessagess_DisregardNotCounted() {
        // message3 was disregarded - should not count
        assertEquals(2, Message.returnTotalMessagess());
    }

    @Test
    public void testPrintMessages_ShowsSentMessage() {
        assertTrue(Message.printMessages().contains(message1.getMessageID()));
    }

    @Test
    public void testPrintMessages_EmptyWhenNoneSent() {
        Message.resetMessages();
        assertEquals("No messages have been sent.", Message.printMessages());
    }

    // -------------------------------------------------------
    // Part 3 Tests
    // -------------------------------------------------------

    // Test: Sent Messages array contains expected test data
    @Test
    public void testSentMessagesArray_ContainsExpectedData() {
        assertTrue(Message.getSentMessages().contains("Did you get the cake?"));
        assertTrue(Message.getSentMessages().contains("It is dinner time!"));
    }

    // Test: Display longest message
    @Test
    public void testDisplayLongestMessage() {
        String result = Message.displayLongestMessage();
        // Message 2 is the longest: "Where are you? You are late! I have asked you to be on time."
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
    }

    // Test: Search by Message ID - Message 4
    @Test
    public void testSearchByMessageID() {
        String id = message4.getMessageID();
        String result = Message.searchByMessageID(id);
        assertTrue(result.contains("It is dinner time!"));
        assertTrue(result.contains("0838884567"));
    }

    // Test: Search messages for a particular recipient +27838884567
    @Test
    public void testSearchByRecipient() {
        String result = Message.searchByRecipient("+27838884567");
        // Should return message2 and message5
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    // Test: Delete message using message hash - Message 2
    @Test
    public void testDeleteMessageByHash() {
        String hash = message2.getMessageHash();
        String result = Message.deleteMessageByHash(hash);
        assertTrue(result.contains("successfully deleted"));
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
    }

    // Test: Display report shows all messages
    @Test
    public void testDisplayReport() {
        String report = Message.displayReport();
        assertTrue(report.contains("Message Hash"));
        assertTrue(report.contains("Recipient"));
        assertTrue(report.contains("Message"));
    }
}