package vocanec.marino.urlshortener.controllers;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HomeControllerTests {

    @Test
    public void testHome() {
        given()
                .when()
                .get("/")
                .then()
                .contentType(ContentType.HTML)
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testHelp() {
        given()
                .when()
                .get("/help")
                .then()
                .contentType(ContentType.HTML)
                .statusCode(HttpStatus.SC_OK);
    }
}
