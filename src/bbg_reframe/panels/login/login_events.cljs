(ns bbg-reframe.panels.login.login-events
  (:require [bbg-reframe.firebase.firebase-events :as firebase-events]
            [bbg-reframe.localstorage.localstorage-events :refer [remove-fb-collections-local-store-when-signed-out]]
            [bbg-reframe.network-events :as network-events]
            [bbg-reframe.events :as events]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [re-frame-firebase-nine.firebase-auth :refer [get-current-user
                                                          on-auth-state-changed
                                                          on-auth-state-changed-callback]]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
 ::auth-state-changed
 [remove-fb-collections-local-store-when-signed-out]
 (fn-traced [{:keys [db]} [_ _]]
            (if (get-current-user)
              {:db (-> db
                       (assoc :email (fb-reframe/get-current-user-email))
                       (assoc :uid (fb-reframe/get-current-user-uid)))}
              ;; for some reasons collections are still there
              {:db (-> db
                       (dissoc :email)
                       (dissoc :uid)
                       (dissoc :collections))})))

(re-frame/reg-event-fx
 ::sign-in-success
 (fn-traced [_ [_ userCredential]]
            (println "User signed-in: " (.-email (.-user userCredential)))
            {:dispatch [::sync-with-fb]}))

(re-frame/reg-event-fx
 ::sign-in-error
 (fn-traced [{:keys [db]} [_ error]]
            (.log js/console error)
            {:db (assoc db :error "Error signing in!")
             :dispatch-later {:ms 2000
                              :dispatch [::network-events/reset-error]}}))

(re-frame/reg-event-fx
 ::sign-out-success
 [remove-fb-collections-local-store-when-signed-out]
 (fn-traced [{:keys [db]} [_ _]]
            (let [_ (println "User signed-out")]
              {:db (-> db
                  (dissoc :email)
                  (dissoc :uid)
                  (dissoc :collections))
               :dispatch [::events/navigate [:home]]})))

(re-frame/reg-event-fx
 ::sign-in
 (fn-traced [_ [_ email password]]
            {:fx [; [:dispatch [::sign-out]]
                  [::fb-reframe/firebase-sign-in {:email email
                                                  :password password
                                                  :success ::sign-in-success
                                                  :error ::sign-in-error}]]}))

(re-frame/reg-event-db
 ::sign-up-success
 (fn-traced [db [_ userCredential]]
            (println "User created")
            (.log js/console userCredential)
            db))

(re-frame/reg-event-fx
 ::sign-up
 (fn-traced [_ [_ email password]]
            {::fb-reframe/firebase-create-user {:email email
                                                :password password
                                                :success ::sign-up-success}}))


(re-frame/reg-event-fx
 ::sign-out
 (fn-traced [_ [_]]
            {::fb-reframe/firebase-sign-out {:success ::sign-out-success}}))

(re-frame/reg-event-fx
 ::sync-with-fb
 (fn-traced [_ [_]]
            (println "Dummy Syncing")
            {:dispatch [::firebase-events/sync-fb-user-data]}))


(comment

  (re-frame/dispatch [::sign-up "new@gmail.com" "password"])

  (re-frame/dispatch [::sign-in "dranidis@gmail.com" "password"])
  (re-frame/dispatch [::sign-in "new@gmail.com" "password"])

  (re-frame/dispatch [::sign-out])
  


  (on-auth-state-changed on-auth-state-changed-callback)
 ;
  )