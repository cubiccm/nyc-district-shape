# NYC District Shape

Checks if a given coordinate is included in any election district of NYC.

## Run

Not very familiar with Java but this is how I run the program:

Install *Maven* first, on macOS it's
```
brew install maven
```

then

```
mvn clean compile assembly:single
java -jar target/district_shape-1.0-SNAPSHOT-jar-with-dependencies.jar /path/to/NYC_Election_Districts.geojson
```