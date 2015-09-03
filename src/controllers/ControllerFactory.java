package controllers;

public class ControllerFactory {

	public static Controller getController(String uri) {
		String redirSyntx = "/redirect?url=";

		if (uri.startsWith("/hello")) {
			return new HelloController();
		}
		if (uri.startsWith(redirSyntx)) {
			return new RedirectController(uri.substring(redirSyntx.length()));
		}
		if (uri.startsWith("/status")) {
			return new StatusController();
		}
		return new IndexConroller();
	}

}
