package pl.ds.bulma.components.models.columns;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Getter
@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class DefaultResponsiveColumnComponent {

    @Inject
    @Getter
    private ResponsiveColumnStyle mobileColumnStyle;

    @Inject
    @Getter
    private ResponsiveColumnStyle tabletColumnStyle;

    @Inject
    @Getter
    private ResponsiveColumnStyle desktopColumnStyle;

}
