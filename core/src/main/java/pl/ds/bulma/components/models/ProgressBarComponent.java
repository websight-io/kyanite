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
public class ProgressBarComponent {
    @Inject
    @Getter
    @Default(intValues = 50)
    private Integer value;

    @Inject
    @Getter
    private String variant;

    @Inject
    @Getter
    private String size;

    @Inject
    @Getter
    private boolean isIntermediate;

    @Inject
    @Getter
    private String[] progressBarClasses;

    @PostConstruct
    private void init() {
        List<String> classes = new ArrayList<>();
        if (size != null) {
            classes.add(size);
        }
        if (variant != null) {
            classes.add(variant);
        }

        progressBarClasses = classes.toArray(new String[]{});
    }
}
