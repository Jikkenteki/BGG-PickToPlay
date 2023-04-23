(ns bbg-reframe.panels.login.login-subs
  (:require [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::email
 (fn [db]
   (:email db)))

(re-frame/reg-sub
 ::connected-to-fb
 :<- [::fb-reframe/on-value [".info/connected"]]
 (fn [connected?]
   connected?))

