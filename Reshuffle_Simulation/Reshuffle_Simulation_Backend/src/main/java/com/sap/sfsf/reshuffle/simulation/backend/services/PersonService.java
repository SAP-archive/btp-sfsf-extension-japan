package com.sap.sfsf.reshuffle.simulation.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.connectivity.DefaultErpHttpDestination;
import com.sap.cloud.sdk.s4hana.connectivity.ErpHttpDestination;
import com.sap.sfsf.reshuffle.simulation.backend.util.VdmUtil;
import com.sap.sfsf.vdm.namespaces.ecpersonalinformation.PerEmail;
import com.sap.sfsf.vdm.namespaces.ecpersonalinformation.PerEmailByKeyFluentHelper;
import com.sap.sfsf.vdm.services.DefaultECPersonalInformationService;

@Service
public class PersonService {
	private ErpHttpDestination destination;

	private Logger LOG = LoggerFactory.getLogger(PersonService.class);

	private void init(){
		this.destination = DestinationAccessor.getDestination("SFSF").asHttp()
		.decorate(DefaultErpHttpDestination::new);
	}
	

	/**
	 * IDを利用してEmailアドレスを取得
	 * @param personIdExternal
	 * @return
	 */
	public String getEmailAddressById(String personIdExternal) {
		this.init();
		PerEmailByKeyFluentHelper query = new DefaultECPersonalInformationService().withServicePath("odata/v2")
				.getPerEmailByKey("8448", personIdExternal);
		LOG.debug("PerEmail query:" + query);
		String email = null;
		try {
			PerEmail perEmail = query.execute(destination);
			email = VdmUtil.getStringFromVdm(perEmail,"emailAddress");
		} catch (ODataException e) {
			//e.printStackTrace();
			LOG.error("=== Error getting email for employee! ===");
		}

		return email;
	}


}
