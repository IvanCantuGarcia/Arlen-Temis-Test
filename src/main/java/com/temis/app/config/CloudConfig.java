package com.temis.app.config;

import com.temis.app.client.ChatAIClient;
import com.temis.app.client.CloudStorageClient;
import com.temis.app.client.DocumentClassifierClient;
import com.temis.app.client.VertexAIClient;

import java.io.IOException;

import com.temis.app.config.properties.CloudConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CloudConfig {

    private final CloudConfigProperties cloudConfigProperties;

    @Autowired
    public CloudConfig(CloudConfigProperties cloudConfigProperties) {
        this.cloudConfigProperties = cloudConfigProperties;
    }

    @Bean
    public VertexAIClient vertexAIClient() throws IOException {
        return new VertexAIClient(
                cloudConfigProperties.getProjectId(),
                cloudConfigProperties.getLocation(),
                cloudConfigProperties.getVertexai().getModelName()
        );
    }

    @Bean
    public CloudStorageClient cloudStorageClient() {
        return new CloudStorageClient(
                cloudConfigProperties.getProjectId(),
                cloudConfigProperties.getStorage().getBucketName()
        );
    }

    @Bean
    @DependsOn({"cloudStorageClient"})
    public ChatAIClient chatAIClient(@Autowired CloudStorageClient cloudStorageClient) throws IOException {
        return new ChatAIClient(
                cloudConfigProperties.getProjectId(),
                cloudConfigProperties.getLocation(),
                cloudConfigProperties.getVertexai().getModelName(),
                cloudStorageClient
        );
    }

    @Bean
    @DependsOn({"cloudStorageClient"})
    public DocumentClassifierClient documentClassifierClient(@Autowired CloudStorageClient cloudStorageClient) throws IOException {
        return new DocumentClassifierClient(
                cloudConfigProperties.getProjectId(),
                cloudConfigProperties.getLocation(),
                cloudConfigProperties.getVertexai().getModelName(),
                cloudStorageClient
        );
    }
}