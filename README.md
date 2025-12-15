# Spring Boot News Platform

A real-time news platform built with Spring Boot that displays news articles with automatic updates using Server-Sent Events (SSE). The application fetches news from NewsAPI.org and stores it in PostgreSQL, with a Thymeleaf-based frontend that updates in real-time without page refreshes.

## Features

- 📰 Real-time news updates using Server-Sent Events (SSE)
- 🔄 Automatic frontend updates without page refresh
- 🗄️ PostgreSQL database for news storage
- 🎨 Thymeleaf templating engine for frontend
- 🔑 Admin endpoint for generating and fetching news
- 📡 Integration with NewsAPI.org

## Prerequisites

- Java 17 or higher
- PostgreSQL database
- Maven
- NewsAPI.org API key (get it from [newsapi.org](https://newsapi.org))

## Technology Stack

- **Backend**: Spring Boot
- **Database**: PostgreSQL
- **Frontend**: Thymeleaf
- **Real-time Updates**: Server-Sent Events (SSE)
- **External API**: NewsAPI.org

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd <project-directory>
```

### 2. Configure Environment Variables

Create a `.env` file in the root directory of the project with the following configuration:

```env
DB_HOST=<your_host>
DB_NAME=<your_db>
DB_USER=<your_user>
DB_PASS=<your_pass>
NEWS_API_KEY=<newsapi.org_key>
NEWS_API_URL=https://newsapi.org/v2
```

**Example `.env` file:**

```env
DB_HOST=localhost
DB_NAME=news_db
DB_USER=postgres
DB_PASS=yourpassword
NEWS_API_KEY=your_api_key_here
NEWS_API_URL=https://newsapi.org/v2
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

### Admin Endpoint

Use the admin endpoint to fetch and generate news articles from NewsAPI.org:

```
POST {base_url}/news/admin/generate
```

**Example:**

```bash
curl -X POST http://localhost:8080/news/admin/generate
```

This endpoint will:
1. Fetch the latest news from NewsAPI.org
2. Store the articles in the PostgreSQL database
3. Trigger real-time updates on all connected frontend clients via SSE

## How It Works

### Server-Sent Events (SSE)

The application uses SSE to push updates from the server to the client in real-time. When new news articles are inserted into the database:

1. The backend emits an SSE event
2. The frontend listens for these events
3. The page updates automatically without requiring a manual refresh

This provides a seamless user experience with live news updates.

## Project Structure

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yourpackage/
│   │   │       ├── controller/     # REST controllers
│   │   │       ├── model/          # Entity models
│   │   │       ├── repository/     # JPA repositories
│   │   │       └── service/        # Business logic
│   │   └── resources/
│   │       ├── templates/          # Thymeleaf templates
│   │       ├── static/             # CSS, JS, images
│   │       └── application.properties
│   └── test/
├── .env                            # Environment variables
├── pom.xml                         # Maven dependencies
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

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

**Note**: Remember to never commit your `.env` file to version control. Add it to `.gitignore` to keep your credentials safe.
