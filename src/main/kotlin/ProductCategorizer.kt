package br.com.lukinhasssss

import br.com.lukinhasssss.OpenAIUtils.createOpenAIService
import br.com.lukinhasssss.OpenAIUtils.sendRequestToOpenAI

// Constantes
const val EMPTY = ""


// Função principal
suspend fun main() {
    val service = createOpenAIService()

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

        val response = sendRequestToOpenAI(
            service = service,
            systemPrompt = systemPrompt,
            userPrompt = productName
        )

        response.choices.forEach { println(it.message.content) }
    }
}

// Função para ler entrada válida do usuário
private fun readValidInput(prompt: String, errorMessage: String): String {
    while (true) {
        println(prompt)
        val input = readLine() ?: EMPTY
        if (input.isNotBlank()) return input
        println(errorMessage)
    }
}

// Função para criar o prompt do sistema
private fun createSystemPrompt(categories: String): String {
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
