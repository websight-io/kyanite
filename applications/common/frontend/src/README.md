# Kyanite FE approach

Kyanite is based on the bulma library, so the approach used is partially imposed from the top. Bulma allows us to override it's variables in several ways, however our approach is to override it with [CSS variables](https://bulma.io/documentation/features/css-variables/). 

## Design

[Design in Figma](https://www.figma.com/file/O02Ems00S1WqaOO2ICPhGx/WebSight.io---design-system%2Fcomponents-library?node-id=11-5036&t=7aZwEBfp2yk6ZtIG-0)

## Overwriting in Websight.io

Design is **partially** based on Bulma variables, therefore some of them can be easily overriden. Hovewer, websight.io has custom design and some cases like `has-text-light` and `has-background-light` has different values (in Bulma, there is one variable `$light` and all classes with "light" in name has the same value). It is desirable to overwrite bulma variables (no code repetition etc.) but also not all variables can be overwritten, or the output class (bulma generates most of them throught scss functions) still should be styled in our scss files.

## Further reading

[Bulma Concepts](https://bulma.io/documentation/customize/concepts/)


# Development

In the case of Bulma components, before adding a new variable, check (by inspecting code in the browser) whether Bulma does not already have a given variable for the component or in the `:root`, e.g. `—-kyanite-card-background-color`. If the variable is missing, create it according to the scheme: '--kyanite-custom' + component_name + description, e.g. `--kyanite-custom-input-placeholder-color`. Use given or created variable in styling component.

## Creating new components

When creating new component (not from Bulma), follow lighthouse rules (add aria-labels when needed, width and height attributes to img etc.). Also, when adding styles, create css variables for easy customization, especially for colors. You can take any Bulma component as an example. Add 'custom' in variable name (as written above). To create dark theme version, write your styles under `.theme-dark` css class. Light theme is the default theme. 

When creating new component from Bulma the only difference is it has dark version, but some changes in kyanite can break it. Make sure that customization is made seemlessly for other themes.

## Current components
As dark theme was introduced after creating components, the significant amount of them is not customizable or does not looks good in dark theme template. It is desired to fill these style gaps when changing the component.

## Light and dark mode
Kyanite does not support light and dark system mode, it forces one of each with class from space template on `html` tag: `theme-dark` and `theme-light`. It can be used as a base color system for any new theme.


### Additional info

Where you can, please use build-in [Bulma variables](https://bulma.io/documentation/features/css-variables/)

Font weights:
```css
--kyanite-weight-light: 300;
--kyanite-weight-normal: 400;
--kyanite-weight-medium: 500;
--kyanite-weight-semibold: 600;
--kyanite-weight-bold: 700;
--kyanite-weight-extrabold: 800;
```
Base theme vars:
```css
--kyanite-family-primary
--kyanite-family-secondary
--kyanite-navbar-height

// backgorund color
--kyanite-background

// base text color
--kyanite-text 


// link color
--kyanite-link 

// animation duration
--kyanite-duration 

// easing animation type
--kyanite-easing 
```

Base grey scale (has all the classes:
`is-{name}`, `has-text-{name}`, `is-background-{name}`, `has-background-{name}`, eg. `has-background-grey`):

```css
--kyanite-black-bis: #14161a;
--kyanite-black-ter: #1f2229;
--kyanite-grey-darker: #2e333d;
--kyanite-grey-dark: #404654;
--kyanite-grey: #69748c;
--kyanite-grey-light: #abb1bf;
--kyanite-grey-lighter: #d6d9e0;
--kyanite-white-ter: #f3f4f6;
--kyanite-white-bis: #f9fafb;
```

Custom gray (**notice the different spelling than the above**) scale (has all the classes:
`is-{name}`, `has-text-{name}`, `is-background-{name}`, `has-background-{name}`, eg. `has-background-gray-15`):


**Gray hue value: 0**


Gray shades:
```css
--kyanite-gray-00
--kyanite-gray-10
--kyanite-gray-15
--kyanite-gray-20
--kyanite-gray-25
--kyanite-gray-30
--kyanite-gray-35
--kyanite-gray-40
--kyanite-gray-45
--kyanite-gray-50
--kyanite-gray-55
...
--kyanite-gray-95
--kyanite-gray-100
```

Grey shades are generated from gray, gray color is created as any other color: with `h`, `s` and `l`:
```css
--kyanite-gray-h: {value} // eg. 240deg
--kyanite-gray-s: {value} // eg. 70%
--kyanite-gray-l: {value} // eg. 50%
```

There is also another gray scale, dedicated for texts `--kyanite-text`

**Text hue value: 220**