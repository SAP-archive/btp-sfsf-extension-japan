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
public class CompetenciesServiceTest {

    private static final MockUtil mockUtil = new MockUtil();
    private static final JsonSchemaValidator jsonValidator = JsonSchemaValidator
            .matchesJsonSchemaInClasspath("Competency-schema.json");

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
    public void test_GET_NormalCase() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/odata/v4/ReshuffleService/Competencies?$filter=(currentPositionID eq '50014345')")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator);
    }

    @Test
    public void test_GET_ErrorCase_Invalid_CurrentPositionID() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/odata/v4/ReshuffleService/Competencies?$filter=(currentPositionID eq 'Invalid')")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void test_GET_ErrorCase_Invalid_Query() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/odata/v4/ReshuffleService/Competencies?$filter=(invalid eq 'invalid')")
                .then()
                .statusCode(400);
    }
}
