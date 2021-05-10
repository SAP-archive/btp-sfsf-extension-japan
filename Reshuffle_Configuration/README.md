# Reshuffle_Configuration

## API Ref
### GET /config
Get configurable values used on Applicants app and Simulation app.
#### Response Body Example 

```json:return value example
{
    "startDateTime": "2021/04/01",
    "tenureMonth": 11,
    "rateFormKey1": 801,
    "rateFormKey2": 401,
    "rateFormKey3": 201,
    "span": 120
}

```

### PUT /config
Update configurable values used on Applicants app and Simulation app.
#### Request Body Example 

```json:input value example
{
    "startDateTime": "2021/04/01",
    "tenureMonth": 11,
    "rateFormKey1": 801,
    "rateFormKey2": 401,
    "rateFormKey3": 201,
    "span": 120
}

```

### GET /jpconfig
Get configured values to show on Applicants app and Simulation app.
#### Response Body Example 

```json:return value example
{
    "startDateTime": "2021/04/01",
    "tenureMonth": 11
}

```

### GET /rateform
Get rate forms to select "rateFormKeyN" on "/config".
#### Response Body Example

```json:return value example
[
    {
        "formTemplateId": "102",
        "formTemplateName": "2012 Performance Review"
    },
    {
        "formTemplateId": "801",
        "formTemplateName": "2018 Performance Review"
    },
    ...
]
```