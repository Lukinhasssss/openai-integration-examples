package br.com.lukinhasssss

suspend fun main() {
    val service = OpenAIUtils.createOpenAIService()

    val systemPrompt = "You are a customer service representative helping a customer to create a shopping list, but you can only write 5 products."
    val userPrompt = "Generate 5 products"

    val response = OpenAIUtils.sendRequestToOpenAI(
        service = service,
        systemPrompt = systemPrompt,
        userPrompt = userPrompt
    )

    response.choices.forEach { println(it.message.content) }
}