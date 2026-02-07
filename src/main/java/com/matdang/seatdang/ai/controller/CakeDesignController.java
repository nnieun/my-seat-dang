package com.matdang.seatdang.ai.controller;

import com.matdang.seatdang.ai.dto.ErrorResponseDto;
import com.matdang.seatdang.ai.dto.GeneratedImageResponseDto;
import com.matdang.seatdang.ai.entity.GeneratedImageUrl;
import com.matdang.seatdang.ai.repository.GeneratedImageUrlRepository;
import com.matdang.seatdang.ai.service.CakeDesignService;
import com.matdang.seatdang.auth.service.AuthService;
import com.matdang.seatdang.member.entity.Customer;
import com.matdang.seatdang.member.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CakeDesignController {
    private final CakeDesignService cakeDesignService;
    private final GeneratedImageUrlRepository generatedImageUrlRepository;
    private final AuthService authService;

    @PostMapping("/ai/generate")
    @ResponseBody
    public ResponseEntity<?> generateImage(@RequestParam("cakeDescription") String cakeDescription) throws IOException, InterruptedException {
        Long customerId = authService.getAuthenticatedMember().getMemberId();
        GeneratedImageResponseDto response = cakeDesignService.generateCakeImage(customerId,cakeDescription);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/ai-result")
    @ResponseBody
    public Map<String, Object> showResult(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int size) {

        // 현재 로그인된 고객 정보
        Long customerId = authService.getAuthenticatedMember().getMemberId();

        // 페이지 요청 객체 생성
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        // 페이징 처리된 이미지 목록 조회
        Page<GeneratedImageUrl> imagePage = generatedImageUrlRepository.findPageByCustomerId(customerId, pageable);

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("imageList", imagePage.getContent());
        response.put("currentPage", page);
        response.put("totalPages", imagePage.getTotalPages());

        return response;
    }

    @GetMapping("/ai-result")
    public String showResult(Model model){

        // 현재 로그인된 고객 정보
        Long customerId = authService.getAuthenticatedMember().getMemberId();
        // 고객이 생성한 모든 이미지 조회
        List<GeneratedImageUrl> imageList = generatedImageUrlRepository.findAllByCustomerId(customerId);
        // 모델에 이미지 목록 추가
        model.addAttribute("imageList", imageList);

        return "ai/ai-result";
    }

}