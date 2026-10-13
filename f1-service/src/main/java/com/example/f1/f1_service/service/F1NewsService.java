package com.example.f1.f1_service.service;

import com.example.f1.f1_service.dto.NewsDto;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.rometools.rome.feed.synd.SyndEntry;
import org.jdom2.Element;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class F1NewsService {

    private static final String RSS_URL =
            "https://feeds.bbci.co.uk/sport/formula1/rss.xml";

    @Cacheable("f1News")
    public List<NewsDto> getLatestNews() throws Exception {

        log.info("Fetching F1 news from BBC RSS...");

        SyndFeedInput input = new SyndFeedInput();

        try (XmlReader reader = new XmlReader(
                URI.create(RSS_URL).toURL())) {

            SyndFeed feed = input.build(reader);

            return feed.getEntries().stream()
                    .limit(5)
                    .map(entry -> new NewsDto(
                            entry.getTitle(),
                            entry.getDescription() != null
                                    ? entry.getDescription().getValue()
                                    : null,
                            extractImageUrl(entry),
                            entry.getLink(),
                            entry.getPublishedDate() != null
                                    ? entry.getPublishedDate().toInstant()
                                    : null
                    ))
                    .toList();
        }
    }

    private String extractImageUrl(SyndEntry entry) {

        for (Element element : entry.getForeignMarkup()) {

            if ("thumbnail".equals(element.getName())
                    && "http://search.yahoo.com/mrss/"
                    .equals(element.getNamespaceURI())) {

                return element.getAttributeValue("url");
            }
        }

        return null;
    }



}
