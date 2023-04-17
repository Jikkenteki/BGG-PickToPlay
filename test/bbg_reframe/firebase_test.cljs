(ns bbg-reframe.firebase-test
  (:require [bbg-reframe.emulator :refer [connect-fb-emulator-empty-db]]
            [bbg-reframe.events :as events]
            [bbg-reframe.firebase.events :as fb-events]
            [bbg-reframe.forms.forms :refer [db-set-value!]]
            [bbg-reframe.panels.collections.collections-events :refer [add-if-not-exists get-collections]]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.panels.login.login-events :as login-events]
            [bbg-reframe.panels.login.login-panel :refer [save-games]]
            [cljs.test :refer-macros [deftest testing is]]
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
           _ (re-frame/dispatch [::fb-events/fetch-games])
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

(deftest test-new-collection
  (testing "new collection"
    (rf-test/run-test-async
     (let [email "dranidis@gmail.com"
           _ (connect-fb-emulator-empty-db)
           _ (re-frame/dispatch-sync [::events/initialize-db])
           _ (re-frame/dispatch [::login-events/sign-in email "password"])]
       (rf-test/wait-for
        [::login-events/sign-in-success]
        (let [_ (println "New collection UID" (fb-reframe/get-current-user-uid))
              key (add-if-not-exists "collection name")
              _ (println (str "RETURNED:" key))
              ;; key2 (add-if-not-exists "collection name")
              ;; _ (println (str "RETURNED:" key2))
              _ (println (str "Collections: " (get-collections)))]
          (is (not (nil? key)))))))))


;; (deftest test-new-collection-1
;;   (testing "new collection sync"
;;     (rf-test/run-test-sync
;;      (let [email "dranidis@gmail.com"
;;            _ (connect-fb-emulator-empty-db)
;;            _ (re-frame/dispatch [::events/initialize-db])
;;            _ (re-frame/dispatch [::login-events/sign-in email "password"])
;;            _ (println "New collection UID" (fb-reframe/get-current-user-uid))
;;            key (add-if-not-exists "collection name")
;;            _ (println (str "RETURNED:" key))
;;               ;; key2 (add-if-not-exists "collection name")
;;               ;; _ (println (str "RETURNED:" key2))
;;            _ (println (str "Collections: " (get-collection-names (get-collections))))]
;;        (is (not (nil? key)))))))

(comment
  (re-frame/dispatch [::login-events/sign-in "dranidis@gmail.com" "password"])
  ;
  )
