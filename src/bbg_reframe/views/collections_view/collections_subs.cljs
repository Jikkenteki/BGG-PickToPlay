(ns bbg-reframe.views.collections-view.collections-subs
  (:require             [bbg-reframe.views.game-view.game-subs :as auth-subs]
                        [re-frame.core :as re-frame]))

;; auth-collections
(re-frame/reg-sub
 ::collections-auth
 :<- [::auth-subs/on-auth-value ["collections"]]
 (fn [coll [_]]
   coll))