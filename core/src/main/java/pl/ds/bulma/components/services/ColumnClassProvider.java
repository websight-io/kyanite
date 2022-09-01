package pl.ds.bulma.components.services;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import pl.ds.bulma.components.models.columns.ColumnSizes;
import pl.ds.bulma.components.models.columns.ResponsiveColumnStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component(service = ColumnClassProvider.class)
public class ColumnClassProvider {

    public String[] getClasses(Map<String, ResponsiveColumnStyle> columnStyleMap) {
        List<String> classList = new ArrayList<>();
        columnStyleMap.forEach((key, columnStyle) -> {
            if (columnStyle.isNarrowColumn()) {
                classList.add("is-narrow-" + key);
            } else {
                String sizeClass = createSizeClass(ColumnSizes.findByName(columnStyle.getSize()).getCssClass());
                addToClassListIfNotNull(classList, key, sizeClass);

                String offsetClass = createOffsetClass(ColumnSizes.findByName(columnStyle.getOffset()).getCssClass());
                addToClassListIfNotNull(classList, key, offsetClass);
            }
        });

        return classList.isEmpty() ? new String[]{} : classList.toArray(new String[]{});
    }

    private void addToClassListIfNotNull(List<String> classList, String key, String cssClass) {
        if (cssClass != null) {
            classList.add(cssClass + "-" + key);
        }
    }

    private String createSizeClass(String classPart) {
        return StringUtils.EMPTY.equals(classPart) ? null : "is-" + classPart;
    }

    private String createOffsetClass(String classPart) {
        return StringUtils.EMPTY.equals(classPart) ? null : "is-offset-" + classPart;
    }

}
