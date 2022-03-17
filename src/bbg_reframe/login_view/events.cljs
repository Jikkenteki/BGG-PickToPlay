(ns bbg-reframe.login-view.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.events :as events]
            [re-frame-firebase-nine.firebase-auth :refer [get-current-user]]))

(def poll-time-interval-ms 1000)

(re-frame/reg-event-fx
 ::poll-user
 (fn-traced [cofx [_ timeout]]
            (let [time (::time (:db cofx))]
              (if (> time timeout)
                (do
                  (println "Log in")
                  {})
                (if (get-current-user)
                  {:db (assoc (:db cofx) :email (fb-reframe/get-current-user-email))}
                  {:db (assoc (:db cofx) ::time (+ time poll-time-interval-ms))
                   :dispatch-later {:ms poll-time-interval-ms
                                    :dispatch [::poll-user timeout]}})))))


(re-frame/reg-event-fx
 ::sign-in-success
 (fn-traced [{:keys [db]} [_ userCredential]]
            (println "User signed-in")
            (let [email (.-email (.-user userCredential))]
              {:db (assoc db :email email)
               :dispatch [::events/navigate [:home]]})))


(re-frame/reg-event-db
 ::sign-out-success
 (fn-traced [db [_]]
            (let [_ (println "User signed-out")] (dissoc db :email))))


(re-frame/reg-event-fx
 ::sign-in
 (fn-traced [_ [_ email password]]
            {:fx [[:dispatch [::sign-out]]
                  [::fb-reframe/firebase-sign-in {:email email
                                                  :password password
                                                  :success ::sign-in-success}]]}))

(re-frame/reg-event-db
 ::sign-up-success
 (fn-traced [db [_ userCredential]]
            (println "User created")
            (let [email (.-email (.-user userCredential))]
              (assoc db :email email))))

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

 ;
  )