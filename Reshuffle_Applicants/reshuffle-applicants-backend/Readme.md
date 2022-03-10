# java-sdk-with-sfsf-demo
注意：本プロジェクトのソースコードの最新版は下記で管理しています。
[Reshuffle_Applicants_jenkins](https://github.tools.sap/jp-sfsf-staffreshuffle/Reshuffle_Applicants_jenkins)
## API Ref
### Path (Dev env)
- https://backend-service-kyoneo-mock.cfapps.jp10.hana.ondemand.com
### GET /CurrentPositionEmployees
- ex
  - /odata/v4/ReshuffleService/CurrentPositionEmployees?$filter=(divisionID eq 'DIR_SALES') and (departmentID eq '5000102') and (positionID eq '3000233')

####  Parameters
| isMandatory | Key            | ValueType | Operand | Example     |
| ----------- | -------------- | --------- | ------- | ----------- |
|             | companyID      | String    | eq      | "5100"      |
|             | businessUnitID | String    | eq      | "SALES"     |
| true        | divisionID     | String    | eq      | "DIR_SALES" |
| true        | departmentID   | String    | eq      | "51505150"  |
|             | positionID     | String    | eq      | "MANU"      |
|             | empJobTenure   | Integer   | ge      | 3           |
|             | empJobTenure   | Integer   | le      | 6           |
|             | willingness    | String    | eq      | "yes"       |


### GET /NextPosition
- ex
  - /odata/v4/ReshuffleService/NextPositions?$filter=(division eq 'DIR_SALES') and (department eq '5000102')

####  Parameters
| isMandatory | Key            | ValueType | Example     |
| ----------- | -------------- | --------- | ----------- |
|             | companyID      | String    | "5100"      |
|             | businessUnitID | String    | "SALES"     |
| true        | divisionID     | String    | "DIR_SALES" |
| true        | departmentID   | String    | "51505150"  |
|             | positionID     | String    | "MANU"      |
|             | empRetire      | String    | "yes"       |

### GET /Candidate
- ex
  - /odata/v4/ReshuffleService/Candidates?$filter=caseid eq 'yoneoTest'

####  Parameters
| isMandatory | Key    | ValueType | Example     |
| ----------- | ------ | --------- | ----------- |
| true        | caseID | String    | "yoneoTest" |

### POST /Candidate
- ex
  - /odata/v4/ReshuffleService/$batch/

#### Tips
- caseID、candidateID、incumbentEmpID以外の値は、その３つの値を元にDBに登録されているデータを取得し、その値に上書きして保存する。そのため、UI側で値をいじっても（不正な値を入れても）修正されます。

#### Request Body Example
```
--batch_readme1225
Content-Type: multipart/mixed; boundary=changeset_6221-4db5-5842

--changeset_6221-4db5-5842
Content-Type: application/http

POST Candidates HTTP/1.1
Content-Type: application/json
Content-ID: id-1644217249975-223

{"caseID":"case-README","positionID":"50023041","positionName":"ITビジネスパートナ","divisionID":"CORP_SVCS","divisionName":"コーポレートサービス本部","departmentID":"50150001","departmentName":"人事(US)","jobGradeID":"GR-07","jobGradeName":"SalaryGrade07","candidateID":"VictorK","candidateName":"KrisVictor","candidateDivisionID":"MANU","candidateDivisionName":"生産本部","candidateDepartmentID":"5000011","candidateDepartmentName":"業務推進部(SG)","candidatePositionID":"3000250","candidatePositionName":"品質保証ディレクタ","candidateJobGradeID":"GR-18","candidateJobGradeName":"SalaryGrade18","candidateJobTenure":12,"candidateLastRating1":null,"candidateLastRating2":null,"candidateLastRating3":"0","candidateTransferTimes":null,"candidateTransferReason":null,"candidateCertification":null,"candidateReshuffleCost":null,"candidateManagerID":"ChenM","incumbentEmpID":"108737","incumbentEmpName":"MiloDixon","incumbentEmpRetirementIntention":null,"incumbentEmpManager":"108736","simulationCheckStatus":null,"simulationCheckDatetime":null,"wfStatus":null,"mailSentFlg":"送信済","mailSentAt":"2022-02-08T01:39:53Z","sfUpsertFlg":null,"sfUpsertAt":null}

--changeset_6221-4db5-5842
Content-Type: application/http

POST Candidates HTTP/1.1
Content-Type: application/json
Content-ID: id-1644217249975-224

{"caseID":"case-README","positionID":"50014275","positionName":"最高執行責任者","divisionID":"EXEC","divisionName":"社長室","departmentID":"50007725","departmentName":"グローバル運用","jobGradeID":"GR-14","jobGradeName":"SalaryGrade14","candidateID":"VictorK","candidateName":"KrisVictor","candidateDivisionID":"MANU","candidateDivisionName":"生産本部","candidateDepartmentID":"5000011","candidateDepartmentName":"業務推進部(SG)","candidatePositionID":"3000250","candidatePositionName":"品質保証ディレクタ","candidateJobGradeID":"GR-18","candidateJobGradeName":"SalaryGrade18","candidateJobTenure":12,"candidateLastRating1":null,"candidateLastRating2":null,"candidateLastRating3":"0","candidateTransferTimes":null,"candidateTransferReason":null,"candidateCertification":null,"candidateReshuffleCost":null,"candidateManagerID":"ChenM","incumbentEmpID":"80281","incumbentEmpName":"MichaelPittman","incumbentEmpRetirementIntention":null,"incumbentEmpManager":"890223","simulationCheckStatus":null,"simulationCheckDatetime":null,"wfStatus":null,"mailSentFlg":"送信済","mailSentAt":"2022-02-08T01:39:53Z","sfUpsertFlg":null,"sfUpsertAt":null}

--changeset_6221-4db5-5842--

--batch_readme1225--
```

### PUT /Candidate
- ex
  - /odata/v4/ReshuffleService/$batch/

#### Tips
- caseID、candidateID、incumbentEmpID以外の値は、その３つの値を元にDBに登録されているデータを取得し、その値に上書きして保存する。そのため、UI側で値をいじっても（不正な値を入れても）修正されます。
- POSTでリクエストしてください。
- 下記の通り、更新処理を行う場合はchangesetの最初にDELETEリクエストを含めてください。

#### Request Body Example
```
--batch_readme1225
Content-Type: multipart/mixed; boundary=changeset_6221-4db5-5842

--changeset_6221-4db5-5842
Content-Type: application/http

DELETE Candidates?$filter=(caseID eq 'case-README') HTTP/1.1

--changeset_6221-4db5-5842
Content-Type: application/http

PUT Candidates HTTP/1.1
Content-Type: application/json
Content-ID: id-1644217249975-223

{"caseID":"case-README","positionID":"50023041","positionName":"ITビジネスパートナ","divisionID":"CORP_SVCS","divisionName":"コーポレートサービス本部","departmentID":"50150001","departmentName":"人事(US)","jobGradeID":"GR-07","jobGradeName":"SalaryGrade07","candidateID":"VictorK","candidateName":"KrisVictor","candidateDivisionID":"MANU","candidateDivisionName":"生産本部","candidateDepartmentID":"5000011","candidateDepartmentName":"業務推進部(SG)","candidatePositionID":"3000250","candidatePositionName":"品質保証ディレクタ","candidateJobGradeID":"GR-18","candidateJobGradeName":"SalaryGrade18","candidateJobTenure":12,"candidateLastRating1":null,"candidateLastRating2":null,"candidateLastRating3":"0","candidateTransferTimes":null,"candidateTransferReason":null,"candidateCertification":null,"candidateReshuffleCost":null,"candidateManagerID":"ChenM","incumbentEmpID":"108737","incumbentEmpName":"MiloDixon","incumbentEmpRetirementIntention":null,"incumbentEmpManager":"108736","simulationCheckStatus":null,"simulationCheckDatetime":null,"wfStatus":null,"mailSentFlg":"送信済","mailSentAt":"2022-02-08T01:39:53Z","sfUpsertFlg":null,"sfUpsertAt":null}

--changeset_6221-4db5-5842
Content-Type: application/http

PUT Candidates HTTP/1.1
Content-Type: application/json
Content-ID: id-1644217249975-224

{"caseID":"case-README","positionID":"50014275","positionName":"最高執行責任者","divisionID":"EXEC","divisionName":"社長室","departmentID":"50007725","departmentName":"グローバル運用","jobGradeID":"GR-14","jobGradeName":"SalaryGrade14","candidateID":"VictorK","candidateName":"KrisVictor","candidateDivisionID":"MANU","candidateDivisionName":"生産本部","candidateDepartmentID":"5000011","candidateDepartmentName":"業務推進部(SG)","candidatePositionID":"3000250","candidatePositionName":"品質保証ディレクタ","candidateJobGradeID":"GR-18","candidateJobGradeName":"SalaryGrade18","candidateJobTenure":12,"candidateLastRating1":null,"candidateLastRating2":null,"candidateLastRating3":"0","candidateTransferTimes":null,"candidateTransferReason":null,"candidateCertification":null,"candidateReshuffleCost":null,"candidateManagerID":"ChenM","incumbentEmpID":"80281","incumbentEmpName":"MichaelPittman","incumbentEmpRetirementIntention":null,"incumbentEmpManager":"890223","simulationCheckStatus":null,"simulationCheckDatetime":null,"wfStatus":null,"mailSentFlg":"送信済","mailSentAt":"2022-02-08T01:39:53Z","sfUpsertFlg":null,"sfUpsertAt":null}

--changeset_6221-4db5-5842--

--batch_readme1225--
```

###  DELETE /Candidate
- ex
  - /odata/v4/ReshuffleService/Candidates?$filter=caseid eq 'case-README'

#### Parameters

| isMandatory | Key    | ValueType | Example     |
| ----------- | ------ | --------- | ----------- |
| true        | caseID | String    | "yoneoTest" |

### POST /getFilter
- ex
  - /odata/v4/ReshuffleService/getFilter

#### Request Body
取得したいエンティティ名を"arg"のバリューにとってください。
```
{
    "arg" : "companies" | "businessunits" | "departments" | "positions" | "caseids"
}
```

### GET /Competencies
- ex
  - /odata/v4/ReshuffleService/Competencies?$filter=currentPosition eq '50025036'

#### Parameters
| isMandatory | Key             | ValueType | Example    |
| ----------- | --------------- | --------- | ---------- |
| true        | currentposition | String    | "50025036" |

### GET /getPhoto
- ex
  - /api/v4/ReshuffleService/Photos/104067（* empID）

#### Parameters
None.

### POST /exportFile
- ex
  - /api/v4/ReshuffleService/exportFile

#### Request Body
```
{
  "filename": "testYoneo",
  "entities": [
    {
      "candidateID": "VictorK",
      "positionID": "3000314",
      "incumbentEmpID": "Rhussin"
    }
  ]
}
```
