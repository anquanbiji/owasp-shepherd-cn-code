package dbProcs;

import java.io.File;

public final class Constants {
	public static final String CATALINA_BASE = System.getProperty("catalina.base");
	public static final String CATALINA_CONF = CATALINA_BASE + File.separator + File.separator + "conf";
	public static final String DBPROP = CATALINA_CONF + File.separator + "database.properties";
	public static final String SETUP_AUTH = CATALINA_CONF + File.separator + "SecurityShepherd.auth";
}
