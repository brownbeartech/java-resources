package tech.brownbear.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractResourceFetcher implements ResourceFetcher {
    protected static Logger logger = LoggerFactory.getLogger(AbstractResourceFetcher.class);
    
    protected final Set<String> directories;

    @FunctionalInterface
    public interface Handler {
        void handle(URL url, Path path);
    }

    public AbstractResourceFetcher(Set<String> directories) {
        this.directories = directories;
        logger.info("Testing Logging : " + directories);
    }

    protected abstract void visitMatchingFiles(String directory, Predicate<Path> filter, Handler handler);

    private List<URL> findMatchingFiles(String directory, Predicate<Path> filter) {
        final List<URL> files = new ArrayList<>();
        visitMatchingFiles(directory, filter, (url, path) -> {
            files.add(url);
        });
        return files;
    }

    private Stream<URL> findInternal(String s) {
        return directories.stream()
            .map(d -> findMatchingFiles(d, match(d, s)))
            .flatMap(List::stream);
    }

    private Predicate<Path> match(String directory, String s) {
        return p -> Paths.get(directory, s).toString().equals(p.toString());
    }

    private Stream<URL> findInternal(Predicate<Path> filter) {
        return directories.stream()
            .map(d -> findMatchingFiles(d, filter))
            .flatMap(List::stream);
    }

    public void visit(Predicate<Path> filter, Handler handler) {
        directories.forEach(d -> visitMatchingFiles(d, filter, handler));
    }

    public List<URL> findAll() {
        return findInternal(p -> true).collect(Collectors.toList());
    }

    public Optional<URL> find() {
        return findInternal(p -> true).findAny();
    }

    public List<URL> findAll(String s) {
        return findInternal(s).collect(Collectors.toList());
    }

    public Optional<URL> find(String s) {
        return findInternal(s).findAny();
    }

    public Optional<URL> find(Predicate<Path> filter) {
        return findInternal(filter).findAny();
    }

    public List<URL> findAll(Predicate<Path> filter) {
        return findInternal(filter).collect(Collectors.toList());
    }
}
