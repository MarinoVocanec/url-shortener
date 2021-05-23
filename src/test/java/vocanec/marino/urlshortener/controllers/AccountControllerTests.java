package vocanec.marino.urlshortener.controllers;

import com.jayway.restassured.http.ContentType;
import net.minidev.json.JSONObject;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasLength;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
public class AccountControllerTests {

    private void assertBadRequest(String accountId, String description) {
        JSONObject requestParameters = new JSONObject();
        requestParameters.put("accountId", accountId);
        given()
                .contentType(ContentType.JSON)
                .body(requestParameters.toJSONString())
                .when()
                .post("/account")
                .then()
                .contentType(ContentType.JSON)
                .body("success", equalTo(false))
                .body("description", equalTo(description))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    private void assertValidRequest(String accountId) {
        JSONObject requestParameters = new JSONObject();
        requestParameters.put("accountId", accountId);
        given()
                .contentType(ContentType.JSON)
                .body(requestParameters.toJSONString())
                .when()
                .post("/account")
                .then()
                .contentType(ContentType.JSON)
                .body("success", equalTo(true))
                .body("description", equalTo("Your account is opened."))
                .body("password", hasLength(8))
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void emptyAccountId() {
        assertBadRequest("", "accountId was not provided.");
    }

    @Test
    public void invalidAccountId() {
        final String invalidCharactersMessage = "accountId can contain only alphanumeric characters.";
        assertBadRequest("Name Surname", invalidCharactersMessage);
        assertBadRequest("Name+Surname", invalidCharactersMessage);
        assertBadRequest("Name*Surname", invalidCharactersMessage);
        assertBadRequest("Name-Surname", invalidCharactersMessage);
        assertBadRequest("Name_Surname", invalidCharactersMessage);
        assertBadRequest("Name/Surname", invalidCharactersMessage);
        assertBadRequest("Name?Surname", invalidCharactersMessage);
        assertBadRequest("Name#Surname", invalidCharactersMessage);
    }

    @Test
    public void longAccountId() {
        assertBadRequest("A".repeat(101), "accountId length cannot be longer than 100, was: 101.");
        assertBadRequest("A".repeat(1000), "accountId length cannot be longer than 100, was: 1000.");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void validAccountId() {
        assertValidRequest("NameSurname");
        assertValidRequest("User42");
        assertValidRequest("U123");
        assertValidRequest("u123");
        assertValidRequest("512");
        assertValidRequest("512User");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void existingAccountId() {
        assertValidRequest("NameSurname");
        assertBadRequest("NameSurname", "Account with accountId: 'NameSurname' already exists.");
    }
}
