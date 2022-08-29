package pl.ds.bulma.components.models.columns;


import lombok.Getter;
import lombok.NoArgsConstructor;
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

@NoArgsConstructor
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
    private String isNarrowColumn;

    @Inject
    @Getter
    private String[] classes;

    @PostConstruct
    private void init() {
        size = createSizeClass(ColumnSizes.findByName(size).getCssClass());
        offset = createOffsetClass(ColumnSizes.findByName(offset).getCssClass());
        popuplateClasses();
    }

    private void popuplateClasses() {
        List<String> classList = new ArrayList<>();
        classList.add(size);
        classList.add(offset);
        if (Boolean.parseBoolean(isNarrowColumn)) {
            //TODO: Refactor this, once implementing responsiveness
            classList.add("is-narrow");
        }
        classes = classList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
                .toArray(new String[]{});
    }

    private String createSizeClass(String classPart) {
        return StringUtils.EMPTY.equals(classPart) ? null : "is-" + classPart;
    }

    private String createOffsetClass(String classPart) {
        return StringUtils.EMPTY.equals(classPart) ? null : "is-offset-" + classPart;
    }

}
