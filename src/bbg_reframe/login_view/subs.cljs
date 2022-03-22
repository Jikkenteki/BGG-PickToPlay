(ns bbg-reframe.login-view.subs
  (:require [re-frame.core :as re-frame]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.login-view.events :as events]))

(re-frame/reg-sub
 ::email
 (fn [db]
   (:email db)))

(re-frame/reg-sub
 ::on-auth-changed
 :<- [::fb-reframe/on-auth-state-changed]
 (fn [uid [_ _]]
   (re-frame/dispatch [::events/set-uid uid])))