const { defineConfig } = require('@playwright/test');

module.exports = defineConfig({
  testDir: './tests',
  // flakiness controls: bounded timeouts, retry only in CI, keep a trace of the first retry
  timeout: 10_000,
  retries: process.env.CI ? 2 : 0,
  fullyParallel: true,
  reporter: [['line']],
  use: {
    baseURL: process.env.API_URL || 'http://127.0.0.1:3000',
    trace: 'on-first-retry',
  },
});
