sap.ui.define([
	"sap/base/Log",
	"sap/m/Label",
	"sap/m/MessageBox",
	"sap/m/MessageStrip",
	"sap/m/Token",
	"sap/ui/core/BusyIndicator",
	"sap/ui/core/Fragment",
	"sap/ui/core/mvc/Controller",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterOperator",
	"sap/ui/model/json/JSONModel",
	"sap/ui/model/resource/ResourceModel",
	"sap/ui/table/library",
	"./BaseController",
	"../model/formatter"
], function (Log, Label, MessageBox, MessageStrip, Token, BusyIndicator, Fragment, Controller, Filter, FilterOperator, JSONModel, 
	ResourceModel, library, BaseController, formatter) {

	"use strict";

	var checkStatus = [{
		status: "",
		name: "ALL"
	}, {
		status: "OK",
		name: "エラーなし"
	}, {
		status: "NG",
		name: "エラーあり"
	}, {
		status: "-",
		name: "チェック未実施"
	}];

	var defaultDepartment = {
		externalCode: "",
		name: "ALL"
	};

	var defaultDivision = {
		externalCode: "",
		name: "ALL"
	};

	var defaultPosition = {
		code: "",
		name: "ALL"
	};

	return BaseController.extend("com.sap.sfsf.simulation.controller.CandidatesList", {
		formatter: formatter,
		onInit: function () {

			var oViewModel = new JSONModel({
				loaded: false,
				divisionBusy: true,
				departmentBusy: true,
				positionBusy: true
			});
			this.getView().setModel(oViewModel, "view");

			var oModel = new JSONModel();
			this.getView().setModel(oModel);
			this.division = [];
			this.department = [];
			this.position = [];

			var modelData = oModel.getData();
			var that = this;

			$.ajax({
				url: "/srv_api/division",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					that.division = result.slice();
					that.division.unshift(defaultDivision);
					modelData.currentDivision = that.division;
					modelData.nextDivision = that.division;

					oViewModel.setProperty("/divisionBusy", false);
					oModel.setData(modelData);

				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/divisionBusy", false);
				}
			});

			$.ajax({
				url: "/srv_api/department",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					that.department = result.slice();
					that.department.unshift(defaultDepartment);
					modelData.currentDepartment = that.department;
					modelData.nextDepartment = that.department;

					oViewModel.setProperty("/departmentBusy", false);
					oModel.setData(modelData);

				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/departmentBusy", false);
				}
			});

			$.ajax({
				url: "/srv_api/position",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
                    that.position = result.slice();
                    that.position.unshift(defaultPosition);
                    modelData.currentPosition = that.position;
                    modelData.nextPosition = that.position;

                    oViewModel.setProperty("/positionBusy", false);
                    oModel.setData(modelData);
				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/positionBusy", false);
				}
			});

			$.ajax({
				url: "/srv_api/caseid",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
 					var caseidList = [];
					for (var item of result){
						caseidList.push({status:item, name:item});
					}
					modelData.caseid = caseidList;
					oModel.setData(modelData);
				},
				error: function (xhr, status, err) {
					MessageBox.error("caseid error");
				}
            });
            
            this.getView().byId("slCaseId").setFilterFunction(function(sTerm, oItem) {
				return oItem.getText().match(new RegExp(sTerm, "i")) || oItem.getKey().match(new RegExp(sTerm, "i"));
			});
            
			modelData.checkStatus = checkStatus;
			modelData.candidateId = "";
			modelData.candidateName = "";

			oModel.setData(modelData);
			oModel.refresh(true);
		},

		onSlCurrentDivisionChange: function (oEvent) {
			this._divisionFilterItemChange(this._getSelectedItemText(this._getSelect("slDivision")), "C", defaultDepartment, defaultPosition);
		},

		onSlCurrentDepartmentChange: function (oEvent) {
			this._departmentFilterItemChange(this._getSelectedItemText(this._getSelect("slDivision")), this._getSelectedItemText(this._getSelect("slDepartment")), "C", defaultPosition);
		},

		onSlNextDivisionChange: function (oEvent) {
			this._divisionFilterItemChange(this._getSelectedItemText(this._getSelect("slNextDivision")), "N", defaultDepartment, defaultPosition);
		},

		onSlNextDepartmentChange: function (oEvent) {
			this._departmentFilterItemChange(this._getSelectedItemText(this._getSelect("slNextDivision")), this._getSelectedItemText(this._getSelect("slNextDepartment")), "N", defaultPosition);
		},

		onSearch: function (oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var oModelData = oModel.getData();
            oModelData.checkEnablement = "";
            oModelData.exportEnablement = "";
			var caseId = this._getSelectedItemText(this._getSelect("slCaseId"));
			
			if(!caseId){
				MessageBox.error(this._getResourceText("ms_selectCaseId"));
				return;
			}

			var sendData = {caseID:caseId};
			var that = this;
			$.ajax({
				url: "/srv_api/list",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					// Filter処理
					var searchResult = result;
                    searchResult = that._searchCaseId(searchResult, that._getSelectedItemText(that._getSelect("slCaseId")));
					searchResult = that._searchCheckStatus(searchResult, that._getSelectedItemText(that._getSelect("slCheckStatus")));
					searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slDivision")), "C");
					searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slDepartment")), "C");
					searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slPosition")), "C");
					searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slNextDivision")), "N");
					searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slNextDepartment")), "N");
					searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slNextPosition")), "N");
					searchResult = that._searchCandidateId(searchResult, oModel.getProperty("/candidateId"));
					searchResult = that._searchCandidateName(searchResult, oModel.getProperty("/candidateName"));

					oModelData.list = searchResult;
					oModel.setData(oModelData);
					oModel.refresh(true);
				},
				error: function (xhr, status, err) {
					MessageBox.error("list error");
				}
			});

			$.ajax({
				url: "/srv_api/status",
				method: "GET",
				data:sendData,
				contentType: "application/json",
				success: function (result, xhr, data) {
                    oModelData.status = result;
                    oModelData.checkEnablement = "";
                    oModelData.exportEnablement = "";
					if(result.status == null || result.status === "NG" || result.status === "WARN" ){
						oModelData.checkEnablement = "TRUE"; 
                    }
                    if(result.status === "NG" || result.status === "WARN"){
						oModelData.exportEnablement = "TRUE"; 
                    }
                    oModel.setData(oModelData);
                    oModel.refresh(true);
					that._createMessageStrip(result);
				},
				error: function (xhr, status, err) {}
			});

            oModel.setData(oModelData);
            oModel.refresh(true);
			this.getView().setModel(oModel);
		},

		onClear: function (oEvent) {
			var oModelData = this.getView().getModel().getData();
			oModelData.candidateId = "";
			oModelData.candidateName = "";
			this.getView().getModel().setData(oModelData);

			this._getSelect("slCaseId").setSelectedKey(null);
			this._getSelect("slCheckStatus").setSelectedKey("");
			this._getSelect("slDivision").setSelectedKey("");
			this._getSelect("slDepartment").setSelectedKey("");
			this._getSelect("slPosition").setSelectedKey("");
			this._getSelect("slNextDivision").setSelectedKey("");
			this._getSelect("slNextDepartment").setSelectedKey("");
			this._getSelect("slNextPosition").setSelectedKey("");
		},

		onSimulationCheck: function (oEvent) {
			BusyIndicator.show(0);

			var oView = this.getView();
			var oModel = oView.getModel();
			var oModelData = oView.getModel().getData();
            oModelData.checkEnablement = "";
            oModelData.exportEnablement = "";
			var caseId = this._getSelectedItemText(this._getSelect("slCaseId"));

			if(!caseId){
				MessageBox.error(this._getResourceText("ms_selectCaseId"));
				return;
			}

			var sendData = {caseID:caseId};
			var that = this;
			var check = function () {
				return $.ajax({
					type: "POST",
					data:caseId,
					contentType: "application/json",
					url: "/srv_api/check",
					dataType: "json",
					async: true,
					success: function (data, textStatus, jqXHR) {
						// Filter処理
						var searchResult = data;
                        searchResult = that._searchCaseId(searchResult, that._getSelectedItemText(that._getSelect("slCaseId")));
						searchResult = that._searchCheckStatus(searchResult, that._getSelectedItemText(that._getSelect("slCheckStatus")));
						searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slDivision")), "C");
						searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slDepartment")), "C");
						searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slPosition")), "C");
						searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slNextDivision")), "N");
						searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slNextDepartment")), "N");
						searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slNextPosition")), "N");
						searchResult = that._searchCandidateId(searchResult, oModel.getProperty("/candidateId"));
						searchResult = that._searchCandidateName(searchResult, oModel.getProperty("/candidateName"));

						oModelData.list = searchResult;
						oModel.setData(oModelData);
						oModel.refresh(true);
					},
					error: function () {
						that.byId("idCandidatesTable").setBusy(false);
						that.byId("check").setEnabled(true);
						that.byId("export").setEnabled(true);
					}
				});
			};

			var getStatus = function () {
				return $.ajax({
					url: "/srv_api/status",
					data:sendData,
					method: "GET",
					contentType: "application/json",
					success: function (result, xhr, data) {
						oModelData.status = result;
                        if(result.status == null || result.status === "NG" || result.status === "WARN" ){
                            oModelData.checkEnablement = "TRUE"; 
                        }
                        if(result.status === "NG" || result.status === "WARN"){
                            oModelData.exportEnablement = "TRUE"; 
                        }
                        oModel.setData(oModelData);
                        oModel.refresh(true);
						that._createMessageStrip(result);
					},
					error: function (xhr, status, err) {}
				});
			};
			
			var dfd = $.Deferred();
			var prms = dfd.promise();
			prms.pipe(check)
				.pipe(getStatus)
				.fail(function () {
					BusyIndicator.hide();
				}).always(function () {
					BusyIndicator.hide();
				});
			dfd.resolve();

			oModel.setData(oModelData);
			oModel.refresh(true);
			this.getView().setModel(oModel);
		},

		onDataExport: function (oEvent) {
			var that = this;
			var oXHR = new XMLHttpRequest();
			oXHR.open("GET", "/srv_api/export");
			oXHR.responseType = "blob";
			oXHR.onload = function () {
				if (oXHR.status < 400 && oXHR.response && oXHR.response.size > 0) {
					var sHeaderContentDisposition = decodeURIComponent(oXHR.getResponseHeader("Content-Disposition"));
					var sPassword = decodeURIComponent(oXHR.getResponseHeader("Password"));
					var aHeaderParts = sHeaderContentDisposition.split("filename=");
					var sFilenameFromServer = aHeaderParts[1];
					if (sap.ui.Device.browser.msie) {
						window.navigator.msSaveOrOpenBlob(oXHR.response, sFilenameFromServer);
					} else {
						var oA = document.createElement("a");
						oA.href = window.URL.createObjectURL(oXHR.response);
						oA.style.display = "none";
						oA.download = sFilenameFromServer;
						document.body.appendChild(oA);
						oA.click();
						MessageBox.information("Password:" + sPassword);
						document.body.removeChild(oA);
						// setTimeout is needed for safari on iOS
						setTimeout(function () {
							window.URL.revokeObjectURL(oA.href);
						}, 250);
					}
				} else {
					MessageBox.error(that._getResourceText("ms_error"));
				}
			}.bind(this);
			oXHR.send();
		},
		
		onPopoverPress: function (oEvent) {
			var oControl = oEvent.getSource();
			var path = oEvent.mParameters.rowBindingContext.sPath;
			var array = path.split("/");
			var position = array[2];
			var list = this.getView().getModel().getData().list;

			if (list[position].checkResult !== "") {
				// create popover
				if (!this._oPopover) {
					Fragment.load({
						name: "com.sap.sfsf.simulation.view.CheckResult",
						controller: this
					}).then(function (oPopover) {
						this._oPopover = oPopover;
						this.getView().addDependent(this._oPopover);
						this._oPopover.bindElement(path);
						this._oPopover.openBy(oControl);
					}.bind(this));
				} else {
					this._oPopover.bindElement(path);
					this._oPopover.openBy(oControl);
				}
			}
		},
		
		onClosePress: function () {
			this._oPopover.close();
        },
        
        _createMessageStrip: function(result){
			var oMs = sap.ui.getCore().byId("msgStrip");
			if (oMs) {
				oMs.destroy();
			}
			
			var statusText = "",
			    statusType = "";

			if (result.hasOwnProperty("status")) {
				if (result.status === "OK") {
					statusText = this._getResourceText("ms_statusText1_i1") + result.TOTAL + " "  + this._getResourceText("ms_statusText1_i2") + " " + this._getResourceText("ms_statusText1_i3") + " " + result.checkedDateTime;
					statusType = "Success";
				} else if (result.status === "NG") {
					statusText = this._getResourceText("ms_statusText1_e1") + result.NG + " " + this._getResourceText("ms_statusText1_e2") + " " + this._getResourceText("ms_statusText1_e3") + " " + result.checkedDateTime;
					statusType = "Error";
				} else if (result.status === "WARN") {
					statusText = this._getResourceText("ms_statusText1_w1") + result.WARN + " "  + this._getResourceText("ms_statusText1_w2") + " " + this._getResourceText("ms_statusText1_w3") + " " +result.checkedDateTime;
					statusType = "Warning";
				} else if (result.status === null) {
					statusText = this._getResourceText("ms_statusText1_i4");
					statusType = "Information";
				}else if(result.status === "APPL"){
					statusText = this._getResourceText("ms_statusText1_i5");
					statusType = "Information";
				}else if(result.status === "APPD"){
					statusText = this._getResourceText("ms_statusText1_i6");
					statusType = "Information";
				}else if(result.status === "DENY"){
					statusText = this._getResourceText("ms_statusText1_i7");
					statusType = "Information";
                }
			} else {
				statusText = this._getResourceText("ms_statusText1_i4");
				statusType = "Information";
			}
			
			if(statusText){
				var oPage = this.byId("vlmessage");
				var oMsgStrip = new MessageStrip("msgStrip", {
					text: statusText,
					showCloseButton: false,
					showIcon: true,
					type: statusType
				});
				oPage.addContent(oMsgStrip);
			}
		}

	});
});