sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageToast",
    "sap/m/ToolbarSpacer",
    "sap/ui/table/Row",
    "jquery.sap.sjax",
    "sap/m/Button",
    "sap/m/Dialog",
    "sap/m/library",
    "sap/m/Text",
    "sap/ui/model/Filter",
    "sap/ui/model/FilterOperator",
    "sap/ui/core/util/Export",
    "sap/ui/core/util/ExportTypeCSV",
    "sap/m/MessageBox",
    'sap/ui/model/type/Integer',
    "sap/ui/core/Fragment",
    "sap/m/Label",
    "sap/m/Input",
    'sap/ui/model/type/String',
    'sap/m/ColumnListItem',
    'sap/m/SearchField',
    'sap/m/ComboBox'
], function(Controller, JSONModel, MessageToast, ToolbarSpacer, TableRow, jQuery, Button, Dialog, Library, Text, Filter, FilterOperator, Export, ExportTypeCSV, MessageBox, typeInteger, Fragment, Label, Input, typeString, ColumnListItem, SearchField, ComboBox) {
    "use strict";

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
		var defaultBusinessUnit = {
			externalCode: "",
			name: "ALL"
        };
        
        var EXISTING = "Existing";
        var NEW = "New";

        var isNewCase="";
        var isNotNewCase="";
	
    return Controller.extend("com.sap.sfsf.applicants.controller.Controller", {
        onInit: function() {
			var oModel = new JSONModel();
			this.getView().setModel(oModel);
			var that = this;

            var oViewModel = new JSONModel({
                loaded: false,
                companyBusy: true,
                businessunitBusy: true,
                divisionBusy: true,
                departmentBusy: true,
                positionBusy: true,
                caseidBusy: true
            });
            this.getView().setModel(oViewModel,"view");
            this._oMultiInput = this.getView().byId("slJobTenure");

            this.getView().getModel().getData().applicantsCount = 0;
            this.getView().getModel().getData().noTransferflagCount = 0;
        	this.getView().getModel().getData().availabilityCount = 0;
        	this.getView().getModel().getData().nextpostionsCount = 0;
        	this.byId("leftProgressIndicator").setPercentValue(0);
        	this.byId("rightProgressIndicator").setPercentValue(0);

            $.ajax({
                url: "/srv_api/departments",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    var adddepdata = {
                    	"externalCode": "",
                    	"name": "ALL"};
                    result.unshift(adddepdata);
                    modelData.department = result;
                    modelData.currentDepartment = result;
                    modelData.nextDepartment = result;
                    oModel.setData(modelData);
                    oViewModel.setProperty("/departmentBusy", false);
                    oModel.refresh(true);
                    
                },
                error: function (xhr, status, err) {
                    MessageBox.error(that.getMsg("department_error"));
                }
            });
            $.ajax({
                url: "/srv_api/divisions",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    var adddivdata = {
                    	"externalCode": "", 
                    	"name": "ALL"};
                    result.unshift(adddivdata);
					modelData.division = result;
                    modelData.currentDivision = result;
                    modelData.nextDivision = result;
					oModel.setData(modelData);
					oViewModel.setProperty("/divisionBusy", false);
					oModel.refresh(true);
                    
                },
                error: function (xhr, status, err) {
                	oViewModel.setProperty("/divisionBusy", false);
                    MessageBox.error(that.getMsg("division_error"));
                }
            });
            $.ajax({
                url: "/srv_api/positions",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    var addposdata = {
                    	"code": "",
                    	"name": "ALL"};
                    result.unshift(addposdata);
					modelData.position = result;
					modelData.currentPosition = result;
					modelData.nextPosition = result;
					oModel.setData(modelData);
					oViewModel.setProperty("/positionBusy", false);
					oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                	oViewModel.setProperty("/positionBusy", false);
                    MessageBox.error(this.getMsg("position_error"));
                }
            });
            $.ajax({
                url: "/config_api/jpconfig",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    var startDateTime =  "（" + result.startDateTime + that.getMsg("date")+"）";
                    modelData.jpconfig = startDateTime;
                    oModel.setData(modelData);
                    oModel.refresh(true);
                    
                },
                error: function (xhr, status, err) {
                }
            });
            $.ajax({
                url: "/srv_api/companies",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    modelData.company = result;
                    //******************************************************************
                    //TODO会社のデフォルトとしてBestRunジャパンを仮で表示させる
		            that.getView().byId("slCompany").setSelectedKey("5000");
		            that.getView().byId("slNextCompany").setSelectedKey("5000");
		            //******************************************************************
                    oModel.setData(modelData);
                    oViewModel.setProperty("/companyBusy", false);
                    oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                	oViewModel.setProperty("/companyBusy", false);
                    MessageBox.error(that.getMsg("company_error"));
                }
            });
            $.ajax({
                url: "/srv_api/businessunits",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    var addbudata = {
                    	"externalCode": "",
                    	"name": "ALL"};
                    result.unshift(addbudata);
                    modelData.businessunit = result;
                    modelData.currentBusinessunit = result;
                    modelData.nextBusinessunit = result;
                    oModel.setData(modelData);
                    oViewModel.setProperty("/businessunitBusy", false);
                    oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                	oViewModel.setProperty("/businessunitBusy", false);
                    MessageBox.error(that.getMsg("businessunit_error"));
                }
            });

            $.ajax({
                url: "/srv_api/caseids",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    var caseidList = [];
					var caseidDefault = "Please select";
					caseidList.push({status:"", name:caseidDefault});
					for (var item of result){
						caseidList.push({status:item, name:item});
                    }
                    modelData.caseid = caseidList;
					oModel.setData(modelData);
					oViewModel.setProperty("/caseidBusy", false);
					oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                	oViewModel.setProperty("/caseidBusy", false);
                    MessageBox.error(that.getMsg("caseid_error"));
                }
            });

            this.byId("left").setSize("100%");
            this.byId("right").setSize("0%");
        },
		onSlCurrentCompanyChange: function (oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
			var CurrentFilterValueCompany = this.getSelectedItemText(this.getSelect("slCompany"));
			var currentPosition = [];
			var currentBusinessUnit = [];
			var currentDivision = [];
			var currentDepartment = [];
			
			if (CurrentFilterValueCompany !== "") {
				currentPosition = modelData.position.filter(function (item, index) {
					if (item.code !== "" && item.hasOwnProperty("company")) {
						if (CurrentFilterValueCompany === item.company) {
							return true;
						}
					}
				});
				var key = "businessUnit";
				var sCurrentBusinessUnit = new Set(currentPosition.map(e => e[key]));
				var acurrentBusinessUnit = currentPosition.filter(e => {
				    return sCurrentBusinessUnit.has(e[key]) && sCurrentBusinessUnit.delete(e[key]);
				});
				for(var i=0; i<acurrentBusinessUnit.length; i++) {
					for(var j=0; j<modelData.businessunit.length; j++) {
						if(acurrentBusinessUnit[i].businessUnit === modelData.businessunit[j].externalCode) {
							currentBusinessUnit.push(modelData.businessunit[j]);
						}
					}
				}
				var key = "division";
				var sCurrentDivision = new Set(currentPosition.map(e => e[key]));
				var acurrentDivision = currentPosition.filter(e => {
				    return sCurrentDivision.has(e[key]) && sCurrentDivision.delete(e[key]);
				});
				for(var i=0; i<acurrentDivision.length; i++) {
					for(var j=0; j<modelData.division.length; j++) {
						if(acurrentDivision[i].division === modelData.division[j].externalCode) {
							currentDivision.push(modelData.division[j]);
						}
					}
				}
				
				var key = "department";
				var sCurrentDepartment = new Set(currentPosition.map(e => e[key]));
				var acurrentDepartment = currentPosition.filter(e => {
				    return sCurrentDepartment.has(e[key]) && sCurrentDepartment.delete(e[key]);
				});
				for(var i=0; i<acurrentDepartment.length; i++) {
					for(var j=0; j<modelData.department.length; j++) {
						if(acurrentDepartment[i].department === modelData.department[j].externalCode) {
							currentDepartment.push(modelData.department[j]);
						}
					}
				}
				currentPosition.unshift(defaultPosition);
				currentBusinessUnit.unshift(defaultBusinessUnit);
				currentDivision.unshift(defaultDivision);
				currentDepartment.unshift(defaultDepartment);
				modelData.currentPosition = currentPosition;
				modelData.currentBusinessunit = currentBusinessUnit;
				modelData.currentDivision = currentDivision;
				modelData.currentDepartment = currentDepartment;
				oModel.refresh(true);
			} else {
				modelData.currentPosition = modelData.position;
				modelData.currentBusinessunit = modelData.businessunit;
				modelData.currentDivision = modelData.division;
				modelData.currentDepartment = modelData.department;
				oModel.refresh(true);
			}
			this.getView().setModel(oModel);

		},
		onSlCurrentBusinessUnitChange: function(oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
			var CurrentFilterValueBusinessUnit = this.getSelectedItemText(this.getSelect("slBusinessunit"));
			var currentDivision = [];
			var currentDepartment = [];
			var currentPosition = [];
			if (CurrentFilterValueBusinessUnit !== "") {
				currentDivision = modelData.division.filter(function (item, index) {
					if (item.externalCode !== "") {
						for (var iBusinessUnitList in item.businessUnit.businessUnitList) {
							if (CurrentFilterValueBusinessUnit === item.businessUnit.businessUnitList[iBusinessUnitList].externalCode) {
								return true;
							}
						}
					}
				});
				currentPosition = modelData.position.filter(function (item, index) {
					if (item.code !== "" && item.hasOwnProperty("businessUnit")) {
						if (CurrentFilterValueBusinessUnit === item.businessUnit) {
							return true;
						}
					}
				});
				var key = "department";
				var sCurrentDepartment = new Set(currentPosition.map(e => e[key]));
				var acurrentDepartment = currentPosition.filter(e => {
				    return sCurrentDepartment.has(e[key]) && sCurrentDepartment.delete(e[key]);
				});
				for(var i=0; i<acurrentDepartment.length; i++) {
					for(var j=0; j<modelData.department.length; j++) {
						if(acurrentDepartment[i].department === modelData.department[j].externalCode) {
							currentDepartment.push(modelData.department[j]);
						}
					}
				}
				currentDivision.unshift(defaultDivision);
				currentPosition.unshift(defaultPosition);
				currentDepartment.unshift(defaultDepartment);
				modelData.currentPosition = currentPosition;
				modelData.currentDepartment = currentDepartment;
				modelData.currentDivision = currentDivision;
				oModel.refresh(true);
			} else {
				modelData.currentDivision = modelData.division;
				modelData.currentPosition = modelData.position;
				modelData.currentDepartment = modelData.department;
				oModel.refresh(true);
			}
			this.getView().setModel(oModel);

		},
		onSlCurrentDivisionChange: function (oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
			var CurrentFilterValueDivision = this.getSelectedItemText(this.getSelect("slDivision"));
			var currentDepartment = [];
			var currentPosition = [];
			var CurrentFilterValueCompany = this.getSelectedItemText(this.getSelect("slCompany"));
			var CurrentFilterValueBusinessUnit = this.getSelectedItemText(this.getSelect("slBusinessunit"));

			if (CurrentFilterValueDivision !== "") {
				currentDepartment = modelData.department.filter(function (item, index) {
					if (item.externalCode !== "") {
						for (var iDivisionList in item.division.divisionList) {
							if (CurrentFilterValueDivision === item.division.divisionList[iDivisionList].externalCode) {
								return true;
							}
						}
					}
				});
				currentPosition = modelData.position.filter(function (item, index) {
					if (item.code !== "" && item.hasOwnProperty("division")) {
						if (CurrentFilterValueDivision === item.division) {
							return true;
						}
					}
				});	
				currentDepartment.unshift(defaultDepartment);
				currentPosition.unshift(defaultPosition);
				modelData.currentDepartment = currentDepartment;
				modelData.currentPosition = currentPosition;
				oModel.refresh(true);
			} else {
				modelData.currentDepartment = modelData.department;
				modelData.currentPosition = modelData.position;
				oModel.refresh(true);
			}
			this.getView().setModel(oModel);

		},
		onSlCurrentDepartmentChange: function (oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
			var CurrentFilterValueDepartment = this.getSelectedItemText(this.getSelect("slDepartment"));
			var currentPosition = [];

			if (CurrentFilterValueDepartment !== "") {
				currentPosition = modelData.currentPosition.filter(function (item, index) {
					if (item.code !== "" && item.hasOwnProperty("department")) {
						if (CurrentFilterValueDepartment === item.department) {
							return true;
						}
					}
				});
				currentPosition.unshift(defaultPosition);
				modelData.currentPosition = currentPosition;
				oModel.refresh(true);
			} else {
				modelData.currentPosition = modelData.position;
			}
			this.getView().setModel(oModel);
		},
		onSlNextCompanyChange: function (oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
			var NextFilterValueCompany = this.getSelectedItemText(this.getSelect("slNextCompany"));
			var nextPosition = [];
			var nextBusinessUnit = [];
			var nextDivision = [];
			var nextDepartment = [];
			
			if (NextFilterValueCompany !== "") {
				nextPosition = modelData.position.filter(function (item, index) {
					if (item.code !== "" && item.hasOwnProperty("company")) {
						if (NextFilterValueCompany === item.company) {
							return true;
						}
					}
				});
				var key = "businessUnit";
				var sNextBusinessUnit = new Set(nextPosition.map(e => e[key]));
				var aNextBusinessUnit = nextPosition.filter(e => {
				    return sNextBusinessUnit.has(e[key]) && sNextBusinessUnit.delete(e[key]);
				});
				for(var i=0; i<aNextBusinessUnit.length; i++) {
					for(var j=0; j<modelData.businessunit.length; j++) {
						if(aNextBusinessUnit[i].businessUnit === modelData.businessunit[j].externalCode) {
							nextBusinessUnit.push(modelData.businessunit[j]);
						}
					}
				}
				var key = "division";
				var sNextDivision = new Set(nextPosition.map(e => e[key]));
				var aNextDivision = nextPosition.filter(e => {
				    return sNextDivision.has(e[key]) && sNextDivision.delete(e[key]);
				});
				for(var i=0; i<aNextDivision.length; i++) {
					for(var j=0; j<modelData.division.length; j++) {
						if(aNextDivision[i].division === modelData.division[j].externalCode) {
							nextDivision.push(modelData.division[j]);
						}
					}
				}
				
				var key = "department";
				var sNextDepartment = new Set(nextPosition.map(e => e[key]));
				var aNextDepartment = nextPosition.filter(e => {
				    return sNextDepartment.has(e[key]) && sNextDepartment.delete(e[key]);
				});
				for(var i=0; i<aNextDepartment.length; i++) {
					for(var j=0; j<modelData.department.length; j++) {
						if(aNextDepartment[i].department === modelData.department[j].externalCode) {
							nextDepartment.push(modelData.department[j]);
						}
					}
				}
				nextPosition.unshift(defaultPosition);
				nextBusinessUnit.unshift(defaultBusinessUnit);
				nextDivision.unshift(defaultDivision);
				nextDepartment.unshift(defaultDepartment);
				modelData.nextPosition = nextPosition;
				modelData.nextBusinessunit = nextBusinessUnit;
				modelData.nextDivision = nextDivision;
				modelData.nextDepartment = nextDepartment;
				oModel.refresh(true);
			} else {
				modelData.nextPosition = modelData.position;
				modelData.nextBusinessunit = modelData.businessunit;
				modelData.nextDivision = modelData.division;
				modelData.nextDepartment = modelData.department;
				oModel.refresh(true);
			}
			this.getView().setModel(oModel);

		},
		onSlNextBusinessUnitChange: function(oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
			var NextFilterValueBusinessUnit = this.getSelectedItemText(this.getSelect("slNextBusinessunit"));
			var nextDivision = [];
			var nextDepartment = [];
			var nextPosition = [];
			if (NextFilterValueBusinessUnit !== "") {
				nextDivision = modelData.division.filter(function (item, index) {
					if (item.externalCode !== "") {
						for (var iBusinessUnitList in item.businessUnit.businessUnitList) {
							if (NextFilterValueBusinessUnit === item.businessUnit.businessUnitList[iBusinessUnitList].externalCode) {
								return true;
							}
						}
					}
				});
				nextPosition = modelData.position.filter(function (item, index) {
					if (item.code !== "" && item.hasOwnProperty("businessUnit")) {
						if (NextFilterValueBusinessUnit === item.businessUnit) {
							return true;
						}
					}
				});
				var key = "department";
				var sNextDepartment = new Set(nextPosition.map(e => e[key]));
				var anextDepartment = nextPosition.filter(e => {
				    return sNextDepartment.has(e[key]) && sNextDepartment.delete(e[key]);
				});
				for(var i=0; i<anextDepartment.length; i++) {
					for(var j=0; j<modelData.department.length; j++) {
						if(anextDepartment[i].department === modelData.department[j].externalCode) {
							nextDepartment.push(modelData.department[j]);
						}
					}
				}
				nextDivision.unshift(defaultDivision);
				nextPosition.unshift(defaultPosition);
				nextDepartment.unshift(defaultDepartment);
				modelData.nextPosition = nextPosition;
				modelData.nextDepartment = nextDepartment;
				modelData.nextDivision = nextDivision;
				oModel.refresh(true);
			} else {
				modelData.nextDivision = modelData.division;
				modelData.nextPosition = modelData.position;
				modelData.nexttDepartment = modelData.department;
				oModel.refresh(true);
			}
			this.getView().setModel(oModel);

		},
		onSlNextDivisionChange: function (oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
			var NextFilterValueDivision = this.getSelectedItemText(this.getSelect("slNextDivision"));
			var nextDepartment = [];
			var nextPosition = [];

			if (NextFilterValueDivision !== "") {
				nextDepartment = modelData.department.filter(function (item, index) {
					if (item.externalCode !== "") {
						for (var iDivisionList in item.division.divisionList) {
							if (NextFilterValueDivision === item.division.divisionList[iDivisionList].externalCode) {
								return true;
							}
						}
					}
				});
				nextPosition = modelData.position.filter(function (item, index) {
					if (item.code !== "" && item.hasOwnProperty("division")) {
						if (NextFilterValueDivision === item.division) {
							return true;
						}
					}
				});

				nextDepartment.unshift(defaultDepartment);
				nextPosition.unshift(defaultPosition);
				modelData.nextDepartment = nextDepartment;
				modelData.nextPosition = nextPosition;
				oModel.refresh(true);
			} else {
				modelData.nextDepartment = modelData.department;
				modelData.nextPosition = modelData.position;
				oModel.refresh(true);
			}
			this.getView().setModel(oModel);

		},
		onSlNextDepartmentChange: function (oEvent) {
			var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
			var NextFilterValueDepartment = this.getSelectedItemText(this.getSelect("slNextDepartment"));
			var nextPosition = [];

			if (NextFilterValueDepartment !== "") {
				nextPosition = modelData.nextPosition.filter(function (item, index) {
					if (item.code !== "" && item.hasOwnProperty("department")) {
						if (NextFilterValueDepartment === item.department) {
							return true;
						}
					}
				});
				nextPosition.unshift(defaultPosition);
				modelData.nextPosition = nextPosition;
				oModel.refresh(true);
			} else {
				modelData.nextPosition = modelData.position;
			}
			this.getView().setModel(oModel);
		},
        onExit: function() {
            this.oReshuffleApplicantsModel.destroy();
        },
        onDragStart: function(oEvent) {
            var oDraggedRow = oEvent.getParameter("target");
            var oDragSession = oEvent.getParameter("dragSession");
            oDragSession.setComplexData("draggedRowContext", oDraggedRow.getBindingContext());
        },
        onDropApplicantListTable: function(oEvent) {
            var oModel = this.getView().getModel();
            var oDragSession = oEvent.getParameter("dragSession");
            var oDraggedRowContext = oDragSession.getComplexData("draggedRowContext");
            if (!oDraggedRowContext) {
                return;
            }
             var checkedcandidateId = oModel.getProperty("candidateID",oDraggedRowContext);
             var candidatejsondata = oModel.getData().candidates;
             if(checkedcandidateId) {
                   for(var i=0; i<candidatejsondata.length; i++) {
                    	if(candidatejsondata[i].currentEmpId === checkedcandidateId) {
                    		candidatejsondata[i].transferFlag ="";
                    		this.formatAvailableToIcon(false);
                    		break;
                    	}
                 	}
                 	var isAddLine = oModel.getProperty("isAddLine",oDraggedRowContext);
                 	if(isAddLine) {
                 		var draggedIndex = oEvent.getParameters().draggedControl.getIndex();
                 		var sId = oModel.getProperty("currentEmpId", oDraggedRowContext);
                 		var nextpostionsdata = this.getView().getModel().getData().nextpositions;
						nextpostionsdata = nextpostionsdata.filter(function (item, index) {
							if (sId === item.currentEmpId){
								return true;
							}
						});
						if(nextpostionsdata.length > 1) {
							this.deleteLine(draggedIndex);
						} else {
							this._clearInfo(oDraggedRowContext);
						}
                 	} else {
                 		oModel.setProperty("candidateID","", oDraggedRowContext);
                 	}
        		this.getCountAvailableCandidate();
        		this.getView().getModel().refresh(true);
            }
            if(!isAddLine) {
                this._clearInfo(oDraggedRowContext);
            }
               this.getCountAvailablePosition(); 
        },
        _clearInfo: function (oDraggedRowContext) {
        	var oModel = this.getView().getModel();
        	oModel.setProperty("candidateID","", oDraggedRowContext);
	        oModel.setProperty("candidateName","", oDraggedRowContext);
	        oModel.setProperty("currentDivision","", oDraggedRowContext);
	        oModel.setProperty("currentDivisionName","", oDraggedRowContext);
	        oModel.setProperty("currentDepartment","", oDraggedRowContext);
	        oModel.setProperty("currentDepartmentName","", oDraggedRowContext);
	        oModel.setProperty("currentPosition","", oDraggedRowContext);
	        oModel.setProperty("currentPositionName","", oDraggedRowContext);
	        oModel.setProperty("currentJobGrade","", oDraggedRowContext);
	        oModel.setProperty("currentJobGrade","", oDraggedRowContext);
	        oModel.setProperty("candidateEmpRetire","", oDraggedRowContext);
        	oModel.setProperty("jobTenure","", oDraggedRowContext);
	        oModel.setProperty("rating1","", oDraggedRowContext);
	        oModel.setProperty("rating2","", oDraggedRowContext);
	        oModel.setProperty("rating3","", oDraggedRowContext);
	        this.getCountAvailableCandidate();
			this.getCountAvailablePosition();
			this.getView().getModel().refresh(true);
	        oModel.refresh(true);
        },
        onDropPositionListTable: function(oEvent) {
            var oModel = this.getView().getModel();
            var oDragSession = oEvent.getParameter("dragSession");
            var oDraggedRowContext = oDragSession.getComplexData("draggedRowContext");
            if (!oDraggedRowContext) {
                return;
            }
            var iDraggedTransferFlag = oModel.getProperty("transferFlag",oDraggedRowContext);
            if(iDraggedTransferFlag){
                MessageBox.alert(this.getMsg("transferAlertMsg"));
                return;
            }
            var oDroppedRow = oEvent.getParameter("droppedControl");
            if (oDroppedRow && oDroppedRow instanceof TableRow) {
                var oDroppedRowContext = oDroppedRow.getBindingContext();
                var iDroppedName = oModel.getProperty("candidateName",oDroppedRowContext);
                var aSelectedIndexes = this.byId("applicantListTable").getSelectedIndices();
                if(iDroppedName){
                	var droppedIndex = oEvent.getParameters().droppedControl.getIndex();
                	var draggedIndex = oEvent.getParameters().draggedControl.getIndex();
                	var draggedData = oEvent.getParameters().dragSession.getComplexData("draggedRowContext").oModel.getData().candidates[draggedIndex];
                	if(aSelectedIndexes.length > 0) {
                		var aSelectedCandidateData=[];
	                	for(var i = 0; i < aSelectedIndexes.length; i++) {
	                		var index = aSelectedIndexes[i];
							var draggedData = this.getView().getModel().getData().candidates[index];
	                		aSelectedCandidateData.push(draggedData);
	                	}
                		this.choosetomakemultilines(aSelectedCandidateData, droppedIndex);
	                }else{
                		this.choosetomakeline(droppedIndex, draggedData);
	                }
                    return;
                }
	            var droppedIndex = oEvent.getParameters().droppedControl.getIndex();
                if(aSelectedIndexes.length > 0) {
                	var aSelectedCandidateData=[];
                	for(var i = 0; i < aSelectedIndexes.length; i++) {
                		var index = aSelectedIndexes[i];
						var draggedData = this.getView().getModel().getData().candidates[index];
                		aSelectedCandidateData.push(draggedData);
                	}
                	this.addLines(aSelectedCandidateData, droppedIndex);
                }else {
	                var iDraggedId = oModel.getProperty("currentEmpId",oDraggedRowContext);
	                var iDraggedName = oModel.getProperty("currentEmpName",oDraggedRowContext);
	                var iDraggedCurrentDivision = oModel.getProperty("currentDivision",oDraggedRowContext);
	                var iDraggedCurrentDivisionName = oModel.getProperty("currentDivisionName",oDraggedRowContext);
	                var iDraggedCurrentDepartment = oModel.getProperty("currentDepartment",oDraggedRowContext);
	                var iDraggedCurrentDepartmentName = oModel.getProperty("currentDepartmentName",oDraggedRowContext);
	                var iDraggedCurrentPosition = oModel.getProperty("currentPosition",oDraggedRowContext);
	                var iDraggedCurrentPositionName = oModel.getProperty("currentPositionName",oDraggedRowContext);
	                var iDraggedCurrentJobGrade = oModel.getProperty("currentJobGrade",oDraggedRowContext);
	                var iDraggedCurrentJobGrade = oModel.getProperty("currentJobGrade",oDraggedRowContext);
	                var iDraggedCurrentEmpRetire = oModel.getProperty("currentEmpRetire",oDraggedRowContext);
	                var iDraggedJobTenure = oModel.getProperty("jobTenure",oDraggedRowContext);
	                var iDraggedRate1 = oModel.getProperty("rating1",oDraggedRowContext);
	                var iDraggedRate2 = oModel.getProperty("rating2",oDraggedRowContext);
	                var iDraggedRate3 = oModel.getProperty("rating3",oDraggedRowContext);
	                oModel.setProperty("transferFlag","✓", oDraggedRowContext);
	                this.formatAvailableToIcon(true);
	                oModel.setProperty("candidateID",iDraggedId, oDroppedRowContext);
	                oModel.setProperty("candidateName",iDraggedName, oDroppedRowContext);
	                oModel.setProperty("currentDivision",iDraggedCurrentDivision, oDroppedRowContext);
	                oModel.setProperty("currentDivisionName",iDraggedCurrentDivisionName, oDroppedRowContext);
	                oModel.setProperty("currentDepartment",iDraggedCurrentDepartment, oDroppedRowContext);
	                oModel.setProperty("currentDepartmentName",iDraggedCurrentDepartmentName, oDroppedRowContext);
	                oModel.setProperty("currentPosition",iDraggedCurrentPosition, oDroppedRowContext);
	                oModel.setProperty("currentPositionName",iDraggedCurrentPositionName, oDroppedRowContext);
	                oModel.setProperty("currentJobGrade",iDraggedCurrentJobGrade, oDroppedRowContext);
	                oModel.setProperty("currentJobGrade",iDraggedCurrentJobGrade, oDroppedRowContext);
	                oModel.setProperty("candidateEmpRetire",iDraggedCurrentEmpRetire, oDroppedRowContext);
	                oModel.setProperty("jobTenure",iDraggedJobTenure, oDroppedRowContext);
	                oModel.setProperty("rating1",iDraggedRate1, oDroppedRowContext);
	                oModel.setProperty("rating2",iDraggedRate2, oDroppedRowContext);
	                oModel.setProperty("rating3",iDraggedRate3, oDroppedRowContext);
	                this.getCountAvailablePosition();
	        		this.getCountAvailableCandidate();
                }
            }
            oModel.refresh(true);
        },
		onOpenNextPositionPane: function() {
            isNewCase = true;
            isNotNewCase = true;
			this._openRightPane();
			this._switchRightPaneContent(isNewCase);
		},
		onOpenEditExistingCasePane: function() {
            isNotNewCase = false;
            isNewCase = false;
			this._openRightPane();
			this._switchRightPaneContent(isNotNewCase);
		},
        _openRightPane: function(){
            this.byId("right").setSize("55%");
            this.byId("left").setSize("45%");
        },
        onHideRight:function(){
            this.byId("right").setSize("0%");
            this.byId("left").setSize("100%");
        },
        onExecute: function(oEvent){
            MessageToast.show(this.getMsg("execute"));
        },
        onLeftReset: function(){
        	var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
        	modelData.company = modelData.company;
			modelData.currentBusinessunit = modelData.businessunit;
			modelData.currentDivision = modelData.division;
			modelData.currentDepartment = modelData.department;
			modelData.currentPosition = modelData.position;
			this.getView().byId("slCompany").setSelectedKey("5000");
        	this.getView().byId("slBusinessunit").setSelectedKey("");
        	this.getView().byId("slDivision").setSelectedKey("");
        	this.getView().byId("slDepartment").setSelectedKey("");
        	this.getView().byId("slPosition").setSelectedKey("");
        	this.getView().byId("slJobTenureLL").setValue("");
        	this.getView().byId("slJobTenureUL").setValue("");
        	this.getView().byId("slWillingness").setSelectedKey("");
        	this.getView().byId("slTransferFlag").setSelectedKey("");
			oModel.refresh(true);
			this.getView().setModel(oModel);
			
        },
        onRightReset: function(){
        	var oView = this.getView();
			var oModel = oView.getModel();
			var modelData = oModel.getData();
        	this.getView().byId("slNextCompany").setSelectedKey("5000");
        	this.getView().byId("slNextBusinessunit").setSelectedKey("");
        	this.getView().byId("slNextDivision").setSelectedKey("");
        	this.getView().byId("slNextDepartment").setSelectedKey("");
        	this.getView().byId("slNextPosition").setSelectedKey("");
            this.getView().byId("slRetirePlan").setSelectedKey("");
            this.getView().byId("slCaseid").setSelectedKey("");
        	modelData.company = modelData.company;
			modelData.nextBusinessunit = modelData.businessunit;
			modelData.nextDivision = modelData.division;
			modelData.nextDepartment = modelData.department;
			modelData.nextPosition = modelData.position;
			oModel.refresh(true);
			this.getView().setModel(oModel);
        },
        onSave: function(){
            var modelData = this.getView().getModel().getData();
        	var aNextpos = modelData.nextpositions;
        	if(!aNextpos || aNextpos.length === 0) {
        		MessageBox.alert(this.getMsg("no_data"));
        		return;
        	}
        	var count = 0;
        	for(var i=0; i<aNextpos.length; i++) {
        		if(aNextpos[i].candidateName === null || aNextpos[i].candidateName === ""){
        			count= count+1;
        		}
        	}
            if(isNewCase) {
                if(count>0) {
                    this.checkCandidate(aNextpos);
                }else {
                    this.save(aNextpos);
                }    
            }else if(!isNotNewCase) {
                if(count>0) {
                    this.checkCandidateForExisting(aNextpos);
                }else {
                    this.update(aNextpos);
                }
            }else {
                this.save(aNextpos);
            }
        },
        sendData: function(nextpostions, sText, caseStatus){
        	var oView = this.getView();
        	var that = this;
        	var positionListTable = oView.byId("positionListTable");
        	var jsondata = JSON.parse(JSON.stringify(nextpostions));
        	var postdata  = [];
        	//TODO isAddLineなどの、不要な項目（バックエンドに送らない項目）を削除する
        	postdata = jsondata.filter(function (item, index) {
					item.currentEmpRetire = item.candidateEmpRetire;
					item.caseID = sText;
					delete item.photo;
					delete item.candidateEmpRetire;
                    delete item.isAddLine;
                     delete item.transferFlag;
					return true;
			});
        	positionListTable.setBusy(true);
        	 $.ajax({
					url: "/srv_api/candidates",
					method: "POST",
					data: JSON.stringify(postdata),
                                        contentType: "applicantion/json",
					success: function (result, xhr, data) {
                         positionListTable.setBusy(false);
                         if(caseStatus === NEW) {
                             MessageToast.show(that.getMsg("case") + sText +that.getMsg("saved"));
                         }else if(caseStatus === EXISTING){
                             MessageToast.show(that.getMsg("case") + sText +that.getMsg("updated"));
                         }
					},
					error: function (xhr, status, err) {
                         positionListTable.setBusy(false);
                         if(xhr.status === 422) {
                            MessageBox.error(that.getMsg("invalid_caseId")); 
                         }else if(xhr.status === 401) {
                            MessageBox.error(that.getMsg("different_user")); 
                         }else{
                            MessageBox.error(that.getMsg("error"));
                         }
					}
				});
        },
        checkCandidate: function() {
        	var ButtonType = Library.ButtonType;
        	var that = this;
        	var oView = this.getView();
        	var modelData = oView.getModel().getData();
        	var oModel = oView.getModel();
        	var nextpositions=[];
        	var oDialog = new Dialog({
                title: this.getMsg("confirm"),
                type: 'Message',
                content: new Text({ 
                    text:this.getMsg("availablePost")
                }),
                beginButton: new Button({
                    type: ButtonType.Emphasized,
                    text: this.getMsg("continue"),
                    press: function () {
						nextpositions = modelData.nextpositions.filter(function (item, index) {
							if(item.candidateID && item.candidateName){
								return true;
							}
						});
						oModel.refresh(true);
                    	that.save(nextpositions);
                        oDialog.close();
                    }
                }),
                endButton: new Button({
                    text: this.getMsg("cancel"),
                    press: function () {
                        oDialog.close();
                    }
                }),
                afterClose: function () {
                    oDialog.destroy();
                }
            });
            oDialog.open();
        },
        checkCandidateForExisting: function(nextpositions) {
            MessageBox.error(this.getMsg("unable_to_update_with_available_post"));
        },
        save: function(nextpositions){
            var ButtonType = Library.ButtonType;
        	var that = this;
        	var oDialog = new Dialog({
                title: this.getMsg("confirm"),
                type: 'Message',
                content: new Text({ 
                    text: this.getMsg("confirmMsg")
                }),
                beginButton: new Button({
                    type: ButtonType.Emphasized,
                    text: this.getMsg("continue"),
                    press: function () {
                        that._inputCaseId(nextpositions);
                        oDialog.close();
                    }
                }),
                endButton: new Button({
                    text: this.getMsg("cancel"),
                    press: function () {
                        oDialog.close();
                    }
                }),
                afterClose: function () {
                    oDialog.destroy();
                }
            });
            oDialog.open();
        },
        update: function(nextpositions){
            var ButtonType = Library.ButtonType;
        	var that = this;
        	var oDialog = new Dialog({
                title: this.getMsg("confirm"),
                type: 'Message',
                content: new Text({ 
                    text: this.getMsg("confirmMsgForUpdating")
                }),
                beginButton: new Button({
                    type: ButtonType.Emphasized,
                    text: this.getMsg("continue"),
                    press: function () {
                        var sText = that.getSelectedItemText(that.getSelect("slCaseid"));
                        that.sendData(nextpositions, sText, EXISTING);
                        oDialog.close();
                    }
                }),
                endButton: new Button({
                    text: this.getMsg("cancel"),
                    press: function () {
                        oDialog.close();
                    }
                }),
                afterClose: function () {
                    oDialog.destroy();
                }
            });
            oDialog.open();
        },
        _inputCaseId: function(nextpositions){
        	var ButtonType = Library.ButtonType;
        	var that = this;
        	var oDialog = new Dialog({
                title: this.getMsg("confirm"),
                type: 'Message',
                content: [
						new Label({
							text: this.getMsg("inputCaseId"),
							labelFor: "inputCaseId"
						}),
						new Input("inputCaseId", {
							width: "100%",
							placeholder: this.getMsg("CaseId"),
							required: true,
							maxLength: 50
						})
					],
                beginButton: new Button({
                    type: ButtonType.Emphasized,
                    text: this.getMsg("save"),
                    press: function () {
                    	var sText = sap.ui.getCore().byId("inputCaseId").getValue();
                    	if(sText === "" || sText === null) {
                    		sap.ui.getCore().byId("inputCaseId").setValueState("Error");
                    		sap.ui.getCore().byId("inputCaseId").setValueStateText(that.getMsg("caseid_require"));
                    	}else {
					        that.sendData(nextpositions, sText, NEW);
	                        oDialog.close();
                    	}
                    }
                }),
                endButton: new Button({
                    text: this.getMsg("cancel"),
                    press: function () {
                        oDialog.close();
                    }
                }),
                afterClose: function () {
                    oDialog.destroy();
                }
            });
            oDialog.open();
        },
        onDataExport: function(jsondata, sFileName) {
             var that = this;

                var sExportUrl = "/srv_api/export";
                var sExportQueryString = "?filename=" + sFileName;
                var sUrl = sExportUrl + sExportQueryString;

                for(var i = 0; i<jsondata.length; i++) {
                    delete jsondata[i].photo;
                }
                var oXHR = new XMLHttpRequest();
                oXHR.open("POST", sUrl);
                oXHR.setRequestHeader("Content-Type", "application/json");
                oXHR.responseType = "blob";
                oXHR.onload = function () {
	                if (oXHR.status < 400 && oXHR.response && oXHR.response.size > 0) {
	                   var sHeaderContentDisposition = decodeURIComponent(oXHR.getResponseHeader("Content-Disposition"));
	                   var sPassword = decodeURIComponent(oXHR.getResponseHeader("Password"));
	                   var aHeaderParts = sHeaderContentDisposition.split("filename=");
	                   var sFilenameFromServer = aHeaderParts[1];
	                   if (sap.ui.Device.browser.msie) {
	                        window.navigator.msSaveOrOpenBlob(oXHR.response, sFilenameFromServer);
	                    } else {
	                        var oA = document.createElement("a");
	                        oA.href = window.URL.createObjectURL(oXHR.response);
	                        oA.style.display = "none";
	                         oA.download = sFilenameFromServer;
	                         document.body.appendChild(oA);
	                         oA.click();
	                         MessageBox.information("Password:" + sPassword);
	                         document.body.removeChild(oA);
	                         setTimeout(function () {
	                            window.URL.revokeObjectURL(oA.href);
	                            }, 250);
	                        }
	                 } else {
	                    MessageBox.error(that.getMsg("fail_msg"));
	                 }
                    }.bind(this);
                  oXHR.send(JSON.stringify(jsondata));
                },
        onDataLeftExport: function(oEvent) {
        	var oView = this.getView();
        	var that = this;
        	var sFileName = "applicants";
        	var jsondata = oView.getModel().getData().candidates;
        	if(!jsondata) {
        		MessageBox.alert(this.getMsg("no_data_for_download"));
        		return;
        	}
        	this.onDataExport(jsondata, sFileName);
        },
        onDataRightExport: function(oEvent) {
        	var oView = this.getView();
        	var that = this;
        	var sFileName = "nextpositions";
        	var jsondata = oView.getModel().getData().nextpositions;
        	if(!jsondata) {
        		MessageBox.alert(this.getMsg("no_data_for_download"));
        		return;
        	}
        	this.onDataExport(jsondata, sFileName);
        },
        onValueHelpRequested: function() {
			this._oValueHelpDialog = sap.ui.xmlfragment("com.sap.sfsf.applicants.view.JobTenureDialog", this);
			this.getView().addDependent(this._oValueHelpDialog);
			this._oValueHelpDialog.setRangeKeyFields([{
				label: this.getMsg("jobTenure"),
				key: "JobTenure",
				type: "Number",
				typeInstance: new typeInteger({}, {
					maxLength: 7
				})
			}]);

			this._oValueHelpDialog.setTokens(this._oMultiInput.getTokens());
			this._oValueHelpDialog.open();
		},
		onValueHelpOkPress: function (oEvent) {
			var aTokens = oEvent.getParameter("tokens");
			this._oMultiInput.setTokens(aTokens);
			this._oValueHelpDialog.close();
		},
		onValueHelpCancelPress: function () {
			this._oValueHelpDialog.close();
		},
		onValueHelpAfterClose: function () {
			this._oValueHelpDialog.destroy();
		},
        getTable : function(){
            return this.byId("applicantListTable");
        },
        getSelectedItemText: function(oSelect) {
            return oSelect.getSelectedItem() ? oSelect.getSelectedItem().getKey() : "";
        },
        getSelect: function(sId) {
            return this.getView().byId(sId);
        },
        getMsg: function(msg){
            var oResourceModel = this.getView().getModel("i18n");
            return oResourceModel.getResourceBundle("i18n").getText(msg);
        },
        onLeftGoBtn: function(){
        	var that = this;
            var oView = this.getView();
            var CurrenctFilterValueCompany = this.getSelectedItemText(this.getSelect("slCompany"));
            var CurrenctFilterValueBusinessUnit = this.getSelectedItemText(this.getSelect("slBusinessunit"));
            var CurrenctFilterValueDivision = this.getSelectedItemText(this.getSelect("slDivision"));
            var CurrenctFilterValueDepartment = this.getSelectedItemText(this.getSelect("slDepartment"));
            var CurrenctFilterValuePosition = this.getSelectedItemText(this.getSelect("slPosition"));
            var CurrenctFilterValueJobTenureLL =  this.byId("slJobTenureLL").getValue();
            var CurrenctFilterValueJobTenureUL =  this.byId("slJobTenureUL").getValue();
            var CurrenctFilterValueWillingness = this.byId("slWillingness")._getSelectedItemText();
            var CurrenctFilterValueTransferFlag = this.byId("slTransferFlag").getSelectedIndex();
            var positionParameter = "";
            var jobTenureLLParameter = "";
            var jobTenureULParameter= "";
            var willingnessParameter = "";
            var companyParameter = "";
            var businessUnitParameter ="";
            
            if (CurrenctFilterValueDivision === "" || CurrenctFilterValueDepartment === "" ) {
                MessageBox.alert(this.getMsg("mandatoryMsg"));
                return;
            }
             if(CurrenctFilterValueCompany !== ""){
                companyParameter = "&company="+CurrenctFilterValueCompany;
            }
             if(CurrenctFilterValueBusinessUnit !== ""){
                businessUnitParameter = "&businessUnit="+CurrenctFilterValueBusinessUnit;
            }
            if(CurrenctFilterValuePosition !== ""){
                positionParameter = "&position="+CurrenctFilterValuePosition;
            }
            if(CurrenctFilterValueJobTenureLL !== ""){
                jobTenureLLParameter = "&tenureLL="+CurrenctFilterValueJobTenureLL;
            }
            if(CurrenctFilterValueJobTenureUL !== ""){
                jobTenureULParameter = "&tenureUL="+CurrenctFilterValueJobTenureUL;
            }
            if(CurrenctFilterValueWillingness !== ""){
                willingnessParameter = "&willingness="+CurrenctFilterValueWillingness;
            }
            
            var applicantListTable = oView.byId("applicantListTable");
            var nextposjsondata = oView.byId("positionListTable").getModel().getData().nextpositions;
            applicantListTable.setBusy(true);
            var oModel = oView.getModel();
            var transferflagCount = 0;
            var noTransferflagCount = 0;
            $.ajax({
                url: "/srv_api/currentpositions?division=" + CurrenctFilterValueDivision+"&department=" +CurrenctFilterValueDepartment + companyParameter + businessUnitParameter +positionParameter + jobTenureLLParameter+jobTenureULParameter + willingnessParameter,
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    applicantListTable.setBusy(false);
                    if (result) {
	                    var oModelData = oView.getModel().getData();
	                   oModelData.candidates = result;
	                   oModelData.applicantsCount = result.length;
	                   oModel.setData(oModelData);
	                   oModel.refresh(true);
                    } else {
                        MessageBox.alert(that.getMsg("not_found"));
                        oModelData.candidates = {};
                        oModelData.applicantsCount = 0;
                        oModelData.noTransferflagCount = 0;
                        oModel.setData(oModelData);
                        oModel.refresh(true);
                    }
                    var checkedcandidateId = [];
                    var candidatejsondata = oView.getModel().getData().candidates;
                    if(nextposjsondata) {
	                    for(var k = 0; k < nextposjsondata.length; k++) {
	                    	if(nextposjsondata[k].candidateID) {
	                    		checkedcandidateId.push(nextposjsondata[k].candidateID);
	                    	}
	                    }
                    }

                    if(checkedcandidateId) {
                    	for(var h=0; h<checkedcandidateId.length; h++){
                    		for(var l=0; l<candidatejsondata.length; l++) {
                    			if(candidatejsondata[l].currentEmpId === checkedcandidateId[h]) {
                    				candidatejsondata[l].transferFlag ="✓";
                    				that.formatAvailableToIcon(true);
                    				transferflagCount++;
                    				break;
                    			}
                    		}
                    	}
                    }
                    if(CurrenctFilterValueTransferFlag === 2) {
                    	var filterflagData = [];
                    	for(var m = 0; m < checkedcandidateId.length; m++) {
		                    for(var j=0; j< candidatejsondata.length; j++) {
		                    	if(candidatejsondata[j].currentEmpId === checkedcandidateId[m]){
		                    		candidatejsondata[j].transferFlag ="✓";
		                    		that.formatAvailableToIcon(true);
			                    	filterflagData.push(candidatejsondata[j]);
		                    	}
		                    }
                    	}
                    	oModelData.candidates = filterflagData;
                    	oModelData.applicantsCount = filterflagData.length;
                    	oModelData.noTransferflagCount = filterflagData.length;
                    } else if(CurrenctFilterValueTransferFlag === 1) {
                    	var filternoflagdata = candidatejsondata;
                    	for(var o = 0; o< checkedcandidateId.length; o++) {
                    		for(var n = 0; n< filternoflagdata.length; n++) {
                    			if(filternoflagdata[n].currentEmpId === checkedcandidateId[o]){
                    				filternoflagdata.splice(n, 1);
                    				break;
                    			}
                    		}
                    	}
                    	oModelData.candidates = filternoflagdata;
                    	oModelData.applicantsCount = filternoflagdata.length;
                    	oModelData.noTransferflagCount = 0;
                    } else {
                    	oModelData.candidates = candidatejsondata;
                    	oModelData.applicantsCount = candidatejsondata.length;
                    }
                    	oModel.setData(oModelData);
	                	oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                    applicantListTable.setBusy(false);
                    MessageBox.error(that.getMsg("errorMsg"));
                }
            });
        },
    	onCheckCandidateId() {
        	var oView = this.getView();
            var oModel = oView.getModel();
            var candidateIdList = [];
            var that = this;
        	if(oModel.getData().nextpositions){
        		candidateIdList = this.getCandidateIdList();
	            if(candidateIdList.length !== 0) {
	            	var ButtonType = Library.ButtonType;
	            	var oDialog = new Dialog({
		                title: this.getMsg("confirm"),
		                type: 'Message',
		                content: new Text({ 
		                    text:this.getMsg("checkCancelCandidates")
		                }),
		                beginButton: new Button({
		                    type: ButtonType.Emphasized,
		                    text: this.getMsg("continue"),
		                    press: function () {
		                        oDialog.close();
		                        that.setEmptyTransferFlag(candidateIdList);
		                        that.onRightGoBtn(candidateIdList);
		                        that.resetStatus();
		                    }
		                }),
		                endButton: new Button({
		                    text: this.getMsg("cancel"),
		                    press: function () {
		                        oDialog.close();
		                    }
		                }),
		                afterClose: function () {
		                    oDialog.destroy();
		                }
		            });
	            oDialog.open();
	            }else {
	            	this.onRightGoBtn(candidateIdList); 
	            }
	   
            }else{
            	this.onRightGoBtn(candidateIdList);
            }
        },
        onRightGoBtn: function(candidateIdList){
            var oView = this.getView();
            var oModel = oView.getModel();
            var NextFilterValueCompany = this.getSelectedItemText(this.getSelect("slNextCompany"));
            var NextFilterValueBusinessUnit = this.getSelectedItemText(this.getSelect("slNextBusinessunit"));
            var NextFilterValueDivision = this.getSelectedItemText(this.getSelect("slNextDivision"));
            var NextFilterValueDepartment = this.getSelectedItemText(this.getSelect("slNextDepartment"));
            var NextFilterValuePosition = this.getSelectedItemText(this.getSelect("slNextPosition"));
            var NextFilterValueRetirePlan = this.byId("slRetirePlan")._getSelectedItemText();
            var nextPositionParameter="";
            var retirementParameter="";
            var nextCompanyParameter = "";
            var nextBusinessUnitParameter = "";
            
            if (NextFilterValueDivision === "" || NextFilterValueDepartment === "" ) {
                MessageBox.alert(this.getMsg("mandatoryMsg"));
                return;
            }
            if(NextFilterValueCompany !== ""){
                nextCompanyParameter = "&company="+NextFilterValueCompany;
            }
            if(NextFilterValueBusinessUnit !== ""){
                nextBusinessUnitParameter = "&businessUnit="+NextFilterValueBusinessUnit;
            }
            if(NextFilterValuePosition !== ""){
                nextPositionParameter = "&position="+NextFilterValuePosition;
            }
            if(NextFilterValueRetirePlan === "yes" || NextFilterValueRetirePlan === "no"){
                retirementParameter = "&retirement="+NextFilterValueRetirePlan;
            }
            var positionListTable = oView.byId("positionListTable");
            positionListTable.setBusy(true);
            $.ajax({
                url: "/srv_api/nextpositions?division=" + NextFilterValueDivision+"&department=" +NextFilterValueDepartment +nextCompanyParameter +nextBusinessUnitParameter + nextPositionParameter+retirementParameter,
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    positionListTable.setBusy(false);
                    if (result) {
						var oModelData = oModel.getData();
						for(var i=0; i < result.length; i++) {
							result[i].candidateEmpRetire = "";
						}
						oModelData.nextpositions = result;
						oModelData.originNextpositions = JSON.parse(JSON.stringify(result));
					　oModelData.nextpostionsCount = result.length;
						oModel.setData(oModelData);
						oModel.refresh(true);
                    } else {
                        MessageBox.alert(that.getMsg("not_found"));
                        oModelData.nextpositions = {};
                        oModel.setData(oModelData);
                        oModel.refresh(true);
                    }
                },
                error: function (xhr, status, err) {
                    positionListTable.setBusy(false);
                    MessageBox.error(that.getMsg("errorMsg"));
                }
            });
        },
        formatAvailableToIcon : function(bAvailable) {
			return bAvailable ? "sap-icon://accept" : null;
		},
		onDeleteCandidatePlanBtn: function() {
			var nextPositionList = this.getView().getModel().getObject("/nextpositions");
			var isAllDataPassed =  true;
			var checkedStatusList = ["OK", "APPL", "APPD"];
			var sSelectedCaseId = "";

			if(!Array.isArray(nextPositionList)) {
                MessageBox.error(this.getMsg("errorMsg"));
				return;
			}

			nextPositionList.forEach(nextPosition => {
				var isSingleDataPassed = false;
				checkedStatusList.forEach(checkedStatus => {
					isSingleDataPassed = isSingleDataPassed || nextPosition.checkStatus === checkedStatus? true: false;
				});
				isAllDataPassed = isSingleDataPassed && isAllDataPassed;
			});
			sSelectedCaseId = nextPositionList[0].caseID;

			if(isAllDataPassed) {
				MessageBox.error(this.getMsg("CantDeleteCase_error"));
			} else {
				var that = this;
				var ButtonType = Library.ButtonType;
				var oDialog = new Dialog({
					title: this.getMsg("confirm"),
					type: 'Message',
					content: new Text({
						text: this.getMsg("checkDeleteCandidates")
					}),
					beginButton: new Button({
						type: ButtonType.Emphasized,
						text: this.getMsg("continue"),
						press: function () {
							oDialog.close();
							$.ajax({
								url: "/srv_api/candidates/" + sSelectedCaseId,
								method: "DELETE",
								contentType: "application/json",
								success: function (result, xhr, data) {
									MessageBox.information(that.getMsg("success_deleteCase_msg"), {
										onClose: function () {
											location.reload();
										}
									});
								},
								error: function (xhr, status, err) {
									MessageBox.error(that.getMsg("errorMsg"));
								}
							});
						}
					}),
					endButton: new Button({
						text: this.getMsg("cancel"),
						press: function () {
							oDialog.close();
						}
					}),
					afterClose: function () {
						oDialog.destroy();
					}
				});
				oDialog.open();
			}
		},
		onCancelBtn: function(){
        	var oView = this.getView();
            var oModel = oView.getModel();
            var candidateIdList = [];
            var that = this;
        	if(oModel.getData().nextpositions){
        		candidateIdList = this.getCandidateIdList();
	            if(candidateIdList.length !== 0) {
	            	var ButtonType = Library.ButtonType;
	            	var oDialog = new Dialog({
		                title: this.getMsg("confirm"),
		                type: 'Message',
		                content: new Text({ 
		                    text:this.getMsg("checkCancelCandidates")
		                }),
		                beginButton: new Button({
		                    type: ButtonType.Emphasized,
		                    text: this.getMsg("continue"),
		                    press: function () {
		                        oDialog.close();
                                that.onGoButton();
		                    }
		                }),
		                endButton: new Button({
		                    text: this.getMsg("cancel"),
		                    press: function () {
		                        oDialog.close();
		                    }
		                }),
		                afterClose: function () {
		                    oDialog.destroy();
		                }
		            });
	            oDialog.open();
	            }
        	}
		},
		cancelApplicants: function(candidateIdList){
			this.setEmptyTransferFlag(candidateIdList);
			this.setEmptyCandidate();
			var originNextPositions = this.getView().getModel().getData().originNextpositions;
			var data = JSON.parse(JSON.stringify(originNextPositions));
			this.getView().getModel().getData().nextpositions = data;
			this.resetStatus();
		},
		getCandidateIdList: function(){
			var oView = this.getView();
            var oModel = oView.getModel();
            var candidateIdList = [];
            var that = this;
	        for(var i = 0; i < oModel.getData().nextpositions.length; i++) {
	           	if(oModel.getData().nextpositions[i].candidateID !== null) {
	           		candidateIdList.push(oModel.getData().nextpositions[i].candidateID);
	           	}
	        }
	        return	candidateIdList;
		},
		setEmptyTransferFlag: function(candidateIdList){
			var oView = this.getView();
            var oModel = oView.getModel();
	        for(var j = 0; j < candidateIdList.length; j++) {
	        	if(oModel.getData().candidates) {
	        	for(var k = 0; k < oModel.getData().candidates.length; k++) {
	          		if(oModel.getData().candidates[k].currentEmpId === candidateIdList[j]) {
	           			oModel.getData().candidates[k].transferFlag = "";
	           			this.formatAvailableToIcon(false);
	           		}
	           	}
	        	}
	         }
			oModel.refresh();
		},
		setEmptyCandidate: function(){
			var oModel = this.getView().getModel();
			var nextpositionList = this.getView().getModel().getData().nextpositions;
			for(var i = 0; i < nextpositionList.length; i++) {
				nextpositionList[i].candidateID = "";
				nextpositionList[i].candidateName = "";
				nextpositionList[i].currentDivision = "";
				nextpositionList[i].currentDivisionName = "";
				nextpositionList[i].currentDepartment = "";
				nextpositionList[i].currentDepartmentName = "";
				nextpositionList[i].currentPosition = "";
				nextpositionList[i].currentPositionName = "";
				nextpositionList[i].currentJobGrade = "";
				nextpositionList[i].currentJobGrade = "";
				nextpositionList[i].candidateEmpRetire = "";
				nextpositionList[i].jobTenure = "";
				nextpositionList[i].rating1 = "";
				nextpositionList[i].rating2 = "";
				nextpositionList[i].rating3 = "";
			}
			oModel.refresh();
		},
		onClickImage: function(oEvent) {
			var oButton = oEvent.getSource();
			var path = oEvent.getSource().getParent().getRowBindingContext().sPath;
			var candidates = this.getView().getModel().getData().candidates;
			if (!this._oPopover) {
				Fragment.load({
					id:"showInfo",
					name: "com.sap.sfsf.applicants.view.ShowInfo",
					controller: this
				}).then(function(oPopover){
					this._oPopover = oPopover;
					this.getView().addDependent(this._oPopover);
					this._oPopover.bindElement(path);
					this._oPopover.openBy(oButton);
				}.bind(this));
			} else {
				this._oPopover.bindElement(path);
				this._oPopover.openBy(oButton);
			}
		},
		onClickClose: function(oEvent) {
			var oButton = oEvent.getSource();
			this._oPopover.close(oButton);
		},
		resetStatus: function() {
			var nextpositionslistcount = this.getView().getModel().getData().nextpositions.length;
			var candidateslistcount = this.getView().getModel().getData().candidates.length;
			this.getView().getModel().getData().applicantsCount = candidateslistcount;
            this.getView().getModel().getData().noTransferflagCount = 0;
            this.getView().getModel().getData().availabilityCount = 0;
            this.getView().getModel().getData().nextpostionsCount = nextpositionslistcount;
            this.byId("rightProgressIndicator").setPercentValue(0);
            this.byId("leftProgressIndicator").setPercentValue(0);
            this.byId("rightProgressIndicator").setDisplayValue("0%");
            this.byId("leftProgressIndicator").setDisplayValue("0%");
            this.getView().getModel().refresh();
		},
		getCountAvailablePosition: function() {
			var nextpositons = this.getView().getModel().getData().nextpositions;
            var count = this.getView().getModel().getData().nextpostionsCount;
            // var count = this.getView().getModel().getData().nextpositions.length;
			for(var i=0; i<nextpositons.length; i++) {
				if(nextpositons[i].candidateID === null || nextpositons[i].candidateID === "") {
					count--;
				}
			}
			this.getView().getModel().getData().availabilityCount = count;
			var nextpositionLength = this.getView().getModel().getData().originNextpositions.length;
			this.byId("rightProgressIndicator").setPercentValue(count/nextpositionLength*100);
            this.byId("rightProgressIndicator").setDisplayValue(Math.floor(count/nextpositionLength*100) + "%");
            this.getView().getModel().refresh(true);
		},
		getCountAvailableCandidate: function() {
			var candidates = this.getView().getModel().getData().candidates;
			var count = candidates.length;
			for(var i=0; i<candidates.length; i++) {
				if(!candidates[i].transferFlag)  {
					count--;
				} else if(candidates[i].transferFlag === null|| candidates[i].transferFlag === ""){
					count++;
				}
			}
			this.getView().getModel().getData().noTransferflagCount = count;
			this.byId("leftProgressIndicator").setPercentValue(count/candidates.length*100);
			this.byId("leftProgressIndicator").setDisplayValue(Math.floor(count/candidates.length*100) + "%");
		},
		addLines: function(aSelectedCandidateData, droppedIndex) {
			for(var i=0; i < aSelectedCandidateData.length ; i++) {
				var draggedData = aSelectedCandidateData[i];
				if(draggedData.transferFlag === "✓"){
					 MessageBox.alert(this.getMsg("transferAlertMsg"));
                	break;
				}
			var selectedData = this.getView().getModel().getData().nextpositions[droppedIndex];
			var copySelectedData = JSON.parse(JSON.stringify(selectedData));
			copySelectedData.candidateName = draggedData.currentEmpName;
			copySelectedData.candidateID = draggedData.currentEmpId;
			copySelectedData.currentDivision = draggedData.currentDivision;
			copySelectedData.currentDivisionName = draggedData.currentDivisionName;
			copySelectedData.currentDepartment = draggedData.currentDepartment;
			copySelectedData.currentDepartmentName = draggedData.currentDepartmentName;
			copySelectedData.currentPosition = draggedData.currentPosition;
			copySelectedData.currentPositionName = draggedData.currentPositionName;
			copySelectedData.currentJobGrade = draggedData.currentJobGrade;
			copySelectedData.currentJobGrade = draggedData.currentJobGrade;
			copySelectedData.candidateEmpRetire = draggedData.currentEmpRetire;
			copySelectedData.jobTenure = draggedData.jobTenure;
			copySelectedData.rating1 = draggedData.rating1;
			copySelectedData.rating2 = draggedData.rating2;
			copySelectedData.rating3 = draggedData.rating3;
			var nextpostionsdata = this.getView().getModel().getData().nextpositions;
			copySelectedData.isAddLine = true;
			if(i===0) {
				nextpostionsdata.splice(droppedIndex+i, 1, copySelectedData);
			}else {
				nextpostionsdata.splice(droppedIndex+i, 0, copySelectedData);
			}
			this.getCountAvailablePosition();
			if(this.getView().getModel().getData().candidates){
				this.setTransferFlag();
				this.getCountAllCandidates();
				this.getCountAvailableCandidate();
			}
			this.getView().getModel().refresh(true);	
			}
		},
		deleteLine: function(draggedIndex) {
			var nextpostionsdata = this.getView().getModel().getData().nextpositions;
			nextpostionsdata = nextpostionsdata.filter(function (item, index) {
				if (index !== draggedIndex){
					return true;
				}
			});
			var data = JSON.parse(JSON.stringify(nextpostionsdata));
			this.getView().getModel().getData().nextpositions = data;
			this.getCountAllCandidates();
			this.getCountAvailableCandidate();
			this.getView().getModel().refresh(true);
			
		},
		setTransferFlag: function() {
			var candidateIdList = [];
			candidateIdList = this.getCandidateIdList();
			var oView = this.getView();
            var oModel = oView.getModel();
	        for(var j = 0; j < candidateIdList.length; j++) {
	        	for(var k = 0; k < oModel.getData().candidates.length; k++) {
	          		if(oModel.getData().candidates[k].currentEmpId === candidateIdList[j]) {
	           			oModel.getData().candidates[k].transferFlag = "✓";
	           			this.formatAvailableToIcon(true);
	           		}
	           	}
	         }
			oModel.refresh();
		},
		getCountAllPositions: function(){
			var nextpositons = this.getView().getModel().getData().nextpositions;
			var count = nextpositons.length;
			this.getView().getModel().getData().nextpostionsCount = count;
		},
		getCountAllCandidates: function(){
			var candidates = this.getView().getModel().getData().candidates;
			var count = candidates.length;
			this.getView().getModel().getData().applicantsCount = count;
		},
		choosetomakeline: function(droppedIndex, draggedData){
			var that = this;
	            	var ButtonType = Library.ButtonType;
	            	var oDialog = new Dialog({
		                title: this.getMsg("confirm"),
		                type: 'Message',
		                content: new Text({ 
		                    text:this.getMsg("existing_candidates_msg")
		                }),
		                beginButton: new Button({
		                    type: ButtonType.Emphasized,
		                    text: this.getMsg("continue"),
		                    press: function () {
		                        oDialog.close();
		                        that.addAutoLine(droppedIndex, draggedData);
		                    }
		                }),
		                endButton: new Button({
		                    text: this.getMsg("cancel"),
		                    press: function () {
		                        oDialog.close();
		                    }
		                }),
		                afterClose: function () {
		                    oDialog.destroy();
		                }
		            });
	            oDialog.open();
		},
		choosetomakemultilines: function(aSelectedCandidateData, droppedIndex){
			var that = this;
	            	var ButtonType = Library.ButtonType;
	            	var oDialog = new Dialog({
		                title: this.getMsg("confirm"),
		                type: 'Message',
		                content: new Text({ 
		                    text: this.getMsg("existing_candidates_msg")
		                }),
		                beginButton: new Button({
		                    type: ButtonType.Emphasized,
		                    text: this.getMsg("continue"),
		                    press: function () {
		                        oDialog.close();
		                        that.addAutoLineByPosition(droppedIndex);
		                        droppedIndex = parseInt(droppedIndex, 10) + parseInt(1,10);
	                			that.addLines(aSelectedCandidateData, droppedIndex);
		                    }
		                }),
		                endButton: new Button({
		                    text: this.getMsg("cancel"),
		                    press: function () {
		                        oDialog.close();
		                    }
		                }),
		                afterClose: function () {
		                    oDialog.destroy();
		                }
		            });
	            oDialog.open();
		},
		addAutoLine: function(droppedIndex, draggedData) {
			var selectedData = this.getView().getModel().getData().nextpositions[droppedIndex];
			selectedData.isAddLine = true;
			var copySelectedData = JSON.parse(JSON.stringify(selectedData));
			copySelectedData.candidateName = draggedData.currentEmpName;
			copySelectedData.candidateID = draggedData.currentEmpId;
			copySelectedData.currentDivision = draggedData.currentDivision;
			copySelectedData.currentDivisionName = draggedData.currentDivisionName;
			copySelectedData.currentDepartment = draggedData.currentDepartment;
			copySelectedData.currentDepartmentName = draggedData.currentDepartmentName;
			copySelectedData.currentPosition = draggedData.currentPosition;
			copySelectedData.currentPositionName = draggedData.currentPositionName;
			copySelectedData.currentJobGrade = draggedData.currentJobGrade;
			copySelectedData.currentJobGrade = draggedData.currentJobGrade;
			copySelectedData.candidateEmpRetire = draggedData.currentEmpRetire;
			copySelectedData.jobTenure = draggedData.jobTenure;
			copySelectedData.rating1 = draggedData.rating1;
			copySelectedData.rating2 = draggedData.rating2;
			copySelectedData.rating3 = draggedData.rating3;
			copySelectedData.isAddLine = true;
			var nextpostionsdata = this.getView().getModel().getData().nextpositions;
			nextpostionsdata.splice(droppedIndex+1, 0, copySelectedData);
			this.setTransferFlag();
			this.getCountAllCandidates();
			this.getCountAvailableCandidate();
			this.getView().getModel().refresh(true);
		},
		addAutoLineByPosition: function(droppedIndex) {
			var selectedData = this.getView().getModel().getData().nextpositions[droppedIndex];
			selectedData.isAddLine = true;
			var copySelectedData = JSON.parse(JSON.stringify(selectedData));
			copySelectedData.candidateName = null;
			copySelectedData.candidateID = null;
			copySelectedData.currentDivision = null;
			copySelectedData.currentDivisionName = null;
			copySelectedData.currentDepartment = null;
			copySelectedData.currentDepartmentName = null;
			copySelectedData.currentPosition = null;
			copySelectedData.currentPositionName = null;
			copySelectedData.currentJobGrade = null;
			copySelectedData.currentJobGrade = null;
			copySelectedData.candidateEmpRetire = null;
			copySelectedData.jobTenure = null;
			copySelectedData.rating1 = null;
			copySelectedData.rating2 = null;
			copySelectedData.rating3 = null;
			copySelectedData.isAddLine = true;
			var nextpostionsdata = this.getView().getModel().getData().nextpositions;
			nextpostionsdata.splice(parseInt(droppedIndex, 10)+parseInt(1, 10), 0, copySelectedData);
			this.getView().getModel().refresh(true);
		},
		mentionDuplicate: function(oEvent) {
			var sPath = oEvent.getSource().getParent().getRowBindingContext().sPath;
			var droppedIndex = sPath.split("/")[sPath.split("/").length - 1];
			var selectedCandidateId = this.getView().getModel().getData().nextpositions[droppedIndex].candidateID;
			this.getView().getModel().getData().path = sPath;
			var selectedPosition = this.getView().getModel().getProperty(sPath).nextPosition;
			if(selectedCandidateId===null) {
                this.handleNextPositionNamePress(oEvent, selectedPosition);
            } else if(selectedCandidateId==="") {
                this.handleNextPositionNamePress(oEvent, selectedPosition);
            } else {
			    var that = this;
	            var ButtonType = Library.ButtonType;
	            var oDialog = new Dialog({
		            title: this.getMsg("confirm"),
		            type: 'Message',
		            content: new Text({ 
		                text: this.getMsg("existing_candidates_msg")
		            }),
		            beginButton: new Button({
		                type: ButtonType.Emphasized,
		                text: this.getMsg("continue"),
		                press: function () {
		                    oDialog.close();
		                    that.addAutoLineByPosition(droppedIndex);
		                    var addedPath = parseInt(droppedIndex, 10) + parseInt(1, 10);
		                    that.getView().getModel().getData().path = sPath.split("/")[0] + "/" + addedPath;
		                    that.handleNextPositionNamePress(oEvent, selectedPosition);
		                }
		            }),
		            endButton: new Button({
		                text: this.getMsg("cancel"),
		                press: function () {
		                    oDialog.close();
		                }
		            }),
		            afterClose: function () {
		                oDialog.destroy();
		            }
		        });
	            oDialog.open();
			}
		},
		handleNextPositionNamePress: function(oEvent, selectedPosition) {
			this._oBasicSearchField = new SearchField({
				showSearchButton: false
			});
			var oModel = this.getView().getModel();
            var nextpositiondata = oModel.getData().nextpositions;
            this._oPositionValueHelpDialog = sap.ui.xmlfragment("com.sap.sfsf.applicants.view.PositionDialog", this);
            this.getView().addDependent(this._oPositionValueHelpDialog);
            this._oPositionValueHelpDialog.setBusy(true);

			this._oPositionValueHelpDialog.setRangeKeyFields([{
				label: "Product",
				key: "ProductId",
				type: "string",
				typeInstance: new typeString({}, {
					maxLength: 7
				})
			}]);
			this._oPositionValueHelpDialog.getFilterBar().setBasicSearch(this._oBasicSearchField);
            this._oPositionValueHelpDialog.open();
            var that = this;
			$.ajax({
                url: "/srv_api/userpositionprecisions?currentPosition=" + selectedPosition,
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    that._oPositionValueHelpDialog.setBusy(false);
                    var modelData = oModel.getData();
                    result = result.filter(function(item, index) {
                    	var count=0;
                    	for(var i = 0; i < nextpositiondata.length; i++) {
                    		if(nextpositiondata[i].candidateID === item.currentEmpId) {
                    			count++;
                    		}
                    	}
                    	if(count===0) {
                    		return true;
                    	}
                    });
                    modelData.userpositionprecisions = result;
                    oModel.setData(modelData);
                    oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                    MessageBox.error(that.getMsg("errorMsg"));
                    that._oPositionValueHelpDialog.setBusy(false);
                }
            });
		},
		onPositionValueHelpOkPress: function (oEvent) {
			var currentEmpId ="";
			var tokens = oEvent.getParameter("tokens");
			var path = this.getView().getModel().getData().path;
			var index = path.split("/")[path.length - 1];
			if(tokens.length === 0) {
				path = this.getView().getModel().getData().path;
				index = path.split("/")[path.length - 1];
				this.deleteLine(parseInt(index,10));
				this._oPositionValueHelpDialog.close();
				return;
			}
			var oView = this.getView();
			var nextpositions = this.getView().getModel().getData().nextpositions;
			var isDuplicate = tokens.filter(function(item, index) {
				var selectedcurrentEmpId = tokens[index].mProperties.key;
				for(var i=0; i < nextpositions.length; i++) {
					if(nextpositions[i].candidateID===selectedcurrentEmpId){
						return true;
					}
				}
			});
			if(isDuplicate.length > 0) {
				 MessageBox.alert(this.getMsg("transferAlertMsg"));
				 this.deleteLine(parseInt(index,10));
				 this._oPositionValueHelpDialog.close();
			}else {
			for(var i = 0; i < oEvent.getParameter("tokens").length; i++) {
				if(i!==0) {
					currentEmpId = currentEmpId + "," + oEvent.getParameter("tokens")[i].mProperties.key;
				}else{
					currentEmpId = oEvent.getParameter("tokens")[i].mProperties.key;
				}
            }
            if(currentEmpId.length > 512) {
                MessageBox.alert(this.getMsg("toomanyusers"));
                return;
            }
			var that = this;
			var sPath = this.getView().getModel().getData().path;
			var droppedIndex = sPath.split("/")[sPath.split("/").length - 1];
            var selectedCandidateId = this.getView().getModel().getData().nextpositions[droppedIndex].candidateID;
            var positionListTable = oView.byId("positionListTable");
            positionListTable.setBusy(true);
				$.ajax({
				//TODO 変更**************************************************************
                url: "/srv_api/currentpositions?currentEmpIds=" + currentEmpId,
                // url: "/srv_api/selectedCandidates?currentEmpId=" + currentEmpId,
                //**************************************************************
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    positionListTable.setBusy(false);
                    if (result) {
                    //TODO 削除**************************************************************
                //    currentEmpId = currentEmpId.split(',');
                //    	result = result.filter(function (item, index) {
                //    		for(var i=0; i<currentEmpId.length; i++) {
                //    			if (item.currentEmpId === currentEmpId[i]) {
				// 					return true;
				// 				}
                //    		}
				// 		});
                    //**************************************************************
	                    var oModelData = oView.getModel().getData();
	                    var oModel = oView.getModel();
	                   oModelData.selectedCandidates = result;
	                   if(result.length > 1) {
	                   	that.addLinesByPosition(result, droppedIndex);
	                   }
	                   that.addInfoByPosition(result, droppedIndex);
	                   oModel.setData(oModelData);
	                   oModel.refresh(true);
	                 } else {
	                        MessageBox.alert(that.getMsg("not_found"));
	                        oModelData.selectedCandidates = {};
	                        oModel.setData(oModelData);
	                        oModel.refresh(true);
	                    }
	            	 },
                error: function (xhr, status, err) {
                    positionListTable.setBusy(false);
                    MessageBox.error(that.getMsg("errorMsg"));
                	}
            	});
			}
			this._oPositionValueHelpDialog.close();
		},
		onPositionValueHelpCancelPress: function () {
			var path = this.getView().getModel().getData().path;
			var index = path.split("/")[path.length - 1];
			this.deleteLine(parseInt(index,10));
			this._oPositionValueHelpDialog.close();
		},
		onFilterBarSearch: function (oEvent) {
			var sSearchQuery = this._oBasicSearchField.getValue(),
				aSelectionSet = oEvent.getParameter("selectionSet");
			var aFilters = aSelectionSet.reduce(function (aResult, oControl) {
				if (oControl.getValue()) {
					aResult.push(new Filter({
						path: oControl.getName(),
						operator: FilterOperator.Contains,
						value1: oControl.getValue()
					}));
				}

				return aResult;
			}, []);
			aFilters.push(new Filter({
				filters: [
					new Filter({ path: "UserPositionPrecisionsRate", operator: FilterOperator.Contains, value1: sSearchQuery })
				],
				and: false
			}));
			this._filterTable(new Filter({
				filters: aFilters,
				and: true
			}));
		},
		_filterTable: function (oFilter) {
			this.oColModel = new JSONModel({
									"cols": [
										{
											"label": this.getMsg("candidateID"),
											"template": "currentEmpId"
										},
										{
											"label": this.getMsg("candidateName"),
											"template": "currentEmpName"
										},
										{
											"label": this.getMsg("competencyPercentage"),
											"template": "competencyPercentage"
										}
									]
								}
							);
		 	var aCols = this.oColModel.getData().cols;
		 	var aUserPositionPrecisionsData = this.getView().getModel().getData().userpositionprecisions;
		 	var iUserPositionPrecisionsRate = oFilter.aFilters[0].oValue1;
		 	aUserPositionPrecisionsData = aUserPositionPrecisionsData.filter(function (item, index) {
		 		if(iUserPositionPrecisionsRate === "ALL") {
		 			return true;
		 		}else {
					if (parseInt(item.competencyPercentage, 10) >= parseInt(iUserPositionPrecisionsRate)) {
							return true;
					}
		 		}
			});
			this.oUserPositionPrecisionsModel = new JSONModel(aUserPositionPrecisionsData);
			var aUserPositionPrecisionsModel = this.oUserPositionPrecisionsModel.getData();
			this._oPositionValueHelpDialog.getTableAsync().then(function (oTable) {
				oTable.setModel(this.oColModel, "columns");
				oTable.setModel(this.oUserPositionPrecisionsModel);

				if (oTable.bindRows) {
					oTable.bindAggregation("rows", "/", function () {
						return new ColumnListItem({
							cells: aUserPositionPrecisionsModel.map(function (column) {
									if(column.competencyPercentage === iUserPositionPrecisionsRate){
										return true;
									}
							})
						});
					});
				}
				
				if (oTable.bindItems) {
					oTable.bindAggregation("items", "/", function () {
						return new ColumnListItem({
							cells: aCols.map(function (column) {
								return new Label({ text: "{" + column.template + "}" });
							})
						});
					});
				}

				this._oPositionValueHelpDialog.update();
			}.bind(this));
		},
		addLinesByPosition: function(aSelectedCandidateData, droppedIndex) {
			if(aSelectedCandidateData.length <= 1) {
	                this.putInfoNextPosition(aSelectedCandidateData, droppedIndex);
	                this.getCountAvailablePosition();
			}else {
				for(var i=0; i < aSelectedCandidateData.length ; i++) {
					var draggedData = aSelectedCandidateData[i];
					if(draggedData.transferFlag === "✓"){
						 MessageBox.alert(this.getMsg("transferAlertMsg"));
	                	break;
					}
				var selectedData = this.getView().getModel().getData().nextpositions[droppedIndex];
				var copySelectedData = JSON.parse(JSON.stringify(selectedData));
				copySelectedData.candidateName = draggedData.currentEmpName;
				copySelectedData.candidateID = draggedData.currentEmpId;
				copySelectedData.currentDivision = draggedData.currentDivision;
				copySelectedData.currentDivisionName = draggedData.currentDivisionName;
				copySelectedData.currentDepartment = draggedData.currentDepartment;
				copySelectedData.currentDepartmentName = draggedData.currentDepartmentName;
				copySelectedData.currentPosition = draggedData.currentPosition;
				copySelectedData.currentPositionName = draggedData.currentPositionName;
				copySelectedData.currentJobGrade = draggedData.currentJobGrade;
				copySelectedData.currentJobGrade = draggedData.currentJobGrade;
				copySelectedData.candidateEmpRetire = draggedData.currentEmpRetire;
				copySelectedData.jobTenure = draggedData.jobTenure;
				copySelectedData.rating1 = draggedData.rating1;
				copySelectedData.rating2 = draggedData.rating2;
				copySelectedData.rating3 = draggedData.rating3;
				copySelectedData.transferFlag = "✓";
				var nextpostionsdata = this.getView().getModel().getData().nextpositions;
				copySelectedData.isAddLine = true;
				if(i===0) {
					nextpostionsdata.splice(parseInt(droppedIndex, 10)+parseInt(i, 10), 1, copySelectedData);
				}else {
					nextpostionsdata.splice(parseInt(droppedIndex, 10)+parseInt(i, 10), 0, copySelectedData);
				}
				this.getCountAvailablePosition();
				if(this.getView().getModel().getData().candidates){
					this.setTransferFlag();
					this.getCountAllCandidates();
					this.getCountAvailableCandidate();
				}
				}
			}
			this.getView().getModel().refresh(true);
		},
		putInfoNextPosition: function (aSelectedCandidateData, droppedIndex) {
			var oModel = this.getView().getModel();
	        oModel.getData().nextpositions[droppedIndex].candidateID=aSelectedCandidateData[0].candidateID;
			oModel.getData().nextpositions[droppedIndex].candidateName=aSelectedCandidateData[0].candidateName;
			oModel.getData().nextpositions[droppedIndex].currentDivision=aSelectedCandidateData[0].currentDivision;
			oModel.getData().nextpositions[droppedIndex].currentDivisionName=aSelectedCandidateData[0].currentDivisionName;
			oModel.getData().nextpositions[droppedIndex].currentDepartment=aSelectedCandidateData[0].currentDepartment;
			oModel.getData().nextpositions[droppedIndex].currentDepartmentName=aSelectedCandidateData[0].currentDepartmentName;
			oModel.getData().nextpositions[droppedIndex].currentPosition=aSelectedCandidateData[0].currentPosition;
			oModel.getData().nextpositions[droppedIndex].currentPositionName=aSelectedCandidateData[0].currentPositionName;
			oModel.getData().nextpositions[droppedIndex].currentJobGrade=aSelectedCandidateData[0].currentJobGrade;
			oModel.getData().nextpositions[droppedIndex].currentJobGrade=aSelectedCandidateData[0].currentJobGrade;
			oModel.getData().nextpositions[droppedIndex].candidateEmpRetire=aSelectedCandidateData[0].candidateEmpRetire;
			oModel.getData().nextpositions[droppedIndex].jobTenure=aSelectedCandidateData[0].jobTenure;
			oModel.getData().nextpositions[droppedIndex].rating1=aSelectedCandidateData[0].rating1;
			oModel.getData().nextpositions[droppedIndex].rating2=aSelectedCandidateData[0].rating2;
			oModel.getData().nextpositions[droppedIndex].rating3=aSelectedCandidateData[0].rating3;
			oModel.getData().nextpositions[droppedIndex].transferFlag = "✓";
		},
		addInfoByPosition: function (aSelectedCandidateData, droppedIndex) {
			var oModel = this.getView().getModel();
	        oModel.getData().nextpositions[droppedIndex].candidateID=aSelectedCandidateData[0].currentEmpId;
			oModel.getData().nextpositions[droppedIndex].candidateName=aSelectedCandidateData[0].currentEmpName;
			oModel.getData().nextpositions[droppedIndex].currentDivision=aSelectedCandidateData[0].currentDivision;
			oModel.getData().nextpositions[droppedIndex].currentDivisionName=aSelectedCandidateData[0].currentDivisionName;
			oModel.getData().nextpositions[droppedIndex].currentDepartment=aSelectedCandidateData[0].currentDepartment;
			oModel.getData().nextpositions[droppedIndex].currentDepartmentName=aSelectedCandidateData[0].currentDepartmentName;
			oModel.getData().nextpositions[droppedIndex].currentPosition=aSelectedCandidateData[0].currentPosition;
			oModel.getData().nextpositions[droppedIndex].currentPositionName=aSelectedCandidateData[0].currentPositionName;
			oModel.getData().nextpositions[droppedIndex].currentJobGrade=aSelectedCandidateData[0].currentJobGrade;
			oModel.getData().nextpositions[droppedIndex].currentJobGrade=aSelectedCandidateData[0].currentJobGrade;
			oModel.getData().nextpositions[droppedIndex].candidateEmpRetire=aSelectedCandidateData[0].candidateEmpRetire;
			oModel.getData().nextpositions[droppedIndex].jobTenure=aSelectedCandidateData[0].jobTenure;
			oModel.getData().nextpositions[droppedIndex].rating1=aSelectedCandidateData[0].rating1;
			oModel.getData().nextpositions[droppedIndex].rating2=aSelectedCandidateData[0].rating2;
			oModel.getData().nextpositions[droppedIndex].rating3=aSelectedCandidateData[0].rating3;
			oModel.getData().nextpositions[droppedIndex].transferFlag = "✓";
			if(this.getView().getModel().getData().candidates){
					this.setTransferFlag();
				}
        },
		_switchRightPaneContent: function(isNewCase) {
            var oView = this.getView();
            var oModel = oView.getModel();
                //Filterbarを変更する
                if(isNewCase) {
                    oView.byId("RightFilterBar").setHeader(this.getMsg("nextPositionList"));
                    this.byId("filterGroupItem1").setVisible(false);
                    this.byId("filterGroupItem2").setVisible(true);
                    this.byId("filterGroupItem3").setVisible(true);
                    this.byId("filterGroupItem4").setVisible(true);
                    this.byId("filterGroupItem5").setVisible(true);
                    this.byId("filterGroupItem6").setVisible(true);
                    this.byId("filterGroupItem7").setVisible(true);
					this.byId("deleteCandidatePlanBtn").setVisible(false);
                    //tableリフレッシュ
                    if(oModel.getData().nextpositions){
                        oModel.getData().saveExistCase= JSON.parse(JSON.stringify(oModel.getData().nextpositions));
                    }
                    oModel.getData().nextpositions= oModel.getData().saveNewCase;
                    oModel.getData().originNextpositions= oModel.getData().saveNewCase;
                    // oModel.getData().originNewCase= oModel.getData().saveNewCase;

                    oModel.getData().availabilityCountExistCase= oModel.getData().availabilityCount;
                    oModel.getData().nextpostionsCountExistCase= oModel.getData().nextpostionsCount;
                    oModel.getData().availabilityCount= oModel.getData().availabilityCountNewCase;
                    oModel.getData().nextpostionsCount= oModel.getData().nextpostionsCountNewCase;

                    oModel.refresh(true);
                    this._changeProgressBar();
                } else {
                    var selectedCaseID = this.getSelectedItemText(this.getSelect("slCaseid"));
                    oView.byId("RightFilterBar").setHeader(this.getMsg("nextPositionList")+"("+selectedCaseID+")");
                    this.byId("filterGroupItem1").setVisible(true);
                    this.byId("filterGroupItem2").setVisible(false);
                    this.byId("filterGroupItem3").setVisible(false);
                    this.byId("filterGroupItem4").setVisible(false);
                    this.byId("filterGroupItem5").setVisible(false);
                    this.byId("filterGroupItem6").setVisible(false);
                    this.byId("filterGroupItem7").setVisible(false);
					this.byId("deleteCandidatePlanBtn").setVisible(true);
					this.byId("deleteCandidatePlanBtn").setEnabled(false);
                    if(oModel.getData().nextpositions){
                        oModel.getData().saveNewCase= JSON.parse(JSON.stringify(oModel.getData().nextpositions));
                    }
                    oModel.getData().nextpositions= oModel.getData().saveExistCase;
                    oModel.getData().originNextpositions= oModel.getData().saveExistCase;
                    // oModel.getData().originExistCase= oModel.getData().saveExistCase;

                    oModel.getData().availabilityCountNewCase= oModel.getData().availabilityCount;
                    oModel.getData().nextpostionsCountNewCase= oModel.getData().nextpostionsCount;
                    oModel.getData().availabilityCount= oModel.getData().availabilityCountExistCase;
                    oModel.getData().nextpostionsCount= oModel.getData().nextpostionsCountExistCase;

                    oModel.refresh(true);
                    this._changeProgressBar();
                }
        },
        _changeProgressBar: function () {
            var oView = this.getView();
            var oModel = oView.getModel();
             if(oModel.getData().nextpositions){
                this.resetTransferFlag();
                this.setTransferFlag();
            }else {
                this.resetTransferFlag();
                this.getView().getModel().getData().availabilityCount=0;
                this.getView().getModel().getData().nextpostionsCount=0;
                this.byId("rightProgressIndicator").setPercentValue(0);
                this.byId("rightProgressIndicator").setDisplayValue("0%");
            }
            if(oModel.getData().candidates) {
                this.getCountAvailableCandidate();
                this.getCountAllCandidates();
            }
            oModel.refresh(true);
        },
        onGoByCaseID: function() {
            var that = this;
            var oView = this.getView();
            var oModel = oView.getModel();
            var selectedCaseID = this.getSelectedItemText(this.getSelect("slCaseid"));
            oView.byId("RightFilterBar").setHeader(this.getMsg("nextPositionList")+"("+selectedCaseID+")");
            var positionListTable = oView.byId("positionListTable");
            positionListTable.setBusy(true);
             $.ajax({
                 //TODO 変更**************************************************************
                url: "/srv_api/candidates/"+selectedCaseID,
                // url: "/srv_api/selectedCaseid"+selectedCaseID,
                //**************************************************************
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    positionListTable.setBusy(false);
                    if (result) {
                        var oModelData = oModel.getData();
                        var jsonresult = JSON.parse(result);
						oModelData.nextpositions = jsonresult;
						oModelData.originNextpositions = JSON.parse(result);
					　oModelData.nextpostionsCount = jsonresult.length;
                        oModel.setData(oModelData);
                        that.getCountAvailablePosition();
                        if(oModelData.candidates) {
                            that.resetTransferFlag();
                            that.setTransferFlag();
                            that.getCountAvailableCandidate();
                        }
                        oModel.refresh(true);
						that.oView.byId("deleteCandidatePlanBtn").setEnabled(true);
                    } else {
                        MessageBox.alert(that.getMsg("not_found"));
                        oModelData.nextpositions = {};
                        oModel.setData(oModelData);
                        oModel.refresh(true);
                    }
                },
                error: function (xhr, status, err) {
                    positionListTable.setBusy(false);
                    MessageBox.error(that.getMsg("errorMsg"));
                }
            });
        },
        onGoButton: function(){
                 if(isNewCase) {
                    this.onCheckCandidateId();
                }else if (!isNotNewCase) {
                    this.onGoByCaseID();
                }
        },
		resetTransferFlag: function() {
			var oView = this.getView();
            var oModel = oView.getModel();
            if(oModel.getData().candidates){
                for(var k = 0; k < oModel.getData().candidates.length; k++) {
                    oModel.getData().candidates[k].transferFlag = "";
                    this.formatAvailableToIcon(false);
                }
                oModel.refresh();
            }
		}
    });
});
