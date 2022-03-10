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

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // Tests are run in ascending order by method name.
public class CandidateServiceTest {
    private static final MockUtil mockUtil = new MockUtil();
    private static final JsonSchemaValidator jsonValidator1 = JsonSchemaValidator
            .matchesJsonSchemaInClasspath("Candidate-schema-pattern1.json");
    private static final JsonSchemaValidator jsonValidator2 = JsonSchemaValidator
            .matchesJsonSchemaInClasspath("Candidate-schema-pattern2.json");

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
    public void test01_POST_NormalCase_Maximum() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"caseID\":\"forBackendIntegrationTest\",\"positionID\":\"50023041\",\"positionName\":\"ITビジネスパートナ\",\"divisionID\":\"CORP_SVCS\",\"divisionName\":\"コーポレートサービス本部\",\"departmentID\":\"50150001\",\"departmentName\":\"人事(US)\",\"jobGradeID\":\"GR-07\",\"jobGradeName\":\"SalaryGrade07\",\"candidateID\":\"VictorK\",\"candidateName\":\"KrisVictor\",\"candidateDivisionID\":\"MANU\",\"candidateDivisionName\":\"生産本部\",\"candidateDepartmentID\":\"5000011\",\"candidateDepartmentName\":\"業務推進部(SG)\",\"candidatePositionID\":\"3000250\",\"candidatePositionName\":\"品質保証ディレクタ\",\"candidateJobGradeID\":\"GR-18\",\"candidateJobGradeName\":\"SalaryGrade18\",\"candidateJobTenure\":12,\"candidateLastRating1\":null,\"candidateLastRating2\":null,\"candidateLastRating3\":\"0\",\"candidateManagerID\":\"ChenM\",\"incumbentEmpID\":\"\",\"incumbentEmpName\":\"MiloDixon\",\"incumbentEmpRetirementIntention\":null,\"incumbentEmpManagerID\":\"108736\"}")
                // All information will be overwritten except for the following columns: caseID, positionID, CandidateID, IncumbentID
                .when()
                .post("/odata/v4/ReshuffleService/Candidates")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON);
    }

    @Test
    public void test02_GET_NormalCase() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/odata/v4/ReshuffleService/Candidates?$filter=(caseID eq 'forBackendIntegrationTest')")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator1);
    }

    @Test
    public void test03_PUT_NormalCase() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"caseID\":\"forBackendIntegrationTest\",\"positionID\":\"50023041\",\"positionName\":\"ITビジネスパートナ\",\"divisionID\":\"CORP_SVCS\",\"divisionName\":\"コーポレートサービス本部\",\"departmentID\":\"50150001\",\"departmentName\":\"人事(US)\",\"jobGradeID\":\"GR-07\",\"jobGradeName\":\"SalaryGrade07\",\"candidateID\":\"VictorK\",\"candidateName\":\"KrisVictor\",\"candidateDivisionID\":\"MANU\",\"candidateDivisionName\":\"生産本部\",\"candidateDepartmentID\":\"5000011\",\"candidateDepartmentName\":\"業務推進部(SG)\",\"candidatePositionID\":\"3000250\",\"candidatePositionName\":\"品質保証ディレクタ\",\"candidateJobGradeID\":\"GR-18\",\"candidateJobGradeName\":\"SalaryGrade18\",\"candidateJobTenure\":12,\"candidateLastRating1\":null,\"candidateLastRating2\":null,\"candidateLastRating3\":\"0\",\"candidateManagerID\":\"ChenM\",\"incumbentEmpID\":\"108737\",\"incumbentEmpName\":\"MiloDixon\",\"incumbentEmpRetirementIntention\":null,\"incumbentEmpManagerID\":\"108736\"}")
                .when()
                .put("/odata/v4/ReshuffleService/Candidates(caseID='forBackendIntegrationTest',positionID='50023041',candidateID='VictorK')")
                .then()
                .statusCode(204);
    }

    @Test
    public void test040_GET_NormalCase_After_PUT() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/odata/v4/ReshuffleService/Candidates?$filter=(caseID eq 'forBackendIntegrationTest')")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(jsonValidator2);
    }

    @Test
    public void test045_POST_NormalCase_Minimum_with_hyphen() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"caseID\":\"forBackendIntegration-Test\",\"positionID\":\"3000967\",\"incumbentEmpID\":\"jandrina\",\"candidateID\":\"802981\"}")
                .when()
                .post("/odata/v4/ReshuffleService/Candidates")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON);
    }

    @Test
    public void test046_GET_NormalCase_Minimum_with_hyphen() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/odata/v4/ReshuffleService/Candidates?$filter=(caseID eq 'forBackendIntegration-Test')")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void test05_DELETE_NormalCase() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/odata/v4/ReshuffleService/Candidates(caseID='forBackendIntegrationTest',positionID='50023041',candidateID='VictorK')")
                .then()
                .statusCode(204);
    }

    @Test
    public void test060_POST_ErrorCase_CandidateID_Duplicate_CaseID() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"caseID\":\"forBackendIntegrationTest\",\"positionID\":\"3000250\",\"positionName\":\"ITビジネスパートナ\",\"divisionID\":\"CORP_SVCS\",\"divisionName\":\"コーポレートサービス本部\",\"departmentID\":\"50150001\",\"departmentName\":\"人事(US)\",\"jobGradeID\":\"GR-07\",\"jobGradeName\":\"SalaryGrade07\",\"candidateID\":\"null\",\"candidateName\":\"KrisVictor\",\"candidateDivisionID\":\"MANU\",\"candidateDivisionName\":\"生産本部\",\"candidateDepartmentID\":\"5000011\",\"candidateDepartmentName\":\"業務推進部(SG)\",\"candidatePositionID\":\"3000250\",\"candidatePositionName\":\"品質保証ディレクタ\",\"candidateJobGradeID\":\"GR-18\",\"candidateJobGradeName\":\"SalaryGrade18\",\"candidateJobTenure\":12,\"candidateLastRating1\":null,\"candidateLastRating2\":null,\"candidateLastRating3\":\"0\",\"candidateManagerID\":\"ChenM\",\"incumbentEmpID\":\"108737\",\"incumbentEmpName\":\"MiloDixon\",\"incumbentEmpRetirementIntention\":null,\"incumbentEmpManagerID\":\"108736\"}")
                .when()
                .post("/odata/v4/ReshuffleService/Candidates")
                .then()
                .statusCode(400);
    }

    @Test
    public void test065_POST_ErrorCase_CandidateID_Null() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"caseID\":\"forBackendIntegrationTest_ErrorCase\",\"positionID\":\"3000250\",\"positionName\":\"ITビジネスパートナ\",\"divisionID\":\"CORP_SVCS\",\"divisionName\":\"コーポレートサービス本部\",\"departmentID\":\"50150001\",\"departmentName\":\"人事(US)\",\"jobGradeID\":\"GR-07\",\"jobGradeName\":\"SalaryGrade07\",\"candidateID\":\"null\",\"candidateName\":\"KrisVictor\",\"candidateDivisionID\":\"MANU\",\"candidateDivisionName\":\"生産本部\",\"candidateDepartmentID\":\"5000011\",\"candidateDepartmentName\":\"業務推進部(SG)\",\"candidatePositionID\":\"3000250\",\"candidatePositionName\":\"品質保証ディレクタ\",\"candidateJobGradeID\":\"GR-18\",\"candidateJobGradeName\":\"SalaryGrade18\",\"candidateJobTenure\":12,\"candidateLastRating1\":null,\"candidateLastRating2\":null,\"candidateLastRating3\":\"0\",\"candidateManagerID\":\"ChenM\",\"incumbentEmpID\":\"108737\",\"incumbentEmpName\":\"MiloDixon\",\"incumbentEmpRetirementIntention\":null,\"incumbentEmpManagerID\":\"108736\"}")
                .when()
                .post("/odata/v4/ReshuffleService/Candidates")
                .then()
                .statusCode(400);
    }

    @Test
    public void test070_POST_ErrorCase_PositionID_Invalid() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"caseID\":\"forBackendIntegrationTest_ErrorCase\",\"positionID\":\"invalid\",\"positionName\":\"ITビジネスパートナ\",\"divisionID\":\"CORP_SVCS\",\"divisionName\":\"コーポレートサービス本部\",\"departmentID\":\"50150001\",\"departmentName\":\"人事(US)\",\"jobGradeID\":\"GR-07\",\"jobGradeName\":\"SalaryGrade07\",\"candidateID\":\"VictorK\",\"candidateName\":\"KrisVictor\",\"candidateDivisionID\":\"MANU\",\"candidateDivisionName\":\"生産本部\",\"candidateDepartmentID\":\"5000011\",\"candidateDepartmentName\":\"業務推進部(SG)\",\"candidatePositionID\":\"3000250\",\"candidatePositionName\":\"品質保証ディレクタ\",\"candidateJobGradeID\":\"GR-18\",\"candidateJobGradeName\":\"SalaryGrade18\",\"candidateJobTenure\":12,\"candidateLastRating1\":null,\"candidateLastRating2\":null,\"candidateLastRating3\":\"0\",\"candidateManagerID\":\"ChenM\",\"incumbentEmpID\":\"108737\",\"incumbentEmpName\":\"MiloDixon\",\"incumbentEmpRetirementIntention\":null,\"incumbentEmpManagerID\":\"108736\"}")
                .when()
                .post("/odata/v4/ReshuffleService/Candidates")
                .then()
                .statusCode(400);
    }

    @Test
    public void test075_POST_ErrorCase_Without_caseID() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"positionID\":\"3000967\",\"incumbentEmpID\":\"jandrina\",\"candidateID\":\"802981\"}")
                .when()
                .post("/odata/v4/ReshuffleService/Candidates")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON);
    }

    @Test
    public void test08_GET_ErrorCase_CaseID_Invalid() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/odata/v4/ReshuffleService/Candidates?$filter=(caseID eq 'forBackendIntegrationTest_ErrorCase')")
                .then()
                .statusCode(200);
    }

    @Test
    public void test09_PUT_ErrorCase_NotExist() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"caseID\":\"forBackendIntegrationTest_ErrorCase\",\"positionID\":\"50023041\",\"positionName\":\"ITビジネスパートナ\",\"divisionID\":\"CORP_SVCS\",\"divisionName\":\"コーポレートサービス本部\",\"departmentID\":\"50150001\",\"departmentName\":\"人事(US)\",\"jobGradeID\":\"GR-07\",\"jobGradeName\":\"SalaryGrade07\",\"candidateID\":\"VictorK\",\"candidateName\":\"KrisVictor\",\"candidateDivisionID\":\"MANU\",\"candidateDivisionName\":\"生産本部\",\"candidateDepartmentID\":\"5000011\",\"candidateDepartmentName\":\"業務推進部(SG)\",\"candidatePositionID\":\"3000250\",\"candidatePositionName\":\"品質保証ディレクタ\",\"candidateJobGradeID\":\"GR-18\",\"candidateJobGradeName\":\"SalaryGrade18\",\"candidateJobTenure\":12,\"candidateLastRating1\":null,\"candidateLastRating2\":null,\"candidateLastRating3\":\"0\",\"candidateManagerID\":\"ChenM\",\"incumbentEmpID\":\"108737\",\"incumbentEmpName\":\"MiloDixon\",\"incumbentEmpRetirementIntention\":null,\"incumbentEmpManagerID\":\"108736\"}")
                .when()
                .put("/odata/v4/ReshuffleService/Candidates(caseID='forBackendIntegrationTest',positionID='3000250',candidateID='103227')")
                .then()
                .statusCode(400);
    }

    @Test
    public void test10_PUT_ErrorCase_Conflict_Between_IncumbentEmpID_and_PositionID() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body("{\"caseID\":\"forBackendIntegrationTest\",\"positionID\":\"50023041\",\"positionName\":\"ITビジネスパートナ\",\"divisionID\":\"CORP_SVCS\",\"divisionName\":\"コーポレートサービス本部\",\"departmentID\":\"50150001\",\"departmentName\":\"人事(US)\",\"jobGradeID\":\"GR-07\",\"jobGradeName\":\"SalaryGrade07\",\"candidateID\":\"VictorK\",\"candidateName\":\"KrisVictor\",\"candidateDivisionID\":\"MANU\",\"candidateDivisionName\":\"生産本部\",\"candidateDepartmentID\":\"5000011\",\"candidateDepartmentName\":\"業務推進部(SG)\",\"candidatePositionID\":\"3000250\",\"candidatePositionName\":\"品質保証ディレクタ\",\"candidateJobGradeID\":\"GR-18\",\"candidateJobGradeName\":\"SalaryGrade18\",\"candidateJobTenure\":12,\"candidateLastRating1\":null,\"candidateLastRating2\":null,\"candidateLastRating3\":\"0\",\"candidateManagerID\":\"ChenM\",\"incumbentEmpID\":\"108734\",\"incumbentEmpName\":\"MiloDixon\",\"incumbentEmpRetirementIntention\":null,\"incumbentEmpManagerID\":\"108736\"}")
                .when()
                .put("/odata/v4/ReshuffleService/Candidates(caseID='forBackendIntegrationTest',positionID='50023041',candidateID='VictorK')")
                .then()
                .statusCode(500);
    }

    @Test
    public void test11_DELETE_ErrorCase_NotExist() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/odata/v4/ReshuffleService/Candidates(caseID='forBackendIntegrationTest',positionID='3000250',candidateID='103227')")
                .then()
                .statusCode(404);
    }

    @Test
    public void test12_DELETE_NormalCase_Cleaning() throws Exception {
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/odata/v4/ReshuffleService/Candidates(caseID='forBackendIntegration-Test',positionID='3000967',candidateID='802981')")
                .then()
                .statusCode(204);
    }
}
