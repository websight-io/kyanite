package pl.ds.bulma.components.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NotificationComponent {
    @Inject
    @Getter
    private String content;

    @Inject
    @Getter
    private String variant;

    @Inject
    @Getter
    private String isLight;

    @Inject
    @Getter
    private String button;

    @Inject
    @Getter
    private String[] notificationClasses;

    @PostConstruct
    private void init() {
        List<String> classes = new ArrayList<>();
        if ("true".equals(isLight)) {
            classes.add("is-light");
        }
        if (variant != null) {
            classes.add(variant);
        }

        notificationClasses = classes.toArray(new String[]{});
    }

}
