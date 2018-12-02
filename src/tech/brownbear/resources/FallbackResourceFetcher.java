package tech.brownbear.resources;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FallbackResourceFetcher implements ResourceFetcher {
    private final LocalResourceFetcher localResourceFetcher;
    private final ClasspathResourceFetcher classpathResourceFetcher;

    public FallbackResourceFetcher(
        Class<?> clazz,
        Set<String> directories) {
        this.localResourceFetcher = new LocalResourceFetcher(directories);
        this.classpathResourceFetcher = new ClasspathResourceFetcher(clazz, directories);
    }

    @Override
    public List<URL> findAll() {
        return chooseListFetcher(localResourceFetcher::findAll, classpathResourceFetcher::findAll);
    }

    @Override
    public Optional<URL> find() {
        return chooseOptionalFetcher(localResourceFetcher::find, classpathResourceFetcher::find);
    }

    @Override
    public List<URL> findAll(String s) {
        return chooseListFetcher(() -> localResourceFetcher.findAll(s), () -> classpathResourceFetcher.findAll(s));
    }

    @Override
    public Optional<URL> find(String s) {
        return chooseOptionalFetcher(() -> localResourceFetcher.find(s), () -> classpathResourceFetcher.find(s));
    }

    @Override
    public Optional<URL> find(Predicate<Path> filter) {
        return chooseOptionalFetcher(
            () -> localResourceFetcher.find(filter),
            () -> classpathResourceFetcher.find(filter));
    }

    @Override
    public List<URL> findAll(Predicate<Path> filter) {
        return chooseListFetcher(
            () -> localResourceFetcher.findAll(filter),
            () -> classpathResourceFetcher.findAll(filter));
    }

    private Optional<URL> chooseOptionalFetcher(Supplier<Optional<URL>> f1, Supplier<Optional<URL>> f2) {
        Optional<URL> url = f1.get();
        return url.isPresent() ? url : f2.get();
    }

    private List<URL> chooseListFetcher(Supplier<List<URL>> f1, Supplier<List<URL>> f2) {
        List<URL> urls = f1.get();
        return urls.isEmpty() ? f2.get() : urls;
    }
}
