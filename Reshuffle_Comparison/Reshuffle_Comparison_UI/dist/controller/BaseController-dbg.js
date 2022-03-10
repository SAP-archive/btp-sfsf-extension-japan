sap.ui.define([
    "sap/ui/core/Fragment",
	"sap/ui/core/mvc/Controller",
	"sap/ui/core/UIComponent"
], function (Fragment, Controller, UIComponent, mobileLibrary) {
    "use strict";
    
	return Controller.extend("com.sap.sfsf.comparison.Reshuffle_Comparison_UI.controller.BaseController", {
        getFormFragment: function (sid, sFragmentName) {

			return Fragment.load({
                name: this.fragmentDirectory + sFragmentName,
                id : sid,
				controller: this
            })
        },
        getResourceText: function (sKey) {
            return this.getView().getModel("i18n").getResourceBundle().getText(sKey);
        },

        showDialogFragment: function (sid, sDialogFragmentName) {
            var sPage = "filter";
            var fInit = null;
            if (!this._oDialogs) {
                this._oDialogs = {};
            }

            if (!this._oDialogs[sDialogFragmentName]) {
                this.getFormFragment(this.getView().getId(), sDialogFragmentName)
                .then(function (oDialog) {
                    this._oDialogs[sDialogFragmentName] = oDialog;

                    var i18Model = this.getOwnerComponent().getModel("i18n");
                    oDialog.setModel(i18Model, "i18n");

                    this.getView().addDependent(this._oDialogs[sDialogFragmentName]);
                    if (fInit) {
                        fInit(this._oDialogs[sDialogFragmentName]);
                    }

                    this.getView().addDependent(this._oValueHelpDialog);

                    // opens the dialog
                    this._oDialogs[sDialogFragmentName].open(sPage);
                }.bind(this));
            } else {
                this._oDialogs[sDialogFragmentName].open(sPage);
            }
        },
        _getSelect: function (sId) {
			return this.getView().byId(sId);
		}
	});

});