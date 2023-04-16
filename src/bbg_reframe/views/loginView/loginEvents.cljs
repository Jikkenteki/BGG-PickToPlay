(ns bbg-reframe.login-view.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.network-events :as events]
            [re-frame-firebase-nine.firebase-auth :refer [get-current-user on-auth-state-changed on-auth-state-changed-callback]]))

(re-frame/reg-event-db
 ::auth-state-changed
 (fn-traced [db [_ _]]
            (if (get-current-user)
              (assoc db :email (fb-reframe/get-current-user-email))
              db)))

(re-frame/reg-event-fx
 ::sign-in-success
 (fn-traced [_ [_ userCredential]]
            (println "User signed-in: " (.-email (.-user userCredential)))
            {}))

(re-frame/reg-event-fx
 ::sign-in-error
 (fn-traced [{:keys [db]} [_ error]]
            (.log js/console error)
            {:db (assoc db :error "Error signing in!")
             :dispatch-later {:ms 2000
                              :dispatch [::events/reset-error]}}))

(re-frame/reg-event-db
 ::sign-out-success
 (fn-traced [db [_]]
            (let [_ (println "User signed-out")]
              (dissoc db :email))))

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

(comment

  (re-frame/dispatch [::sign-in "dranidis@gmail.com" "password"])

  (re-frame/dispatch [::sign-in "adranidisb@gmail.com" "password"])
  (re-frame/dispatch [::sign-out])

  (on-auth-state-changed on-auth-state-changed-callback)
 ;
  )