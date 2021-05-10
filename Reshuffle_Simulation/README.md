# Resuffle_Simulation

## Get /status
Get the check process status:
```json
{
    "status":"NG/WARN/OK",
    "checkedDateTime":"yyyy/MM/dd HH:mm:ss (JST)",
    "TOTAL": iTotalCnt,
    "NG": iNGCnt,
    "OK": iOKCnt,
    "WARN": iWARNCnt
}
```

## Get /list
Get the candidate list from HANA DB

## Get /company
Get company list

## Get /businessunit
Get business unit list

## Get /division
Get division list

## Get /department
Get department list

## Get /position
Get position list ( Only comapny in Japan)

## Post /check
Check the candidate
return the checked status in header and the checked list
*Header*
checkResult:NG/OK
*Body*
candidate list

## Post /upsert
Send the checked simulation back to SFSF.
Payload format:
```json
[{
    "candidateId":"xxx",
    "nextPosition":"xxx_position"
},{
    "candidateId":"yyy",
    "nextPosition":"yyy_position"
}
]
```

*Response Header*
upsert_status: success/fail

*Response Body*
Updated Candidate List



## POST /mail
Send mail to the candidate in the list right now.
```json
[{
    "candidateId":"xxx",
    "nextPosition":"xxx_position"
},{
    "candidateId":"yyy",
    "nextPosition":"yyy_position"
}
]
```
*Response Header*
mail_status: success/fail

*Response Body*
Updated Candidate List


## POST /mailjob
Send mail to the candidate in the list using job.
*Header*: 
startDateTime = YYYYMMDDhhmm
*Body*
```json
[{
    "candidateId":"xxx",
    "nextPosition":"xxx_position"
},{
    "candidateId":"yyy",
    "nextPosition":"yyy_position"
}
]
```

*Response Header*
mailjob_status: success/fail

*Response Body*
Updated Candidate List




## local test
```bash
cd Resuffle_Simulation_Backend
sudo docker-compose up --build
```
Test by accessing localhost:8080

