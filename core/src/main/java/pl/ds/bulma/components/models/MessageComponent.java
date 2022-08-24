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
public class MessageComponent {
    @Inject
    @Getter
    private String header;

    @Inject
    @Getter
    private String content;

    @Inject
    @Getter
    private String variant;

    @Inject
    @Getter
    private String size;

    @Inject
    @Getter
    @Default(booleanValues = false)
    private Boolean showButton;

    @Inject
    @Getter
    private String[] messageClasses;

    @Inject
    @Getter
    @Default(booleanValues = true)
    private Boolean showHeader;

    @PostConstruct
    private void init() {
        List<String> classes = new ArrayList<>();
        if (size != null) {
            classes.add(size);
        }
        if (variant != null) {
            classes.add(variant);
        }

        messageClasses = classes.toArray(new String[]{});
    }

}
