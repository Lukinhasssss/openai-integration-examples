package br.com.lukinhasssss

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import java.net.URI

object AwsSecretsManagerConfig {
    fun createClient(): SecretsManagerClient =
        try {
            SecretsManagerClient
                .builder()
                .region(Region.SA_EAST_1)
                .credentialsProvider { AwsBasicCredentials.create("localstack", "localstack") }
                .endpointOverride(URI.create("http://localhost:4566"))
                .build()
        } catch (e: Exception) {
            throw RuntimeException("Error creating SecretsManagerClient", e)
        }
}