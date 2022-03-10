using {
    Candidates as DbCandidates,
    Configs    as DbConfigs,
    Config     as DbConfig
} from '../db/schema';


@requires : 'authenticated-user'
service ReshuffleService {

    entity CurrentPositionEmployees {
        empID          : cds.String(50);
        empName        : cds.String(256);
        empRetire      : cds.String enum {
            yes;
            no;
        };
        departmentID   : cds.String(128);
        divisionID     : cds.String(128);
        empManagerID   : cds.String(256);
        positionID     : cds.String(10);
        jobGradeID     : cds.String(256);
        departmentName : cds.String(128);
        divisionName   : cds.String(128);
        positionName   : cds.String(255);
        jobGradeName   : cds.String(32);
        empJobTenure   : cds.Integer;
        willingness    : cds.String enum {
            yes;
            no;
        };
        lastRating1    : cds.String(5);
        lastRating2    : cds.String(5);
        lastRating3    : cds.String(5);
        companyID      : cds.String;
        businessUnitID : cds.String;
    }

    entity NextPositions {
        empID                  : cds.String(50);
        empName                : cds.String(256);
        empRetire              : cds.String enum {
            yes;
            no;
        };
        divisionID             : cds.String(128);
        departmentID           : cds.String(128);
        empManagerID           : cds.String(256);
        positionID             : cds.String(10);
        jobGradeID             : cds.String(256);
        divisionName           : cds.String(128);
        departmentName         : cds.String(128);
        positionName           : cds.String(255);
        jobGradeName           : cds.String(32);
        companyID              : cds.String;
        businessUnitID         : cds.String;
        empRetirementIntension : cds.String;
    }

    entity Competencies {
        currentEmpID         : cds.String(50);
        currentEmpName       : cds.String(256);
        currentPositionID    : cds.String;
        competencyPercentage : Integer;
    }

    entity Candidates as select * from DbCandidates;
    entity Configs    as select * from DbConfigs;
    action getFilter(arg : String(20)) returns array of filterReturn;
    action getConfig() returns DbConfig;
}

type filterReturn {
    ID       : String;
    name     : String;
    parentID : String;
}

type content {
    candidateID    : String(50);
    positionID     : String(10);
    incumbentEmpID : String(50);
}
