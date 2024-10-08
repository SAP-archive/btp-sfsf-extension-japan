package com.sap.sfsf.reshuffle.simulation.backend.controller;

import java.net.PasswordAuthentication;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    private Logger LOG = LoggerFactory.getLogger(UpsertController.class);

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
    String upsert(@RequestBody List<Candidate> list, HttpServletResponse response) {
        List<MyEmpJobUpsert> upsertList = new ArrayList<MyEmpJobUpsert>();
        Date startDate = configService.getStartDate();
        String casdID = list.get(0).getCaseID();
        for (Candidate candidate : list) {
            upsertList.add(new MyEmpJobUpsert(candidate.getCandidateID(), startDate));
        }
        try {
            upsertService.upsert(upsertList);
            List<Candidate> updatedList = candidateService.updateUpsertFlg(list, casdID, "更新済");
            response.setHeader("upsert_status", "success");
            return new GsonBuilder().serializeNulls().setDateFormat("yyyy/MM/dd HH:mm:ss").create().toJson(updatedList);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("更新失敗：" + e.getMessage());
            List<Candidate> updatedList = candidateService.updateUpsertFlg(list, casdID, "更新失敗");
            response.setHeader("upsert_status", "fail");
            return new GsonBuilder().serializeNulls().setDateFormat("yyyy/MM/dd HH:mm:ss").create().toJson(updatedList);
        }
    }

    /**
     * メール送信処理
     * 
     * @param list
     * @return
     */
    @RequestMapping(value = "/mail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    String sendMail(@RequestBody List<Candidate> list, HttpServletResponse response) {
        List<MimeMessage> messageList = new ArrayList<MimeMessage>();
        List<String> idList = new ArrayList<String>();
        Destination mailDest = DestinationAccessor.getDestination("MAIL");
        String from = mailDest.get("mail.smtp.from").getOrElse("").toString();
        String caseID = list.get(0).getCaseID();

        try {
            for (Candidate candidate : list) {
                String mailAddress = personService.getEmailAddressById(candidate.getCandidateID());
                String bcc = "ryoko.yoshiba@sap.com";

                if (mailAddress != null) {

                    MimeMessage mailMessage = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = null;

                    helper = new MimeMessageHelper(mailMessage, true, "ISO-2022-JP");

                    helper.setTo(mailAddress);
                    helper.setBcc(bcc);
                    helper.setFrom(from);
                    helper.setSubject("異動発令");
                    String mailBody = mailBodyService.generateMailBody(candidate);
                    helper.setText(mailBody, true);

                    messageList.add(mailMessage);
                    idList.add(candidate.getCandidateID());

                }
            }

            MimeMessage[] mailArray = new MimeMessage[messageList.size()];
            javaMailSender.send(messageList.toArray(mailArray));
            List<Candidate> updatedList = candidateService.updateMailFlg(list, caseID, "送信済");
            response.setHeader("mail_status", "success");
            return new GsonBuilder().serializeNulls().setDateFormat("yyyy/MM/dd HH:mm:ss").create().toJson(updatedList);

        } catch (Exception e) {
            LOG.error("送信失敗:" + e.getMessage());
            List<Candidate> updatedList = candidateService.updateMailFlg(list, caseID, "送信失敗");
            response.setHeader("mail_status", "fail");
            return new GsonBuilder().serializeNulls().setDateFormat("yyyy/MM/dd HH:mm:ss").create().toJson(updatedList);
        }

    }

    /**
     * メールジョブ処理
     * 
     * @param list
     * @param request
     * @return
     */
    @RequestMapping(value = "/mailjob", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String createEmailJob(@RequestBody List<Candidate> list, HttpServletRequest request,
            HttpServletResponse response) {
        String startDT = request.getHeader("startDateTime");
        String caseID = list.get(0).getCaseID();
        String requestURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        LOG.debug("Request URL:" + requestURL);
        try {
            jobService.createEmailJob(requestURL, list, startDT);
            List<Candidate> updatedList = candidateService.updateMailFlg(list, caseID, "予約済");
            response.setHeader("mailjob_status", "success");
            return new GsonBuilder().serializeNulls().setDateFormat("yyyy/MM/dd HH:mm:ss").create().toJson(updatedList);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            List<Candidate> updatedList = candidateService.updateMailFlg(list, caseID, "予約失敗");
            response.setHeader("mailjob_status", "fail");
            return new GsonBuilder().serializeNulls().setDateFormat("yyyy/MM/dd HH:mm:ss").create().toJson(updatedList);
        }
    }
}