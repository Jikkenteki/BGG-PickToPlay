(ns bbg-reframe.firebase.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.network-events :as events]))

;; fb
(re-frame/reg-event-fx
 ::fb-set
 (fn-traced [_ [_ path value]]
            {::fb-reframe/firebase-set {:path (into ["users" (fb-reframe/get-current-user-uid)] path)
                                        :data value
                                        :success #(println "Success")}}))
;;
;; fetch games from firebase
;;
(re-frame/reg-event-fx
 ::fetch-games
 (fn-traced [_ [_ _]]
            {::fb-reframe/on-value-once {:path ["users" (fb-reframe/get-current-user-uid) "cached-games"]
                                         :success ::events/read-fetched-games}}))
