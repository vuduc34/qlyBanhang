package project.psa.dataserver.service;

import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private String GEMINI_URL;

    @PostConstruct
    public void init() {
        // Cấu hình URL API với API Key từ application.properties
        GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;
    }

    public String getGeminiResponse(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        // Xây dựng payload JSON
        JSONObject payload = new JSONObject();
        try {
            JSONArray partsArray = new JSONArray();
            JSONObject content = new JSONObject();
            content.put("text", prompt);
            partsArray.put(content);

            JSONObject candidates = new JSONObject();
            candidates.put("parts", partsArray);

            JSONArray contentsArray = new JSONArray();
            contentsArray.put(candidates);

            payload.put("contents", contentsArray);
        } catch (JSONException e) {
            // Xử lý lỗi JSON (nếu có)
            return "Error building JSON request: " + e.getMessage();
        }

        // Thiết lập headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo HttpEntity với payload và headers
        HttpEntity<String> entity = new HttpEntity<>(payload.toString(), headers);

        try {
            // Thực hiện POST request
            ResponseEntity<String> response = restTemplate.exchange(
                    GEMINI_URL, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                return "Error: Received non-OK response: " + response.getStatusCode();
            }

            // Xử lý response JSON
            JSONObject jsonResponse = new JSONObject(response.getBody());

            // Lấy nội dung từ response JSON
            String result = jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            return result;

        } catch (Exception e) {
            // Xử lý lỗi HTTP hoặc bất kỳ ngoại lệ nào
            return "Error during API call: " + e.getMessage();
        }
    }
}