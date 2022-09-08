package pl.ds.bulma.components.services;

import org.junit.jupiter.api.Test;
import pl.ds.bulma.components.models.columns.ResponsiveColumnStyle;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ColumnClassProviderTest {

    private final ColumnClassProvider columnClassProvider = new ColumnClassProvider();

    @Test
    void getClassesWhenColumnStyleMapContainsNarrowColumnTest() {
        Map<String, ResponsiveColumnStyle> columnStyleMap = new HashMap<>();

        ResponsiveColumnStyle mobileColumnStyle = new ResponsiveColumnStyle();
        ResponsiveColumnStyle tabletColumnStyle = new ResponsiveColumnStyle();
        tabletColumnStyle.setNormalColumn(true);
        tabletColumnStyle.setSizeType("fraction");
        tabletColumnStyle.setSize("half");
        tabletColumnStyle.setOffsetType("evenNumber");
        tabletColumnStyle.setOffset("two");

        columnStyleMap.put("mobile", mobileColumnStyle);
        columnStyleMap.put("tablet", tabletColumnStyle);

        String[] resultArray = columnClassProvider.getClasses(columnStyleMap);

        assertThat(resultArray).containsExactlyInAnyOrder("is-narrow-mobile", "is-half-tablet", "is-offset-2-tablet");
    }

    @Test
    void getClassesWhenColumnStyleMapContainsEmptySizeAndOffsetTest() {
        Map<String, ResponsiveColumnStyle> columnStyleMap = new HashMap<>();

        ResponsiveColumnStyle mobileColumnStyle = new ResponsiveColumnStyle();
        ResponsiveColumnStyle tabletColumnStyle = new ResponsiveColumnStyle();
        tabletColumnStyle.setNormalColumn(true);
        tabletColumnStyle.setSizeType("fraction");
        tabletColumnStyle.setSize("");
        tabletColumnStyle.setOffsetType("evenNumber");
        tabletColumnStyle.setOffset("");

        columnStyleMap.put("mobile", mobileColumnStyle);
        columnStyleMap.put("tablet", tabletColumnStyle);

        String[] resultArray = columnClassProvider.getClasses(columnStyleMap);

        assertThat(resultArray).containsExactly("is-narrow-mobile");
    }
}
