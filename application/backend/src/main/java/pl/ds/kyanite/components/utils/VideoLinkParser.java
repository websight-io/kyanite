/*
 * Copyright (C) 2023 Dynamic Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.ds.kyanite.components.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoLinkParser {

  private static final String[] YOUTUBE_VIDEO_ID_REGEX = {"\\?vi?=([^&]*)", "watch\\?.*v=([^&]*)",
      "(?:embed|vi?)/([^/?]*)", "(?:user/[A-Za-z0-9]*#[a-z]/[a-z]/[a-z0-9]*/*)/([^&]*)",
      "^([A-Za-z0-9\\-]*)"};
  private static final String YOUTUBE_PLAYER_URL_PREFIX = "https://www.youtube.com/embed/";
  private static final String[] VIMEO_VIDEO_ID_REGEX = {
      "(?:video|channels|groups/name/videos)/([^/?]*)",
      "^([A-Za-z0-9\\-]*)"};

  private static final String YOUTUBE_URL_REGEX = "^(https?)?(://)?"
      + "(www.)?(m.)?((youtube.com)|(youtu.be)|(youtube-nocookie.com))/";

  private static final String VIMEO_URL_REGEX = "^(https?)?(://)?"
      + "(www.)?(m.)?((vimeo.com)|(player.vimeo.com))/";
  private static final String VIMEO_PLAYER_URL_PREFIX = "https://player.vimeo.com/video/";

  public static String getYouTubeLink(String link) {
    if (isYouTubeFullUrl(link)) {
      link = extractVideoIdFromUrl(link, YOUTUBE_VIDEO_ID_REGEX, YOUTUBE_URL_REGEX);
    }

    return YOUTUBE_PLAYER_URL_PREFIX + link;
  }

  public static String getVimeoLink(String link) {
    if (isVimeoFullUrl(link)) {
      link = extractVideoIdFromUrl(link, VIMEO_VIDEO_ID_REGEX, VIMEO_URL_REGEX);
    }
    return VIMEO_PLAYER_URL_PREFIX + link;
  }

  private static boolean isYouTubeFullUrl(String link) {
    return link.contains("youtube.com") || link.contains("youtu.be") || link.contains(
        "youtube-nocookie.com");
  }

  private static boolean isVimeoFullUrl(String link) {
    return link.contains("vimeo.com");
  }

  private static String extractVideoIdFromUrl(String url, String[] sourceVideoIdRegex,
      String sourceUrlRegex) {
    String linkWithoutProtocolAndDomain = linkWithoutProtocolAndDomain(url, sourceUrlRegex);

    for (String regex : sourceVideoIdRegex) {
      Pattern compiledPattern = Pattern.compile(regex);
      Matcher matcher = compiledPattern.matcher(linkWithoutProtocolAndDomain);

      if (matcher.find()) {
        return matcher.group(1);
      }
    }

    return null;
  }

  private static String linkWithoutProtocolAndDomain(String url, String sourceUrlRegex) {
    Pattern compiledPattern = Pattern.compile(sourceUrlRegex);
    Matcher matcher = compiledPattern.matcher(url);

    if (matcher.find()) {
      return url.replace(matcher.group(), "");
    }
    return url;
  }
}
