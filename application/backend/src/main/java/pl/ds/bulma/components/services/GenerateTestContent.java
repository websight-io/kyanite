/*
 * Copyright (C) 2022 Dynamic Solutions
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

package pl.ds.bulma.components.services;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;
import pl.ds.bulma.components.models.ContentComponent;
import pl.ds.bulma.components.models.TitleComponent;
import pl.ds.bulma.components.utils.ContentGeneration;

public class GenerateTestContent {
  public static void main(String[] args)
      throws IllegalAccessException, IOException,
      InstantiationException, InvocationTargetException, NoSuchMethodException {
    generateTestContent(TitleComponent.class);
    generateTestContent(ContentComponent.class);
  }

  private static <T> void generateTestContent(Class<T> classT)
      throws IOException, IllegalAccessException,
      InstantiationException, NoSuchMethodException, InvocationTargetException {
    System.out.println("Auto-generating test content");

    String rootPath = "./content/src/main/content/jcr_root/content/bulma/pages/";
    String className = "TestContent_%s".formatted(classT.getSimpleName());
    String classPath = rootPath + "/" + className;
    Files.createDirectories(Paths.get(classPath));
    String contentFilePath = classPath + "/" + ".content.xml";

    List<Field> classFields = getClassFields(classT);
    var fieldNameToValues = classFields.stream()
        .collect(Collectors.toMap(Field::getName,
            it -> it.getAnnotation(ContentGeneration.class)));

    var fieldNames = fieldNameToValues.keySet().stream().sorted().toList();

    List<List<Object>> perm = permuteLists(
        fieldNames.stream().map(it -> {
          var annotation = fieldNameToValues.get(it);
          if (annotation.booleanValues().length > 0) {
            return List.<Object>of(Boolean.FALSE, Boolean.TRUE);
          }
          return List.<Object>of(annotation.stringValues());
        }).toList());

    StringBuilder testContent = new StringBuilder();
    for (int j = 0; j < perm.size(); j++) {
      var permutationInstance = perm.get(j);
      var componentInstance = classT.getDeclaredConstructor().newInstance();
      for (int i = 0; i < fieldNames.size(); i++) {
        var fieldName = fieldNames.get(i);
        Object fieldValue = permutationInstance.get(i);
        var field = classFields.stream().filter(it -> it.getName().equals(fieldName)).findFirst();
        field.get().set(componentInstance, fieldValue);
      }
      String xml = getXmlContent(j, componentInstance, classT);
      testContent.append(xml);
    }

    String page = """
        <?xml version="1.0" encoding="UTF-8"?>
        <jcr:root xmlns:jcr="http://www.jcp.org/jcr/1.0" xmlns:nt="http://www.jcp.org/jcr/nt/1.0" xmlns:ws="http://ds.pl/websight" xmlns:sling="http://sling.apache.org/jcr/sling/1.0"
            jcr:primaryType="ws:Page">
            <jcr:content
                jcr:primaryType="ws:PageContent"
                jcr:title="%s"
                sling:resourceType="bulma/components/page"
                ws:template="/libs/bulma/templates/basicpage"
                navbarFixed="true"
                navbarFixedPosition="has-navbar-fixed-top">
                <pagecontainer
                    jcr:primaryType="nt:unstructured"
                    sling:resourceType="bulma/components/pagecontainer">
                    %s
                </pagecontainer>
            </jcr:content>
        </jcr:root>
        """.formatted(className, testContent);

    Files.writeString(Paths.get(contentFilePath), page, Charset.defaultCharset());

    System.out.println("Auto-generated test content done");
    System.out.println(testContent);
  }

  public static <T> List<Field> getClassFields(Class<T> classT) {
    var classFields = FieldUtils.getFieldsListWithAnnotation(classT, ContentGeneration.class);
    for (var a : classFields) {
      a.setAccessible(true);
    }
    return classFields;
  }

  public static List<List<Object>> permuteLists(List<List<Object>> lists) {
    List<List<Object>> permutations = new ArrayList<>();
    permuteListsImpl(lists, 0, new ArrayList<>(), permutations);
    return permutations;
  }

  private static void permuteListsImpl(List<List<Object>> lists, int index,
                                       List<Object> current, List<List<Object>> permutations) {
    if (index == lists.size()) {
      permutations.add(new ArrayList<>(current));
      return;
    }

    List<Object> list = lists.get(index);
    for (Object s : list) {
      current.add(s);
      permuteListsImpl(lists, index + 1, current, permutations);
      current.remove(current.size() - 1);
    }
  }

  private static <T> String getXmlContent(int uniqueIndex, T componentInstance, Class<T> classT) {
    var classFields = FieldUtils.getFieldsListWithAnnotation(
        classT, ContentGeneration.class);
    String uniqueName = "uniqueName_%s".formatted(uniqueIndex);
    String path = "bulma/components/%s".formatted("title");
    String fields = classFields.stream().map(it -> {
      try {
        it.setAccessible(true);
        return "%s=\"%s\"".formatted(it.getName(), it.get(componentInstance).toString());
      } catch (IllegalAccessException e) {
        return "";
      }
    }).collect(Collectors.joining("\n"));

    return """
        <%s
          jcr:primaryType="nt:unstructured"
          sling:resourceType="%s"
          %s/>
        """.formatted(uniqueName, path, fields);
  }
}
