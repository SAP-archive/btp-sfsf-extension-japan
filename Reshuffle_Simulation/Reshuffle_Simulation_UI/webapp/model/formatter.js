sap.ui.define(function() {
	"use strict";

	var Formatter = {

		checkState :  function (checkresult) {
            switch(checkresult){
                case "OK":
					return "Success";
                case "NG":
                    return "Error";
                case "WARN":
                    return "Warning";
                default:
					return "None";            
            }
		},

		checkProcessState :  function (checkresult) {
			if (checkresult === "OK") {
				return "Success";
			} else if (checkresult === "WARN") {
				return "Warning";
			} else if (checkresult === "NG") {
				return "Error";
			} else{
				return "";
			}
		},		
		getStatusName: function (statuscode) {
			if(statuscode){
				var oResourceBundle = this.getOwnerComponent().getModel("i18n").getResourceBundle();
				switch (statuscode) {
					case "OK":
						return oResourceBundle.getText("Success");
					case "NG":
						return oResourceBundle.getText("Error");
					case "WARN":
						return oResourceBundle.getText("Warning");
					case "APPL":
						return oResourceBundle.getText("Success");
					case "APPD":
						return oResourceBundle.getText("Success");
					case "DENY":
						return oResourceBundle.getText("Declined");
					default:
						return oResourceBundle.getText("UnChecked");
				}
			}
		},

		getSendMailFlagName: function(mailSentFlg){
			if(!mailSentFlg){
				var oResourceBundle = this.getOwnerComponent().getModel("i18n").getResourceBundle();
				return oResourceBundle.getText("sl_sendMail_unsent");
				// return "未送信";
			}else{
				return mailSentFlg;
			}
		},
		mailSentStatus: function(mailSentFlg){
			switch (mailSentFlg) {
				case "送信済":
					return "Success";
				case "予約済":
					return "Success";
				case "送信失敗":
					return "Error";
				case "予約失敗":
					return "Error";
				case "":
					return "Warning";
				default:
					return "None";
			}
		},
		getUpsertFlagName: function(upsertFlg){
			if(!upsertFlg){
				var oResourceBundle = this.getOwnerComponent().getModel("i18n").getResourceBundle();
				return oResourceBundle.getText("sl_upsert_unprocessed");
			}else{
				return upsertFlg;
			}
		},
		upsertStatus: function(upsertFlg){
			switch (upsertFlg) {
				case "更新済":
					return "Success";
				case "更新失敗":
					return "Error";
				case "":
					return "Warning";
				default:
					return "None";
			}
		},
        activateWFButton : function(status){
            switch(status){
                case "OK":
                    console.log("status is " + status);
                    return true;
                default: 
                    return false;
            }            
        },

        activatePdfButton : function (){
            return true;

        },
        activateSendMailButton : function(){
            return true;

        },
        activateSendSFSFButton : function(){
            return true;
            
        }
	};

	return Formatter;

}, /* bExport= */ true);
