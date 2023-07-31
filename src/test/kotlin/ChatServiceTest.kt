import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ChatServiceTest {
    private lateinit var chatService: ChatService

    @Before
    fun setUp() {
        chatService = ChatService()
    }

    @Test
    fun createMessage_existingChat_chatHasMessage() {
        val userId = 1
        val chatId = 1
        val content = "Hello!"

        chatService.createMessage(userId, chatId, content)

        val chat = chatService.getChats(userId).find { it.id == chatId }
        val messages = chat?.messages
        assertEquals(1, messages?.size)
        assertEquals(content, messages?.first()?.content)
    }

    @Test
    fun createMessage_newChat_chatIsCreated() {
        val userId = 1
        val chatId = 1
        val content = "Hello!"

        chatService.createMessage(userId, chatId, content)

        val chat = chatService.getChats(userId).find { it.id == chatId }
        assertTrue(chat != null)
    }

    @Test
    fun deleteMessage_existingMessage_messageIsDeleted() {
        val userId = 1
        val chatId = 1
        val messageId = 1
        val content = "Hello!"
        chatService.createMessage(userId, chatId, content)

        chatService.deleteMessage(userId, chatId, messageId)

        val chat = chatService.getChats(userId).find { it.id == chatId }
        val messages = chat?.messages
        assertEquals(0, messages?.size)
    }

    @Test
    fun deleteMessage_nonExistingMessage_noChange() {
        val userId = 1
        val chatId = 1
        val messageId = 1
        val content = "Hello!"
        chatService.createMessage(userId, chatId, content)

        chatService.deleteMessage(userId, chatId, messageId + 1)

        val chat = chatService.getChats(userId).find { it.id == chatId }
        val messages = chat?.messages
        assertEquals(1, messages?.size)
    }
}