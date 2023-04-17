(ns bbg-reframe.views.collections-view.collections-subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::email
 (fn [db]
   (:email db)))

