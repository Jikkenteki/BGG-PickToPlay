(ns bbg-reframe.panels.home.home-panel
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.panels.home.home-subs :as home-subs]
            [bbg-reframe.events :as events]
            [bbg-reframe.panels.home.components.loading-games-info-comp :refer [loading-games-info-comp]]
            [bbg-reframe.panels.home.components.games-list-comp :refer [games-list-comp]]
            [bbg-reframe.panels.home.components.bottom-buttons-bar-comp :refer [bottom-buttons-bar-comp]]
            ["sax" :as sax]
            [bbg-reframe.panels.home.components.search-comp :refer [search-games-query-comp search-games-results-comp]]
            [bbg-reframe.panels.home.components.search-comp-events :as search-comp-events]))

; required for tubax to work
(js/goog.exportSymbol "sax" sax)

(defn home-panel []
  (let [_ @(re-frame/subscribe [::home-subs/available-games])]
    [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
     [:h1.text-3xl.font-bold.mb-2.mt-4.px-1
      "PickToPlay"]
     [loading-games-info-comp]
     [games-list-comp]
     [search-games-results-comp (fn [id] 
                                  (re-frame/dispatch [::search-comp-events/reset-search])
                                  (re-frame/dispatch [::events/navigate [:game-view :id id]]))]
     [search-games-query-comp]
     [bottom-buttons-bar-comp]]))
