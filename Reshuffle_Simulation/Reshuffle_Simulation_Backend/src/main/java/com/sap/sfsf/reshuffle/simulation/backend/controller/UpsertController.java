package com.sap.sfsf.reshuffle.simulation.backend.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.GsonBuilder;
import com.sap.cloud.sdk.cloudplatform.connectivity.Destination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyEmpJobUpsert;
import com.sap.sfsf.reshuffle.simulation.backend.services.CandidateService;
import com.sap.sfsf.reshuffle.simulation.backend.services.ConfigService;
import com.sap.sfsf.reshuffle.simulation.backend.services.EmpJobUpsertService;
import com.sap.sfsf.reshuffle.simulation.backend.services.JobSchedulerService;
import com.sap.sfsf.reshuffle.simulation.backend.services.PersonService;
import com.sap.sfsf.reshuffle.simulation.backend.services.MailBodyService;

@Controller
public class UpsertController {
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private PersonService personService;
	
	@Autowired
	private CandidateService candidateService;
	
	@Autowired
	private EmpJobUpsertService upsertService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private MailBodyService mailBodyService;
	
	@Autowired
	JobSchedulerService jobService;

	/**
	 * SFSF側へ更新しに行く処理
	 * 
	 * @param list
	 * @return
	 */
	@RequestMapping(value = "/upsert", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	String sendMail(@RequestBody List<Candidate> list ) {
		List<SimpleMailMessage> messageList = new ArrayList<SimpleMailMessage>();
		List<MyEmpJobUpsert> upsertList = new ArrayList<MyEmpJobUpsert>();
		List<String> idList = new ArrayList<String>();
		for(Candidate candidate: list) {
			String mailAddress = personService.getEmailAddressById(candidate.getCandidateID());
			Destination mailDest = DestinationAccessor.getDestination("MAIL");
			String from = mailDest.get("mail.smtp.from").getOrElse("").toString();
			
			mailAddress = "liang.liang.wang@sap.com";
			
			if (mailAddress != null) {
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				mailMessage.setTo(mailAddress);
				mailMessage.setFrom(from);
				mailMessage.setSubject("異動発令");
				String mailBody = mailBodyService.generateMailBody(candidate);
				mailMessage.setText(mailBody);
				mailMessage.setText("次のポジションへ異動："+ candidate.getNextPosition());
				messageList.add(mailMessage);
				idList.add(candidate.getCandidateID());
			}
			upsertList.add(new MyEmpJobUpsert(candidate.getCandidateID(),new Date()));
			
		}
		SimpleMailMessage[] mailArray = new SimpleMailMessage[messageList.size()];
		try {
			javaMailSender.send(messageList.toArray(mailArray));
			upsertService.upsert(upsertList);
			List<Candidate> updatedList = candidateService.updateMailFlg(list,"Y");
			return new GsonBuilder()
					.setDateFormat("yyyy/MM/dd HH:mm:ss")
					.serializeNulls()
					.create()
					.toJson(updatedList);
			
		}catch(Exception e) {
			e.printStackTrace();
			return "{\"msg\":\"Upsert failed!\"}";
		}
	}
}