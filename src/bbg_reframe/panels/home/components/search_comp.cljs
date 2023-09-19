(ns bbg-reframe.panels.home.components.search-comp
  (:require [bbg-reframe.panels.home.components.game-comp :refer [game-comp]]
            [bbg-reframe.panels.home.components.game-comp-with-info :refer [game-comp-with-info]]
            [bbg-reframe.panels.home.components.search-comp-events :as search-comp-events]
            [bbg-reframe.panels.home.components.search-comp-subs :as subs]
            [re-frame.core :as re-frame]))

(defn search-games-query-comp
  []
  (let [substring @(re-frame/subscribe [::subs/game-substring-query])]
    [:div.p-2
     [:div.flex.relative
      [:input.input-box.grow
       {:auto-focus true
        :type "text"
        :value substring
        :placeholder "Search games (type at least two characters)"
        :on-change #(re-frame/dispatch [::search-comp-events/search-game-name-with-substring (-> % .-target .-value)])}]
      (when (not (nil? substring))
        [:button.absolute.right-4.bottom-0.top-0.text-slate-400.px-3
         {:class "hover:text-slate-200"
          :on-click #(re-frame/dispatch
                      [::search-comp-events/reset-search])}
         [:i.fa-solid.fa-times.fa-xl]])]]))

(defn search-games-results-comp
  [full-height? with-simple-game-comp? on-click]
  (let [search-results @(re-frame/subscribe [::subs/games-search-results])
        game-comp-to-use (if with-simple-game-comp? game-comp game-comp-with-info)]
    [:div.overflow-auto.min-h-fit.px-2
     {:class (if full-height? "max-h-full" "max-h-[40%] shrink-0")}
     [:ul
      (map
       (fn [game]
         ^{:key (:id game)}
         [game-comp-to-use game on-click])
       search-results)]]))
