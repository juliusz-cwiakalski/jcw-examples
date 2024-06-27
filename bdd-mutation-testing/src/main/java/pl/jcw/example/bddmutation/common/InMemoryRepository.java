package pl.jcw.example.bddmutation.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class InMemoryRepository<T, ID> {

  private final ConcurrentHashMap<ID, T> store = new ConcurrentHashMap<>();

  public <S extends T> S save(S entity) {
    ID id = getId(entity);
    store.put(id, entity);
    return entity;
  }

  public Optional<T> findById(ID id) {
    return Optional.ofNullable(store.get(id));
  }

  public boolean existsById(ID id) {
    return store.containsKey(id);
  }

  public List<T> findAll() {
    return new ArrayList<>(store.values());
  }

  public long count() {
    return store.size();
  }

  public void deleteById(ID id) {
    store.remove(id);
  }

  public void delete(T entity) {
    ID id = getId(entity);
    store.remove(id);
  }

  protected Stream<T> findByPredicate(Predicate<T> predicate) {
    return store.values().stream().filter(predicate);
  }

  protected abstract ID getId(T entity);
}
