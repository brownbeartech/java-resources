# Java Resource Management

This package provides utilities for dealing with loading resources inside
of Java programs. Loading files from the classpath can be tricky the
contained classes help to reduce some of those headaches.

## Two ways to fetch resources

There are two ways to load resources, either by getting a List of `URL` objects
or by interacting with the `Path` and `URL` while the connection to the
File System is open.

### Load resource URLs

```java
ClasspathResourceFetcher fetcher = new ClasspathResourceFetcher(
    MyClass.class,
    Collections.singleton("/src/path/to/dir"));
    
// Find all resources in the provided directories
List<URL> resources = fetcher.findAll();

// Apply a predicate to the Path and filter down the results
Predicate<Path> filter = p -> p.getFileName().toString().endsWith(".soy");
resources = fetcher.findAll((filter);

// Find one expected (or optional) matching resource
filter = p -> p.getFileName().toString().equals("file.xml");

Optional<URL> resource = fetcher.find(filter);
```

### Visit the resources

```java
ClasspathResourceFetcher fetcher = new ClasspathResourceFetcher(
    MyClass.class,
    Collections.singleton("/src/path/to/dir"));

Predicate<Path> filter = p -> p.getFileName().toString().endsWith(".soy");

final Map<String, String> files = new HashMap<>();
Handler handler = (url, path) -> {
    byte[] encoded = Files.readAllBytes(path);
    String content = new String(encoded, StandardCharsets.UTF_8);
    files.put(p.getFileName().toString(), content);
};

fetcher.visit(filter, handler);
```

## Note
Resource management is still a bit of a blackbox to me and I find sometimes that I need
to visit the resources to load them properly instead of just dealing with the `URL` objects
as the first method does.