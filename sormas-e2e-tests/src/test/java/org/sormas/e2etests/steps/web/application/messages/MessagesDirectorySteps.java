package org.sormas.e2etests.steps.web.application.messages;

import cucumber.api.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.sormas.e2etests.helpers.AssertHelpers;
import org.sormas.e2etests.helpers.WebDriverHelpers;
import org.sormas.e2etests.helpers.files.FilesHelper;
import org.sormas.e2etests.steps.BaseSteps;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.sormas.e2etests.pages.application.cases.CreateNewCasePage.ACTION_CONFIRM_POPUP_BUTTON;
import static org.sormas.e2etests.pages.application.cases.EditCasePage.SAMPLES_CARD_LABORATORY;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.ACTION_YES_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.ADD_VACCINATION_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.ASSIGNEE_LABEL;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.ASSIGN_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.CASE_SAVED_POPUP_DE;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.CLINICAL_MEASUREMENT_HEADER;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.CLOSE_POPUP;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.CREATE_NEW_CASE_POPUP_WINDOW_DE;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.CREATE_NEW_SAMPLE_POPUP_WINDOW_DE;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.CURRENT_HOSPITALIZATION_HEADER;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.DOWNLOAD_PROCESSED_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.DOWNLOAD_UNPROCESSED_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.EDIT_ASSIGNEE_FILTER_SELECT_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.EXPOSURE_INVESTIGATION_HEADER;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.FETCH_MESSAGES_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.FETCH_MESSAGES_NULL_DATE;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.FETCH_MESSAGES_NULL_TIME_COMBOBOX;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.FIRST_RECORD_DISEASE_VARIANT;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.FIRST_TIME_FETCH_MESSAGE_POPUP;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.FORWARDED_MESSAGE_COUNTER;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.GET_NEW_MESSAGES_POPUP;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.GRID_MESSAGE_UUID_TITLE;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.GRID_RESULTS_TYPE;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.HEADER_OF_ENTRY_LINK;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.MARK_AS_FORWARDED_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.MARK_AS_UNCLEAR_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.MESSAGE_DELETE_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.MESSAGE_DIRECTORY_HEADER_DE;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.MESSAGE_UUID_TEXT;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.NEW_CASE_EMAIL_ADDRESS_INPUT;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.NEW_CASE_PHONE_NUMBER_INPUT;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.NEW_SAMPLE_DATE_OF_REPORT_INPUT;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.NEW_SAMPLE_SPECIMEN_CONDITION_INPUT;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.NEW_SAMPLE_TESTED_DISEASE_INPUT;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.NEW_SAMPLE_TEST_RESULT_INPUT;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.NEW_SAMPLE_TEST_RESULT_VERIFIED_RADIOBUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.NEW_SAMPLE_TEST_RESULT_VERIFIED_SELECTED_VALUE;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.NEXT_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.NO_NEW_REPORTS_POPUP;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.PATHOGEN_DETECTION_REPORTING_PROCESS_HEADER_DE;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.POPUP_CONFIRM_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.POPUP_WINDOW_CANCEL_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.POPUP_WINDOW_DISCARD_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.POPUP_WINDOW_SAVE_AND_OPEN_CASE_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.POPUP_WINDOW_SAVE_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.RESET_FILTER_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.SEARCH_MESSAGE_INPUT;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.SEND_TO_ANOTHER_ORGANIZATION_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.TOTAL_MESSAGE_COUNTER;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.TYPE_OF_MESSAGE_COMBOBOX;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.UNCLEAR_MESSAGE_COUNTER;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.UPDATE_CASE_DISEASE_VARIANT_CONFIRM_BUTTON;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.getProcessMessageButtonByIndex;
import static org.sormas.e2etests.pages.application.messages.MessagesDirectoryPage.getProcessStatusByIndex;

@Slf4j
public class MessagesDirectorySteps implements En {

  public static List<String> uuids = new ArrayList<>();
  public static List<String> shortenedUUIDS = new ArrayList<>();

  @Inject
  public MessagesDirectorySteps(
      WebDriverHelpers webDriverHelpers,
      BaseSteps baseSteps,
      SoftAssert softly,
      AssertHelpers assertHelpers) {

    When(
        "I click on fetch messages button",
        () -> {
          webDriverHelpers.clickOnWebElementBySelector(FETCH_MESSAGES_BUTTON);
          TimeUnit.SECONDS.sleep(2); // wait for fetch
          if (webDriverHelpers.isElementVisibleWithTimeout(
              NO_NEW_REPORTS_POPUP, 1)) { // because of tests works in parallel
            webDriverHelpers.clickOnWebElementBySelector(CLOSE_POPUP);
            Actions action = new Actions(baseSteps.getDriver());
            action.sendKeys(Keys.ESCAPE);
          }
          if (webDriverHelpers.isElementVisibleWithTimeout(GET_NEW_MESSAGES_POPUP, 1)) {
            webDriverHelpers.clickOnWebElementBySelector(
                UPDATE_CASE_DISEASE_VARIANT_CONFIRM_BUTTON);
          }
          if (webDriverHelpers.isElementVisibleWithTimeout(FIRST_TIME_FETCH_MESSAGE_POPUP, 1)) {
            webDriverHelpers.clickOnWebElementBySelector(ACTION_YES_BUTTON);
            webDriverHelpers.fillInWebElement(
                FETCH_MESSAGES_NULL_DATE,
                LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            webDriverHelpers.selectFromCombobox(FETCH_MESSAGES_NULL_TIME_COMBOBOX, "00:00");
            webDriverHelpers.clickOnWebElementBySelector(ACTION_CONFIRM_POPUP_BUTTON);
          }
        });

    And(
        "^I click on process button for (\\d+) result in Message Directory page$",
        (Integer resultNumber) -> {
          webDriverHelpers.clickOnWebElementBySelector(
              getProcessMessageButtonByIndex(resultNumber));
        });

    And(
        "^I check that create new case form with pathogen detection reporting process is displayed for DE$",
        () -> {
          webDriverHelpers.waitUntilIdentifiedElementIsPresent(CREATE_NEW_CASE_POPUP_WINDOW_DE);
          webDriverHelpers.waitUntilIdentifiedElementIsPresent(
              PATHOGEN_DETECTION_REPORTING_PROCESS_HEADER_DE);
        });

    And(
        "^I check that new sample form with pathogen detection reporting process is displayed$",
        () -> {
          webDriverHelpers.waitUntilIdentifiedElementIsPresent(CREATE_NEW_SAMPLE_POPUP_WINDOW_DE);
          webDriverHelpers.waitForPageLoadingSpinnerToDisappear(20);
        });

    And(
        "^I click on YES button in Update case disease variant popup window$",
        () -> {
          webDriverHelpers.waitUntilElementIsVisibleAndClickable(
              UPDATE_CASE_DISEASE_VARIANT_CONFIRM_BUTTON);
          webDriverHelpers.clickOnWebElementBySelector(UPDATE_CASE_DISEASE_VARIANT_CONFIRM_BUTTON);
        });

    And(
        "I click on {string} button in new sample form with pathogen detection reporting process",
        (String option) -> {
          switch (option) {
            case "save and open case":
              webDriverHelpers.waitUntilElementIsVisibleAndClickable(
                  POPUP_WINDOW_SAVE_AND_OPEN_CASE_BUTTON);
              webDriverHelpers.clickOnWebElementBySelector(POPUP_WINDOW_SAVE_AND_OPEN_CASE_BUTTON);
              break;
            case "cancel":
              webDriverHelpers.waitUntilElementIsVisibleAndClickable(POPUP_WINDOW_CANCEL_BUTTON);
              webDriverHelpers.clickOnWebElementBySelector(POPUP_WINDOW_CANCEL_BUTTON);
              break;
            case "discard":
              webDriverHelpers.waitUntilElementIsVisibleAndClickable(POPUP_WINDOW_DISCARD_BUTTON);
              webDriverHelpers.clickOnWebElementBySelector(POPUP_WINDOW_DISCARD_BUTTON);
              break;
            case "save":
              webDriverHelpers.waitUntilElementIsVisibleAndClickable(POPUP_WINDOW_SAVE_BUTTON);
              webDriverHelpers.clickOnWebElementBySelector(POPUP_WINDOW_SAVE_BUTTON);
              break;
          }
        });

    Then(
        "^I back to message directory$",
        () -> {
          webDriverHelpers.waitUntilElementIsVisibleAndClickable(MESSAGE_DIRECTORY_HEADER_DE);
        });

    And(
        "^I verify that status for result (\\d+) is set to processed in Message Directory page$",
        (Integer resultNumber) -> {
          softly.assertEquals(
              webDriverHelpers.getTextFromWebElement(getProcessStatusByIndex(resultNumber)),
              "Verarbeitet",
              "This message is not processed!");
          softly.assertAll();
        });

    And(
        "^I collect message uuid$",
        () -> {
          uuids.add(webDriverHelpers.getValueFromWebElement(MESSAGE_UUID_TEXT));
        });

    Then(
        "^I click Delete button in Message form$",
        () -> {
          webDriverHelpers.waitUntilIdentifiedElementIsVisibleAndClickable(MESSAGE_DELETE_BUTTON);
          webDriverHelpers.clickOnWebElementBySelector(MESSAGE_DELETE_BUTTON);
        });

    And(
        "^I confirm message deletion$",
        () -> {
          webDriverHelpers.waitUntilIdentifiedElementIsVisibleAndClickable(POPUP_CONFIRM_BUTTON);
          webDriverHelpers.clickOnWebElementBySelector(POPUP_CONFIRM_BUTTON);
        });

    And(
        "^I filter messages by collected uuid$",
        () -> {
          System.out.println("UUID: " + uuids.get(0));
          webDriverHelpers.fillAndSubmitInWebElement(SEARCH_MESSAGE_INPUT, uuids.get(0));
          TimeUnit.SECONDS.sleep(2); // wait for reaction
          webDriverHelpers.waitForPageLoadingSpinnerToDisappear(40);
        });

    And(
        "^I check that number of displayed messages results is (\\d+)$",
        (Integer number) -> {
          String textFromCounter =
              webDriverHelpers.getTextFromPresentWebElement(TOTAL_MESSAGE_COUNTER).substring(4);
          assertHelpers.assertWithPoll20Second(
              () ->
                  Assert.assertEquals(
                      Integer.parseInt(textFromCounter),
                      number.intValue(),
                      "Number of displayed messages is not correct"));
        });

    And(
        "^I check that the Delete button is not available$",
        () -> {
          softly.assertFalse(
              webDriverHelpers.isElementVisibleWithTimeout(MESSAGE_DELETE_BUTTON, 2),
              "Delete message button is visible!");
          softly.assertAll();
        });

    And(
        "^I click on reset filters button from Message Directory$",
        () -> {
          webDriverHelpers.clickOnWebElementBySelector(RESET_FILTER_BUTTON);
          webDriverHelpers.waitForPageLoadingSpinnerToDisappear(40);
        });

    And(
        "^I click on the Mark as unclear button$",
        () -> {
          webDriverHelpers.waitUntilIdentifiedElementIsVisibleAndClickable(MARK_AS_UNCLEAR_BUTTON);
          webDriverHelpers.clickOnWebElementBySelector(MARK_AS_UNCLEAR_BUTTON);
        });

    And(
        "I filter messages by {string} in Message Directory",
        (String option) -> {
          switch (option) {
            case "Unclear":
              webDriverHelpers.clickOnWebElementBySelector(UNCLEAR_MESSAGE_COUNTER);
              webDriverHelpers.waitForPageLoadingSpinnerToDisappear(40);
              break;
            case "Forwarded":
              webDriverHelpers.clickOnWebElementBySelector(FORWARDED_MESSAGE_COUNTER);
              webDriverHelpers.waitForPageLoadingSpinnerToDisappear(40);
              break;
          }
        });

    And(
        "^I check that number of displayed messages results for Unklar is (\\d+)$",
        (Integer number) -> {
          String textFromCounter =
              webDriverHelpers.getTextFromPresentWebElement(UNCLEAR_MESSAGE_COUNTER).substring(6);
          assertHelpers.assertWithPoll20Second(
              () ->
                  Assert.assertEquals(
                      Integer.parseInt(textFromCounter),
                      number.intValue(),
                      "Number of displayed messages is not correct"));
        });

    And(
        "^I click on the Mark as a forwarded button$",
        () -> {
          webDriverHelpers.clickOnWebElementBySelector(MARK_AS_FORWARDED_BUTTON);
          webDriverHelpers.waitForPageLoadingSpinnerToDisappear(40);
        });

    And(
        "^I check that number of displayed messages results for Weitergeleitet is (\\d+)$",
        (Integer number) -> {
          String textFromCounter =
              webDriverHelpers
                  .getTextFromPresentWebElement(FORWARDED_MESSAGE_COUNTER)
                  .substring(14);
          assertHelpers.assertWithPoll20Second(
              () ->
                  Assert.assertEquals(
                      Integer.parseInt(textFromCounter),
                      number.intValue(),
                      "Number of displayed messages is not correct"));
        });

    And(
        "I check if {string} is prefilled in New sample form while processing a DEMIS LabMessage",
        (String option) -> {
          switch (option) {
            case "date of report":
              softly.assertFalse(
                  webDriverHelpers
                      .getValueFromWebElement(NEW_SAMPLE_DATE_OF_REPORT_INPUT)
                      .isEmpty(),
                  "Date of report is empty!");
              softly.assertAll();
              break;
            case "test result":
              softly.assertFalse(
                  webDriverHelpers.getValueFromWebElement(NEW_SAMPLE_TEST_RESULT_INPUT).isEmpty(),
                  "Tested result is empty!");
              softly.assertAll();
              break;
            case "specimen condition":
              softly.assertFalse(
                  webDriverHelpers
                      .getValueFromWebElement(NEW_SAMPLE_SPECIMEN_CONDITION_INPUT)
                      .isEmpty(),
                  "Specimen condition is empty!");
              softly.assertAll();
              break;
            case "test result verified":
              softly.assertFalse(
                  webDriverHelpers.isElementChecked(NEW_SAMPLE_TEST_RESULT_VERIFIED_RADIOBUTTON),
                  "Test result verified is not checked!");
              softly.assertAll();
              break;
            case "tested disease":
              softly.assertFalse(
                  webDriverHelpers
                      .getValueFromWebElement(NEW_SAMPLE_TESTED_DISEASE_INPUT)
                      .isEmpty(),
                  "Tested disease is empty!");
              softly.assertAll();
              break;
          }
        });

    And(
        "^I check if \"([^\"]*)\" is prefilled in New case form while processing a DEMIS LabMessage$",
        (String option) -> {
          switch (option) {
            case "email address":
              softly.assertFalse(
                  webDriverHelpers.getValueFromWebElement(NEW_CASE_EMAIL_ADDRESS_INPUT).isEmpty(),
                  "Email address is empty!");
              softly.assertAll();
              break;
            case "phone number":
              softly.assertFalse(
                  webDriverHelpers.getValueFromWebElement(NEW_CASE_PHONE_NUMBER_INPUT).isEmpty(),
                  "Phone number is empty!");
              softly.assertAll();
              break;
          }
        });

    Then(
        "^I check if \"([^\"]*)\" is set to \"([^\"]*)\"$",
        (String option, String value) -> {
          switch (option) {
            case "specimen condition":
              softly.assertEquals(
                  webDriverHelpers.getValueFromWebElement(NEW_SAMPLE_SPECIMEN_CONDITION_INPUT),
                  value,
                  "Value in specimen condition is incorrect!");
              softly.assertAll();
              break;
            case "test result verified":
              softly.assertEquals(
                  webDriverHelpers.getTextFromWebElement(
                      NEW_SAMPLE_TEST_RESULT_VERIFIED_SELECTED_VALUE),
                  value,
                  "Value in test result verified is incorrect!");
              softly.assertAll();
              break;
            case "tested disease":
              softly.assertEquals(
                  webDriverHelpers.getValueFromWebElement(NEW_SAMPLE_TESTED_DISEASE_INPUT),
                  value,
                  "Value in tested disease is incorrect!");
              softly.assertAll();
              break;
          }
        });

    And(
        "I check if disease variant field for first record displays {string} in Message Directory",
        (String diseaseVariant) -> {
          webDriverHelpers.waitUntilIdentifiedElementIsPresent(FIRST_RECORD_DISEASE_VARIANT);
          softly.assertEquals(
              webDriverHelpers.getTextFromWebElement(FIRST_RECORD_DISEASE_VARIANT),
              diseaseVariant,
              "Disease variant is not empty");
          softly.assertAll();
        });

    And(
        "^I select \"([^\"]*)\" type of message in Message Directory page$",
        (String typeOfMessage) -> {
          webDriverHelpers.waitUntilIdentifiedElementIsPresent(TYPE_OF_MESSAGE_COMBOBOX);
          webDriverHelpers.selectFromCombobox(TYPE_OF_MESSAGE_COMBOBOX, typeOfMessage);
        });

    And(
        "^I check that all displayed messages have \"([^\"]*)\" in grid Message Directory Type column$",
        (String type) -> {
          webDriverHelpers.waitUntilAListOfElementsHasText(GRID_RESULTS_TYPE, type);
        });

    Then(
        "^I check if there is no displayed sample result on Edit case page$",
        () -> {
          softly.assertFalse(
              webDriverHelpers.isElementPresent(SAMPLES_CARD_LABORATORY), "Element is present!");
          softly.assertAll();
        });

    And(
        "I click next button while processing a {string} in DEMIS LabMessage",
        (String option) -> {
          if (webDriverHelpers.isElementVisibleWithTimeout(CASE_SAVED_POPUP_DE, 5)) {
            webDriverHelpers.clickOnWebElementBySelector(CASE_SAVED_POPUP_DE);
          }
          switch (option) {
            case "hospitalization":
              webDriverHelpers.waitUntilIdentifiedElementIsPresent(CURRENT_HOSPITALIZATION_HEADER);
              webDriverHelpers.scrollToElement(NEXT_BUTTON);
              webDriverHelpers.clickOnWebElementBySelector(NEXT_BUTTON);
              break;
            case "clinical measurement":
              webDriverHelpers.waitUntilIdentifiedElementIsPresent(CLINICAL_MEASUREMENT_HEADER);
              webDriverHelpers.scrollToElement(NEXT_BUTTON);
              webDriverHelpers.clickOnWebElementBySelector(NEXT_BUTTON);
              break;
            case "exposure investigation":
              webDriverHelpers.waitUntilIdentifiedElementIsPresent(EXPOSURE_INVESTIGATION_HEADER);
              webDriverHelpers.scrollToElement(NEXT_BUTTON);
              webDriverHelpers.clickOnWebElementBySelector(NEXT_BUTTON);
              webDriverHelpers.waitUntilIdentifiedElementIsPresent(ADD_VACCINATION_BUTTON);
              break;
          }
        });

    And(
        "I download {string} message from Message Directory page",
        (String message) -> {
          switch (message) {
            case "processed":
              webDriverHelpers.scrollToElement(DOWNLOAD_PROCESSED_BUTTON);
              webDriverHelpers.clickOnWebElementBySelector(DOWNLOAD_PROCESSED_BUTTON);
              break;
            case "unprocessed":
              webDriverHelpers.scrollToElement(DOWNLOAD_UNPROCESSED_BUTTON);
              webDriverHelpers.clickOnWebElementBySelector(DOWNLOAD_UNPROCESSED_BUTTON);
              break;
          }

          TimeUnit.SECONDS.sleep(5); // wait for download
        });

    And(
        "I verify if lab message file is downloaded correctly",
        () -> {
          String shortenedUUID = shortenedUUIDS.get(0);
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          String file =
              "sormas_lab_message_"
                  + shortenedUUID
                  + "_"
                  + formatter.format(LocalDate.now())
                  + ".pdf";
          FilesHelper.waitForFileToDownload(file, 40);
          FilesHelper.deleteFile(file);
        });

    And(
        "^I collect shortened message uuid from Message Directory page$",
        () -> {
          shortenedUUIDS.add(webDriverHelpers.getTextFromWebElement(GRID_MESSAGE_UUID_TITLE));
        });

    And(
        "I assign the Assignee to the message on Message Directory page",
        () -> {
          webDriverHelpers.clickOnWebElementBySelector(ASSIGN_BUTTON);
          webDriverHelpers.selectFromCombobox(EDIT_ASSIGNEE_FILTER_SELECT_BUTTON, "Ad MIN");
          TimeUnit.SECONDS.sleep(5);
          webDriverHelpers.clickOnWebElementBySelector(POPUP_WINDOW_SAVE_BUTTON);
        });

    And(
        "^I check that \"([^\"]*)\" is assigned to the message on Message Directory page$",
        (String assignee) -> {
          webDriverHelpers.refreshCurrentPage();
          softly.assertEquals(
              webDriverHelpers.getTextFromPresentWebElement(ASSIGNEE_LABEL),
              assignee,
              "Incorrect value is assigned to the assignee");
          softly.assertAll();
        });

    Then(
        "^I check if there are all needed buttons in HTML message file$",
        () -> {
          webDriverHelpers.scrollToElement(MESSAGE_DELETE_BUTTON);
          webDriverHelpers.waitUntilIdentifiedElementIsPresent(MESSAGE_DELETE_BUTTON);
          webDriverHelpers.waitUntilIdentifiedElementIsPresent(MARK_AS_UNCLEAR_BUTTON);
          webDriverHelpers.waitUntilIdentifiedElementIsPresent(MARK_AS_FORWARDED_BUTTON);
          webDriverHelpers.waitUntilIdentifiedElementIsPresent(SEND_TO_ANOTHER_ORGANIZATION_BUTTON);
        });

    And(
        "^I close HTML message$",
        () -> {
          webDriverHelpers.clickOnWebElementBySelector(CLOSE_POPUP);
          webDriverHelpers.waitForPageLoaded();
        });

    Then(
        "^I check if there are any buttons from processed message in HTML message file$",
        () -> {
          webDriverHelpers.scrollToElement(HEADER_OF_ENTRY_LINK);
          softly.assertFalse(
            webDriverHelpers.isElementPresent(MESSAGE_DELETE_BUTTON),
                  "Delete message is available!");
          softly.assertAll();
          softly.assertFalse(
                  webDriverHelpers.isElementPresent(MARK_AS_UNCLEAR_BUTTON),
                 "Delete message is available!");
          softly.assertAll();
          softly.assertFalse(
                  webDriverHelpers.isElementPresent(MARK_AS_FORWARDED_BUTTON),
                  "Delete message is available!");
          softly.assertAll();
          softly.assertFalse(
                  webDriverHelpers.isElementPresent(SEND_TO_ANOTHER_ORGANIZATION_BUTTON),
                  "Delete message is available!");
          softly.assertAll();
        });
  }
}
