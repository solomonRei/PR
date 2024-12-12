package com.pr.parser.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "raft")
@Primary
public class RaftProperties {
    private String nodeId;
    private String clusterNodes;
    private String udpPort;
}
