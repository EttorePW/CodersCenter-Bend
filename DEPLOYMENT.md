# CodersCenter Backend - Render Deployment Guide

## 🚀 Deployment auf Render

Dieses Projekt ist für das Deployment auf [Render.com](https://render.com) mit Docker konfiguriert.

### Voraussetzungen

1. **GitHub Repository**: Lade dein Projekt auf GitHub hoch
2. **Render Account**: Erstelle einen kostenlosen Account auf render.com
3. **Email Provider**: Gmail oder anderer SMTP-Provider für E-Mail-Funktionalität

### Deployment Schritte

#### 1. Repository auf GitHub hochladen

```bash
git remote add origin https://github.com/your-username/CodersCenter-Bend.git
git push -u origin main
```

#### 2. Render Service erstellen

1. Gehe zu [render.com](https://render.com) und logge dich ein
2. Klicke auf "New +" und wähle "Blueprint"
3. Verbinde dein GitHub Repository
4. Render wird automatisch die `render.yaml` Konfiguration erkennen

#### 3. Umgebungsvariablen konfigurieren

Nach dem Deployment musst du folgende Variablen manuell im Render Dashboard setzen:

- `MAIL_USERNAME`: Deine Gmail-Adresse
- `MAIL_PASSWORD`: Gmail App-Password (nicht dein normales Passwort!)
- `CORS_ALLOWED_ORIGINS`: Die URL deines Frontends

#### 4. Gmail App-Password erstellen

1. Gehe zu deinem Google Account
2. Aktiviere 2-Factor Authentication
3. Gehe zu "App passwords" und erstelle ein neues App-Password für "Mail"
4. Verwende dieses Passwort als `MAIL_PASSWORD`

### 🐳 Docker Konfiguration

Das Projekt verwendet einen Multi-Stage Docker Build:

- **Build Stage**: Kompiliert die Anwendung mit Maven
- **Runtime Stage**: Schlankes JRE Image für die Produktion
- **Sicherheit**: Non-root User, Health Checks
- **Optimierung**: Layer Caching für schnellere Builds

### 📊 Monitoring

- **Health Check**: `/actuator/health`
- **Metrics**: `/actuator/metrics` (nur in der Entwicklung)
- **Info**: `/actuator/info`

### 🔧 Lokale Tests

Teste den Docker Build lokal:

```bash
# Build das Image
docker build -t coders-center-backend .

# Run mit Umgebungsvariablen
docker run -p 8080:8080 \\
  -e DATABASE_URL="your-db-url" \\
  -e DATABASE_PASSWORD="your-db-password" \\
  -e JWT_SECRET="your-jwt-secret" \\
  coders-center-backend
```

### 🌍 Produktions-URLs

Nach dem Deployment erhältst du:

- **Backend API**: `https://coders-center-backend.onrender.com`
- **API Docs**: `https://coders-center-backend.onrender.com/swagger-ui.html`
- **Health Check**: `https://coders-center-backend.onrender.com/actuator/health`

### ⚠️ Wichtige Hinweise

1. **Free Tier**: Render's Free Tier schläft nach 15 Min Inaktivität
2. **Cold Starts**: Erste Requests nach dem Aufwachen können langsam sein
3. **Database**: PostgreSQL Database wird automatisch erstellt
4. **Logs**: Verfügbar im Render Dashboard

### 🔄 Updates

Für Updates einfach neuen Code pushen:

```bash
git add .
git commit -m "Update backend"
git push origin main
```

Render deployed automatisch bei jedem Push auf den main Branch.