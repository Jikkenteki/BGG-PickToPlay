(ns bbg-reframe.panels.home.components.search-comp
  (:require [bbg-reframe.panels.home.components.game-comp-with-info :refer [game-comp-with-info]]
            [bbg-reframe.panels.home.components.search-comp-events :as search-comp-events]
            [bbg-reframe.panels.home.components.search-comp-subs :as subs]
            [re-frame.core :as re-frame]))

(defn search-games-query-comp
  []
  (let [substring (re-frame/subscribe [::subs/game-substring-query])]
    [:input.input-box.min-w-0
     {:type "text"
      :value @substring
      :placeholder "Search games (type at least two characters)"
      :on-change #(re-frame/dispatch [::search-comp-events/search-game-name-with-substring (-> % .-target .-value)])}]))

(defn search-games-results-comp
  []
  (let [search-results @(re-frame/subscribe [::subs/games-search-results])]
    [:div.overflow-auto.shrink-0.min-h-fit.px-2
     {:class "max-h-[40%]"}
     [:ul
      (map
       (fn [game]
         ^{:key (:id game)}
         [game-comp-with-info game])
       search-results)]]))
