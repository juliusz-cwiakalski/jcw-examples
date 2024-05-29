package pl.jcw.example.bddmutation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class TestEventConsumer<T> {

  private final List<T> events = new CopyOnWriteArrayList<>();
  private final Class<T> eventType;

  @EventListener
  public void handleEvent(T event) {
    if (event.getClass().isAssignableFrom(eventType)) {
      events.add(event);
    }
  }

  public List<T> findEvents(Predicate<T> predicate) {
    return events.stream().filter(predicate).toList();
  }

  public T findEvent(Predicate<T> predicate) {
    List<T> allMatching = findEvents(predicate);
    if (allMatching.size() != 1) {
      throw new IllegalStateException(
          "Expected exactly one event to match the predicate but found "
              + allMatching.size()
              + " matching events");
    }
    return allMatching.getFirst();
  }

  public List<T> getEvents() {
    return new ArrayList<>(events); // Return a copy to prevent modification
  }
}
