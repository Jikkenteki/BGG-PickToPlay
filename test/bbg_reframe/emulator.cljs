(ns bbg-reframe.emulator
  (:require [re-frame-firebase-nine.fb-reframe :refer [fb-reframe-config connect-emulator]]
            [re-frame-firebase-nine.firebase-database :refer [fb-set! fb-ref get-db]]))

(defn connect-fb-emulator-empty-db
  []
  (fb-reframe-config {:temp-path [:firebase-temp-storage]
                      :firebase-config {:apiKey "AIzaSyCLH4BlNSOfTrMlB_90Hsxg5cr3bn3p-7E",
                                        :authDomain "help-me-pick-what-to-play.firebaseapp.com",
                                        :databaseURL "https://help-me-pick-what-to-play-default-rtdb.europe-west1.firebasedatabase.app",
                                        :projectId "help-me-pick-what-to-play",
                                        :storageBucket "help-me-pick-what-to-play.appspot.com",
                                        :messagingSenderId "780911312465",
                                        :appId "1:780911312465:web:bbd9007195b3c630910270"}})

  (connect-emulator)
  (fb-set! (fb-ref (get-db)) nil))