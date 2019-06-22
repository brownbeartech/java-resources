package tech.brownbear.resources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
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
            synchronized (ClasspathResourceFetcher.class) {
                try (FileSystem fs = fs(dir.toURI())) {
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

    private FileSystem fs(URI uri) throws IOException {
        try {
            return FileSystems.newFileSystem(uri, Collections.emptyMap());
        } catch (FileSystemAlreadyExistsException e) {
            return FileSystems.getFileSystem(uri);
        }
    }
}