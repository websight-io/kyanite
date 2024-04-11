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

## Creating a New Theme for Kyanite

Kyanite supports two themes: the default theme and a dark mode. If you wish to create your own custom theme, follow these steps:

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
Create a CSS file containing your custom styles. Kyanite utilizes CSS Variables, enabling easy customization. Below are example variables you can overwrite:

```css
    --color-primary--50: #F3F0FF;
    --color-primary--100: #EBE4FF;
    --color-primary--200: #D8CDFF;
    --color-primary--300: #BDA6FF;
    --color-primary--400: #9E73FF;
    --color-primary--500: #823BFF;
    --color-primary--600: #7714FF;
    --color-primary--700: #6200EE;
    --color-primary--800: #5901D6;
    --color-primary--900: #4A03AF;
    --color-primary--1000: #2C0077;

    --color-secondary--50: #EFFEFB;
    --color-secondary--100: #C7FFF5;
    --color-secondary--200: #8FFFEC;
    --color-secondary--300: #4FF9E1;
    --color-secondary--400: #1BE6CF;
    --color-secondary--500: #03DAC6;
    --color-secondary--600: #00A297;
    --color-secondary--700: #048179;
    --color-secondary--800: #096662;
    --color-secondary--900: #0D5450;
    --color-secondary--1000: #003433;

    --color-grey--100: #181818;
    --color-grey--200: #2a2a2a;
    --color-grey--300: #494949;
    --color-grey--400: #686868;
    --color-grey--500: #7f7f7f;
    --color-grey--600: #959595;
    --color-grey--700: #bbbbbb;
    --color-grey--800: #e1e1e1;
    --color-grey--900: #f2f3f5;
    --color-grey--1000: #f9f9fa;
    
    --kyanite-link: var(--color-primary--500);
    --kyanite-code-background: #000;
    --kyanite-code: #eaeaea;
    --kyanite-background: #0A0A0B;
    --kyanite-body-background-color: #0A0A0B;
    --kyanite-text: #ECF5FF;
    --kyanite-body-color: #ECF5FF;

    --kyanite-custom-input-placeholder-color: #A3A4B4;
    --kyanite-custom-checkbox-radio-hover: #A3A4B4;

    --kyanite-custom-table-striped-row-even-background-color: #181818;

    --kyanite-custom-tag-background: #555;
    --kyanite-custom-tag-text: #CCC;

    --kyanite-custom-title: #ECF5FF;
    --kyanite-custom-subtitle: #ECF5FF;

    --kyanite-custom-article-thumbnail-mdi-color: #ECF5FF;

    --kyanite-custom-card-background: #232323;
    --kyanite-custom-card-footer-border: #555;

    --kyanite-custom-footer-background: #0A0A0B;
    --kyanite-custom-footer-text: #ECF5FF;
    --kyanite-custom-footer-menu-label: #9E73FF;

    --kyanite-custom-navbar-menu-background: #232323;
    --kyanite-custom-navbar-megamenu-background: #232323;
    --kyanite-custom-navbar-item-color-hover: #7714FF;
    --kyanite-custom-navbar-dropdown-item-background-hover: #232323;
    --kyanite-custom-navbar-dropdown-item-text-hover: #ECF5FF;
    --kyanite-custom-navbar-burger-color: #ECF5FF;
```

### 3. Add the Style File to the Bundle

Ensure that the style file is included in the bundle by adding the following lines to your configuration:
``` 
Sling-Bundle-Resources: /apps/my-project/webroot
WebSight-Apps-WebRoot: /apps/my-project/webroot
```

### 4. Create a Space with Your Page Space Template

Using the WebSight CMS, create a new space using your newly created Page Space template.
