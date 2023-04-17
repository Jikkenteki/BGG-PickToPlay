(ns bbg-reframe.views.login-view.login-subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::email
 (fn [db]
   (:email db)))

