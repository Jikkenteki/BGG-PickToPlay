(ns bbg-reframe.views
  (:require ["sax" :as sax]
            [bbg-reframe.routes :as routes]
            [bbg-reframe.subs :as subs]
            [bbg-reframe.views.collectionsView.collectionsView :refer [collections-view-panel]]
            [bbg-reframe.views.gameView.gameView :refer [game-view-panel]]
            [bbg-reframe.views.homeView.homeView :refer [home-panel]]
            [bbg-reframe.views.loginView.loginView :refer [login-view-panel]]
            [re-frame.core :as re-frame]))

; required for tubax to work
(js/goog.exportSymbol "sax" sax)

(defmethod routes/panels :home-panel [] [home-panel])
(defmethod routes/panels :game-view-panel [] [game-view-panel])
(defmethod routes/panels :login-view-panel [] [login-view-panel])
(defmethod routes/panels :collections-view-panel [] [collections-view-panel])

;; main
(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    (routes/panels @active-panel)))
