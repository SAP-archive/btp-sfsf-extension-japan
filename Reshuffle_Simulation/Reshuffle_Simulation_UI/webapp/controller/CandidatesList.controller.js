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

	var defaultPosition = {
		code: "",
		name: "ALL"
	};

    return BaseController.extend("com.sap.sfsf.simulation.controller.CandidatesList", {
        formatter: formatter,
        onInit: function () {

            this.getView().byId("slCaseId").setFilterFunction(function (sTerm, oItem) {
                return oItem.getText().match(new RegExp(sTerm, "i")) || oItem.getKey().match(new RegExp(sTerm, "i"));
            });

            var oModel = this.getOwnerComponent().getModel();
            var modelData = oModel.getData();
            modelData.candidateID = "";
			modelData.candidateName = "";
            modelData.checkStatus = checkStatus;
            modelData.checkEnablement = false;
            modelData.exportEnablement = false;
            oModel.setData(modelData);
            this.getView().setModel(oModel,"simulation");

            this.oViewModel = this.getOwnerComponent().getModel();
            this.getView().getModel(this.oViewModel, "view");      

            var oButtonStatusModel = new JSONModel();
            var oButtonStatusModelData = oButtonStatusModel.getData();
            oButtonStatusModelData.checkEnablement = false;
            oButtonStatusModelData.exportEnablement = false;
            oButtonStatusModel.setData(oButtonStatusModelData);
            this.getView().setModel(oButtonStatusModel, "buttonStatus");
        },

        onSlCurrentDivisionChange: function (oEvent) {
            this._divisionFilterItemChange(this._getSelectedItemText(this._getSelect("slDivision")), "C", defaultDepartment, defaultPosition, "simulation");
        },

        onSlCurrentDepartmentChange: function (oEvent) {
            this._departmentFilterItemChange(this._getSelectedItemText(this._getSelect("slDivision")), this._getSelectedItemText(this._getSelect("slDepartment")), "C", defaultPosition, "simulation");
        },

        onSlNextDivisionChange: function (oEvent) {
            this._divisionFilterItemChange(this._getSelectedItemText(this._getSelect("slNextDivision")), "N", defaultDepartment, defaultPosition, "simulation");
        },

        onSlNextDepartmentChange: function (oEvent) {
            this._departmentFilterItemChange(this._getSelectedItemText(this._getSelect("slNextDivision")), this._getSelectedItemText(this._getSelect("slNextDepartment")), "N", defaultPosition, "simulation");
        },

        onSearch: function (oEvent) {
            var oView = this.getView();
            var oModel = oView.getModel("simulation");
            var oModelData = oModel.getData();
            var caseId = this._getSelectedItemText(this._getSelect("slCaseId"));

            if (!caseId) {
                MessageBox.error(this._getResourceText("ms_selectCaseId"));
                return;
            }

            var sendData = { caseID: caseId };
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
                    searchResult = that._searchCandidateId(searchResult, oModel.getProperty("/candidateID"));
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
                data: sendData,
                contentType: "application/json",
                success: function (result, xhr, data) {
                    oModelData.status = result;
                    oModel.setData(oModelData);
                    oModel.refresh(true);
                    that._createMessageStrip(result);
                    that._setButtonStatus(result);
                },
                error: function (xhr, status, err) { }
            });

            oModel.setData(oModelData);
            oModel.refresh(true);
            this.getView().setModel(oModel, "simulation");
        },

        onClear: function (oEvent) {
            var oModelData = this.getView().getModel("simulation").getData();
            oModelData.candidateID = "";
            oModelData.candidateName = "";
            this.getView().getModel("simulation").setData(oModelData);

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
            var oModel = oView.getModel("simulation");
            var oModelData = oModel.getData();
            var caseId = this._getSelectedItemText(this._getSelect("slCaseId"));

            if (!caseId) {
                MessageBox.error(this._getResourceText("ms_selectCaseId"));
                return;
            }

            var sendData = { caseID: caseId };
            var that = this;
            var check = function () {
                return $.ajax({
                    type: "POST",
                    data: caseId,
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
                        searchResult = that._searchCandidateId(searchResult, oModel.getProperty("/candidateID"));
                        searchResult = that._searchCandidateName(searchResult, oModel.getProperty("/candidateName"));

                        oModelData.list = searchResult;
                        oModel.setData(oModelData);
                        oModel.refresh(true);
                    },
                    error: function () {
                        that.byId("simulationCandidatesTable").setBusy(false);
                        that.byId("check").setEnabled(true);
                        that.byId("export").setEnabled(true);
                    }
                });
            };

            var getStatus = function () {
                return $.ajax({
                    url: "/srv_api/status",
                    data: sendData,
                    method: "GET",
                    contentType: "application/json",
                    success: function (result, xhr, data) {
                        oModelData.status = result;
                        that._createMessageStrip(result);
                        that._setButtonStatus(result);
                        oModel.setData(oModelData);
                        oModel.refresh(true);
                    },
                    error: function (xhr, status, err) { }
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
                    that.getView().setModel(oModel, "simulation");
                });
            dfd.resolve();
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
            var list = this.getView().getModel("simulation").getData().list;

            if (list[position].simulationCheckResult !== "") {
                // create popover
                if (!this._oPopover) {
                    Fragment.load({
                        name: "com.sap.sfsf.simulation.view.CheckResult",
                        controller: this
                    }).then(function (oPopover) {
                        this._oPopover = oPopover;
                        this.getView().addDependent(this._oPopover);
                        this._oPopover.bindElement({ path: "simulation>" + path, model: "simulation" });
                        this._oPopover.openBy(oControl);
                    }.bind(this));
                } else {
                    this._oPopover.bindElement({ path: "simulation>" + path, model: "simulation" });
                    this._oPopover.openBy(oControl);
                }
            }
        },

        onClosePress: function () {
            this._oPopover.close();
        },

        _createMessageStrip: function (result) {
            var oMs = sap.ui.getCore().byId("msgStrip");
            if (oMs) {
                oMs.destroy();
            }

            var statusText = "",
                statusType = "";

            switch(result.status){
                case "OK" :
                    statusText = this._getResourceText("ms_statusText1_i1") + result.TOTAL + " " + this._getResourceText("ms_statusText1_i2") + " " + this._getResourceText("ms_statusText1_i3") + " " + result.checkedDateTime;
                    statusType = "Success";
                    break;
                case "NG" :
                    statusText = this._getResourceText("ms_statusText1_e1") + result.NG + " " + this._getResourceText("ms_statusText1_e2") + " " + this._getResourceText("ms_statusText1_e3") + " " + result.checkedDateTime;
                    statusType = "Error";
                    break;
                case "WARN" :
                    statusText = this._getResourceText("ms_statusText1_w1") + result.WARN + " " + this._getResourceText("ms_statusText1_w2") + " " + this._getResourceText("ms_statusText1_w3") + " " + result.checkedDateTime;
                    statusType = "Warning";
                    break;
                case null :
                    statusText = this._getResourceText("ms_statusText1_i4");
                    statusType = "Information";
                    break;
                case "APPL" :
                    statusText = this._getResourceText("ms_statusText1_i5");
                    statusType = "Information";
                    break;
                case "APPD" :
                    statusText = this._getResourceText("ms_statusText1_i6");
                    statusType = "Information";
                    break;
                case "DENY" :
                    statusText = this._getResourceText("ms_statusText1_i7");
                    statusType = "Information";
                    break;
                default:
                    statusText = this._getResourceText("ms_statusText1_i4");
                    statusType = "Information";                    
            }

            if (statusText) {
                var oPage = this.byId("vlmessage");
                var oMsgStrip = new MessageStrip("msgStrip", {
                    text: statusText,
                    showCloseButton: false,
                    showIcon: true,
                    type: statusType
                });
                oPage.addContent(oMsgStrip);
            }
        },

        _setButtonStatus: function (result) {
            var oModel = new JSONModel();
            var oModelData = oModel.getData();
            if (result.status) {
                switch(result.status){
                    case null:
                        oModelData.checkEnablement = true;
                        oModelData.exportEnablement = false;
                        break;
                    case "OK":
                        oModelData.checkEnablement = false;
                        oModelData.exportEnablement = false;
                        break;
                    case "NG":
                        oModelData.checkEnablement = false;
                        oModelData.exportEnablement = true;
                        break;
                    case "WARN":
                        oModelData.checkEnablement = false;
                        oModelData.exportEnablement = true;
                        break;
                    default:
                        oModelData.checkEnablement = false;
                        oModelData.exportEnablement = false;                  
                    }
                oModel.setData(oModelData);
            }
            this.getView().setModel(oModel, "buttonStatus");
        }

    });
});