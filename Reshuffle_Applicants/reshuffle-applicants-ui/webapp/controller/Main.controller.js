sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/core/mvc/View",
    "sap/ui/core/mvc/ViewType",
    "sap/m/MessageBox",
    "../model/MainModel",
	'../utils/EventBusHelper'
],
    /**
     * @param {typeof sap.ui.core.mvc.Controller} Controller
     * @param {typeof sap.ui.core.mvc.View} View
     * @param {typeof sap.ui.core.mvc.ViewType} ViewType
     * @param {sap.m.MessageBox} MessageBox 
     * @param {typeof Object} MainModel
     * @param {com.sap.sfsf.reshuffle.applicants.EventBusHelper} EventBusHelper
     */
    function (Controller, View, ViewType, MessageBox, MainModel, EventBusHelper) {
        "use strict";
        return Controller.extend("com.sap.sfsf.reshuffle.applicants.controller.Main", {
            isRightPaneOpen: false,

            /** @type {Object} */
            mainModel: null,

            onInit() {
                EventBusHelper.subscribeOpenNextPositionTableEvent(this, this._openNextPostionTable);
                EventBusHelper.subscribeCloseNextPositionTableEvent(this, this._closeRightTable);
                
                EventBusHelper.subscribeOpenEditCaseTableEvent(this, this._openEditCasePostionTable);
                EventBusHelper.subscribeCloseEditCaseTableEvent(this, this._closeRightTable);

                var configurationBackendUrl = this.getOwnerComponent().getManifestEntry("sap.app").dataSources.reshuffleConfigurationDataSource.uri;
                this.mainModel = new MainModel(configurationBackendUrl);
                this.getView().setModel(this.mainModel);
            },
            /**
             * 右側のpaneを閉じても良いかチェック
             * @param {()=>void} success 
             */
            _confirmCloseRightPane: function(success) {
                var _this = this;
                if(this.isRightPaneOpen) {
                    // TODO: i18n
			        MessageBox.confirm("保存していない変更は削除されます", {
                        onClose: function(action) {
			        	    if(action == "OK") {
			        			success();
			        		}
			        	}
			        });
                } else {
                    success();
                }
            },
            _openNextPostionTable: function() {
                var _this = this;
                this._confirmCloseRightPane(()=> {
                    this.getOwnerComponent().runAsOwner(()=>{
                        sap.ui.core.BusyIndicator.show();

                        var vbox = /** @type {sap.m.VBox} */ (_this.byId("mainVboxRigh"));
                        vbox.destroyItems();

                        View.create({
                            id: "mainRightView",
                            viewName: "com.sap.sfsf.reshuffle.applicants.view.NextPositionTable",
                            type: ViewType.XML
                        }).then(function(view){
                            vbox.insertItem(view, 0);
                            var splitterLayoutDataLeft = /** @type {sap.ui.layout.SplitterLayoutData} */(_this.byId("mainSplitterLayoutDataLeft"));
                            var splitterLayoutDatRight = /** @type {sap.ui.layout.SplitterLayoutData} */(_this.byId("mainSplitterLayoutDataRight"));
                            splitterLayoutDataLeft.setSize("45%")
                            splitterLayoutDatRight.setSize("55%") 
                            sap.ui.core.BusyIndicator.hide();
                            _this.isRightPaneOpen = true;
                        })
                    });   
                });
            },
            _openEditCasePostionTable: function() {
                var _this = this;
                this._confirmCloseRightPane(()=> {
                    this.getOwnerComponent().runAsOwner(()=>{
                        sap.ui.core.BusyIndicator.show();

                        var vbox = /** @type {sap.m.VBox} */ (_this.byId("mainVboxRigh"));
                        vbox.destroyItems();

                        View.create({
                            id: "mainRightView",
                            viewName: "com.sap.sfsf.reshuffle.applicants.view.EditCaseTable",
                            type: ViewType.XML
                        }).then(function(view){
                            vbox.insertItem(view, 0);

                            var splitterLayoutDataLeft = /** @type {sap.ui.layout.SplitterLayoutData} */(_this.byId("mainSplitterLayoutDataLeft"));
                            var splitterLayoutDatRight = /** @type {sap.ui.layout.SplitterLayoutData} */(_this.byId("mainSplitterLayoutDataRight"));
                            splitterLayoutDataLeft.setSize("45%")
                            splitterLayoutDatRight.setSize("55%") 
                            sap.ui.core.BusyIndicator.hide();
                            _this.isRightPaneOpen = true;
                        })
                    });   
                })
            },
            _closeRightTable() {
                var splitterLayoutDataLeft = /** @type {sap.ui.layout.SplitterLayoutData} */(this.byId("mainSplitterLayoutDataLeft"));
                var splitterLayoutDatRight = /** @type {sap.ui.layout.SplitterLayoutData} */(this.byId("mainSplitterLayoutDataRight"));
                splitterLayoutDataLeft.setSize("100%")
                splitterLayoutDatRight.setSize("0%")   
                var vbox = /** @type {sap.m.VBox} */ (this.byId("mainVboxRigh"));
                vbox.destroyItems();
                this.isRightPaneOpen = false;
            },
        });
    });
