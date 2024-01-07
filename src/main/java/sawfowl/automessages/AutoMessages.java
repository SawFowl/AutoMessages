package sawfowl.automessages;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.scheduler.ScheduledTask;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.reference.ConfigurationReference;
import org.spongepowered.configurate.reference.ValueReference;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import com.google.inject.Inject;

import net.kyori.adventure.audience.Audience;

import sawfowl.automessages.configure.Config;
import sawfowl.automessages.configure.Locales;
import sawfowl.localeapi.api.TextUtils;
import sawfowl.localeapi.api.event.LocaleServiseEvent;
import sawfowl.localeapi.api.serializetools.SerializeOptions;

@Plugin("automessages")
public class AutoMessages {

	private PluginContainer pluginContainer;
	private Path configDir;
	private static Logger logger;
	private ConfigurationReference<CommentedConfigurationNode> mainConfigReference;
	private ValueReference<Config, CommentedConfigurationNode> mainConfig;
	private Locales locales;
	private ScheduledTask scheduledTask;

	public Config getConfig() {
		return mainConfig.get();
	}

	@Inject
	public AutoMessages(PluginContainer pluginContainer, @ConfigDir(sharedRoot = false) Path configDir) {
		logger = LogManager.getLogger("AutoMessages");
		this.pluginContainer = pluginContainer;
		this.configDir = configDir.resolve("Config.conf");
	}

	@Listener
	public void onServerPreInit(LocaleServiseEvent.Construct event) {
		load();
		locales = new Locales(event.getLocaleService(), getConfig().isJsonLocales());
		Sponge.eventManager().registerListeners(pluginContainer, locales);
	}

	@Listener
	public void onCompleteLoad(StartedEngineEvent<Server> event) {
		createTask();
	}

	@Listener
	public void onReload(RefreshGameEvent event) {
		load();
		locales.reloadMessages();
		Optional<ServerPlayer> optPlayer = event.cause().first(ServerPlayer.class);
		if(optPlayer.isPresent()) {
			ServerPlayer player = optPlayer.get();
			player.sendMessage(locales.getText(player.locale(), "ReloadMessage"));
		} else {
			Optional<Audience> optAudience = event.cause().first(Audience.class);
			if(optAudience.isPresent()) {
				optAudience.get().sendMessage(locales.getText(Sponge.systemSubject().locale(), "ReloadMessage"));
			} else logger.info(TextUtils.clearDecorations(locales.getText(Sponge.systemSubject().locale(), "ReloadMessage")));
		}
		createTask();
	}

	private void load() {
		try {
			if(mainConfigReference == null || mainConfig == null) mainConfigReference = SerializeOptions.createHoconConfigurationLoader(2).path(configDir).build().loadToReference();
			mainConfigReference.load();
			mainConfig = mainConfigReference.referenceTo(Config.class);
			if(!configDir.toFile().exists()) mainConfigReference.save();
		} catch (ConfigurateException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	private void createTask() {
		if(scheduledTask != null) {
			scheduledTask.cancel();
			scheduledTask = null;
		}
		scheduledTask = Sponge.asyncScheduler().submit(
			Task.builder().interval(getConfig().getUpdateInterval(), TimeUnit.SECONDS).plugin(pluginContainer).execute(() -> {
				Sponge.server().onlinePlayers().forEach(player -> {
					if(!locales.getLocalizedMessages(player.locale()).isEmpty()) player.sendMessage(locales.getLocalizedMessages(player.locale()).getText());
				});
				if(getConfig().isRandom()) {
					locales.updateRandom();
				} else locales.setNext();
			}).build()
		);
	}

}
