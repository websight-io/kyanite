[![CI](https://github.com/websight-io/kyanite/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/websight-io/kyanite/actions/workflows/ci.yml)
# Kyanite

Kyanite is a sample component library for the [WebSight CMS](https://www.websight.io/) based on [Bulma](https://bulma.io/).

![](assets/bulma-template.png)
## Using

### Separate project
This project delivers everything what is needed to run as a separate project. All you need is just [build](#build) and [run](#how-to-run) local environment.

### Using on existing environment
To use this project on existing environment you have to use a command from section ['Build bundles with local deployment'](#build-bundles-with-local-deployment)


## Modules
- `application` - components related code and scripts
  - `common` - base for common functionality and components
    - `backend` - backend code and scripts
    - `frontend` - frontend code and scripts
  - `extensions` - directory for extension modules
  - `blogs` - directory for blog related components
    - `backend` - backend code and scripts
- `content` - sample content built with delivered components
- `distribution` - builds a distribution of the project - instance feature model and docker images for runtime components
- `environment` - contains scripts and files used to build environment
  - `local` - starts local environment
- `tests` - responsible for the automatic distribution validation
  - `content` - contains content used for end-to-end tests
  - `end-to-end` - end-to-end tests validating distribution

## Development

### Build
```bash
./mvnw clean install
```

### Build bundles with local deployment
```bash
./mvnw clean install -P autoInstallBundle
```

### How to run
Once Docker images are ready, all you need is to run Docker Compose from the `environment/local` folder:

```bash
docker compose up
```

After the run, you can get access to the application using: http://localhost:8080/apps/websight/index.html/content::spaces (credentials are `wsadmin/wsadmin`)

This module uses Google Style and verifies code against its rules.

## Contributing
Please read our [Contributing Guide](./CONTRIBUTING.md) before submitting a Pull Request to the project.

## Community support
Please check the community support section in [WebSight Starter](https://github.com/websight-io/starter#community-support).

## License
Kyanite components is `open-source` project with `Apache License 2.0` license.

## Light and dark mode
Kyanite does not support light and dark system mode, it forces one of each with class from space template on `html` tag: `theme-dark` and `theme-light`. It can be used as a base color system for any new theme. 

## Creating a New Theme for Kyanite

Kyanite supports creating custom themes, based on light or dark color palette.
If you wish to create your own custom theme, follow these steps:

### 1. Create a New Page Space Template

Begin by creating a new Page Space template. Add a `stylePath` property with the path to the CSS file containing your custom styles.

```json
{
  "sling:resourceType": "ws:PagesTemplate",
  "title": "Kyanite - My Own Theme",
  "stylesPath": "/apps/my-project/webroot/my-own-theme.css",
  "allowedChildren": [
    ...
  ]
}
```

### 2. Prepare Your Styles
Create a CSS file containing your custom styles. Kyanite utilizes CSS Variables, enabling easy customization. All build-in colors (like `primary`, `secondary`, `gray`, `success`, `warning`, `danger`, `info`, `link`, `text`) are generated from **hsl** value:
```css
  --kyanite-primary-h: 220deg;
  --kyanite-primary-s: 100%;
  --kyanite-primary-l: 50%;
```

And then shades are generated (number indicates the lightness in hsl value):
```css
  --kyanite-primary-00
  --kyanite-primary-10
  --kyanite-primary-15
  --kyanite-primary-20
  --kyanite-primary-25
  --kyanite-primary-30
  --kyanite-primary-35
  --kyanite-primary-40
  --kyanite-primary-45
  --kyanite-primary-50
  --kyanite-primary-55
  ...
  --kyanite-primary-95
  --kyanite-primary-100
```

If some of the shade doesn't match the preferred color value, you can assign your own lightness (lightness it the only value you can manipulate), eg.
```css
--kyanite-primary-30-l: 32%
```
So the `--kyanite-primary-30` variable will have value: `hsl(220 100% 32%)` instead of `hsl(220 100% 30%)`


These are the colors that can be overwritten in hex value (be aware of grey, which is different than gray):
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

Other global variables (for others, check `:root`):

```css
  --kyanite-link: var(--kyanite-primary-50);
  --kyanite-code-background: #000;
  --kyanite-code: #eaeaea;
  --kyanite-background: #0A0A0B;
  --kyanite-body-background-color: #0A0A0B;
  --kyanite-text: #ECF5FF;
  --kyanite-body-color: #ECF5FF;
```

If you wish to customize components, use css variables inside top level component's name class:
``` 
  .card {
    --kyanite-card-background-color: #ffffff;
    --kyanite-card-footer-border-top: #ededed;
  }
```

### 3. Add the Style File to the Bundle

Ensure that the style file is included in the bundle by adding the following lines to your configuration:
``` 
Sling-Bundle-Resources: /apps/my-project/webroot
WebSight-Apps-WebRoot: /apps/my-project/webroot
```

### 4. Create a Space with Your Page Space Template

Using the WebSight CMS, create a new space using your newly created Page Space template.
