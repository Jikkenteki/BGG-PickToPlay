(ns bbg-reframe.subs
  (:require
   [re-frame.core :as re-frame]))

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

(re-frame/reg-sub
 ::loading
 (fn [db [_ id]]
   (:loading db)))

(re-frame/reg-sub
 ::error
 (fn [db [_ id]]
   (:error db)))
