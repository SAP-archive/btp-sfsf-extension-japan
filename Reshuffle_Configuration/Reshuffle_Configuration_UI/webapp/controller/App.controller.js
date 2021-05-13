sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/base/Log",
	"sap/ui/model/json/JSONModel",
	"sap/ui/thirdparty/jquery",
    "sap/ui/core/Fragment",
    "sap/m/MessageBox",
    "sap/m/MessageToast"
], function (Controller, Log, JSONModel, jQuery, Fragment, MessageBox, MessageToast) {
	"use strict";
	return Controller.extend("com.sap.sfsf.configuration.controller.App", {
		
		configUrlBase: "/srv_api/",
		configContentType: "application/json",

		onAfterRendering: function () {
			//set busy indicators until initialization step finishes
			var aBusySwitchIds = [
				"pubNextDate",
				"pubTenureInput",
				"rateFormInput1",
				"rateFormInput2",
				"rateFormInput3",
			];
			this._switchBusyIndicators(aBusySwitchIds, true);

			//get configuration data
			this._retrieveConfigFromRemote();
			this._retrieveRateFormFromRemote();
		},
		
		onEditBtn: function () {
			var aVisibleSwitchIds = [
					"saveBtn",
					"cancelBtn"
				];
			var aEditableSwitchIds = [
					"pubNextDate",
					"pubTenureInput",
					"rateFormInput1",
					"rateFormInput2",
					"rateFormInput3"
				];
			var aEnableSwitchIds = [
				];
			this.getView().byId("editBtn").setVisible(false);
			this._switchVisibles(aVisibleSwitchIds, true);
			this._switchEditables(aEditableSwitchIds, true);
		},
		
		onCancelBtn: function () {
			var aVisibleSwitchIds = [
					"saveBtn",
					"cancelBtn"
				];
			var aEditableSwitchIds = [
					"pubNextDate",
					"pubTenureInput",
					"rateFormInput1",
					"rateFormInput2",
					"rateFormInput3"
				];
			var aEnableSwitchIds = [
				];
			this.getView().byId("editBtn").setVisible(true);
			this._retrieveConfigFromRemote();
		},
		
		onSaveBtn: function () {
			var oView = this.getView();
			var i18nModel = this.getView().getModel("i18n").getResourceBundle();

			var aInputs = [
				oView.byId("pubNextDate"),
				oView.byId("pubTenureInput")
			];
			var bValidationError = false;
			
			aInputs.forEach(function (oInput) {
				bValidationError = this._validateInput(oInput) || bValidationError;
			}, this);
			
			if (bValidationError) {
				var sValidationError = i18nModel.getText("validErr");
				MessageBox.alert(sValidationError);
			} else {
				//collect posting strings
				var sPayload = this._collectPostingString();
				this._postConfigDataToRemote(sPayload, function() {
					var aVisibleSwitchIds = [
						"saveBtn",
						"cancelBtn"
					];
					var aEditableSwitchIds = [
						"pubNextDate",
						"pubTenureInput",
						"rateFormInput1",
						"rateFormInput2",
						"rateFormInput3"
					];
					var aEnableSwitchIds = [
					];
					this.getView().byId("editBtn").setVisible(true);
					this._switchVisibles(aVisibleSwitchIds, false);
					this._switchEditables(aEditableSwitchIds, false);
				}.bind(this));
			}
		},
		
		_switchBusyIndicators: function (idArray, bool) {
			var oView = this.getView();
			idArray.forEach(function (sId) {
				oView.byId(sId).setBusy(bool);
			}, this);
		},
		
		_switchVisibles: function(idArray, bool) {
			var oView = this.getView();
			idArray.forEach(function (sId) {
				oView.byId(sId).setVisible(bool);
			}, this);
		},
		
		_switchEditables: function(idArray, bool) {
			var oView = this.getView();
			idArray.forEach(function (sId) {
				oView.byId(sId).setEditable(bool);
			}, this);
		},
		
		_switchEnables: function(idArray, bool) {
			var oView = this.getView();
			idArray.forEach(function (sId) {
				oView.byId(sId).setEnabled(bool);
			}, this);
		},
		
		_validateInput: function (oInput) {
			var sValueState = "None";
			var bValidationError = false;
			var oBinding = oInput.getBinding("value");

			try {
				oBinding.getType().validateValue(oInput.getValue());
			} catch (oException) {
				sValueState = "Error";
				bValidationError = true;
			}

			oInput.setValueState(sValueState);
			return bValidationError;
		},
		
		_retrieveConfigFromRemote: function() {
			var that = this;
			var i18nModel = this.getView().getModel("i18n").getResourceBundle();
			var sAjaxError = i18nModel.getText("ajaxError");
			
			jQuery.ajax({
				type: "GET",
				contentType: that.configContentType,
				url: that.configUrlBase + "config",
				dataType: "json",
				async: true,
				success: function (data, textStatus, jqXHR) {
					var oModel = new JSONModel(data);
					that.getView().setModel(oModel);
				},
				error: function () {
					MessageToast.show(sAjaxError);
				}
			}).always(function() {
				var aNotBusyConfigSwitchIds = [
					"pubNextDate",
					"pubTenureInput",
				];
				that._switchBusyIndicators(aNotBusyConfigSwitchIds, false);
			});
		},
		
		_retrieveRateFormFromRemote: function() {
			var that = this;
			var oView = this.getView();
			var i18nModel = this.getView().getModel("i18n").getResourceBundle();
			var sAjaxError = i18nModel.getText("ajaxError");

			jQuery.ajax({
				type: "GET",
				contentType: "application/json",
				url: "/srv_api/rateform",
				dataType: "json",
				async: true,
				success: function (data, textStatus, jqXHR) {
					var oRateFormModel = new JSONModel(data);
					oView.setModel(oRateFormModel, "rateform");
					
					var rateform1 = oView.getModel().getObject("/").rateFormKey1;
					var rateform2 = oView.getModel().getObject("/").rateFormKey2;
					var rateform3 = oView.getModel().getObject("/").rateFormKey3;
					oView.byId("rateFormInput1").setSelectedKey(rateform1);
					oView.byId("rateFormInput2").setSelectedKey(rateform2);
					oView.byId("rateFormInput3").setSelectedKey(rateform3);

					oView.byId("editBtn").setEnabled(true);
				},
				error: function () {
					MessageToast.show(sAjaxError);
				}
			}).always(function() {
				var aNotBusyRateFormSwitchIds = [
					"rateFormInput1",
					"rateFormInput2",
					"rateFormInput3"
				];
				that._switchBusyIndicators(aNotBusyRateFormSwitchIds, false);
			});	
		},
		
		_collectPostingString: function() {
			var oView = this.getView();
			var sDate = oView.byId("pubNextDate").getValue();
			var sSpan = oView.byId("pubTenureInput").getValue();
			var sRate1 = oView.byId("rateFormInput1").getSelectedKey();
			var sRate2 = oView.byId("rateFormInput2").getSelectedKey();
			var sRate3 = oView.byId("rateFormInput3").getSelectedKey();
			var sPostingJson = JSON.stringify({
				"startDateTime": sDate,
				"span": Number(sSpan),
				"rateFormKey1": sRate1,
				"rateFormKey2": sRate2,
				"rateFormKey3": sRate3
			});
			return sPostingJson;
		},
		
		_postConfigDataToRemote: function(sPayload, doneFunction) {
			var that = this;
			var i18nModel = this.getView().getModel("i18n").getResourceBundle();
			var sSuccess = i18nModel.getText("saveSuccess");
			var sError = i18nModel.getText("saveError");
			this.getView().setBusy(true);
			$.ajax({
				type: "PUT",
				contentType: "application/json",
				url: "/srv_api/config",
				data: sPayload,
				async: true,
				success: function (data, textStatus, jqXHR) {
					MessageToast.show(sSuccess);
					that.getView().setBusy(false);
				},
				error: function () {
					MessageToast.show(sError);
					that.getView().setBusy(false);
				}
			}).done(doneFunction);	
		}
	});
});
