(ns bbg-reframe.firebase.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.network-events :as events]
            [clojure.string :as string]))

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
                                         :success ::events/read-fetched-games}}))

(comment
  (into ["users" (fb-reframe/get-current-user-uid)] ["cached-games"])

  (string/join "/" (into ["users" (fb-reframe/get-current-user-uid)] ["cached-games"]))
  ;
  )