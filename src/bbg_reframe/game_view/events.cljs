(ns bbg-reframe.game-view.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [re-frame-firebase-nine.fb-reframe :as fb-reframe]
            [bbg-reframe.network-events :as events]))

(re-frame/reg-event-fx
 ::save-game
 (fn-traced [{:keys [db]} [_ {:keys [id available group-with comment
                                     ;add-to-collection
                                     ]}]]
            (let [user-id (fb-reframe/get-current-user-uid)]
              (if (nil? user-id)
                {:dispatch [::events/set-error "Login to save extra info about games"]}
                {::fb-reframe/firebase-update {:path ["users" user-id]
                                               :path-data-map {["available" id] (if available available nil)
                                                               ["group-with" id] group-with
                                                               ["comment" id] comment
                                                          ;;  ["collections" add-to-collection "games" id] true
                                                               }
                                               :success #(println "Successfully saved game: " id)}}))))

(comment

  (fb-reframe/get-current-user-uid))