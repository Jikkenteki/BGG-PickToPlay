(ns bbg-reframe.firebase-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [day8.re-frame.test :as rf-test]
            [bbg-reframe.forms.forms :refer [db-set-value!]]
            [bbg-reframe.subs :as subs]
            [re-frame.core :as re-frame]
            [bbg-reframe.firebase.events :as fb-events]
            [bbg-reframe.login-view.view :refer [save-games]]
            [re-frame-firebase-nine.fb-reframe :refer [fb-reframe-config connect-emulator]]
            [bbg-reframe.events :as events]))

(defn connect-fb-emulator
  []
  (fb-reframe-config {:temp-path [:firebase-temp-storage]
                      :firebase-config {:apiKey "AIzaSyCLH4BlNSOfTrMlB_90Hsxg5cr3bn3p-7E",
                                        :authDomain "help-me-pick-what-to-play.firebaseapp.com",
                                        :databaseURL "https://help-me-pick-what-to-play-default-rtdb.europe-west1.firebasedatabase.app",
                                        :projectId "help-me-pick-what-to-play",
                                        :storageBucket "help-me-pick-what-to-play.appspot.com",
                                        :messagingSenderId "780911312465",
                                        :appId "1:780911312465:web:bbd9007195b3c630910270"}})

  (connect-emulator))

(deftest test-db-test-value
  (testing "db-set-value!"
    (rf-test/run-test-sync
     (let [db (re-frame/subscribe [::subs/db])
           games {"1" {:id "1" :name "game"}}
           _ (db-set-value! [:games] games)]
       (is (= games (:games @db)))))))



(deftest test-save-fetch-cached-games-fb
  (testing "save-fetch-cached-games-fb"
    (rf-test/run-test-sync
     (let [db (re-frame/subscribe [::subs/db])
           _ (connect-fb-emulator)
           _ (re-frame/dispatch-sync [::events/initialize-db])
           games {"1" {:id "1" :name "game1"}
                  "2" {:id "2" :name "game2"}}
           _ (println (:games  @db))
           _ (db-set-value! [:games] games)
           _ (save-games)
           _ (println (:games  @db))
           _ (re-frame/dispatch-sync [::events/initialize-db])
           _ (println (:games  @db))
           _ (re-frame/dispatch [::fb-events/fetch-games])
           _ (println (:games  @db))]
       (is (= games (:games @db)))))))
