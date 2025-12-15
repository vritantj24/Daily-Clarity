# Daily-Clarity News Platform

A real-time news platform built with Spring Boot that displays news articles with automatic updates using Server-Sent Events (SSE). The application fetches news from NewsAPI.org and stores it in PostgreSQL, with a Thymeleaf-based frontend that updates in real-time without page refreshes.

## Features

- 📰 Real-time news updates using Server-Sent Events (SSE)
- 🔄 Automatic frontend updates without page refresh
- 🗄️ PostgreSQL database for news storage
- 🎨 Thymeleaf templating engine for frontend
- 🔑 Secured admin endpoint with Basic Authentication
- ⏰ Automated news fetching every 2 hours via scheduler
- 📡 Integration with NewsAPI.org

## Prerequisites

- Java 21
- PostgreSQL database
- Maven
- NewsAPI.org API key (get it from [newsapi.org](https://newsapi.org))

## Technology Stack

- **Backend**: Spring Boot
- **Database**: PostgreSQL
- **Frontend**: Thymeleaf
- **Real-time Updates**: Server-Sent Events (SSE)
- **External API**: NewsAPI.org
- **Security**: Spring Security (Basic Authentication)
- **Scheduling**: Spring Scheduler

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd <project-directory>
```

### 2. Configure Environment Variables

Create a `.env` file in the root directory of the project with the following configuration:

```env
DB_HOST=<your-database-host>
DB_NAME=<your-database-name>
DB_USER=<your-database-username>
DB_PASS=<your-database-password>
NEWS_API_KEY=<your-newsapi-key>
NEWS_API_URL=https://newsapi.org/v2
ADMIN_USER=<your-admin-username>
ADMIN_PASS=<your-admin-password>
```

**Example `.env` file:**

```env
DB_HOST=localhost
DB_NAME=news_db
DB_USER=postgres
DB_PASS=yourpassword
NEWS_API_KEY=your_api_key_here
NEWS_API_URL=https://newsapi.org/v2
ADMIN_USER=admin
ADMIN_PASS=securepassword123
```

### 3. Set Up PostgreSQL Database

Connect to your PostgreSQL database and run the following SQL command to create the news table:

```sql
CREATE TABLE IF NOT EXISTS news(
    id SERIAL PRIMARY KEY,
    title TEXT,
    description TEXT,
    url TEXT,
    img_url TEXT,
    published_at TIMESTAMPTZ,
    content TEXT,
    source VARCHAR(100),
    author VARCHAR(100),
    category VARCHAR(50)
);
```

### 4. Get NewsAPI.org API Key

1. Visit [newsapi.org](https://newsapi.org)
2. Sign up for a free account
3. Copy your API key
4. Add it to your `.env` file

### 5. Build and Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080` (or your configured port).

## Usage

### Homepage

Navigate to the homepage to view the latest news articles:

```
http://localhost:8080/news/
```

The homepage automatically updates when new news articles are added to the database, thanks to SSE integration.

### Admin Endpoint (Secured)

Use the admin endpoint to manually fetch and generate news articles from NewsAPI.org:

```
POST {base_url}/news/admin/generate
```

**Authentication Required**: This endpoint is protected with Basic Authentication. You must provide the credentials configured in your `.env` file.

**Example using curl:**

```bash
curl -X POST http://localhost:8080/news/admin/generate \
  -u admin:securepassword123
```

**Example using Postman:**
1. Set request type to `POST`
2. Enter URL: `http://localhost:8080/news/admin/generate`
3. Go to Authorization tab
4. Select "Basic Auth"
5. Enter username and password from your `.env` file

This endpoint will:
1. Fetch the latest news from NewsAPI.org
2. Store the articles in the PostgreSQL database
3. Trigger real-time updates on all connected frontend clients via SSE

### Automatic News Updates

The application includes a scheduler that automatically fetches and updates news every 2 hours. This means:

- **Manual Updates**: Use the admin endpoint anytime
- **Automatic Updates**: News refreshes every 2 hours automatically
- **Real-time Display**: All updates (manual or scheduled) instantly appear on connected clients via SSE

You don't need to manually trigger the admin endpoint regularly; the scheduler handles it for you.

## How It Works

### Server-Sent Events (SSE)

The application uses SSE to push updates from the server to the client in real-time. When new news articles are inserted into the database:

1. The backend emits an SSE event
2. The frontend listens for these events
3. The page updates automatically without requiring a manual refresh

This provides a seamless user experience with live news updates.

### Scheduled News Fetching

A Spring Scheduler runs every 2 hours to:
1. Query NewsAPI.org for the latest articles
2. Insert new articles into the PostgreSQL database
3. Broadcast updates to all connected clients via SSE

This ensures your news platform always has fresh content without manual intervention.

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourpackage/
│   │   │       ├── controller/        # REST controllers
│   │   │       ├── model/             # Entity models
│   │   │       ├── repository/        # JPA repositories
│   │   │       ├── service/           # Business logic
│   │   │       ├── config/            # Security & configuration
│   │   │       └── scheduler/         # Scheduled tasks
│   │   └── resources/
│   │       ├── templates/             # Thymeleaf templates
│   │       ├── static/                # CSS, JS, images
│   │       └── application.properties
│   └── test/
├── .env                                # Environment variables
├── pom.xml                             # Maven dependencies
└── README.md
```

## Environment Variables Reference

| Variable | Description | Example |
|----------|-------------|---------|
| `DB_HOST` | PostgreSQL host address | `localhost` or `127.0.0.1` |
| `DB_NAME` | Database name | `news_db` |
| `DB_USER` | Database username | `postgres` |
| `DB_PASS` | Database password | `yourpassword` |
| `NEWS_API_KEY` | NewsAPI.org API key | `abc123def456...` |
| `NEWS_API_URL` | NewsAPI.org base URL | `https://newsapi.org/v2` |
| `ADMIN_USER` | Admin endpoint username | `admin` |
| `ADMIN_PASS` | Admin endpoint password | `securepassword123` |

## Security Considerations

- The admin endpoint is protected with Basic Authentication
- Never commit your `.env` file to version control
- Use strong passwords for `ADMIN_PASS`
- Consider using HTTPS in production
- Rotate credentials periodically
- Keep your NewsAPI.org key confidential

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Verify database credentials in `.env` file
- Check if the database exists
- Confirm the news table has been created

### NewsAPI.org Issues
- Verify your API key is valid
- Check your API usage limits (free tier has restrictions)
- Ensure `NEWS_API_URL` is correct

### SSE Not Working
- Check browser console for JavaScript errors
- Verify the SSE endpoint is accessible
- Ensure CORS is properly configured if accessing from different domains

### Admin Endpoint Access Denied
- Verify `ADMIN_USER` and `ADMIN_PASS` in `.env` file
- Ensure credentials are correctly passed in the request
- Check Spring Security configuration

### Scheduler Not Running
- Verify the scheduler is enabled in your Spring Boot configuration
- Check application logs for scheduler execution messages
- Ensure the application has not thrown exceptions during startup

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
