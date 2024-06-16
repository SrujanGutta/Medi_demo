package com.example.demo.controller;

import com.example.demo.entity.Claim;
import com.example.demo.entity.Member;
import com.example.demo.repository.ClaimRepository;
import com.example.demo.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Operation(summary = "Get members with claims after a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved members"),
            @ApiResponse(responseCode = "204", description = "No members found")
    })
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getMembersWithClaimsAfter() {
        List<Claim> claimsAfterDate = claimRepository.findByClaimDateAfter();
        if (claimsAfterDate.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<Member> members = claimsAfterDate.stream()
                .map(Claim::getMember)
                .distinct()
                .collect(Collectors.toList());
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Member updated successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @PutMapping("/members/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member newMember) {
        return memberRepository.findById(id)
                .map(member -> {
                    member.setFirst_name(newMember.getFirst_name());
                    member.setLast_name(newMember.getLast_name());
                    member.setPhone_number(newMember.getPhone_number());
                    member.setDob(newMember.getDob());
                    member.setDemographics(newMember.getDemographics());
                    Member updatedMember = memberRepository.save(member);
                    return new ResponseEntity<>(updatedMember, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
