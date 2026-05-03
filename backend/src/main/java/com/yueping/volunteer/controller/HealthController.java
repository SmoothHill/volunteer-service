package com.yueping.volunteer.controller;

import com.yueping.volunteer.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<?> health() {
        return ApiResponse.ok(Collections.singletonMap("status", "UP"));
    }
}

