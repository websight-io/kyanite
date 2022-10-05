import { defineConfig } from 'cypress';

export default defineConfig({
  chromeWebSecurity: false,
  screenshotsFolder: 'build/screenshots',
  videosFolder: 'build/video',
  fixturesFolder: false,
  video: false,
  viewportWidth: 1280,
  viewportHeight: 1024,
  e2e: {
    setupNodeEvents(on, config) {},
    baseUrl: 'http://localhost:8080',
    specPattern: 'tests/**/*.cy.{js,jsx,ts,tsx}',
    supportFile: 'support/index.ts',
    experimentalSessionAndOrigin: true,
    defaultCommandTimeout: 6000,
  }
});
