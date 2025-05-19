package com.empik.complaints.controllers;

import com.empik.complaints.models.Complaint;
import com.empik.complaints.models.ComplaintRequest;
import com.empik.complaints.models.ComplaintResponse;
import com.empik.complaints.services.ComplaintService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/complaints")
public class ComplaintsController {

    private final ComplaintService complaintService;

    @GetMapping("")
    public List<ComplaintResponse> getComplaints() {
        var complaints = complaintService.getAll();
        return complaints.stream().map(complaint -> {
            var productComplaintId = complaint.getProductComplaintId();
            return new ComplaintResponse(
                    productComplaintId.getProductId(),
                    complaint.getContent(),
                    complaint.getDate(),
                    productComplaintId.getComplainant(),
                    complaint.getCountry(),
                    complaint.getComplaintCount()
            );
        }).toList();
    }

    @PostMapping("")
    public ResponseEntity<String> addComplaint(@RequestBody ComplaintRequest complaintBody, HttpServletRequest request) {
        var country = complaintService.getCountryByIp(request);
        var date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        var complaint = new Complaint(complaintBody.productId(), complaintBody.complainant(), date, complaintBody.content(), country);
        complaintService.save(complaint);
        return new ResponseEntity<>("Complaint successfully created.", HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}/{complainant}")
    public ResponseEntity<String> editComplaint(@PathVariable String productId, @PathVariable String complainant, @RequestBody ComplaintRequest complaintBody) {
        var content = complaintBody.content();
        complaintService.editContent(productId, complainant, content);

        return new ResponseEntity<>(String.format(
                "Complaint for product %s by %s updated. New content: %s",
                productId,
                complainant,
                content
            ), HttpStatus.OK
        );
    }
}
