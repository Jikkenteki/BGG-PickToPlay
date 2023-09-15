(ns bbg-reframe.panels.home.components.games-list-comp
  (:require
   [bbg-reframe.panels.home.components.game-comp :refer [game-comp]]
   [bbg-reframe.subs :as subs]
   [re-frame.core :as re-frame]
   [bbg-reframe.events :as events]))

(defn games-list-comp []
  (let [open-tab @(re-frame/subscribe [::subs/ui :open-tab])
        result @(re-frame/subscribe [::subs/result])
        players @(re-frame/subscribe [::subs/form :players])
        time @(re-frame/subscribe [::subs/form :time-available])]
    [:div.min-h-0.flex.flex-col.grow.mb-1.relative
     [:div
      {:id "games-list-sliders-button"
       :class (when (= open-tab :sliders-tab) "active")
       :on-click #(re-frame/dispatch
                   [::events/set-open-tab :sliders-tab])}
      [:i.fa-solid.fa-sliders.fa-xl]]
     [:div.overflow-auto.px-2
      (map (fn [game]
             ^{:key (:id game)} [game-comp game players time])
           result)]]))