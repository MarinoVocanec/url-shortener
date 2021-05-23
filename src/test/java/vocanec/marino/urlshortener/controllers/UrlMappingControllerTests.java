package vocanec.marino.urlshortener.controllers;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
public class UrlMappingControllerTests {

    private static final Map<String, String> USER_CREDENTIALS = new HashMap<>();

    @Before
    public void createAccounts() {
        if(USER_CREDENTIALS.isEmpty()) {
            createAccountForAccountId("UserA");
            createAccountForAccountId("UserB");
        }
    }

    private String createAuthorizationValue(String accountId) {
        byte[] bytes = Base64.getEncoder().encode((accountId + ":" + USER_CREDENTIALS.get(accountId)).getBytes());
        return "Basic " + new String(bytes);
    }

    private void createAccountForAccountId(String accountId) {
        JSONObject requestParameters = new JSONObject();
        requestParameters.put("accountId", accountId);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestParameters.toJSONString())
                .post("/account");

        Assert.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
        USER_CREDENTIALS.put(accountId, response.jsonPath().getString("password"));
    }

    private String createShortUrl(String accountId, String url) {
        return createShortUrl(accountId, url, 302);
    }

    private String createShortUrl(String accountId, String url, int redirectType) {
        JSONObject requestParameters = new JSONObject();
        requestParameters.put("url", url);
        requestParameters.put("redirectType", redirectType);

        Response response = given()
                .header("Authorization", createAuthorizationValue(accountId))
                .contentType(ContentType.JSON)
                .body(requestParameters.toJSONString())
                .post("/register");

        Assert.assertEquals(HttpStatus.SC_CREATED, response.statusCode());
        return response.jsonPath().getString("shortUrl");
    }

    private void assertInvalidUrlMappingRequest(String accountId, String url, int redirectType, String errorMessage) {
        JSONObject requestParameters = new JSONObject();
        requestParameters.put("url", url);
        requestParameters.put("redirectType", redirectType);

        given()
                .contentType(ContentType.JSON)
                .body(requestParameters.toJSONString())
                .header("Authorization", createAuthorizationValue(accountId))
                .when()
                .post("/register")
                .then()
                .contentType(ContentType.JSON)
                .body("errorMessage", equalTo(errorMessage))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    private void assertUnauthorizedStatisticRequest(String accountId) {
        given()
                .when()
                .get("/statistic/" + accountId)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    private void assertStatisticRequest(String accountId, String statisticAccountId, int httpStatus) {
        given()
                .header("Authorization", createAuthorizationValue(accountId))
                .when()
                .get("/statistic/" + statisticAccountId)
                .then()
                .statusCode(httpStatus);
    }

    private void assertValidRedirection(String shortUrl) {
        given()
                .when()
                .get(shortUrl)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void unauthorizedRegister() {
        given()
                .when()
                .post("/register")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void emptyUrl() {
        assertInvalidUrlMappingRequest("UserA", "", 302, "url was not provided.");
    }

    @Test
    public void invalidUrls() {
        for(String invalidUrl : Arrays.asList("abc", "url:", "htp://1245:", "*q123", "https:/maps.google.com")) {
            assertInvalidUrlMappingRequest("UserA", invalidUrl, 302,
                    "Provided input is not a valid url. Was: " + invalidUrl);
        }
    }

    @Test
    public void invalidRedirectTypes() {
        for(int redirectType : Arrays.asList(-1, 0, 1, 2, 3, 300, 303, 304)) {
            assertInvalidUrlMappingRequest("UserB", "https://www.google.com", redirectType,
                    "Redirect type must be 301 or 302. Was: " + redirectType);
        }
    }

    @Test
    public void duplicateUrl() {
        createShortUrl("UserA", "https://www.youtube.com");
        assertInvalidUrlMappingRequest("UserA", "https://www.youtube.com", 302,
                "Mapping for given URL already exists.");
    }

    @Test
    public void differentUserSameUrl() {
        createShortUrl("UserA", "http://facebook.com");
        createShortUrl("UserB", "http://facebook.com");
    }

    @Test
    public void redirection() {
        String shortUrl = createShortUrl("UserA", "http://127.0.0.1:8080", 301);
        assertValidRedirection(shortUrl);
    }

    @Test
    public void unauthorizedStatistic() {
        assertUnauthorizedStatisticRequest("UserA");
    }

    @Test
    public void forbiddenStatistic() {
        assertStatisticRequest("UserA", "UserB", HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void numberOfUrls() {
        String accountId = "UserC";
        createAccountForAccountId(accountId);
        List<String> urls = Arrays.asList("https://www.google.com", "https://www.youtube.com", "http://facebook.com");
        for(String url : urls) {
            createShortUrl(accountId, url);
        }

        Response response = given()
                .header("Authorization", createAuthorizationValue(accountId))
                .get("/statistic/" + accountId);
        Map<Object, Object> responseMap = response.jsonPath().getMap("$"); // $ == root element

        Assert.assertEquals(urls.size(), responseMap.size());
        for(String url : urls) {
            Assert.assertTrue(responseMap.containsKey(url));
        }
    }

    @Test
    public void numberOfVisits() {
        String accountId = "UserA";
        String url = "http://127.0.0.1:8080/help";
        String shortUrl = createShortUrl(accountId, url, 301);

        int numberOfVisits = 10;
        for(int i = 0; i < numberOfVisits; i++) {
            assertValidRedirection(shortUrl);
        }

        Response response = given()
                .header("Authorization", createAuthorizationValue(accountId))
                .get("/statistic/" + accountId);
        Map<Object, Object> responseMap = response.jsonPath().getMap("$"); // $ == root element
        Assert.assertEquals(numberOfVisits, responseMap.get(url));
    }
}
