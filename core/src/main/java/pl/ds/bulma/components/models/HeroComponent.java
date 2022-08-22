package pl.ds.bulma.components.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeroComponent {
    @Inject
    @Getter
    @Default(values = "Default title")
    private String title;

    @Inject
    @Getter
    private String subTitle;

    @Inject
    @Getter
    private String variant;

    @Inject
    @Getter
    private String size;

    @Inject
    @Getter
    private String[] heroClasses;

    @PostConstruct
    private void init() {
        List<String> classes = new ArrayList<>();
        if (size != null) {
            classes.add(size);
        }
        if (variant != null) {
            classes.add(variant);
        }

        heroClasses = classes.toArray(new String[]{});
    }
}
