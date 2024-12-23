package br.com.lukinhasssss

import br.com.lukinhasssss.OpenAIUtils.createOpenAIService
import br.com.lukinhasssss.OpenAIUtils.sendRequestToOpenAI
import br.com.lukinhasssss.TokenCounter.count
import java.nio.file.Files
import java.nio.file.Path

suspend fun main() {
    val service = createOpenAIService()

    val systemPrompt =
        """
            Identify the purchase profile of each customer.
            
            The response should be:
            
            Customer - describe the customer's profile in three words
        """.trimIndent()

    val customers = loadCustomersFromFile()

    val tokenCount = count(customers)

    val model = if (tokenCount > 4096) "gpt-3.5-turbo-16k" else "gpt-3.5-turbo"

    val response = sendRequestToOpenAI(
        model = model,
        service = service,
        systemPrompts = listOf(systemPrompt, customers)
    )

    println(response.choices.first().message.content)

    println("Token count and model from application: {tokenCount: $tokenCount, modelUsed: $model}\n")
    println("Token count and model from OpenAI: {tokenCount: ${response.usage?.promptTokens}, modelUsed: ${response.model.id}}\n")
    println("Usage: ${response.usage}\n")
}

private fun loadCustomersFromFile(): String =
    try {
        val path = Path.of(
            ClassLoader.getSystemResource("purchase_list_with_100_customers.csv").toURI()
        )
        Files.readAllLines(path).joinToString("\n")
    } catch (e: Exception) {
        throw RuntimeException("Error loading the file!", e)
    }
