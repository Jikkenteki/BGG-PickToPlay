(ns bbg-reframe.game-view.subs
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.subs :as subs]))

(re-frame/reg-sub
 ::game
 :<- [::subs/games]
 (fn [games [_ id]]
   (get games id)))