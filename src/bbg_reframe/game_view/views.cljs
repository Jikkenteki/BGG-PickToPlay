(ns bbg-reframe.game-view.views
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.game-view.subs :as game-view-subs]
            [bbg-reframe.game-view.events :as game-view-events]
            [bbg-reframe.forms.forms :refer [dropdown-search   db-get-ref input-element]]
            [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]
            [bbg-reframe.forms.bind :refer [bind-form-to-sub!]]))

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
        game @(re-frame/subscribe [::subs/game (:id route-params)])]
    [:div.max-w-xl.mx-auto.flex-col.h-full.bg-stone-800.text-neutral-100
     [naive-nav-bar]
     [:img.h-50. {:src (:thumbnail game)}]
     [:h1.text-3xl.font-bold.mb-2.px-1 (:name game)]
     [:h1 [:b (:id game)]]
     [:h2 "Rating: " (:rating game)]
     [:h2 "Weight: " (:weight game)]
     [:h2 "Playing time: "  (:playingtime game) " min"]
     [:h2 "Players: "  (:minplayers game) " - " (:maxplayers game)]
     [:h2 "Year: " (:yearpublished game)]
     [:a {:href (str "https://boardgamegeek.com/boardgame/" (:id game)) :target "_blank"} "Visit game at Boardgamegeek"]

     [:br]
     [personal-info (:id game)]]))
