package se.docker.alpine.build.gateway.api.v1;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Nested;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Base64;


@DisplayName("Restful tests")
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("unittest")
public class PackageApiTest
{
    public static final String MY_PACKAGE = "myPackage";
    public static final String MY_PACKAGE_BASE64 = "bXlQYWNrYWdl";

    @DisplayName("Creates a member resource in the collection resource. " +
            "The URI of the created member resource is automatically assigned and returned " +
            "in the response Location header field. ")
    @Test
    @Order(1)
    public void testCollectionPostPackage()
    {
        given()
                .when().post("/v1/packages")
                .then()
                .header("Location", matchesPattern("http://localhost:[0-9]+/v1/packages/1"))
                .statusCode(201);
    }

    @DisplayName("Retrieves the URIs of the member resources of the collection resource in the response body")
    @Test
    @Order(2)
    public void testCollectionGetPackage()
    {
        given()
                .when().get("/v1/packages")
                .then()
                .statusCode(200)
                .body(matchesPattern("\\[http://localhost:[0-9]+/v1/packages/1\\]"));
    }


    @DisplayName("Replaces all the representations of the member tarball resource or create the member resource if it does not exist, with the representation in the request body.")
    @Test
    @Order(10)
    public void testMemberPutTarBallPackage()
    {
        String originalInput = "test input";
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());

        given()
         //       .multiPart("controlName2", "my_file_name.txt", MY_PACKAGE_BASE64)
       // .multiPart(new File("/tmp/apa.txt"))
                .body(encodedString)
                .when().put("/v1/packages/1/tarball")
                .then()
                .statusCode(200);
    }

    @DisplayName("Replaces all the representations of the member name resource or create the member resource if it does not exist, with the representation in the request body.")
    @Test
    @Order(12)
    public void testMemberPutNamePackage()
    {
        given()
                .body(MY_PACKAGE)
                .when().put("/v1/packages/1/name")
                .then()
                .statusCode(200);
    }

    @DisplayName("Replaces all the representations of the member resource or create the member resource if it does not exist, with the representation in the request body.")
    @Test
    @Order(13)
    public void testMemberGetNamePackage()
    {
        given()
                .when()
                .get("/v1/packages/1/name")
                .then()
                .body(is(MY_PACKAGE))
                .statusCode(200);
    }

    @DisplayName("Retrieves a representation of the member resource in the response body")
    @Test
    @Order(14)
    public void testMemberGetPackage()
    {
        given()
                .when().get("/v1/packages/1")
                .then()
                .statusCode(200)
                .body(is("{\"name\"" +
                        ":\"" + MY_PACKAGE + "\"}"));
    }

    @DisplayName("Replace all the representations of the member resources of the collection resource with the representation in the request body, or create the collection resource if it does not exist.")
    @Test
    @Order(30)
    public void testCollectionPutPackage()
    {
        given()
                .when().put("/v1/packages/1")
                .then()
                .header("Location", matchesPattern("http://localhost:[0-9]+/v1/packages/1"))
                .statusCode(201);
    }

    @DisplayName("Retrieves a representation of the member resource in the response body")
    @Test
    @Order(31)
    public void testMemberGetNameEmptyPackage()
    {
        given()
                .when()
                .get("/v1/packages/1/name")
                .then()
                .body(is(""))
                .statusCode(200);
    }


}