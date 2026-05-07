package com.hnh.controller.ai;

import com.hnh.service.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin("*")
public class AiController {

    @Autowired
    private OpenAiService openAiService;

    @PostMapping("/parse-product")
    public Map<String, String> parseProduct(@RequestBody Map<String, String> request) {
        String rawText = request.get("rawText");
        String systemPrompt = "Bạn là chuyên gia phân tích dữ liệu sản phẩm. Hãy bóc tách JSON:\n"
                + "{\n"
                + "  \"name\": \"Tên sản phẩm\",\n"
                + "  \"slug\": \"duong-dan-than-thien\",\n"
                + "  \"shortDescription\": \"mô tả ngắn\",\n"
                + "  \"description\": \"mô tả chi tiết\",\n"
                + "  \"price\": 100000,\n"
                + "  \"brand\": \"Tác giả/Thương hiệu\",\n"
                + "  \"category\": \"Thể loại\",\n"
                + "  \"code\": \"Mã sản phẩm\"\n"
                + "}\n"
                + "Chỉ trả về JSON.";
        return aiResponse(rawText, systemPrompt);
    }

    @PostMapping("/parse-category")
    public Map<String, String> parseCategory(@RequestBody Map<String, String> request) {
        String rawText = request.get("rawText");
        String systemPrompt = "Bạn là chuyên gia phân tích danh mục. Hãy bóc tách JSON:\n"
                + "{\n"
                + "  \"name\": \"Tên danh mục\",\n"
                + "  \"slug\": \"slug-danh-muc\"\n"
                + "}\n"
                + "Chỉ trả về JSON.";
        return aiResponse(rawText, systemPrompt);
    }

    @PostMapping("/parse-brand")
    public Map<String, String> parseBrand(@RequestBody Map<String, String> request) {
        String rawText = request.get("rawText");
        String systemPrompt = "Bạn là chuyên gia phân tích tác giả/thương hiệu. Hãy bóc tách JSON:\n"
                + "{\n"
                + "  \"name\": \"Tên\",\n"
                + "  \"code\": \"Mã\",\n"
                + "  \"description\": \"Mô tả\"\n"
                + "}\n"
                + "Chỉ trả về JSON.";
        return aiResponse(rawText, systemPrompt);
    }

    @PostMapping("/parse-supplier")
    public Map<String, String> parseSupplier(@RequestBody Map<String, String> request) {
        String rawText = request.get("rawText");
        String systemPrompt = "Bạn là chuyên gia phân tích nhà cung cấp/NXB. Hãy bóc tách JSON đầy đủ:\n"
                + "{\n"
                + "  \"displayName\": \"Tên hiển thị\",\n"
                + "  \"code\": \"Mã NXB\",\n"
                + "  \"contactFullname\": \"Tên người liên hệ\",\n"
                + "  \"contactEmail\": \"Email người liên hệ\",\n"
                + "  \"contactPhone\": \"SĐT người liên hệ\",\n"
                + "  \"companyName\": \"Tên công ty\",\n"
                + "  \"taxCode\": \"Mã số thuế\",\n"
                + "  \"email\": \"Email công ty\",\n"
                + "  \"phone\": \"SĐT công ty\",\n"
                + "  \"fax\": \"Số Fax\",\n"
                + "  \"website\": \"Website\",\n"
                + "  \"addressLine\": \"Địa chỉ chi tiết\",\n"
                + "  \"description\": \"Mô tả\",\n"
                + "  \"note\": \"Ghi chú\"\n"
                + "}\n"
                + "Chỉ trả về JSON.";
        return aiResponse(rawText, systemPrompt);
    }

    @PostMapping("/parse-unit")
    public Map<String, String> parseUnit(@RequestBody Map<String, String> request) {
        String rawText = request.get("rawText");
        String systemPrompt = "Bóc tách JSON đơn vị tính:\n"
                + "{\n"
                + "  \"name\": \"Tên đơn vị (Quyển, Bộ...)\"\n"
                + "}\n"
                + "Chỉ trả về JSON.";
        return aiResponse(rawText, systemPrompt);
    }

    @PostMapping("/parse-tag")
    public Map<String, String> parseTag(@RequestBody Map<String, String> request) {
        String rawText = request.get("rawText");
        String systemPrompt = "Bóc tách JSON Tag:\n"
                + "{\n"
                + "  \"name\": \"Tên tag\",\n"
                + "  \"slug\": \"slug-tag\"\n"
                + "}\n"
                + "Chỉ trả về JSON.";
        return aiResponse(rawText, systemPrompt);
    }

    @PostMapping("/parse-property")
    public Map<String, String> parseProperty(@RequestBody Map<String, String> request) {
        String rawText = request.get("rawText");
        String systemPrompt = "Bóc tách JSON thuộc tính:\n"
                + "{\n"
                + "  \"name\": \"Tên thuộc tính\",\n"
                + "  \"code\": \"Mã\",\n"
                + "  \"description\": \"Mô tả\"\n"
                + "}\n"
                + "Chỉ trả về JSON.";
        return aiResponse(rawText, systemPrompt);
    }

    @PostMapping("/parse-specification")
    public Map<String, String> parseSpecification(@RequestBody Map<String, String> request) {
        String rawText = request.get("rawText");
        String systemPrompt = "Bóc tách JSON thông số:\n"
                + "{\n"
                + "  \"name\": \"Tên thông số\",\n"
                + "  \"code\": \"Mã\",\n"
                + "  \"description\": \"Mô tả\"\n"
                + "}\n"
                + "Chỉ trả về JSON.";
        return aiResponse(rawText, systemPrompt);
    }

    private Map<String, String> aiResponse(String rawText, String systemPrompt) {
        String aiResponse = openAiService.chatWithGpt(rawText, systemPrompt);
        Map<String, String> result = new HashMap<>();
        result.put("raw", aiResponse);
        return result;
    }
}
