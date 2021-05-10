package com.sap.sfsf.reshuffle.simulation.backend.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sap.sfsf.reshuffle.simulation.backend.model.Candidate;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyDisciplinary;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyEmpJob;
import com.sap.sfsf.reshuffle.simulation.backend.model.MyPerGlobalInfoJPN;
import com.sap.sfsf.reshuffle.simulation.backend.util.ListToMapUtil;
import com.sap.sfsf.vdm.namespaces.ecemploymentinformation.EmpJob;

@Service
public class SimulationCheckService {
	private Logger LOG = LoggerFactory.getLogger(SimulationCheckService.class);

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private DisciplinaryService disciplinaryService;

	private static final int transferTimesLimit = 5;
	private static final int ratingLimit = 3;
	private static final int transferYearLimit = 1;
	private static final int tenureWarnLimit = 3;

	/**
	 * 候補者の雇用情報をチェック
	 * <li>異動回数</li>
	 * <li>異動後勤続期間</li>
	 * <li>同じポジションで勤続年数が長い</li>
	 * 
	 * 
	 * @param list
	 */
	public void checkEmploymentInformation(List<Candidate> list) {
		LOG.debug("==== Check employment information start ====");
		Map<String, EmpJob> empJobMap = employeeService.getCurrentJobListMap();
		for (Candidate candidate : list) {
			String userID = candidate.getCandidateID();
			LOG.debug("User ID:" + userID);

			if (candidate.getCheckResult() == null) {
				candidate.setCheckResult("");
			}
			EmpJob currentJob = empJobMap.get(userID);
			if (currentJob != null) {
				JsonObject currentJobJSON = new Gson().fromJson(new Gson().toJson(currentJob), JsonObject.class);
				LOG.debug("Current Job JSON:" + new Gson().toJson(currentJob));
				checkOnLeave(candidate, currentJobJSON);
				checkPeriodAfterLastTransfer(candidate);
				checkSamePositionPeriod(candidate);
			} else {
				LOG.error("No current job for candidate:" + userID);
			}
			checkTransferTimes(candidate);

		}
		LOG.debug("==== Check employment information End ====");
	}

	/**
	 * 候補者の個人情報をチェック
	 * <li>3年間連続評価</li>
	 * <li>親縁者が同じ部署にいないかを確認</li>
	 * 
	 * @param list
	 */
	public void checkPersonInformation(List<Candidate> list) {
		Map<String, MyEmpJob> empJobMap = employeeService.getCurrentJobListMapByQuery();
		Map<String, MyPerGlobalInfoJPN> spouseListMap = employeeService.getSpouseListMap();
		Map<String, MyDisciplinary> harassmentMap = disciplinaryService.getHarassmentMap();
		Map<String, MyDisciplinary> victimMap = disciplinaryService.getVictimMap();
		Map<String, Candidate> candidateMap = ListToMapUtil.getMap("candidateID", list);

		LOG.debug("==== Check person information Start====");
		for (Candidate candidate : list) {
			checkRating(candidate);
			checkRelative(candidate, spouseListMap, empJobMap);
			checkDisplinary(candidate, candidateMap, harassmentMap, empJobMap);
			checkHarassment(candidate, candidateMap, victimMap, empJobMap);
		}
		LOG.debug("==== Check person information End ====");
	}

	/**
	 * ハラスメントチェック 過去にハラスメントされた人と同じ部署に配属されないかを確認
	 * 
	 * @param candidate
	 * @param candidateMap  候補者一覧Map
	 * @param harassmentMap ハラスメントMap
	 * @param empJobMap
	 */
	private void checkHarassment(Candidate candidate, Map<String, Candidate> candidateMap,
			Map<String, MyDisciplinary> victimMap, Map<String, MyEmpJob> empJobMap) {
		String candidateId = candidate.getCandidateID();
		if (victimMap.containsKey(candidateId)) {
			String aggressorId = victimMap.get(candidateId).getUser();
			if (candidateMap.containsKey(aggressorId)) {
				Candidate aggressorCandidate = candidateMap.get(aggressorId);
				if (aggressorCandidate.getNextDivision().equals(candidate.getNextDivision())
						|| aggressorCandidate.getNextDepartment().equals(candidate.getNextDepartment())
						|| aggressorCandidate.getNextPosition().equals(candidate.getNextPosition())) {
					candidate.setCheckStatus("NG");
					appendCheckResult(candidate, "Employee " + candidate.getCandidateName() + " was harassed by "
							+ aggressorCandidate.getCandidateName());
				}
			}
		}
	}

	/**
	 * 懲罰歴チェック
	 * 
	 * @param candidate
	 * @param candidateMap
	 * @param harassmentMap
	 * @param empJobMap
	 */
	private void checkDisplinary(Candidate candidate, Map<String, Candidate> candidateMap,
			Map<String, MyDisciplinary> harassmentMap, Map<String, MyEmpJob> empJobMap) {
		String candidateId = candidate.getCandidateID();
		if (harassmentMap.containsKey(candidateId)) {
			String reason = harassmentMap.get(candidateId).getReason();
			appendCheckResult(candidate, "Employee was punished before for the reason:" + reason);
			candidate.setCheckStatus("NG");
		}
	}

	/**
	 * 親縁者チェック
	 * 
	 * @param candidate     異動候補者
	 * @param spouseListMap 配偶者リストマップ
	 * @param empJobMap     現職マップ
	 */
	private void checkRelative(Candidate candidate, Map<String, MyPerGlobalInfoJPN> spouseListMap,
			Map<String, MyEmpJob> empJobMap) {
		LOG.debug("=== Relative check start ===");
		String candidateId = candidate.getCandidateID();
		String candidateDepartment = candidate.getNextDepartment();
		if (spouseListMap.containsKey(candidateId)) {
			String spouseId = spouseListMap.get(candidateId).getPersonIdExternal();
			String spouseDepartment = empJobMap.get(spouseId).getDepartment();
			if (spouseDepartment.equals(candidateDepartment)) {
				candidate.setCheckStatus("NG");
				appendCheckResult(candidate, "Employee and spouse cannot be in the same department!");
			}
		}
		LOG.debug("=== Relative check end ===");
	}

	public void preCheck(List<Candidate> list) {
		for (Candidate candidate : list) {
			candidate.setCheckStatus(null);
			candidate.setCheckResult(null);
		}
	}

	/**
	 * Final check, if no error, set status to OK.
	 */
	public void finalCheck(List<Candidate> list) {
		Date now = new Date();
		for (Candidate candidate : list) {
			if (candidate.getCheckResult().equals("")) {
				candidate.setCheckStatus("OK");
			}
			candidate.setCheckDateTime(now);
		}
		candidateService.deleteAll();
		candidateService.saveAll(list);
	}

	/**
	 * 評価歴を確認
	 * 
	 * @param candidate
	 * @param ratingList
	 */
	private void checkRating(Candidate candidate) {
		LOG.debug("=== Check Rating Start ===");
		Integer r1, r2, r3;
		r1 = candidate.getRating1() == null ? Integer.MAX_VALUE : Integer.parseInt(candidate.getRating1());
		r2 = candidate.getRating2() == null ? Integer.MAX_VALUE : Integer.parseInt(candidate.getRating2());
		r3 = candidate.getRating3() == null ? Integer.MAX_VALUE : Integer.parseInt(candidate.getRating3());
		if (r1 < ratingLimit || r2 < ratingLimit || r3 < ratingLimit) {
			candidate.setCheckStatus("NG");
			appendCheckResult(candidate, "Employee's performance is not good enough!");
		}
		LOG.debug("=== Check Rating End ===");
	}

	/**
	 * 異動回数を確認
	 * 
	 * @param candidate
	 * @param transferTimes
	 */
	public void checkTransferTimes(Candidate candidate) {
		LOG.debug("=== Check Transfer Times Start ===");
		if (candidate.getTransferTimes() > transferTimesLimit) {
			candidate.setCheckStatus("NG");
			appendCheckResult(candidate,
					"Employee has been transferred for more than " + transferTimesLimit + " times!");
		}
		LOG.debug("=== Check Transfer Times End ===");
	}

	/**
	 * 社員は休職中かを確認
	 * 
	 * @param candidate
	 * @param currentJobJSON
	 */
	private void checkOnLeave(Candidate candidate, JsonObject currentJobJSON) {
		LOG.debug("=== Check On Leave Start ===");
		if (currentJobJSON.has("event")) {
			String event = currentJobJSON.get("event").getAsString();
			if (event.equals("3671")) {
				candidate.setCheckStatus("NG");
				appendCheckResult(candidate, "Employee is on leave!");
			}
		}
		LOG.debug("=== Check On Leave End ===");
	}

	/**
	 * 異動後の勤続年数を確認
	 * 
	 * @param candidate
	 */
	public void checkPeriodAfterLastTransfer(Candidate candidate) {
		LOG.debug("=== Check Period After Last Transfer Start ===");
		if (candidate.getJobTenure() < transferYearLimit) {
			appendCheckResult(candidate, "Employee cannot be transfered within 1 year after last transfer!");
			candidate.setCheckStatus("NG");
		}
		LOG.debug("=== Check Period After Last Transfer End ===");
	}

	/**
	 * 勤続年数を確認
	 * 
	 * @param candidate
	 */
	public void checkSamePositionPeriod(Candidate candidate) {
		LOG.debug("=== Check Same Position Period Start ===");
		if (candidate.getJobTenure() > tenureWarnLimit) {
			if (candidate.getCheckStatus() == null) {
				candidate.setCheckStatus("WARN");
			}
			appendCheckResult(candidate, "Employee stays at the same position for " + tenureWarnLimit + " years!");
		}
		LOG.debug("=== Check Same Position Period End ===");

	}

	private void appendCheckResult(Candidate candidate, String result) {
		StringBuilder sb = new StringBuilder(candidate.getCheckResult());
		String content = String.format("<li>%s</li>", result);
		sb.append(content);
		candidate.setCheckResult(sb.toString());
	}

}
