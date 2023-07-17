# Bulma variables

Bulma allows to override variables and provides an instruction on [how to do it](https://bulma.io/documentation/customize/with-webpack/).

## Design

[Design in Figma](https://www.figma.com/file/O02Ems00S1WqaOO2ICPhGx/WebSight.io---design-system%2Fcomponents-library?node-id=11-5036&t=7aZwEBfp2yk6ZtIG-0)

## Overwriting in Websight.io

Design is **partially** based on Bulma variables, therefore some of them can be easily overriden. Hovewer, websight.io has custom design and some cases like `has-text-light` and `has-background-light` has different values (in Bulma, there is one variable `$light` and all classes with "light" in name has the same value). It is desirable to overwrite bulma variables (no code repetition etc.) but also not all variables can be overwritten, or the output class (bulma generates most of them throught scss functions) still should be styled in our scss files.

## Further reading

[Bulma Concepts](https://bulma.io/documentation/customize/concepts/)

[Bulma Utilities](https://bulma.io/documentation/utilities/)
