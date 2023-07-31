class Main {}

data class Message(val id: Int, val senderId: Int, var content: String, var isRead: Boolean = false)

data class Chat(val id: Int, val messages: MutableList<Message> = mutableListOf())

class ChatService {
    private val chats: MutableMap<Int, Chat> = mutableMapOf()
    private var chatIdCounter = 1
    private var messageIdCounter = 1


    fun createChat(userId: Int) {
        if (!chats.containsKey(userId)) chats[userId] = Chat(chatIdCounter++)
    }

    fun deleteChat(userId: Int) {
        chats.remove(userId)
    }

    fun getChats(userId: Int): List<Chat> {
        return chats.values.toList()
    }

    fun getUnreadChatsCount(userId: Int): Int {
        return chats.values.count { chat -> chat.messages.any { message -> message.senderId != userId && !message.isRead } }
    }

    fun getLatestMessage(userId: Int): List<String> {
        return chats.values.map { chat -> chat.messages.lastOrNull()?.content ?: "Нет сообщений" }
    }

    fun getMessageFromChat(userId: Int, chatId: Int, lastMessageId: Int, count: Int): List<Message> {
        val chat = chats[chatId]
        return chat?.let {
            val messages = it.messages.dropWhile { message -> message.id <= lastMessageId }.take(count)
            messages.forEach { message -> message.isRead = true }
            messages
        } ?: emptyList()
    }

    fun createMessage(userId: Int, chatId: Int, text: String) {
        val chat = chats[chatId] ?: run {
            createChat(userId)
            chats[chatId]
        }
        val message = Message(messageIdCounter++, userId, text)
        chat?.messages?.add(message)
    }

    fun deleteMessage(userId: Int, chatId: Int, messageId: Int) {
        val chat = chats[chatId]
        chat?.messages?.removeIf { message -> message.id == messageId }
    }
}
