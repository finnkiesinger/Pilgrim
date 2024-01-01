package EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventBus {
    private final Map<Class<? extends Event>, List<Consumer<Event>>> subscriptions = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends Event> void Subscribe(Class<T> eventType, Consumer<T> listener) {
        subscriptions.putIfAbsent(eventType, new ArrayList<>());
        subscriptions.get(eventType).add((Consumer<Event>) listener);
    }

    public void Emit(Event event) {
        List<Consumer<Event>> eventListeners = subscriptions.get(event.getClass());
        if (eventListeners != null) {
            eventListeners.forEach(listener -> listener.accept(event));
        }
    }
}
