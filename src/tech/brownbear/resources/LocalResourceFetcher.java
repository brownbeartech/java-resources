package tech.brownbear.resources;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalResourceFetcher extends AbstractResourceFetcher {
    private static final String ROOT = Paths.get(PathHelper.getRootPath(), "src").toString();

    public LocalResourceFetcher(Set<String> directories) {
        super(prefixDirectories(directories));
    }

    private static Set<String> prefixDirectories(Set<String> directories) {
        Path root = getPath(ROOT);
        if (root != null) {
            return directories.stream()
                .map(d -> Paths.get(root.toString(), d).toString())
                .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private static URL getResource(String path) {
        try {
            return Paths.get(path).toUri().toURL();
        } catch (MalformedURLException e) {
            logger.error("Bad local resource provided", e);
            return null;
        }
    }

    private static Path getPath(String path) {
        try {
            return Paths.get(path);
        } catch (InvalidPathException e) {
            logger.error("Bad local path provided", e);
            return null;
        }
    }

    @Override
    protected void visitMatchingFiles(String directory, Predicate<Path> filter, Handler handler) {
        try {
            Path dir = getPath(directory);
            if (dir == null) {
                return;
            }
            try (Stream<Path> paths = Files.walk(dir)) {
                paths.filter(filter).forEach(path -> {
                    URL resource = getResource(path.toString());
                    if (resource != null) {
                        handler.handle(resource, path);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}