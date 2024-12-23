package br.com.lukinhasssss

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException

class SecretsManagerImpl {
    fun getSecretAsString(secretName: String): String {
        val secretsManagerClient = AwsSecretsManagerConfig.createClient()

        try {
            val getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build()

            val getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest)

            return getSecretValueResponse.secretString()
        } catch (e: SecretsManagerException) {
            throw RuntimeException("Error getting secret $secretName", e)
        } finally {
            secretsManagerClient.close()
        }
    }

    inline fun <reified T> getSecretAsObject(secretName: String): T {
        val secretAsString = getSecretAsString(secretName)

        try {
            val objectMapper = jacksonObjectMapper()
            return objectMapper.readValue(secretAsString, T::class.java)
        } catch (e: Exception) {
            throw RuntimeException("Error parsing secret $secretName", e)
        }
    }
}