(ns bbg-reframe.test-firebase.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [bbg-reframe.test-firebase.firebase.fb-reframe :as fb-reframe :refer [get-current-user]]))


(def poll-time-interval-ms 200)

(re-frame/reg-event-fx
 ::poll-user
 (fn-traced [cofx [_ timeout]]
            (let [time (::time (:db cofx))]
              (if (> time timeout)
                (do
                  (println "Log in")
                  {})
                (if (get-current-user)
                  {:db (assoc (:db cofx) ::email (fb-reframe/get-current-user-email))}
                  {:db (assoc (:db cofx) ::time (+ time poll-time-interval-ms))
                   :dispatch-later {:ms poll-time-interval-ms
                                    :dispatch [::poll-user timeout]}})))))


(re-frame/reg-event-fx
 ::update-value
 (fn-traced [_ [_ path value]]
            {::fb-reframe/firebase-set {:path path
                                        :data value
                                        :success #(println "Success")}}))

(re-frame/reg-event-fx
 ::push-value
 (fn-traced [_ [_ path value]]
            {::fb-reframe/firebase-push {:path path
                                         :data value
                                         :success #(println "Success")
                                         :key-path [:key]}}))

(re-frame/reg-event-fx
 ::update-available
 (fn-traced [_ [_ id value]]
            {::fb-reframe/firebase-set {:path ["users" (fb-reframe/get-current-user-uid) "available" id]
                                        :data value
                                        :success #(println "Success")}}))

(re-frame/reg-event-fx
 ::update-group-with
 (fn-traced [_ [_ id value]]
            {::fb-reframe/firebase-set {:path ["users" (fb-reframe/get-current-user-uid) "group-with" id]
                                        :data value
                                        :success #(println "Success")}}))

(re-frame/reg-event-fx
 ::new-collection
 (fn-traced [_ [_ name]]
            {::fb-reframe/firebase-push {:path ["users" (fb-reframe/get-current-user-uid) "collections"]
                                         :data {:name (if name name "New collection")}
                                         :success #(println "Success")
                                         :key-path [::current-collection-key]}}))

(defn- if-nil-getcurrentkey
  [collection-key cofx]
  (if collection-key collection-key (get-in cofx [:db ::current-collection-key])))

(re-frame/reg-event-fx
 ::delete-collection
 (fn-traced [cofx [_ collection-id]]
            {::fb-reframe/firebase-set
             {:path ["users" (fb-reframe/get-current-user-uid)
                     "collections" (if-nil-getcurrentkey collection-id cofx)]
              :data nil
              :success #(println "Successfully deleted")}}))

(re-frame/reg-event-fx
 ::add-game-to-collection
 (fn-traced [cofx [_ game-id collection-id]]
            {::fb-reframe/firebase-set
             {:path ["users" (fb-reframe/get-current-user-uid)
                     "collections" (if-nil-getcurrentkey collection-id cofx) "games" game-id]
              :data true
              :success #(println "Success")}}))

(re-frame/reg-event-fx
 ::remove-game-from-collection
 (fn-traced [cofx [_ game-id collection-id]]
            {::fb-reframe/firebase-set
             {:path ["users" (fb-reframe/get-current-user-uid)
                     "collections" (if-nil-getcurrentkey collection-id cofx) "games" game-id]
              :data nil
              :success #(println "Success")}}))


(re-frame/reg-event-db
 ::sign-in-success
 (fn-traced [db [_ userCredential]]
            (println "User signed-in")
            (let [email (.-email (.-user userCredential))]
              (assoc db ::email email))))

(re-frame/reg-event-db
 ::sign-out-success
 (fn-traced [db [_]]
            (println "User signed-out")
            (dissoc db ::email)))


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
              (assoc db ::email email))))

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
  ;;
  ;; examples of dispatch events
  ;;

  ::push-value ;; for linter

  (re-frame/dispatch [::sign-in "adranidisb@gmail.com" "password"])

  (re-frame/dispatch [::sign-in "dranidis@gmail.com" "password"])

  (re-frame/dispatch [::sign-out])

  (fb-reframe/get-current-user-uid)

  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "games" "1" "available"] false])
  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "games" "available" "1"] true]) ;; or this?

  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "games" "1" "group-with"] "0"])

  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "collections" "collection-1"] (str #{"1" "3" "5"})])
  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "collections" "collection-2"] (str #{"4" "2"})])
  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "collections" "collection-3"] {:name "My collection" :games #{"1" "2"}}])
  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "collections" "collection-1"] {:name "In the car" :games #{"1" "4" "5"}}])
  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "collections" "collection-0"] {:name "Empty collection" :games #{}}])


  ;; (re-frame/dispatch
  ;;  [::update-value ["users" (fb-reframe/get-current-user-uid) "collections"]
  ;;   {:collection-01 {:name "collection01" :games (str #{"0" "1"})}
  ;;    :collection-02 {:name "collection02" :games (str #{"2" "3"})}}])

  (re-frame/dispatch [::new-collection "A_newcollector 15"])
  (re-frame/dispatch [::new-collection])
  (re-frame/dispatch [::delete-collection])
  (re-frame/dispatch [::delete-collection "-MxmXZhex9r0ouhklU8j"])
  (re-frame/dispatch [::add-game-to-collection  "15"])
  (re-frame/dispatch [::add-game-to-collection  "19"])
  (re-frame/dispatch [::add-game-to-collection  "21"])
  (re-frame/dispatch [::add-game-to-collection  "21" "-MxmXcJIieM3txwDIu8a"])
  (re-frame/dispatch [::remove-game-from-collection "15"])

  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "available" "19"] true])
  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "available" "0"] nil])
  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "available" "2"] nil])

  (re-frame/dispatch [::update-value ["users" (fb-reframe/get-current-user-uid) "group-with" "10"] "90"])

  ;
  )