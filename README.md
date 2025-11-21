# Kotlin Validator - Spring MVC Example

A working example demonstrating the **[kotlin-validator](https://github.com/noovoweb/kotlin-validator)** library with **Spring Boot MVC**.

## 📚 Full Documentation

**For complete documentation, setup guides, and all features, see:**

👉 **[kotlin-validator Library README](https://github.com/noovoweb/kotlin-validator)**

The library README includes:
- Complete setup instructions
- All validation annotations
- Custom validator creation
- Advanced usage (I/O operations, dispatchers)
- Internationalization (i18n)
- Spring MVC integration guide
- Performance optimization

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Gradle

### Run the Application

```bash
./gradlew bootRun
```

Application starts at: `http://localhost:8080`

### Test the API

#### Using cURL

```bash
# Test email validation
curl -X POST http://localhost:8080/mvc/api/string/email \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com"}'

# Test with French locale
curl -X POST http://localhost:8080/mvc/api/string/email \
  -H "Content-Type: application/json" \
  -H "Accept-Language: fr" \
  -d '{"email":"invalid"}'
```

#### Using Postman

Import the included collection:
- **File:** `Kotlin Validator Spring MVC API.postman_collection.json`
- **Base URL:** `http://localhost:8080`

The collection includes comprehensive test requests for all validators.

## ✨ What's Included

This example demonstrates:

✅ **Built-in validators** organized by category:
- String validation (@Email, @Url, @Pattern, etc.)
- Numeric validation (@Min, @Max, @Between, etc.)
- Date/Time validation (@Future, @Past, @Today, etc.)
- Collection validation (@Size, @Distinct, @NotEmpty, etc.)
- File validation (@MimeType, @FileExtension, etc.)
- Network validation (@IPv4, @IPv6, @MacAddress, etc.)
- Conditional validation (@Same, @RequiredIf, etc.)

✅ **Custom validators** (password strength)

✅ **Internationalization** (English/French via Accept-Language header)

✅ **Nested validation** with @Valid annotation

✅ **Automatic error handling** (422 status codes)

✅ **Traditional Spring MVC patterns**

**API Base Path:** `/mvc/api/*`

## 💡 Usage Example

```kotlin
// 1. Define request with validators
@Validated
data class RegisterRequest(
    @Required @Email
    val email: String?,
    
    @Required @StrongPassword
    val password: String?
)

// 2. Use in controller
@RestController
@RequestMapping("/api")
class UserController(
    private val contextProvider: ValidationContextProvider
) {
    @PostMapping("/register")
    fun register(
        @RequestBody payload: RegisterRequest,
        httpRequest: HttpServletRequest
    ): Map<String, Any> = runBlocking {
        // Validates with i18n support
        RegisterRequestValidator().validate(
            payload, 
            contextProvider.get(httpRequest)
        )
        
        mapOf("message" to "Success", "data" to payload)
    }
}
```

**Validation Error Response:**
```json
{
  "status": 422,
  "message": "Validation Failed",
  "errors": {
    "email": ["Please enter a valid email address"],
    "password": ["Password must be at least 12 characters..."]
  }
}
```

## 🌍 Internationalization

Test with different locales using the `Accept-Language` header:

```bash
# English (default)
curl -X POST http://localhost:8080/mvc/api/scenario/register \
  -H "Content-Type: application/json" \
  -d '{"email":"invalid"}'

# French
curl -X POST http://localhost:8080/mvc/api/scenario/register \
  -H "Content-Type: application/json" \
  -H "Accept-Language: fr" \
  -d '{"email":"invalid"}'
```

## 🔧 Dependencies

```kotlin
dependencies {
    implementation("com.noovoweb:kotlin-validator-annotations:0.1.0-beta.1")
    implementation("com.noovoweb:kotlin-validator-runtime:0.1.0-beta.1")
    implementation("com.noovoweb:kotlin-validator-spring-mvc:0.1.0-beta.1")
    ksp("com.noovoweb:kotlin-validator-processor:0.1.0-beta.1")
}
```

## 📖 Learn More

For detailed documentation on:
- Setting up the library
- Creating custom validators
- Advanced usage patterns
- Performance tuning
- All validator options

**Visit:** 👉 **[kotlin-validator Library README](https://github.com/noovoweb/kotlin-validator)**

## 📝 License

This example project is provided as-is for demonstration purposes.
