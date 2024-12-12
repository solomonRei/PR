package com.pr.parser.api.controller;

import com.pr.parser.service.LeaderElectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/election")
@RequiredArgsConstructor
public class ElectionController {

    private final LeaderElectionService leaderElectionService;

    @PostMapping("/start")
    public ResponseEntity<String> startElection() {
        leaderElectionService.startElection();
        return ResponseEntity.ok("Election process started.");
    }

    @GetMapping("/role")
    public ResponseEntity<String> getNodeRole() {
        return ResponseEntity.ok("Current role: " + leaderElectionService.getCurrentRole());
    }


    @GetMapping("/leader")
    public ResponseEntity<String> getCurrentLeader() {
        String leaderId = leaderElectionService.getLeaderId();
        return ResponseEntity.ok(leaderId != null ? "Current leader: " + leaderId : "No leader elected.");
    }
}
