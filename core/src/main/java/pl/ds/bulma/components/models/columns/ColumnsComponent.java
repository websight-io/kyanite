package pl.ds.bulma.components.models.columns;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.inject.Inject;
import java.util.stream.StreamSupport;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ColumnsComponent {

    private static final String COLUMN_COMPONENT_RESOURCE_TYPE = "bulma/components/columns/column";
    private static final String COLUMN_STYLE = "ColumnStyle";

    @SlingObject
    private Resource resource;

    @Getter
    @Inject
    @Default(values = "is-tablet")
    private String columnsActivationLevel;

    public boolean hasOnlyColumnsWhichHasNoChildren() {
        return StreamSupport
                .stream(resource.getChildren().spliterator(), false)
                .allMatch(it -> isColumn(it) && (!it.hasChildren() || allOfItsChildrenAreColumnStyles(it)));
    }

    private boolean allOfItsChildrenAreColumnStyles(Resource resource) {
        return StreamSupport
                .stream(resource.getChildren().spliterator(), false)
                .allMatch(it -> it.getResourceType().contains(COLUMN_STYLE));
    }

    private boolean isColumn(Resource resource) {
        return COLUMN_COMPONENT_RESOURCE_TYPE.equals(resource.getResourceType());
    }
}
