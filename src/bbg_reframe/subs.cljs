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
