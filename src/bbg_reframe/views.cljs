(ns bbg-reframe.views
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.events :as events]
   [bbg-reframe.network-events :as network-events]
   [bbg-reframe.model.sort-filter :refer [rating-for-number-of-players sorting-fun game->best-rec-not]]
   [goog.string :as gstring]
   [goog.string.format]
   ["sax" :as sax]))


; required for tubax to work
(js/goog.exportSymbol "sax" sax)

(def SHOW_PLAYABILITY true)


(defn game-div
  [game players]
  (let [;_ (println game)
        playability (gstring/format "%.2f" (rating-for-number-of-players
                                            game players))]
    ^{:key (random-uuid)}
    [:p
     (when SHOW_PLAYABILITY (str
                             (:id game) " "
                             "type: " (:type game) " "
                             "v: " (case (game->best-rec-not game players)
                                     0 "best"
                                     1 "rec "
                                     2 "not "
                                     "loading") " - " playability " - " (gstring/format "%.2f" (* playability (:rating game)))  " - "))
     (:name game)]))

(defn result-div
  [result]
  (let [players @(re-frame/subscribe [::subs/form :players])]
    [:div.pl-6.flex-auto.overflow-auto
     (map
      (fn [game]
        (game-div game players))
      result)]))

(defn sort-list []
  (let [options  (map name (keys sorting-fun))
        value (re-frame/subscribe [::subs/form :sort-id])]
    [:div.grid.grid-cols-2.grid-rows-2.gap-3.mb-1
     (doall (for [option options]
              ^{:key option}
              [:div.button.flex {:class (when (= @value option) "active")
                                 :on-click #(re-frame/dispatch [::events/update-form :sort-id option])}
               [:p.m-auto option]]))]))

(defn slider
  [id label min max step]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.flex.justify-between.mb-2.last:mb-1
     [:p.my-auto label]
     [:div.connector-line.grow.my-auto.ml-2]
     [:div.flex {:class "basis-1/2"}
      [:input.range.my-auto.mr-2.grow {:type "range" :min min :max max :step step :value @value :id id
                                       :onChange #(re-frame/dispatch [::events/update-form
                                                                      id
                                                                      (-> % .-target .-value)])}]
      [:span.range-slider-ticket-value @value]]]))

(defn user-panel []
  (let [user (re-frame/subscribe [::subs/user])]
    [:div.flex.items-center.mb-1.h-full
     [:input.input-box.min-w-0.grow.h-full {:type "text"
                                            :id "name"
                                            :value @user
                                            :placeholder "Insert BGG username"
                                            :on-change #(re-frame/dispatch [::events/update-user (-> % .-target .-value)])}]
     [:button.button.min-w-fit.px-2.ml-1 {:disabled (if (= @user "") true false)
                                          :on-click #(re-frame/dispatch [::network-events/fetch-collection])} "Fetch collection"]]))

(defn sliders []
  [:<>
   (slider :take "Take" 1 100 1)
   (slider :higher-than "Rating higher than" 0 10 0.1)
   (slider :players "For number of players" 1 10 1)
   (slider :threshold "Playability threshold" 0 0.95 0.05)
   (slider :time-limit "Time limit" 10 500 10)])

(defn overlay []
  (let [open-tab (re-frame/subscribe [::subs/ui :open-tab])]
    [:div.mb-2
     {:style {:display (when (= @open-tab "")
                         "none")}}
     (case @open-tab
       "sliders" (sliders)
       "username" (user-panel)
       "sort" (sort-list)
       nil)]))

(defn buttons-bar []
  (let [open-tab (re-frame/subscribe [::subs/ui :open-tab])]
    [:div.bottom-overlay-box-shadow.pr-2.p-1.z-10.flex.flex-col
     (overlay)
     [:div.flex.gap-2
      [:div.button.flex-1.flex {:class (when (= @open-tab "sliders") "active")
                                :style {:flex-grow "4"}
                                :on-click  #(re-frame/dispatch [::events/set-open-tab "sliders"])}
       [:i.mx-auto.my-auto {:class "fa-solid fa-sliders fa-xl"}]]
      [:div.button.flex-1.flex.ml-1 {:class (when (= @open-tab "sort") "active")
                                     :style {:flex-grow "4"}
                                     :on-click  #(re-frame/dispatch [::events/set-open-tab "sort"])}
       [:i.mx-auto.my-auto {:class "fa-solid fa-sort fa-xl"}]]
      [:div.button.flex-1.flex.ml-1 {:class (when (= @open-tab "username") "active")
                                     :on-click  #(re-frame/dispatch [::events/set-open-tab "username"])}
       [:i.mx-auto.my-auto {:class "fa-solid fa-user fa-xl"}]]]]))

(defn error-box [error-msg]
  [:div.error-box
   [:p error-msg]])

(defn loading-games-message []
  (let [loading (re-frame/subscribe [::subs/loading])]
    (when @loading
      [:div.flex.ml-3
       [:div.sk-folding-cube
        [:div.sk-cube1.sk-cube]
        [:div.sk-cube2.sk-cube]
        [:div.sk-cube4.sk-cube]
        [:div.sk-cube3.sk-cube]]
       [:p.ml-2.inline.italic "loading games..."]])))

(defn games-list []
  (let [result (re-frame/subscribe [::subs/result])]
    [:div.overflow-auto.grow.px-1
     (result-div @result)]))

(defn main-panel []
  (let [error-msg (re-frame/subscribe [::subs/error-msg])]
    [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200
    ;;  (fn-queue)
     (when @error-msg (error-box @error-msg))
     [:h1.text-3xl.font-bold.mb-2.px-1
      "HMPWTP "
      [:span.text-sm.font-normal "aka 'Help me pick what to play'"]]
     (loading-games-message)
     (games-list)
     (buttons-bar)]))
