package pl.ds.bulma.components.models.columns;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.models.annotations.Default;

import javax.inject.Inject;

@Getter
public class ResponsiveColumnStyle {

    @Inject
    @Getter
    private boolean isNarrowColumn;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String sizeType;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String size;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String offsetType;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String offset;

}
