package pl.ds.bulma.components.models;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class BreadcrumbElementComponent {
    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String url;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String label;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String icon;
}