# SpotNotes Exercise App
This is a simple app put together to allow for KMP focused exercises. It is not meant to be overly opinionated, architected, or clean as we would rather see how you approach things.

### Basic Behavior
The basic premise of the app is to allow a user to save a journal "notes" about places they've been. When the user saves a note, it will show up in the history tab with the text and the distance between this note and the previous note. This obviously isn't the most compelling app idea, but it's just for exercise purposes.

### Location
Both Android and iOS have a class (`LocationManager` in Android, `Model` in iOS) for handling the location and distance requests. Permissions are requested up front when the app opens. There currently isn't fallback behavior, so be sure to allow them.

### Presentation
There is currently a basic UI in place using Compose and SwiftUI

### KMP
This app represents a project which was developed separately for Android and iOS and is just starting to migrate to KMP. To save time, we've done the initial KMP setup, but there is no real code there yet.

# Exercise Brief

We would like to continue the migration of this app to KMP and introduce improved functionality.
- As much of the new and existing business logic as possible should move to shared code
    - Architecture of the shared code should support continued migration and development in the shared code
    - Shared UI is not required at this point
- Introduce persistence of the notes
    - The notes should be saved to a database so that they can be viewed even after app restart
    - Database logic should happen in shared code
- Limit usage of location services
  - Our imaginary users have complained about the battery impact of getting location each time they save a note. We want to limit how often location is requested.
    - Yes, this doesn't make much since for an app that's all about pairing notes and locations, but that's ok for the exercise.
  - If it has been less than one minute since the user saved their last note. The new note should be saved, but don't request a new location. Instead use the location from the last note
  - If it has been more than one minute since the user saved their last note. Request an updated location and save the note as normal
  - This logic should live in shared code