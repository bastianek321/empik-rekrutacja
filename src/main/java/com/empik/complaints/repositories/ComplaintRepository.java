package com.empik.complaints.repositories;

import com.empik.complaints.models.Complaint;
import com.empik.complaints.models.ProductComplaintId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, ProductComplaintId> {

}
