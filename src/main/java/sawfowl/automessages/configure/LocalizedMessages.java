package sawfowl.automessages.configure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.kyori.adventure.text.Component;

public class LocalizedMessages {

	public LocalizedMessages(){}

	public LocalizedMessages(List<Component> list) {
		components = list;
	}

	private List<Component> components = new ArrayList<Component>();
	private int currentNumer = 0;

	public void next() {
		currentNumer = currentNumer >= components.size() -1 ? 0 : currentNumer + 1;
	}

	public void updateRandom(Random random) {
		if(!isEmpty()) currentNumer = random.nextInt(components.size());
	}

	public Component getText() {
		return isEmpty() ? Component.empty() : components.get(currentNumer);
	}

	public boolean isEmpty() {
		return components.isEmpty();
	}

}
