sap.ui.define([
	"sap/ui/core/util/MockServer",
	"sap/ui/model/json/JSONModel",
	"sap/base/Log"
], function (MockServer, JSONModel, Log) {

	return {
		init: function () {
			
			var _sAppPath = "com/sap/sfsf/comparison/Reshuffle_Comparison_UI/",
				_sListJsonFilesPath = _sAppPath + "localService/mockdata/list.json",
				_scDivisionJsonFilesPath = _sAppPath + "localService/mockdata/currentDivision.json",
				_scDepartmentJsonFilesPath = _sAppPath + "localService/mockdata/currentDepartment.json",
				_scPositionJsonFilesPath = _sAppPath + "localService/mockdata/currentPosition.json",
				_snDivisionJsonFilesPath = _sAppPath + "localService/mockdata/nextDivision.json",
				_snDepartmentJsonFilesPath = _sAppPath + "localService/mockdata/nextDepartment.json",
				_snPositionJsonFilesPath = _sAppPath + "localService/mockdata/nextPosition.json",
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
			
			var jsonCurrentDivisionData = "";
			jQuery.ajax(sap.ui.require.toUrl(_scDivisionJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonCurrentDivisionData = oData;
				}
			});
			var jsonCurrentDepartmentData = "";
			jQuery.ajax(sap.ui.require.toUrl(_scDepartmentJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonCurrentDepartmentData = oData;
				}
			});
			var jsonCurrentPositionData = "";
			jQuery.ajax(sap.ui.require.toUrl(_scPositionJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonCurrentPositionData = oData;
				}
			});
			var jsonNextDivisionData = "";
			jQuery.ajax(sap.ui.require.toUrl(_snDivisionJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonNextDivisionData = oData;
				}
			});
			var jsonNextDepartmentData = "";
			jQuery.ajax(sap.ui.require.toUrl(_snDepartmentJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonNextDepartmentData = oData;
				}
			});
			var jsonNextPositionData = "";
			jQuery.ajax(sap.ui.require.toUrl(_snPositionJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonNextPositionData = oData;
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
			
			var currentDivision = {
				method: "GET",
				path: new RegExp("candidates/currentdivisions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonCurrentDivisionData);
				}
			};

			var currentDepartment = {
				method: "GET",
				path: new RegExp("candidates/currentdepartments(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonCurrentDepartmentData);
				}
			};		

			var currentPosition = {
				method: "GET",
				path: new RegExp("candidates/currentpositions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonCurrentPositionData);
				}
			};		

			var nextDivision = {
				method: "GET",
				path: new RegExp("candidates/nextdivisions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonNextDivisionData);
				}
			};

			var nextDepartment = {
				method: "GET",
				path: new RegExp("candidates/nextdepartments(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonNextDepartmentData);
				}
			};		

			var nextPosition = {
				method: "GET",
				path: new RegExp("candidates/nextpositions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonNextPositionData);
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
				requests: [list, currentDivision, currentDepartment, currentPosition, nextDivision, nextDepartment, nextPosition,caseId, jobHistory]
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