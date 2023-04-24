(ns bbg-reframe.panels.home.components.search-comp
  (:require
   [bbg-reframe.panels.home.components.search-comp-subs :as subs]
   [bbg-reframe.panels.home.components.search-comp-events :as search-comp-events]
   [re-frame.core :as re-frame]))

(defn search-games-query-comp
  []
  (let [substring (re-frame/subscribe [::subs/game-substring-query])]
    [:input.input-box.min-w-0;.grow.h-full
     {:type "text"
      :value @substring
      :placeholder "Search games (type at least two characters)"
      :on-change #(re-frame/dispatch [::search-comp-events/search-game-name-with-substring (-> % .-target .-value)])}]))

(defn search-games-results-comp
  [on-click-handle-game-id]
  (let [search-results @(re-frame/subscribe [::subs/games-search-results])]
    [:div.overflow-auto.grow.px-3
     [:ul
      (map
       (fn [game]
         ^{:key (:id game)}
         [:li
          [:span
          ;;  (:id game) " : "
           [:a {:on-click #(on-click-handle-game-id (:id game))}
            (:name game)]]])
       search-results)]]))
