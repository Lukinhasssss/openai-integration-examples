package br.com.lukinhasssss

import br.com.lukinhasssss.OpenAIUtils.createOpenAIService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

suspend fun main() {
    val service = createOpenAIService()

    val systemPrompt = """
        You are a product review sentiment analyzer.
        Write a paragraph of up to 50 words summarizing the reviews and then assign a general sentiment to the product.
        Identify also 3 strengths and 3 weaknesses based on the reviews.
        
        #### Output format
        Product name:
        Review summary: [summarize in up to 50 words]
        General sentiment: [should be: POSITIVE, NEUTRAL, or NEGATIVE]
        Strengths: [3 bullet points]
        Weaknesses: [3 bullet points]
    """

    val reviewsDirectory = Path.of("src/main/resources/reviews")
    val reviewsFiles = Files.walk(reviewsDirectory, 1)
        .filter { it.toString().endsWith(".txt") }
        .map { it.fileName.toString().removePrefix("reviews-").removeSuffix(".txt") }
        .toList()

    reviewsFiles.forEach { fileName ->
        println("Analyzing product: $fileName...\n")

        val userPrompt = loadFile(fileName)

        val response = OpenAIUtils.sendRequestToOpenAI(
            service = service,
            systemPrompt = systemPrompt,
            userPrompt = userPrompt
        ).choices.first().message.content

        saveAnalysis(fileName, response ?: throw RuntimeException("No response from OpenAI!")).also {
            println("Analysis saved for product: $fileName\n")
        }
    }.also {
        println("\nAll products analyzed!\n")
        service.close()
    }
}

private fun loadFile(fileName: String): String {
    return try {
        val path = Path.of("src/main/resources/reviews/reviews-$fileName.txt")
        Files.readAllLines(path).toString()
    } catch (e: Exception) {
        throw RuntimeException("Error loading the file!", e.cause)
    }
}

private fun saveAnalysis(fileName: String, analysis: String) {
    try {
        val path = Path.of("src/main/resources/analysis/sentiment-analysis-$fileName.txt")
        Files.writeString(path, analysis, StandardOpenOption.CREATE_NEW)
    } catch (e: Exception) {
        throw RuntimeException("Error saving the file!", e.cause)
    }
}
