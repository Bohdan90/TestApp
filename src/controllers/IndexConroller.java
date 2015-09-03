package controllers;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class IndexConroller extends Controller {

	protected final static ByteBuf CONTENT = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer(
					"Plz use syntax like: /hello /status or /redirect?url=",
					CharsetUtil.US_ASCII));

	@Override
	public FullHttpResponse getResponse() {
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				CONTENT.duplicate());
		response.headers().set(CONTENT_TYPE, "text/plain");
		response.headers().set(CONTENT_LENGTH,
				response.content().readableBytes());
		return response;
	}

}
