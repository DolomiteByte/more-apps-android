# DolomiteByte More Apps

[English](README.md) · Deutsch

Eine gemeinsame Jetpack-Compose-Material-3-Seite, auf der Nutzer weitere Android-Apps von DolomiteByte entdecken. Katalog, Texte, Icons und die Weiterleitung zu Google Play liegen in diesem Repository. Jede Host-App bestimmt selbst den Menüort und liefert ihr eigenes Theme.

Die Bibliothek wird als **Quellcode-Modul** in Pubdash, TookAction, Fileco, Everscan und TheraBuddy eingebunden. Ein Maven-Artefakt wird derzeit nicht veröffentlicht.

| Dunkles Theme · TheraBuddy | Helles Theme · Pubdash |
| :---: | :---: |
| <img src="docs/images/therabuddy-dark.png" alt="Weitere Apps in TheraBuddy mit dunklem Theme" width="260"> | <img src="docs/images/pubdash-light.png" alt="Weitere Apps in Pubdash mit hellem Theme" width="260"> |

## Funktionsumfang

- Ein fester Katalog eigener Apps. Die gerade verwendete App bleibt ausgeblendet, auch bei Varianten mit einem Zusatz wie `.debug` in der Application-ID.
- Angezeigt werden nur Einträge mit `publishedOnPlay = true`. TookAction ist im Katalog enthalten, erscheint derzeit aber nicht in den anderen Apps.
- Ein Google-Play-Eintrag wird **erst nach einem Tipp** geöffnet. Die Bibliothek versucht zuerst die Play-Store-App und nutzt sonst eine App für den HTTPS-Link, etwa den Browser.
- Das Material-3-Theme kommt von der Host-App. Texte liegen auf Englisch und Deutsch vor; für andere Sprachen gelten die englischen Standardressourcen.
- Katalog und Icons liegen lokal. Die Bibliothek fordert keine Berechtigungen an, lädt keinen Katalog nach, erfasst keine Klicks und installiert keine Apps.

## Voraussetzungen

| Voraussetzung | Wert |
| --- | --- |
| Minimale Android-API | 26 |
| Compile SDK der Host-App | 36 oder höher |
| UI | Jetpack Compose mit Material 3 |
| Eigenständiger Build | Gradle Wrapper 9.7.1, Android Gradle Plugin 9.3.2, Kotlin-Compose-Plugin 2.4.10 |

Navigation und Theme bleiben in der Verantwortung der Host-App. Die genannten Plugin-Versionen beschreiben den getesteten eigenständigen Build dieser Bibliothek; die Host-App benötigt eine dazu kompatible Android- und Compose-Konfiguration.

## In eine Android-App einbinden

Die fünf DolomiteByte-Apps binden einen geprüften Commit dieses Repositories als Git-Submodul unter `shared/more-apps` ein.

### 1. Submodul hinzufügen oder auschecken

Für eine neue Host-App:

```sh
git submodule add https://github.com/DolomiteByte/more-apps-android.git shared/more-apps
```

Nach dem Klonen einer bereits angebundenen App:

```sh
git submodule update --init --recursive
```

Bei GitHub Actions muss `actions/checkout` mit `submodules: true` laufen. Das öffentliche Submodul benötigt dafür kein zusätzliches Zugriffstoken für ein privates Repository.

### 2. Gradle-Modul registrieren

In `settings.gradle.kts` der Host-App:

```kotlin
include(":moreapps")
project(":moreapps").projectDir = file("shared/more-apps")
```

In `app/build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":moreapps"))
}
```

Der Root-Build muss `com.android.library` und `org.jetbrains.kotlin.plugin.compose` für das Modul bereitstellen. Falls bisher nur `com.android.application` deklariert ist, `com.android.library` mit `apply false` ergänzen.

### 3. Seite öffnen

`MoreAppsScreen` stellt eine eigene Top-App-Bar samt Zurück-Schaltfläche bereit:

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.dolomitebyte.moreapps.MoreAppsScreen

@Composable
fun MoreAppsDestination(onBack: () -> Unit) {
    val context = LocalContext.current

    AppTheme {
        MoreAppsScreen(
            hostPackageName = context.packageName,
            onBack = onBack,
        )
    }
}
```

`AppTheme` steht hier für das Material-3-Theme der Host-App. Übergib die **Application-ID zur Laufzeit** (`context.packageName` oder `BuildConfig.APPLICATION_ID`), damit auch Debug- und QA-Varianten ihre eigene App ausblenden.

Wenn die Host-App Top-App-Bar und Zurück-Navigation selbst bereitstellt, verwende `MoreAppsContent`. Der Menüeintrag **Weitere Apps** steht im Seitenmenü unter Einstellungen oder, ohne Seitenmenü, in den Einstellungen. Sein lokalisierter Text ist `com.dolomitebyte.moreapps.R.string.more_apps_title`.

| Host-App | Einstieg | Ansicht |
| --- | --- | --- |
| Pubdash | Seitenmenü unter Einstellungen | `MoreAppsScreen` |
| TookAction | Seitenmenü unter Einstellungen | `MoreAppsContent` im vorhandenen Scaffold |
| Fileco | Einstellungen | `MoreAppsScreen` |
| Everscan | Seitenmenü unter Einstellungen | `MoreAppsScreen` |
| TheraBuddy | Seitenmenü unter Einstellungen | `MoreAppsScreen` |

## API

| API | Zweck |
| --- | --- |
| `MoreAppsScreen(hostPackageName, onBack, modifier, onOpenApp)` | Vollständige Material-3-Seite mit Top-App-Bar. |
| `MoreAppsContent(hostPackageName, modifier, onOpenApp)` | Kataloginhalt für ein vorhandenes Scaffold. |
| `moreAppsFor(hostPackageName)` | Sichtbare Einträge ohne die Host-App und deren Varianten. |
| `openGooglePlayListing(context, app)` | `ACTION_VIEW` für die Play-Store-App mit HTTPS-Fallback. |
| `DolomiteApp` | Freigegebene Paketnamen, Texte, Icons und Veröffentlichungsflags. |

Der optionale Callback `onOpenApp` ersetzt das standardmäßige Öffnen des Stores. Die Host-App kann damit eigene Fehlerbehandlung ergänzen. `openGooglePlayListing` liefert `true`, wenn eine App den Intent angenommen hat; das bestätigt weder das Laden des Eintrags noch eine Installation.

## Katalog pflegen

| App | Application-ID | In anderen Apps sichtbar |
| --- | --- | --- |
| Pubdash | `com.dolomitebyte.pubdash` | Ja |
| TookAction | `com.dolomitebyte.tookaction` | Nein — `publishedOnPlay = false` |
| Fileco | `com.dolomitebyte.fileco` | Ja |
| Everscan | `com.dolomitebyte.everscan` | Ja |
| TheraBuddy | `com.dolomitebyte.therabuddy` | Ja |

Der Katalog ist eine feste Liste, keine automatische Play-Store-Suche. Für einen neuen oder bisher ausgeblendeten Eintrag:

1. Prüfen, ob der öffentliche Google-Play-Eintrag mit der richtigen Application-ID für die Zielgruppe erreichbar ist.
2. `DolomiteApp`, englische und deutsche Texte sowie Icon aktualisieren. `publishedOnPlay = true` erst nach dieser Prüfung setzen.
3. Unit-Tests und Debug-Build ausführen. Sichtbarkeit in einer anderen App und das Ausblenden der eigenen App prüfen.
4. Den neuen Bibliotheks-Commit prüfen, dann den gepinnten Submodul-Commit in jeder Host-App aktualisieren und diese Apps bauen.

## Google-Play-Richtlinien

Google nennt in der [Anleitung zu App-Inhalten](https://support.google.com/googleplay/android-developer/answer/9859455?hl=de) einen Menüpunkt **Weitere Apps** für eigene Apps als Beispiel zur Anzeigenangabe und unterscheidet ihn von Bannern und Vollbildanzeigen für Eigenwerbung. Die [Richtlinie zur App-Werbung](https://support.google.com/googleplay/android-developer/answer/9899004?hl=de) untersagt irreführende Werbung und Weiterleitungen ohne bewusste Nutzeraktion.

Diese Bibliothek öffnet einen klar gekennzeichneten Store-Eintrag nur nach einem Tipp. Jede Host-App bleibt selbst für ihre **gesamte** Anzeigenangabe, Datenschutzhinweise, Store-Darstellung und die Prüfung aktueller Richtlinien verantwortlich.

## Bauen und prüfen

Mit JDK 21 und Android SDK Platform 36 den enthaltenen Gradle-Wrapper verwenden:

```sh
./gradlew testDebugUnitTest assembleDebug
```

Unter Windows:

```powershell
.\gradlew.bat testDebugUnitTest assembleDebug
```

Die Unit-Tests prüfen das Ausblenden der Host-App, Debug-Application-IDs und nicht veröffentlichte Einträge. Die Compose-Datei enthält Vorschauen für die vollständige Seite und den reinen Inhalt; Host-Apps sollten den Menüeinstieg zusätzlich mit ihrem eigenen Theme prüfen.

## Lizenzierung

Das Repository ist öffentlich, damit die DolomiteByte-Apps den gemeinsamen Quellcode beziehen können. Ein Maven-Release und eine `LICENSE`-Datei existieren derzeit nicht; Bedingungen für eine Nutzung durch Dritte wurden nicht festgelegt.
