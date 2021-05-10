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
    
    //to show in combobox, filterbar
    var DEFAULT_DIVISION = {
		externalCode: "",
		name: "ALL"
	};    
    var DEFAULT_DEPARTMENT = {
		externalCode: "",
		name: "ALL"
	};
	var DEFAULT_POSITION = {
		code: "",
		name: "ALL"
	};
	var DEFAULT_BUSINESSUNIT = {
		externalCode: "",
		name: "ALL"
	};
	
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
            });
            this.getView().setModel(oViewModel,"view");
            
            //Initialize data count and progress indicator
            this.getView().getModel().getData().applicantsCount = 0;
            this.getView().getModel().getData().noTransferflagCount = 0;
        	this.getView().getModel().getData().availabilityCount = 0;
        	this.getView().getModel().getData().nextpostionsCount = 0;
        	this.byId("leftProgressIndicator").setPercentValue(0);
        	this.byId("rightProgressIndicator").setPercentValue(0);
        	
        	//External Connections to get the data of select conditions 
            $.ajax({
                url: "/srv_api/departments",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    result.unshift(DEFAULT_DEPARTMENT);
                    modelData.department = result;
                    modelData.currentDepartment = result;
                    modelData.nextDepartment = result;
                    oModel.setData(modelData);
                    oViewModel.setProperty("/departmentBusy", false);
                    oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                	oViewModel.setProperty("/departmentBusy", false);
                    MessageBox.error(that._getMsg("departmentErrorMsg"));
                }
            });
            $.ajax({
                url: "/srv_api/divisions",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    result.unshift(DEFAULT_DIVISION);
					modelData.division = result;
                    modelData.currentDivision = result;
                    modelData.nextDivision = result;
					oModel.setData(modelData);
					oViewModel.setProperty("/divisionBusy", false);
					oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                	oViewModel.setProperty("/divisionBusy", false);
                    MessageBox.error(that._getMsg("divisionErrorMsg"));
                }
            });
            $.ajax({
                url: "/srv_api/positions",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    result.unshift(DEFAULT_POSITION);
					modelData.position = result;
					modelData.currentPosition = result;
					modelData.nextPosition = result;
					oModel.setData(modelData);
					oViewModel.setProperty("/positionBusy", false);
					oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                	oViewModel.setProperty("/positionBusy", false);
                    MessageBox.error(that._getMsg("positionErrorMsg"));
                }
            });
            $.ajax({
                url: "/config_api/jpconfig",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    var startDateTime =  "（" + result.startDateTime + that._getMsg("dateValue") + "）";
                    modelData.jpconfig = startDateTime;
                    oModel.setData(modelData);
                    oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                    MessageBox.error(that._getMsg("dateErrorMsg"));
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
                    //to set BEST RUN Japan as a default data (Temporary)
		            that.getView().byId("slCompany").setSelectedKey("5000");
		            that.getView().byId("slNextCompany").setSelectedKey("5000");
		            //******************************************************************
                    oModel.setData(modelData);
                    oViewModel.setProperty("/companyBusy", false);
                    oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                	oViewModel.setProperty("/companyBusy", false);
                    MessageBox.error(that._getMsg("companyErrorMsg"));
                }
            });
            $.ajax({
                url: "/srv_api/businessunits",
                method: "GET",
                contentType: "application/json",
                success: function (result, xhr, data) {
                    var modelData = oModel.getData();
                    result.unshift(DEFAULT_BUSINESSUNIT);
                    modelData.businessunit = result;
                    modelData.currentBusinessunit = result;
                    modelData.nextBusinessunit = result;
                    oModel.setData(modelData);
                    oViewModel.setProperty("/businessunitBusy", false);
                    oModel.refresh(true);
                },
                error: function (xhr, status, err) {
                	oViewModel.setProperty("/businessunitBusy", false);
                    MessageBox.error(that._getMsg("businessunitErrorMsg"));
                }
            });
            
            this.byId("left").setSize("100%");
            this.byId("right").setSize("0%");
        },
		onSlCurrentCompanyChange: function (oEvent) {
			var oModel = this.getView().getModel();
			var modelData = oModel.getData();
			var CurrentFilterValueCompany = this._getSelectedItemText(this._getSelect("slCompany"));
			var currentPosition = [];
			var currentBusinessUnit = [];
			var currentDivision = [];
			var currentDepartment = [];
			
			if (CurrentFilterValueCompany == "") {
				modelData.currentPosition = modelData.position;
				modelData.currentBusinessunit = modelData.businessunit;
				modelData.currentDivision = modelData.division;
				modelData.currentDepartment = modelData.department;
				oModel.refresh(true);
				this.getView().setModel(oModel);
				return;
			}
			//set position data which selected company has
			currentPosition = modelData.position.filter(function (item, index) {
				if (item.code !== "" && item.hasOwnProperty("company")) {
					if (CurrentFilterValueCompany === item.company) {
						return true;
					}
				}
			});
			//Get only Business Unit externalCode data from narrowed down position data
			var key = "businessUnit";
			var sCurrentBusinessUnit = new Set(currentPosition.map(e => e[key]));
			var acurrentBusinessUnit = currentPosition.filter(e => {
			    return sCurrentBusinessUnit.has(e[key]) && sCurrentBusinessUnit.delete(e[key]);
			});
			//Set Business Unit data using Business Unit externalCode data
			for(var i=0; i<acurrentBusinessUnit.length; i++) {
				for(var j=0; j<modelData.businessunit.length; j++) {
					if(acurrentBusinessUnit[i].businessUnit === modelData.businessunit[j].externalCode) {
						currentBusinessUnit.push(modelData.businessunit[j]);
					}
				}
			}
			//Get only Divition externalCode data from narrowed down position data
			var key = "division";
			var sCurrentDivision = new Set(currentPosition.map(e => e[key]));
			var acurrentDivision = currentPosition.filter(e => {
			    return sCurrentDivision.has(e[key]) && sCurrentDivision.delete(e[key]);
			});
			//Set Divition data using Divition externalCode data
			for(var i=0; i<acurrentDivision.length; i++) {
				for(var j=0; j<modelData.division.length; j++) {
					if(acurrentDivision[i].division === modelData.division[j].externalCode) {
						currentDivision.push(modelData.division[j]);
					}
				}
			}
			//Get only Department externalCode data from narrowed down position data
			var key = "department";
			var sCurrentDepartment = new Set(currentPosition.map(e => e[key]));
			var acurrentDepartment = currentPosition.filter(e => {
			    return sCurrentDepartment.has(e[key]) && sCurrentDepartment.delete(e[key]);
			});
			//Set Department data using Department externalCode data
			for(var i=0; i<acurrentDepartment.length; i++) {
				for(var j=0; j<modelData.department.length; j++) {
					if(acurrentDepartment[i].department === modelData.department[j].externalCode) {
						currentDepartment.push(modelData.department[j]);
					}
				}
			}
			currentPosition.unshift(DEFAULT_POSITION);
			currentBusinessUnit.unshift(DEFAULT_BUSINESSUNIT);
			currentDivision.unshift(DEFAULT_DIVISION);
			currentDepartment.unshift(DEFAULT_DEPARTMENT);
			modelData.currentPosition = currentPosition;
			modelData.currentBusinessunit = currentBusinessUnit;
			modelData.currentDivision = currentDivision;
			modelData.currentDepartment = currentDepartment;
			oModel.refresh(true);
			this.getView().setModel(oModel);
		},
		onSlCurrentBusinessUnitChange: function(oEvent) {
			var oModel = this.getView().getModel();
			var modelData = oModel.getData();
			var CurrentFilterValueBusinessUnit = this._getSelectedItemText(this._getSelect("slBusinessunit"));
			var currentDivision = [];
			var currentDepartment = [];
			var currentPosition = [];
			if (CurrentFilterValueBusinessUnit == "") {
				modelData.currentDivision = modelData.division;
				modelData.currentPosition = modelData.position;
				modelData.currentDepartment = modelData.department;
				oModel.refresh(true);
				this.getView().setModel(oModel);
				return;
			}
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
			currentDivision.unshift(DEFAULT_DIVISION);
			currentPosition.unshift(DEFAULT_POSITION);
			currentDepartment.unshift(DEFAULT_DEPARTMENT);
			modelData.currentPosition = currentPosition;
			modelData.currentDepartment = currentDepartment;
			modelData.currentDivision = currentDivision;
			oModel.refresh(true);
			this.getView().setModel(oModel);
		},
		onSlCurrentDivisionChange: function (oEvent) {
			var oModel = this.getView().getModel();
			var modelData = oModel.getData();
			var CurrentFilterValueDivision = this._getSelectedItemText(this._getSelect("slDivision"));
			var currentDepartment = [];
			var currentPosition = [];
			var CurrentFilterValueCompany = this._getSelectedItemText(this._getSelect("slCompany"));
			var CurrentFilterValueBusinessUnit = this._getSelectedItemText(this._getSelect("slBusinessunit"));

			if (CurrentFilterValueDivision == "") {
				modelData.currentDepartment = modelData.department;
				modelData.currentPosition = modelData.position;
				oModel.refresh(true);
				this.getView().setModel(oModel);
				return;
			}
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
			currentDepartment.unshift(DEFAULT_DEPARTMENT);
			currentPosition.unshift(DEFAULT_POSITION);
			modelData.currentDepartment = currentDepartment;
			modelData.currentPosition = currentPosition;
			oModel.refresh(true);
			this.getView().setModel(oModel);
		},
		onSlCurrentDepartmentChange: function (oEvent) {
			var oModel = this.getView().getModel();
			var modelData = oModel.getData();
			var CurrentFilterValueDepartment = this._getSelectedItemText(this._getSelect("slDepartment"));
			var currentPosition = [];

			if (CurrentFilterValueDepartment == "") {
				modelData.currentPosition = modelData.position;
				this.getView().setModel(oModel);
				return;
			}
			currentPosition = modelData.currentPosition.filter(function (item, index) {
				if (item.code !== "" && item.hasOwnProperty("department")) {
					if (CurrentFilterValueDepartment === item.department) {
						return true;
					}
				}
			});
			currentPosition.unshift(DEFAULT_POSITION);
			modelData.currentPosition = currentPosition;
			oModel.refresh(true);
			this.getView().setModel(oModel);
		},
		onSlNextCompanyChange: function (oEvent) {
			var oModel = this.getView().getModel();
			var modelData = oModel.getData();
			var NextFilterValueCompany = this._getSelectedItemText(this._getSelect("slNextCompany"));
			var nextPosition = [];
			var nextBusinessUnit = [];
			var nextDivision = [];
			var nextDepartment = [];
			
			if (NextFilterValueCompany == "") {
				modelData.nextPosition = modelData.position;
				modelData.nextBusinessunit = modelData.businessunit;
				modelData.nextDivision = modelData.division;
				modelData.nextDepartment = modelData.department;
				oModel.refresh(true);
				this.getView().setModel(oModel);
				return;
			}
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
			nextPosition.unshift(DEFAULT_POSITION);
			nextBusinessUnit.unshift(DEFAULT_BUSINESSUNIT);
			nextDivision.unshift(DEFAULT_DIVISION);
			nextDepartment.unshift(DEFAULT_DEPARTMENT);
			modelData.nextPosition = nextPosition;
			modelData.nextBusinessunit = nextBusinessUnit;
			modelData.nextDivision = nextDivision;
			modelData.nextDepartment = nextDepartment;
			oModel.refresh(true);
			this.getView().setModel(oModel);
		},
		onSlNextBusinessUnitChange: function(oEvent) {
			var oModel = this.getView().getModel();
			var modelData = oModel.getData();
			var NextFilterValueBusinessUnit = this._getSelectedItemText(this._getSelect("slNextBusinessunit"));
			var nextDivision = [];
			var nextDepartment = [];
			var nextPosition = [];
			if (NextFilterValueBusinessUnit == "") {
				modelData.nextDivision = modelData.division;
				modelData.nextPosition = modelData.position;
				modelData.nexttDepartment = modelData.department;
				oModel.refresh(true);
				this.getView().setModel(oModel);
				return;
			}
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
			nextDivision.unshift(DEFAULT_DIVISION);
			nextPosition.unshift(DEFAULT_POSITION);
			nextDepartment.unshift(DEFAULT_DEPARTMENT);
			modelData.nextPosition = nextPosition;
			modelData.nextDepartment = nextDepartment;
			modelData.nextDivision = nextDivision;
			oModel.refresh(true);
			this.getView().setModel(oModel);

		},
		onSlNextDivisionChange: function (oEvent) {
			var oModel = this.getView().getModel();
			var modelData = oModel.getData();
			var NextFilterValueDivision = this._getSelectedItemText(this._getSelect("slNextDivision"));
			var nextDepartment = [];
			var nextPosition = [];

			if (NextFilterValueDivision == "") {
				modelData.nextDepartment = modelData.department;
				modelData.nextPosition = modelData.position;
				oModel.refresh(true);
				this.getView().setModel(oModel);
				return;
			}
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
			nextDepartment.unshift(DEFAULT_DEPARTMENT);
			nextPosition.unshift(DEFAULT_POSITION);
			modelData.nextDepartment = nextDepartment;
			modelData.nextPosition = nextPosition;
			oModel.refresh(true);
			this.getView().setModel(oModel);

		},
		onSlNextDepartmentChange: function (oEvent) {
			var oModel = this.getView().getModel();
			var modelData = oModel.getData();
			var NextFilterValueDepartment = this._getSelectedItemText(this._getSelect("slNextDepartment"));
			var nextPosition = [];

			if (NextFilterValueDepartment == "") {
				modelData.nextPosition = modelData.position;
				this.getView().setModel(oModel);
				return;
			}
			nextPosition = modelData.nextPosition.filter(function (item, index) {
				if (item.code !== "" && item.hasOwnProperty("department")) {
					if (NextFilterValueDepartment === item.department) {
						return true;
					}
				}
			});
			nextPosition.unshift(DEFAULT_POSITION);
			modelData.nextPosition = nextPosition;
			oModel.refresh(true);
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
        		this._getCountAvailableCandidate();
        		this.getView().getModel().refresh(true);
            }
	           this._clearInfo(oDraggedRowContext);
               this._getCountAvailablePosition(); 
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
	        this._getCountAvailableCandidate();
			this._getCountAvailablePosition();
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
                MessageBox.alert(this._getMsg("transferAlertMsg"));
                return;
            }
            var oDroppedRow = oEvent.getParameter("droppedControl");
            if (oDroppedRow && oDroppedRow instanceof TableRow) {
                var oDroppedRowContext = oDroppedRow.getBindingContext();
                var iDroppedName = oModel.getProperty("candidateName",oDroppedRowContext);
                if(iDroppedName){
                	MessageBox.alert(this._getMsg("transferAlertMsg"));
                    return;
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
	                this._getCountAvailablePosition();
	        		this._getCountAvailableCandidate();
                }
            }
            oModel.refresh(true);
        },
        onTransferDestination: function(){
            this.byId("right").setSize("55%");
            this.byId("left").setSize("45%");
        },
        onHideRight:function(){
            this.byId("right").setSize("0%");
            this.byId("left").setSize("100%");
        },
        onLeftReset: function(){
			var oModel = this.getView().getModel();
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
			var oModel = this.getView().getModel();
			var modelData = oModel.getData();
        	this.getView().byId("slNextCompany").setSelectedKey("5000");
        	this.getView().byId("slNextBusinessunit").setSelectedKey("");
        	this.getView().byId("slNextDivision").setSelectedKey("");
        	this.getView().byId("slNextDepartment").setSelectedKey("");
        	this.getView().byId("slNextPosition").setSelectedKey("");
        	this.getView().byId("slRetirePlan").setSelectedKey("");
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
        		MessageBox.alert("保存するデータがありません");
        		return;
        	}
        	var count = 0;
        	for(var i=0; i<aNextpos.length; i++) {
        		if(aNextpos[i].candidateName === null || aNextpos[i].candidateName === ""){
        			count= count+1;
        		}
        	}
        	if(count>0){
        		this.checkCandidate(aNextpos);
        	}else {
        		this._save(aNextpos);
        	}
        },
        _sendData: function(nextpostions){
        	var oView = this.getView();
        	var that = this;
        	var positionListTable = oView.byId("positionListTable");
        	var jsondata = JSON.parse(JSON.stringify(nextpostions));
        	var postdata  = [];
        	//delete data that is not sent
        	postdata = jsondata.filter(function (item, index) {
					item.currentEmpRetire = item.candidateEmpRetire;
					delete item.photo;
					delete item.candidateEmpRetire;
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
						 MessageToast.show(that._getMsg("success"));
					},
					error: function (xhr, status, err) {
						 positionListTable.setBusy(false);
						MessageBox.error(that._getMsg("error"));
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
                title: this._getMsg("confirm"),
                type: 'Message',
                content: new Text({ 
                    text:this._getMsg("availablePost")
                }),
                beginButton: new Button({
                    type: ButtonType.Emphasized,
                    text: this._getMsg("continue"),
                    press: function () {
						nextpositions = modelData.nextpositions.filter(function (item, index) {
							if(item.candidateID && item.candidateName){
								return true;
							}
						});
						oModel.refresh(true);
                    	that._save(nextpositions);
                        oDialog.close();
                    }
                }),
                endButton: new Button({
                    text: this._getMsg("cancel"),
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
        _save: function(nextpositions){
        	var ButtonType = Library.ButtonType;
        	var that = this;
        	var oDialog = new Dialog({
                title: this._getMsg("confirm"),
                type: 'Message',
                content: new Text({ 
                    text: this._getMsg("confirmMsg")
                }),
                beginButton: new Button({
                    type: ButtonType.Emphasized,
                    text: this._getMsg("continue"),
                    press: function () {
				        that._sendData(nextpositions);
                        oDialog.close();
                    }
                }),
                endButton: new Button({
                    text: this._getMsg("cancel"),
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
	                    MessageBox.error(that._getMsg("exportError"));
	                 }
                    }.bind(this);
                  oXHR.send(JSON.stringify(jsondata));
                },
        onDataLeftExport: function(oEvent) {
        	var oView = this.getView();
        	var sFileName = "applicants";
        	var jsondata = oView.getModel().getData().candidates;
        	if(!jsondata) {
        		MessageBox.alert(this._getMsg("noDataToDownload"));
        		return;
        	}
        	this.onDataExport(jsondata, sFileName);
        },
        onDataRightExport: function(oEvent) {
        	var oView = this.getView();
        	var sFileName = "nextpositions";
        	var jsondata = oView.getModel().getData().nextpositions;
        	if(!jsondata) {
        		MessageBox.alert(this._getMsg("noDataToDownload"));
        		return;
        	}
        	this.onDataExport(jsondata, sFileName);
        },
        _getSelectedItemText: function(oSelect) {
            return oSelect.getSelectedItem() ? oSelect.getSelectedItem().getKey() : "";
        },
        _getSelect: function(sId) {
            return this.getView().byId(sId);
        },
        _getMsg: function(msg){
            var oResourceModel = this.getView().getModel("i18n");
            return oResourceModel.getResourceBundle("i18n").getText(msg);
        },
        onLeftGoBtn: function(){
        	var that = this;
            var oView = this.getView();
            var CurrenctFilterValueCompany = this._getSelectedItemText(this._getSelect("slCompany"));
            var CurrenctFilterValueBusinessUnit = this._getSelectedItemText(this._getSelect("slBusinessunit"));
            var CurrenctFilterValueDivision = this._getSelectedItemText(this._getSelect("slDivision"));
            var CurrenctFilterValueDepartment = this._getSelectedItemText(this._getSelect("slDepartment"));
            var CurrenctFilterValuePosition = this._getSelectedItemText(this._getSelect("slPosition"));
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
                MessageBox.alert(this._getMsg("mandatoryMsg"));
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
                        MessageBox.alert("not found");
                        oModelData.candidates = {};
                        oModelData.applicantsCount = 0;
                        oModelData.noTransferflagCount = 0;
                        oModel.setData(oModelData);
                        oModel.refresh(true);
                    }
                    var checkedcandidateId = [];
                    var candidatejsondata = oView.getModel().getData().candidates;
                    //to check if there is candidate who is already transfered and if yes, get their candidateIDs
                    if(nextposjsondata) {
	                    for(var k = 0; k < nextposjsondata.length; k++) {
	                    	if(nextposjsondata[k].candidateID) {
	                    		checkedcandidateId.push(nextposjsondata[k].candidateID);
	                    	}
	                    }
                    }
                    //if there is transfered candidates, set transfer flag
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
                    //control data on UI side in terms of transger flag
                    //2 = transfer flag is checked
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
                    //1 = transfer flag is not checked
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
                    MessageBox.error("error");
                }
            });
        },
    	onCheckCandidateId() {
        	var oView = this.getView();
            var oModel = oView.getModel();
            var candidateIdList = [];
            var that = this;
        	if(oModel.getData().nextpositions){
        		candidateIdList = this._getCandidateIdList();
	            if(candidateIdList.length !== 0) {
	            	var ButtonType = Library.ButtonType;
	            	var oDialog = new Dialog({
		                title: this._getMsg("confirm"),
		                type: 'Message',
		                content: new Text({ 
		                    text:this._getMsg("checkCancelCandidates")
		                }),
		                beginButton: new Button({
		                    type: ButtonType.Emphasized,
		                    text: this._getMsg("continue"),
		                    press: function () {
		                        oDialog.close();
		                        that._setEmptyTransferFlag(candidateIdList);
		                        that.onRightGoBtn(candidateIdList);
		                        that._resetStatus();
		                    }
		                }),
		                endButton: new Button({
		                    text: this._getMsg("cancel"),
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
            var NextFilterValueCompany = this._getSelectedItemText(this._getSelect("slNextCompany"));
            var NextFilterValueBusinessUnit = this._getSelectedItemText(this._getSelect("slNextBusinessunit"));
            var NextFilterValueDivision = this._getSelectedItemText(this._getSelect("slNextDivision"));
            var NextFilterValueDepartment = this._getSelectedItemText(this._getSelect("slNextDepartment"));
            var NextFilterValuePosition = this._getSelectedItemText(this._getSelect("slNextPosition"));
            var NextFilterValueRetirePlan = this.byId("slRetirePlan")._getSelectedItemText();
            var nextPositionParameter="";
            var retirementParameter="";
            var nextCompanyParameter = "";
            var nextBusinessUnitParameter = "";
            
            if (NextFilterValueDivision === "" || NextFilterValueDepartment === "" ) {
                MessageBox.alert(this._getMsg("mandatoryMsg"));
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
                        MessageBox.alert("not found");
                        oModelData.nextpositions = {};
                        oModel.setData(oModelData);
                        oModel.refresh(true);
                    }
                },
                error: function (xhr, status, err) {
                    positionListTable.setBusy(false);
                    MessageBox.error("error");
                }
            });
        },
        formatAvailableToIcon : function(bAvailable) {
			return bAvailable ? "sap-icon://accept" : null;
		},
		onCancelBtn: function(){
        	var oView = this.getView();
            var oModel = oView.getModel();
            var candidateIdList = [];
            var that = this;
        	if(oModel.getData().nextpositions){
        		candidateIdList = this._getCandidateIdList();
	            if(candidateIdList.length !== 0) {
	            	var ButtonType = Library.ButtonType;
	            	var oDialog = new Dialog({
		                title: this._getMsg("confirm"),
		                type: 'Message',
		                content: new Text({ 
		                    text:this._getMsg("checkCancelCandidates")
		                }),
		                beginButton: new Button({
		                    type: ButtonType.Emphasized,
		                    text: this._getMsg("continue"),
		                    press: function () {
		                        oDialog.close();
		                        that._cancelApplicants(candidateIdList);
		                    }
		                }),
		                endButton: new Button({
		                    text: this._getMsg("cancel"),
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
		_cancelApplicants: function(candidateIdList){
			this._setEmptyTransferFlag(candidateIdList);
			this._setEmptyCandidate();
			var originNextPositions = this.getView().getModel().getData().originNextpositions;
			var data = JSON.parse(JSON.stringify(originNextPositions));
			this.getView().getModel().getData().nextpositions = data;
			this._resetStatus();
		},
		_getCandidateIdList: function(){
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
		_setEmptyTransferFlag: function(candidateIdList){
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
		_setEmptyCandidate: function(){
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
		_resetStatus: function() {
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
		_getCountAvailablePosition: function() {
			var nextpositons = this.getView().getModel().getData().nextpositions;
			var count = this.getView().getModel().getData().originNextpositions.length;
			for(var i=0; i<nextpositons.length; i++) {
				if(nextpositons[i].candidateID === null || nextpositons[i].candidateID === "") {
					count--;
				}
			}
			this.getView().getModel().getData().availabilityCount = count;
			var nextpositionLength = this.getView().getModel().getData().originNextpositions.length;
			this.byId("rightProgressIndicator").setPercentValue(Math.floor(count/nextpositionLength*100));
			this.byId("rightProgressIndicator").setDisplayValue(Math.floor(count/nextpositionLength*100) + "%");
		},
		_getCountAvailableCandidate: function() {
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
			this.byId("leftProgressIndicator").setPercentValue(Math.floor(count/candidates.length*100));
			this.byId("leftProgressIndicator").setDisplayValue(Math.floor(count/candidates.length*100) + "%");
		}
    });
});
