package pl.ds.kyanite.fragments.components.models;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
class ExperienceFragmentTest {

  private static final String PATH = "/content/fragments";
  private final SlingContext context = new SlingContext(ResourceResolverType.RESOURCERESOLVER_MOCK);

  @BeforeEach
  public void init() {
    context.addModelsForClasses(ExperienceFragment.class);
    context.load().json(requireNonNull(
        Thread.currentThread().getContextClassLoader().getResourceAsStream("fragment.json")), PATH);
  }

  @Test
  void emptyFragment() {
    ExperienceFragment model =
        requireNonNull(requireNonNull(context.resourceResolver()).getResource(PATH + "/empty"))
            .adaptTo(ExperienceFragment.class);

    assertThat(model).isNotNull();
    assertThat(model.getResource()).isNull();
    assertThat(model.getValidPage()).isFalse();
    assertThat(model.getPagePath()).isNull();
  }

  @Test
  void validFragment() {
    ExperienceFragment model =
        requireNonNull(requireNonNull(context.resourceResolver()).getResource(PATH + "/with-valid-resource"))
            .adaptTo(ExperienceFragment.class);

    assertThat(model).isNotNull();
    assertThat(model.getResource()).isEqualTo("/content/fragments/pages/fragments/fragment");
    assertThat(model.getValidPage()).isTrue();
    assertThat(model.getPagePath()).isEqualTo("/content/fragments/pages/fragments/fragment.html");
  }

  @Test
  void validFragmentWithSlashAtResourcePath() {
    ExperienceFragment model =
        requireNonNull(requireNonNull(context.resourceResolver()).getResource(PATH + "/with-slash-at-the-end"))
            .adaptTo(ExperienceFragment.class);

    assertThat(model).isNotNull();
    assertThat(model.getResource()).isEqualTo("/content/fragments/pages/fragments/fragment/");
    assertThat(model.getValidPage()).isTrue();
    assertThat(model.getPagePath()).isEqualTo("/content/fragments/pages/fragments/fragment.html");
  }

  @Test
  void invalidResourceFragment() {
    ExperienceFragment model =
        requireNonNull(requireNonNull(context.resourceResolver()).getResource(PATH + "/with-invalid-resource"))
            .adaptTo(ExperienceFragment.class);

    assertThat(model).isNotNull();
    assertThat(model.getResource()).isEqualTo("/content/fragments/pages/fragments/invalid");
    assertThat(model.getValidPage()).isFalse();
    assertThat(model.getPagePath()).isEqualTo("/content/fragments/pages/fragments/invalid.html");
  }

  @Test
  void notExistingResourceFragment() {
    ExperienceFragment model =
        requireNonNull(requireNonNull(context.resourceResolver()).getResource(PATH + "/with-not-existing-resource"))
            .adaptTo(ExperienceFragment.class);

    assertThat(model).isNotNull();
    assertThat(model.getResource()).isEqualTo("/content/fragments/pages/fragments/not-existing");
    assertThat(model.getValidPage()).isFalse();
    assertThat(model.getPagePath()).isEqualTo("/content/fragments/pages/fragments/not-existing.html");
  }

}