(ns bbg-reframe.emulator
  (:require [bbg-reframe.env-variables :refer [api-key-env]]
            [re-frame-firebase-nine.fb-reframe :refer [connect-emulator fb-reframe-config] :as fb-reframe]
            [re-frame-firebase-nine.firebase-database :refer [fb-ref fb-set!
                                                              get-db]]))

(defn connect-fb-emulator-empty-db
  []
  (fb-reframe-config {:temp-path [:firebase-temp-storage]
                      :firebase-config {:apiKey api-key-env,
                                        :authDomain "help-me-pick-what-to-play.firebaseapp.com",
                                        :databaseURL "https://help-me-pick-what-to-play-default-rtdb.europe-west1.firebasedatabase.app",
                                        :projectId "help-me-pick-what-to-play",
                                        :storageBucket "help-me-pick-what-to-play.appspot.com",
                                        :messagingSenderId "780911312465",
                                        :appId "1:780911312465:web:bbd9007195b3c630910270"}})

  (connect-emulator)
  ;; not permitted
  ;; (fb-set! (fb-ref (get-db)) nil)
  )


(comment

  (connect-emulator)

  (connect-fb-emulator-empty-db)

  (-> (fb-set! (fb-ref (get-db)) nil)
      (.then #(js/console.log %)))
  
  (fb-set! (fb-ref (get-db) ["users" (fb-reframe/get-current-user-uid)]) nil
           #(js/console.log "then " %)
           #(js/console.log "catch " %))
  ;

  )