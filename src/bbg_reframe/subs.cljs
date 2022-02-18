(ns bbg-reframe.subs
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.model.sort-filter :refer [sorting-fun]]))

(re-frame/reg-sub
 ::result
 (fn [db]
   (:result db)))

(re-frame/reg-sub
 ::fields
 (fn [db]
   (:fields db)))

(re-frame/reg-sub
 ::form
 (fn [db [_ id]]
   (get-in db [:form id])))
