package ar.com.aws.api.util.logging;

import org.apache.cxf.ext.logging.event.LogEvent;
import org.apache.cxf.ext.logging.event.LogEventSender;
import org.apache.cxf.ext.logging.event.LogMessageFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4JEventSender implements LogEventSender {

	protected final Logger messagesLog = LogManager.getLogger("REST_MESSAGES");
	
	@Override
	public void send(LogEvent event) {
		if (isLoggedMessage(event)) {
			messagesLog.info(LogMessageFormatter.format(event));
		}
	}
	
	private boolean isLoggedMessage(LogEvent event) {
		String eventAddress = event.getAddress();
		
		if (eventAddress != null) {
			if (eventAddress.contains("openapi.json") || eventAddress.contains("openapi.yaml")) {
				return false;
			}
			
			if (eventAddress.contains("swagger-ui.css")) {
				return false;
			}
	
			if (eventAddress.contains("swagger-ui-standalone-preset.js")) {
				return false;
			}
	
			if (eventAddress.contains("swagger-ui-bundle.js")) {
				return false;
			}
			
			if (eventAddress.contains("favicon-32x32.png") || eventAddress.contains("favicon-16x16.png")) {
				return false;
			}
		}
		
		if (event.getContentType() != null && (event.getContentType().equals("image/webp") || event.getContentType().equals("image/avif"))) {
			return false;
		}
		
		return true;
	}
}
