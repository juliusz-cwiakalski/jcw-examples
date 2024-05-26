package pl.jcw.example.bddmutation.common;

import com.github.ksuid.Ksuid;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdGenerator {
  public static String nextId() {
    return Ksuid.newKsuid().toString();
  }
}
