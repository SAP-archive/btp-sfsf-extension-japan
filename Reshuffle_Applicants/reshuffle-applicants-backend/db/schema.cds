using {managed} from '@sap/cds/common';

@cds.persistence.exists
entity Candidates : managed {
    key caseID                          : String(50);
    key positionID                      : String(10);
        positionName                    : String(50);
        divisionID                      : String(10);
        divisionName                    : String(50);
        departmentID                    : String(10);
        departmentName                  : String(50);
        jobGradeID                      : String(5);
        jobGradeName                    : String(50);
    key candidateID                     : String(50);
        candidateName                   : String(50);
        candidateDivisionID             : String(10);
        candidateDivisionName           : String(50);
        candidateDepartmentID           : String(10);
        candidateDepartmentName         : String(50);
        candidatePositionID             : String(10);
        candidatePositionName           : String(50);
        candidateJobGradeID             : String(50);
        candidateJobGradeName           : String(50);
        candidateJobTenure              : Integer;
        candidateLastRating1            : String(5);
        candidateLastRating2            : String(5);
        candidateLastRating3            : String(5);
        candidateManagerID              : String(256);
        incumbentEmpID                  : String(50);
        incumbentEmpName                : String(50);
        incumbentEmpRetirementIntention : String(5);
        incumbentEmpManagerID           : String(256);
}

@cds.persistence.exists
entity Configs : Config {}

type Config {
    startDateTime       : DateTime;
    span                : Integer;
    competencyThreshold : Integer;
    rateFormKey1        : String(10);
    rateFormKey2        : String(10);
    rateFormKey3        : String(10);
    presidentName       : String(32);
    mailTemplate        : String(4112);
};
