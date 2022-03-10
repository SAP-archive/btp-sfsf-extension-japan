sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterOperator",
	"sap/ui/model/json/JSONModel",
	"sap/ui/model/Sorter",
	"./BaseController"
], function (Controller, Filter, FilterOperator, JSONModel, Sorter, BaseController) {
	"use strict";

	return BaseController.extend("com.sap.sfsf.comparison.Reshuffle_Comparison_UI.controller.Main", {

		onInit: function () {
			this.fragmentDirectory = "com.sap.sfsf.comparison.Reshuffle_Comparison_UI.view.fragment.";
			this.getView().byId("slCaseId").setFilterFunction(function (sTerm, oItem) {
				return oItem.getText().match(new RegExp(sTerm, "i")) || oItem.getKey().match(new RegExp(sTerm, "i"));
			});
		},

		onGroup: function () {
			this.showDialogFragment(this.getView().getId(), "GroupDialog");
		},
		onSort: function () {
			this.showDialogFragment(this.getView().getId(), "SortDialog");
		},
		onSetting: function () {
			this.showDialogFragment(this.getView().getId(), "SettingDialog");
		},

		handleSortDialogConfirm: function (oEvent) {
			var oView = this.getView();
			var oTable = oView.byId("CandidatesTable");
			var mParams = oEvent.getParameters();
			var oBinding = oTable.getBinding("items");
			var aSorters = [];
			var sPath = mParams.sortItem.getKey();
			var bDescending = mParams.sortDescending;
			aSorters.push(new sap.ui.model.Sorter(sPath, bDescending));
			oBinding.sort(aSorters);
		},

		handleGroupDialogConfirm: function (oEvent) {
			var oView = this.getView();
			var oTable = oView.byId("CandidatesTable");
			var mParams = oEvent.getParameters();
			var oBinding = oTable.getBinding("items"),
				sPath,
				bDescending,
				vGroup,
				aGroups = [];
			if (mParams.groupItem) {
				sPath = mParams.groupItem.getKey();
				bDescending = mParams.groupDescending;
				var vGroup = function (oContext) {
					var name = oContext.getProperty(sPath);
					return {
						key: name,
						text: name
					};
				};
				aGroups.push(new sap.ui.model.Sorter(sPath, bDescending, vGroup));
			}
			oBinding.sort(aGroups);
		},
		onCancelSettingDialog: function (oEvent) {
			this.oDataBeforeOpen = {};
			oEvent.getSource().close();
		},
		onOKSettingDialog: function (oEvent) {
			var oView = this.getView();
			var selectedItems = oEvent.mParameters.payload.columns.selectedItems
			var length = selectedItems.length;
			var tableItems = oEvent.mParameters.payload.columns.tableItems;
			var tablelength = tableItems.length;
			for (var item of tableItems) {
				var id = item.columnKey;
				this.getView().byId(id).setVisible(false);
			}
			for (var item of selectedItems) {
				var id = item.columnKey;
				this.getView().byId(id).setVisible(true);
			}
			oEvent.getSource().close();
		},

		onAfterRendering: function () {
			this._oCompareButton = this.getView().byId("compareBtn");
		},

		onToNextPage: function () {
			this.getOwnerComponent().getRouter().navTo("page2");
		},

		onSelection: function (oEvent) {
			var iSelectedItemsCount,
				bShowCompareButton;

			this.getOwnerComponent().aSelectedItems = oEvent.getSource().getSelectedContextPaths();
			iSelectedItemsCount = this.getOwnerComponent().aSelectedItems.length;
			bShowCompareButton = iSelectedItemsCount > 1;

			if (bShowCompareButton) {
				this.getResourceText("compareBtnName");
				this._oCompareButton.setText(this.getResourceText("compareBtnName") + " (" + this.getOwnerComponent().aSelectedItems.length + ")");
			}

			this._oCompareButton.setVisible(bShowCompareButton);
		},

		handleCaseIdSelectionFinish: function (oEvent) {
			var selectedItems = oEvent.getParameter("selectedItems");
			this.selectedCaseId = [];

			for (var i = 0; i < selectedItems.length; i++) {
				this.selectedCaseId.push(selectedItems[i].getText());

			}

		},

		onSearch: function (oEvent) {
			var oModel = this.getView().getModel();
			var oModelData = oModel.getData();
			var searchResult = oModelData.candidates;

			searchResult = this._searchCaseId(searchResult, this.selectedCaseId);
			searchResult = this._searchDivision(searchResult, this._getSelectedItemText(this._getSelect("slCandidateDivision")), "C");
			searchResult = this._searchDepartment(searchResult, this._getSelectedItemText(this._getSelect("slCandidateDepartment")), "C");
			searchResult = this._searchPosition(searchResult, this._getSelectedItemText(this._getSelect("slCandidatePosition")), "C");
			searchResult = this._searchDivision(searchResult, this._getSelectedItemText(this._getSelect("slDivision")), "N");
			searchResult = this._searchDepartment(searchResult, this._getSelectedItemText(this._getSelect("slDepartment")), "N");
			searchResult = this._searchPosition(searchResult, this._getSelectedItemText(this._getSelect("slPosition")), "N");
			searchResult = this._searchCandidateId(searchResult, oModel.getProperty("/candidateId"));
			searchResult = this._searchCandidateName(searchResult, oModel.getProperty("/candidateName"));

			oModelData.list = searchResult;
			oModel.setData(oModelData);
			oModel.refresh(true);
			this.getView().setModel(oModel);
		},
		onClear: function (oEvent) {
			var oModelData = this.getView().getModel().getData();
			oModelData.candidateId = "";
			oModelData.candidateName = "";

			this.getView().byId("slCaseId").removeAllSelectedItems();
			this._getSelect("slCandidateDivision").setSelectedKey("");
			this._getSelect("slCandidateDepartment").setSelectedKey("");
			this._getSelect("slCandidatePosition").setSelectedKey("");
			this._getSelect("slDivision").setSelectedKey("");
			this._getSelect("slDepartment").setSelectedKey("");
			this._getSelect("slPosition").setSelectedKey("");

			this.getView().getModel().setData(oModelData);
		},
		_searchDivision: function (searchResult, key, field) {
			if (key === "") {
				return searchResult;
			} else {
				return searchResult.filter(function (item, index) {
					if (field === "C") {
						if (item.candidateDivision === key) {
							return true;
						} else {
							return false;
						}
					} else if (field === "N") {
						if (item.division === key) {
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
						if (item.candidateDepartment === key) {
							return true;
						} else {
							return false;
						}
					} else if (field === "N") {
						if (item.department === key) {
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
						if (item.candidatePosition === key) {
							return true;
						} else {
							return false;
						}
					} else if (field === "N") {
						if (item.position === key) {
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

		_searchCaseId: function (searchResult, selectedCaseIds) {
			if (selectedCaseIds && selectedCaseIds.length > 0) {
				return searchResult.filter(function (item, index) {
					if (selectedCaseIds.includes(item.caseID)) {
						return true;
					} else {
						return false;
					}
				});
			} else {
				return searchResult;
			}
		}

	});
});