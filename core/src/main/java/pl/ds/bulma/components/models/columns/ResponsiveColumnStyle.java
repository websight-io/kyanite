package pl.ds.bulma.components.models.columns;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Getter
@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ResponsiveColumnStyle {

    @Inject
    private boolean isNormalColumn;

    @Inject
    @Default(values = StringUtils.EMPTY)
    private String sizeType;

    @Inject
    @Default(values = StringUtils.EMPTY)
    private String size;

    @Inject
    @Default(values = StringUtils.EMPTY)
    private String offsetType;

    @Inject
    @Default(values = StringUtils.EMPTY)
    private String offset;

}
