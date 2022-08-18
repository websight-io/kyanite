package pl.ds.bulma.components.models;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ButtonsListComponent {

    @Inject
    @Default(values = "false")
    @Getter
    private String hasAddons;

    @Inject
    @Default(values = "is-left")
    @Getter
    private String alignment;
}
