# Music Harmony App: A JavaFX Application Demonstrating Data Structures

"Music Harmony App" is a feature-rich desktop application developed with **Java** and **JavaFX** to showcase the real-world utility of classic data structures—primarily **Binary Search Trees (BSTs)**—through a robust music management platform. Users can authenticate, manage playlists, play music, and experience efficient data handling, with all information persistently stored in an **SQLite** database.

---

## ✨ Features

- **User Authentication**  
  Register and login securely to manage personal playlists and music libraries.

- **Music Playback**  
  Enjoy music with an integrated player right within the app.

- **Playlist Management**  
  - Create and delete playlists.
  - Add, remove, and search for songs in playlists via an intuitive interface.

- **Song Management**  
  Organize, search for, and manage songs efficiently using custom BST implementations for each playlist.

- **Data Persistence**  
  All user, playlist, and song data are stored in a durable SQLite database (`music_harmony.db`).

- **Interactive GUI**  
  Built on JavaFX for a modern, user-friendly experience.

---

## 🌳 Data Structures Implemented

Central to the app’s logic is a custom **Binary Search Tree (BST)**:
- `BSTNode.java`: Fundamental node definition.
- `PlaylistBST.java`: BST to manage songs in playlists (insert, search, delete in \(O(\log n)\) time for balanced trees).
- Used predominantly in `PlaylistManager.java` for efficient playlist operations.

---

## 🛠️ Technologies Used

- **Java (JDK 11+)**
- **JavaFX:** UI components (bundled with recent JDKs; JARs included in `lib/`).
- **SQLite:** Persistent storage for users, playlists, and songs.
- **SQLite JDBC Driver:** (e.g., `sqlite-jdbc-3.45.3.0 (1).jar`)
- **SLF4J:** Logging backend (`slf4j-api-1.7.32.jar`, `slf4j-simple-1.7.32.jar`).

---

## 📁 Project Structure

```
Data-Structures/
├── .gitignore
├── BSTNode.java
├── DatabaseManager.java
├── LoginPane.java
├── MainPane.java
├── MusicHarmonyApp.java
├── MusicPlayer.java
├── Playlist.java
├── PlaylistBST.java
├── PlaylistManager.java
├── PlaylistPane.java
├── README.md
├── SignupPane.java
├── Song.java
├── User.java
├── UserManager.java
├── music_harmony.db
└── lib/
    ├── javafx-*.jar         # JavaFX modules
    ├── slf4j-api-1.7.32.jar
    ├── slf4j-simple-1.7.32.jar
    ├── sqlite-jdbc-3.45.3.0 (1).jar
    └── ...                  # Other supporting files
```

---

## ⚙️ Setup and Installation

### Prerequisites
- **JDK 11 or newer**
- **IDE:** IntelliJ, Eclipse, or VS Code with Java extensions

### Steps

1. **Clone the Repository**
    ```
    git clone 
    cd Data-Structures
    ```

2. **Configure JavaFX**
    - If not using the bundled JARs (`lib/`), download from [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/).
    - Point your IDE/project module path to the JavaFX SDK JARs (see `lib/`).

3. **Database Setup**
    - Ensure `music_harmony.db` is in the project root (auto-created/managed by `DatabaseManager.java`).

4. **Compile and Run**
   - **From IDE:** Import and run `MusicHarmonyApp.java` as a JavaFX application.
   - **Command Line:**
     ```
     javac --module-path /lib --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.graphics *.java
     java --module-path /lib --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.graphics MusicHarmonyApp
     ```
     *(Replace `/lib` with your JavaFX SDK path.)*

---

## ▶️ Usage

1. **Launch the Application:**  
   Run `MusicHarmonyApp.java` from your IDE or terminal.

2. **Login/Signup:**  
   Use the login interface or register as a new user.

3. **Manage Playlists and Songs:**  
   Create, delete, and organize playlists. Add, remove, or search for songs efficiently.

4. **Enjoy Music Playback:**  
   Select a song and use the built-in player to listen directly within the app.

---

## 💡 Conclusion

The "Music Harmony App" project effectively demonstrates the power and flexibility of **Java** and **JavaFX** for building interactive desktop applications, with a particular focus on the practical application of **data structures**. Through its custom **Binary Search Tree** implementations, the system showcases how organized data management leads to responsive and scalable music management functionalities. Serving as both a functional tool and an educational resource, this application highlights how foundational concepts like BSTs can optimize real-world applications—making them robust, user-friendly, and extensible.

