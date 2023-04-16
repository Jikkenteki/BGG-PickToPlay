(ns bbg-reframe.views.collectionsView.collectionsSubs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::email
 (fn [db]
   (:email db)))

