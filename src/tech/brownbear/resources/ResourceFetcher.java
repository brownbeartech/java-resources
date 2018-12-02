package tech.brownbear.resources;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ResourceFetcher {
    List<URL> findAll();

    Optional<URL> find();

    List<URL> findAll(String s);

    Optional<URL> find(String s);

    Optional<URL> find(Predicate<Path> filter);

    List<URL> findAll(Predicate<Path> filter);
}
