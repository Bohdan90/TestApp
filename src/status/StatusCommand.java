package status;

import java.util.List;
import java.util.Map;

import database.Request;

public class StatusCommand {

	private int totalCount;
	private int ipCount;
	private int conCount;
	private List<Map<String, String>> ipRequests;
	private List<Map<String, String>> redirUrls;
	private List<Request> lastRequests;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getIpCount() {
		return ipCount;
	}

	public void setIpCount(int ipCount) {
		this.ipCount = ipCount;
	}

	public List<Map<String, String>> getIpRequests() {
		return ipRequests;
	}

	public void setIpRequests(List<Map<String, String>> ipRequests) {
		this.ipRequests = ipRequests;
	}

	public List<Map<String, String>> getRedirectUrls() {
		return redirUrls;
	}

	public void setRedirectUrls(List<Map<String, String>> redirectUrls) {
		this.redirUrls = redirectUrls;
	}

	public List<Request> getLastRequests() {
		return lastRequests;
	}

	public void setLastRequests(List<Request> lastRequests) {
		this.lastRequests = lastRequests;
	}

	public int getConnectsCount() {
		return conCount;
	}

	public void setConnectsCount(int connectsCount) {
		this.conCount = connectsCount;
	}

}
