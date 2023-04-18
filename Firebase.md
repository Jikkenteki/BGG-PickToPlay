# Firebase

## Observations about Offline
- Sign in is not possible off-line.
- On reload when off-line, user is still signed in. 
- Pushing is possible during offline (unless app is reload). Success is not received. Pushed values are sent to FB when online again and success is received. On reload any unsaved data are lost.
    - Note: The Firebase Realtime Database web APIs do not persist data offline outside of the session. In order for writes to be persisted to the server, the web page must not be closed before the data is written to the server


## Data storage handling

Personalised data such as collections are stored:
- in app-db and localstorage
- in fb (when online)

On sign-in, data are fetched from fb and stored in app-db and localstorage.

On reload, even when offline, if user is still logged-in, app-db data are fetched from localstorage.

On sign-out (or on reload with signed out user), app-db and localstorate data are deleted.

## On reload

Sometimes user is logged out.

