sap.ui.define([
	"sap/ui/core/util/MockServer",
	"sap/ui/model/json/JSONModel",
	"sap/base/Log"
], function (MockServer, JSONModel, Log) {

	return {
		init: function () {
			
			var _sAppPath = "com/sap/sfsf/applicants/";
			var	_sJsonComFilesPath = _sAppPath + "localService/mockdata/companies.json";
			var	_sJsonBuFilesPath = _sAppPath + "localService/mockdata/businessunits.json";
			var	_sJsonFilesPath = _sAppPath + "localService/mockdata/departments.json";
			var	_sJsonCanFilesPath = _sAppPath + "localService/mockdata/currentpositions.json";
			var	_sJsonDivFilesPath = _sAppPath + "localService/mockdata/divisions.json";
			var	_sJsonPosFilesPath = _sAppPath + "localService/mockdata/positions.json";
			var	_sJsonRatingFilesPath = _sAppPath + "localService/mockdata/ratings.json";
			var	_sJsonNextPosFilesPath = _sAppPath + "localService/mockdata/nextpositions.json";
			var	_sJsonPhotoFilesPath = _sAppPath + "localService/mockdata/photo.json";
			var	_sJsonDateFilesPath = _sAppPath + "localService/mockdata/jpconfig.json";
			var _sFileUrlPath = "localService/mockdata/applicants20200729144925.zip";
			var _sJsonCanPostFilesPath=  _sAppPath + "localService/mockdata/candidatespost.json";
			var _sJsonUserPositionPrecisionsFilesPath=  _sAppPath + "localService/mockdata/userpositionprecisions.json";
            var _sJsonSelectedCanFilesPath=  _sAppPath + "localService/mockdata/selectedCandidates.json"
            var _sJsonCaseIDFilesPath=  _sAppPath + "localService/mockdata/caseids.json"
            var _sJsonSelectedCaseIDFilesPath=  _sAppPath + "localService/mockdata/candidatesbycaseid.json"

			// create
			// var oModel = new JSONModel();
			//oModel.loadData(sap.ui.require.toUrl(_sJsonFilesPath),false);
			var jsonComData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonComFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					jsonComData = oData;
				}
			});
			var jsonBuData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonBuFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					jsonBuData = oData;
				}
			});
			var jsonData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					jsonData = oData;
				}
			});
			var jsonCanData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonCanFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					jsonCanData = oData;
				}
			});
			var jsonDivData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonDivFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					
					jsonDivData = oData;
				}
			});
			var jsonPosData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonPosFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					
					jsonPosData = oData;
				}
			});
			var jsonRatingData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonRatingFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					
					jsonRatingData = oData;
				}
			});
			var jsonNextPosData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonNextPosFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					
					jsonNextPosData = oData;
				}
			});
			var jsonPhotoData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonPhotoFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					
					jsonPhotoData = oData;
				}
			});
			var jsonDateData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonDateFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					
					jsonDateData = oData;
				}
			});
			var jsonCanPostData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonCanPostFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					
					jsonCanPostData = oData;
				}
			});
			var jsonUserPositionPrecisionsData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonUserPositionPrecisionsFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					jsonUserPositionPrecisionsData = oData;
				}
			});
			var jsonSelectedCanData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonSelectedCanFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					jsonSelectedCanData = oData;
				}
            });
            var jsonCaseIDData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonCaseIDFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					jsonCaseIDData = oData;
				}
            });
            var jsonSelectedCaseIDData = "";
			jQuery.ajax(sap.ui.require.toUrl(_sJsonSelectedCaseIDFilesPath), {
				async: false,
				dataType: "json",
				success: function (oData) {
					jsonSelectedCaseIDData = oData;
				}
			});
			var sFileUrl = _sFileUrlPath;
			var mHeaders = {
				"Content-Disposition": "attachment;filename=applicants20200729144925.zip",
				"Content-Type": "application/octet-stream",
				"Password": "SNp9'Fc+5d"
			};
			var companies = {
				method: "GET",
				path: new RegExp("companies(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonComData);
				}
			};
			var businessunits = {
				method: "GET",
				path: new RegExp("businessunits(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonBuData);
				}
			};
			var currentpositions = {
				method: "GET",
				path: new RegExp("currentpositions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonCanData);
				}
			};
			var divisions = {
				method: "GET",
				path: new RegExp("divisions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonDivData);
				}
			};
			var departments = {
				method: "GET",
				path: new RegExp("departments(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonData);
				}
			};
			var positions = {
				method: "GET",
				path: new RegExp("positions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonPosData);
				}
			};
			var ratings = {
				method: "GET",
				path: new RegExp("ratings(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonRatingData);
				}
			};
			var nextpositions = {
				method: "GET",
				path: new RegExp("nextpositions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonNextPosData);
				}
			};
			var candidatespost = {
				method: "POST",
				path: new RegExp("candidatespost(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonCanPostData);
				}
			};
			var photo = {
				method: "GET",
				path: new RegExp("photo(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respond(200, {}, jsonPhotoData);
				}
			};
			var jpconfig = {
				method: "GET",
				path: new RegExp("jpconfig(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonDateData);
				}
			};
			var fileexport = {
				method: "GET",
				path: new RegExp("export(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondFile(200, mHeaders, sFileUrl);
				}
			};
			var userpositionprecisions = {
				method: "GET",
				path: new RegExp("userpositionprecisions(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonUserPositionPrecisionsData);
				}
			};
			var selectedCan = {
				method: "GET",
				path: new RegExp("selectedCandidates(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonSelectedCanData);
				}
            };
            var caseids = {
				method: "GET",
				path: new RegExp("caseids(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonCaseIDData);
				}
            };
            var selectedCaseid = {
				method: "GET",
				path: new RegExp("selectedCaseid(.*)"),
				response: function (oXhr, sUrlParams) {
					oXhr.respondJSON(200, {}, jsonSelectedCaseIDData);
				}
			};

			var oMockServer = new MockServer({
				rootUri: "/srv_api/",
				requests: [companies, businessunits, currentpositions, divisions, departments, positions, nextpositions, candidatespost, photo, ratings, fileexport, userpositionprecisions, selectedCan, caseids, selectedCaseid]
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