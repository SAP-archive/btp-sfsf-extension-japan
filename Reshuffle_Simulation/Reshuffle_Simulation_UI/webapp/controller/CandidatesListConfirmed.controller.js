sap.ui.define([
    "sap/base/Log",
    "sap/m/Label",
    "sap/m/MessageBox",
    "sap/m/MessageToast",
    "sap/m/MessageStrip",
    "sap/ui/core/BusyIndicator",
    "sap/ui/core/Fragment",
    "sap/ui/core/mvc/Controller",
    "sap/ui/model/Filter",
    "sap/ui/model/FilterOperator",
    "sap/ui/model/json/JSONModel",
    "sap/ui/model/resource/ResourceModel",
    "sap/ui/table/library",
    "sap/ui/thirdparty/jquery",
    "./BaseController",
    "../model/formatter"
], function (Log, Label, MessageBox, MessageToast, MessageStrip, BusyIndicator, Fragment, Controller, Filter, FilterOperator, JSONModel,
    ResourceModel, library, jQuery, BaseController, formatter) {

    "use strict";

    var mailSentFlag = [{
        status: "",
        name: "ALL"
    }, {
        status: "-",
        name: "未送信"
    }, {
        status: "送信済",
        name: "送信済"
    }, {
        status: "送信失敗",
        name: "送信失敗"
    }];

    var defaultDepartment = {
        externalCode: "",
        name: "ALL"
    };

    var defaultPosition = {
        code: "",
        name: "ALL"
    };

    return BaseController.extend("com.sap.sfsf.simulation.controller.CandidatesListConfirmed", {
        formatter: formatter,
        onInit: function () {

            this.oModel = this.getOwnerComponent().getModel();
            this.modelData = this.oModel.getData();
            this.modelData.mailSentFlag = mailSentFlag;
            this.modelData.candidateID = "";
            this.modelData.candidateName = "";
            this.getView().setModel(this.oModel, "confirmation");

            this.oViewModel = this.getOwnerComponent().getModel();
            this.getView().getModel(this.oViewModel, "view");

            var oButtonStatusModel = new JSONModel();
            var oButtonStatusModelData = oButtonStatusModel.getData();
            oButtonStatusModelData.enableWfButton = false;
            oButtonStatusModelData.enablePDFButton = false;
            oButtonStatusModelData.enableSendMailButton = false;
            oButtonStatusModelData.enableSendSFSFButton = false;
            oButtonStatusModel.setData(oButtonStatusModelData);
            this.getView().setModel(oButtonStatusModel, "buttonStatus");
        },

        onSlCurrentDivisionChange: function (oEvent) {
            this._divisionFilterItemChange(this._getSelectedItemText(this._getSelect("slDivision")), "C", defaultDepartment, defaultPosition, "confirmation");
        },

        onSlCurrentDepartmentChange: function (oEvent) {
            this._departmentFilterItemChange(this._getSelectedItemText(this._getSelect("slDivision")), this._getSelectedItemText(this._getSelect("slDepartment")), "C", defaultPosition, "confirmation");
        },

        onSlNextDivisionChange: function (oEvent) {
            this._divisionFilterItemChange(this._getSelectedItemText(this._getSelect("slNextDivision")), "N", defaultDepartment, defaultPosition, "confirmation");
        },

        onSlNextDepartmentChange: function (oEvent) {
            this._departmentFilterItemChange(this._getSelectedItemText(this._getSelect("slNextDivision")), this._getSelectedItemText(this._getSelect("slNextDepartment")), "N", defaultPosition, "confirmation");
        },

        onSearch: function (oEvent) {
            var oView = this.getView();
            var oModel = oView.getModel("confirmation");
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

                    var searchResult = result;
                    searchResult = that._searchCaseId(searchResult, caseId);
                    searchResult = that._searchSentMailFlag(searchResult, that._getSelectedItemText(that._getSelect("slMailSent")));
                    searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slDivision")), "C");
                    searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slDepartment")), "C");
                    searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slPosition")), "C");
                    searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slNextDivision")), "N");
                    searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slNextDepartment")), "N");
                    searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slNextPosition")), "N");
                    searchResult = that._searchCandidateId(searchResult, oModel.getProperty("/candidateID"));
                    searchResult = that._searchCandidateName(searchResult, oModel.getProperty("/candidateName"));

                    oModelData.confirmationList = searchResult;
                    oModel.setData(oModelData);
                    oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                    MessageBox.error(that._getResourceText("ms_error"));
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
                error: function (xhr, status, err) {
                }
            });

            this.getView().setModel(oModel, "confirmation");
        },

        onClear: function (oEvent) {
            this._getSelect("slCaseId").setSelectedKey(null);
            this._getSelect("slCaseId").setSelectedKey("");
            this._getSelect("slMailSent").setSelectedKey("");
            this._getSelect("slDivision").setSelectedKey("");
            this._getSelect("slDepartment").setSelectedKey("");
            this._getSelect("slPosition").setSelectedKey("");
            this._getSelect("slNextDivision").setSelectedKey("");
            this._getSelect("slNextDepartment").setSelectedKey("");
            this._getSelect("slNextPosition").setSelectedKey("");

            var oModelData = this.getView().getModel("confirmation").getData();
            oModelData.candidateID = "";
            oModelData.candidateName = "";
            this.getView().getModel("confirmation").setData(oModelData);
        },

        onPressSendMail: function (oEvent) {
            var aIndices = this.byId("idCandidatesTable").getSelectedIndices();
            if (aIndices.length === 0) {
                MessageBox.error(this._getResourceText("ms_error_select"));
                return;
            } else {
                var oButton = oEvent.getSource();
                if (!this._oPopover) {
                    Fragment.load({
                        id: "sendMailFragment",
                        name: "com.sap.sfsf.simulation.view.SendMail",
                        controller: this
                    }).then(function (oPopover) {
                        this._oPopover = oPopover;
                        this.getView().addDependent(this._oPopover);

                        //メール送信日のデフォルト値＝発令日
                        $.ajax({
                            url: "/config_api/config",
                            method: "GET",
                            contentType: "application/json",
                            success: function (result, xhr, data) {
                                Fragment.byId("sendMailFragment", "sendMailDate").setDateValue(new Date(result.startDateTime));
                            },
                            error: function (xhr, status, err) {
                                Log.error(this.getView().getControllerName(), "/config_api/config");
                                Fragment.byId("sendMailFragment", "sendMailDate").setDateValue(new Date());
                            }
                        });

                        this._oPopover.openBy(oButton);
                    }.bind(this));
                } else {
                    this._oPopover.openBy(oButton);
                }
            }
        },

        onSendMail: function (oEvent) {
            this._oPopover.close();
            BusyIndicator.show(0);

            var oView = this.getView();
            var oModel = oView.getModel("confirmation");
            var oModelData = oModel.getData();

            var aIndices = this.byId("idCandidatesTable").getSelectedIndices();
            var candidatesList = [];
            for (var item in aIndices) {
                var caseId = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).caseID;
                var candidateId = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).candidateID;
                var nextPosition = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).nextPosition;
                candidatesList.push({ caseID: caseId, candidateID: candidateId, nextPosition: nextPosition });
            }
            var sendData = JSON.stringify(candidatesList);
            var that = this;

            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/srv_api/mail",
                data: sendData,
                dataType: "json",
                async: true,
                success: function (data, textStatus, jqXHR) {
                    BusyIndicator.hide();
                    var mailStatus = jqXHR.getResponseHeader("mail_status");
                    if (mailStatus === "success") {
                        MessageBox.information(that._getResourceText("ms_send_mail"));
                    } else if (mailStatus === "fail") {
                        MessageBox.error(that._getResourceText("ms_send_mail_error"));
                    }
                    var searchResult = data;
                    searchResult = that._searchCaseId(searchResult, that._getSelectedItemText(that._getSelect("slCaseId")));
                    searchResult = that._searchSentMailFlag(searchResult, that._getSelectedItemText(that._getSelect("slMailSent")));
                    searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slDivision")), "C");
                    searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slDepartment")), "C");
                    searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slPosition")), "C");
                    searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slNextDivision")), "N");
                    searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slNextDepartment")), "N");
                    searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slNextPosition")), "N");
                    searchResult = that._searchCandidateId(searchResult, oModel.getProperty("/candidateID"));
                    searchResult = that._searchCandidateName(searchResult, oModel.getProperty("/candidateName"));

                    oModelData.confirmationList = searchResult;
                    oModel.setData(oModelData);
                    oModel.refresh(true);
                },
                error: function () {
                    BusyIndicator.hide();
                }
            });

            this.getView().setModel(oModel, "confirmation");
        },

        onSendMailJob: function (oEvent) {
            this._oPopover.close();
            BusyIndicator.show(0);

            var oView = this.getView();
            var oModel = oView.getModel("confirmation");
            var oModelData = oModel.getData();

            var aIndices = this.byId("idCandidatesTable").getSelectedIndices();

            var sendMailDate = Fragment.byId("sendMailFragment", "sendMailDate").getDateValue();
            var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({ pattern: "yyyyMMddHHmm" });
            var formatDate = dateFormat.format(sendMailDate);
            var headers = { startDateTime: formatDate };

            var candidatesList = [];
            for (var item in aIndices) {
                var caseId = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).caseID;
                var candidateId = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).candidateID;
                var nextPosition = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).nextPosition;
                candidatesList.push({ caseID: caseId, candidateID: candidateId, nextPosition: nextPosition });
            }
            var sendData = JSON.stringify(candidatesList);
            var that = this;

            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/srv_api/mailjob",
                headers: headers,
                data: sendData,
                dataType: "json",
                async: true,
                success: function (data, textStatus, jqXHR) {
                    BusyIndicator.hide();
                    var mailjobStatus = jqXHR.getResponseHeader("mailjob_status");
                    if (mailjobStatus === "success") {
                        MessageBox.information(that._getResourceText("ms_arrange_send_mail"));
                    } else if (mailjobStatus === "fail") {
                        MessageBox.information(that._getResourceText("ms_arrange_send_mail_error"));
                    }

                    var searchResult = data;
                    searchResult = that._searchCaseId(searchResult, that._getSelectedItemText(that._getSelect("slCaseId")));
                    searchResult = that._searchSentMailFlag(searchResult, that._getSelectedItemText(that._getSelect("slMailSent")));
                    searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slDivision")), "C");
                    searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slDepartment")), "C");
                    searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slPosition")), "C");
                    searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slNextDivision")), "N");
                    searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slNextDepartment")), "N");
                    searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slNextPosition")), "N");
                    searchResult = that._searchCandidateId(searchResult, oModel.getProperty("/candidateID"));
                    searchResult = that._searchCandidateName(searchResult, oModel.getProperty("/candidateName"));

                    oModelData.confirmationList = searchResult;        
                    oModel.setData(oModelData);
                    oModel.refresh(true);
                },
                error: function () {
                    BusyIndicator.hide();
                }
            });

            this.getView().setModel(oModel, "confirmation");
        },

        onSendSFSF: function (oEvent) {
            BusyIndicator.show(0);

            var oView = this.getView();
            var oModel = oView.getModel("confirmation");
            var oModelData = oModel.getData();
            var candidatesList = [];
            for (var item in oModelData.confirmationList) {
                var caseId = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).caseID;
                var candidateId = oModelData.confirmationList[item].candidateID;
                var nextPosition = oModelData.confirmationList[item].nextPosition;
                candidatesList.push({ caseID: caseId, candidateID: candidateId, nextPosition: nextPosition });
            }
            var sendData = JSON.stringify(candidatesList);

            var that = this;
            MessageBox.confirm(this._getResourceText("ms_confirm_send_sfsf"), {
                actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
                emphasizedAction: MessageBox.Action.OK,
                onClose: function (sAction) {
                    if (sAction !== "OK") {
                        BusyIndicator.hide();
                        return;
                    } else {

                        $.ajax({
                            type: "POST",
                            contentType: "application/json",
                            url: "/srv_api/upsert",
                            data: sendData,
                            dataType: "json",
                            async: true,
                            success: function (data, textStatus, jqXHR) {
                                BusyIndicator.hide();
                                var upsertStatus = jqXHR.getResponseHeader("upsert_status");
                                if (upsertStatus === "success") {
                                    MessageBox.information(that._getResourceText("ms_send_sfsf"));
                                } else if (upsertStatus === "fail") {
                                    MessageBox.error(that._getResourceText("ms_send_sfsf_error"));
                                }

                                var searchResult = data;
                                searchResult = that._searchCaseId(searchResult, that._getSelectedItemText(that._getSelect("slCaseId")));
                                searchResult = that._searchSentMailFlag(searchResult, that._getSelectedItemText(that._getSelect("slMailSent")));
                                searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slDivision")), "C");
                                searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slDepartment")), "C");
                                searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slPosition")), "C");
                                searchResult = that._searchDivision(searchResult, that._getSelectedItemText(that._getSelect("slNextDivision")), "N");
                                searchResult = that._searchDepartment(searchResult, that._getSelectedItemText(that._getSelect("slNextDepartment")), "N");
                                searchResult = that._searchPosition(searchResult, that._getSelectedItemText(that._getSelect("slNextPosition")), "N");
                                searchResult = that._searchCandidateId(searchResult, oModel.getProperty("/candidateID"));
                                searchResult = that._searchCandidateName(searchResult, oModel.getProperty("/candidateName"));

                                oModelData.confirmationList = searchResult;
                                oModel.setData(oModelData);
                                oModel.refresh(true);
                            },
                            error: function () {
                                BusyIndicator.hide();
                            }
                        });
                        that.getView().setModel(oModel, "confirmation");
                    }
                }
            });
        },
        _createMessageStrip: function (result) {
            var oMs = sap.ui.getCore().byId("msgStrip");
            if (oMs) {
                oMs.destroy();
            }

            var statusText = "",
                statusType = "";

            if (result.hasOwnProperty("status")) {
                if (result.status === "NG") {
                    statusText = this._getResourceText("ms_statusText2_e1") + result.NG + " " + this._getResourceText("ms_statusText2_e2") + " " + this._getResourceText("ms_statusText2_e3") + result.checkedDateTime;
                    statusType = "Error";
                } else if (result.status === "WARN") {
                    statusText = this._getResourceText("ms_statusText2_w1") + result.WARN + " " + this._getResourceText("ms_statusText2_w2") + " " + this._getResourceText("ms_statusText2_w3") + result.checkedDateTime;
                    statusType = "Warning";
                } else if (result.status === "APPL") {
                    statusText = this._getResourceText("ms_statusText1_i5");
                    statusType = "Information";
                } else if (result.status === "APPD") {
                    statusText = this._getResourceText("ms_statusText1_i6");
                    statusType = "Information";
                } else if (result.status === "DENY") {
                    statusText = this._getResourceText("ms_statusText1_i7");
                    statusType = "Information";
                } else if (result.status === null) {
                    statusText = this._getResourceText("ms_statusText2_e4");
                    statusType = "Information";
                }
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
            var modelData = oModel.getData();
            if (result.status) {
                switch (result.status) {
                    case "OK":
                        modelData.enableWfButton = true;
                        modelData.enablePDFButton = false;
                        modelData.enableSendMailButton = false;
                        modelData.enableSendSFSFButton = false;
                        break;
                    case "WARN":
                        modelData.enableWfButton = false;
                        modelData.enablePDFButton = false;
                        modelData.enableSendMailButton = false;
                        modelData.enableSendSFSFButton = false;
                        break;
                    case "NG":
                        modelData.enableWfButton = false;
                        modelData.enablePDFButton = false;
                        modelData.enableSendMailButton = false;
                        modelData.enableSendSFSFButton = false;
                        break;
                    case "APPL":
                        modelData.enableWfButton = false;
                        modelData.enablePDFButton = false;
                        modelData.enableSendMailButton = false;
                        modelData.enableSendSFSFButton = false;
                        break;
                    case "DENY":
                        modelData.enableWfButton = false;
                        modelData.enablePDFButton = false;
                        modelData.enableSendMailButton = false;
                        modelData.enableSendSFSFButton = false;
                        break;
                    case "APPD":
                        modelData.enableWfButton = false;
                        modelData.enablePDFButton = true;
                        modelData.enableSendMailButton = true;
                        modelData.enableSendSFSFButton = true;
                        break;
                    default:
                        modelData.enableWfButton = false;
                        modelData.enablePDFButton = false;
                        modelData.enableSendMailButton = false;
                        modelData.enableSendSFSFButton = false;
                }
                oModel.setData(modelData);
            }
            this.getView().setModel(oModel, "buttonStatus");
        },

        onWF: function (oEvent) {

            BusyIndicator.show(0);
            var oView = this.getView();
            var oModel = oView.getModel("confirmation");
            var oModelData = oModel.getData();
            var caseId = this._getSelectedItemText(this._getSelect("slCaseId"));

            if (!caseId) {
                MessageBox.error(this._getResourceText("ms_selectCaseId"));
                return;
            }

            var sendData = { caseID: caseId };
            var that = this;

            $.ajax({
                type: "POST",
                contentType: "text/plain",
                url: "/srv_api/workflow",
                data: caseId,
                async: true,
                success: function (data, textStatus, jqXHR) {
                    BusyIndicator.hide();
                    oModelData.status.status = "APPL";
                    oModel.setData(oModelData);
                    oModel.refresh(true);
                    that._createMessageStrip(oModelData.status);
                    that._setButtonStatus( oModelData.status);
                    MessageBox.information(that._getResourceText("ms_wf_start"));
                },
                error: function () {
                    BusyIndicator.hide();
                    MessageBox.error(that._getResourceText("ms_wf_failure"));
                }
            });
            this.getView().setModel(oModel, "confirmation");
        },

        onPDF: function (oEvent) {
            BusyIndicator.show(0);

            var oView = this.getView();
            var oModel = oView.getModel("confirmation");

            var aIndices = this.byId("idCandidatesTable").getSelectedIndices();
            if (aIndices.length === 0) {
                MessageBox.error(this._getResourceText("ms_error_select"));
                BusyIndicator.hide();
                return;
            }

            var candidatesList = [];
            for (var item in aIndices) {
                var candidateId = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).candidateID;
                var candidateName = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).candidateName + " 殿";
                var candidateDepartmentName = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).candidateDepartmentName;
                var candidateDivisionName = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).candidateDivisionName;
                var candidatePositionName = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).candidatePositionName;
                var departmentName = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).departmentName;
                var divisionName = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).divisionName;
                var positionName = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).positionName;
                candidatesList.push(
                    {
                        candidateID: candidateId,
                        candidateName: candidateName,
                        candidateDepartmentName: candidateDepartmentName,
                        candidateDivisionName: candidateDivisionName,
                        candidatePositionName: candidatePositionName,
                        departmentName: departmentName,
                        divisionName: divisionName,
                        positionName: positionName
                    });
            }
            var sendData = JSON.stringify(candidatesList);

            var that = this;
            var oXHR = new XMLHttpRequest();
            oXHR.open("POST", "/srv_api/pdf");
            oXHR.setRequestHeader("Content-Type", "application/json; charset=utf8");
            oXHR.responseType = "blob";
            oXHR.onload = function () {
                if (oXHR.status < 400 && oXHR.response && oXHR.response.size > 0) {
                    BusyIndicator.hide();
                    var sHeaderContentDisposition = decodeURIComponent(oXHR.getResponseHeader("Content-Disposition"));
                    var aHeaderParts = sHeaderContentDisposition.split("filename=");
                    var sFilenameFromServer = aHeaderParts[1];
                    if (sap.ui.Device.browser.msie) {
                        window.navigator.msSaveOrOpenBlob(oXHR.response, sFilenameFromServer);
                    } else {
                        var oA = document.createElement("a");
                        oA.href = window.URL.createObjectURL(oXHR.response);
                        oA.style.display = "none";
                        oA.download = "辞令.pdf";
                        document.body.appendChild(oA);
                        oA.click();
                        document.body.removeChild(oA);
                        // setTimeout is needed for safari on iOS
                        setTimeout(function () {
                            window.URL.revokeObjectURL(oA.href);
                        }, 250);
                    }
                } else {
                    BusyIndicator.hide();
                    MessageBox.error(that._getResourceText("ms_error"));
                }
            }.bind(this);
            oXHR.send(sendData);
        },

        _searchSentMailFlag: function (searchResult, key) {
            if (key === "") {
                return searchResult;
            } else if (key === "-") {
                return searchResult.filter(function (item, index) {
                    if (item.mailSentFlg === null) {
                        return true;
                    } else {
                        return false;
                    }
                });
            } else {
                return searchResult.filter(function (item, index) {
                    if (item.mailSentFlg === key) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }
        }

    });
});