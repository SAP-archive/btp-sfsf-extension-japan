import json = sap.ui.model.json;
import odata = sap.ui.model.odata;
import mvc = sap.ui.core.mvc;
declare namespace com.sap.sfsf.reshuffle.applicants {
	namespace model {
		class CurrentPositionemployeeTableModel extends json.JSONModel {
			constructor(backendServiceBaseUrl: string, backendServiceApiBaseUrl: string, errorMessageToastCallback: (text: string) => void);
			_getFiltersData();
			getTableData(filters: CurrentPositionEmployeeTableFilterValues, startLoading: ()=>void, finishLoading: ()=>void);
			setDivisionFilterByBusinessUnit(businessunitId: string): void;
			setDepartmentFilterByDivision(divisionId: string): void;
			setPositionFilterByDepartment(departmentId: string): void;
			updateTransferedFlagStatusByEmpID(empID: String, status: Boolean): void;
			tableRowDragged(rowIndex: int): void;
			tableRowDropped(empID: String): void;
			errorMessageToastCallback: (text: string) => void;
			exportTableData(success: (password: string) => void, fail: () => void): void;
			backendServiceBaseUrl: string;
			backendServiceApiBaseUrl: string;
			filterData: object;
		}

		class CurrentPositionEmployeeTableFilterValues {
			company: String;
			businessUnit: String;
			division: String;
			department: String;
			position: String;
			tenureUL: int;
			tenureLL: int;
			willingness: String;
		}

		class NextPositionTableModel extends json.JSONModel {
			constructor(backendServiceBaseUrl: string, backendServiceApiBaseUrl: string, errorMessageToastCallback: (text: String) => void);
			_getFiltersData();
			getTableData(filters: NextPositionTableFilterValues, startLoading: ()=>void, finishLoading: ()=>void);
			setDivisionFilterByBusinessUnit(businessunitId: string): void;
			setDepartmentFilterByDivision(divisionId: string): void;
			setPositionFilterByDepartment(departmentId: string): void;
			getAssignedCandidateEmpID(index: int): string;
			tableRowDropped(droppedIndex: Number, draggedData: Object): Boolean;
			tableRowDragged(draggedIndex: int): void;
			hasEmptyPost(): boolean;
			saveCandidates(model: odata.v4.ODataModel, caseID: String, success: ()=>void, failed: ()=>void): void;
			errorMessageToastCallback(text: string): void;
			getCompetencies(selectedRowIndex: int, success: ()=>void, failed: ()=>void): void;
			competencySelected(selectedCompetencyData: Object): Boolean;
			exportTableData(success: (password: string) => void, fail: () => void): void;
			backendServiceBaseUrl: string;
			backendServiceApiBaseUrl: string;
			filterData: object;
		}

		class NextPositionTableFilterValues {
			company: String;
			businessUnit: String;
			division: String;
			department: String;
			position: String;
			retire: String;
		}

		class EditCaseTableModel extends json.JSONModel {
			constructor(backendServiceBaseUrl: string, backendServiceApiBaseUrl: string, errorMessageToastCallback: (text: string) => void);
			getTableData(caseID: String, startLoading: ()=>void, finishLoading: ()=>void): void;
			clearAndReloadFilter(): void;
			hasEmptyPost(): Boolean;
			updateRow(rowIndex: int, candidateID: String, candidateName: String): Boolean;
			tableRowDragged(draggedIndex: int): void;
			deleteCurrentCase(success:()=>void, fail:()=>void): void;
			getAssignedCandidateID(index: int): string;
			saveCase(model: odata.v4.ODataModel, success: ()=>void, failed: ()=>void): void;
			getCompetencies(selectedRowIndex: int, success: ()=>void, failed: ()=>void): void;
			competencySelected(selectedCompetencyData: Object): Boolean;
			exportTableData(success: (password: string) => void, fail: () => void): void;
		}
	}

	class EventBusHelper {
		subscribeOpenNextPositionTableEvent(controller: mvc.Controller, fn: Function): void
		publishOpenNextPositionTableEvent(controller: mvc.Controller);
		subscribeCloseNextPositionTableEvent(controller: mvc.Controller, fn: Function): void
		publishCloseNextPositionTableEvent(controller: mvc.Controller);

		subscribeOpenEditCaseTableEvent(controller: mvc.Controller, fn: Function): void
		publishOpenEditCaseTableEvent(controller: mvc.Controller);
		subscribeCloseEditCaseTableEvent(controller: mvc.Controller, fn: Function): void
		publishCloseEditCaseTableEvent(controller: mvc.Controller);
	}

}

