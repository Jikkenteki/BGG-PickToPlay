(ns bbg-reframe.firebase-test
  (:require [bbg-reframe.emulator :refer [connect-fb-emulator-empty-db]]
            [bbg-reframe.events :as events]
            [bbg-reframe.firebase.firebase-events :as firebase-events]
            [bbg-reframe.forms.forms :refer [db-set-value!]]
            [bbg-reframe.panels.login.login-events :as login-events]
            [bbg-reframe.panels.login.login-panel :refer [save-games]]
            [bbg-reframe.subs :as subs]
            [cljs.test :refer-macros [deftest testing is run-tests]]
            [day8.re-frame.test :as rf-test]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [re-frame-firebase-nine.firebase-auth :refer [on-auth-state-changed
                                                          on-auth-state-changed-callback]]
            [re-frame.core :as re-frame]))

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
           _ (connect-fb-emulator-empty-db)
           _ (re-frame/dispatch-sync [::events/initialize-db])
           games {"1" {:id "1" :name "game1"}
                  "2" {:id "2" :name "game2"}}
           _ (println (str "INIT " (:games  @db)))
           _ (db-set-value! [:games] games)
           _ (println "UID" (fb-reframe/get-current-user-uid))
           _ (save-games)
           _ (println (str "After save " (:games  @db)))
           _ (re-frame/dispatch-sync [::events/initialize-db])
           _ (println (str "After init " (:games  @db)))
           _ (re-frame/dispatch [::firebase-events/fetch-games])
           _ (println (str "After dispatch fetch " (:games  @db)))]
       (is (= games (:games @db)))))))

(deftest test-sign-in
  (testing "sign-up"
    (rf-test/run-test-async
     (let [email "dranidis@gmail.com"
           _ (connect-fb-emulator-empty-db)
           _ (on-auth-state-changed (fn [x]
                                      (re-frame/dispatch [::login-events/auth-state-changed])
                                      (on-auth-state-changed-callback x)))
           _ (re-frame/dispatch-sync [::events/initialize-db])
           _ (re-frame/dispatch [::login-events/sign-in email "password"])]
       (rf-test/wait-for [::login-events/sign-in-success]
                         (is (= email (fb-reframe/get-current-user-email))))))))


(comment
  (re-frame/dispatch [::login-events/sign-in "dranidis@gmail.com" "password"])


  1

  (run-tests)

  ;
  )




