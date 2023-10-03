## Description

Module contains Front-End for port of Kyanite components. During build it copy build results to web root folder in core module.

## Usage:

Build

```
mvn clean install
```

Local development

Run `npm run sync` to watch your changes. Those will be deployed to local environment on the fly

Local deployment

Front-End files are deployed as a part of `core` package.

1. Build this module
2. Build with local deployment `core` module


## CSS Variables:

CSS Variables are generated automaticly in 'global-variables.scss'. These variables can be `used` or `redefined` in your project.

Places where the data necessary to generate css variables are located:
1. `/atomic-design-system/00-token/` - Contain folders named and arranged according to the purpose of css attributes. 
2. `/bulma-overwrite/_variables.scss` - Contain defined spacing names and values.

Important files:
1. `global-variables.scss` - File responsible for generating css variables base on sass maps imported from '/atomic-design-system/00-token/*' and '/bulma-overwrite/_variables.scss'.

IMPORTANT: The names of maps defined in the '/atomic-design-system/00-token/' and '/bulma-overwrite/_variables.scss' can't be changed because the rest of 'atomic-design-system' uses css variables from 'global-varibles.scss'.  