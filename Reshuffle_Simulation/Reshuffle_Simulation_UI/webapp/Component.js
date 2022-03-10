sap.ui.define([
    "sap/ui/core/UIComponent",
    "sap/ui/model/json/JSONModel"
], function (UIComponent, JSONModel) {
    "use strict";

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

    return UIComponent.extend("com.sap.sfsf.simulation.Component", {
        metadata: {
            manifest: "json"
        },
        init: function () {

			var oModel = new JSONModel();
			this.division = [];
			this.department = [];
			this.position = [];

            var oViewModel = new JSONModel({
				loaded: false,
				divisionBusy: true,
				departmentBusy: true,
				positionBusy: true
			});

            var modelData = oModel.getData();
			var that = this;

			$.ajax({
				url: "/srv_api/division",
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
                    if(result){
                        that.division = result.slice();
                        that.division.unshift(defaultDivision);
                        modelData.division = that.division;
                        modelData.currentDivision = that.division;
                        modelData.nextDivision = that.division;    
                    }
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
                    if(result){
                        that.department = result.slice();
                        that.department.unshift(defaultDepartment);
                        modelData.department = that.department;
                        modelData.currentDepartment = that.department;
                        modelData.nextDepartment = that.department;
                    }

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
                    if(result.length>0){
                        that.position = result.slice();
                        that.position.unshift(defaultPosition);
                        modelData.position = that.position;
                        modelData.currentPosition = that.position;
                        modelData.nextPosition = that.position;
                    }

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

            this.setModel(oModel);
            this.setModel(oViewModel,"view")

            // call the init function of the parent
            UIComponent.prototype.init.apply(this, arguments);
        }
    });
}
);
