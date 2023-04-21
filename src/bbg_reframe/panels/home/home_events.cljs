(ns bbg-reframe.panels.home.home-events 
  (:require [bbg-reframe.forms.utils :refer [if-nil?->value]]
            [day8.re-frame.tracing :refer [fn-traced]]
            [re-frame.core :as re-frame]))

(defn make-available
  [all-games available]
  (reduce-kv
   (fn [m k v]
     (-> m
         (assoc k v)
         (assoc-in [k :available] (if-nil?->value ((keyword k) available) false))))
   {}
   all-games))

(re-frame/reg-event-fx
 ::make-available
 (fn-traced
  [{:keys [db]} [_ games]]
  (println "Make available " games)
  {:db (assoc db :games (make-available (:games db) games))
   :dispatch [:bbg-reframe.network-events/update-result]}))