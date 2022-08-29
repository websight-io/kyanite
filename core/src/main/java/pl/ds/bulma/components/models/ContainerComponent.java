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
public class ContainerComponent {

    @Inject
    @Getter
    private boolean isWideScreen;

    @Inject
    @Getter
    private String[] containerClasses;

    @PostConstruct
    private void init() {
        List<String> classes = new ArrayList<>();
        if (isWideScreen) {
            classes.add("is-widescreen");
        }

        containerClasses = classes.toArray(new String[]{});
    }
}
