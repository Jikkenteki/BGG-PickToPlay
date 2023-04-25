(ns bbg-reframe.config
  (:require [bbg-reframe.env-variables  :refer [api-key-env
                                                cors-server-uri-env]]
            [clojure.string :as string]))

(def debug?
  ^boolean goog.DEBUG)

(def delay-between-fetches 500)
(def cors-server-uri (string/replace cors-server-uri-env "//$" "/")) 
(def xml-api 2)

(def fb-reframe-config-map {:temp-path [:firebase-temp-storage]
                            :firebase-config {:apiKey api-key-env,
                                              :authDomain "help-me-pick-what-to-play.firebaseapp.com",
                                              :databaseURL "https://help-me-pick-what-to-play-default-rtdb.europe-west1.firebasedatabase.app",
                                              :projectId "help-me-pick-what-to-play",
                                              :storageBucket "help-me-pick-what-to-play.appspot.com",
                                              :messagingSenderId "780911312465",
                                              :appId "1:780911312465:web:bbd9007195b3c630910270"}})