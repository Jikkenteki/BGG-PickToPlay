(ns bbg-reframe.localstorage.localstorage
  (:require [bbg-reframe.events :as events]
            [bbg-reframe.localstorage.localstorage-events :as localstorage-events]
            [bbg-reframe.model.localstorage :refer [get-item item-exists?]]
            [clojure.edn :refer [read-string]]
            [re-frame.core :as re-frame]))

(defn init-localstorage
  "Called when app launches"
  []
  (when (and (item-exists? "bgg-user") (item-exists? "bgg-games"))
    (println "Using local storage data")
    (re-frame/dispatch [::events/update-user (get-item "bgg-user")])
    (re-frame/dispatch [::events/update-games (read-string (get-item "bgg-games"))]))
  (when (item-exists? "bgg-ui-settings")
    (re-frame/dispatch [::events/update-ui-settings (read-string (get-item "bgg-ui-settings"))]))
  (when (item-exists? "fb-collections")
    (re-frame/dispatch [::localstorage-events/update-fb-collections (read-string (get-item "fb-collections"))])))