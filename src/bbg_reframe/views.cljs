(ns bbg-reframe.views
  (:require
   [re-frame.core :as re-frame]
   [bbg-reframe.subs :as subs]
   [bbg-reframe.components.loading-games-info-comp :refer [loading-games-info-comp]]
   [bbg-reframe.components.games-list-comp :refer [games-list-comp]]
   [bbg-reframe.components.bottom-buttons-bar-comp :refer [bottom-buttons-bar-comp]]
   [bbg-reframe.components.error-box-comp :refer [error-box-comp]]
   ["sax" :as sax]
   [bbg-reframe.components.search-comp :refer [search-comp]]
   [bbg-reframe.components.search-results-comp :refer [search-results-comp]]

   [bbg-reframe.routes :as routes]))

; required for tubax to work
(js/goog.exportSymbol "sax" sax)


(defn home-panel []
  (let [error-msg (re-frame/subscribe [::subs/error-msg])]
    [:div.max-w-xl.mx-auto.flex.flex-col.h-full.bg-stone-800.text-neutral-200

     (when @error-msg (error-box-comp @error-msg))
     [:h1.text-3xl.font-bold.mb-2.px-1
      "HMPWTP "
      [:span.text-sm.font-normal "aka 'Help me pick what to play'"]]
     [loading-games-info-comp]
     [games-list-comp]
     [search-results-comp]
     [search-comp]
     [bottom-buttons-bar-comp]]))

(defmethod routes/panels :home-panel [] [home-panel])

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (routes/panels @active-panel)))
