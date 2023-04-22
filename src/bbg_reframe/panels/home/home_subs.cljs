(ns bbg-reframe.panels.home.home-subs 
  (:require [bbg-reframe.panels.home.home-events :as home-events]
            [bbg-reframe.panels.game.game-subs :as game-subs]
            [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::available-games
 :<- [::game-subs/on-auth-value ["available"]]
 (fn [games]
   (when (seq games)
     (re-frame/dispatch [::home-events/make-available games]))
   games))