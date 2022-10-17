# Bulma

Bulma is a sample component library for the [WebSight CMS](https://www.websight.io/) based on [Bulma](https://bulma.io/). You can try it together with a demo site. Check the [Getting Started](https://www.websight.io/getting-started/) page for information on how to run a local instance in 5 minutes.

## Modules
- `core` - components related code and scripts
- `ui.frontend` - front-end build
- `tests` - responsible for the automatic validation of the Bulma components
    - `content` - the minimal set of components and pages used during testing
    - `end-to-end` - end-to-end tests validating both  Bulma components on authoring and publication



## Development

Build
```
mvn clean install
```

Build bundles with local deployment
```
mvn clean install -P autoInstallBundle
```

This module uses Google Style and verifies code against its rules.

## License
Bulma components is `open-source` project with `Apache License 2.0` license.