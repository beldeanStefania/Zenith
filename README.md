# Zenith
Collective Project 5th semester UBB

Zenith is a web application that generates playlists based on your current mood. By quickly gathering how you feel (via a simple survey or manual selection), it fetches recommended tracks and creates a personalized Spotify playlist. It also displays a profile page where you can:

- Store a bio and favorite music genres per user (saved via localStorage).
- Upload a profile picture or set a URL image.
- View your generated playlists, each tagged with its corresponding mood.
- Play or preview playlists in Spotify.

The entire setup aims to deliver a streamlined, mood-driven music discovery experience.


## Features
- Authentication: Uses tokens (Bearer <token>) to access protected endpoints (handled by the backend).
- Profile Editing: Users can edit their profile, including bio, favorite genres, and a profile picture/URL, which are saved in localStorage.
- Mood Chart: Displays a line chart (using Recharts) representing the user's mood for each playlist.
- Playlist Management:
  - Fetches user-specific playlists from the backend.
  - Allows playing a playlist via an API call to the Spotify backend integration.
  - Survey: Provides a quick wizard-like survey (optional) for generating new playlists.


## Technology Stack

- **Frontend**
    - [React](https://react.dev/) (TypeScript)
    - [Vite](https://vitejs.dev/) for fast development and bundling
    - [Axios](https://axios-http.com/) for HTTP requests
    - [Recharts](https://recharts.org/) for data visualization
    - CSS Modules or standard CSS for styling
    - LocalStorage for client-side profile data

- **Backend**
    - [Spring Boot](https://spring.io/projects/spring-boot) and Java
    - [Spring Security](https://spring.io/projects/spring-security) for authentication
    - [Spring Data JPA](https://spring.io/projects/spring-data-jpa) for database access
    - [Spotify Web API](https://developer.spotify.com/documentation/web-api/) for fetching music data
    - [MySQL](https://www.mysql.com/) for the database
    - [Lombok](https://projectlombok.org/) for boilerplate code reduction
    - [Maven](https://maven.apache.org/) for dependency management
    - [JUnit 5](https://junit.org/junit5/) for testing
    - [Mockito](https://site.mockito.org/) for mocking

## Installation

Follow these steps to set up the project locally:

### 1. Clone the Repository

```bash
git clone https://github.com/YourUsername/music-moodify-frontend.git
```

### 2. Change Directory

```bash
cd music-moodify-frontend
```

### 3. Install Dependencies

```bash
npm install
```
OR 
```bash
yarn install
```

### 4. Start the Development Server

```bash
npm run dev
```
OR 
```bash
yarn dev
```

By default, the app will be available at http://localhost:5173/ (Vite's default port).