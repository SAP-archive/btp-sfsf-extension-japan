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
        
        _divisionFilterItemChange: function (selectedDivision, field, defaultDepartment, defaultPosition, modelName) {
			var oView = this.getView();
			var oModel = oView.getModel(modelName);
			var modelData = oModel.getData();
			var department = [];
			var position = [];

			if (selectedDivision) {
				
				department = this._getFilteredDepartment(selectedDivision, modelData.department);
				department.unshift(defaultDepartment);

				position = this._getFilteredPosition(selectedDivision, "", modelData.position);
				position.unshift(defaultPosition);
				
				if(field === "C"){
					modelData.currentDepartment = department;
					modelData.currentPosition = position;
					
				}else{
					modelData.nextDepartment = department;
					modelData.nextPosition = position;
				}
				
			} else {
				if(field === "C"){
					modelData.currentDepartment = modelData.department;
					modelData.currentPosition = modelData.position;
					
				}else{
					modelData.nextDepartment = modelData.department;
					modelData.nextPosition = modelData.position;
				}
			}
			oModel.refresh(true);
			this.getView().setModel(oModel, modelName);
		},

		_departmentFilterItemChange: function (selectedDivision, selectedDepartment, field, defaultPosition, modelName) {
			var oView = this.getView();
			var oModel = oView.getModel(modelName);
			var modelData = oModel.getData();
			var position = [];

			if (selectedDepartment) {
				position = this._getFilteredPosition(selectedDivision, selectedDepartment, modelData.position);
				position.unshift(defaultPosition);
				if(field==="C"){
					modelData.currentPosition = position;
				}else{
					modelData.nextPosition = position;
				}
				oModel.refresh(true);
			} else {
				if(field==="C"){
					modelData.currentPosition = this.position;
				}else{
					modelData.nextPosition = this.position;
				}
			}
			this.getView().setModel(oModel, modelName);
		},

		_getFilteredDepartment: function (selectedDivision, department) {
			return department.filter(function (item, index) {
				if (item.externalCode) {
					for (var iDivisionList in item.division.divisionList) {
						if (selectedDivision === item.division.divisionList[iDivisionList].externalCode) {
							return true;
						}
					}
				}
			});
		},

		_getFilteredPosition: function (selectedDivision, selectedDepartment, position) {
			if (!selectedDepartment) {
				return position.filter(function (item, index) {
					if (item.hasOwnProperty("division")) {
						if (selectedDivision === item.division) {
							return true;
						}
					}
				});
			} else {
				return position.filter(function (item, index) {
					if (item.code !== "" && item.hasOwnProperty("department")) {
						if (selectedDivision === item.division && selectedDepartment === item.department) {
							return true;
						}
					}
				});
			}
		},
		_searchCheckStatus: function (searchResult, key) {
			if (!key) {
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
			if (!key) {
				return searchResult;
			} else {
				return searchResult.filter(function (item, index) {
                    switch(field){
                        case "C":
                            if (item.candidateDivisionID === key) {
                                return true;
                            } else {
                                return false;
                            }
                        case "N":
                            if (item.divisionID === key) {
                                return true;
                            } else {
                                return false;
                            }
                        default :
                            return false;
                    }
				});
			}
		},
		_searchDepartment: function (searchResult, key, field) {
			if (!key) {
				return searchResult;
			} else {
				return searchResult.filter(function (item, index) {
                    switch(field){
                        case "C":
                            if (item.candidateDepartmentID === key) {
                                return true;
                            } else {
                                return false;
                            }
                        case "N":
                            if (item.departmentID === key) {
                                return true;
                            } else {
                                return false;
                            }
                        default :
                            return false;
                    }
				});
			}
		},
		_searchPosition: function (searchResult, key, field) {
			if (!key) {
				return searchResult;
			} else {
				return searchResult.filter(function (item, index) {
                    switch(field){
                        case "C":
                            if (item.candidatePositionID === key) {
                                return true;
                            } else {
                                return false;
                            }
                        case "N":
                            if (item.positionID === key) {
                                return true;
                            } else {
                                return false;
                            }
                        default :
                            return false;
                    }
				});
			}
		},
		
		_searchCandidateId: function (searchResult, key) {
			if (!key) {
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
			if (!key) {
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
            return searchResult.filter(function (item, index) {
                if (item.caseID === key) {
                    return true;
                } else {
                    return false;
                }
            });
		}
	});
});