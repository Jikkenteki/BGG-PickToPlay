(ns bbg-reframe.config
  (:require [bbg-reframe.env-variables  :refer [cors-server-uri-env api-key-env]]))

(def debug?
  ^boolean goog.DEBUG)

(def delay-between-fetches 500)
(def cors-server-uri cors-server-uri-env)
(def xml-api 2)

(def fb-reframe-config-map {:temp-path [:firebase-temp-storage]
                            :firebase-config {:apiKey api-key-env,
                                              :authDomain "help-me-pick-what-to-play.firebaseapp.com",
                                              :databaseURL "https://help-me-pick-what-to-play-default-rtdb.europe-west1.firebasedatabase.app",
                                              :projectId "help-me-pick-what-to-play",
                                              :storageBucket "help-me-pick-what-to-play.appspot.com",
                                              :messagingSenderId "780911312465",
                                              :appId "1:780911312465:web:bbd9007195b3c630910270"}})