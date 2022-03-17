(ns bbg-reframe.login-view.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing-stubs :refer [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.events :as events]))


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
            (println "User signed-out")
            (dissoc db :email)))


;; (re-frame/reg-event-fx
;;  ::sign-in
;;  (fn-traced [_ [_ email password]]
;;             {::fb-reframe/firebase-sign-in {:email email
;;                                             :password password
;;                                             :success ::sign-in-success}}))

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
