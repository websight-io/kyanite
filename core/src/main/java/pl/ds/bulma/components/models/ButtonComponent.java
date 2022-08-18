package pl.ds.bulma.components.models;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ButtonComponent {

    @Inject
    @Getter
    @Default(values = "Label")
    private String label;

    @Inject
    @Getter
    @Default(values = "button")
    private String type;

    @Inject
    @Getter
    @Default(values = "is-primary")
    private String variant;

    @Inject
    @Getter
    @Default(values = "is-normal")
    private String size;

    @Inject
    @Default(values = "false")
    private String isLight;

    @Inject
    @Default(values = "false")
    private String isOutlined;

    @Inject
    @Default(values = "false")
    private String isRounded;

    @Inject
    @Default(values = "false")
    private String isFullWidth;

    @Inject
    @Getter
    @Default(values = "false")
    private String isDisabled;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String url;

    @Inject
    @Getter
    @Default(values = "false")
    private String openInNewTab;

    @Inject
    @Getter
    private String[] buttonClasses;

    @PostConstruct
    private void init() {
        List<String> classes = new ArrayList<>();
        classes.add(size);
        classes.add(variant);
        if (Boolean.parseBoolean(isLight)) {
            classes.add("is-light");
        }
        if (Boolean.parseBoolean(isOutlined)) {
            classes.add("is-outlined");
        }
        if (Boolean.parseBoolean(isRounded)) {
            classes.add("is-rounded");
        }
        if (Boolean.parseBoolean(isFullWidth)) {
            classes.add("is-fullwidth");
        }
        buttonClasses = classes.toArray(new String[]{});
    }
}
