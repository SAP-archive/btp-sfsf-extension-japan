sap.ui.define([
	"sap/ui/core/UIComponent",
	"sap/ui/Device",
	"sap/ui/model/json/JSONModel",
	"com/sap/sfsf/comparison/Reshuffle_Comparison_UI/model/models"
], function (UIComponent, Device, JSONModel, models) {
	"use strict";

	var defaultCurrentDepartment = {
		currentDepartment: "",
		currentDepartmentName: "ALL"
	};

	var defaultCurrentDivision = {
		currentDivision: "",
		currentDivisionName: "ALL"
	};

	var defaultCurrentPosition = {
		currentPosition: "",
		currentPositionName: "ALL"
	};
	var defaultNextDepartment = {
		nextDepartment: "",
		nextDepartmentName: "ALL"
	};

	var defaultNextDivision = {
		nextDivision: "",
		nextDivisionName: "ALL"
	};

	var defaultNextPosition = {
		nextPosition: "",
		nextPositionName: "ALL"
	};
	return UIComponent.extend("com.sap.sfsf.comparison.Reshuffle_Comparison_UI.Component", {

		metadata: {
			manifest: "json"
		},
		
		/**
		 * The component is initialized by UI5 automatically during the startup of the app and calls the init method once.
		 * @public
		 * @override
		 */
		init: function () {

			var oModel = new JSONModel();
			var oModelData = oModel.getData();

			var oViewModel = new JSONModel({
				loaded: false,
				divisionBusy: true,
				departmentBusy: true,
				positionBusy: true
			});

			var modelData = oModel.getData();
			var that = this;
			
			jQuery.ajax({
				url: "/config_api/jpconfig",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					var startDateTime = "（" + result.startDateTime + "付）";
					modelData.startDateTime = startDateTime;
					oModel.setData(modelData);
				},
				error: function (xhr, status, err) {
					Log.error(this.getView().getControllerName(), "/config_api/jpconfig");
				}
			});
			
			$.ajax({
				url: "/srv_api/candidates/currentdivisions",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					that.currentDivision = result.slice();
					that.currentDivision.unshift(defaultCurrentDivision);
					modelData.currentDivision = that.currentDivision;

					oViewModel.setProperty("/divisionBusy", false);
					oModel.setData(modelData);

				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/divisionBusy", false);
				}
            });
            
			$.ajax({
				url: "/srv_api/candidates/nextdivisions",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					that.nextDivision = result.slice();
					that.nextDivision.unshift(defaultNextDivision);
					modelData.nextDivision = that.nextDivision;

					oViewModel.setProperty("/divisionBusy", false);
					oModel.setData(modelData);

				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/divisionBusy", false);
				}
			});
			$.ajax({
				url: "/srv_api/candidates/currentdepartments",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					that.currentDepartment = result.slice();
					that.currentDepartment.unshift(defaultCurrentDepartment);
					modelData.currentDepartment = that.currentDepartment;

					oViewModel.setProperty("/departmentBusy", false);
					oModel.setData(modelData);

				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/departmentBusy", false);
				}
			});

            $.ajax({
				url: "/srv_api/candidates/nextdepartments",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					that.nextDepartment = result.slice();
					that.nextDepartment.unshift(defaultNextDepartment);
					modelData.nextDepartment = that.nextDepartment;

					oViewModel.setProperty("/departmentBusy", false);
					oModel.setData(modelData);

				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/departmentBusy", false);
				}
            });
            
			$.ajax({
				url: "/srv_api/candidates/currentpositions",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
                    that.curentPosition = result.slice();
                    that.curentPosition.unshift(defaultCurrentPosition);
                    modelData.currentPosition = that.curentPosition;

                    oViewModel.setProperty("/positionBusy", false);
                    oModel.setData(modelData);
				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/positionBusy", false);
				}
			});

            $.ajax({
				url: "/srv_api/candidates/nextpositions",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
                    that.nextPosition = result.slice();
                    that.nextPosition.unshift(defaultNextPosition);
                    modelData.nextPosition = that.nextPosition;

                    oViewModel.setProperty("/positionBusy", false);
                    oModel.setData(modelData);
				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/positionBusy", false);
				}
            });
            
			$.ajax({
				url: "/srv_api/candidates/caseids",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					var caseidList = [];
					// var caseidDefault = that._getResourceText("caseIdDefaultText");
					// caseidList.push({status:"", name:caseidDefault});
					for (var item of result){
						caseidList.push({status:item, name:item});
					}
					modelData.caseid = caseidList;
					oModel.setData(modelData);
				},
				error: function (xhr, status, err) {
				}
			});
			
			$.ajax({
				url: "/srv_api/candidates",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					var searchResult = result;
					oModelData.list = searchResult;
					oModelData.candidates = searchResult;
					oModel.setData(oModelData);
					oModel.refresh(true);
				},
				error: function (xhr, status, err) {
					console.log("candidates error");
				}
            });
            
			modelData.candidateId = "";
			modelData.candidateName = "";
			oModel.setData(oModelData);
			this.setModel(oModel);

			// call the base component's init function
			UIComponent.prototype.init.apply(this, arguments);

			// enable routing
			this.getRouter().initialize();

			// set the device model
			this.setModel(models.createDeviceModel(), "device");
		}

	});
});