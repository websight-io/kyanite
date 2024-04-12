# Kyanite FE approach

Kyanite is based on the bulma library, so the approach used is partially imposed from the top. Bulma allows us to override it's variables in many ways, however our approach is to override it with CSS variables. This way, when creating new project with custom colors, it is easy to run project without compiling sass first.

# Development

In the case of Bulma components, before adding a new variable, check (by inspecting code in the browser) whether Bulma does not already have a given variable for the component or in the `:root`, e.g. `—-kyanite-card-background-color`. If the variable is missing, create it according to the scheme: '-kyanite-custom' + component_name + description, e.g. `--kyanite-custom-input-placeholder-color`. Use given or created variable in styling component.

## Design

[Design in Figma](https://www.figma.com/file/O02Ems00S1WqaOO2ICPhGx/WebSight.io---design-system%2Fcomponents-library?node-id=11-5036&t=7aZwEBfp2yk6ZtIG-0)

## Overwriting in Websight.io

Design is **partially** based on Bulma variables, therefore some of them can be easily overriden. Hovewer, websight.io has custom design and some cases like `has-text-light` and `has-background-light` has different values (in Bulma, there is one variable `$light` and all classes with "light" in name has the same value). It is desirable to overwrite bulma variables (no code repetition etc.) but also not all variables can be overwritten, or the output class (bulma generates most of them throught scss functions) still should be styled in our scss files.

## Further reading

[Bulma Concepts](https://bulma.io/documentation/customize/concepts/)

