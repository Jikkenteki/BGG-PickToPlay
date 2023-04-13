(ns bbg-reframe.game-view.views
  (:require [bbg-reframe.components.game-css-class :refer [game-icon-players]]
            [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]
            [bbg-reframe.forms.bind :refer [bind-form-to-sub!]]
            [bbg-reframe.forms.forms :refer [db-get-ref dropdown-search
                                             input-element]]
            [bbg-reframe.game-view.events :as game-view-events]
            [bbg-reframe.game-view.subs :as game-view-subs]
            [bbg-reframe.model.plays :refer [last-played number-of-plays
                                             played-rank]]
            [bbg-reframe.model.sort-filter :refer [game->best-rec-not]]
            [bbg-reframe.subs :as subs]
            [re-frame.core :as re-frame]))

(defn personal-info
  [game-id]
  (let [form-path (bind-form-to-sub! [::game-view-subs/game-info game-id] [:game-form game-id])]
    [:div.border-2.p-2
     [:h2 "Personal Information"]
     [:div
      [:label "Comment"] [:br]
      [input-element {:type :textarea :path (into form-path [:comment])
                      :placeholder "Write a comment..."
                      :class "input-box min-w-0 grow h-full"}]]
     [:div
      [:label "Available"]
      [input-element {:type :checkbox :path (into form-path [:available])}]]
     [:div
      [:label "Group Item with"]
      (dropdown-search {:db-path (into form-path [:group-with])
                        :options (vals @(db-get-ref [:games]))
                        :id-keyword :id
                        :display-keyword :name
                        :button-text-empty "Click to select a game"
                        :input-placeholder "Type to find a game"
                        :select-nothing-text "(no game)"
                        :sort? true
                        :button-class "button min-w-fit px-2 ml-1"
                        :input-class "input-box min-w-0 grow h-full"
                        :option-class "option bg-stone-800 text-neutral-200"})]

     [:button.button.min-w-fit.px-2.ml-1
      {:on-click (fn [_]
                     ;;  (db-set-value! [:game-form :add-to-collection (str id)] nil)
                   (re-frame/dispatch
                    [::game-view-events/save-game @(db-get-ref form-path)]))} "Save"]]))

(defn game-view-panel
  []
  (let [route-params @(re-frame/subscribe [::subs/route-params 1])
        games @(re-frame/subscribe [::subs/games])
        game @(re-frame/subscribe [::subs/game (:id route-params)])
        minplayers (:minplayers game)
        maxplayers (:maxplayers game)]
    [:div.max-w-xl.mx-auto.flex-col.h-full.bg-stone-800.text-neutral-100
     [naive-nav-bar]
     [:img.h-50. {:src (:thumbnail game)}]
     [:h1.text-3xl.font-bold.mb-2.px-1 (:name game)]
     [:h1 [:b (:id game)]]
     [:h2 "Rating: " (:rating game)]
     [:h2 "Weight: " (:weight game)]
     [:h2 "Playing time: "  (:playingtime game) " min"]
     [:span "Players: "
      [:span (map
              (fn [p] [:span p (game-icon-players game p)])
              (map
               #(+ % minplayers)
               (range (inc (- maxplayers minplayers)))))]]
     [:h2 "Year: " (:yearpublished game)]
     [:a {:href (str "https://boardgamegeek.com/boardgame/" (:id game)) :target "_blank"} "Visit game at Boardgamegeek"]

     [:br]
     [:div "Plays: " (number-of-plays game)
      " Last played: " (last-played game) " Ranked (plays): " (played-rank games game)]
     [personal-info (:id game)]]))

(comment
  (def game @(db-get-ref [:games "324856"]))
  game
  (def minplayers (:minplayers game))
  (def maxplayers (:maxplayers game))
  (:votes game)
  minplayers
  maxplayers
  (map #(+ % minplayers) (range (inc (- maxplayers minplayers))))
  (map #(game->best-rec-not game %) [2 3 4 5])
  (game->best-rec-not game 2)
  (map #(game->best-rec-not game %) (map #(+ % minplayers) (range (inc (- maxplayers minplayers)))))
  (game-icon-players game 2)
  (map #(game-icon-players game %) (map
                                    #(+ % minplayers)
                                    (range (inc (- maxplayers minplayers)))))



  ;
  )