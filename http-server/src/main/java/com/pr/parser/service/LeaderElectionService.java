package com.pr.parser.service;

import com.pr.parser.config.RaftProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LeaderElectionService {

    private static final int ELECTION_TIMEOUT_MIN = 150;
    private static final int ELECTION_TIMEOUT_MAX = 300;
    private static final int HEARTBEAT_INTERVAL = 100;
    private static final Logger log = LoggerFactory.getLogger(LeaderElectionService.class);

    private enum Role {FOLLOWER, CANDIDATE, LEADER}
    private final Object voteLock = new Object();
    private int votesReceived = 0;

    private volatile Role currentRole = Role.FOLLOWER;
    private String nodeId;
    private int udpPort;
    private List<String> clusterNodes;
    private String leaderId = null;

    private DatagramSocket udpSocket;
    private final RaftProperties raftProperties;


    @PostConstruct
    public void init() {
        this.nodeId = raftProperties.getNodeId();
        this.udpPort = Integer.parseInt(raftProperties.getUdpPort());
        this.clusterNodes = Arrays.asList(raftProperties.getClusterNodes().split(","));

        System.out.println("Node ID: " + nodeId);
        System.out.println("UDP Port: " + udpPort);
        System.out.println("Cluster nodes: " + clusterNodes);

        System.out.println("Starting leader election service for node: " + nodeId);
        Executors.newSingleThreadExecutor().submit(this::startUdpListener);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::sendHeartbeatsOrRunElection, 0, HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public void startElection() {
        resetElectionState();
        startElectionIfTimeout();
    }

    private synchronized void resetElectionState() {
        currentRole = Role.FOLLOWER;
        leaderId = null;
        votesReceived = 0;
        System.out.println("Election state has been reset. Node is now a FOLLOWER.");
    }

    private void startUdpListener() {
        try {
            udpSocket = new DatagramSocket(udpPort);
            log.info("UDP Listener started on port {}", udpPort);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                udpSocket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                log.info("Received UDP message: '{}' from {}", message, packet.getAddress());
                handleUdpMessage(message);
            }
        } catch (Exception e) {
            log.error("Error in UDP Listener: {}", e.getMessage(), e);
        }
    }


    private void handleUdpMessage(String message) {
        String[] parts = message.split(":");
        String type = parts[0];
        String senderId = parts[1];

        System.out.println("Received UDP message: " + message);

        switch (type) {
            case "HEARTBEAT":
                handleHeartbeat(senderId);
                break;
            case "VOTE_REQUEST":
                handleVoteRequest(senderId);
                break;
            case "VOTE_RESPONSE":
                handleVoteResponse(senderId);
                break;
        }
    }

    private synchronized void handleHeartbeat(String leaderId) {
        System.out.println("Received heartbeat from leader: " + leaderId);
        if (!leaderId.equals(this.nodeId)) {
            this.currentRole = Role.FOLLOWER;
            this.leaderId = leaderId;
            System.out.println("Received heartbeat from leader: " + leaderId);
        }
    }

    private synchronized void handleVoteRequest(String candidateId) {
        System.out.println("Received vote request from candidate: " + candidateId);
        if (this.currentRole == Role.FOLLOWER || this.leaderId == null) {
            String destination = resolveDestination(candidateId);
            if (destination != null) {
                sendMessage("VOTE_RESPONSE:" + nodeId, destination);
                System.out.println("Voted for candidate: " + candidateId);
            } else {
                log.error("Cannot resolve destination for candidateId: {}", candidateId);
            }
        }
    }


    private synchronized void handleVoteResponse(String voterId) {
        System.out.println("Received vote from: " + voterId);

        synchronized (voteLock) {
            votesReceived++;

            int majority = (clusterNodes.size() / 2) + 1;
            if (votesReceived >= majority) {
                currentRole = Role.LEADER;
                leaderId = nodeId;
                System.out.println("Node " + nodeId + " is now the LEADER with " + votesReceived + " votes!");
                notifyIntermediaryServer(nodeId);
                sendHeartbeats();
            } else {
                System.out.println("Node " + nodeId + " has received " + votesReceived + " votes so far.");
            }
        }
    }

    private void sendHeartbeats() {
        for (String node : clusterNodes) {
            if (!node.contains(":" + udpPort)) {
                sendMessage("HEARTBEAT:" + nodeId, node);
            }
        }
    }


    private String resolveDestination(String candidateId) {
        for (String node : clusterNodes) {
            if (node.startsWith("http-server-" + candidateId + ":")) {
                return node;
            }
        }
        return null;
    }


    private void sendHeartbeatsOrRunElection() {
        if (currentRole == Role.LEADER) {
            for (String node : clusterNodes) {
                if (!node.contains(":" + udpPort)) {
                    sendMessage("HEARTBEAT:" + nodeId, node);
                }
            }
        } else {
            startElectionIfTimeout();
        }
    }

    public void startElectionIfTimeout() {
        try {
            Thread.sleep(new Random().nextInt(ELECTION_TIMEOUT_MAX - ELECTION_TIMEOUT_MIN) + ELECTION_TIMEOUT_MIN);
            synchronized (voteLock) {
                votesReceived = 0;
            }
            currentRole = Role.CANDIDATE;
            leaderId = null;
            System.out.println("Starting election...");

            for (String node : clusterNodes) {
                if (!node.contains(":" + udpPort)) {
                    sendMessage("VOTE_REQUEST:" + nodeId, node);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void sendMessage(String message, String destination) {
        log.info("Sending message '{}' to {}", message, destination);
        try {
            String[] hostAndPort = destination.split(":");
            if (hostAndPort.length != 2) {
                throw new IllegalArgumentException("Invalid destination format: " + destination);
            }
            InetAddress address = InetAddress.getByName(hostAndPort[0]);
            int port = Integer.parseInt(hostAndPort[1]);

            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);

            udpSocket.send(packet);
        } catch (Exception e) {
            log.error("Error sending UDP message to {}: {}", destination, e.getMessage(), e);
        }
    }


    private void notifyIntermediaryServer(String leaderId) {
        String intermediaryUrl = "http://proxy-server:9090/leader/update";
        WebClient.create()
                .post()
                .uri(intermediaryUrl)
                .bodyValue(Map.of("leaderId", leaderId))
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(response -> System.out.println("Updated intermediary server with leader: " + leaderId))
                .doOnError(error -> System.err.println("Failed to update intermediary server: " + error.getMessage()))
                .subscribe();
    }

    public String getLeaderId() {
        return leaderId;
    }

    public String getCurrentRole() {
        return currentRole.name();
    }

}

