package com.sap.sfsf.reshuffle.applicants.backend;

import com.sap.cloud.sdk.testutil.MockUtil;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FilterServiceTest {

    private static final MockUtil mockUtil = new MockUtil();
    private static final JsonSchemaValidator jsonValidator = JsonSchemaValidator
            .matchesJsonSchemaInClasspath("Filter-schema.json");

    @Autowired
    private MockMvc mvc;

    @BeforeClass
    public static void beforeClass() {
        String destinationName = "SFSF";
        String alias = "internal";

        mockUtil.mockDefaults();
        mockUtil.mockDestination(destinationName, alias);
    }

    @Before
    public void before() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    public void test_POST_NormalCase_Companies() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"arg\": \"companies\"}")
                .when()
                .post("/odata/v4/ReshuffleService/getFilter")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator);
    }

    @Test
    public void test_POST_NormalCase_Businessunits() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"arg\": \"businessunits\"}")
                .when()
                .post("/odata/v4/ReshuffleService/getFilter")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator);
    }

    @Test
    public void test_POST_NormalCase_Divisions() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"arg\": \"divisions\"}")
                .when()
                .post("/odata/v4/ReshuffleService/getFilter")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator);
    }

    @Test
    public void test_POST_NormalCase_Departments() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"arg\": \"departments\"}")
                .when()
                .post("/odata/v4/ReshuffleService/getFilter")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator);
    }

    @Test
    public void test_POST_NormalCase_Positions() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"arg\": \"positions\"}")
                .when()
                .post("/odata/v4/ReshuffleService/getFilter")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator);
    }

    @Test
    public void test_POST_NormalCase_Caseids() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"arg\": \"caseids\"}")
                .when()
                .post("/odata/v4/ReshuffleService/getFilter")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator);
    }

    @Test
    public void test_POST_ErrorCase_Invalid_Argument() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"arg\": \"invalid\"}")
                .when()
                .post("/odata/v4/ReshuffleService/getFilter")
                .then()
                .statusCode(400);
    }
}
