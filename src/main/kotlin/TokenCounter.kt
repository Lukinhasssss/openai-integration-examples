package br.com.lukinhasssss

import com.aallam.ktoken.Tokenizer
import java.math.BigDecimal

suspend fun main() {
    val tokenizer = Tokenizer.of(model = "gpt-4-turbo")

    val quantidadeDeTokens = tokenizer.encode("Identifique o perfil de compra de cade cliente").size

    val custo = BigDecimal.valueOf(quantidadeDeTokens.toLong()) // Quantidade de tokens
        .divide(BigDecimal.valueOf(1000)) // A divisao por 1000 é devido a cobranca ser a cada 1000 tokens
        .multiply(BigDecimal.valueOf(0.0000025)) // Custo por token

    println("$quantidadeDeTokens tokens equivalem a R$ $custo")
}
