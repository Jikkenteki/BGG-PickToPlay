(ns bbg-reframe.game-view.views
  (:require [re-frame.core :as re-frame]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.game-view.subs :as game-view-subs]
            [bbg-reframe.game-view.events :as game-view-events]
            [bbg-reframe.forms.forms :refer [input dropdown-search   db-get-ref]]
            [bbg-reframe.components.nav-bar-comp :refer [naive-nav-bar]]
            [re-frame-firebase-nine.example.forms.forms :refer [db-set-value!]]))

(defn game-view-panel
  []
  (let [route-params @(re-frame/subscribe [::subs/route-params 1])
        game @(re-frame/subscribe [::game-view-subs/game (:id route-params)])
        id (:id game)
        form-path [:game-form]
        ;; available-id @(re-frame/subscribe [::game-view-subs/available id])
        ;; group-with-id @(re-frame/subscribe [::game-view-subs/group-with id])
        ]
    ;; (db-set-value! (into form-path [:available id]) available-id)
    ;; (db-set-value! (into form-path [:group-with id]) group-with-id)
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
     [:a {:href (str "https://boardgamegeek.com/boardgame/" id) :target "_blank"} "Visit game at Boardgamegeek"]

     [:br]
     [:div.border-2.p-2
      [:h2 "Personal Information"]
      [input {:label "Available" :type :checkbox :path (into form-path [:available  (str id)])}]
      [:div
       [:label "Group Item with"]
       (dropdown-search {:db-path (into form-path [:group-with (str id)])
                         :options (vals @(db-get-ref [:games]))
                         :id-keyword :id
                         :display-keyword :name
                         :button-text-empty "Click to select a game"
                         :input-placeholder "Type to find a game"
                         :select-nothing-text "(no game)"
                         :sort? true})]

      [:button.button.min-w-fit.px-2.ml-1
       {:on-click (fn [_]
                     ;;  (db-set-value! [:game-form :add-to-collection (str id)] nil)
                    (re-frame/dispatch
                     [::game-view-events/save-game {:id id
                                                    :available @(db-get-ref [:game-form :available id])
                                                    :group-with @(db-get-ref [:game-form :group-with id])
                                                    ;; :add-to-collection  @(db-get-ref [:game-form :add-to-collection id])
                                                    }]))}"Save"]]]))
