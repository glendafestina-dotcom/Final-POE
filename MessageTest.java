import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        Message.resetMessages();
        message1 = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?", 1);
        message2 = new Message("08575975889", "Hi Keegan, did you receive the payment?", 2);
    }

    // --- checkMessageID ---

    @Test
    public void testCheckMessageID_Success() {
        assertTrue(message1.checkMessageID());
    }

    @Test
    public void testCheckMessageID_IDIsExactlyTenChars() {
        assertEquals(10, message1.getMessageID().length());
    }

    // --- checkRecipientCell ---

    @Test
    public void testCheckRecipientCell_Success() {
        assertEquals("Cell phone number successfully captured.",
                message1.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCell_Failure() {
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            message2.checkRecipientCell()
        );
    }

    // --- checkMessageLength ---

    @Test
    public void testCheckMessageLength_Success() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    public void testCheckMessageLength_Failure() {
        Message longMsg = new Message("+27718693002", "A".repeat(260), 3);
        String result = longMsg.checkMessageLength();
        assertTrue(result.contains("exceeds 250 characters"));
    }

    // --- createMessageHash ---

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
        String hash = message1.getMessageHash();
        assertTrue(hash.contains("HI"));
        assertTrue(hash.contains("TONIGHT"));
    }

    // --- SentMessage ---

    @Test
    public void testSentMessage_Send() {
        assertEquals("Message successfully sent.", message1.SentMessage(1));
    }

    @Test
    public void testSentMessage_Disregard() {
        assertEquals("Press 0 to delete the message.", message1.SentMessage(2));
    }

    @Test
    public void testSentMessage_Store() {
        assertEquals("Message successfully stored.", message1.SentMessage(3));
    }

    // --- returnTotalMessagess ---

    @Test
    public void testReturnTotalMessagess_TwoSent() {
        message1.SentMessage(1);
        message2.SentMessage(1);
        assertEquals(2, Message.returnTotalMessagess());
    }

    @Test
    public void testReturnTotalMessagess_DisregardNotCounted() {
        message1.SentMessage(1);
        message2.SentMessage(2);
        assertEquals(1, Message.returnTotalMessagess());
    }

    // --- printMessages ---

    @Test
    public void testPrintMessages_ShowsSentMessage() {
        message1.SentMessage(1);
        assertTrue(Message.printMessages().contains(message1.getMessageID()));
    }

    @Test
    public void testPrintMessages_EmptyWhenNoneSent() {
        assertEquals("No messages have been sent.", Message.printMessages());
    }
}