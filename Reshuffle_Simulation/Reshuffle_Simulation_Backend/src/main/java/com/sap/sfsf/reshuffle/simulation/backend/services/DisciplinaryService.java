package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sap.cloud.sdk.odatav2.connectivity.FilterExpression;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataQueryBuilder;
import com.sap.cloud.sdk.odatav2.connectivity.ODataType;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyDisciplinary;
import com.sap.sfsf.reshuffle.simulation.backend.util.ListToMapUtil;

@Service
public class DisciplinaryService {

	/**
	 * ハラスメント記録を取得
	 * @return
	 */
	public List<MyDisciplinary> getHarassmentByQuery() {
		List<MyDisciplinary> list = null;
		String[] selects = { "User", "cust_Reason", "cust_Severity", "cust_dateofincident", "cust_AffectedEmployee",
				"cust_IncidentStatus" };
		FilterExpression filter = new FilterExpression("cust_Reason", "eq", new ODataType<String>("Harassment"));
		try {
			list = ODataQueryBuilder.withEntity("odata/v2", "cust_DisciplinaryAction")
					.select(selects)
					.filter(filter)
					.build()
					.execute("SFSF")
					.asList(MyDisciplinary.class);
		} catch (ODataException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/**
	 * ハラスメントマップを取得
	 * @return
	 */
	public Map<String, MyDisciplinary> getHarassmentMap(){
		List<MyDisciplinary> list = this.getHarassmentByQuery();
		return ListToMapUtil.getMap("user", list);
	}
	
	public Map<String, MyDisciplinary> getVictimMap(){
		List<MyDisciplinary> list = this.getHarassmentByQuery();
		return ListToMapUtil.getMap("affectedEmployee", list);
	}
}
