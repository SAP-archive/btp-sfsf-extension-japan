sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/base/Log",
	"sap/ui/model/json/JSONModel"
], function(Controller, Log, JSONModel){
	"use strict";
	return Controller.extend("com.sap.sfsf.simulation.controller.App", {
		onInit: function(){
			Log.info(this.getView().getControllerName(), "onInit");

			this.getOwnerComponent().getRouter().attachRouteMatched(this._onRouteMatched, this);
			this.getOwnerComponent().getRouter().attachBypassed(this._onBypassed, this);
			
			var oModel = new JSONModel();
			this.getView().setModel(oModel);
			jQuery.ajax({
				url: "/config_api/jpconfig",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					var modelData = oModel.getData();
					var startDateTime = "（" + result.startDateTime + "付）";
					modelData.startDateTime = startDateTime;
					oModel.setData(modelData);
				},
				error: function (xhr, status, err) {
					Log.error(this.getView().getControllerName(), "/config_api/jpconfig");
				}
			});

		},

		_onRouteMatched: function(oEvent) {
			Log.info(this.getView().getControllerName(), "_onRouteMatched");
			var oConfig = oEvent.getParameter("config");

			// select the corresponding item in the left menu
			this.setSelectedMenuItem(oConfig.name);
		},

		setSelectedMenuItem: function(sKey) {
			// this.byId("OverflowToolbarLayoutData").setSelectedKey(sKey);
		},

		_onBypassed: function(oEvent) {
			var sHash = oEvent.getParameter("hash");
			Log.info(
				this.getView().getControllerName(),
				"_onBypassed Hash=" + sHash
			);
		// },

		// onItemSelect: function(oEvent) {
		// 	var sKey = oEvent.getParameter("item").getKey();
		// 	Log.info(this.getView().getControllerName(), "onItemSelect Key=" + sKey);
		// 	sKey = "simulationcheck";
		// 	this.getOwnerComponent().getRouter().navTo("simulationcheck");
		// },
		
		// onCollapseExpandPress: function () {
		// 	var oToolPage = this.byId("toolPage");
		// 	oToolPage.setSideExpanded(!oToolPage.getSideExpanded());
		// }, 
		
		// onHomePress: function(){
		// 	this.getOwnerComponent().getRouter().navTo("home");
		}
	});
});
