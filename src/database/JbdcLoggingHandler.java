package database;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Map;

import server.HttpServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class JbdcLoggingHandler extends LoggingHandler {

	protected Request request = new Request();
	protected long startTime;

	public JbdcLoggingHandler(LogLevel info) {
		super(info);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		UnpooledUnsafeDirectByteBuf byteMsg = (UnpooledUnsafeDirectByteBuf) msg;
		request.setReceived_bytes(request.getReceived_bytes()
				+ byteMsg.readableBytes());
		super.channelRead(ctx, msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		ByteBuf byteMsg = (ByteBuf) msg;
		request.setSentBytes((request.getSentBytes() + byteMsg.readableBytes()));
		super.write(ctx, msg, promise);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		this.startTime = System.currentTimeMillis();
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		request.setIp(((InetSocketAddress) ctx.channel().remoteAddress())
				.getAddress().getHostAddress());
		request.setPeriod(new Date());
		request.setTimestamp(System.currentTimeMillis() - this.startTime);
		Map<String, String> uriMap = (Map<String, String>) ctx.channel()
				.attr(HttpServerHandler.URI_KEY).get();

		request.setUri(uriMap.get("uri"));
		request.setRedirectUrl(uriMap.get("redirectUrl"));
		RequestJdbcDao dao = new RequestJdbcDao();
		dao.saveRequest(request);
		super.channelUnregistered(ctx);
	}
}
