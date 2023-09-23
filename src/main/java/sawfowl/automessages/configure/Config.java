package sawfowl.automessages.configure;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Config {

	public Config(){}

	@Setting("JsonLocales")
	private boolean jsonLocales = true;
	@Setting("Random")
	private boolean random = false;
	@Setting("UpdateInterval")
	private int updateInterval = 60;

	public boolean isJsonLocales() {
		return jsonLocales;
	}

	public int getUpdateInterval() {
		return updateInterval;
	}

	public boolean isRandom() {
		return random;
	}

}
