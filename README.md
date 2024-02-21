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
--base-color-body-background: #1c1c1c;
--base-color-secondary-background: #232323;
--base-color-contrast-background: #555;
--base-color-body-text: #fff;
--base-color-secondary-text: #aaa;
--base-color-contrast-text: #ccc;
--color-primary--50: #f3f0ff;
--color-primary--100: #ebe4ff;
--color-primary--200: #d8cdff;
--color-primary--300: #bda6ff;
--color-primary--400: #9e73ff;
--color-primary--500: #823bff;
--color-primary--600: #7714ff;
--color-primary--700: #6200ee;
--color-primary--800: #5901d6;
--color-primary--900: #4a03af;
--color-primary--950: #2c0077;
--color-secondary--50: #effefb;
--color-secondary--100: #c7fff5;
--color-secondary--200: #8fffec;
--color-secondary--300: #4ff9e1;
--color-secondary--400: #1be6cf;
--color-secondary--500: #1be6cf;
--color-secondary--600: #00a297;
--color-secondary--700: #048179;
--color-secondary--800: #096662;
--color-secondary--900: #0d5450;
--color-secondary--950: #003433;
```

### 3. Add the Style File to the Bundle

Ensure that the style file is included in the bundle by adding the following lines to your configuration:
``` 
Sling-Bundle-Resources: /apps/my-project/webroot
WebSight-Apps-WebRoot: /apps/my-project/webroot
```

### 4. Create a Space with Your Page Space Template

Using the WebSight CMS, create a new space using your newly created Page Space template.
