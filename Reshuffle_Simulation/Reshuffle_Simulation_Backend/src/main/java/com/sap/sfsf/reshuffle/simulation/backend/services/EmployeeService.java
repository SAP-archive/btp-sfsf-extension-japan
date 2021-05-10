package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.datamodel.odata.helper.Order;
import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQuery;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataType;
import com.sap.cloud.sdk.s4hana.connectivity.DefaultErpHttpDestination;
import com.sap.cloud.sdk.s4hana.connectivity.ErpHttpDestination;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyEmpJob;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyPerGlobalInfoJPN;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyRating;
import com.sap.sfsf.reshuffle.simulation.backend.util.CustomFilterExpression;
import com.sap.sfsf.reshuffle.simulation.backend.util.DateTimeUtil;
import com.sap.sfsf.reshuffle.simulation.backend.util.ListToMapUtil;
import com.sap.sfsf.vdm.namespaces.ecemploymentinformation.EmpJob;
import com.sap.sfsf.vdm.namespaces.ecemploymentinformation.EmpJobFluentHelper;
import com.sap.sfsf.vdm.services.DefaultECEmploymentInformationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
	final ErpHttpDestination destination = DestinationAccessor.getDestination("SFSF_2nd").asHttp()
			.decorate(DefaultErpHttpDestination::new);
	
	private Logger LOG = LoggerFactory.getLogger(EmployeeService.class);
	
	/**
	 * 異動履歴の一覧を取得
	 * @return
	 */
	public List<EmpJob> getTransferList() {
		LOG.debug("=== Get transfer list start ===");
		List<EmpJob> list = null;
		try {
			list = new DefaultECEmploymentInformationService()
				.withServicePath("odata/v2")
				.getAllEmpJob()
				.select(
						EmpJob.USER_ID,
						EmpJob.START_DATE,
						EmpJob.END_DATE,
						EmpJob.EVENT,
						EmpJob.EVENT_REASON)
				.filter(EmpJob.EVENT.eq("3681")
						)
				.execute(destination);
		} catch (ODataException e) {
			LOG.error("=== Error Getting Transfer Times by ID ===");
			e.printStackTrace();
		}
		
		LOG.debug("=== Get transfer list end ===");
		return list;
	}
	
	/**
	 * 異動一覧をMapに変換
	 * @return
	 */
	public Map<String, List<EmpJob>> getTransferListMap(){
		LOG.debug("=== Transfer to map start ===");
		List<EmpJob> list = this.getTransferList();
		Map<String, List<EmpJob>> map = list.stream().collect(
				Collectors.groupingBy(job->{
					JsonObject jobJSON = new Gson().fromJson(new Gson().toJson(job), JsonObject.class);
					return jobJSON.get("userId").getAsString();
				}));
		LOG.debug("=== Transfer to map end ===");
		return map;
	}
	
	/**
	 * 現在の EmpJob List情報を取得
	 * @return
	 */
	public List<EmpJob> getCurrentJobs() {
		LOG.debug("=== Get current jobs start ===");
		List<EmpJob> list = null;

		//LocalDateTime endDate = DateTimeUtil.getEndDate();
		LocalDateTime today = DateTimeUtil.getToday();
		
		EmpJobFluentHelper empQuery = new DefaultECEmploymentInformationService()
				.withServicePath("odata/v2")
				.getAllEmpJob()
				.select(
						EmpJob.USER_ID,
						EmpJob.START_DATE,
						EmpJob.END_DATE,
						EmpJob.EVENT,
						EmpJob.EVENT_REASON,
						EmpJob.MANAGER_ID
						)
				.filter(EmpJob.END_DATE.gt(today)
						.and(EmpJob.START_DATE.le(today))
						)
				.orderBy(EmpJob.END_DATE, Order.DESC);
		LOG.info("Query:"+empQuery.toQuery().toString());
		try {
			list = empQuery.execute(destination);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		LOG.debug("=== Get current jobs end ===");
		return list;
	}
	
	/**
	 * ODataQueryBuilderを利用して現在のEmpJobを取得
	 * $expand = positionNav/parentPosition
	 * @return
	 */
	public List<MyEmpJob> getCurrentJobsByQuery() {
		LOG.debug("=== Get current jobs start ===");
		
		//LocalDateTime endDate = DateTimeUtil.getEndDate();
		LocalDateTime today = DateTimeUtil.getToday();
		
		String[] selects = {"userId","position","department","startDate","endDate","event","eventReason","positionNav/parentPosition/code"};
		CustomFilterExpression filter1 = new CustomFilterExpression("startDate","le",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("endDate","gt",DateTimeUtil.getDateTimeFilter(today));
		
		List<MyEmpJob> list = null;
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "EmpJob")
				.select(selects)
				.expand("positionNav/parentPosition")
				.filter(filter1.and(filter2))
				.build();
		LOG.debug("Query:"+query.toString());
		try {
			list = query
					.execute("SFSF_2nd")
					.asList(MyEmpJob.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		LOG.debug("=== Get current jobs end ===");
		return list;
	}

	
	
	/**
	 * Return EmpJob List as Map
	 * @return
	 */
	public Map<String, EmpJob> getCurrentJobListMap(){
		List<EmpJob> list  = this.getCurrentJobs();
		return ListToMapUtil.getMap("userId", list);
	}
	
	/**
	 * Return EmpJob List as Map
	 * @return
	 */
	public Map<String, MyEmpJob> getCurrentJobListMapByQuery(){
		List<MyEmpJob> list  = this.getCurrentJobsByQuery();
		return ListToMapUtil.getMap("userId", list);
	}

	/**
	 * 配偶者情報を取得
	 * @return
	 */
	public List<MyPerGlobalInfoJPN> getSpouseByQuery(){
		List<MyPerGlobalInfoJPN> list = null;
		String[] selects = {"personIdExternal","customString13","customString20"};
		LocalDateTime today = DateTimeUtil.getToday();
		CustomFilterExpression filter1 = new CustomFilterExpression("startDate","le",DateTimeUtil.getDateTimeFilter(today));
		CustomFilterExpression filter2 = new CustomFilterExpression("endDate","gt",DateTimeUtil.getDateTimeFilter(today));
		//FilterExpression filter3 = new FilterExpression("customString13","eq", new ODataType<String>("5458"));
		try {
			list = ODataQueryBuilder
					.withEntity("odata/v2", "PerGlobalInfoJPN")
					.select(selects)
					.filter(filter1.and(filter2))
					.build()
					.execute("SFSF_2nd")
					.asList(MyPerGlobalInfoJPN.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 配偶者Mapを取得
	 * @return
	 */
	public Map<String,MyPerGlobalInfoJPN> getSpouseListMap(){
		List<MyPerGlobalInfoJPN> list = this.getSpouseByQuery();
		return ListToMapUtil.getMap("personIdExternal", list);
	}
	
	/**
	 * User IDを利用して直近3回評価歴を取得
	 * @param userID
	 * @return
	 */
	public List<MyRating> getRatingByID(String userID){
		List<MyRating> list = null;
		String[] selects = {"userId","rating","startDate","endDate"};
		try {
			list = ODataQueryBuilder
					.withEntity("odata/v2", "TrendData_SysOverallPerformance")
					.select(selects)
					.top(3)
					.build()
					.execute("SFSF_2nd")
					.asList(MyRating.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		LOG.debug(new Gson().toJson(list));
		return list;
	}

	/**
	 * 直近３年の評価歴を取得
	 * 
	 * @return
	 */
	public List<MyRating> getRatingList(){
		LOG.debug("=== Get Rating List start ===");
		List<MyRating> list = null;
		String[] selects = {"userId","rating","startDate","endDate","lastModified"};
		LocalDateTime threeYearsAgo = DateTimeUtil.getYearsAgo(3);
		FilterExpression filter = new FilterExpression("lastModified","ge",new ODataType<Date>(DateTimeUtil.getDate(threeYearsAgo)));
		ODataQuery query = ODataQueryBuilder
				.withEntity("odata/v2", "TrendData_SysOverallPerformance")
				.select(selects)
				.filter(filter)
				.build();
		LOG.debug("Rating List Query:"+query.toString());
		try {
			list = query
					.execute("SFSF_2nd")
					.asList(MyRating.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}
		LOG.debug(new Gson().toJson(list));
		LOG.debug("=== Get Rating List end ===");
		return list;
	}

	/**
	 * 評価歴リストをMapへ変換
	 * @return
	 */
	public Map<String, List<MyRating>> getRatingMap(){
		LOG.debug("=== Rating to map start ===");
		List<MyRating> list = this.getRatingList();	
		return ListToMapUtil.getGroupedList(list);
	}


}
