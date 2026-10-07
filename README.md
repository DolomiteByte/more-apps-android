# DolomiteByte More Apps

Shared Android Material 3 library for the **More apps** menu destination in Pubdash, TookAction,
Fileco, Everscan and TheraBuddy. The catalogue, descriptions, app icons and Play Store navigation
live here. Each host keeps its own Material 3 theme and chooses the menu location.

The five app repositories include this private repository as a Git submodule at `shared/more-apps`.
Add it to Gradle with `include(":moreapps")` and
`project(":moreapps").projectDir = file("shared/more-apps")`, then add
`implementation(project(":moreapps"))` to the app. Clone with `--recurse-submodules`, or run
`git submodule update --init --recursive` after cloning.

`TookAction` is in the catalogue but remains hidden until its public Play listing exists. Once
published, change `publishedOnPlay` to `true` here and advance the submodule commit in each app.

The screen opens only first-party Google Play listings after an explicit tap. It never launches a
download or displays an ad banner, interstitial or notification. The host app's existing ad
declaration stays governed by its other advertising. See Google's [ads declaration examples](https://support.google.com/googleplay/android-developer/answer/9859455)
and [app promotion policy](https://support.google.com/googleplay/android-developer/answer/9899004).
