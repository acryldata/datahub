const domainName = "CypressNestedDomain";
const domainDescription = "CypressNestedDomainDescription";

//Delete Unecessary Existing Domains
const deleteExisitingDomain = () => {
    cy.get('a[href*="urn:li"] span[class^="ant-typography"]')
          .should('be.visible')
          .its('length')
          .then((length) => {
        for (let i = 0; i < length - 1; i++) {
            cy.get('a[href*="urn:li"] span[class^="ant-typography"]')
                .should('be.visible')
                .first()
                .click({ force: true });
            deleteFromDomainDropdown();
        }
    });   
    cy.waitTextVisible('Marketing'); 
 }  

const createDomain = () => {
    cy.get('.anticon-plus').first().click()
    cy.waitTextVisible('Create New Domain')
    cy.get('[data-testid="create-domain-name"]').click().type(domainName);
    cy.get('[data-testid="create-domain-description"]').click().type(domainDescription);
    cy.clickOptionWithTestId("create-domain-button");
    cy.waitTextVisible("Created domain!");
    }

const moveDomaintoRootLevel = () => {
    cy.clickOptionWithText(domainName);
    cy.openThreeDotMenu();
    cy.clickOptionWithTestId("entity-menu-move-button");
    cy.get('[data-testid="move-domain-modal"]').contains("Marketing").click({force: true});
    cy.waitTextVisible('Marketing')
    cy.clickOptionWithTestId("move-domain-modal-move-button")
   }

const moveDomaintoParent = () => {
    cy.get('[data-testid="domain-list-item"]').contains("Marketing").prev().click();
    cy.clickOptionWithText(domainName);
    cy.waitTextVisible(domainName)
    cy.openThreeDotMenu();
    cy.clickOptionWithTestId("entity-menu-move-button");
    cy.clickOptionWithTestId("move-domain-modal-move-button")
   }

const getDomainList = (domainName) =>{
    cy.contains('span.ant-typography-ellipsis', domainName)
          .parent('.sc-dPaNzc')
          .find('[aria-label="right"]')
          .click();
    }

const deleteFromDomainDropdown = () => {
     cy.clickOptionWithText('Filters')
     cy.openThreeDotMenu();
     cy.clickOptionWithTestId("entity-menu-delete-button");
     cy.waitTextVisible("Are you sure you want to remove this Domain?");
     cy.clickOptionWithText("Yes");
    }       

const deleteDomain = () => {
    cy.clickOptionWithText(domainName).waitTextVisible('Domains');
    deleteFromDomainDropdown()
    } 

const verifyEditAndPerformAddAndRemoveActionForDomain = (entity, action, text, body) =>{
    cy.clickOptionWithText(entity)
    cy.clickOptionWithText(action)  
    cy.get('[data-testid="tag-term-modal-input"]').type(text)
    cy.get('[data-testid="tag-term-option"]').contains(text).click()
    cy.clickOptionWithText(body)
    cy.get('[data-testid="add-tag-term-from-modal-btn"]').click()
    cy.waitTextVisible(text)
    }

const clearAndType = (text) =>{
    cy.get('[role="textbox"]').click().clear().type(text) 
    }

const clearAndDelete = () =>{
    cy.clickOptionWithText("Edit")
    cy.get('[role="textbox"]').click().clear()  
    cy.clickOptionWithTestId("description-editor-save-button") 
    cy.waitTextVisible('No documentation')   
    cy.mouseover('.ant-list-item-meta-content')
    cy.get('[aria-label="delete"]').click()
    cy.waitTextVisible('Link Removed')
    }

describe("Verify nested domains test functionalities", () => {
    beforeEach (() => {
    cy.loginWithCredentials();
    cy.goToDomainList();
  });
  
    it("Verify Create a new domain", () => {
        deleteExisitingDomain()  
        cy.get('a[href*="urn:li"] span[class^="ant-typography"]')
          .should('be.visible')    
        createDomain();
        cy.waitTextVisible("Domains");
    });
        
    it  ("verify Move domain root level to parent level", () => {
        cy.waitTextVisible(domainName)
        moveDomaintoRootLevel();
        cy.waitTextVisible("Moved Domain!")
        cy.goToDomainList();
        cy.waitTextVisible("1 sub-domain");
    });

    it("Verify Move domain parent level to root level", () => {
        moveDomaintoParent();
        cy.waitTextVisible("Moved Domain!")
        cy.goToDomainList();
        cy.waitTextVisible(domainName);
        cy.waitTextVisible(domainDescription);
    });

    it("Verify Edit Domain Name", () => {
        cy.clickFirstOptionWithText(domainName)
        cy.clickOptionWithText('Filters')

        //edit name
        cy.get('.anticon-edit').eq(0).click().then(() => {
            cy.get('.ant-typography-edit-content').type(" Edited").type('{enter}');
          });     
        cy.waitTextVisible(domainName + " Edited")
    })

    it("Verify Remove the domain", () => {
        deleteDomain();
        cy.goToDomainList();
        cy.ensureTextNotPresent(domainName);
        cy.ensureTextNotPresent(domainDescription);
    });

    it('Verify Add and delete sub domain', () => {
        cy.clickFirstOptionWithText('Marketing')
        cy.clickOptionWithText('Filters')
        createDomain();
        cy.ensureTextNotPresent('Created domain!')
        getDomainList('Marketing')
        cy.clickOptionWithText(domainName)
        deleteFromDomainDropdown()
        cy.ensureTextNotPresent(domainName)
    })
    
    it('Verify entities tab with addeding and deleting assets and performimg some actions', () => {
        cy.clickFirstOptionWithText('Marketing');
        cy.clickOptionWithText('Add assets');
        cy.waitTextVisible("Add assets to Domain");
        cy.enterTextInSpecificTestId("search-bar", 3, 'Baz Chart 1')
        cy.clickOptionWithSpecificClass('.ant-checkbox', 1)
        cy.clickOptionWithId('#continueButton')
        cy.waitTextVisible("Added assets to Domain!")
        cy.openThreeDotDropdown()
        cy.clickOptionWithText("Edit")
        cy.clickOptionWithSpecificClass('.ant-checkbox', 1)
        verifyEditAndPerformAddAndRemoveActionForDomain('Glossary Terms', 'Add Glossary Terms', 'AccountBalance', 'Add Glossary Terms')
        verifyEditAndPerformAddAndRemoveActionForDomain('Tags', 'Add tags', 'Legacy', 'Add Tags')
        cy.clickOptionWithText('Baz Chart 1')
        cy.waitTextVisible("Legacy")
        cy.waitTextVisible("AccountBalance")
        cy.waitTextVisible("Marketing")
        cy.go('back')
        cy.openThreeDotDropdown()
        cy.clickOptionWithText("Edit")
        cy.clickOptionWithSpecificClass('.ant-checkbox', 1)
        verifyEditAndPerformAddAndRemoveActionForDomain('Glossary Terms', 'Remove Glossary Terms', 'AccountBalance', 'Remove Glossary Terms')
        cy.ensureTextNotPresent("AccountBalance")
        verifyEditAndPerformAddAndRemoveActionForDomain('Tags', 'Remove tags', 'Legacy', 'Remove Tags')
        cy.ensureTextNotPresent("Legacy")
        cy.clickTextOptionWithClass('.ant-dropdown-trigger', 'Domain')
        cy.clickOptionWithText('Unset Domain')
        cy.clickOptionWithText("Yes");
        cy.clickOptionWithText('Baz Chart 1')
        cy.waitTextVisible('Dashboards')
        cy.reload()
        cy.ensureTextNotPresent("Marketing")
    })
    
    it("Verify Documentation tab by adding editing Description and adding link", () => {
        cy.clickOptionWithText("Marketing")
        cy.clickOptionWithId('#rc-tabs-0-tab-Documentation')
        cy.clickFirstOptionWithText("Add Documentation")
        clearAndType("Test added")
        cy.clickOptionWithTestId("description-editor-save-button")
        cy.waitTextVisible('Description Updated')
        cy.waitTextVisible('Test added')
        cy.clickFirstOptionWithTestId("add-link-button")
        cy.waitTextVisible("Add Link")
        cy.enterTextInTestId("add-link-modal-url", 'www.test.com')
        cy.enterTextInTestId("add-link-modal-label", 'Test Label')
        cy.clickOptionWithTestId("add-link-modal-add-button")
        cy.waitTextVisible("Test Label")
        cy.goToDomainList();
        cy.waitTextVisible("Test added")
        cy.clickOptionWithText("Marketing")
        cy.clickOptionWithText("Documentation")
        clearAndDelete()
    })
    
    it("Verify Right side panel functionalityies", () => {
        cy.clickOptionWithText("Marketing")
        cy.waitTextVisible("Filters")
        cy.clickOptionWithText("Add Documentation")
        clearAndType("Test documentation")
        cy.clickOptionWithTestId("description-editor-save-button")
        cy.ensureTextNotPresent("Add Documentation")
        cy.waitTextVisible('Test documentation')
        cy.clickFirstOptionWithSpecificTestId("add-link-button", 1)
        cy.waitTextVisible("URL")
        cy.enterTextInTestId("add-link-modal-url", 'www.test.com')
        cy.enterTextInTestId("add-link-modal-label", 'Test Label')
        cy.clickOptionWithTestId("add-link-modal-add-button")
        cy.waitTextVisible("Test Label")
        cy.clickOptionWithTestId("add-owners-button")
        cy.waitTextVisible("Find a user or group")
        cy.clickTextOptionWithClass(".rc-virtual-list-holder-inner", "DataHub")
        cy.clickOptionWithText("Find a user or group")
        cy.clickOptionWithId('#addOwnerButton')
        cy.waitTextVisible("DataHub")
        cy.goToDomainList();
        cy.waitTextVisible("Test documentation")
        cy.waitTextVisible("DataHub")
        cy.clickOptionWithText("Marketing")
        cy.clickOptionWithText("Documentation")
        clearAndDelete()
    })
});
