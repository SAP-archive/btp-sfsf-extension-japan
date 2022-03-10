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
public class photoControllerTest {
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
    public void test_GET_NormalCase() throws Exception {
        RestAssuredMockMvc
                .given()
                .when()
                .get("/api/v4/ReshuffleService/Photos/104067")
                .then()
                .statusCode(200)
                .contentType("image/jpeg");
    }

    @Test
    public void test_GET_ErrorCase() throws Exception {
        RestAssuredMockMvc
                .given()
                .when()
                .get("/api/v4/ReshuffleService/Photos/")
                .then()
                .statusCode(404);
    }
}
