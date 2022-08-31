package pl.ds.bulma.components.models.columns;


import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ColumnComponent {

    @SlingObject
    private Resource resource;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String sizeType;

    @Inject
    private String size;

    @Inject
    @Getter
    @Default(values = StringUtils.EMPTY)
    private String offsetType;

    @Inject
    private String offset;

    @Inject
    private boolean isNarrowColumn;

    @Inject
    @Getter
    private String[] classes;

    @PostConstruct
    private void init() {
        String sizeClass = createSizeClass(ColumnSizes.findByName(size).getCssClass());
        String offsetClass = createOffsetClass(ColumnSizes.findByName(offset).getCssClass());
        populateClasses(sizeClass, offsetClass);
    }

    private void populateClasses(String sizeClass, String offsetClass) {
        List<String> classList = new ArrayList<>();
        if (isNarrowColumn) {
            //TODO: Refactor this, once implementing responsiveness
            classes = new String[]{"is-narrow"};
            return;
        }

        classList.add(sizeClass);
        classList.add(offsetClass);
        classes = classList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                .toArray(new String[]{});

        if (classes.length == 0) {
            classes = new String[]{};
        }
    }

    private String createSizeClass(String classPart) {
        return StringUtils.EMPTY.equals(classPart) ? null : "is-" + classPart;
    }

    private String createOffsetClass(String classPart) {
        return StringUtils.EMPTY.equals(classPart) ? null : "is-offset-" + classPart;
    }

}
