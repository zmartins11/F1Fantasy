package com.example.f1.f1_service.dto;

import java.time.Instant;

public record NewsDto(String title,
                      String description,
                      String imageUrl,
                      String articleUrl,
                      Instant publishedAt) {
}
