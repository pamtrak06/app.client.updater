package org.app.client.updater.mock;

//import org.apache.wink.client.MockHttpServer;

//public class MockHttpServer implements Container {
//
//	private static Connection connection;
//
//	@Override
//	public void handle(Request request, Response response) {
//		try {
//			PrintStream body = response.getPrintStream();
//			long time = System.currentTimeMillis();
//
//			response.setValue("Content-Type", "text/plain");
//			response.setValue("Server", "HelloWorld/1.0 (Simple 4.0)");
//			response.setDate("Date", time);
//			response.setDate("Last-Modified", time);
//
//			body.println("Hello World");
//			body.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void start(int port) throws IOException {
//		Container container = new MockHttpServer();
//		Server server = new ContainerServer(container);
//		connection = new SocketConnection(server);
//		SocketAddress address = new InetSocketAddress(port);
//		connection.connect(address);
//	}
//
//	public static void stop() throws IOException {
//		connection.close();
//	}
//
//}
public class MockUnitHttpServer {
//	private static MockHttpServer mockServer;
//
//	public static void start(int port) {
//		mockServer = new MockHttpServer(port);
//		mockServer.startServer();
//		String url = "http://localhost:" + mockServer.getServerPort() + "/src/test/resources";
//		MockHttpServer.MockHttpServerResponse response = new MockHttpServer.MockHttpServerResponse();
//		response.setMockResponseContent("some response");
//		response.setMockResponseCode(200);
//		mockServer.setMockHttpServerResponses(response);
//	}
//	
//	public void readRemoteFile(){
//		
//	}
//
//	public static void stop() {
//		mockServer.stopServer();
//
//	}
	
	public static void start(int port) {
		
	}
	public static void stop() {
			
	}
}
