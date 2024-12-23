package br.com.lukinhasssss

import br.com.lukinhasssss.OpenAIUtils.DEFAULT_MODEL_ID
import br.com.lukinhasssss.TokenCounter.count
import com.aallam.ktoken.Tokenizer
import java.math.BigDecimal

object TokenCounter {
    suspend fun count(text: String, model: String = DEFAULT_MODEL_ID): Int {
        val tokenizer = Tokenizer.of(model = model)
        return tokenizer.encode(text).size
    }
}

suspend fun main() {
    val quantidadeDeTokens = count("Identifique o perfil de compra de cade cliente")

    val custo = BigDecimal.valueOf(quantidadeDeTokens.toLong()) // Quantidade de tokens
        .divide(BigDecimal.valueOf(1000)) // A divisao por 1000 é devido a cobranca ser a cada 1000 tokens
        .multiply(BigDecimal.valueOf(0.0000025)) // Custo por token

    println("$quantidadeDeTokens tokens equivalem a R$ $custo")
}
