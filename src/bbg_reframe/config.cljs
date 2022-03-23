(ns bbg-reframe.config)

(def debug?
  ^boolean goog.DEBUG)

(def delay-between-fetches 500)
(def cors-server-uri "https://guarded-wildwood-02993.herokuapp.com/")
(def xml-api 2)

(def fb-reframe-config-map {:temp-path [:firebase-temp-storage]
                            :firebase-config {:apiKey "AIzaSyCLH4BlNSOfTrMlB_90Hsxg5cr3bn3p-7E",
                                              :authDomain "help-me-pick-what-to-play.firebaseapp.com",
                                              :databaseURL "https://help-me-pick-what-to-play-default-rtdb.europe-west1.firebasedatabase.app",
                                              :projectId "help-me-pick-what-to-play",
                                              :storageBucket "help-me-pick-what-to-play.appspot.com",
                                              :messagingSenderId "780911312465",
                                              :appId "1:780911312465:web:bbd9007195b3c630910270"}})