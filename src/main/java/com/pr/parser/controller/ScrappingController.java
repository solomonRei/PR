package com.pr.parser.controller;

import com.pr.parser.service.ScrappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScrappingController {

    private final ScrappingService scrappingService;

    @GetMapping("/parse")
    public String handleRequest() {
       return scrappingService.scrapPage();
    }

}
