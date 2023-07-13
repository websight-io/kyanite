package pl.ds.kyanite.components.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BlogPostTableOfContentsItemComponent {

    private String title;
    private String url;
    private String headingLevel;
}
