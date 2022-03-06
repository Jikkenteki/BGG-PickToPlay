(ns bbg-reframe.components.games-list-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.components.game-comp :refer [game-comp]]))

(defn games-list-comp []
  (let [result @(re-frame/subscribe [::subs/result])
        players @(re-frame/subscribe [::subs/form :players])
        time @(re-frame/subscribe [::subs/form :time-limit])]
    [:div.overflow-auto.grow.px-3
     (map
      (fn [game]
        (game-comp game players time))
      result)]))