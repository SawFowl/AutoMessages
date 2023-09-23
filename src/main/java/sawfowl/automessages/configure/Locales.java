package sawfowl.automessages.configure;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.spongepowered.api.event.Listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import sawfowl.localeapi.api.ConfigTypes;
import sawfowl.localeapi.api.LocaleService;
import sawfowl.localeapi.event.LocaleEvent;
import sawfowl.localeapi.utils.AbstractLocaleUtil;

public class Locales {

	private final LocaleService localeService;
	private final boolean json;
	private String pluginid = "automessages";
	private Map<Locale, LocalizedMessages> localizedMap = new HashMap<Locale, LocalizedMessages>();
	private Random random = new Random();
	public Locales(LocaleService localeService, boolean json) {
		this.localeService = localeService;
		this.json = json;
		localeService.localesExist(pluginid);
		localeService.createPluginLocale(pluginid, ConfigTypes.HOCON, org.spongepowered.api.util.locale.Locales.DEFAULT);
		localeService.createPluginLocale(pluginid, ConfigTypes.HOCON, org.spongepowered.api.util.locale.Locales.RU_RU);
		generateDefault();
		generateRu();
	}

	public LocalizedMessages getLocalizedMessages(Locale locale) {
		return localizedMap.getOrDefault(locale, localizedMap.get(org.spongepowered.api.util.locale.Locales.DEFAULT));
	}

	public void setNext() {
		localizedMap.values().forEach(LocalizedMessages::next);
	}

	public void updateRandom() {
		localizedMap.values().forEach(messages -> messages.updateRandom(random));
	}

	public void reloadMessages() {
		localizedMap.clear();
		localeService.getPluginLocales(pluginid).forEach((k, v) -> localizedMap.put(k, new LocalizedMessages(getListTexts(k, "Messages"))));
	}

	@Listener
	public void onReload(LocaleEvent event) {
		if(!pluginid.equals(event.plugin())) return;
		if(localizedMap.containsKey(event.getLocale())) localizedMap.remove(event.getLocale());
		localizedMap.put(event.getLocale(), new LocalizedMessages(getListTexts(event.getLocale(), "Messages")));
	}

	public LocaleService getLocaleService() {
		return localeService;
	}

	public Component getText(Locale locale, Object... path) {
		return getAbstractLocaleUtil(locale).getComponent(json, path);
	}

	public List<Component> getListTexts(Locale locale, Object... path) {
		return getAbstractLocaleUtil(locale).getListComponents(json, path);
	}

	private void generateDefault() {
		Locale locale = org.spongepowered.api.util.locale.Locales.DEFAULT;
		boolean save = checkList(locale, Arrays.asList(toText("&bDemo clickable message with a link to the developer's discord server").clickEvent(ClickEvent.openUrl("https://discord.gg/7xnZGSYJH9")), toText("&3===========================\n&eA simple message with line breaks.\n&3==========================="), toText("&2Message with pop-up text display on hovering").hoverEvent(HoverEvent.showText(toText("&e&lHello, world!"))), toText("&#0e93d2T&#299fd7h&#44abdce &#5eb7e1g&#79c3e6r&#94cfeba&#afdbf0d&#c9e7f5i&#e4f3fae&#ffffffn&#fae7e7t &#f5cfcfm&#f0b7b7e&#eb9f9fs&#e68787s&#e16f6fa&#dc5757g&#d73f3fe&#d22727."), toText("&#0e93d2&m=&#2190ca&m=&#338cc1&m=&#4689b9&m=&#5885b1&m=&#6b82a8&m=&#7d7ea0&m=&#907b98&m=&#a27790&m=&#b57487&m=&#c7707f&m=&#da6d77&m=&#ec696e&m=&#ff6666&m=&#ec696e&m=&#da6d77&m=&#c7707f&m=&#b57487&m=&#a27790&m=&#907b98&m=&#7d7ea0&m=&#6b82a8&m=&#5885b1&m=&#4689b9&m=&#338cc1&m=&#2190ca&m=&#0e93d2&m=\n&eMessage with a combination of the functionality of other options.\n&#0e93d2&m=&#2190ca&m=&#338cc1&m=&#4689b9&m=&#5885b1&m=&#6b82a8&m=&#7d7ea0&m=&#907b98&m=&#a27790&m=&#b57487&m=&#c7707f&m=&#da6d77&m=&#ec696e&m=&#ff6666&m=&#ec696e&m=&#da6d77&m=&#c7707f&m=&#b57487&m=&#a27790&m=&#907b98&m=&#7d7ea0&m=&#6b82a8&m=&#5885b1&m=&#4689b9&m=&#338cc1&m=&#2190ca&m=&#0e93d2&m=").clickEvent(ClickEvent.openUrl("https://discord.gg/7xnZGSYJH9")).hoverEvent(HoverEvent.showText(toText("&bClick to open the link")))), null, "Messages");
		save = check(locale, toText("&aPlugin reloaded"), null, "ReloadMessage") || save;
		localizedMap.put(locale, new LocalizedMessages(getListTexts(locale, "Messages")));
		if(save) save(locale);
	}

	private void generateRu() {
		Locale locale = org.spongepowered.api.util.locale.Locales.RU_RU;
		boolean save = checkList(locale, Arrays.asList(toText("&bДемонстрационное кликабельное сообщение с ссылкой на дискорд сервер разработчика").clickEvent(ClickEvent.openUrl("https://discord.gg/7xnZGSYJH9")), toText("&3===========================\n&eПростое сообщение с переносом строк.\n&3==========================="), toText("&2Сообщение с отображением всплывающего текста при наведении").hoverEvent(HoverEvent.showText(toText("&e&lПривет мир!"))), toText("&#fb0000С&#fb1a1aо&#fc3333о&#fc4d4dб&#fd6666щ&#fd8080е&#fd9999н&#feb3b3и&#feccccе &#ffe6e6с &#ffffffг&#e7f1f6р&#cee3edа&#b6d5e4д&#9dc7dbи&#85b9d3е&#6dabcaн&#549dc1т&#3c8fb8о&#2381afм&#0b73a6."), toText("&#0e93d2&m=&#2190ca&m=&#338cc1&m=&#4689b9&m=&#5885b1&m=&#6b82a8&m=&#7d7ea0&m=&#907b98&m=&#a27790&m=&#b57487&m=&#c7707f&m=&#da6d77&m=&#ec696e&m=&#ff6666&m=&#ec696e&m=&#da6d77&m=&#c7707f&m=&#b57487&m=&#a27790&m=&#907b98&m=&#7d7ea0&m=&#6b82a8&m=&#5885b1&m=&#4689b9&m=&#338cc1&m=&#2190ca&m=&#0e93d2&m=\n&eСообщение с комбинацией функционала других вариантов.\n&#0e93d2&m=&#2190ca&m=&#338cc1&m=&#4689b9&m=&#5885b1&m=&#6b82a8&m=&#7d7ea0&m=&#907b98&m=&#a27790&m=&#b57487&m=&#c7707f&m=&#da6d77&m=&#ec696e&m=&#ff6666&m=&#ec696e&m=&#da6d77&m=&#c7707f&m=&#b57487&m=&#a27790&m=&#907b98&m=&#7d7ea0&m=&#6b82a8&m=&#5885b1&m=&#4689b9&m=&#338cc1&m=&#2190ca&m=&#0e93d2&m=").clickEvent(ClickEvent.openUrl("https://discord.gg/7xnZGSYJH9")).hoverEvent(HoverEvent.showText(toText("&bКлик для открытия ссылки.")))), null, "Messages");
		save = check(locale, toText("&aПлагин перезагружен"), null, "ReloadMessage");
		localizedMap.put(locale, new LocalizedMessages(getListTexts(locale, "Messages")));
		if(save) save(locale);
	}

	private AbstractLocaleUtil getAbstractLocaleUtil(Locale locale) {
		return localeService.getPluginLocales(pluginid).getOrDefault(locale, localeService.getPluginLocales(pluginid).get(org.spongepowered.api.util.locale.Locales.DEFAULT));
	}

	private Component toText(String string) {
		return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
	}

	private boolean check(Locale locale, Component value, String comment, Object... path) {
		return getAbstractLocaleUtil(locale).checkComponent(json, value, comment, path);
	}

	private boolean checkList(Locale locale, List<Component> value, String comment, Object... path) {
		return getAbstractLocaleUtil(locale).checkListComponents(json, value, comment, path);
	}

	private void save(Locale locale) {
		getAbstractLocaleUtil(locale).saveLocaleNode();
	}

}
