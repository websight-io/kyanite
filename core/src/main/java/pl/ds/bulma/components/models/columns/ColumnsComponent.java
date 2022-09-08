package pl.ds.bulma.components.models.columns;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ColumnsComponent {

    private static final String COLUMN_COMPONENT_RESOURCE_TYPE = "bulma/components/columns/column";
    private static final String SLING_RESOURCE_TYPE = "sling:resourceType";

    @SlingObject
    private Resource resource;

    @Getter
    @Inject
    @Default(values = "is-tablet")
    private String columnsActivationLevel;

    @Getter
    @Inject
    private boolean isCustomGapLevel;

    @Getter
    @Inject
    @Default(values = "is-3")
    private String mobileGapLevel;

    @Getter
    @Inject
    @Default(values = "is-3")
    private String tabletGapLevel;

    @Getter
    @Inject
    @Default(values = "is-3")
    private String desktopGapLevel;

    @Getter
    @Inject
    private String[] classes;

    @PostConstruct
    private void init() {
        List<String> classList = new ArrayList<>();
        if (isCustomGapLevel) {
            classList.add("is-variable");
            classList.add(mobileGapLevel + "-mobile");
            classList.add(tabletGapLevel + "-tablet");
            classList.add(desktopGapLevel + "-desktop");
        }
        classList.add(columnsActivationLevel);
        classes = classList.toArray(new String[]{});
    }

    public boolean hasOnlyColumnsWhichHasNoChildren() {
        return StreamSupport
                .stream(resource.getChildren().spliterator(), false)
                .allMatch(it -> isColumn(it) && (!it.hasChildren() || allOfItsChildrenAreColumnStyles(it)));
    }

    private boolean allOfItsChildrenAreColumnStyles(Resource resource) {
        return StreamSupport
                .stream(resource.getChildren().spliterator(), false)
                .allMatch(it -> it.getValueMap().get(SLING_RESOURCE_TYPE) == null);
    }

    private boolean isColumn(Resource resource) {
        return COLUMN_COMPONENT_RESOURCE_TYPE.equals(resource.getResourceType());
    }
}
