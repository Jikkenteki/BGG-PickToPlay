(ns bbg-reframe.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]

   [bbg-reframe.events :as events]
   [bbg-reframe.network-events :as network-events]

   [bbg-reframe.views :as views]
   [bbg-reframe.routes :as routes]

   [bbg-reframe.config :as config]
   [bbg-reframe.model.localstorage :refer [item-exists? get-item remove-item!]]
   [clojure.tools.reader.edn :refer [read-string]]
   [re-frame.loggers :refer [console]]

   [bbg-reframe.login-view.events :as login-events]
   [re-frame-firebase-nine.fb-reframe :refer [set-browser-session-persistence fb-reframe-config]]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
    ;; at the beginning so that they are loaded first
  (fb-reframe-config {:temp-path [:firebase-temp-storage]
                      :firebase-config {:apiKey "AIzaSyCLH4BlNSOfTrMlB_90Hsxg5cr3bn3p-7E",
                                        :authDomain "help-me-pick-what-to-play.firebaseapp.com",
                                        :databaseURL "https://help-me-pick-what-to-play-default-rtdb.europe-west1.firebasedatabase.app",
                                        :projectId "help-me-pick-what-to-play",
                                        :storageBucket "help-me-pick-what-to-play.appspot.com",
                                        :messagingSenderId "780911312465",
                                        :appId "1:780911312465:web:bbd9007195b3c630910270"}})
  (set-browser-session-persistence)

  (console :log "Deleting bbg-ui-settings from local storage (Remove me!)")
  (remove-item! "bgg-ui-settings")

  (re-frame/dispatch-sync [::events/initialize-db])
  (routes/start!)

  (re-frame/dispatch [::network-events/cors-check])
  (when (and (item-exists? "bgg-user") (item-exists? "bgg-games"))
    (console :log "Using local storage data")
    (re-frame/dispatch [::events/update-user (get-item "bgg-user")])
    (re-frame/dispatch [::events/update-games (read-string (get-item "bgg-games"))]))
  (when (item-exists? "bgg-ui-settings")
    (re-frame/dispatch [::events/update-ui-settings (read-string (get-item "bgg-ui-settings"))]))
  (re-frame/dispatch [::network-events/update-result])

    ;; poll for a signed-in user for 2 seconds
  ;; auth is not ready
  (re-frame/dispatch [::login-events/poll-user 20000])

  (dev-setup)
  (mount-root))

(comment
  (remove-item! "bgg-ui-settings")
  (init))
