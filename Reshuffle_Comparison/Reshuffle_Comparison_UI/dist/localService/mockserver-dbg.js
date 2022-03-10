sap.ui.define([
	"sap/ui/core/util/MockServer",
	"sap/ui/model/json/JSONModel",
	"sap/base/Log"
], function (MockServer, JSONModel, Log) {

	return {
		init: function () {
			
			var _sAppPath = "com/sap/sfsf/comparison/Reshuffle_Comparison_UI/",
				_sListJsonFilesPath = _sAppPath + "localService/mockdata/list.json",
				_scDivisionJsonFilesPath = _sAppPath + "localService/mockdata/candidateDivision.json",
				_scDepartmentJsonFilesPath = _sAppPath + "localService/mockdata/candidateDepartment.json",
				_scPositionJsonFilesPath = _sAppPath + "localService/mockdata/candidatePosition.json",
				_snDivisionJsonFilesPath = _sAppPath + "localService/mockdata/division.json",
				_snDepartmentJsonFilesPath = _sAppPath + "localService/mockdata/department.json",
				_snPositionJsonFilesPath = _sAppPath + "localService/mockdata/position.json",
				_sCaseIdJsonFilesPath = _sAppPath + "localService/mockdata/case.json",
				_sJpConfigJsonFilesPath = _sAppPath + "localService/mockdata/jpconfig.json";
				
			var _sJobHistory_akuwa = _sAppPath + "localService/mockdata/jobhistory_akuwa.json";

			// create
			var oModel = new JSONModel();

			var jsonListData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sListJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonListData = oData;
				}
			});
			
			var jsonCandidateDivisionData = "";
			jQuery.ajax(sap.ui.require.toUrl(_scDivisionJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonCandidateDivisionData = oData;
				}
			});
			var jsonCandidateDepartmentData = "";
			jQuery.ajax(sap.ui.require.toUrl(_scDepartmentJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonCandidateDepartmentData = oData;
				}
			});
			var jsonCandidatePositionData = "";
			jQuery.ajax(sap.ui.require.toUrl(_scPositionJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonCandidatePositionData = oData;
				}
			});
			var jsonDivisionData = "";
			jQuery.ajax(sap.ui.require.toUrl(_snDivisionJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonDivisionData = oData;
				}
			});
			var jsonDepartmentData = "";
			jQuery.ajax(sap.ui.require.toUrl(_snDepartmentJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonDepartmentData = oData;
				}
			});
			var jsonPositionData = "";
			jQuery.ajax(sap.ui.require.toUrl(_snPositionJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonPositionData = oData;
				}
			});			var jsonCaseidData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sCaseIdJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonCaseidData = oData;
				}
			});
			var jsonJobHistoryData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJobHistory_akuwa),{
				async: false,
				dataType: "json",
				success: function (oData){
					oModel.setData(oData);
					jsonJobHistoryData = oData;
				}
			});

			var list = {
				method: "GET",
				path: new RegExp("candidates(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonListData);
				}
			};
			
			var candidateDivision = {
				method: "GET",
				path: new RegExp("candidates/candidatedivisions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonCandidateDivisionData);
				}
			};

			var candidateDepartment = {
				method: "GET",
				path: new RegExp("candidates/candidatedepartments(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonCandidateDepartmentData);
				}
			};		

			var candidatePosition = {
				method: "GET",
				path: new RegExp("candidates/candidatepositions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonCandidatePositionData);
				}
			};		

			var division = {
				method: "GET",
				path: new RegExp("candidates/divisions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonDivisionData);
				}
			};

			var department = {
				method: "GET",
				path: new RegExp("candidates/departments(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonDepartmentData);
				}
			};		

			var position = {
				method: "GET",
				path: new RegExp("candidates/positions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonPositionData);
				}
			};	
			var caseId = {
				method: "GET",
				path: new RegExp("candidates/caseids(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonCaseidData);
				}
			};

			var jobHistory = {
				method: "GET",
				path: new RegExp("jobhistories/akuwa(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonJobHistoryData);
				}
			};


			var jsonJpConfigData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJpConfigJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonJpConfigData = oData;
				}
			});

			var jpconfig = {
				method: "GET",
				path: new RegExp("jpconfig(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonJpConfigData);
				}
			};	

			var oMockServer = new MockServer({
				rootUri: "/srv_api/",
				requests: [list, candidateDivision, candidateDepartment, candidatePosition, division, department, position,caseId, jobHistory]
			});

			var oMockServerConfig = new MockServer({
				rootUri: "/config_api/",
				requests: [jpconfig]
			});

			// start
			oMockServer.start();
			sap.ui.require(["sap/ui/core/ComponentSupport"]);

			oMockServerConfig.start();
			sap.ui.require(["sap/ui/core/ComponentSupport"]);


			Log.info("Running the app with mock data");
		}
	};
});