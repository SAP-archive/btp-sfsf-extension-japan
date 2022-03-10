sap.ui.define([
	'sap/ui/core/mvc/Controller',
	"sap/ui/core/Fragment",
	'sap/m/MessageToast',
	'sap/ui/table/Row',
	"sap/m/Input",
	"sap/m/Dialog",
	"sap/m/Label",
	"sap/m/Button",
	"sap/m/Text",
	"sap/ui/model/Filter",
	"sap/ui/model/FilterOperator",
	"sap/m/MessageBox",
	'../model/EditCaseTableModel',
	'../utils/EventBusHelper'
], 
/**
 * 
 * @param {typeof sap.ui.core.mvc.Controller} Controller 
 * @param {typeof sap.ui.core.Fragment} Fragment
 * @param {typeof sap.m.MessageToast} MessageToast
 * @param {typeof sap.ui.table.Row} TableRow
 * @param {typeof sap.m.Input} Input
 * @param {typeof sap.m.Dialog} Dialog
 * @param {typeof sap.m.Label} Label
 * @param {typeof sap.m.Button} Button
 * @param {typeof sap.m.Text} Text
 * @param {typeof sap.ui.model.Filter} Filter
 * @param {typeof sap.ui.model.FilterOperator} FilterOperator
 * @param {sap.m.MessageBox} MessageBox
 * @param {typeof com.sap.sfsf.reshuffle.applicants.model.EditCaseTableModel} EditCaseTableModel
 * @param {com.sap.sfsf.reshuffle.applicants.EventBusHelper} EventBusHelper
 */
function (Controller, Fragment, MessageToast, TableRow, Input, Dialog, Label, Button, Text, Filter, FilterOperator, MessageBox, EditCaseTableModel, EventBusHelper) {
	"use strict";
	return Controller.extend("com.sap.sfsf.reshuffle.applicants.controller.EditCaseTable", {
		/** @type {com.sap.sfsf.reshuffle.applicants.model.EditCaseTableModel} */
		editCaseTableModel: null,

		/** @type {sap.m.Dialog} */
		_competencyListDialog: null,
		onInit() {

			var serviceUrl = this.getOwnerComponent().getManifestEntry("sap.app").dataSources.reshuffleBackendDataSource.uri
			var serviceApiUrl = this.getOwnerComponent().getManifestEntry("sap.app").dataSources.reshuffleBackendDataSourceApi.uri;

			// モデルをセットする
			this.editCaseTableModel = new EditCaseTableModel(serviceUrl, serviceApiUrl, this.showErrorMessageToast);

			this.getView().setModel(this.editCaseTableModel);
			this.resetView();
		},
		resetView() {
			var deleteButton = /** @type {sap.m.Button} */ (this.byId("EditCaseTableToolBarDeleteCaseButton"));
			deleteButton.setEnabled(false);
		},
		/**
		 * Filterの、Selectorで値が選ばれると呼ばれる.
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onSelectFilterSelector(oEvent) {
			/** @type {sap.m.Select} */
			// @ts-ignore
			var selector = oEvent.getSource();
			var selectedItemId = selector.getSelectedItem().getKey();
			if(selector.getName() == "caseids") {

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

			var selector = /** @type {sap.m.Select} */ (filterBar.determineControlByName("EditCaseTableFilterCaseIdFilterName"));
			var caseId = selector.getSelectedItem().getKey();
						var table = /** @type {sap.ui.table.Table} */ (this.byId("EditCaseTable"));

			this.editCaseTableModel.getTableData(caseId, ()=> {
				table.setBusy(true)
			}, ()=> {
				table.setBusy(false);
				var deleteButton = /** @type {sap.m.Button} */ (this.byId("EditCaseTableToolBarDeleteCaseButton"));
				deleteButton.setEnabled(true);
			})
		},
		/**
		 * FilterBarで検索を開始する前のチェック
		 * @param {sap.ui.comp.filterbar.FilterBar} filterBar 
		 * @returns {string} エラーメッセージ。エラーが無い時はnull
		 */
		_validateFilter(filterBar) {
			
			// TODO!!
			var caseIdSelector = /** @type {sap.m.Select} */ (filterBar.determineControlByName("EditCaseTableFilterCaseIdFilterName"));

			return null;
		},
		/**
		 * 非表示(キャンセル）ボタンのイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onPresssCancelEditCase: function(oEvent) {
			var _this = this;
			// TODO: i18n
			MessageBox.confirm("保存していない変更は削除されます", {
				onClose: function(action) {
					if(action == "OK") {
						EventBusHelper.publishCloseEditCaseTableEvent(_this);
					}
				}
			})			
		},
		/**
		 * 削除ボタンのイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onPresssDelteCase: function(oEvent) {
			var _this = this;
			MessageBox.confirm("表示中のケースを削除しますか？", {
				onClose: function(action) {
					if(action == "OK") {
						var table = /** @type {sap.ui.table.Table} */ (_this.byId("EditCaseTable"));
						table.setBusy(true);
						_this.editCaseTableModel.deleteCurrentCase(()=> {
							table.setBusy(false);
							_this.editCaseTableModel.clearAndReloadFilter();
							_this.resetView();
						}, ()=> {
							_this.showErrorMessageToast("error");
							table.setBusy(false);
						})
					}
				}
			}) // TODO: i18n
		},
		/**
		 * 保存ボタンのイベント
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onPressSave: function(oEvent) {
			var _this = this;
			var hasEmptyPost = this.editCaseTableModel.hasEmptyPost();

			// TODO: i18n
			var message = "保存しますか？";
			if(hasEmptyPost) {
				message = "空きポストは保存されません。このまま保存しますか？"
			}
			MessageBox.confirm(message, {
				onClose(action) {
					if(action == "OK") {
						_this._save();
					}
				}
			});
		},
		_save() {
			sap.ui.core.BusyIndicator.show(100);
			var model = /** @type {sap.ui.model.odata.v4.ODataModel} */ (this.getOwnerComponent().getModel("reshuffleBackendService"));
			this.editCaseTableModel.saveCase(model, ()=>{
					// success 保存
					//TODO: use i18n
					this.showErrorMessageToast("success");
					sap.ui.core.BusyIndicator.hide();
				}, ()=>{
					//TODO: use i18n
					this.showErrorMessageToast("some error happened");
					sap.ui.core.BusyIndicator.hide();
				});
		},
		onPressExport: function(oEvent) {
			sap.ui.core.BusyIndicator.show();
			this.editCaseTableModel.exportTableData((password)=> {
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
				this.showErrorMessageToast("Export Error") // TODO: i18n
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
			var onSuccessDrop = /** @type {(empID: String)=>void} */ (oDragSession.getComplexData("onSuccessDrop"));
			var draggedRowData = oDraggedRow.getBindingContext().getObject();
			var iDroppedRowIndex = oDroppedRow.getIndex();
			var assignedCandidateID = this.editCaseTableModel.getAssignedCandidateID(iDroppedRowIndex);
			var result =	this.editCaseTableModel.updateRow(iDroppedRowIndex, draggedRowData["empID"], draggedRowData["empName"])
			if(result) {
				onSuccessDrop(assignedCandidateID);
				return;
			}
			MessageBox.alert("異動が重複しています"); // TODO: i18n
		},
		/**
		 * 
		 * @param {sap.ui.base.Event} oEvent 
		 */
		onDragStart(oEvent) {
			var oDragSession = /** @type { sap.ui.core.dnd.DragSession } */ (oEvent.getParameter("dragSession"));
			var targetRow = /** @type {sap.ui.table.Row} */ (oEvent.getParameter("target"));
			var targetRowIndex = targetRow.getIndex();
			var candidateEmpID = targetRow.getBindingContext().getProperty("candidateID");

			var _this = this;

			// ドロップが成功した際に呼ばれることを期待するコールバック
			function onSuccessDrop() {
				_this.editCaseTableModel.tableRowDragged(targetRowIndex);
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
			this.editCaseTableModel.getCompetencies(selectedRowIndex, ()=>{
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
			var isSucess = this.editCaseTableModel.competencySelected(obj);
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