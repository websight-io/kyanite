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
public class PricingTableComponent {
    @Inject
    @Getter
    @Default(booleanValues = false)
    private boolean isComparative;

    @Inject
    @Getter
    @Default(booleanValues = false)
    private boolean isHorizontal;

    @Inject
    @Getter
    private String[] pricingClasses;

    @PostConstruct
    private void init() {
        List<String> classes = new ArrayList<>();
        if (isComparative) {
            classes.add("is-comparative");
        }
        if (isHorizontal) {
            classes.add("is-horizontal");
        }

        pricingClasses = classes.toArray(new String[]{});
    }
}
