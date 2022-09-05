package pl.ds.bulma.components.models.columns;


import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.bulma.components.services.ColumnClassProvider;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class ColumnComponent extends DefaultResponsiveColumnComponent {

    private static final String SLING_RESOURCE_TYPE = "sling:resourceType";

    @SlingObject
    private Resource resource;

    @OSGiService
    private ColumnClassProvider columnClassProvider;

    @Inject
    @Getter
    private String[] classes;

    @PostConstruct
    private void init() {
        Map<String, ResponsiveColumnStyle> columnStyleMap = new HashMap<>();
        addToColumnStyleMapIfColumnStyleIsNotNull(columnStyleMap, "mobile", getMobileColumnStyle());
        addToColumnStyleMapIfColumnStyleIsNotNull(columnStyleMap, "tablet", getTabletColumnStyle());
        addToColumnStyleMapIfColumnStyleIsNotNull(columnStyleMap, "desktop", getDesktopColumnStyle());

        classes = columnClassProvider.getClasses(columnStyleMap);
    }

    public List<Resource> getChildrenComponents() {
        return StreamSupport
                .stream(resource.getChildren().spliterator(), false)
                .filter(it -> it.getValueMap().get(SLING_RESOURCE_TYPE) != null)
                .collect(Collectors.toList());
    }

    private void addToColumnStyleMapIfColumnStyleIsNotNull(Map<String, ResponsiveColumnStyle> columnStyleMap,
                                                           String screen,
                                                           ResponsiveColumnStyle columnStyle) {
        if (columnStyle != null) {
            columnStyleMap.put(screen, columnStyle);
        }
    }

}
