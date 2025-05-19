package com.empik.complaints.services;

import com.empik.complaints.models.Complaint;
import com.empik.complaints.models.ProductComplaintId;
import com.empik.complaints.repositories.ComplaintRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public void save(Complaint newComplaint) {
        var complaint = complaintRepository.findById(newComplaint.getProductComplaintId());
        if(complaint.isEmpty()){
            complaintRepository.save(newComplaint);
        } else {
            incrementCountAndSave(newComplaint);
        }
    }

    public void editContent(String productId, String complainant, String content){
        var productComplaintId = new ProductComplaintId(productId, complainant);
        var complaint = complaintRepository.findById(productComplaintId);
        if (complaint.isEmpty()) throw new RuntimeException(String.format("No complaint for product %s by complainant %s found!", productId, complainant));
        var editedComplaint = complaint.get();
        editedComplaint.setContent(content);
        complaintRepository.save(editedComplaint);
    }

    public List<Complaint> getAll() {
        return complaintRepository.findAll();
    }

    public void incrementCountAndSave(Complaint complaint) {
        var newCount = incrementCount(complaint);
        if (newCount == 0) {
            throw new RuntimeException("Item does not exist yet!");
        }
        complaint.setComplaintCount(newCount);
        complaintRepository.save(complaint);
    }

    // IP address has to be external
    public String getCountryByIp(HttpServletRequest request) {
        var ipAddress = getClientIp(request);
        try {
            URL url = new URL("http://ip-api.com/json/" + ipAddress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.getResponseMessage();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = in.lines().collect(Collectors.joining());
            in.close();
            ObjectNode json = new ObjectMapper().readValue(response, ObjectNode.class);
            return json.get("country").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error getting location by ip address", e);
        }
    }

    private int incrementCount(Complaint complaint) {
        var existingComplaint = complaintRepository.findById(complaint.getProductComplaintId());
        return existingComplaint.map(count -> count.getComplaintCount() + 1).orElse(0);
    }

    private String getClientIp(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");
        if (header != null && !header.isEmpty()) {
            return header.split(",")[0]; // first IP in case of proxies
        }
        return request.getRemoteAddr();
    }
}
