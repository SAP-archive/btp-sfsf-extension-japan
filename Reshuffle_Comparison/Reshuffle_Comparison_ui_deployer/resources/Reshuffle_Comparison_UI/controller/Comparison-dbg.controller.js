sap.ui.define([
	"sap/m/MessageBox",
	"sap/ui/core/mvc/Controller",
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/Fragment",
	"sap/ui/core/ResizeHandler",
	"sap/ui/core/routing/History",
	"sap/ui/core/syncStyleClass",
	"../model/formatter",
	"./BaseController"
], function (MessageBox, Controller, JSONModel, Fragment, ResizeHandler, History, syncStyleClass, formatter, BaseController) {
	"use strict";

	var SCREEN_MAX_SIZES = {
		PHONE: 600,
		TABLET: 1024
	};

	var ITEMS_COUNT_PER_SCREEN_SIZE = {
		PHONE: 1,
		TABLET: 2,
		DESKTOP: 4
	};

	return BaseController.extend("com.sap.sfsf.comparison.Reshuffle_Comparison_UI.controller.Comparison", {

		onInit: function () {
			this._oRootControl = this.getOwnerComponent().getRootControl();
			this.oRouter = this.getOwnerComponent().getRouter();
			this.oRouter.attachRouteMatched(this.onRouteMatched, this);

			this._aCachedItems = {};
			this._aAllCandidates = this.getOwnerComponent().getModel().getData();
			this._aSelectedItems = [];
			this._iPagesCount = this._getPagesCount(this._oRootControl.$().innerWidth());
			this._bIsDesktop = this._checkIsDesktop();

			this._iFirstItem = 0;
			this._iLastItem = this._iPagesCount;

			this._setModels();

			// Resize handler is needed in order us to determine how many items
			// will be shown next to each other, depending on the screen size.
			this._iResizeHandlerId = ResizeHandler.register(
				this.getOwnerComponent().getRootControl(),
				this._onResize.bind(this));
		},

		onRouteMatched: function (oEvent) {
			this._aSelectedItems = this.getOwnerComponent().aSelectedItems;
			this._aSelectedItemsIds = this._getSelectedItemsIds();
			this._iPagesCount = this._getPagesCount(this._oRootControl.$().innerWidth());
			this._bIsDesktop = this._checkIsDesktop();
			this._updateFirstPage();

			this._aCandidatesToShow = this._getCandidatesToShow(this._aAllCandidates);

			this.getView().getModel("settings").setData({
				pagesCount: this._iPagesCount,
				isDesktop: this._bIsDesktop
			});
			this.getView().getModel("candidates").setData(this._aCandidatesToShow);
		},

		onAfterRendering: function () {
			this._oCarouselSnapped = this.getView().byId("carousel-snapped");
			this._oCarouselExpanded = this.getView().byId("carousel-expanded");
			this._oDynamicPage = this.getView().byId("dynamic-page");
		},

		onPageChanged: function (oEvent) {
			var aActivePages = oEvent.getParameter("activePages"),
				oCandidatesData;
			this._iFirstItem = aActivePages[0];
			this._iLastItem = aActivePages[aActivePages.length - 1] + 1;
			this._updateCarouselsActivePage();

			oCandidatesData = this._getModelData(this._aCandidatesToShow.Candidates);
			this.getView().getModel("candidates").setData(oCandidatesData);
		},

		onPanelExpanded: function (oEvent) {
			var oSource = oEvent.getSource();

			oSource.getParent().getContent()[1].getItems().forEach(function (oControl) {
				oControl.getItems()[1].setVisible(oSource.getExpanded());
			});
		},

		onStateChange: function (oEvent) {
			var bIsExpanded = oEvent.getParameter("isExpanded");

			// This is needed because of animation issues with Carousel control
			// when it is placed in the Title area of the DynamicPage
			bIsExpanded && this._oDynamicPage.removeSnappedContent(this._oCarouselSnapped);
			!bIsExpanded && this._oDynamicPage.addSnappedContent(this._oCarouselSnapped);
		},

		onNavBack: function () {
			var oHistory = History.getInstance();
			var sPreviousHash = oHistory.getPreviousHash();

			if (sPreviousHash !== undefined) {
				window.history.go(-1);
			} else {
				var oRouter = this.getOwnerComponent().getRouter();
				oRouter.navTo("page1", {}, true);
			}
		},
		onJobHistory: function (oEvent, candidateId) {
			var oButton = oEvent.getSource(),
				oView = this.getView();

			var oModel = oView.getModel();
			var oModelData = oModel.getData();
			var that = this;
			$.ajax({
				url: "/srv_api/jobhistories/" + candidateId,
				method: "GET",
				contentType: "application/json",
				success: function (result, xhr, data) {
					for (var item of result) {
						if (item.startDate !== null) {
							item.startDate = that._getformatDate(item.startDate);
						}
						if (item.endDate !== null) {
							item.endDate = that._getformatDate(item.endDate);
						}
						if (item.lastModifiedDate !== null) {
							item.lastModifiedDate = that._getformatDate(item.lastModifiedDate);
						}
					}
					result.sort(function (a, b) {
						return parseFloat(a.bgOrderPos) - parseFloat(b.bgOrderPos);
					});
					oModelData.jobHistory = result;
					oModel.setData(oModelData);
					oModel.refresh(true);
				},
				error: function (xhr, status, err) {
					MessageBox.error("list error");
				}
			});

			this.getView().setModel(oModel);

			// create popover
			if (!this._pPopover) {
				this._pPopover = Fragment.load({
					id: oView.getId(),
					name: "com.sap.sfsf.comparison.Reshuffle_Comparison_UI.view.fragment.JobHistory",
					controller: this
				}).then(function (oPopover) {
					oView.addDependent(oPopover);
					return oPopover;
				});
			}

			this._pPopover.then(function (oPopover) {
				oPopover.openBy(oButton);
			});
		},

		_getformatDate: function (inputDate) {
			var d = new Date(Number(inputDate.replace(/\D/g, ''))),
				month = '' + (d.getMonth() + 1),
				day = '' + d.getDate(),
				year = d.getFullYear();

			if (month.length < 2)
				month = '0' + month;
			if (day.length < 2)
				day = '0' + day;

			return [year, month, day].join('/');
		},

		_onResize: function (oEvent) {
			var iWidth = oEvent.size.width,
				iNewPagesCount = this._getPagesCount(iWidth);

			if (iNewPagesCount !== this._iPagesCount) {
				this._iPagesCount = iNewPagesCount;
				this.getView().getModel("settings").setProperty("/pagesCount", this._iPagesCount);

				this._bIsDesktop = this._checkIsDesktop();
				this.getView().getModel("settings").setProperty("/isDesktop", this._bIsDesktop);

				this._updateFirstPage();
				this._updateCandidatesData();
			}
		},

		_updateFirstPage: function () {
			var iAllCanddatesCount = this._aSelectedItems.length;

			// In some cases we need to adjust the first visible page, because it may
			// happen that the screen was smaller and we only showed 1 item, but then
			// the screen becomes bigger and we need to show, for example, 4 items.
			// If the user was on the last page of the Carousel control (considered
			// as first and only visibile page),  when the screen gets bigger and the
			// visible pages become more, the first page should be adjusted in a way
			// that allows us to show the required number of visible pages.
			if (this._iFirstItem + this._iPagesCount > iAllCanddatesCount) {
				this._iFirstItem = iAllCanddatesCount - this._iPagesCount;
				this._updateCarouselsActivePage();
			}
		},

		_updateCarouselsActivePage: function () {
			// Synchronization of the two Carousels
			this._oCarouselSnapped.setActivePage(this._oCarouselSnapped.getPages()[this._iFirstItem]);
			this._oCarouselExpanded.setActivePage(this._oCarouselExpanded.getPages()[this._iFirstItem]);
		},

		_setModels: function () {
			this.getView().setModel(new JSONModel(), "settings");
			this.getView().setModel(new JSONModel(), "candidates");
		},

		_updateCandidatesData: function () {
			var oCandidatesData = this._getModelData(this._aCandidatesToShow.Candidates);

			this.getView().getModel("candidates").setData(oCandidatesData);
		},

		_getPagesCount: function (iWidth) {
			var iAllCandidates = this._aSelectedItems.length,
				iPagesCount;

			if (iWidth <= SCREEN_MAX_SIZES.PHONE) {
				iPagesCount = ITEMS_COUNT_PER_SCREEN_SIZE.PHONE;
			} else if (iWidth <= SCREEN_MAX_SIZES.TABLET) {
				iPagesCount = ITEMS_COUNT_PER_SCREEN_SIZE.TABLET;
			} else {
				iPagesCount = ITEMS_COUNT_PER_SCREEN_SIZE.DESKTOP;
			}

			if (iAllCandidates && iPagesCount > iAllCandidates) {
				iPagesCount = iAllCandidates;
			}

			return iPagesCount;
		},

		_checkIsDesktop: function () {
			return this._iPagesCount === ITEMS_COUNT_PER_SCREEN_SIZE.DESKTOP;
		},

		_getSelectedItemsIds: function () {
			return this._aSelectedItems.map(function (item) {
				return parseInt(item.split("/").pop());
			});
		},

		_getCandidatesToShow: function (aAllCandidates) {
			var aSelectedCandidates = [];

			this._aSelectedItemsIds.forEach(function (id) {
				aSelectedCandidates.push(aAllCandidates.list[id]);
			});

			return this._getModelData(aSelectedCandidates);
		},

		_getModelData: function (aSelectedCandidates) {
			var allProps = [],
				ilastPage = this._iFirstItem + this._iPagesCount,
				oProp,
				oCurrentCandidate,
				oCurrentCandidateInformation,
				oPropertyValue;

			this._iLastItem = ilastPage > aSelectedCandidates.length ? aSelectedCandidates.length : ilastPage;

			// Manipulates data in a way that allows us to be able to display the name
			// of the property as a Panel title and the values of this property on each
			// of the products to be displayed in different columns in a table-like view.
			for (var key in aSelectedCandidates[0]) {
				if (aSelectedCandidates[0].hasOwnProperty(key) && key !== "caseId" && key !== "candidateID" && key !== "candidateName" && key !==
					"candidateDivisionName" && key !== "candidateDepartmentName" && key !== "candidatePositionName" && key !== "divisionName" && key !==
					"departmentName" && key !== "positionName" && key !== "modifiedAt" && key !== "modifiedBy" && key !== "createdAt" && key !==
					"createdBy") {
					oProp = {};
					oProp.key = this.getResourceText(key);
					oProp.values = [];

					for (var i = this._iFirstItem; i < this._iLastItem; i++) {
						oCurrentCandidate = aSelectedCandidates[i];
						oCurrentCandidateInformation = this._aCachedItems[oCurrentCandidate.candidateID] && this._aCachedItems[oCurrentCandidate.candidateID]
							[key];

						// Performance optimization logic: reusing already created information for products,
						// instead of creating a new one, each time a same product is selected.
						if (oCurrentCandidateInformation) {
							oProp.values.push(oCurrentCandidateInformation);

						} else {
							if (key == "candidateDivision") {
								oPropertyValue = {
									text: "<strong>" + aSelectedCandidates[i][key] + " : " + aSelectedCandidates[i]["candidateDivisionName"] + "</strong>"
								};
							} else if (key == "candidateDepartment") {
								oPropertyValue = {
									text: "<strong>" + aSelectedCandidates[i][key] + " : " + aSelectedCandidates[i]["candidateDepartmentName"] + "</strong>"
								};
							} else if (key == "candidatePosition") {
								oPropertyValue = {
									text: "<strong>" + aSelectedCandidates[i][key] + " : " + aSelectedCandidates[i]["candidatePositionName"] + "</strong>"
								};
							} else if (key == "division") {
								oPropertyValue = {
									text: "<strong>" + aSelectedCandidates[i][key] + " : " + aSelectedCandidates[i]["divisionName"] + "</strong>"
								};
							} else if (key == "Department") {
								oPropertyValue = {
									text: "<strong>" + aSelectedCandidates[i][key] + " : " + aSelectedCandidates[i]["departmentName"] + "</strong>"
								};
							} else if (key == "position") {
								oPropertyValue = {
									text: "<strong>" + aSelectedCandidates[i][key] + " : " + aSelectedCandidates[i]["positionName"] + "</strong>"
								};
							} else {
								oPropertyValue = {
									text: "<strong>" + aSelectedCandidates[i][key] + "</strong>"
								};
							}

							oProp.values.push(oPropertyValue);
							this._cacheCandidateInformation(oCurrentCandidate, key, oPropertyValue);
						}
					}

					allProps.push(oProp);
				}
			}

			return {
				Candidates: aSelectedCandidates,
				Props: allProps
			};
		},

		_cacheCandidateInformation: function (oCandidate, sProp, oPropertyValue) {
			var sProductId = oCandidate.CandidateID;

			if (!this._aCachedItems[sProductId]) {
				this._aCachedItems[sProductId] = {};
			}

			this._aCachedItems[sProductId][sProp] = oPropertyValue;
		}

	});

});