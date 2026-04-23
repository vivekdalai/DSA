# DRY Principle

## Exercise 1: TaxCalculator

Problem: You have three region-specific order processors (USOrderProcessor, EUOrderProcessor, UKOrderProcessor) that
each duplicate the same tax calculation logic.
Your task is to extract a TaxCalculator interface with region-specific implementations, then refactor the order
processors to use it.

```java
// Before: Each processor duplicates tax calculation
interface TaxCalculator {
    double calculateTax(double amount);

    String getRegion();
}

class USTaxCalculator implements TaxCalculator {
    public double calculateTax(double amount) {
        return amount * 0.10;
    }

    public String getRegion() {
        return "US";
    }
}

class EUTaxCalculator implements TaxCalculator {
    public double calculateTax(double amount) {
        return amount * 0.20;
    }

    public String getRegion() {
        return "EU";
    }
}

class UKTaxCalculator implements TaxCalculator {
    public double calculateTax(double amount) {
        return amount * 0.15;
    }

    public String getRegion() {
        return "UK";
    }
}

// TODO: Extract a TaxCalculator interface and region implementations.
// TODO: Refactor OrderProcessor to accept a TaxCalculator.
class OrderProcessor {

    private final TaxCalculator taxCalculator;

    public OrderProcessor(TaxCalculator taxCalculator) {
        this.taxCalculator = taxCalculator;
    }

    void processOrder(double amount) {
        double tax = taxCalculator.calculateTax(amount);
        double total = amount + tax;
        String region = taxCalculator.getRegion();

        System.out.printf("%s Order - Subtotal: $%.2f, Tax: $%.2f, Total: $%.2f", region, amount, tax, total);
        System.out.println();
    }

}

public class Main {
    public static void main(String[] args) {
        // After refactoring, usage should look like:
        OrderProcessor usProcessor = new OrderProcessor(new USTaxCalculator());
        usProcessor.processOrder(100.0);

        OrderProcessor euProcessor = new OrderProcessor(new EUTaxCalculator());
        euProcessor.processOrder(100.0);

        OrderProcessor ukProcessor = new OrderProcessor(new UKTaxCalculator());
        ukProcessor.processOrder(100.0);

    }
}

```

### Exercise 2: ConfigLoader

Refactor ConfigLoader
Problem: Your application loads configuration from three sources: a config file, environment variables, and default
values. Each source currently has its own parsing and validation pipeline, but the pipeline logic (read, parse,
validate, return) is identical. Your task is to eliminate this duplication.

```java

import java.util.List;
import java.util.Map;
import java.util.HashMap;

interface ConfigSource {
    String loadValue(String key);
}

// Before: Each source has its own load-parse-validate pipeline
class FileConfigSource implements ConfigSource {
    public Map<String, String> fileConfig;

    public FileConfigSource(Map<String, String> fileConfig) {
        this.fileConfig = fileConfig;
    }

    public String loadValue(String key) {
        return fileConfig.get(key);
    }

}

class EnvConfigSource implements ConfigSource {
    public String loadValue(String key) {
        return System.getenv(key.replace(".", "_").toUpperCase());
    }
}

class DefaultConfigSource implements ConfigSource {
    Map<String, String> defaults;

    public DefaultConfigSource(Map<String, String> defaults) {
        this.defaults = defaults;
    }

    public String loadValue(String key) {
        return defaults.get(key);
    }
}

class AppConfig {
    private Map<String, String> fileConfig = new HashMap<>();
    private Map<String, String> defaults = new HashMap<>();

    public AppConfig() {
        fileConfig.put("db.host", "localhost");
        fileConfig.put("db.port", "5432");
        defaults.put("db.host", "127.0.0.1");
        defaults.put("db.port", "3306");
        defaults.put("db.timeout", "30");
    }

    public Map<String, String> getFileConfig() {
        return fileConfig;
    }

    public Map<String, String> getDefaults() {
        return defaults;
    }
}

// TODO: Extract a ConfigSource interface.
// TODO: Create a ConfigLoader that tries sources in priority order.
class ConfigLoader {

    private final List<ConfigSource> configSources;

    public ConfigLoader(ConfigSource... sources) {
        this.configSources = List.of(sources);
    }

    public String get(String key) {
        for (ConfigSource source : configSources) {
            String value = source.loadValue(key);
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return null;
    }
}

public class Main {
    public static void main(String[] args) {
        // After refactoring, usage should look like:
        AppConfig appConfig = new AppConfig();
        Map<String, String> fileConfig = appConfig.getFileConfig();
        Map<String, String> defaults = appConfig.getDefaults();
        ConfigLoader loader = new ConfigLoader(
                new FileConfigSource(fileConfig),
                new EnvConfigSource(),
                new DefaultConfigSource(defaults)
        );
        System.out.println("db.host = " + loader.get("db.host"));
        System.out.println("db.port = " + loader.get("db.port"));
        System.out.println("db.timeout = " + loader.get("db.timeout"));
    }
}

```

# KISS Principe --> No Exercise --> Super Easy

# YAGNI Principe --> No Exercise --> Super Easy

# Law od Demeter

### Exercise 1: OrderSummary Printer

Simplify OrderSummary Printer

Problem: You have an OrderSummaryPrinter that uses train wrecks to access nested data. Refactor it so the printer only
calls methods on the Order object directly.

```java
class Address {
    private String city;

    public Address(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }
}

class Customer {
    private Address address;

    public Customer(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }
}

class CreditCard {
    private String last4Digits;

    public CreditCard(String last4Digits) {
        this.last4Digits = last4Digits;
    }

    public String getLast4Digits() {
        return last4Digits;
    }
}

class Payment {
    private CreditCard creditCard;

    public Payment(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }
}

class Order {
    private Customer customer;
    private Payment payment;

    public Order(Customer customer, Payment payment) {
        this.customer = customer;
        this.payment = payment;
    }

    // TODO: Add delegation methods

    public String getCustomerCity() {
        return customer.getAddress().getCity();
    }

    public String getPaymentLast4Digits() {
        return payment.getCreditCard().getLast4Digits();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Payment getPayment() {
        return payment;
    }
}

class OrderSummaryPrinter {
    public void print(Order order) {
        // CURRENT (violates LoD):
        // String city = order.getCustomer().getAddress().getCity();
        // String last4 = order.getPayment().getCreditCard().getLast4Digits();

        // TODO: Refactor to use delegation methods on Order
        String city = order.getCustomerCity(); // replace with order.getCustomerCity()
        String last4 = order.getPaymentLast4Digits(); // replace with order.getPaymentLast4Digits()

        System.out.println("Ship to: " + city);
        System.out.println("Paid with card ending in: " + last4);
    }
}

class Main {
    public static void main(String[] args) {
        Address address = new Address("San Francisco");
        Customer customer = new Customer(address);
        CreditCard card = new CreditCard("4242");
        Payment payment = new Payment(card);
        Order order = new Order(customer, payment);

        OrderSummaryPrinter printer = new OrderSummaryPrinter();
        printer.print(order);
    }
}
```

### Exercise 2: Library Checkout System

Simplify Library Checkout System
Problem: Build a checkout system where a CheckoutService only talks to a Library object, never reaching into shelves,
sections, or books directly.

```java
import java.util.List;
import java.util.ArrayList;

class Book {
    private String title;
    private String author;
    private boolean available;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void markCheckedOut() {
        this.available = false;
    }
}

class Section {
    private String name;
    private List<Book> books;

    public Section(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    // TODO: Add a checkoutBook(String title) method that returns boolean
    // - Search through this section's books for a matching title
    // - If found AND available, mark it as checked out and return true
    // - If not found or already checked out, return false

    public boolean checkoutBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title) && book.isAvailable()) {
                book.markCheckedOut();
                return true;
            }
        }
        return false;
    }
}

class Library {
    private List<Section> sections;

    public Library() {
        this.sections = new ArrayList<>();
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    // TODO: Add a checkoutBook(String title) method that returns boolean
    // - Iterate through all sections and delegate to each section's checkoutBook()
    // - Return true as soon as any section successfully checks out the book
    // - Return false if no section has the book or it's unavailable
    // - CheckoutService should only need to call this single method
    public boolean checkoutBook(String title) {
        for (Section section : sections) {
            if (section.checkoutBook(title)) return true;
        }
        return false;
    }
}

class CheckoutService {
    private Library library;

    public CheckoutService(Library library) {
        this.library = library;
    }

    public boolean checkout(String bookTitle) {
        // TODO: Replace with a single call to library's checkoutBook() method
        // This method should be one line - no direct access to sections or books
        return library.checkoutBook(bookTitle);
    }
}

class Main {
    public static void main(String[] args) {
        Library library = new Library();

        Section fiction = new Section("Fiction");
        fiction.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
        fiction.addBook(new Book("1984", "George Orwell"));
        library.addSection(fiction);

        Section nonFiction = new Section("Non-Fiction");
        nonFiction.addBook(new Book("Clean Code", "Robert C. Martin"));
        library.addSection(nonFiction);

        CheckoutService service = new CheckoutService(library);

        boolean result1 = service.checkout("The Great Gatsby");
        System.out.println("Checking out \"The Great Gatsby\"... " + (result1 ? "Success!" : "Failed (not found)"));

        boolean result2 = service.checkout("The Great Gatsby");
        System.out.println("Checking out \"The Great Gatsby\"... " + (result2 ? "Success!" : "Failed (already checked out)"));

        boolean result3 = service.checkout("Unknown Book");
        System.out.println("Checking out \"Unknown Book\"... " + (result3 ? "Success!" : "Failed (not found)"));
    }
}
```

# Separation of Concerns

### Exercise 1: Refactor UserProfileHandler

A junior developer's monolithic UserProfileHandler mixes HTTP parsing, validation, database queries, business rules, and
logging in a single method. A colleague started separating it into proper layers (UserValidator, UserRepository,
UserProfileService, UserProfileController) but left the method bodies empty.

```java
import java.util.*;

// --- Mock infrastructure (DO NOT modify) ---
class User {
    private String id, name, email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

class Database {
    private static final Map<String, User> byId = new HashMap<>();
    private static final Map<String, User> byEmail = new HashMap<>();

    static void addUser(User u) {
        byId.put(u.getId(), u);
        byEmail.put(u.getEmail(), u);
    }

    static User findUser(String id) {
        return byId.get(id);
    }

    static User findByEmail(String email) {
        return byEmail.get(email);
    }

    static void saveUser(User u) {
        byId.put(u.getId(), u);
        byEmail.put(u.getEmail(), u);
    }
}

class Logger {
    static void log(String msg) {
        System.out.println("[LOG] " + msg);
    }
}

class HttpServletRequest {
    private final Map<String, String> params = new HashMap<>();

    void setParameter(String key, String value) {
        params.put(key, value);
    }

    public String getParameter(String key) {
        return params.get(key);
    }
}

class HttpServletResponse {
    private int status;
    private final StringBuilder body = new StringBuilder();

    public void setStatus(int code) {
        this.status = code;
    }

    public Writer getWriter() {
        return new Writer(body);
    }

    int getStatus() {
        return status;
    }

    String getBody() {
        return body.toString();
    }

    static class Writer {
        private final StringBuilder sb;

        Writer(StringBuilder sb) {
            this.sb = sb;
        }

        public void write(String s) {
            sb.append(s);
        }
    }
}

// --- Reference: Working monolithic handler (DO NOT modify) ---
// Study this to understand what each separated class should do.
class UserProfileHandler {
    public void updateProfile(HttpServletRequest req, HttpServletResponse res) {
        String userId = req.getParameter("userId");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        if (name == null || name.isBlank()) {
            res.setStatus(400);
            res.getWriter().write("{\"error\": \"Name is required\"}");
            return;
        }
        if (email == null || !email.contains("@")) {
            res.setStatus(400);
            res.getWriter().write("{\"error\": \"Valid email is required\"}");
            return;
        }

        User user = Database.findUser(userId);
        if (user == null) {
            res.setStatus(404);
            res.getWriter().write("{\"error\": \"User not found\"}");
            return;
        }

        User existingUser = Database.findByEmail(email);
        if (existingUser != null && !existingUser.getId().equals(userId)) {
            res.setStatus(409);
            res.getWriter().write("{\"error\": \"Email already taken\"}");
            return;
        }

        user.setName(name);
        user.setEmail(email);
        Database.saveUser(user);

        Logger.log("Profile updated for user: " + userId);

        res.setStatus(200);
        res.getWriter().write("{\"message\": \"Profile updated\", \"id\": \""
                + userId + "\"}");
    }
}

// --- Skeleton classes (fill in the TODOs) ---
class UserValidator {
    public void validate(String name, String email) {
        // TODO: If name is null or blank, throw new IllegalArgumentException("Name is required")
        // TODO: If email is null or doesn't contain "@", throw new IllegalArgumentException("Valid email is required")
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
    }
}

class UserRepository {
    public User findById(String id) {
        // TODO: Return Database.findUser(id)
        return Database.findUser(id);
    }

    public User findByEmail(String email) {
        // TODO: Return Database.findByEmail(email)
        return Database.findByEmail(email);
    }

    public void save(User user) {
        // TODO: Call Database.saveUser(user)
        Database.saveUser(user);
    }
}

class UserProfileService {
    private final UserRepository repository;
    private final UserValidator validator;

    public UserProfileService(UserRepository repository, UserValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public void updateProfile(String userId, String name, String email) {
        // TODO: Call validator.validate(name, email)
        validator.validate(name, email);


        // TODO: Look up user via repository.findById(userId)
        //       If null, throw new RuntimeException("User not found")
        User user = repository.findById(userId);
        if (user == null) throw new RuntimeException("User not found");


        // TODO: Check email uniqueness via repository.findByEmail(email)
        //       If taken by a different user, throw new RuntimeException("Email already taken")
        User otherUser = repository.findByEmail(email);
        if (otherUser != null && !otherUser.getId().equals(user)) throw new RuntimeException("Email already taken");


        // TODO: Update user's name and email, call repository.save(user)
        repository.save(user);


        // TODO: Call Logger.log("Profile updated for user: " + userId)
        Logger.log("Profile updated for user: " + userId);
    }
}

class UserProfileController {
    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    public void handleUpdate(HttpServletRequest req, HttpServletResponse res) {
        // TODO: Parse userId, name, email from request parameters
        String userId = req.getParameter("userId");
        String name = req.getParameter("name");
        String email = req.getParameter("email");


        // TODO: Try calling service.updateProfile(userId, name, email)
        // TODO: On success → set status 200, write {"message": "Profile updated", "id": "<userId>"}
        // TODO: Catch IllegalArgumentException → set status 400, write {"error": "<message>"}
        // TODO: Catch RuntimeException("User not found") → set status 404, write {"error": "<message>"}
        // TODO: Catch RuntimeException("Email already taken") → set status 409, write {"error": "<message>"}

        try {
            service.updateProfile(userId, name, email);
            res.setStatus(200);
            res.getWriter().write("{\"message\": \"Profile updated\", \"id\": \"" + userId + "\"}");

        } catch (IllegalArgumentException e) {
            res.setStatus(400);
            res.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");

        } catch (RuntimeException e) {
            if (e.getMessage().equals("User not found")) {
                res.setStatus(404);
            } else if (e.getMessage().equals("Email already taken")) {
                res.setStatus(409);
            }
            res.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }

    }
}

// --- Tests (DO NOT modify) ---
public class Main {
    public static void main(String[] args) {
        Database.addUser(new User("u1", "Alice", "alice@example.com"));
        Database.addUser(new User("u2", "Bob", "bob@example.com"));

        UserRepository repository = new UserRepository();
        UserValidator validator = new UserValidator();
        UserProfileService service = new UserProfileService(repository, validator);
        UserProfileController controller = new UserProfileController(service);

        // Test 1: Successful update
        HttpServletRequest req1 = new HttpServletRequest();
        req1.setParameter("userId", "u1");
        req1.setParameter("name", "Alice Smith");
        req1.setParameter("email", "alice.smith@example.com");
        HttpServletResponse res1 = new HttpServletResponse();
        controller.handleUpdate(req1, res1);
        System.out.println("Test 1 - Status: " + res1.getStatus()
                + ", Response: " + res1.getBody());

        // Test 2: Empty name (validation failure)
        HttpServletRequest req2 = new HttpServletRequest();
        req2.setParameter("userId", "u1");
        req2.setParameter("name", "");
        req2.setParameter("email", "test@example.com");
        HttpServletResponse res2 = new HttpServletResponse();
        controller.handleUpdate(req2, res2);
        System.out.println("Test 2 - Status: " + res2.getStatus()
                + ", Response: " + res2.getBody());

        // Test 3: User not found
        HttpServletRequest req3 = new HttpServletRequest();
        req3.setParameter("userId", "u99");
        req3.setParameter("name", "Ghost");
        req3.setParameter("email", "ghost@example.com");
        HttpServletResponse res3 = new HttpServletResponse();
        controller.handleUpdate(req3, res3);
        System.out.println("Test 3 - Status: " + res3.getStatus()
                + ", Response: " + res3.getBody());

        // Test 4: Email already taken
        HttpServletRequest req4 = new HttpServletRequest();
        req4.setParameter("userId", "u1");
        req4.setParameter("name", "Alice");
        req4.setParameter("email", "bob@example.com");
        HttpServletResponse res4 = new HttpServletResponse();
        controller.handleUpdate(req4, res4);
        System.out.println("Test 4 - Status: " + res4.getStatus()
                + ", Response: " + res4.getBody());
    }
}
```

### Exercise 2: Build a ReportGenerator

Build a ReportGenerator
A report generation system has been designed with properly separated concerns: DataFetcher, ReportProcessor,
ReportFormatter, ReportDeliverer, and a ReportGenerator orchestrator. The interfaces and some implementations are
provided, but SalesReportProcessor, CsvReportFormatter, and ReportGenerator have empty TODO method bodies.

```java
import java.util.*;
import java.util.stream.*;

// --- Data types ---
class SalesRecord {
    private final String product;
    private final double amount;
    private final String date;

    public SalesRecord(String product, double amount, String date) {
        this.product = product;
        this.amount = amount;
        this.date = date;
    }

    public String product() {
        return product;
    }

    public double amount() {
        return amount;
    }

    public String date() {
        return date;
    }
}

class ReportData {
    private final double totalSales;
    private final double averageSale;
    private final String topProduct;
    private final int recordCount;

    public ReportData(double totalSales, double averageSale,
                      String topProduct, int recordCount) {
        this.totalSales = totalSales;
        this.averageSale = averageSale;
        this.topProduct = topProduct;
        this.recordCount = recordCount;
    }

    public double totalSales() {
        return totalSales;
    }

    public double averageSale() {
        return averageSale;
    }

    public String topProduct() {
        return topProduct;
    }

    public int recordCount() {
        return recordCount;
    }
}

// --- Interfaces: implement these ---
interface DataFetcher {
    List<SalesRecord> fetch();
}

interface ReportProcessor {
    ReportData process(List<SalesRecord> records);
}

interface ReportFormatter {
    String format(ReportData data);
}

interface ReportDeliverer {
    void deliver(String formattedReport);
}

// --- Provided implementations (DO NOT modify) ---
class InMemoryDataFetcher implements DataFetcher {
    private final List<SalesRecord> data;

    public InMemoryDataFetcher(List<SalesRecord> data) {
        this.data = data;
    }

    @Override
    public List<SalesRecord> fetch() {
        return data;
    }
}

class ConsoleReportDeliverer implements ReportDeliverer {
    @Override
    public void deliver(String formattedReport) {
        System.out.println(formattedReport);
    }
}

// --- Skeleton classes (fill in the TODOs) ---
class SalesReportProcessor implements ReportProcessor {
    @Override
    public ReportData process(List<SalesRecord> records) {
        // TODO: Calculate totalSales as the sum of all record amounts
        // TODO: Find topProduct (the product with the highest total amount across all its records)
        // TODO: Calculate averageSale as totalSales / records.size()
        // TODO: Return new ReportData(totalSales, averageSale, topProduct, records.size())


        double totalSales = 0;
        String topProduct = "";
        double maxAmount = Double.MIN_VALUE;
        for (SalesRecord salesRecord : records) {
            totalSales += salesRecord.amount();

            if (maxAmount < salesRecord.amount()) {
                maxAmount = salesRecord.amount();
                topProduct = salesRecord.product();
            }
        }
        double averageSale = totalSales / records.size();

        return new ReportData(totalSales, averageSale, topProduct, records.size());
    }
}

class CsvReportFormatter implements ReportFormatter {
    @Override
    public String format(ReportData data) {
        // TODO: Return a CSV string with format:
        // "Sales Report\nTotal Sales,$<totalSales formatted as %.2f>\nAverage Sale,$<averageSale formatted as %.2f>\nTop Product,<topProduct>\nRecord Count,<recordCount>"
        return String.format("Sales Report\nTotal Sales,$%.2f\nAverage Sale,$%.2f\nTop Product,%s\nRecord Count,%s", data.totalSales(), data.averageSale(), data.topProduct(), data.recordCount());
    }
}

class ReportGenerator {
    private final DataFetcher fetcher;
    private final ReportProcessor processor;
    private final ReportFormatter formatter;
    private final ReportDeliverer deliverer;

    public ReportGenerator(DataFetcher fetcher, ReportProcessor processor,
                           ReportFormatter formatter, ReportDeliverer deliverer) {
        this.fetcher = fetcher;
        this.processor = processor;
        this.formatter = formatter;
        this.deliverer = deliverer;
    }

    public void generate() {
        // TODO: Fetch records using fetcher.fetch()
        // TODO: Process records into ReportData using processor.process()
        // TODO: Format ReportData into a string using formatter.format()
        // TODO: Deliver the formatted string using deliverer.deliver()
        List<SalesRecord> records = fetcher.fetch();
        ReportData reportData = processor.process(records);
        String formattedData = formatter.format(reportData);
        deliverer.deliver(formattedData);

    }
}

// --- Tests ---
public class Main {
    public static void main(String[] args) {
        List<SalesRecord> testData = List.of(
                new SalesRecord("Widget Pro", 500.0, "2024-01-15"),
                new SalesRecord("Widget Pro", 750.0, "2024-01-16"),
                new SalesRecord("Gadget X", 200.0, "2024-01-17"),
                new SalesRecord("Widget Pro", 1200.0, "2024-01-18"),
                new SalesRecord("Gadget X", 350.0, "2024-01-19")
        );

        DataFetcher fetcher = new InMemoryDataFetcher(testData);
        ReportProcessor processor = new SalesReportProcessor();
        ReportFormatter formatter = new CsvReportFormatter();
        ReportDeliverer deliverer = new ConsoleReportDeliverer();

        ReportGenerator generator = new ReportGenerator(
                fetcher, processor, formatter, deliverer);
        generator.generate();
    }
}
```

# Coupling and Cohesion

### Exercise 1: Identify and Fix Low Cohesion
Identify and Fix Low Cohesion
Problem: You have a UserManager class that handles four unrelated responsibilities: creating users, calculating subscription fees, formatting addresses, and logging activity. It works, but it violates cohesion because those responsibilities have nothing to do with each other. Your job is to split UserManager into four focused classes.

```java
import java.util.*;

class User {
    private String name;
    private String email;
    private String tier;
    private String street;
    private String city;
    private String zip;

    public User(String name, String email, String tier,
                String street, String city, String zip) {
        this.name = name;
        this.email = email;
        this.tier = tier;
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getTier() { return tier; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getZip() { return zip; }
}

// --- Reference: low-cohesion class (extract logic from here) ---
class UserManager {
    public User createUser(String name, String email, String tier,
                           String street, String city, String zip) {
        User user = new User(name, email, tier, street, city, zip);
        System.out.println("Created user: " + user.getName()
                + " (" + user.getEmail() + ")");
        return user;
    }

    public double calculateSubscriptionFee(User user) {
        switch (user.getTier()) {
            case "PREMIUM": return 19.99;
            case "BASIC":   return 9.99;
            default:        return 0.00;
        }
    }

    public String formatAddress(User user) {
        return user.getStreet() + ", " + user.getCity()
                + ", " + user.getZip();
    }

    public void logActivity(User user, String action) {
        System.out.println("Activity: " + user.getName()
                + " - " + action);
    }
}

// --- Refactored classes (fill in the TODOs) ---
class UserService {
    public User createUser(String name, String email, String tier,
                           String street, String city, String zip) {
        // TODO: Move the createUser() logic from UserManager here
        User user = new User(name, email, tier, street, city, zip);
        System.out.println("Created user: " + user.getName()
                + " (" + user.getEmail() + ")");
        return user;
    }
}

class BillingService {
    public double calculateSubscriptionFee(User user) {
        // TODO: Move the calculateSubscriptionFee() logic from UserManager here
        switch (user.getTier()) {
            case "PREMIUM": return 19.99;
            case "BASIC":   return 9.99;
            default:        return 0.00;
        }
    }
}

class AddressFormatter {
    public String formatAddress(User user) {
        // TODO: Move the formatAddress() logic from UserManager here
        return user.getStreet() + ", " + user.getCity()
                + ", " + user.getZip();
    }
}

class ActivityLogger {
    public void logActivity(User user, String action) {
        // TODO: Move the logActivity() logic from UserManager here
        System.out.println("Activity: " + user.getName()
                + " - " + action);
    }
}

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        BillingService billingService = new BillingService();
        AddressFormatter addressFormatter = new AddressFormatter();
        ActivityLogger activityLogger = new ActivityLogger();

        User alice = userService.createUser("Alice", "alice@example.com",
                "PREMIUM", "123 Main St", "Springfield", "62704");

        double fee = billingService.calculateSubscriptionFee(alice);
        System.out.printf("Subscription fee for %s user: $%.2f%n",
                alice.getTier(), fee);

        String address = addressFormatter.formatAddress(alice);
        System.out.println("Address: " + address);

        activityLogger.logActivity(alice, "logged in");
    }
}
```

### Exercise 2: Reduce Coupling
Reduce Coupling
Problem: A tightly coupled NotificationService used to directly create EmailSender, SmsSender, and PushSender inside its notifyUser method. The structure has been refactored to use a MessageSender interface and dependency injection, but the method bodies are empty. Your job is to fill in the TODOs so each sender prints the correct output.


```java
import java.util.*;

interface MessageSender {
    void send(String userId, String message);
}

class EmailSender implements MessageSender {
    @Override
    public void send(String userId, String message) {
        // TODO: Print "[Email] Sent to <userId>@company.com: <message>"
        System.out.println("[Email] Sent to " + userId + "@company.com: " + message);
    }
}

class SmsSender implements MessageSender {
    @Override
    public void send(String userId, String message) {
        // TODO: Print "[SMS] Sent to +1234567890: <message>"
        System.out.println("[SMS] Sent to +1234567890: " + message);
    }
}

class PushSender implements MessageSender {
    @Override
    public void send(String userId, String message) {
        // TODO: Print "[Push] Sent to <userId>: <message>"
        System.out.println("[Push] Sent to " + userId + ": " + message);
    }
}

class NotificationService {
    private final List<MessageSender> senders;

    public NotificationService(List<MessageSender> senders) {
        this.senders = senders;
    }

    public void notifyUser(String userId, String message) {
        // TODO: Loop through this.senders and call send(userId, message) on each
        for (MessageSender sender : this.senders) {
            sender.send(userId, message);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        List<MessageSender> senders = List.of(
                new EmailSender(),
                new SmsSender(),
                new PushSender()
        );
        NotificationService service = new NotificationService(senders);
        service.notifyUser("user42", "Your order has been shipped");
    }
}
```

# Composing Objects Principle
### Exercise 1: NotificationBuilder
Design NotificationBuilder
Problem: You have inherited a notification system with a combinatorial explosion of classes: EmailNotification, UrgentEmailNotification, ScheduledSmsNotification, UrgentScheduledEmailNotification, and so on. The system has been refactored to use composition with a DeliveryChannel, Priority, and Schedule, but the method bodies are empty. Your job is to fill in the TODOs so each component prints the correct output.

```java
enum Priority { LOW, NORMAL, URGENT }

interface DeliveryChannel {
    void deliver(String priority, String message);
}

interface Schedule {
    void apply(String priority);
}

class EmailChannel implements DeliveryChannel {
    @Override
    public void deliver(String priority, String message) {
        // TODO: Print "[<priority>] Sending via Email: <message>"
        System.out.println("[" + priority + "] Sending via Email: " + message);
    }
}

class SmsChannel implements DeliveryChannel {
    @Override
    public void deliver(String priority, String message) {
        // TODO: Print "[<priority>] Sending via SMS: <message>"
        System.out.println("[" + priority + "] Sending via SMS: " + message);
    }
}

class PushChannel implements DeliveryChannel {
    @Override
    public void deliver(String priority, String message) {
        // TODO: Print "[<priority>] Sending via Push Notification: <message>"
        System.out.println("[" + priority + "] Sending via Push Notification: " + message);
    }
}

class ImmediateSchedule implements Schedule {
    @Override
    public void apply(String priority) {
        // TODO: Print "[<priority>] Scheduling: sending immediately"
        System.out.println("[" + priority + "] Scheduling: sending immediately");
    }
}

class DelayedSchedule implements Schedule {
    private final int minutes;
    public DelayedSchedule(int minutes) { this.minutes = minutes; }

    @Override
    public void apply(String priority) {
        // TODO: Print "[<priority>] Scheduling: delayed by <minutes> minutes"
        System.out.println("[" + priority + "] Scheduling: delayed by " + this.minutes + " minutes");
    }
}

class Notification {
    private final DeliveryChannel channel;
    private final Priority priority;
    private final Schedule schedule;

    public Notification(DeliveryChannel channel, Priority priority, Schedule schedule) {
        this.channel = channel;
        this.priority = priority;
        this.schedule = schedule;
    }

    public void send(String message) {
        // TODO: Call schedule.apply() with priority.name(), then
        //       call channel.deliver() with priority.name() and message
        schedule.apply(priority.name());
        channel.deliver(priority.name(), message);
    }
}

public class Main {
    public static void main(String[] args) {
        Notification urgentEmail = new Notification(
                new EmailChannel(), Priority.URGENT, new ImmediateSchedule());
        urgentEmail.send("Server is down!");

        System.out.println();

        Notification scheduledSms = new Notification(
                new SmsChannel(), Priority.LOW, new DelayedSchedule(30));
        scheduledSms.send("Weekly report is ready.");

        System.out.println();

        Notification pushNotif = new Notification(
                new PushChannel(), Priority.NORMAL, new ImmediateSchedule());
        pushNotif.send("You have a new follower.");
    }
}
```

### Exercise 2: CharacterCreator
Design CharacterCreator
Problem: You are building a role-playing game. Instead of creating classes like SwordKnightWithFireball and BowRogueWithInvisibility, the system uses composition: a Character composes a Weapon, an Armor, and an Ability. The interfaces, skeleton implementations, and Character class are provided, but the method bodies are empty. Your job is to fill in the TODOs so each component returns the correct description and the Character delegates to its composed objects.

```java
interface Weapon {
    String attack();
}

interface Armor {
    String defend();
}

interface Ability {
    String useAbility();
}

class SwordWeapon implements Weapon {
    public String attack() {
        // TODO: Return "attacks with a sword: Slash!"
        return "attacks with a sword: Slash!";
    }
}

class BowWeapon implements Weapon {
    public String attack() {
        // TODO: Return "attacks with a bow: Arrow shot!"
        return "attacks with a bow: Arrow shot!";
    }
}

class HeavyArmor implements Armor {
    public String defend() {
        // TODO: Return "defends with heavy armor: Blocks 80% damage"
        return "defends with heavy armor: Blocks 80% damage";
    }
}

class FireballAbility implements Ability {
    public String useAbility() {
        // TODO: Return "uses ability: Fireball! Deals 50 fire damage"
        return "uses ability: Fireball! Deals 50 fire damage";
    }
}

class Character {
    private String name;
    private Weapon weapon;
    private Armor armor;
    private Ability ability;

    public Character(String name, Weapon weapon, Armor armor, Ability ability) {
        this.name = name;
        this.weapon = weapon;
        this.armor = armor;
        this.ability = ability;
    }

    public void setWeapon(Weapon weapon) { this.weapon = weapon; }

    public void attack() {
        // TODO: Print "<name> <weapon.attack()>"
        System.out.println(name + " " + weapon.attack());
    }

    public void defend() {
        // TODO: Print "<name> <armor.defend()>"
        System.out.println(name + " " + armor.defend());
    }

    public void useAbility() {
        // TODO: Print "<name> <ability.useAbility()>"
        System.out.println(name + " " + ability.useAbility());
    }
}

public class Main {
    public static void main(String[] args) {
        Character knight = new Character("Arthur",
                new SwordWeapon(), new HeavyArmor(), new FireballAbility());
        knight.attack();
        knight.defend();
        knight.useAbility();

        System.out.println();
        System.out.println("Arthur finds a magic bow!");
        knight.setWeapon(new BowWeapon());
        knight.attack();
    }
}
```






















