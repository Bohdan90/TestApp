package controllers;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import status.StatusCommand;
import status.StatusPage;
import database.RequestJdbcDao;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class StatusController extends Controller {

	@Override
	public FullHttpResponse getResponse() {
		RequestJdbcDao dao = new RequestJdbcDao();
		StatusCommand sc = dao.getStatusCommand();
		sc.setConnectsCount(this.getConnections());
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				StatusPage.getContent(sc));
		response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
		response.headers().set(CONTENT_LENGTH,
				response.content().readableBytes());
		return response;
	}

}
