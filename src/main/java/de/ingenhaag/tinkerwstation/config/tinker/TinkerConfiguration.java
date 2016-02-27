package de.ingenhaag.tinkerwstation.config.tinker;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tinker", ignoreUnknownFields = false)
public class TinkerConfiguration {

  	private String host = "localhost";
  	private int port = 4223;
	
  	private final Cron cron = new Cron();
  	
  	private final Callback callback = new Callback();
  	
  	public static class Cron {
  		private String saveinterval = "*/5 * * * * *";

		public String getSaveinterval() {
			return saveinterval;
		}

		public void setSaveinterval(String saveinterval) {
			this.saveinterval = saveinterval;
		}
  		
  	}
  	
  	public static class Callback {
  		private int interval = 10000;

		public int getInterval() {
			return interval;
		}

		public void setInterval(int interval) {
			this.interval = interval;
		}	
  	}


	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Callback getCallback() {
		return callback;
	}

	public Cron getCron() {
		return cron;
	}
  	
}
