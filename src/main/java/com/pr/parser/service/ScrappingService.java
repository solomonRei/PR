package com.pr.parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrappingService {

    private final WebClientService webClientService;
    public void scrapPage() {
        webClientService.fetchHtmlContent("")
                .subscribe(System.out::println);
    }
}
