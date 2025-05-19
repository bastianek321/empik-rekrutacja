package com.empik.complaints.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
public class ProductComplaintId implements Serializable {

    private String productId;
    private String complainant;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof ProductComplaintId)) return false;
        return Objects.equals(productId, ((ProductComplaintId) o).productId) &&
                Objects.equals(complainant, ((ProductComplaintId) o).complainant);
    }

    @Override
    public int hashCode(){
        return Objects.hash(productId, complainant);
    }
}
