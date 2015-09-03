package server;

import database.JbdcLoggingHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline chPipeline = ch.pipeline();
		chPipeline.addLast("log", new JbdcLoggingHandler(LogLevel.INFO));
		chPipeline.addLast("codec", new HttpServerCodec());
		chPipeline.addLast("handler", new HttpServerHandler());
	}
}
