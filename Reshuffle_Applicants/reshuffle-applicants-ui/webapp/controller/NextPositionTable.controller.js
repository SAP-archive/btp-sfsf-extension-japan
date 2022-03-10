sap.ui.define([
	'sap/ui/core/mvc/Controller',
	"sap/ui/core/Fragment",
	"sap/ui/model/json/JSONModel",
	'sap/m/MessageToast',
	'sap/ui/table/Row',
	"sap/m/Input",
	"sap/m/Dialog",
	"sap/m/Label",
	"sap/m/Button",
	"sap/m/MessageBox",
	"sap/m/Text",
	'../model/NextPositionTableModel',
	'../utils/EventBusHelper'
], 
/**
 * 
 * @param {typeof sap.ui.core.mvc.Controller} Controller 
 * @param {typeof sap.ui.core.Fragment} Fragment
 * @param {typeof sap.ui.model.json.JSONModel} JSONModel
 * @param {typeof sap.m.MessageToast} MessageToast
 * @param {typeof sap.ui.table.Row} TableRow
 * @param {typeof sap.m.Input} Input
 * @param {typeof sap.m.Dialog} Dialog
 * @param {typeof sap.m.Label} Label
 * @param {typeof sap.m.Button} Button
 * @param {sap.m.MessageBox} MessageBox
 * @param {typeof sap.m.Text} Text
 * @param {typeof com.sap.sfsf.reshuffle.applicants.model.NextPositionTableModel} NextPositionTableModel
 * @param {com.sap.sfsf.reshuffle.applicants.EventBusHelper} EventBusHelper
 */
function (Controller, Fragment, JSONModel, MessageToast, TableRow, Input, Dialog, Label, Button, MessageBox, Text, NextPositionTableModel, EventBusHelper) {
	"use strict";
	return Controller.extend("com.sap.sfsf.reshuffle.applicants.controller.NextPositionTable", {
		/** @type {com.sap.sfsf.reshuffle.applicants.model.NextPositionTableModel} */
		nextPositionTableModel: null,

		/** @type {sap.m.Dialog} */
		_competencyListDialog: null,

		onInit() {

			// @ts-ignore
			var serviceUrl = this.getOwnerComponent().getManifestEntry("sap.app").dataSources.reshuffleBackendDataSource.uri
			var serviceApiUrl = this.getOwnerComponent().getManifestEntry("sap.app").dataSources.reshuffleBackendDataSourceApi.uri;

			// モデルをセットする
			this.nextPositionTableModel = new NextPositionTableModel(serviceUrl, serviceApiUrl, this.showErrorMessageToast);
			this.getView().setModel(this.nextPositionTableModel);
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
					this.nextPositionTableModel.setDivisionFilterByBusinessUnit(selectedItemId);
					break;
				case "DivisionSelector":
					this.nextPositionTableModel.setDepartmentFilterByDivision(selectedItemId);
					break;
				case "DepartmentSelector":
					this.nextPositionTableModel.setPositionFilterByDepartment(selectedItemId);
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
			
			var filters = /** @type {com.sap.sfsf.reshuffle.applicants.model.NextPositionTableFilterValues} */ ({});
      filterGroupItems.forEach((filterGroupItem) => {
        /** @type {sap.m.Select} */
        // @ts-ignore
        var selector = filterBar.determineControlByFilterItem(filterGroupItem, false);
				var val = "";
				if(selector.getSelectedItem() != null) {
			  	val = selector.getSelectedItem().getKey();
				}
        switch(selector.getName()) {
          case "CompanySelector":
						filters.company = val;
            break;
          case "BusinessUnitSelector":
						filters.businessUnit = val;
            break;
          case "DivisionSelector":
						filters.division = val;
            break;
          case "DepartmentSelector":
						filters.department = val;
            break;
          case "PositionSelector":
						filters.position = val;
            break;
					case "RetireSelector":
						filters.retire = val;
						break;
          default:
            this.showErrorMessageToast("Some error happened");
        }
      });

			var table = /** @type {sap.ui.table.Table} */ (this.byId("NextPositionTable"));
			this.nextPositionTableModel.getTableData(filters, ()=> {table.setBusy(true)}, ()=> {table.setBusy(false)});
		},
		/**
		 * FilterBarで検索を開始する前のチェック
		 * @param {sap.ui.comp.filterbar.FilterBar} filterBar 
		 * @returns {string} エラーメッセージ。エラーが無い時はnull
		 */
		_validateFilter(filterBar) {
			
			var divisionSelector = /** @type {sap.m.Select} */ (filterBar.determineControlByName("NextPositionTableFilterDivisionFilterName"));
			var departmentSelector = /** @type {sap.m.Select} */ (filterBar.determineControlByName("NextPositionTableFilterDepartmentFilterName"));

			if(divisionSelector.getSelectedItem().getKey() === "" || departmentSelector.getSelectedItem().getKey() === "") {
				return this.getMsg("filterMandatoryMessage");
			}
			return null;
		},
		/**
		 * 異動先ポジション非表示（キャンセル）ボタンのイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onPresssCancelNextPosition: function(oEvent) {
			var _this = this;
			// TODO: i18n
			MessageBox.confirm("保存していない変更は削除されます", {
				onClose: function(action) {
					if(action == "OK") {
						EventBusHelper.publishCloseNextPositionTableEvent(_this);
					}
				}
			})			
		},
		/**
		 * 保存ボタンのイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onPressSave: function(oEvent) {
			var _this = this;
			var hasEmptyPost = this.nextPositionTableModel.hasEmptyPost();
			if(hasEmptyPost) {
				MessageBox.confirm("空きポストは保存されません。続けますか？", { // TODO: i18n
					onClose(action) {
						if(action == "OK") {
							_this._save();
						}
					}
				});
				return;
			}	
			this._save();
		},
		_save() {
			var _this = this;
			var caseIdInput = new Input("inputCaseId", {
				width: "100%",
				placeholder: this.getMsg("saveDialogInputCaseIdPlaceholder"),
				required: true,
				maxLength: 50
			});

			var dialog = new Dialog({
				title: this.getMsg("saveDialogInputCaseIdTitle"),
				type: sap.m.DialogType.Message,
				content: [
					new Label({
						text: this.getMsg("saveDialogInputCaseIdLabel"),
						labelFor: "inputCaseId"
					}),
					caseIdInput
				],
				beginButton: new Button({
					type: sap.m.ButtonType.Emphasized,
					text: this.getMsg("saveButton"),
					press: function() {
						var sText = caseIdInput.getValue();
            if(sText === "" || sText === null) {
							caseIdInput.setValueState(sap.ui.core.ValueState.Error)
            }else {
							var model = /** @type {sap.ui.model.odata.v4.ODataModel} */ (_this.getOwnerComponent().getModel("reshuffleBackendService"));

							sap.ui.core.BusyIndicator.show(100);
							_this.nextPositionTableModel.saveCandidates(model, sText, ()=>{
								// success 保存
								//TODO: use i18n
								_this.showErrorMessageToast("success");
								sap.ui.core.BusyIndicator.hide();
							}, (msg)=>{
								//TODO: use i18n
								MessageBox.error(msg);
								sap.ui.core.BusyIndicator.hide();
							});
							dialog.close();
           	}
					}
				}),
				endButton: new Button({
					text: this.getMsg("cancelButton"),
					press: function() {
						dialog.close();
					}
				}),
				afterClose: function() {
					dialog.destroy();
				}
			});
			dialog.open();
		},
		onPressExport: function(oEvent) {
			sap.ui.core.BusyIndicator.show();

			this.nextPositionTableModel.exportTableData((password)=> {
				sap.ui.core.BusyIndicator.hide();

				var oDefaultMessageDialog = new Dialog({
					type: sap.m.DialogType.Message,
					title: "Password", // TODO; i18n
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
		 * ドロップされた時に呼ばれるイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onDropTable(oEvent){
			var oDragSession = /** @type {sap.ui.core.dnd.DragSession} */(oEvent.getParameter("dragSession"));
			var oDraggedRow = /** @type {sap.ui.table.Row} */ (oEvent.getParameter("draggedControl"));
			var oDroppedRow = /** @type {sap.ui.table.Row} */ (oEvent.getParameter("droppedControl"));

			// dropが成功した時にドラッグ元から渡されるコールバック
			var onSuccessDrop = /** @type {(exsistedEmpId:String)=>void} */ (oDragSession.getComplexData("onSuccessDrop"));
			var draggedRowData = oDraggedRow.getBindingContext().getObject();
			var iDroppedRowIndex = oDroppedRow.getIndex();
			var assignedCandidateEmpID = this.nextPositionTableModel.getAssignedCandidateEmpID(iDroppedRowIndex);
			var result = this.nextPositionTableModel.tableRowDropped(iDroppedRowIndex, draggedRowData);
			if(result) {
				onSuccessDrop(assignedCandidateEmpID);
				return;
			}
			MessageBox.alert("異動が重複しています"); // TODO: i18n. 更新が失敗した理由が確実にこれである保証なし
		},
		/**
		 * 
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onDragStart(oEvent) {
			var oDragSession = /** @type { sap.ui.core.dnd.DragSession } */ (oEvent.getParameter("dragSession"));
			var targetRow = /** @type {sap.ui.table.Row} */ (oEvent.getParameter("target"));
			var targetRowIndex = targetRow.getIndex();
			var candidateEmpID = targetRow.getBindingContext().getProperty("candidateData/empID");

			var _this = this;

			// ドロップが成功した際に呼ばれることを期待するコールバック
			function onSuccessDrop() {
				_this.nextPositionTableModel.tableRowDragged(targetRowIndex);
			}
			oDragSession.setComplexData("onSuccessDrop", onSuccessDrop);
			oDragSession.setData("empID", candidateEmpID);
		},
		/**
		 * Positionカラムのテキストがクリックされた時のイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onPressPositionName(oEvent){

			var _this = this;
			var link = /** @type {sap.m.Link} */ (oEvent.getSource());
			var selectedRow = /** @type {sap.ui.table.Row} */ (link.getParent());
			var selectedRowIndex = selectedRow.getIndex();

			sap.ui.core.BusyIndicator.show(100);
			this.nextPositionTableModel.getCompetencies(selectedRowIndex, ()=>{
				if(_this._competencyListDialog != null) {
					_this._competencyListDialog.open();
					sap.ui.core.BusyIndicator.hide();
					return;
				}

				Fragment.load({
					name: "com.sap.sfsf.reshuffle.applicants.fragment.CompetencyListDialog",
					controller: _this
				}).then((dialog)=> {
					_this.getView().addDependent(dialog);
					dialog.open();
					_this._competencyListDialog = dialog;
					sap.ui.core.BusyIndicator.hide();
				})
			}, ()=> {
				sap.ui.core.BusyIndicator.hide();
			});
		},
		/**
		 * 
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onPressCompetencyListItem(oEvent) {
			var rowContext = /** @type {sap.ui.model.Context} */ (oEvent.getParameter("rowContext"));
			if(rowContext == null || rowContext == undefined) {
				return;
			}
			var obj = rowContext.getObject();
			var isSucess = this.nextPositionTableModel.competencySelected(obj);
			if(this._competencyListDialog != null) {
				this._competencyListDialog.close();
			}
			if(!isSucess) {
				MessageBox.alert("異動が重複しています"); // TODO: i18n. 更新が失敗した理由が確実にこれである保証なし
			}
		},
		/**
		 * 
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onPressCompetencyListCancelButton(oEvent) {
			if(this._competencyListDialog != null) {
				this._competencyListDialog.close();
			}
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