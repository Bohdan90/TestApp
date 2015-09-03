package status;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import database.Request;

public final class StatusPage {

	private static final String NEWLINE = "\r\n";
	private static String display;

	public static ByteBuf getContent(StatusCommand stc) {
		getStatTbl(stc);
		getRedirTbl(stc);
		display += "</body>" + NEWLINE + "</html>" + NEWLINE;
		return Unpooled.copiedBuffer(display, CharsetUtil.UTF_8);
	}

	private static void getStatTbl(StatusCommand status) {
		display = "<html><head><title>Status</title>"
				+ "<style>"
				+ "table, td, th {border: 2px double  black;} table{margin: 7px;} td, th {padding: 2px;}"
				+ "</style>" + "</head>" + NEWLINE + "<body>" + NEWLINE;
		display += "Total requests: " + status.getTotalCount() + "<br>";
		display += "Unic IP requests : " + status.getIpCount() + "<br>";
		display += "Connections: " + status.getConnectsCount() + "<br><br>";
		display += "<div style='float: left;'><table><thead><tr><th>IP</th><th>Count</th><th>Last request time</th></tr></thead>";
		display += "IP statistic:<br>";
		for (Map<String, String> ip : status.getIpRequests()) {
			display += "<tr><td>" + ip.get("src_ip") + "</td><td>"
					+ ip.get("count") + "</td><td>" + ip.get("period")
					+ "</td></tr>";
		}
		display += "</table><br>";
	}

	private static void getRedirTbl(StatusCommand status) {
		display += "Statistic of redirection:<br>";
		display += "<table><thead><tr><th>Redirect url</th><th>Count</th></tr></thead>";
		for (Map<String, String> url : status.getRedirectUrls()) {
			display += "<tr><td>" + url.get("redirect_url") + "</td><td>"
					+ url.get("count") + "</td></tr>";
		}
		display += "</table></div>";
		display += "<div style='float: left;'>Last request statistic:<br>";
		display += "<table><thead><tr><th>src_ip</th><th>URI</th><th>timestamp(ms)</th>"
				+ "<th>sent_bytes</th><th>received_bytes</th><th>speed (bytes/sec)</th></tr></thead>";
		for (Request req : status.getLastRequests()) {
			display += "<tr><td>" + req.getIp() + "</td><td>" + req.getUri()
					+ "</td><td>" + req.getTimestamp() + "</td><td>"
					+ req.getSentBytes() + "</td><td>"
					+ req.getReceived_bytes() + "</td><td>" + req.getSpeed()
					+ "</td></tr>";
		}
		display += "</table></div>";
	}
}
