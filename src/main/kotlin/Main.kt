package br.com.lukinhasssss

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import kotlin.time.Duration.Companion.seconds

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
suspend fun main() {
    val openAiSecrets = SecretsManagerImpl().getSecretAsObject<OpenAiIntegrationSecrets>("openai-integration-secrets")
    val socketTimeout = 60.seconds

    val openAiConfig = OpenAIConfig(
        token = openAiSecrets.apiKey,
        timeout = Timeout(socket = socketTimeout)
    )

    val openAiService = OpenAI(openAiConfig)

    val system = ChatMessage.System("You are a customer service representative helping a customer to create a shopping list, but you can only write 5 products.")
    val user = ChatMessage.User("Generate 5 products")

    val completionRequest = ChatCompletionRequest(
        model = ModelId("gpt-4"),
        messages = listOf(system, user)
    )

    openAiService.chatCompletion(completionRequest).choices.forEach { println(it) }
}