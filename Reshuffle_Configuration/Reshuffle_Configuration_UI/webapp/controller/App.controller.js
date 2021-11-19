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
				"mtPresidentName",
				"mailEditor",
				"rateFormInput1",
				"rateFormInput2",
				"rateFormInput3",
				"CompThresholdSlider"
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
					"mtPresidentName",
					"mailEditor",
					"rateFormInput1",
					"rateFormInput2",
					"rateFormInput3"
				];
			var aEnableSwitchIds = [
					"mtBtnMarginConfig",
					"CompThresholdSlider"
				];
			this.getView().byId("editBtn").setVisible(false);
			this._switchVisibles(aVisibleSwitchIds, true);
			this._switchEditables(aEditableSwitchIds, true);
			this._switchEnables(aEnableSwitchIds, true);
		},
		
		onCancelBtn: function () {
			var aVisibleSwitchIds = [
					"saveBtn",
					"cancelBtn"
				];
			var aEditableSwitchIds = [
					"pubNextDate",
					"pubTenureInput",
					"mtPresidentName",
					"mailEditor",
					"rateFormInput1",
					"rateFormInput2",
					"rateFormInput3"
				];
			var aEnableSwitchIds = [
					"mtBtnMarginConfig",
					"CompThresholdSlider"
				];
			this.getView().byId("editBtn").setVisible(true);
			this._switchVisibles(aVisibleSwitchIds, false);
			this._switchEditables(aEditableSwitchIds, false);
			this._switchEnables(aEnableSwitchIds, false);
			
			this._retrieveConfigFromRemote();
		},
		
		onSaveBtn: function () {
			var oView = this.getView();
			var i18nModel = this.getView().getModel("i18n").getResourceBundle();

			var aInputs = [
				oView.byId("pubNextDate"),
				oView.byId("pubTenureInput"),
				oView.byId("mtPresidentName")
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
						"mtPresidentName",
						"mailEditor",
						"rateFormInput1",
						"rateFormInput2",
						"rateFormInput3"
					];
					var aEnableSwitchIds = [
						"mtBtnMarginConfig",
						"CompThresholdSlider"
					];
					this.getView().byId("editBtn").setVisible(true);
					this._switchVisibles(aVisibleSwitchIds, false);
					this._switchEditables(aEditableSwitchIds, false);
					this._switchEnables(aEnableSwitchIds, false);
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
					"mtPresidentName",
					"mailEditor",
					"CompThresholdSlider"
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
		
		onMailPreview: function (oEvt) {
			//This code was generated by the layout editor.
			var mailHtml = this.getView().byId("mailEditor").getValue();
			var i18nModel = this.getView().getModel("i18n").getResourceBundle();
			var viewerTitle = i18nModel.getText("viewerTitle");
			var closeBtnText = i18nModel.getText("closeBtn");
			
			var sPath = sap.ui.require.toUrl("com/sap/sfsf/configuration") + "/model/placeholder.json";
			var that = this;
			jQuery.ajax(sPath, {
				async: false,
				dataType: "json"
			}).done(function(data) {
				Object.keys(data).forEach(function (key) {
					var holderAndSample = this[key];
					var placeHolder = holderAndSample["placeholder"];
					var placeHolderRegExp = new RegExp(placeHolder);
					var sample = holderAndSample["sample"];
					mailHtml = mailHtml.replace(placeHolderRegExp, sample);
				}, data);

				that.oResizableDialog = new sap.m.Dialog({
					title: viewerTitle,
					contentWidth: "550px",
					contentHeight: "700px",
					resizable: true,
					content: new sap.ui.core.HTML({
						content: mailHtml
					}),
					endButton: new sap.m.Button({
						text: closeBtnText,
						press: function () {
							that.oResizableDialog.close();
						}.bind(that)
					})
				});

				//to get access to the controller's model
				that.getView().addDependent(that.oResizableDialog);
				that.oResizableDialog.open();
			});
		},
		
		_collectPostingString: function() {
			var oView = this.getView();
			var sDate = oView.byId("pubNextDate").getValue();
			var sSpan = oView.byId("pubTenureInput").getValue();
			var sMailTemplate = oView.byId("mailEditor").getValue();
			var sPresidentName = oView.byId("mtPresidentName").getValue();
			var sMailTemplate = oView.byId("mailEditor").getValue();
			var sRate1 = oView.byId("rateFormInput1").getSelectedKey();
			var sRate2 = oView.byId("rateFormInput2").getSelectedKey();
			var sRate3 = oView.byId("rateFormInput3").getSelectedKey();
			var sCompThreshold = oView.byId("CompThresholdSlider").getValue();

			var sPostingJson = JSON.stringify({
				"startDateTime": sDate,
				"span": Number(sSpan),
				"rateFormKey1": sRate1,
				"rateFormKey2": sRate2,
				"rateFormKey3": sRate3,
				"presidentName": sPresidentName,
				"mailTemplate": sMailTemplate,
				"competencyThreshold": Number(sCompThreshold)
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
		},

		onResizeEditor: function () {
			var oEditor = this.getView().byId("mailEditor");
			var oldHeight = oEditor.getHeight();
			if(oldHeight.match(/^[3-5]00px/)) {
				var oldHeightHead = oEditor.getHeight().slice(0, 1);
				var newHeightHead = parseInt(oldHeightHead, 10) + 2;
				var newHeight = newHeightHead + "00px";
				oEditor.setHeight(newHeight);
			} else {
				oEditor.setHeight("300px");
			}
		},
		
		onMarginConfig: function() {
			if (!this._oMarginDialog) {
				Fragment.load({
					name: "com.sap.sfsf.configuration.fragment.MarginDialog",
					controller: this
				}).then(function(oMarginDialog) {
					this._oMarginDialog = oMarginDialog;
					this.getView().addDependent(this._oMarginDialog);
					this._oMarginDialog.open();
				}.bind(this));
			} else {
				this._oMarginDialog.open();
			}
		},
		
		onMarginOk: function() {
			this._oMarginDialog.close();
			var iMarginSize = sap.ui.getCore().byId("marginInput").getValue();
			
			if(iMarginSize) {
				var oEditor = this.getView().byId("mailEditor");
				var isCustomFormatDefined = oEditor.getNativeApiTinyMCE4().formatter.get("margin");
				if(!isCustomFormatDefined) {
					oEditor.getNativeApiTinyMCE4().formatter.register("customMargin", {
						block: 'p',
						styles: {
							margin: '0px ' + iMarginSize + 'px auto'
    					}
					});
				}
				oEditor.getNativeApiTinyMCE4().formatter.apply("customMargin");
			} else {
				var i18nModel = this.getView().getModel("i18n").getResourceBundle();
				var sMarginInputErr = i18nModel.getText("miError");
				MessageToast.show(sMarginInputErr);
			}
						
		},

		onMarginCancel: function() {
			this._oMarginDialog.close();	
		},

		onInfoPopover: function (oEvt) {
			var oButton = oEvt.getSource();

			if (!this._oPopover) {
				Fragment.load({
					name: "com.sap.sfsf.configuration.fragment.MailPlaceHolderInfo",
					controller: this
				}).then(function(pPopover) {
					this._oPopover = pPopover;
					this.getView().addDependent(this._oPopover);
					this._oPopover.openBy(oButton);
				}.bind(this));
			} else {
				this._oPopover.openBy(oButton);
			}
		}
	});
});