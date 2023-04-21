(ns bbg-reframe.firebase.firebase-events
  (:require
   [bbg-reframe.network-events :as network-events]
   [clojure.string :as string]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [re-frame-firebase-nine.fb-reframe :as fb-reframe]
   [re-frame.core :as re-frame]
   [re-frame.loggers :refer [console]]))

;; fb
(re-frame/reg-event-fx
 ::fb-save-games
 (fn-traced [{:keys [db]} [_]]
            {::fb-reframe/firebase-set {:path (into ["users" (fb-reframe/get-current-user-uid)] ["cached-games"])
                                        :data (str (:games db))
                                        :success #(println "Successfully saved games in fb")}}))
;;
;; fetch games from firebase
;;
(re-frame/reg-event-fx
 ::fetch-games
 (fn-traced [_ [_ _]]
            {::fb-reframe/on-value-once {:path ["users" (fb-reframe/get-current-user-uid) "cached-games"]
                                         :success ::network-events/read-fetched-games}}))

;; set event
(re-frame/reg-event-fx
 ::fb-set
 (fn-traced [_ [_ {:keys [path data]}]]
            {::fb-reframe/firebase-set {:path (into ["users" (fb-reframe/get-current-user-uid)] path)
                                        :data data}}))

;; adds a new key-data pair under the path. The key is sored in key-path in db
;; note normally a :success and :error events are expected
(re-frame/reg-event-fx
 ::fb-push
 (fn-traced [_ [_ {:keys [path data key-path success]}]]
            {::fb-reframe/firebase-push {:path (into ["users" (fb-reframe/get-current-user-uid)] path)
                                         :data data
                                         :success #(println (str "Successfully pushed: " data %))
                                         :key-path key-path}}))


;;
;; sync user data from firebase
;;
(re-frame/reg-event-fx
 ::sync-fb-user-data
 (fn-traced
  [_ [_ _]]
  {::fb-reframe/on-value-once {:path ["users" (fb-reframe/get-current-user-uid) "collections"]
                               :success ::network-events/handle-fb-fetched-collections}}))



(comment

  (into ["users" (fb-reframe/get-current-user-uid)] ["cached-games"])

  (string/join "/" (into ["users" (fb-reframe/get-current-user-uid)] ["cached-games"]))

  (re-frame/dispatch [::fb-set {:path ["collections" "new collection"] :data "games"}])
  (re-frame/dispatch [::fb-set {:path ["collections" "new collection" "name"] :data "collection name"}])
  (re-frame/dispatch [::fb-set {:path ["collections" "new collection" "games" 123] :data true}])
  (re-frame/dispatch [::fb-set {:path ["collections" "new collection" "games" 123] :data nil}])

(fb-reframe/get-current-user-uid)
  (re-frame/dispatch [::fb-set {:path ["collections" "-NTOQH19rgxRLU4GVB0O"] :data nil}])
  (re-frame/dispatch [::fb-set {:path ["users" 
                                       (fb-reframe/get-current-user-uid) 
                                       ] :data nil}])

  (re-frame/dispatch [::fb-push {:path ["collections"]
                                 :data {:name "aaa" :games {"123" true}}
                                 :key-path [:firebase :new-collection-id]}])

  (re-frame/reg-event-fx
   ::handle-collections
   (fn-traced [{:keys [db]} [_ collections]]
              (js/console.log collections)
              {:db (assoc db :collections collections)}))

  (re-frame/reg-event-fx
   ::fetch-collections
   (fn-traced [_ [_ _]]
              {::fb-reframe/on-value-once {:path ["users" (fb-reframe/get-current-user-uid) "collections"]
                                           :success ::handle-collections}}))

  (re-frame/dispatch [::fetch-collections])
  (js/console.log @(re-frame/subscribe [::collections]))
  (console :log (str @(re-frame/subscribe [::collections])))

  (name :-NTOQH19rgxRLU4GVB0O)


  ;
  )
