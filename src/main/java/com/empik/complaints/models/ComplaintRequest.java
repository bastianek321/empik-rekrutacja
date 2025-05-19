package com.empik.complaints.models;

public record ComplaintRequest(
        String productId,
        String content,
        String complainant)
{}
