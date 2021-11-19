sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/ui/core/UIComponent",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterOperator",
    "sap/ui/core/Fragment"
], function (Controller, UIComponent, Filter, FilterOperator, Fragment) {
	"use strict";

	return Controller.extend("com.sap.sfsf.simulation.controller.BaseController", {
		 _getResourceText: function (sKey) {
            return this.getView().getModel("i18n").getResourceBundle().getText(sKey);
        },
        
        _divisionFilterItemChange: function (selectedDivision, key, defaultDepartment, defaultPosition) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
			var department = [];
			var position = [];

			if (selectedDivision !== "") {
				
				department = this._getFilteredDepartment(selectedDivision);
				department.unshift(defaultDepartment);

				position = this._getFilteredPosition(selectedDivision, "");
				position.unshift(defaultPosition);
				
				if(key === "C"){
					modelData.currentDepartment = department;
					modelData.currentPosition = position;
					
				}else{
					modelData.nextDepartment = department;
					modelData.nextPosition = position;
				}
				
			} else {
				if(key === "C"){
					modelData.currentDepartment = this.department;
					modelData.currentPosition = this.position;
					
				}else{
					modelData.nextDepartment = this.department;
					modelData.nextPosition = this.position;
				}
			}
			oModel.refresh(true);
			this.getView().setModel(oModel);
		},

		_departmentFilterItemChange: function (selectedDivision, selectedDepartment, key, defaultPosition) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
			var position = [];

			if (selectedDepartment !== "") {
				position = this._getFilteredPosition(selectedDivision, selectedDepartment);
				position.unshift(defaultPosition);
				if(key==="C"){
					modelData.currentPosition = position;
				}else{
					modelData.nextPosition = position;
				}
				oModel.refresh(true);
			} else {
				if(key==="C"){
					modelData.currentPosition = this.position;
				}else{
					modelData.nextPosition = this.position;
				}
			}
			this.getView().setModel(oModel);
		},

		_getFilteredDepartment: function (selectedDivision) {
			return this.department.filter(function (item, index) {
				if (item.externalCode !== "") {
					for (var iDivisionList in item.division.divisionList) {
						if (selectedDivision === item.division.divisionList[iDivisionList].externalCode) {
							return true;
						}
					}
				}
			});
		},

		_getFilteredPosition: function (selectedDivision, selectedDepartment) {
			if (!selectedDepartment) {
				return this.position.filter(function (item, index) {
					if (item.hasOwnProperty("division")) {
						if (selectedDivision === item.division) {
							return true;
						}
					}
				});
			} else {
				return this.position.filter(function (item, index) {
					if (item.code !== "" && item.hasOwnProperty("department")) {
						if (selectedDivision === item.division && selectedDepartment === item.department) {
							return true;
						}
					}
				});
			}
		},
		_searchCheckStatus: function (searchResult, key) {
			if (key === "") {
				return searchResult;
			} else if (key === "-") {
				return searchResult.filter(function (item, index) {
					if (!item.checkStatus) {
						return true;
					} else {
						return false;
					}
				});
			} else {
				return searchResult.filter(function (item, index) {
                    if (key === "NG"){
                        if (item.checkStatus === key) {
                            return true;
                        } else {
                            return false;
                        }
                    }else{
                        if (item.checkStatus !== "NG") {
                            return true;
                        } else {
                            return false;
                        }
                    }
				});
			}
		},
		_searchDivision: function (searchResult, key, field) {
			if (key === "") {
				return searchResult;
			} else {
				return searchResult.filter(function (item, index) {
					if (field === "C") {
						if (item.currentDivision === key) {
							return true;
						} else {
							return false;
						}
					} else if (field === "N") {
						if (item.nextDivision === key) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				});
			}
		},
		_searchDepartment: function (searchResult, key, field) {
			if (key === "") {
				return searchResult;
			} else {
				return searchResult.filter(function (item, index) {
					if (field === "C") {
						if (item.currentDepartment === key) {
							return true;
						} else {
							return false;
						}
					} else if (field === "N") {
						if (item.nextDepartment === key) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				});
			}
		},
		_searchPosition: function (searchResult, key, field) {
			if (key === "") {
				return searchResult;
			} else {
				return searchResult.filter(function (item, index) {
					if (field === "C") {
						if (item.currentPosition === key) {
							return true;
						} else {
							return false;
						}
					} else if (field === "N") {
						if (item.nextPosition === key) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				});
			}
		},
		
		_searchCandidateId: function (searchResult, key) {
			if (key === "") {
				return searchResult;
			} else {
				return searchResult.filter(function (item, index) {
					if (item.candidateID === key) {
						return true;
					} else {
						return false;
					}
				});
			}
		},
		
		_searchCandidateName: function (searchResult, key) {
			if (key === "") {
				return searchResult;
			} else {
				return searchResult.filter(function (item, index) {
					if (~item.candidateName.indexOf(key)) {
						return true;
					} else {
						return false;
					}
				});
			}
		},

		_getSelect: function (sId) {
			return this.getView().byId(sId);
		},

		_getSelectedItemText: function (oSelect) {
			return oSelect.getSelectedItem() ? oSelect.getSelectedItem().getKey() : "";
		},

		_searchCaseId: function (searchResult, key) {
			if(key !== "-"){
				return searchResult.filter(function (item, index) {
					if (item.caseID === key) {
						return true;
					} else {
						return false;
					}
				});
			}else{
				return searchResult;
			}
		}

	});
});