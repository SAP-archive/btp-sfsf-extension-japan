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
public class CurrentPositionEmployeeServiceTest {

    private static final MockUtil mockUtil = new MockUtil();

    private static final JsonSchemaValidator jsonValidator_pattern1 = JsonSchemaValidator
            .matchesJsonSchemaInClasspath("CurrentPosition-schema-pattern1.json");
    private static final JsonSchemaValidator jsonValidator_pattern2 = JsonSchemaValidator
            .matchesJsonSchemaInClasspath("CurrentPosition-schema-pattern2.json");
    private static final JsonSchemaValidator jsonValidator_pattern3 = JsonSchemaValidator
            .matchesJsonSchemaInClasspath("CurrentPosition-schema-pattern3.json");

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
                .when()
                .get("/odata/v4/ReshuffleService/CurrentPositionEmployees?$filter=(divisionID eq 'DIR_SALES') and (departmentID eq '5000102') and (positionID eq '3000233')")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator_pattern1);
    }

    @Test
    public void test_GET_NormalCase_Minimum() throws Exception {
        RestAssuredMockMvc
                .given()
                .when()
                .get("/odata/v4/ReshuffleService/CurrentPositionEmployees?$filter=(divisionID eq 'MANU') and (departmentID eq '5000011')")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator_pattern2);
    }

    @Test
    public void test_GET_NormalCase_Maximum() throws Exception {
        RestAssuredMockMvc
                .given()
                .when()
                .get("/odata/v4/ReshuffleService/CurrentPositionEmployees?$filter=(companyID eq '5100') and (businessUnitID eq 'PRODS') and (divisionID eq 'MANU') and (departmentID eq '5000011') and (positionID eq '3000381') and (empJobTenure ge 3) and (empJobTenure le 6)")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator_pattern3);
    }

    @Test
    public void test_GET_ErrorCase_Without_Query() throws Exception {
        RestAssuredMockMvc
                .given()
                .when()
                .get("/odata/v4/ReshuffleService/CurrentPositionEmployees")
                .then()
                .statusCode(400);
    }

    @Test
    public void test_GET_ErrorCase_Without_DepartmentID() throws Exception {
        RestAssuredMockMvc
                .given()
                .when()
                .get("/odata/v4/ReshuffleService/CurrentPositionEmployees?$filter=(divisionID eq 'MANU')")
                .then()
                .statusCode(400);
    }

    @Test
    public void test_GET_ErrorCase_Invalid_DivisionID() throws Exception {
        RestAssuredMockMvc
                .given()
                .when()
                .get("/odata/v4/ReshuffleService/CurrentPositionEmployees?$filter=(divisionID eq 'Invalid') and (departmentID eq '5000011')")
                .then()
                .statusCode(200);
    }
}
