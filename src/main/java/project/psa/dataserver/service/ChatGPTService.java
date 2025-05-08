package project.psa.dataserver.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class ChatGPTService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public String getChatGptResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        String requestBody = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}] }";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    OPENAI_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Xử lý response từ OpenAI API
                String responseBody = response.getBody();
                // Trích xuất câu trả lời từ response (thường là JSON)
                return responseBody; // Trả về câu trả lời từ API
            } else {
                return "Error: " + response.getStatusCode();
            }
        } catch (Exception e) {
            return "Error during API call: " + e.getMessage();
        }
    }
}
