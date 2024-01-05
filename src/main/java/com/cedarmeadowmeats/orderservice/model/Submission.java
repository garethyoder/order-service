package com.cedarmeadowmeats.orderservice.model;

public record Submission(String name, String email, String phone, String createdDate, String lastUpdatedDate,
                         Integer version) {
}
