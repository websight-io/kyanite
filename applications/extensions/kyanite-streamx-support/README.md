# Kyanite StreamX Support

Module contains support for the integration with [StreamX](https://streamx.dev).

This bundle provides the implementations for the RelatedResourcesSelector interface.

Each selector contains the configuration allows to disable it. See:

[HomepageRelatedResourcesSelectorConfig](./src/main/java/pl/ds/kyanite/streamx/resource/impl/selector/HomepageRelatedResourcesSelectorConfig.java)

## Usage

Using the connector in the project requires adding proper configuration. Additionally, notice that
using `pl.ds.kyanite:kyanite-streamx-support:${kyanite.version}`
requires the `kyanite-streamx-support` service user.

Example:

```json
{
  "bundles": [
    {
      "id": "pl.ds.kyanite:kyanite-streamx-support:${kyanite.version}",
      "start-order": "25"
    }
  ],
  "configurations": {
    "org.apache.sling.serviceusermapping.impl.ServiceUserMapperImpl.amended~kyanite-streamx-support": {
      "user.mapping": [
        "kyanite-streamx-support=[kyanite-streamx-support]"
      ]
    }
  },
  "repoinit:TEXT|true": "@file"
}

```

Repo init with service user setup:

```
create service user kyanite-streamx-support with path system/websight

set ACL for kyanite-streamx-support
    allow   jcr:read    on /content,/published
end
```


Note: make sure you added `<filesInclude>*.json</filesInclude>` after base feature.
Your repo init entry will be added to resulting repo init script according to the order
of `<aggregate>` items. If you add `<filesInclude>*.json</filesInclude>` at the beginning
service user will be getting created before `/content` and `/published` resources will be crated
which will result with error.