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
public class FileExportControllerTest {
    private static final MockUtil mockUtil = new MockUtil();

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
    public void test_POST_NormalCase_2entity() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"filename\":\"IntegrationTest\",\"entities\":[{\"candidateID\":\"VictorK\",\"positionID\":\"3000314\",\"incumbentEmpID\":\"Rhussin\"},{\"candidateID\":\"802981\",\"positionID\":\"3000967\",\"incumbentEmpID\":\"jandrina\"}]}")
                .when()
                .post("/api/v4/ReshuffleService/exportFile")
                .then()
                .statusCode(201)
                .contentType("application/zip");
    }

    @Test
    public void test_POST_NormalCase_only_candidateID() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"filename\":\"IntegrationTest\",\"entities\":[{\"candidateID\":\"VictorK\",\"incumbentEmpID\":\"null\"}]}")
                .when()
                .post("/api/v4/ReshuffleService/exportFile")
                .then()
                .statusCode(201)
                .contentType("application/zip");
    }

    @Test
    public void test_POST_NormalCase_only_positionID() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"filename\":\"IntegrationTest\",\"entities\":[{\"positionID\":\"3000314\"}]}")
                .when()
                .post("/api/v4/ReshuffleService/exportFile")
                .then()
                .statusCode(201)
                .contentType("application/zip");
    }

    @Test
    public void test_POST_NormalCase_without_Filename() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"entities\":[{\"candidateID\":\"VictorK\",\"positionID\":\"3000314\",\"incumbentEmpID\":\"Rhussin\"},{\"candidateID\":\"802981\",\"positionID\":\"3000967\",\"incumbentEmpID\":\"jandrina\"}]}")
                .when()
                .post("/api/v4/ReshuffleService/exportFile")
                .then()
                .statusCode(201)
                .contentType("application/zip");
    }
}
