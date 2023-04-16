(ns bbg-reframe.login-view.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::email
 (fn [db]
   (:email db)))

