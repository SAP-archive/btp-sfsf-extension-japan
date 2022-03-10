sap.ui.define([
	'sap/ui/core/mvc/Controller',
	'sap/m/MessageToast',
	'sap/m/Dialog',
	'sap/m/Text',
	'sap/m/Button',
	"sap/ui/core/Fragment",
	'../model/CurrentPositionEmployeeTableModel',
	'../utils/EventBusHelper',
	'../utils/formatter'
],
/**
 * 
 * @param {typeof sap.ui.core.mvc.Controller} Controller 
 * @param {typeof sap.m.MessageToast} MessageToast
 * @param {typeof sap.m.Dialog} Dialog
 * @param {typeof sap.m.Text} Text
 * @param {typeof sap.m.Button} Button
 * @param {typeof sap.ui.core.Fragment} Fragment
 * @param {typeof com.sap.sfsf.reshuffle.applicants.model.CurrentPositionemployeeTableModel} CurrentPositionEmployeeTableModel
 * @param {com.sap.sfsf.reshuffle.applicants.EventBusHelper} EventBusHelper
 * @param {*} formatter
 */
function(Controller, MessageToast, Dialog, Text, Button, Fragment, CurrentPositionEmployeeTableModel, EventBusHelper, formatter) {
	"use strict";

	return Controller.extend("com.sap.sfsf.reshuffle.applicants.controller.CurrentPositionemployeeTable", {
		formatter: formatter,
		
		/** @type {com.sap.sfsf.reshuffle.applicants.model.CurrentPositionemployeeTableModel} */
	 	currentPositionEmployeeTableModel: null,

		/** @type {sap.m.ResponsivePopover} */
		employeeInfoPopover: null,
		
		onInit() {
			var _this = this;

			// 顔写真の読み込み時にFragment.loadが固まるので事前に読み込んでおく
			Fragment.load({
				name: "com.sap.sfsf.reshuffle.applicants.fragment.EmployeeInfoPopover",
				controller: _this
			}).then((/** @type {sap.m.ResponsivePopover} */ popover)=> {
				_this.employeeInfoPopover = popover;
				_this.getView().addDependent(_this.employeeInfoPopover);
			})

			var serviceUrl = this.getOwnerComponent().getManifestEntry("sap.app").dataSources.reshuffleBackendDataSource.uri
			var serviceApiUrl = this.getOwnerComponent().getManifestEntry("sap.app").dataSources.reshuffleBackendDataSourceApi.uri;

			// モデルをセットする
			this.currentPositionEmployeeTableModel = new CurrentPositionEmployeeTableModel(serviceUrl, serviceApiUrl, this.showErrorMessageToast);
			this.getView().setModel(this.currentPositionEmployeeTableModel);
		},
		/**
		 * Filterの、Selectorで値が選ばれると呼ばれる.
		 * 選ばれた値に応じてフィルターの値を更新する
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onSelectFilterSelector(oEvent) {
			/** @type {sap.m.Select} */
			// @ts-ignore
			var selector = oEvent.getSource();
			var selectedItemId = selector.getSelectedItem().getKey();

			switch(selector.getName()) {
				case "CompanySelector":
					break;
				case "BusinessUnitSelector":
					this.currentPositionEmployeeTableModel.setDivisionFilterByBusinessUnit(selectedItemId);
					break;
				case "DivisionSelector":
					this.currentPositionEmployeeTableModel.setDepartmentFilterByDivision(selectedItemId);
					break;
				case "DepartmentSelector":
					this.currentPositionEmployeeTableModel.setPositionFilterByDepartment(selectedItemId);
					break;
				case "PositionSelector":
					break;
				default:
					this.showErrorMessageToast("Some error happened");
			}
		},
		/**
		 * filterBarから呼ばれるメソッド
		 * sap.ui.model.Filterを使うと、"businessUnit"などのentityに無い物がはじかれてしまうので手動で
		 * $filterパラメータを組み立てる
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onSearch(oEvent) {
			/** @type {sap.ui.comp.filterbar.FilterBar} */
			// @ts-ignore
			var filterBar = oEvent.getSource(); 

			var errMsg = this._validateFilter(filterBar);
			if(errMsg != null) {
				this.showErrorMessageToast(errMsg);
				return;
			}

			var filterGroupItems = filterBar.getFilterGroupItems();
			
			var filters = /** @type {com.sap.sfsf.reshuffle.applicants.model.CurrentPositionEmployeeTableFilterValues} */ ({});
			console.log("Filter!");
      filterGroupItems.forEach((filterGroupItem) => {
				var control = filterGroupItem.getControl();
				switch(filterGroupItem.getName()) {
					case "CurrentPiositionEmployeeTableFilterCompanyFilterName":
						// @ts-ignore
						var selected = control.getSelectedItem();
						filters.company = selected == null ? "" : selected.getKey();
						break;
					case "CurrentPiositionEmployeeTableFilterGroupBusinessUnitFilterName":
						// @ts-ignore
						var selected = control.getSelectedItem();
						filters.businessUnit = selected == null ? "" : selected.getKey();
						break;
					case "CurrentPiositionEmployeeTableFilterGroupDivisionFilterName":
						// @ts-ignore
						var selected = control.getSelectedItem();
						filters.division = selected == null ? "" : selected.getKey();
						break;
					case "CurrentPiositionEmployeeTableFilterGroupDepartmentFilterName":
						// @ts-ignore
						var selected = control.getSelectedItem();
						filters.department = selected == null ? "" : selected.getKey();
						break;
					case "CurrentPiositionEmployeeTableFilterGroupPositionFilterName":
						// @ts-ignore
						var selected = control.getSelectedItem();
						filters.position = selected == null ? "" : selected.getKey();
						break;
					case "CurrentPiositionEmployeeTableFilterGroupTenureULFilterName": // upper limit
						// @ts-ignore
						var val = control.getValue();
						if(val == ""  || val == null) {
							filters.tenureUL = null;
						} else {
							filters.tenureUL = Number(val); // TODO: Intのみを受け付ける様な処理。
						}
						break;
					case "CurrentPiositionEmployeeTableFilterGroupTenureLLFilterName": // lower limit
						// @ts-ignore
						var val = control.getValue();
						if(val == "" || val == null) {
							filters.tenureLL = null;
						} else {
							filters.tenureLL = Number(val);
						}
						break;
					case "CurrentPiositionEmployeeTableFilterGroupWillingnessFilterName":
						// @ts-ignore
						var selected = control.getSelectedItem();
						filters.willingness = selected == null ? "" : selected.getKey();
						break;
				}
      });
			var table = /** @type {sap.ui.table.Table} */ (this.byId("CurrentPositionEmployeeTable"));
			this.currentPositionEmployeeTableModel.getTableData(filters, ()=> {table.setBusy(true)}, ()=> {table.setBusy(false)});
		},
		/**
		 * FilterBarで検索を開始する前のチェック
		 * @param {sap.ui.comp.filterbar.FilterBar} filterBar 
		 * @returns {string} エラーメッセージ。エラーが無い時はnull
		 */
		_validateFilter(filterBar) {
			
			var divisionSelector = /** @type {sap.m.Select} */ (filterBar.determineControlByName("CurrentPiositionEmployeeTableFilterGroupDivisionFilterName"));
			var departmentSelector = /** @type {sap.m.Select} */ (filterBar.determineControlByName("CurrentPiositionEmployeeTableFilterGroupDepartmentFilterName"));

			if(divisionSelector.getSelectedItem().getKey() === "" || departmentSelector.getSelectedItem().getKey() === "") {
				return this.getMsg("filterMandatoryMessage");
			}
			return null;
		},
		/**
		 * 異動先ポジション表示ボタンのイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onPresssOpenNextPosition: function(oEvent) {
			EventBusHelper.publishOpenNextPositionTableEvent(this);
		},
		/**
		 * 編集テーブル表示ボタンのイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onPressOpenEditCase: function(oEvent) {
			EventBusHelper.publishOpenEditCaseTableEvent(this);
		},
		onPressExport: function(oEvent) {
			sap.ui.core.BusyIndicator.show();
			this.currentPositionEmployeeTableModel.exportTableData((password)=> {
				sap.ui.core.BusyIndicator.hide();
				var oDefaultMessageDialog = new Dialog({
					type: sap.m.DialogType.Message,
					title: "Password", // TODO: i18n
					content: new Text({ text: password }),
					beginButton: new Button({
						text: "OK",
						press: function () {
							oDefaultMessageDialog.close();
						}
					})
				});
				oDefaultMessageDialog.open();
			}, ()=> {
				sap.ui.core.BusyIndicator.hide();
				this.showErrorMessageToast("Export Error"); // TODO: i18n
			});
		},
		/**
		 * ドラッグ開始時に呼ばれるイベント 
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onDragStart(oEvent) {
			var oDragSession = /** @type { sap.ui.core.dnd.DragSession } */ (oEvent.getParameter("dragSession"));
			var targetRow = /** @type {sap.ui.table.Row} */ (oEvent.getParameter("target"));
			var targetRowIndex = targetRow.getIndex();

			var _this = this;

			/**
			 * ドロップが成功した際に呼ばれることを期待するコールバック
			 * @param {String} exsistedEmpId ドロップされたrowに割り当てられていたempID. 人の割り当てが入れ替えられた時にnull以外が渡される
			 */
			function onSuccessDrop(exsistedEmpId) {
				if(exsistedEmpId != null) {
					_this.currentPositionEmployeeTableModel.updateTransferedFlagStatusByEmpID(exsistedEmpId, false);
				}
				_this.currentPositionEmployeeTableModel.tableRowDragged(targetRowIndex);
			}
			oDragSession.setComplexData("onSuccessDrop", onSuccessDrop);
		},
		/**
		 * ドラッグアンドドロップでドロップされた時に呼ばれるイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onDropTable(oEvent) {
			var oDragSession = /** @type {sap.ui.core.dnd.DragSession} */(oEvent.getParameter("dragSession"));
			var oDraggedRow = /** @type {sap.ui.table.Row} */ (oEvent.getParameter("draggedControl"));
			var oDroppedRow = /** @type {sap.ui.table.Row} */ (oEvent.getParameter("droppedControl"));

			// dropが成功した時にドラッグ元から渡されるコールバック
			var onSuccessDrop = /** @type {Function} */ (oDragSession.getComplexData("onSuccessDrop"));
			var empID = oDragSession.getData("empID");

			this.currentPositionEmployeeTableModel.tableRowDropped(empID);
			onSuccessDrop();
		},
		/**
		 * テーブルのcellのクリックイベント
		 * 顔写真のクリックイベントを取得したい。Imageのpressを利用すると、画像が読み込まれていないタイミングで
		 * 押しても反応しないのでcellのクリックイベントを利用
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onCellClick(oEvent) {
			var columnIndex = oEvent.getParameter("columnIndex");

			// 0列目==顔写真のカラム
			if(columnIndex == 0) {
				var path = oEvent.getParameter("rowBindingContext").getPath();
				if(this.employeeInfoPopover) {
					this.employeeInfoPopover.bindElement(path);
					this.employeeInfoPopover.openBy(oEvent.getSource())
				}
			}
		},
		/**
		 * 従業員情報のポップオーバー内のCloseボタンが押されたときのイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onClickClosePopover(oEvent) {
			this.employeeInfoPopover.close();
		},
		/**
		 * Toastでエラーメッセージを表示するためのメソッド.
		 * filterFragmentで利用する
		 * @param {string} text 
		 */
		showErrorMessageToast(text) {
			MessageToast.show(text);
		},
		/**
		 * i18n経由で文字列取得
		 * @param {string} msg 
		 * @returns {string}
		 */
		getMsg(msg){
      var oResourceModel = this.getView().getModel("i18n");
      // @ts-ignore
      return oResourceModel.getResourceBundle("i18n").getText(msg);
		},
	});
});