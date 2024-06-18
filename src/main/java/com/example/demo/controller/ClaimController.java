package com.example.demo.controller;
import com.example.demo.dto.ClaimRequestDto;
import com.example.demo.entity.Claim;
import com.example.demo.repository.ClaimRepository;
import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
public class ClaimController {
    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Operation(summary = "Get submitted claims grouped by member, medication, and pharmacy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved claims",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Claim.class)) }),
            @ApiResponse(responseCode = "204", description = "No claims found")
    })
    @GetMapping("/submitted")
    public ResponseEntity<List<Claim>> getGroupedSubmittedClaims() {
        List<Claim> claims = claimRepository.findGroupedSubmittedClaims();
        if (claims.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(claims, HttpStatus.OK);
    }

    @Operation(summary = "Add a new claim to an existing member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Claim added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @PostMapping("/claims")
    public ResponseEntity<String> addClaimToMember(@RequestBody ClaimRequestDto claimRequestDto) {
        Long memberId = claimRequestDto.getMemberId();
        String status = claimRequestDto.getStatus();
        if (!memberRepository.existsById(memberId)) {
            return new ResponseEntity<>("Member not found", HttpStatus.NOT_FOUND);
        }
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return new ResponseEntity<>("Member not found", HttpStatus.NOT_FOUND);
        }
        if (status.equals("SUBMITTED") || status.equals("APPROVED") || status.equals("DENIED")) {
            Claim claim = new Claim();
            claim.setMember(member);
            claim.setClaim_date(claimRequestDto.getClaimDate());
            claim.setStatus(claimRequestDto.getStatus());
            claim.setMedication(claimRequestDto.getMedication());
            claim.setPharmacy_name(claimRequestDto.getPharmacyName());
            claimRepository.save(claim);
            return new ResponseEntity<>("Claim added successfully", HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>("Claims can only have status 'SUBMITTED', 'APPROVED', or 'DENIED'", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update the status of a claim")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Claim status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Claim not found")
    })
    @PostMapping("/claims/update-status/{claimId}")
    public ResponseEntity<String> updateClaimStatus(@PathVariable Long claimId, @RequestParam String newStatus) {
        Claim claim = claimRepository.findById(claimId).orElse(null);
        if (claim == null) {
            return new ResponseEntity<>("Claim not found", HttpStatus.NOT_FOUND);
        }

        if (!newStatus.equalsIgnoreCase("APPROVED") && !newStatus.equalsIgnoreCase("DENIED")) {
            return new ResponseEntity<>("Status must be 'APPROVED' or 'DENIED'", HttpStatus.BAD_REQUEST);
        }
        if (!claim.getStatus().equalsIgnoreCase("SUBMITTED")) {
            return new ResponseEntity<>("Only claims with 'SUBMITTED' status can be updated", HttpStatus.BAD_REQUEST);
        }

        claim.setStatus(newStatus.toUpperCase());
        claimRepository.save(claim);
        return new ResponseEntity<>("Claim status updated successfully", HttpStatus.OK);
    }
    @Operation(summary = "Delete a claim by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Claim deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Claim not found")
    })
    @DeleteMapping("/claims/{id}")
    public ResponseEntity<String> deleteClaim(@PathVariable Long id) {
        return claimRepository.findById(id)
                .map(claim -> {
                    claimRepository.delete(claim);
                    return new ResponseEntity<>("Claim deleted successfully", HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>("Claim not found", HttpStatus.NOT_FOUND));
    }
}
