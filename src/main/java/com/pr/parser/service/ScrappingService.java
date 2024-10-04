package com.pr.parser.service;

import com.pr.parser.model.Product;
import com.pr.parser.rest.ScrappingProperties;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScrappingService {

    private final WebClientService webClientService;

    private final ScrappingProperties scrappingProperties;

    public List<Product> parseHtmlForProducts(String html) {
        List<Product> products = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements productElements = document.select(".js-itemsList-item");

        for (Element productElement : productElements) {
            var productName = productElement.select("meta[itemprop=name]").attr("content");
            var productPrice = productElement.select(".card-price_curr").text();
            var productLink = scrappingProperties.getBaseUrl() + productElement.select("a[itemprop=url]").attr("href");

            var product = new Product();
            product.setName(productName);
            product.setPrice(productPrice);
            product.setLink(productLink);

            products.add(product);
        }
        return products;
    }

    public Mono<Map<String, String>> scrapeAdditionalData(String productLink) {
        return webClientService.fetchHtmlContent(productLink)
                .map(html -> parseCharacteristics(Jsoup.parse(html)));
    }

    private Map<String, String> parseCharacteristics(Document document) {
        return document.select("div.tab-pane-inner")
                .select("table")
                .select("tr")
                .stream()
                .map(row -> row.select("td"))
                .filter(columns -> columns.size() == 2)
                .collect(
                        HashMap::new,
                        (map, columns) -> map.put(columns.get(0).text(), columns.get(1).text()),
                        HashMap::putAll
                );
    }


    public void scrapPage() {
        webClientService.fetchHtmlContent("/ru/catalog/electronics/telephones/mobile/?page_=page_3").subscribe(htmlContent -> {
            List<Product> products = parseHtmlForProducts(htmlContent);

            for (Product product : products) {
                scrapeAdditionalData(product.getLink()).subscribe(characteristics -> {
                    product.setCharacteristics(characteristics);
                    System.out.println(product);
                });
            }
        });
    }
}
