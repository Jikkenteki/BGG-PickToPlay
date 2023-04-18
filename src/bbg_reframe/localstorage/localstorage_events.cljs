(ns bbg-reframe.localstorage.localstorage-events
  (:require [bbg-reframe.events :refer [check-spec-interceptor]]
            [bbg-reframe.model.localstorage :refer [remove-item! set-item!]]
            [day8.re-frame.tracing :refer [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :refer [get-current-user]]
            [re-frame.core :as re-frame]))


(defn fb-collections->local-store
  [db]
  (set-item! "fb-collections"  (:collections db)))

;; Interceptor that saves the games from db to local-storage
(def ->fb-collections->local-store (re-frame/after fb-collections->local-store))

(re-frame/reg-event-fx
 ::update-fb-collections
 [check-spec-interceptor ->fb-collections->local-store]
 (fn-traced [{:keys [db]} [_ val]]
            {:db (assoc db :collections val)}))



(defn remove-fb-collections-if-signed-out
  [_]
  (when (not (get-current-user))
    (let [_ (println (str "Remove ls fb-collections when not signed in."))]
      (remove-item! "fb-collections"))))

;; Interceptor  
(def remove-fb-collections-local-store-when-signed-out (re-frame/after remove-fb-collections-if-signed-out))

