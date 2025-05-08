package project.psa.dataserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.psa.dataserver.common.constant;
import project.psa.dataserver.model.ChatRequest;
import project.psa.dataserver.model.ChatResponse;
import project.psa.dataserver.service.ChatGPTService;
import project.psa.dataserver.service.GeminiService;

//@RestController
//@RequestMapping(constant.API.PREFIX_AUTH+"/chat")
public class ChatController {

//    @Autowired
//    private GeminiService geminiService;
//    @Autowired
//    private ChatGPTService chatGPTService;
//
//    @PostMapping("/gemini")
//    public ChatResponse chatGemini(@RequestBody ChatRequest request) {
//        String reply = geminiService.getGeminiResponse(request.getMessage());
//        return new ChatResponse(reply);
//    }
//    @PostMapping("/chatgpt")
//    public ChatResponse chatGpt(@RequestBody ChatRequest request) {
//        String reply = chatGPTService.getChatGptResponse(request.getMessage());
//        return new ChatResponse(reply);
//    }
}
