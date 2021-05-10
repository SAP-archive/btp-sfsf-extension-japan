# Reshuffle_Applicants
This will be used as technical note to communicate for the time being.

## API Ref
Reshuffle_Backend has 3 apis to handle SFSF and HANA Cloud.
You can use this apis like below.

`From approuter: /path/to/the/approuter/srv_api/candidates`

`From Backend  : /path/to/the/backend/candidates`

### GET /candidates
This gets a candidates list from SFSF as JSON array.

ex: /candidates?company=5000&businessUnit=SALES&division=DIR_SALES&department=5000102

#### Args
| isMandatory | Key | ValueType | Example | Constraints |
| ----------- | ---  | --------- | ------- | ------------ |
| true | company     | String | "5000"     | length is between 1 to 50|
| true | businessUnit| String | "SALES"    | length is between 1 to 50|
| true | division    | String | "DIR_SALES"| length is between 1 to 50|
| true | department  | String | "51505150" | length is between 1 to 50|
|      | position    | String | "MANU"     | length is between 1 to 50|
|      | tenureLL    | int    | 2          | value is between 0 to 9999|
|      | tenureUL    | int    | 4          | value is between 1 to 9999|
|      | willingness | String | "yes"      | value is "yes", "no", or "-"|
|      | ratingType  | String | "tenurePosition" | length is betwenn 1 to 50|

HTTP response returns 400 BAD REQUEST if you send wrong args.
Please check cf logs. HTTP Response does not have the error message.
All validation errors are in single log "Filter Validation Error(s)".

ex: Ê0-07-16 14:57:01.583 ERROR 7 --- [nio-8080-exec-4] c.s.s.r.a.backend.HelloController : Filter Validation Error(s) : dsivision is not a filter key, departme,nt is not a filter key, division is mandatory


#### Response Body Example 
```json:return value example
[
   {
        "photo": "data:image/jpeg;base64,/9j...//",
        "candidateID": null,
        "candidateName": null,
        "nextDivision": null,
        "nextDivisionName": null,
        "nextDepartment": null,
        "nextDepartmentName": null,
        "nextManager": null,
        "nextPosition": null,
        "nextPositionName": null,
        "nextJobGrade": null,
        "nextJobGradeName": null,
        "currentEmpId": "mhirayama",
        "currentEmpName": "Ambrose Akinmusire",
        "currentDivision": "DIR_SALES",
        "currentDivisionName": "営業本部",
        "currentDepartment": "5000102",
        "currentDepartmentName": "販売 (JP)",
        "currentManager": "ttoyama",
        "currentPosition": "3000343",
        "currentPositionName": "アシスタント",
        "currentJobGrade": "H-04",
        "currentJobGradeName": null,
        "currentEmpRetire": "no",
        "willingness": "-",
        "rating": null,
        "ratingType": null,
        "ratingTypeName": null,
        "jobTenure": 3,
        "transferTimes": 0,
        "lastTransReason": null,
        "certification": null,
        "checkResult": null,
        "checkStatus": null,
        "checkDateTime": null
   },
    ...

```

### POST /candidates
This sends candidates information as JSON array to the HANA Cloud DB.
The length limitation of below all attributes is "up to 50".

NOTE: The body of GET and POST are almost the same, but extract "photo" and "mimeType" before invoking POST /candidates.

#### Resuest Body Example

```json:return value example
[
   {
        "candidateID": null,
        "candidateName": null,
        "nextDepartment": null,
        "nextDivision": null,
        "nextManager": null,
        "nextPosition": null,
        "nextDepartmentName": null,
        "nextDivisionName": null,
        "nextPositionName": null,
        "nextJobGrade": null,
        "currentEmpId": "27000007",
        "currentEmpName": "Ambrose Akinmusire",
        "currentEmpRetire": null,
        "currentDepartment": "5000036",
        "currentDivision": "MANU",
        "currentManager": "kharuki",
        "currentPosition": "3000208",
        "currentDepartmentName": "業務推進部 (HK)",
        "currentDivisionName": "生産本部",
        "currentPositionName": "生産マネージャ",
        "jobTenure": 6,
        "currentPayGrade": "GR-06",
        "currentPayGradeName": null,
        "transferTimes": 0,
        "lastTransReason": null,
        "certification": null,
        "checkResult": null,
        "checkStatus": null,
        "checkDateTime": null
        "rating": null,
        "ratingType": null,
        "ratingTypeName": null,
    },
    ...

```

### GET /nextpositions
This gets a next positions list from SFSF as JSON array.

#### Args
ex: /nextpositions?company=5000&businessUnit=SALES&division=DIR_SALES&department=5000102

#### Args
| isMandatory | Key | ValueType | Example | Constraints |
| ----------- | ---  | --------- | ------- | ------------ |
| true | company     | String | "5000"     | length is between 1 to 50|
| true | businessUnit| String | "SALES"    | length is between 1 to 50|
| true | division    | String | "DIR_SALES"| length is between 1 to 50|
| true | department  | String | "51505150" | length is between 1 to 50|
|      | position    | String | "MANU"     | length is between 1 to 50|
|      | retirement  | String | "yes"      | value is "yes" or "no" |

HTTP response returns 400 BAD REQUEST if you send wrong args.
Please check cf logs. HTTP Response does not have the error message.
All validation errors are in single log "Filter Validation Error(s)".

ex: Ê0-07-16 14:57:01.583 ERROR 7 --- [nio-8080-exec-4] c.s.s.r.a.backend.HelloController : Filter Validation Error(s) : dsivision is not a filter key, departme,nt is not a filter key, division is mandatory

#### Response Body Example 
The same as GET /candidates

```json:return value example
[
   {
        "candidateID": null,
        "candidateName": null,
        "nextDepartment": null,
        "nextDivision": null,
        "nextManager": null,
        "nextPosition": null,
        "nextDepartmentName": null,
        "nextDivisionName": null,
        "nextPositionName": null,
        "nextJobGrade": null,
        "currentEmpId": "27000007",
        "currentEmpName": "Tom Yorke",
        "currentEmpRetire": null,
        "currentDepartment": "5000036",
        "currentDivision": "MANU",
        "currentManager": "kharuki",
        "currentPosition": "3000208",
        "currentDepartmentName": "業務推進部 (HK)",
        "currentDivisionName": "生産本部",
        "currentPositionName": "生産マネージャ",
        "jobTenure": 6,
        "currentPayGrade": "GR-06",
        "currentPayGradeName": null,
        "transferTimes": 0,
        "lastTransReason": null,
        "certification": null,
        "checkResult": null,
        "checkStatus": null,
        "checkDateTime": null,
        "rating": null,
        "ratingType": null,
        "ratingTypeName": null,
        "photo": "/9j/4AAQSkZJRgABAgA....省略...\n",
        "mimeType": "image/jpeg"
    },
    ...

```

### GET /companies
This is a filter api.

```json:return value example
   {
        "externalCode": "5000",
        "startDate": "/Date(1467676800000)/",
        "endDate": "/Date(253402214400000)/",
        "country": "JPN",
        "name": "BestRun ジャパン"
    }

```

### GET /businessunits
This is a filter api

```json:return value example
   {
        "externalCode": "SALES",
        "startDate": "/Date(1451606400000)/",
        "endDate": "/Date(253402214400000)/",
        "name": "営業統括本部"
    }

```

### GET /divisions
This is a filter api.

```json:return value example
   {
        "externalCode": "DIR_SALES",
        "startDate": "/Date(1451606400000)/",
        "endDate": "/Date(253402214400000)/",
        "name": "営業本部",
        "businessUnit": {
            "businessUnitList": [
                {
                    "externalCode": "SALES",
                    "startDate": null,
                    "endDate": null,
                    "name": null
                }
            ]
        }
    }

```

### GET /positions
This is a filter api

```json:return value example
   {
        "code": "3000341",
        "effectiveStartDate": "/Date(1491004800000)/",
        "effectiveEndDate": "/Date(253402214400000)/",
        "parentPosition": null,
        "name": "北関東支社長",
        "company": "5000",
        "businessUnit": "SALES",
        "division": "DIR_SALES",
        "department": "5000102"
    }

```

### GET /departments
This is a filter api

```json:return value example
   {
        "externalCode": "5000102",
        "startDate": "/Date(1451606400000)/",
        "endDate": "/Date(253402214400000)/",
        "name": "販売 (JP)",
        "division": {
            "divisionList": [
                {
                    "externalCode": "DIR_SALES",
                    "startDate": null,
                    "endDate": null,
                    "name": null,
                    "businessUnit": null
                }
            ]
        }
    }

```

## Performance Notes
none
