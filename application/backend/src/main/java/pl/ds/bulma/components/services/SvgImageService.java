package pl.ds.bulma.components.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.FileNameMap;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jetbrains.annotations.Nullable;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.websight.assets.core.api.Asset;
import pl.ds.websight.assets.core.api.Rendition;

@Component(service = {SvgImageService.class})
public class SvgImageService {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  public String getSvgFromResource(String link, ResourceResolver resourceResolver) {
    Rendition originalRendition = getRendition(link, resourceResolver);
    if (Objects.nonNull(originalRendition)) {
      try (InputStream inputStream = originalRendition.openStream();
          BufferedReader reader = new BufferedReader(
              new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        return reader.lines().collect(Collectors.joining("\n"));
      } catch (IOException e) {
        log.error("Error reading resource: {}", link);
      }
    }
    return StringUtils.EMPTY;
  }


  @Nullable
  private static Rendition getRendition(String link, ResourceResolver resourceResolver) {
    final Resource resource = resourceResolver.getResource(link);
    return Optional.ofNullable(resource)
        .map(res -> res.adaptTo(Asset.class))
        .map(Asset::getOriginalRendition)
        .orElse(null);
  }


  public String getMimeType(String link, ResourceResolver resourceResolver, boolean isInternal) {
    if (isInternal) {
      final Rendition originalRendition = getRendition(link,
          resourceResolver);
      return Optional.ofNullable(originalRendition)
          .map(Rendition::getMimeType)
          .orElse(StringUtils.EMPTY);
    } else {
      final File file = new File(link);
      final FileNameMap fileNameMap = URLConnection.getFileNameMap();
      return fileNameMap.getContentTypeFor(file.getName());
    }
  }

  public String getSvgFromExternalUrl(String link) {
    try {
      URL url = new URL(link);
      try (InputStream inputStream = url.openStream();
          BufferedReader reader = new BufferedReader(
              new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
        return reader.lines().collect(Collectors.joining("\n"));
      }
    } catch (IOException e) {
      log.error("Error getting link {}", link);
      return StringUtils.EMPTY;
    }
  }
}