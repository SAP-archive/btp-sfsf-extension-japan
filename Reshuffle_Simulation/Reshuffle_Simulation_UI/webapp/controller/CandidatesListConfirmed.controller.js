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

	var defaultDivision = {
		externalCode: "",
		name: "ALL"
	};

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
				url: "/srv_api/status",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					modelData.status = result;
				},
				error: function (xhr, status, err) {
					MessageBox.error("division error");
				}
			});
			
			modelData.mailSentFlag = mailSentFlag;
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
			var that = this;

			$.ajax({
				url: "/srv_api/list",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {

					var searchResult = result;
					searchResult = that.searchSentMailFlag(searchResult, that._getSelectedItemText(that._getSelect("slMailSent")));
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
				contentType: "application/json",
				success: function (result, xhr, data) {
					oModelData.status = result;
					that._createMessageStrip(result);
				},
				error: function (xhr, status, err) {
					MessageBox.error("division error");
				}
			});
			
			this.getView().setModel(oModel);
		},
		
		searchSentMailFlag: function (searchResult, key) {
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
		},
		
		onClear: function (oEvent) {
			this._getSelect("slMailSent").setSelectedKey("");
			this._getSelect("slDivision").setSelectedKey("");
			this._getSelect("slDepartment").setSelectedKey("");
			this._getSelect("slPosition").setSelectedKey("");
			this._getSelect("slNextDivision").setSelectedKey("");
			this._getSelect("slNextDepartment").setSelectedKey("");
			this._getSelect("slNextPosition").setSelectedKey("");

			var oModelData = this.getView().getModel().getData();
			oModelData.candidateId = "";
			oModelData.candidateName = "";
			this.getView().getModel().setData(oModelData);
		},
		
		onPressSendMail: function(oEvent){
			var aIndices = this.byId("idCandidatesTable").getSelectedIndices();
			if (aIndices.length === 0) {
				MessageBox.error(this._getResourceText("ms_error_select"));
				return;
			} else {
				var oButton = oEvent.getSource();
				if (!this._oPopover) {
					Fragment.load({
						id:"sendMailFragment",
						name: "com.sap.sfsf.simulation.view.SendMail",
						controller: this
					}).then(function(oPopover){
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
		
		onSendMail: function(oEvent){
			this._oPopover.close();
			BusyIndicator.show(0);
			
			var oView = this.getView();
			var oModel = oView.getModel();
			var oModelData = oView.getModel().getData();
			
			var aIndices = this.byId("idCandidatesTable").getSelectedIndices();
			var candidatesList = [];
			for (var item in aIndices){
				var candidateId = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).candidateID;
				var nextPosition = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).nextPosition;
				candidatesList.push({candidateID:candidateId, nextPosition:nextPosition});
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
					if(mailStatus === "success"){
						MessageBox.information(that._getResourceText("ms_send_mail"));
					}else if (mailStatus === "fail"){
						MessageBox.error(that._getResourceText("ms_send_mail_error"));
					}
					var searchResult = data;
					searchResult = that.searchSentMailFlag(searchResult, that._getSelectedItemText(that._getSelect("slMailSent")));
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
					BusyIndicator.hide();
				}
			});

			oModel.setData(oModelData);
			oModel.refresh(true);
			this.getView().setModel(oModel);
		},

		onSendMailJob : function(oEvent){
			this._oPopover.close();
			BusyIndicator.show(0);
			
			var oView = this.getView();
			var oModel = oView.getModel();
			var oModelData = oView.getModel().getData();
			
			var aIndices = this.byId("idCandidatesTable").getSelectedIndices();
			
			var sendMailDate = Fragment.byId("sendMailFragment", "sendMailDate").getDateValue();
			var dateFormat = sap.ui.core.format.DateFormat.getDateInstance({pattern : "yyyyMMddHHmm" }); 
			var formatDate = dateFormat.format(sendMailDate);
			var headers = {startDateTime: formatDate};

			var candidatesList = [];
			for (var item in aIndices){
				var candidateId = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).candidateID;
				var nextPosition = oModel.getObject(this.byId("idCandidatesTable").getContextByIndex(aIndices[item]).sPath).nextPosition;
				candidatesList.push({candidateID:candidateId, nextPosition:nextPosition});
			}
			var sendData = JSON.stringify(candidatesList);
			var that = this;
			
			$.ajax({
				type: "POST",
				contentType: "application/json",
				url: "/srv_api/mailjob",
				headers:headers,
				data: sendData,
				dataType: "json",
				async: true,
				success: function (data, textStatus, jqXHR) {
					BusyIndicator.hide();
					var mailjobStatus = jqXHR.getResponseHeader("mailjob_status");
					if(mailjobStatus === "success"){
						MessageBox.information(that._getResourceText("ms_arrange_send_mail"));
					}else if (mailjobStatus === "fail"){
						MessageBox.information(that._getResourceText("ms_arrange_send_mail_error"));
					}

					var searchResult = data;
					searchResult = that.searchSentMailFlag(searchResult, that._getSelectedItemText(that._getSelect("slMailSent")));
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
					BusyIndicator.hide();
				}
			});

			oModel.setData(oModelData);
			oModel.refresh(true);
			this.getView().setModel(oModel);
		},
		
		onSendSFSF: function (oEvent) {
			BusyIndicator.show(0);
			
			var oView = this.getView();
			var oModel = oView.getModel();
			var oModelData = oModel.getData();
			var candidatesList = []; 
			for (var item in oModelData.list){
				var candidateId = oModelData.list[item].candidateID;
				var nextPosition = oModelData.list[item].nextPosition;
				candidatesList.push({candidateID:candidateId, nextPosition:nextPosition});
			}
			var sendData = JSON.stringify(candidatesList);

			var that = this;
			MessageBox.confirm(this._getResourceText("ms_confirm_send_sfsf"), {
				actions: [MessageBox.Action.OK, MessageBox.Action.CANCEL],
				emphasizedAction: MessageBox.Action.OK,
				onClose: function (sAction) {
					if(sAction !== "OK"){
						BusyIndicator.hide();
						return;
					}else{
						
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
								if(upsertStatus === "success"){
									MessageBox.information(that._getResourceText("ms_send_sfsf"));
								}else if (upsertStatus === "fail"){
									MessageBox.error(that._getResourceText("ms_send_sfsf_error"));
								}
								
								var searchResult = data;
								searchResult = that.searchSentMailFlag(searchResult, that._getSelectedItemText(that._getSelect("slMailSent")));
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
								BusyIndicator.hide();
							}
						});
			
						oModel.setData(oModelData);
						oModel.refresh(true);
						that.getView().setModel(oModel);
					}
				}
			});
		},
		_createMessageStrip: function(result){
			var oMs = sap.ui.getCore().byId("msgStrip");
			if (oMs) {
				oMs.destroy();
			}
			
			var statusText = "",
			    statusType = "";

			if (result.hasOwnProperty("status")) {
				if (result.status === "NG"){
					statusText = this._getResourceText("ms_statusText2_e1") + result.NG + " " + this._getResourceText("ms_statusText2_e2") + " " + this._getResourceText("ms_statusText2_e3") + result.checkedDateTime;
					statusType ="Error";
				}else if(result.status === "WARN"){
					statusText = this._getResourceText("ms_statusText2_w1") + result.WARN + " " +this._getResourceText("ms_statusText2_w2") + " " +  this._getResourceText("ms_statusText2_w3") + result.checkedDateTime;
					statusType ="Warning";
				}
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