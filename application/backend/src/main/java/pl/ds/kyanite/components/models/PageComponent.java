package pl.ds.kyanite.components.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import pl.ds.kyanite.utils.PageUtil;

@Model(adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageComponent {

  @SlingObject
  private Resource resource;

  public String getPageName() {
    return PageUtil.getPageProperty(resource, "jcr:title")
        .replaceAll("[^A-Za-z0-9-_]", "-")
        .toLowerCase();
  }

  public String getTemplateName() {
    String pageProperty = PageUtil.getPageProperty(resource, "ws:template");
    return pageProperty == null ? ""
        : pageProperty.substring(pageProperty.lastIndexOf("/") + 1);
  }
}
