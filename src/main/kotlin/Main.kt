fun main() {
    val service = ChatService()

    // Создание чата и отправка сообщений
    service.createMessage(1, 1, "Привет!")
    service.createMessage(2, 1, "Привет, как дела?")
    service.createMessage(1, 1, "Всё хорошо, спасибо!")
    service.createMessage(2, 1, "Отлично")

    // Получение списка чатов
    val chats = service.getChats(1)
    println("Список чатов:")
    chats.forEachIndexed { index, chat ->
        println("Чат ${index + 1} (ID: ${chat.id})")
    }

    // Получение количества непрочитанных чатов
    val unreadChatsCount = service.getUnreadChatsCount(1)
    println("Количество непрочитанных чатов: $unreadChatsCount")

    // Получение последних сообщений из чатов
    val latestMessages = service.getLatestMessage(1)
    println("Последние сообщения из чатов:")
    latestMessages.forEachIndexed { index, message ->
        println("Сообщение ${index + 1}: $message")
    }

    // Получение сообщений из чата
    val messages = service.getMessageFromChat(1, 1, 0, 2)
    println("Сообщения из чата 1:")
    messages.forEachIndexed { index, message ->
        println("Сообщение ${index + 1} (ID: ${message.id}): ${message.content}")
    }

    // Удаление сообщения
    service.deleteMessage(1, 1, 1)
    val updatedMessages = service.getMessageFromChat(1, 1, 0, 10)
    println("Сообщения из чата 1 после удаления:")
    updatedMessages.forEachIndexed { index, message ->
        println("Сообщение ${index + 1} (ID: ${message.id}): ${message.content}")
    }

    // Удаление чата
    service.deleteChat(1)
    val remainingChats = service.getChats(1)
    println("Оставшиеся чаты после удаления:")
    remainingChats.forEachIndexed { index, chat ->
        println("Чат ${index + 1} (ID: ${chat.id})")
    }
}

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
