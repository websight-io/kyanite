package pl.ds.bulma.components.models;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class CardComponent {

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String header;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String title;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String subtitle;

    @Inject
    @Getter
    private ImageComponent thumbnail;

    @Inject
    @Getter
    private ImageComponent image;

    @Inject
    @Getter
    private List<URLComponent> urls;

    @Inject
    @Getter
    @Default(values = "Card content")
    private String content;

    @Inject
    @Getter
    private String date;
    @PostConstruct
    private void init() {
        formatArticleDate();
    }

    public void formatArticleDate() {
        if(date ==null){
            return;
        }
        LocalDate localDate = LocalDate.parse(date);
        date = localDate.format(DateTimeFormatter.ofPattern("MMM d, YYYY"));
    }


}
