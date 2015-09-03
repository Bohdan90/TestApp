package controllers;

import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpResponseStatus.MOVED_PERMANENTLY;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

public class RedirectController extends Controller {

	protected final static ByteBuf CONTENT = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("Hello World",
					CharsetUtil.US_ASCII));

	private String redirectUrl;

	public RedirectController(String redirectUrl) {
		this.redirectUrl = "http://" + redirectUrl;
	}

	@Override
	public FullHttpResponse getResponse() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				MOVED_PERMANENTLY, CONTENT.duplicate());
		response.headers().set(LOCATION, this.redirectUrl);
		return response;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;

	}

}
