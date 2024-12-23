package br.com.lukinhasssss

import br.com.lukinhasssss.aws.OpenAIIntegrationSecrets
import br.com.lukinhasssss.aws.SecretsManagerImpl
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import kotlin.time.Duration.Companion.seconds

object OpenAIUtils {
    const val DEFAULT_MODEL_ID = "gpt-4-turbo"

    // Função para criar o serviço OpenAI
    fun createOpenAIService(): OpenAI {
        val openAiSecrets = SecretsManagerImpl().getSecretAsObject<OpenAIIntegrationSecrets>("openai-integration-secrets")
        val socketTimeout = 60.seconds

        val openAiConfig = OpenAIConfig(
            token = openAiSecrets.apiKey,
            timeout = Timeout(socket = socketTimeout)
        )

        return OpenAI(openAiConfig)
    }

    // Função para enviar requisição ao OpenAI utilizando SystemPrompt e UserPrompt
    suspend fun sendRequestToOpenAI(
        model: String = DEFAULT_MODEL_ID,
        service: OpenAI,
        systemPrompt: String,
        userPrompt: String
    ): ChatCompletion = try {
        val systemMessage = ChatMessage.System(systemPrompt)
        val userMessage = ChatMessage.User(userPrompt)

        val completionRequest = ChatCompletionRequest(
            model = ModelId(model),
            messages = listOf(systemMessage, userMessage)
        )

        service.chatCompletion(completionRequest)
    } catch (e: Exception) {
        throw RuntimeException("Error sending request to OpenAI!", e.cause)
    }

    /**
     * Função para enviar requisição ao OpenAI utilizando somente SystemPrompt
     * e com a possibilidade de passar uma lista de Prompts
     */
    suspend fun sendRequestToOpenAI(
        model: String = DEFAULT_MODEL_ID,
        service: OpenAI,
        systemPrompts: List<String>,
    ): ChatCompletion = try {
        val chatMessages = systemPrompts.map { ChatMessage.System(it) }

        val request = ChatCompletionRequest(
            model = ModelId(model),
            messages = chatMessages
        )

        service.chatCompletion(request)
    } catch (e: Exception) {
        throw RuntimeException("Error sending request to OpenAI!", e.cause)
    }
}