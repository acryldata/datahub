import {
  setThemeV2AndIngestionRedesignFlags,
  goToIngestionPage,
  navigateToTab,
} from "./utils";

describe("executor pools tab in manage data sources", () => {
  beforeEach(() => {
    setThemeV2AndIngestionRedesignFlags(true);
    cy.loginWithCredentials();
    goToIngestionPage();
  });

  Cypress.on("uncaught:exception", (err, runnable) => false);

  it("navigate to Executors tab and see executor pools UI", () => {
    navigateToTab("Executors");
    cy.get('div[data-node-key="Executors"]').should(
      "have.class",
      "ant-tabs-tab-active",
    );
    cy.get('[data-testid="delete-pool-button"]').should("be.visible");
    cy.get('[data-testid="create-pool-button"]').should("be.visible");
  });
});
