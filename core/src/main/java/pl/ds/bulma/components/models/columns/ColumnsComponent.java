package pl.ds.bulma.components.models.columns;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import java.util.stream.StreamSupport;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ColumnsComponent {

    private static final String COLUMN_COMPONENT_RESOURCE_TYPE = "bulma/components/columns/column";

    @SlingObject
    private Resource resource;

    public boolean hasOnlyColumnsWhichHasNoChildren() {
        return StreamSupport
                .stream(resource.getChildren().spliterator(), false)
                .allMatch(it -> isColumn(it) && !it.hasChildren());
    }

    private boolean isColumn(Resource resource) {
        return COLUMN_COMPONENT_RESOURCE_TYPE.equals(resource.getResourceType());
    }
}
