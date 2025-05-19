package com.empik.complaints.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Complaint  {

    @EmbeddedId
    private ProductComplaintId productComplaintId;
    @Setter
    private String content;
    private String date;
    private String country;
    @Setter
    private int complaintCount;

    public Complaint(String productId, String complainant, String content, String date, String country){
        this(new ProductComplaintId(productId, complainant), content, date , country, 1);
    }

}
