package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.http.*;
import java.util.List;
import org.json.JSONObject;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class SummaryController {

    private final TodoService todoService;

    @Value("${cohere.api.key}")
    private String cohereApiKey;

    @Value("${slack.webhook.url}")
    private String slackWebhookUrl;

    public SummaryController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/summarize")
    public String summarizeAndSendToSlack() {
        try {
            List<Todo> todos = todoService.getPendingTodos();
            if (todos == null || todos.isEmpty()) {
                return "No todos to summarize.";
            }

            // Build the text prompt
            StringBuilder prompt = new StringBuilder("Summarize the following tasks:\n");
            for (Todo todo : todos) {
                prompt.append("- ").append(todo.getTitle()).append("\n");
            }

            // Construct Cohere API request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "command-light"); // free-tier summarization model
            requestBody.put("prompt", prompt.toString());
            requestBody.put("max_tokens", 100);
            requestBody.put("temperature", 0.3);

            // Send request to Cohere
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.cohere.ai/v1/generate"))
                    .header("Authorization", "Bearer " + cohereApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());

            if (!jsonResponse.has("generations")) {
                return "Cohere API Error: " + jsonResponse.toString();
            }

            String content = jsonResponse.getJSONArray("generations")
                                         .getJSONObject(0)
                                         .getString("text");

            // Post the summary to Slack
            JSONObject slackPayload = new JSONObject();
            slackPayload.put("text", content.trim());

            HttpRequest slackRequest = HttpRequest.newBuilder()
                    .uri(URI.create(slackWebhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(slackPayload.toString()))
                    .build();

            client.send(slackRequest, HttpResponse.BodyHandlers.ofString());

            return "Summary sent to Slack!";
        } catch (Exception e) {
            return "Failed: " + e.getMessage();
        }
    }
}
