(ns bbg-reframe.panels.home.home-panel
  (:require
   ["sax" :as sax]
   [bbg-reframe.events :as events]
   [bbg-reframe.network-events :as network-events]
   [bbg-reframe.panels.home.components.games-list-comp :refer [games-list-comp]]
   [bbg-reframe.panels.home.components.loading-games-info-comp :refer [loading-games-info-comp]]
   [bbg-reframe.panels.home.components.search-comp :refer [search-games-query-comp search-games-results-comp]]
   [bbg-reframe.panels.home.components.search-comp-events :as search-comp-events]
   [bbg-reframe.panels.home.home-subs :as home-subs]
   [bbg-reframe.subs :as subs]
   [re-frame.core :as re-frame]))

; required for tubax to work
(js/goog.exportSymbol "sax" sax)

(defn home-panel []
  (let [_ (re-frame/dispatch [::network-events/update-result])
        _ @(re-frame/subscribe [::home-subs/available-games])
        players @(re-frame/subscribe [::subs/form :players])
        time @(re-frame/subscribe [::subs/form :time-available])
        has-search-results? @(re-frame/subscribe [::subs/has-search-results?])]
    [:div.flex.flex-col.h-full
     [:h1.text-3xl.font-bold.mb-2.mt-4.ml-2
      "PickToPlay"]
     [loading-games-info-comp]
     [:p.ml-2 players " PLAYERS, TIME: " time " mins"]
     [games-list-comp]
     (when has-search-results?
       [search-games-results-comp
        false
        false
        (fn [id]
          (re-frame/dispatch [::search-comp-events/reset-search])
          (re-frame/dispatch [::events/navigate [:game-view :id id]]))])
     [search-games-query-comp]]))
