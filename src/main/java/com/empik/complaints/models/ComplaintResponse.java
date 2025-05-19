package com.empik.complaints.models;

public record ComplaintResponse(String productId, String content, String date, String complainant, String country,
                                int count) {
}
