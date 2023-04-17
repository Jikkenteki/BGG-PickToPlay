(ns bbg-reframe.views.home-view.home-view
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.components.loading-games-info-comp :refer [loading-games-info-comp]]
            [bbg-reframe.components.games-list-comp :refer [games-list-comp]]
            [bbg-reframe.components.bottom-buttons-bar-comp :refer [bottom-buttons-bar-comp]]
            ["sax" :as sax]
            [bbg-reframe.components.search-comp :refer [search-comp]]
            [bbg-reframe.components.search-results-comp :refer [search-results-comp]]))

; required for tubax to work
(js/goog.exportSymbol "sax" sax)

(defn home-panel []
  (let [_ @(re-frame/subscribe [::subs/available-games])]
    [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
     [:h1.text-3xl.font-bold.mb-2.mt-4.px-1
      "PickToPlay"]
     [loading-games-info-comp]
     [games-list-comp]
     [search-results-comp]
     [search-comp]
     [bottom-buttons-bar-comp]]))
