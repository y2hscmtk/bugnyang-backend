package com.winner_cat.init.tag;

import com.winner_cat.domain.article.entity.Tag;
import com.winner_cat.domain.article.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//@Component
//@Order(1)
@RequiredArgsConstructor
public class TagDataLoader implements CommandLineRunner {
    private final TagRepository tagRepository;

    @Override
    public void run(String... args) throws Exception {
        createTag("JavaScript", "#F7DF1E");
        createTag("Python", "#306998");
        createTag("Java", "#5382A1");
        createTag("C#", "#239120");
        createTag("C/C++", "#00599C");
        createTag("Swift", "#FA7343");
        createTag("Kotlin", "#0095D5");
        createTag("TypeScript", "#3178C6");
        createTag("React", "#61DAFB");
        createTag("Angular", "#DD0031");
        createTag("Vue.js", "#4FC08D");
        createTag("Django", "#092E20");
        createTag("Flask", "#000000");
        createTag("Spring", "#6DB33F");
        createTag("Express", "#000000");
        createTag("NestJS", "#E0234E");
        createTag("iOS", "#A2AAAD");
        createTag("Android", "#3DDC84");
        createTag("React Native", "#137CBD");
        createTag("Flutter", "#02569B");
        createTag("SQL", "#00758F");
    }

    public Tag createTag(String tagName, String colorCode) {
        Tag tag = Tag.builder()
                .tagName(tagName)
                .colorCode(colorCode)
                .build();
        return tagRepository.save(tag);
    }
}