# Yooreeka

This is the "official" clone of the Yooreeka project (originally hosted on Google Code). 

---

## 1. Prerequisites Verification

If you have just downloaded the repository, verify that your environment meets the minimum version requirements:

*   **Java Development Kit (JDK 24+)**
    Check your Java version:
    ```bash
    java -version
    ```
    *(Ensure the output shows Java 24 or later)*

*   **Apache Maven**
    Check your Maven installation:
    ```bash
    mvn -version
    ```

---

## 2. Using Maven to Build and Run Tests

The build system has been migrated from Ant to Maven. You can use standard Maven goals for compiling and testing the application.

### Compilation
To compile the project and download all dependencies from Maven Central:
```bash
mvn compile
```

### Running All Tests
To run the entire test suite:
```bash
mvn clean test
```
*(Note: Running this command also executes the custom `clean-tests` execution, which automatically removes transient files created during test executions upon successful completion).*

### Running Individual Chapter Tests
To run tests for a specific chapter individually (saving time and system resources), use the `-Dtest` property:

*   **Chapter 2**:
    ```bash
    mvn test -Dtest=Ch2BeanShellScriptsTest
    ```
*   **Chapter 3**:
    ```bash
    mvn test -Dtest=Ch3BeanShellScriptsTest
    ```
*   **Chapter 4**:
    ```bash
    mvn test -Dtest=Ch4BeanShellScriptsTest
    ```
*   **Chapter 5**:
    ```bash
    mvn test -Dtest=Ch5BeanShellScriptsTest
    ```
*   **Chapter 6**:
    ```bash
    mvn test -Dtest=Ch6BeanShellScriptsTest
    ```
*   **Chapter 7**:
    ```bash
    mvn test -Dtest=Ch7BeanShellScriptsTest
    ```
*   **Playwright Crawler Tests**:
    ```bash
    mvn test -Dtest=PlaywrightTransportTest
    ```
    *(Note: On the first run, Playwright will automatically download and cache its headless browser binaries).*

---

## 3. What's New in v3.0

Version 3.0 represents a complete modernization of the Yooreeka codebase:

*   **JDK 24 Upgrade**: Full compatibility and compilation targeting Java 24 features (using `-release 24`).
*   **Playwright for Java Crawler**: Replaced the legacy purged `HTTPTransport` implementation with a modern browser crawler powered by Microsoft Playwright. It fully supports rendering JavaScript and single-page applications (SPAs) during crawls.
*   **Maven Build Migration**: Replaced the legacy Ant build configuration with a standard `pom.xml`, fetching all libraries from Maven Central.
*   **Decoupled & Eliminated System Scope JARs**: Removed legacy, system-scoped libraries (`jigg`, `rooster`, and local `tm-extractors`). Defunct live Digg API integration has been removed, and `tm-extractors` is now resolved natively from Maven Central.
*   **Upgraded Apache Lucene to 10.5.0**: Upgraded search capabilities to the latest stable Lucene version, refactoring indexers, query builders, and search modules to use modern `StoredFields` and `Builder` APIs. 
*   **Upgraded Drools Engine**: Upgraded Drools dependencies to version `7.73.0.Final` to ensure runtime compatibility with JDK 24.
*   **Automatic Test Cleanup (`clean-tests`)**: Bound a customized execution of the `maven-clean-plugin` to clean up all test-generated crawler folders (`data/ch02/crawl-*`, `data/crawls/`), serialized files, and Lucene indexes automatically upon successful test runs.
*   **Clean Warnings**: Resolved deprecation warnings (e.g. `java.net.URL` constructors, Commons CLI `OptionBuilder`) and enabled optimal Vector API performance using `--add-modules jdk.incubator.vector`.
