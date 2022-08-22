package pl.ds.bulma.components.models;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

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
}
