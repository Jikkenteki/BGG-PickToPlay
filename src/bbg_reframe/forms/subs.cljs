(ns bbg-reframe.forms.subs
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.forms.utils :refer [is-substring?]]))

(re-frame/reg-sub
 ::dropdown-select-options
 (fn [db [_ path all-options]]
   (->> all-options
        (filter
         (fn [{:keys [_ name]}]
           (or (nil? (get-in db path)) (is-substring? (get-in db path) name)))))))

(re-frame/reg-sub
 ::dropdown-select-size
 (fn [[_ path all-options]]
   (re-frame/subscribe [::dropdown-select-options path all-options]))
 (fn [options]
   ;; an extra option is (Nothing)
   (min 10 (inc (count options)))))
