(ns bbg-reframe.panels.home.components.games-list-comp
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.panels.home.components.game-comp :refer [game-comp]]))

(defn games-list-comp []
  (let [result @(re-frame/subscribe [::subs/result])
        players @(re-frame/subscribe [::subs/form :players])
        time @(re-frame/subscribe [::subs/form :time-available])]
    [:div.overflow-auto.grow.px-2.mb-1
     (map
      (fn [game]
        ^{:key (:id game)} [game-comp game players time])
      result)]))