package com.workintech.ecommerce.dto;

public record CategoryResponseDto(Long id,
                                  String code,
                                  String title,
                                  String image,
                                  int rating,
                                  char gender) {
}
