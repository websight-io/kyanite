package pl.ds.bulma.components.models;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TitleComponent {

    @Inject
    @Getter
    @Default(values = "Title")
    private String text;

    @Inject
    @Getter
    @Default(values = "title")
    private String type;

    @Inject
    @Getter
    @Default(values = "h1")
    private String element;

    @Inject
    @Getter
    @Default(booleanValues = false)
    private boolean isSpaced;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String size;

    @Inject
    @Getter
    private String[] titleClasses;

    @PostConstruct
    private void init() {
        List<String> classes = new ArrayList<>();
        classes.add(type);
        if (StringUtils.isNotBlank(size)) {
            classes.add(size);
        }
        if (isSpaced) {
            classes.add("is-spaced");
        }
        titleClasses = classes.toArray(new String[]{});
    }
}
