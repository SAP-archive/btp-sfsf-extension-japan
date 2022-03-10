sap.ui.define([
	"sap/ui/core/UIComponent",
	"sap/ui/Device",
	"sap/ui/model/json/JSONModel",
	"com/sap/sfsf/comparison/Reshuffle_Comparison_UI/model/models"
], function (UIComponent, Device, JSONModel, models) {
	"use strict";

	var defaultCandidateDepartment = {
		candidateDepartment: "",
		candidateDepartmentName: "ALL"
	};

	var defaultCandidateDivision = {
		candidateDivision: "",
		candidateDivisionName: "ALL"
	};

	var defaultCandidatePosition = {
		candidatePosition: "",
		candidatePositionName: "ALL"
	};
	var defaultDepartment = {
		department: "",
		departmentName: "ALL"
	};

	var defaultDivision = {
		division: "",
		divisionName: "ALL"
	};

	var defaultPosition = {
		position: "",
		positionName: "ALL"
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
				url: "/srv_api/candidates/candidatedivisions",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					that.candidateDivision = result.slice();
					that.candidateDivision.unshift(defaultCandidateDivision);
					modelData.candidateDivision = that.candidateDivision;

					oViewModel.setProperty("/divisionBusy", false);
					oModel.setData(modelData);

				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/divisionBusy", false);
				}
            });
            
			$.ajax({
				url: "/srv_api/candidates/divisions",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					that.division = result.slice();
					that.division.unshift(defaultDivision);
					modelData.division = that.division;

					oViewModel.setProperty("/divisionBusy", false);
					oModel.setData(modelData);

				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/divisionBusy", false);
				}
			});
			$.ajax({
				url: "/srv_api/candidates/candidatedepartments",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					that.candidateDepartment = result.slice();
					that.candidateDepartment.unshift(defaultCandidateDepartment);
					modelData.candidateDepartment = that.candidateDepartment;

					oViewModel.setProperty("/departmentBusy", false);
					oModel.setData(modelData);

				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/departmentBusy", false);
				}
			});

            $.ajax({
				url: "/srv_api/candidates/departments",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					that.department = result.slice();
					that.department.unshift(defaultDepartment);
					modelData.department = that.department;

					oViewModel.setProperty("/departmentBusy", false);
					oModel.setData(modelData);

				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/departmentBusy", false);
				}
            });
            
			$.ajax({
				url: "/srv_api/candidates/candidatepositions",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
                    that.candidatePosition = result.slice();
                    that.candidatePosition.unshift(defaultCandidatePosition);
                    modelData.candidatePosition = that.candidatePosition;

                    oViewModel.setProperty("/positionBusy", false);
                    oModel.setData(modelData);
				},
				error: function (xhr, status, err) {
					oViewModel.setProperty("/positionBusy", false);
				}
			});

            $.ajax({
				url: "/srv_api/candidates/positions",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
                    that.position = result.slice();
                    that.position.unshift(defaultPosition);
                    modelData.position = that.position;

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