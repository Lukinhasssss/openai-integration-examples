package br.com.lukinhasssss

import br.com.lukinhasssss.aws.SecretsManagerImpl
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import kotlin.time.Duration.Companion.seconds

// Constantes
const val EMPTY = ""
const val MODEL_ID = "gpt-4"

// Função principal
suspend fun main() {
    val openAiService = createOpenAiService()

    val categories = readValidInput(
        prompt = "\nEnter the valid categories for the products:",
        errorMessage = "\nAt least one category must be informed."
    )

    while (true) {
        val productName = readValidInput(
            prompt = "\nEnter the product name:",
            errorMessage = "\nThe product name must be informed."
        )

        val systemPrompt = createSystemPrompt(categories)
        sendRequestToOpenAi(openAiService, systemPrompt, productName)
    }
}

// Função para criar o serviço OpenAI
fun createOpenAiService(): OpenAI {
    val openAiSecrets = SecretsManagerImpl().getSecretAsObject<OpenAiIntegrationSecrets>("openai-integration-secrets")
    val socketTimeout = 60.seconds

    val openAiConfig = OpenAIConfig(
        token = openAiSecrets.apiKey,
        timeout = Timeout(socket = socketTimeout)
    )

    return OpenAI(openAiConfig)
}

// Função para ler entrada válida do usuário
fun readValidInput(prompt: String, errorMessage: String): String {
    while (true) {
        println(prompt)
        val input = readLine() ?: EMPTY
        if (input.isNotBlank()) return input
        println(errorMessage)
    }
}

// Função para criar o prompt do sistema
fun createSystemPrompt(categories: String): String {
    return """
        You are a product categorizer and should respond with the category of the informed product.
        Choose between one of the following categories: $categories.
        
        ### Usage example:
        
        Q: Headset Audeze Maxwell
        A: Electronics
        
        ### Rules to be followed:
        If the user input isn't a product name, you should respond with the available categories that the user can choose from.
    """.trimIndent()
}

// Função para enviar requisição ao OpenAI
suspend fun sendRequestToOpenAi(service: OpenAI, systemPrompt: String, userPrompt: String) {
    val systemMessage = ChatMessage.System(systemPrompt)
    val userMessage = ChatMessage.User(userPrompt)

    val completionRequest = ChatCompletionRequest(
        model = ModelId(MODEL_ID),
        messages = listOf(systemMessage, userMessage)
    )

    try {
        val response = service.chatCompletion(completionRequest)
        response.choices.forEach { println(it.message.content) }
    } catch (e: Exception) {
        println("An error occurred: ${e.message}")
    }
}
