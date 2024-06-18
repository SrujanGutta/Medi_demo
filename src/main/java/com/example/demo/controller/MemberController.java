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

    @Operation(summary = "Create a new member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Member created successfully"),
            @ApiResponse(responseCode = "409", description = "Member already exists with the given ID")
    })
    @PutMapping("/members/{id}")
    public ResponseEntity<Member> updateMember(@RequestBody Member newMember) {
        Long memberId = newMember.getId();
        if (memberRepository.existsById(memberId)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Member createdMember = memberRepository.save(newMember);
        return new ResponseEntity<>(createdMember, HttpStatus.CREATED);
    }
}
