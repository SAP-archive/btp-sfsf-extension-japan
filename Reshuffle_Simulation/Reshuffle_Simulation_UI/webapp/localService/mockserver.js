sap.ui.define([
	"sap/ui/core/util/MockServer",
	"sap/ui/model/json/JSONModel",
	"sap/base/Log"
], function (MockServer, JSONModel, Log) {

	return {
		init: function () {

			var _sAppPath = "com/sap/sfsf/simulation/",
				_sJsonFilesPath = _sAppPath + "localService/mockdata/model.json",
				_sListJsonFilesPath = _sAppPath + "localService/mockdata/list.json",
				_sCheckedListJsonFilesPath = _sAppPath + "localService/mockdata/list_checked.json",
				_sDivisionJsonFilesPath = _sAppPath + "localService/mockdata/division.json",
				_sDepartmentJsonFilesPath = _sAppPath + "localService/mockdata/department.json",
				_sPositionJsonFilesPath = _sAppPath + "localService/mockdata/position.json",
				_sStatusJsonFilesPath = _sAppPath + "localService/mockdata/status.json",
				_sStatus2JsonFilesPath = _sAppPath + "localService/mockdata/status2.json",
				_sConfigJsonFilesPath = _sAppPath + "localService/mockdata/config.json",
				_sJpConfigJsonFilesPath = _sAppPath + "localService/mockdata/jpconfig.json";
			// create
			var oModel = new JSONModel();
			//oModel.loadData(sap.ui.require.toUrl(_sJsonFilesPath),false);
			var jsonData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonData = oData;
				}
			});
			var jsonListData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sListJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonListData = oData;
				}
			});
			var jsonListCheckedData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sCheckedListJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonListCheckedData = oData;
				}
			});
			var jsonDivisionData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sDivisionJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonDivisionData = oData;
				}
			});
			var jsonDepartmentData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sDepartmentJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonDepartmentData = oData;
				}
			});
			var jsonPositionData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sPositionJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonPositionData = oData;
				}
			});
			var jsonStatusData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sStatusJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonStatusData = oData;
				}
			});
			var jsonStatus2Data = "";
			jQuery.ajax(sap.ui.require.toUrl(_sStatus2JsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonStatus2Data = oData;
				}
			});
			var jsonConfigData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sConfigJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonConfigData = oData;
				}
			});

			var jsonJpConfigData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJpConfigJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					oModel.setData(oData);
					jsonJpConfigData = oData;
				}
			});
			
			var all = {
				method: "GET",
				path: new RegExp("all(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonData);
				}
			};

			var list = {
				method: "GET",
				path: new RegExp("list(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonListData);
				}
			};
			var check = {
				method: "POST",
				path: new RegExp("check(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonListCheckedData);
				}
			};

			var mail = {
				method: "POST",
				path: new RegExp("mail(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonListCheckedData);
				}
			};

			var mailjob = {
				method: "POST",
				path: new RegExp("mailjob(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonListCheckedData);
				}
			};	
			
			var sendMail = {
				method: "POST",
				path: new RegExp("check(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonListCheckedData);
				}
			};
			var division = {
				method: "GET",
				contentType: "application/octet-stream",
				path: new RegExp("division(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonDivisionData);
				}
			};

			var department = {
				method: "GET",
				path: new RegExp("department(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonDepartmentData);
				}
			};		

			var position = {
				method: "GET",
				path: new RegExp("position(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonPositionData);
				}
			};		

			var status = {
				method: "GET",
				path: new RegExp("status(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonStatusData);
				}
			};		

			var status2 = {
				method: "GET",
				path: new RegExp("status2(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonStatus2Data);
				}
			};	
			
			var config = {
				method: "GET",
				path: new RegExp("config(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonConfigData);
				}
			};		

			var jpconfig = {
				method: "GET",
				path: new RegExp("jpconfig(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonJpConfigData);
				}
			};	

			var oMockServer = new MockServer({
				rootUri: "/srv_api/",
				requests: [all, list, check, mail, mailjob, sendMail, division, department, position, status, status2]
			});

			var oMockServerConfig = new MockServer({
				rootUri: "/config_api/",
				requests: [config, jpconfig]
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