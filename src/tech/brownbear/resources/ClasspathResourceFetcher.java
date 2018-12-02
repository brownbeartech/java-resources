package tech.brownbear.resources;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ClasspathResourceFetcher extends AbstractResourceFetcher {
    private final Class<?> clazz;

    public ClasspathResourceFetcher(
        Class<?> clazz,
        Set<String> directories) {
        super(directories);
        this.clazz = clazz;
    }

    private URL getResource(String directory) {
        return clazz.getResource(directory);
    }

    @Override
    protected void visitMatchingFiles(String directory, Predicate<Path> filter, Handler handler) {
        try {
            URL dir = getResource(directory);
            if (dir == null) {
                logger.info("Invalid directory '" + directory + "'");
                return;
            }
            synchronized (this) {
                try (FileSystem fs = FileSystems.newFileSystem(dir.toURI(), Collections.emptyMap())) {
                    try (Stream<Path> paths = Files.walk(fs.getPath(directory))) {
                        paths.filter(filter).forEach(path -> {
                            URL resource = getResource(path.toString());
                            handler.handle(resource, path);
                        });
                    }
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}