package database;

import java.util.Date;

public class Request {

	private int id;
	private String ip;
	private String uri = "";
	private Date period;
	private String redirectUrl = "";
	private int receivedBytes = 0;
	private int sentBytes = 0;
	private long timestamp;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date date) {
		this.period = date;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public int getReceived_bytes() {
		return receivedBytes;
	}

	public void setReceived_bytes(int receivedBytes) {
		this.receivedBytes = receivedBytes;
	}

	public int getSentBytes() {
		return sentBytes;
	}

	public void setSentBytes(int sentBytes) {
		this.sentBytes = sentBytes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public float getSpeed() {
		return (this.timestamp > 0) ? (this.receivedBytes + this.sentBytes)
				* 1000 / this.timestamp : 0;
	}
}
