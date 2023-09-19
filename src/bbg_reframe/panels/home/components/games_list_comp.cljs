(ns bbg-reframe.panels.home.components.games-list-comp
  (:require [bbg-reframe.events :as events]
            [bbg-reframe.panels.home.components.game-comp-with-info :refer [game-comp-with-info]]
            [bbg-reframe.panels.home.components.sliders-comp :refer [sliders-comp]]
            [bbg-reframe.subs :as subs]
            [re-frame.core :as re-frame]))

(defn games-list-comp []
  (let [open-tab @(re-frame/subscribe [::subs/ui :open-tab])
        result @(re-frame/subscribe [::subs/result])
        has-search-results? @(re-frame/subscribe [::subs/has-search-results?])]
    [:div.min-h-0.flex.flex-col.grow.relative
     (when has-search-results?
       [:div.absolute.inset-0
        {:class "bg-black/50"}])
     (when (= open-tab :sliders-tab)
       [:div.absolute.bg-stone-750.bottom-20.left-8.right-8.p-4.rounded-lg.shadow-2xl
        [sliders-comp]])
     [:div
      {:id "games-list-sliders-button"
       :class (when (= open-tab :sliders-tab) "active")
       :on-click #(re-frame/dispatch
                   [::events/set-open-tab :sliders-tab])}
      [:i.fa-solid.fa-sliders.fa-xl]]
     [:div.overflow-auto.px-2
      (map (fn [game]
             ^{:key (:id game)} [game-comp-with-info game])
           result)]]))